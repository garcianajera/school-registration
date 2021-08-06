package net.school.api.registration.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import net.school.api.registration.entity.Course;
import net.school.api.registration.entity.Student;
import net.school.api.registration.service.ReportService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ReportController.class)
class ReportControllerTest {

	@MockBean
	private ReportService reportService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetAllCoursesByStudentId() throws Exception {
		List<Course> courses = new ArrayList<>();
		courses.add(new Course(1, "Math"));
		courses.add(new Course(2, "Spanish"));

		Mockito.when(reportService.getAllCoursesByStudentId(1)).thenReturn(courses);

		mockMvc.perform(get("/api/reports/students/1/courses")) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].id", is(1)))//
				.andExpect(jsonPath("$[0].name", is("Math")));

		Mockito.verify(reportService, Mockito.times(1)).getAllCoursesByStudentId(1);
	}

	@Test
	void testGetAllStudentsByCourseId() throws Exception {
		List<Student> students = new ArrayList<>();
		students.add(new Student(1, "John", "mail@mail.com"));
		students.add(new Student(2, "Paul", "mail2@mail.com"));

		Mockito.when(reportService.getAllStudentsByCourseId(1)).thenReturn(students);

		mockMvc.perform(get("/api/reports/courses/1/students")) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].id", is(1)))//
				.andExpect(jsonPath("$[0].name", is("John")));

		Mockito.verify(reportService, Mockito.times(1)).getAllStudentsByCourseId(1);
	}

	@Test
	void testGetCoursesWithoutStudents() throws Exception {
		List<Course> courses = new ArrayList<>();
		courses.add(new Course(1, "Math"));
		courses.add(new Course(2, "Spanish"));

		Mockito.when(reportService.getCoursesWithoutStudents()).thenReturn(courses);

		mockMvc.perform(get("/api/reports/courses/students/empty")) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].id", is(1)))//
				.andExpect(jsonPath("$[0].name", is("Math")));

		Mockito.verify(reportService, Mockito.times(1)).getCoursesWithoutStudents();
	}

	@Test
	void testGetStudentsWithoutCourses() throws Exception {
		List<Student> students = new ArrayList<>();
		students.add(new Student(1, "John", "mail@mail.com"));
		students.add(new Student(2, "Paul", "mail2@mail.com"));

		Mockito.when(reportService.getStudentsWithoutCourses()).thenReturn(students);

		mockMvc.perform(get("/api/reports/students/courses/empty")) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].id", is(1)))//
				.andExpect(jsonPath("$[0].name", is("John")));

		Mockito.verify(reportService, Mockito.times(1)).getStudentsWithoutCourses();
	}

}
