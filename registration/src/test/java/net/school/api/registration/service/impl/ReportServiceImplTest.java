package net.school.api.registration.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import net.school.api.registration.entity.Course;
import net.school.api.registration.entity.CourseRegistration;
import net.school.api.registration.entity.CourseRegistrationKey;
import net.school.api.registration.entity.Student;
import net.school.api.registration.repository.CourseRepository;
import net.school.api.registration.repository.StudentRepository;
import net.school.api.registration.service.ReportService;

class ReportServiceImplTest {

	@Test
	void testGetAllStudentsByCourseId() {
		CourseRepository repository = Mockito.mock(CourseRepository.class);
		ReportService reportService = new ReportServiceImpl(null, repository);

		Integer courseId = 1;

		Course course = new Course(courseId, "Math");
		Set<CourseRegistration> courseRegistrations = new HashSet<>();
		Integer studentId = 568;
		CourseRegistration courseRegistration = new CourseRegistration(new CourseRegistrationKey(studentId, courseId));
		courseRegistration.setStudent(new Student(studentId, "John", "mail@mail.com"));
		courseRegistrations.add(courseRegistration);

		course.setStudents(courseRegistrations);
		Mockito.when(repository.findById(courseId)).thenReturn(Optional.of(course));

		List<Student> students = reportService.getAllStudentsByCourseId(courseId);

		assertNotNull(students);
		assertEquals(1, students.size());
		assertEquals(studentId, students.get(0).getId());
		assertEquals("John", students.get(0).getName());
	}

	@Test
	void testGetAllCoursesByStudentId() {
		StudentRepository repository = Mockito.mock(StudentRepository.class);
		ReportService reportService = new ReportServiceImpl(repository, null);

		Integer studentId = 1;
		Student student = new Student(studentId, "John", "mail@mail.com");
		Set<CourseRegistration> courseRegistrations = new HashSet<>();
		int courseId = 123;
		CourseRegistration courseRegistration = new CourseRegistration(new CourseRegistrationKey(studentId, courseId));
		courseRegistration.setCourse(new Course(courseId, "Math"));

		courseRegistrations.add(courseRegistration);

		student.setCourses(courseRegistrations);

		Mockito.when(repository.findById(studentId)).thenReturn(Optional.of(student));

		List<Course> courses = reportService.getAllCoursesByStudentId(studentId);

		assertNotNull(courses);
		assertEquals(1, courses.size());
		assertEquals(courseId, courses.get(0).getId());
		assertEquals("Math", courses.get(0).getName());

	}

	@Test
	void testGetStudentsWithoutCourses() {
		StudentRepository repository = Mockito.mock(StudentRepository.class);
		ReportService reportService = new ReportServiceImpl(repository, null);

		List<Student> students = new ArrayList<>();
		students.add(new Student(1, "John", "email@mail.com"));

		Mockito.when(repository.findByCoursesIsEmpty()).thenReturn(students);

		List<Student> studentsService = reportService.getStudentsWithoutCourses();

		assertNotNull(studentsService);
		assertEquals(1, studentsService.size());
		assertEquals(1, studentsService.get(0).getId());
		assertEquals("John", studentsService.get(0).getName());
	}

	@Test
	void testGetCoursesWithoutStudents() {
		CourseRepository repository = Mockito.mock(CourseRepository.class);
		ReportService reportService = new ReportServiceImpl(null, repository);

		List<Course> courses = new ArrayList<>();
		courses.add(new Course(1, "Math"));
		Mockito.when(repository.findByStudentsIsEmpty()).thenReturn(courses);

		List<Course> coursesService = reportService.getCoursesWithoutStudents();

		assertNotNull(coursesService);
		assertEquals(1, coursesService.size());
		assertEquals(1, coursesService.get(0).getId());
		assertEquals("Math", coursesService.get(0).getName());
	}

}
