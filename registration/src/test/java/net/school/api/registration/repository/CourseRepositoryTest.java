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

class CourseRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private CourseRepository courseRepository;

	@Test
	void testFindByName() {
		Course course = new Course(1, "Math");
		entityManager.persist(course);

		Optional<Course> courseFromDb = courseRepository.findByName("Math");
		assertTrue(courseFromDb.isPresent());
		assertEquals("Math", courseFromDb.get().getName());
	}

	@Test
	void testFindByStudentsIsEmpty() {
		Course course = new Course(1, "No Students");
		entityManager.persist(course);

		Student student = new Student(123, "Juan", "mail@mail.com");
		entityManager.persist(student);

		Course courseWithStudents = new Course(2, "Spanish");
		entityManager.persist(courseWithStudents);

		CourseRegistration courseRegistration = new CourseRegistration(
				new CourseRegistrationKey(student.getId(), courseWithStudents.getId()));

		courseRegistration.setCourse(courseWithStudents);
		courseRegistration.setStudent(student);
		entityManager.persist(courseRegistration);

		List<Course> coursesFromDb = courseRepository.findByStudentsIsEmpty();

		assertNotNull(coursesFromDb);
		assertEquals(1, coursesFromDb.size());
		assertEquals(1, coursesFromDb.get(0).getId());
		assertEquals("No Students", coursesFromDb.get(0).getName());

	}

}
