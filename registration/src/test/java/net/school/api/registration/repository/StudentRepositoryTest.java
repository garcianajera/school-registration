package net.school.api.registration.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import net.school.api.registration.entity.Course;
import net.school.api.registration.entity.CourseRegistration;
import net.school.api.registration.entity.CourseRegistrationKey;
import net.school.api.registration.entity.Student;

class StudentRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private StudentRepository studentRepository;

	@Test
	void testFindByNameAndEmail() {
		Student student = new Student(1, "John", "mail@mail.com");
		entityManager.persist(student);

		Optional<Student> studentFromDb = studentRepository.findByNameAndEmail("John", "mail@mail.com");
		assertTrue(studentFromDb.isPresent());
		assertEquals("John", studentFromDb.get().getName());
	}

	@Test
	void testFindByCoursesIsEmpty() {
		Student studentNoCourses = new Student(1, "No Courses", "mail@mail.com");
		entityManager.persist(studentNoCourses);

		Student student = new Student(123, "Juan", "mail@mail.com");
		entityManager.persist(student);

		Course course = new Course(2, "Spanish");
		entityManager.persist(course);

		CourseRegistration courseRegistration = new CourseRegistration(
				new CourseRegistrationKey(student.getId(), course.getId()));

		courseRegistration.setCourse(course);
		courseRegistration.setStudent(student);
		entityManager.persist(courseRegistration);

		List<Student> studentsFromDb = studentRepository.findByCoursesIsEmpty();

		assertNotNull(studentsFromDb);
		assertEquals(1, studentsFromDb.size());
		assertEquals(1, studentsFromDb.get(0).getId());
		assertEquals("No Courses", studentsFromDb.get(0).getName());
	}

}
