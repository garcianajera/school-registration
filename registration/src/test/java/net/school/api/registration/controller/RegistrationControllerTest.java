package net.school.api.registration.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.school.api.registration.entity.Course;
import net.school.api.registration.entity.CourseRegistration;
import net.school.api.registration.entity.CourseRegistrationKey;
import net.school.api.registration.entity.Student;
import net.school.api.registration.exception.RegistrationServiceException;
import net.school.api.registration.service.RegistrationService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RegistrationController.class)
class RegistrationControllerTest {

	@MockBean
	private RegistrationService registrationService;

	@Autowired
	private MockMvc mockMvc;

	private static ObjectMapper mapper = new ObjectMapper();

	@Test
	void testRegisterCourse() throws Exception {
		Course course = new Course(123, "Math");
		Student student = new Student(456, "John", "mail@mail.com");

		CourseRegistrationKey courseRegistrationKey = new CourseRegistrationKey(student.getId(), course.getId());
		CourseRegistration courseRegistration = new CourseRegistration(courseRegistrationKey);
		courseRegistration.setCourse(course);
		courseRegistration.setStudent(student);

		Mockito.when(registrationService.addStudentToCourse(Mockito.any(CourseRegistrationKey.class)))
				.thenReturn(courseRegistration);

		String json = mapper.writeValueAsString(courseRegistrationKey);

		mockMvc.perform(post("/api/registration").contentType(MediaType.APPLICATION_JSON).content(json))//
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.id.studentId", is(456))) //
				.andExpect(jsonPath("$.student.name", is("John"))) //
				.andExpect(jsonPath("$.student.email", is("mail@mail.com"))) //
				.andExpect(jsonPath("$.course.name", is("Math"))) //
				.andExpect(jsonPath("$.id.courseId", is(123)));
	}

	@Test
	void testRegisterCourseMaxStudsentsFail() throws Exception {
		Course course = new Course(123, "Math");
		Student student = new Student(456, "John", "mail@mail.com");

		CourseRegistrationKey courseRegistrationKey = new CourseRegistrationKey(student.getId(), course.getId());

		Mockito.when(registrationService.addStudentToCourse(Mockito.any(CourseRegistrationKey.class)))
				.thenThrow(new RegistrationServiceException("Max Students Fail"));

		String json = mapper.writeValueAsString(courseRegistrationKey);

		mockMvc.perform(post("/api/registration").contentType(MediaType.APPLICATION_JSON).content(json))//
				.andExpect(status().isConflict()) //
				.andExpect(jsonPath("$", is("Max Students Fail")));
	}

}
