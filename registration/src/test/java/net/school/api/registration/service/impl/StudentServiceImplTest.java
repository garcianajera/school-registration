package net.school.api.registration.service.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import net.school.api.registration.entity.Student;
import net.school.api.registration.exception.DuplicateEntityException;
import net.school.api.registration.exception.StudentNotFoundException;
import net.school.api.registration.repository.StudentRepository;
import net.school.api.registration.service.StudentService;

public class StudentServiceImplTest {

	@Test
	public void testGetAllStudents() {

		StudentRepository repository = Mockito.mock(StudentRepository.class);
		StudentService studentService = new StudentServiceImpl(repository);

		List<Student> students = new ArrayList<>();

		students.add(new Student(1, "Juan", "jg@mail.com"));
		students.add(new Student(2, "john", "john@mail.com"));

		Mockito.when(repository.findAll()).thenReturn(students);
		List<Student> studentsFromService = studentService.getAllStudents();

		assertNotNull(studentsFromService);
		assertEquals(2, studentsFromService.size());
		assertEquals(1, studentsFromService.get(0).getId());
		assertEquals("Juan", studentsFromService.get(0).getName());
	}

	@Test
	public void testGetStudent() {
		StudentRepository repository = Mockito.mock(StudentRepository.class);
		StudentService studentService = new StudentServiceImpl(repository);
		Optional<Student> expectedStudent = Optional.of(new Student(1, "juan", "mail@mail.com"));
		Mockito.when(repository.findById(1)).thenReturn(expectedStudent);

		Student student = studentService.getStudent(1);

		assertNotNull(student);
		assertEquals(expectedStudent.get().getId(), student.getId());
		assertEquals(expectedStudent.get().getName(), student.getName());
		assertEquals(expectedStudent.get().getEmail(), student.getEmail());
	}

	@Test
	public void testGetStudentNotFound() {

		StudentRepository repository = Mockito.mock(StudentRepository.class);
		StudentService studentService = new StudentServiceImpl(repository);
		Mockito.when(repository.findById(1)).thenThrow(new StudentNotFoundException(1));

		Exception exception = assertThrows(StudentNotFoundException.class, () -> {
			studentService.getStudent(1);
		});

		assertTrue(exception.getMessage().contains("Could not find student with id: 1"));
	}

	@Test
	public void testSaveStudent() {

		StudentRepository repository = Mockito.mock(StudentRepository.class);
		StudentService studentService = new StudentServiceImpl(repository);

		Student student = new Student(1, "Juan", "mail@mail.com");

		Mockito.when(repository.save(Mockito.any(Student.class))).thenReturn(student);

		Student studentFromService = studentService.saveStudent(student);

		assertNotNull(studentFromService);
		assertEquals(student.getId(), studentFromService.getId());
		assertEquals(student.getName(), studentFromService.getName());
		assertEquals(student.getEmail(), studentFromService.getEmail());
	}

	@Test
	void testSaveStudentDuplicate() {
		StudentRepository repository = Mockito.mock(StudentRepository.class);
		StudentService studentService = new StudentServiceImpl(repository);
		String name = "Juan";
		String email = "mail@mail.com";
		Mockito.when(repository.findByNameAndEmail(name, email)).thenReturn(Optional.of(new Student(2, name, email)));

		Exception exception = assertThrows(DuplicateEntityException.class, () -> {
			studentService.saveStudent(new Student(name, email));
		});

		assertEquals("Student Juan (mail@mail.com) already exists.", exception.getMessage());
	}

	@Test
	public void testUpdateStudent() {
		StudentRepository repository = Mockito.mock(StudentRepository.class);
		StudentService studentService = new StudentServiceImpl(repository);

		Student student = new Student(1, "John", "john@mail.com");

		Mockito.when(repository.findById(1)).thenReturn(Optional.of(student));
		Mockito.when(repository.save(Mockito.any(Student.class))).thenReturn(student);

		Student studentFromService = studentService.updateStudent(1, student);

		assertNotNull(studentFromService);
		assertEquals(student.getId(), studentFromService.getId());
		assertEquals(student.getName(), studentFromService.getName());
		assertEquals(student.getEmail(), studentFromService.getEmail());
	}

	@Test
	public void testUpdateStudentNotFound() {
		StudentRepository repository = Mockito.mock(StudentRepository.class);
		StudentService studentService = new StudentServiceImpl(repository);
		Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

		Exception exception = assertThrows(StudentNotFoundException.class, () -> {
			studentService.updateStudent(1, new Student("John", "john@mail.com"));
		});

		assertTrue(exception.getMessage().contains("Could not find student with id: 1"));
	}

	@Test
	void testUpdateStudentDuplicate() {
		StudentRepository repository = Mockito.mock(StudentRepository.class);
		StudentService studentService = new StudentServiceImpl(repository);
		int id = 1;
		String name = "Juan";
		String email = "mail@mail.com";
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(new Student(id, "John", email)));
		Mockito.when(repository.findByNameAndEmail(name, email)).thenReturn(Optional.of(new Student(2, name, email)));

		Exception exception = assertThrows(DuplicateEntityException.class, () -> {
			studentService.updateStudent(id, new Student(name, email));
		});

		assertEquals("Student Juan (mail@mail.com) already exists.", exception.getMessage());
	}

	@Test
	public void testDeleteStudent() {
		StudentRepository repository = Mockito.mock(StudentRepository.class);
		StudentService studentService = new StudentServiceImpl(repository);

		Mockito.doNothing().when(repository).deleteById(1);

		studentService.deleteStudent(1);

		Mockito.verify(repository, times(1)).deleteById(1);
	}

}
