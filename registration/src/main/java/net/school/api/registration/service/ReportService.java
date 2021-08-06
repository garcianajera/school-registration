package net.school.api.registration.service;

import java.util.List;

import net.school.api.registration.entity.Course;
import net.school.api.registration.entity.Student;

public interface ReportService {
	
	List<Student> getAllStudentsByCourseId(Integer courseId);

	List<Course> getAllCoursesByStudentId(Integer studentId);

	List<Student> getStudentsWithoutCourses();

	List<Course> getCoursesWithoutStudents();

}
