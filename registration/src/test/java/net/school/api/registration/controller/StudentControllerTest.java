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

import net.school.api.registration.entity.Student;
import net.school.api.registration.exception.DuplicateEntityException;
import net.school.api.registration.exception.StudentNotFoundException;
import net.school.api.registration.service.StudentService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = StudentController.class)
public class StudentControllerTest {

	@MockBean
	private StudentService studentService;

	@Autowired
	private MockMvc mockMvc;

	private static ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testGetAllStudents() throws Exception {
		List<Student> students = new ArrayList<>();

		students.add(new Student(1, "Juan", "jg@mail.com"));
		students.add(new Student(2, "john", "john@mail.com"));

		Mockito.when(studentService.getAllStudents()).thenReturn(students);

		mockMvc.perform(get("/api/students")) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].id", is(1)))//
				.andExpect(jsonPath("$[0].name", is("Juan"))) //
				.andExpect(jsonPath("$[0].email", is("jg@mail.com")));
		Mockito.verify(studentService, Mockito.times(1)).getAllStudents();

	}

	@Test
	public void testGetStudent() throws Exception {
		Student student = new Student(1, "john", "john@mail.com");

		Mockito.when(studentService.getStudent(1)).thenReturn(student);

		mockMvc.perform(get("/api/students/1"))//
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.id", is(1))) //
				.andExpect(jsonPath("$.name", is("john"))) //
				.andExpect(jsonPath("$.email", is("john@mail.com")));
		Mockito.verify(studentService, Mockito.times(1)).getStudent(1);
	}

	@Test
	public void testGetStudentNotFound() throws Exception {

		Mockito.when(studentService.getStudent(1)).thenThrow(new StudentNotFoundException(1));

		mockMvc.perform(get("/api/students/1"))//
				.andExpect(status().isNotFound());

		Mockito.verify(studentService, Mockito.times(1)).getStudent(1);

	}

	@Test
	public void testAddStudent() throws Exception {

		Student student = new Student("Juan", "mail@mail.com");
		Student studentFromdb = new Student(1, "Juan", "mail@mail.com");

		Mockito.when(studentService.saveStudent(Mockito.any(Student.class))).thenReturn(studentFromdb);
		String json = mapper.writeValueAsString(student);
		mockMvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(json))//
				.andExpect(status().isCreated()) //
				.andExpect(jsonPath("$.id", is(1))) //
				.andExpect(jsonPath("$.name", is("Juan"))) //
				.andExpect(jsonPath("$.email", is("mail@mail.com")));

	}

	@Test
	public void testAddStudentDuplicate() throws Exception {

		Student student = new Student("Juan", "mail@mail.com");

		Mockito.when(studentService.saveStudent(Mockito.any(Student.class)))
				.thenThrow(new DuplicateEntityException("Duplicate"));
		String json = mapper.writeValueAsString(student);
		mockMvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(json))//
				.andExpect(status().isConflict()).andExpect(jsonPath("$", is("Duplicate")));

	}

	@Test
	public void testUpdateStudent() throws Exception {
		Student student = new Student(1, "John", "new@mail.com");
		Student studentFromdb = new Student(1, "John", "new@mail.com");

		Mockito.when(studentService.updateStudent(Mockito.anyInt(), Mockito.any(Student.class)))
				.thenReturn(studentFromdb);

		String json = mapper.writeValueAsString(student);

		mockMvc.perform(put("/api/students/1").contentType(MediaType.APPLICATION_JSON).content(json))//
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.id", is(1))) //
				.andExpect(jsonPath("$.name", is("John"))) //
				.andExpect(jsonPath("$.email", is("new@mail.com")));
	}

	@Test
	public void testUpdateStudentDuplicate() throws Exception {

		Student student = new Student("Juan", "mail@mail.com");

		Mockito.when(studentService.updateStudent(Mockito.anyInt(), Mockito.any(Student.class)))
				.thenThrow(new DuplicateEntityException("Duplicate"));
		String json = mapper.writeValueAsString(student);
		mockMvc.perform(put("/api/students/1").contentType(MediaType.APPLICATION_JSON).content(json))//
				.andExpect(status().isConflict()).andExpect(jsonPath("$", is("Duplicate")));

	}

	@Test
	public void testUpdateStudentNotFound() throws Exception {
		Student student = new Student("John", "mail@mail.com");
		Mockito.when(studentService.updateStudent(Mockito.anyInt(), Mockito.any(Student.class)))
				.thenThrow(new StudentNotFoundException(1));

		String json = mapper.writeValueAsString(student);

		mockMvc.perform(put("/api/students/1").contentType(MediaType.APPLICATION_JSON).content(json))//
				.andExpect(status().isNotFound());
		Mockito.verify(studentService, times(1)).updateStudent(Mockito.anyInt(), Mockito.any(Student.class));
	}

	@Test
	public void testDeleteStudent() throws Exception {
		Mockito.doNothing().when(studentService).deleteStudent(1);
		mockMvc.perform(delete("/api/students/1")).andExpect(status().isOk());

		Mockito.verify(studentService, times(1)).deleteStudent(1);
	}

	@Test
	public void testDeleteStudentNotFound() throws Exception {
		Mockito.doThrow(new StudentNotFoundException(1)).when(studentService).deleteStudent(1);

		mockMvc.perform(delete("/api/students/1")).andExpect(status().isNotFound());

		Mockito.verify(studentService, times(1)).deleteStudent(1);
	}

}
