package net.school.api.registration.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import net.school.api.registration.entity.Course;
import net.school.api.registration.exception.CourseNotFoundException;
import net.school.api.registration.exception.DuplicateEntityException;
import net.school.api.registration.repository.CourseRepository;
import net.school.api.registration.service.CourseService;

class CourseServiceImplTest {

	@Test
	void testGetAllCourses() {
		CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
		CourseService courseService = new CourseServiceImpl(courseRepository);

		List<Course> courses = new ArrayList<>();

		courses.add(new Course(1, "Math"));
		courses.add(new Course(2, "Spanish"));

		Mockito.when(courseRepository.findAll()).thenReturn(courses);
		List<Course> coursesFromService = courseService.getAllCourses();

		assertNotNull(coursesFromService);
		assertEquals(2, coursesFromService.size());
		assertEquals(1, coursesFromService.get(0).getId());
		assertEquals("Math", coursesFromService.get(0).getName());
	}

	@Test
	void testGetCourseNotFound() {
		CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
		CourseService courseService = new CourseServiceImpl(courseRepository);
		int courseId = 1;

		Mockito.when(courseRepository.findById(courseId)).thenThrow(new CourseNotFoundException(courseId));

		Exception exception = assertThrows(CourseNotFoundException.class, () -> {
			courseService.getCourse(courseId);
		});

		assertTrue(exception.getMessage().contains("Could not find course with id: 1"));
	}

	@Test
	void testGetCourse() {
		CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
		CourseService courseService = new CourseServiceImpl(courseRepository);
		int courseId = 1;
		Optional<Course> expectedCourse = Optional.of(new Course(courseId, "Math"));

		Mockito.when(courseRepository.findById(courseId)).thenReturn(expectedCourse);

		Course courseFromService = courseService.getCourse(courseId);

		assertNotNull(courseFromService);
		assertEquals(expectedCourse.get().getId(), courseFromService.getId());
		assertEquals(expectedCourse.get().getName(), courseFromService.getName());
	}

	@Test
	void testSaveCourse() {
		CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
		CourseService courseService = new CourseServiceImpl(courseRepository);
		int courseId = 1;
		Course course = new Course(courseId, "Math");
		Mockito.when(courseRepository.save(course)).thenReturn(course);
		Course courseFromService = courseService.saveCourse(course);

		assertNotNull(courseFromService);
		assertEquals(course.getId(), courseFromService.getId());
		assertEquals(course.getName(), courseFromService.getName());
	}

	@Test
	void testSaveCourseDuplicate() {
		CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
		CourseService courseService = new CourseServiceImpl(courseRepository);
		String name = "Spanish";

		Mockito.when(courseRepository.findByName(name)).thenReturn(Optional.of(new Course(2, name)));

		Exception exception = assertThrows(DuplicateEntityException.class, () -> {
			courseService.saveCourse(new Course(name));
		});

		assertEquals("Course Spanish already exists.", exception.getMessage());
	}

	@Test
	void testUpdateCourse() {
		CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
		CourseService courseService = new CourseServiceImpl(courseRepository);
		int courseId = 1;
		Course course = new Course(courseId, "Math");
		Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		Mockito.when(courseRepository.save(Mockito.any(Course.class))).thenReturn(course);

		Course courseFromService = courseService.updateCourse(1, course);

		assertNotNull(courseFromService);
		assertEquals(course.getId(), courseFromService.getId());
		assertEquals(course.getName(), courseFromService.getName());
	}

	@Test
	void testUpdateCourseNotFound() {
		CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
		CourseService courseService = new CourseServiceImpl(courseRepository);
		int courseId = 1;
		Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
		Exception exception = assertThrows(CourseNotFoundException.class, () -> {
			courseService.updateCourse(courseId, new Course("Spanish"));
		});

		assertTrue(exception.getMessage().contains("Could not find course with id: 1"));
	}

	@Test
	void testUpdateCourseDuplicate() {
		CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
		CourseService courseService = new CourseServiceImpl(courseRepository);
		int courseId = 1;
		String name = "Spanish";

		Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.of(new Course(1, "Math")));
		Mockito.when(courseRepository.findByName(name)).thenReturn(Optional.of(new Course(2, name)));

		Exception exception = assertThrows(DuplicateEntityException.class, () -> {
			courseService.updateCourse(courseId, new Course(name));
		});

		assertEquals("Course Spanish already exists.", exception.getMessage());
	}

	@Test
	void testDeleteCourse() {
		CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
		CourseService courseService = new CourseServiceImpl(courseRepository);
		Mockito.doNothing().when(courseRepository).deleteById(1);

		courseService.deleteCourse(1);

		Mockito.verify(courseRepository, times(1)).deleteById(1);
	}

}
