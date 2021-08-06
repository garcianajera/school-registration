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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import net.school.api.registration.entity.Course;
import net.school.api.registration.entity.CourseRegistration;
import net.school.api.registration.entity.CourseRegistrationKey;
import net.school.api.registration.entity.Student;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = { "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"

})
class StudentRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private StudentRepository studentRepository;

	@Test
	void testFindByNameAndEmail() {
		Student student = new Student(1, "John", "mail@mail.com");
		entityManager.persist(student);
		entityManager.flush();

		Optional<Student> studentFromDb = studentRepository.findByNameAndEmail("John", "mail@mail.com");
		assertTrue(studentFromDb.isPresent());
		assertEquals("John", studentFromDb.get().getName());
	}

	@Test
	void testFindByCoursesIsEmpty() {
		Student studentNoCourses = new Student(1, "No Courses", "mail@mail.com");
		entityManager.persist(studentNoCourses);
		entityManager.flush();

		Student student = new Student(123, "Juan", "mail@mail.com");
		entityManager.persist(student);
		entityManager.flush();

		Course course = new Course(2, "Spanish");
		entityManager.persist(course);
		entityManager.flush();

		CourseRegistration courseRegistration = new CourseRegistration(
				new CourseRegistrationKey(student.getId(), course.getId()));

		courseRegistration.setCourse(course);
		courseRegistration.setStudent(student);
		entityManager.persist(courseRegistration);
		entityManager.flush();

		List<Student> studentsFromDb = studentRepository.findByCoursesIsEmpty();

		assertNotNull(studentsFromDb);
		assertEquals(1, studentsFromDb.size());
		assertEquals(1, studentsFromDb.get(0).getId());
		assertEquals("No Courses", studentsFromDb.get(0).getName());
	}

}
