package net.school.api.registration.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import net.school.api.registration.entity.Course;
import net.school.api.registration.entity.CourseRegistration;
import net.school.api.registration.entity.Student;
import net.school.api.registration.exception.CourseNotFoundException;
import net.school.api.registration.repository.CourseRepository;
import net.school.api.registration.repository.StudentRepository;
import net.school.api.registration.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	private final StudentRepository studentRepository;

	private final CourseRepository courseRepository;

	public ReportServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository) {
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
	}

	@Override
	public List<Student> getAllStudentsByCourseId(Integer courseId) {
		return courseRepository.findById(courseId).map(c -> {
			return c.getStudents().stream().map(CourseRegistration::getStudent).collect(Collectors.toList());
		}).orElseThrow(() -> new CourseNotFoundException(courseId));
	}

	@Override
	public List<Course> getAllCoursesByStudentId(Integer studentId) {
		return studentRepository.findById(studentId).map(s -> {
			return s.getCourses().stream().map(CourseRegistration::getCourse).collect(Collectors.toList());
		}).orElseThrow(() -> new CourseNotFoundException(studentId));
	}

	@Override
	public List<Student> getStudentsWithoutCourses() {
		return studentRepository.findByCoursesIsEmpty();
	}

	@Override
	public List<Course> getCoursesWithoutStudents() {
		return courseRepository.findByStudentsIsEmpty();
	}

}
