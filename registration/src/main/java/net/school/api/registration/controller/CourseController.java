package net.school.api.registration.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.school.api.registration.entity.Course;
import net.school.api.registration.service.CourseService;

@RestController
@RequestMapping("/api/courses")
@Validated
public class CourseController {

	private final CourseService courseService;

	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping
	List<Course> getAllCourses() {
		return courseService.getAllCourses();
	}

	@GetMapping("/{id}")
	Course getCourse(@PathVariable Integer id) {
		return courseService.getCourse(id);
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	Course addCourse(@RequestBody Course course) {
		return courseService.saveCourse(course);
	}

	@PutMapping("/{id}")
	Course updateCourse(@PathVariable Integer id, @RequestBody Course course) {
		return courseService.updateCourse(id, course);
	}

	@DeleteMapping("/{id}")
	void deleteCourse(@PathVariable Integer id) {
		courseService.deleteCourse(id);
	}
}
