package net.school.api.registration.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import net.school.api.registration.entity.Course;
import net.school.api.registration.exception.CourseNotFoundException;
import net.school.api.registration.exception.DuplicateEntityException;
import net.school.api.registration.repository.CourseRepository;
import net.school.api.registration.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

	private final CourseRepository courseRepository;

	CourseServiceImpl(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	@Override
	public List<Course> getAllCourses() {
		return courseRepository.findAll();
	}

	@Override
	public Course getCourse(Integer id) {
		return courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
	}

	@Override
	public Course saveCourse(Course course) {
		if (isDuplicateCourse(course)) {
			throw new DuplicateEntityException("Course " + course.getName() + " already exists.");
		}
		return courseRepository.save(course);
	}

	@Override
	public Course updateCourse(Integer id, Course course) {

		return courseRepository.findById(id).map(courseFromDb -> {
			if (isDuplicateCourse(course)) {
				throw new DuplicateEntityException("Course " + course.getName() + " already exists.");
			}
			courseFromDb.setName(course.getName());
			return courseRepository.save(courseFromDb);
		}).orElseThrow(() -> new CourseNotFoundException(id));
	}

	@Override
	public void deleteCourse(Integer id) {
		courseRepository.deleteById(id);
	}

	boolean isDuplicateCourse(Course course) {
		return courseRepository.findByName(course.getName()).isPresent();
	}

}
