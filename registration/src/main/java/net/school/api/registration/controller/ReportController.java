package net.school.api.registration.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.school.api.registration.entity.Course;
import net.school.api.registration.entity.Student;
import net.school.api.registration.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

	private final ReportService reportService;

	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}

	@GetMapping("/students/{id}/courses")
	List<Course> getAllCourses(@PathVariable Integer id) {
		return reportService.getAllCoursesByStudentId(id);
	}

	@GetMapping("/courses/{id}/students")
	List<Student> getAllStudents(@PathVariable Integer id) {
		return reportService.getAllStudentsByCourseId(id);
	}

	@GetMapping("/courses/students/empty")
	List<Course> getCoursesWithoutStudents() {
		return reportService.getCoursesWithoutStudents();
	}

	@GetMapping("/students/courses/empty")
	List<Student> getStudentsWithoutCourses() {
		return reportService.getStudentsWithoutCourses();
	}
}
