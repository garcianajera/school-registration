package net.school.api.registration.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import net.school.api.registration.entity.Course;
import net.school.api.registration.entity.CourseRegistration;
import net.school.api.registration.entity.CourseRegistrationKey;
import net.school.api.registration.entity.Student;

@RunWith(SpringRunner.class)
@DirtiesContext
@DataJpaTest
@TestPropertySource(properties = { "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"

})
class CourseRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private CourseRepository courseRepository;

	@Test
	void testFindByName() {
		Course course = new Course(1, "Math");
		entityManager.persist(course);
		entityManager.flush();

		Optional<Course> courseFromDb = courseRepository.findByName("Math");
		assertTrue(courseFromDb.isPresent());
		assertEquals("Math", courseFromDb.get().getName());
	}

	@Test
	void testFindByStudentsIsEmpty() {
		Course course = new Course(1, "No Students");
		entityManager.persist(course);
		entityManager.flush();

		Student student = new Student(123, "Juan", "mail@mail.com");
		entityManager.persist(student);
		entityManager.flush();

		Course courseWithStudents = new Course(2, "Spanish");
		entityManager.persist(courseWithStudents);
		entityManager.flush();

		CourseRegistration courseRegistration = new CourseRegistration(
				new CourseRegistrationKey(student.getId(), courseWithStudents.getId()));

		courseRegistration.setCourse(courseWithStudents);
		courseRegistration.setStudent(student);
		entityManager.persist(courseRegistration);
		entityManager.flush();

		List<Course> coursesFromDb = courseRepository.findByStudentsIsEmpty();

		assertNotNull(coursesFromDb);
		assertEquals(1, coursesFromDb.size());
		assertEquals(1, coursesFromDb.get(0).getId());
		assertEquals("No Students", coursesFromDb.get(0).getName());

	}

}
