package net.school.api.registration.service;

import java.util.List;

import net.school.api.registration.entity.Course;

public interface CourseService {

	List<Course> getAllCourses();

	Course getCourse(Integer id);

	Course saveCourse(Course course);

	Course updateCourse(Integer id, Course course);

	void deleteCourse(Integer id);

}
