package net.school.api.registration.service.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import net.school.api.registration.entity.Course;
import net.school.api.registration.entity.CourseRegistration;
import net.school.api.registration.entity.CourseRegistrationKey;
import net.school.api.registration.entity.Student;
import net.school.api.registration.exception.RegistrationServiceException;
import net.school.api.registration.repository.CourseRegistrationRepository;
import net.school.api.registration.service.CourseService;
import net.school.api.registration.service.StudentService;

class RegistrationServiceImplTest {

	@Test
	void testAddStudentToCourse() throws Exception {
		CourseRegistrationRepository courseRegistrationRepository = Mockito.mock(CourseRegistrationRepository.class);
		StudentService studentService = Mockito.mock(StudentService.class);
		CourseService courseService = Mockito.mock(CourseService.class);
		Integer courseId = 123;
		Integer studentId = 456;

		Student student = new Student(studentId, "John", "john@email.com");
		student.setCourses(new HashSet<>());
		Course course = new Course(courseId, "Math");
		course.setStudents(new HashSet<>());

		CourseRegistration expected = new CourseRegistration(new CourseRegistrationKey(studentId, courseId));
		expected.setCourse(course);
		expected.setStudent(student);

		Mockito.when(courseService.getCourse(courseId)).thenReturn(course);
		Mockito.when(studentService.getStudent(studentId)).thenReturn(student);
		Mockito.when(courseRegistrationRepository.save(Mockito.any(CourseRegistration.class))).thenReturn(expected);

		RegistrationServiceImpl registrationServiceImpl = new RegistrationServiceImpl(courseRegistrationRepository,
				studentService, courseService);

		CourseRegistrationKey courseRegistrationKey = new CourseRegistrationKey(studentId, courseId);

		CourseRegistration courseRegistration = registrationServiceImpl.addStudentToCourse(courseRegistrationKey);

		assertEquals(expected.getId(), courseRegistration.getId());

	}

	@Test
	void testAddStudentToCourseMaxStudentsFail() throws Exception {
		CourseRegistrationRepository courseRegistrationRepository = Mockito.mock(CourseRegistrationRepository.class);
		StudentService studentService = Mockito.mock(StudentService.class);
		CourseService courseService = Mockito.mock(CourseService.class);
		Integer courseId = 123;
		Integer studentId = 456;

		Student student = new Student(studentId, "John", "john@email.com");
		student.setCourses(new HashSet<>());
		Course course = new Course(courseId, "Math");
		course.setStudents(getCourseRegistrationStub(50));

		CourseRegistration expected = new CourseRegistration(new CourseRegistrationKey(studentId, courseId));
		expected.setCourse(course);
		expected.setStudent(student);

		Mockito.when(courseService.getCourse(courseId)).thenReturn(course);
		Mockito.when(studentService.getStudent(studentId)).thenReturn(student);
		Mockito.when(courseRegistrationRepository.save(Mockito.any(CourseRegistration.class))).thenReturn(expected);

		RegistrationServiceImpl registrationServiceImpl = new RegistrationServiceImpl(courseRegistrationRepository,
				studentService, courseService);

		CourseRegistrationKey courseRegistrationKey = new CourseRegistrationKey(studentId, courseId);

		Exception exception = assertThrows(RegistrationServiceException.class, () -> {
			registrationServiceImpl.addStudentToCourse(courseRegistrationKey);

		});

		assertEquals("Course cannot have more than 50 students", exception.getMessage());
	}

	@Test
	void testAddStudentToCourseMaxCoursesFail() {
		CourseRegistrationRepository courseRegistrationRepository = Mockito.mock(CourseRegistrationRepository.class);
		StudentService studentService = Mockito.mock(StudentService.class);
		CourseService courseService = Mockito.mock(CourseService.class);
		Integer courseId = 123;
		Integer studentId = 456;

		Student student = new Student(studentId, "John", "john@email.com");
		student.setCourses(getCourseRegistrationStub(5));

		Course course = new Course(courseId, "Math");

		CourseRegistration expected = new CourseRegistration(new CourseRegistrationKey(studentId, courseId));
		expected.setCourse(course);
		expected.setStudent(student);

		Mockito.when(courseService.getCourse(courseId)).thenReturn(course);
		Mockito.when(studentService.getStudent(studentId)).thenReturn(student);
		Mockito.when(courseRegistrationRepository.save(Mockito.any(CourseRegistration.class))).thenReturn(expected);

		RegistrationServiceImpl registrationServiceImpl = new RegistrationServiceImpl(courseRegistrationRepository,
				studentService, courseService);

		CourseRegistrationKey courseRegistrationKey = new CourseRegistrationKey(studentId, courseId);

		Exception exception = assertThrows(RegistrationServiceException.class, () -> {
			registrationServiceImpl.addStudentToCourse(courseRegistrationKey);

		});

		assertEquals("Student cannot have more than 5 courses", exception.getMessage());
	}

	@Test
	void testAddStudentToCourseCourseAlreadyEnrolledFail() {
		CourseRegistrationRepository courseRegistrationRepository = Mockito.mock(CourseRegistrationRepository.class);
		StudentService studentService = Mockito.mock(StudentService.class);
		CourseService courseService = Mockito.mock(CourseService.class);
		Integer courseId = 123;
		Integer studentId = 456;

		Student student = new Student(studentId, "John", "john@email.com");
		Course course = new Course(courseId, "Math");

		Set<CourseRegistration> courses = new HashSet<>();
		CourseRegistration courseRegistration = new CourseRegistration(new CourseRegistrationKey(studentId, courseId));
		courseRegistration.setCourse(course);
		courseRegistration.setStudent(student);
		courses.add(courseRegistration);

		student.setCourses(courses);

		CourseRegistration expected = new CourseRegistration(new CourseRegistrationKey(studentId, courseId));
		expected.setCourse(course);
		expected.setStudent(student);

		Mockito.when(courseService.getCourse(courseId)).thenReturn(course);
		Mockito.when(studentService.getStudent(studentId)).thenReturn(student);
		
		RegistrationServiceImpl registrationServiceImpl = new RegistrationServiceImpl(courseRegistrationRepository,
				studentService, courseService);

		CourseRegistrationKey courseRegistrationKey = new CourseRegistrationKey(studentId, courseId);

		Exception exception = assertThrows(RegistrationServiceException.class, () -> {
			registrationServiceImpl.addStudentToCourse(courseRegistrationKey);

		});

		assertEquals("Student is already enrolled to course", exception.getMessage());
	}

	@Test
	void testIsStudentEnrolled() {
		RegistrationServiceImpl registrationServiceImpl = new RegistrationServiceImpl(null, null, null);
		Integer courseId = 789;
		Course course = new Course(courseId, "Math");

		Student studentWithCourse = new Student(123, "John", "mail@mail.com");
		CourseRegistration courseRegistration = new CourseRegistration(
				new CourseRegistrationKey(studentWithCourse.getId(), course.getId()));
		courseRegistration.setCourse(course);
		courseRegistration.setStudent(studentWithCourse);
		
		Set<CourseRegistration> courses = new HashSet<>();
		courses.add(courseRegistration);
		studentWithCourse.setCourses(courses);

		Student studentWithoutCourse = new Student(567, "Paul", "paul@mail.com");
		studentWithoutCourse.setCourses(new HashSet<>());

		assertTrue(registrationServiceImpl.isStudentEnrolled(studentWithCourse, courseId));
		assertFalse(registrationServiceImpl.isStudentEnrolled(studentWithoutCourse, courseId));
	}

	private Set<CourseRegistration> getCourseRegistrationStub(int numberOfObjects) {
		Set<CourseRegistration> students = new HashSet<>();
		for (int i = 1; i <= numberOfObjects; i++) {
			students.add(new CourseRegistration(new CourseRegistrationKey(i, i + numberOfObjects)));
		}
		return students;
	}

}
