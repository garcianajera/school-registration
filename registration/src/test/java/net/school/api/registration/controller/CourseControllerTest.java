package net.school.api.registration.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.school.api.registration.entity.Course;
import net.school.api.registration.exception.CourseNotFoundException;
import net.school.api.registration.exception.DuplicateEntityException;
import net.school.api.registration.service.CourseService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CourseController.class)
public class CourseControllerTest {

	@MockBean
	private CourseService courseService;

	@Autowired
	private MockMvc mockMvc;

	private static ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testGetAllCourses() throws Exception {
		List<Course> courses = new ArrayList<>();
		courses.add(new Course(1, "Math"));
		courses.add(new Course(2, "Spanish"));

		Mockito.when(courseService.getAllCourses()).thenReturn(courses);

		mockMvc.perform(get("/api/courses")) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].id", is(1)))//
				.andExpect(jsonPath("$[0].name", is("Math")));

		Mockito.verify(courseService, Mockito.times(1)).getAllCourses();
	}

	@Test
	public void testGetCourse() throws Exception {
		Course course = new Course(1, "Math");
		Mockito.when(courseService.getCourse(1)).thenReturn(course);

		mockMvc.perform(get("/api/courses/1")) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.id", is(1))) //
				.andExpect(jsonPath("$.name", is("Math")));

		Mockito.verify(courseService, Mockito.times(1)).getCourse(1);
	}

	@Test
	public void testGetCourseNotFound() throws Exception {
		Mockito.when(courseService.getCourse(1)).thenThrow(new CourseNotFoundException(1));

		mockMvc.perform(get("/api/courses/1")) //
				.andExpect(status().isNotFound());

		Mockito.verify(courseService, Mockito.times(1)).getCourse(1);
	}

	@Test
	public void testAddCourse() throws Exception {
		Course course = new Course("Math");
		Course courseFrommService = new Course(1, "Math");
		Mockito.when(courseService.saveCourse(Mockito.any(Course.class))).thenReturn(courseFrommService);

		String json = mapper.writeValueAsString(course);

		mockMvc.perform(post("/api/courses").contentType(MediaType.APPLICATION_JSON).content(json))//
				.andExpect(status().isCreated()) //
				.andExpect(jsonPath("$.id", is(1))) //
				.andExpect(jsonPath("$.name", is("Math")));

	}

	@Test
	public void testAddCourseDuplicate() throws Exception {
		Course course = new Course("Math");
		Mockito.when(courseService.saveCourse(Mockito.any(Course.class)))
				.thenThrow(new DuplicateEntityException("Duplicate"));

		String json = mapper.writeValueAsString(course);

		mockMvc.perform(post("/api/courses").contentType(MediaType.APPLICATION_JSON).content(json))//
				.andExpect(status().isConflict()).andExpect(jsonPath("$", is("Duplicate")));

	}

	@Test
	public void testUpdateCourse() throws Exception {
		Course course = new Course("Math");
		Course courseFromService = new Course(1, "Math");
		Mockito.when(courseService.updateCourse(Mockito.anyInt(), Mockito.any(Course.class)))
				.thenReturn(courseFromService);

		String json = mapper.writeValueAsString(course);

		mockMvc.perform(put("/api/courses/1").contentType(MediaType.APPLICATION_JSON).content(json))//
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.id", is(1))) //
				.andExpect(jsonPath("$.name", is("Math")));
	}

	@Test
	public void testUpdateCourseNotFound() throws Exception {
		Course course = new Course("Math");

		Mockito.when(courseService.updateCourse(Mockito.anyInt(), Mockito.any(Course.class)))
				.thenThrow(new CourseNotFoundException(1));

		String json = mapper.writeValueAsString(course);

		mockMvc.perform(put("/api/courses/1").contentType(MediaType.APPLICATION_JSON).content(json))//
				.andExpect(status().isNotFound());
		Mockito.verify(courseService, times(1)).updateCourse(Mockito.anyInt(), Mockito.any(Course.class));
	}

	@Test
	public void testUpdateCourseDuplicate() throws Exception {
		Course course = new Course("Math");
		Mockito.when(courseService.updateCourse(Mockito.anyInt(), Mockito.any(Course.class)))
				.thenThrow(new DuplicateEntityException("Duplicate"));

		String json = mapper.writeValueAsString(course);

		mockMvc.perform(put("/api/courses/1").contentType(MediaType.APPLICATION_JSON).content(json))//
				.andExpect(status().isConflict()).andExpect(jsonPath("$", is("Duplicate")));

	}

	@Test
	public void testDeleteCourse() throws Exception {
		Mockito.doNothing().when(courseService).deleteCourse(1);
		mockMvc.perform(delete("/api/courses/1")).andExpect(status().isOk());

		Mockito.verify(courseService, times(1)).deleteCourse(1);
	}

	@Test
	public void testDeleteCourseNotFound() throws Exception {
		Mockito.doThrow(new CourseNotFoundException(1)).when(courseService).deleteCourse(1);

		mockMvc.perform(delete("/api/courses/1")).andExpect(status().isNotFound());

		Mockito.verify(courseService, times(1)).deleteCourse(1);
	}

}
