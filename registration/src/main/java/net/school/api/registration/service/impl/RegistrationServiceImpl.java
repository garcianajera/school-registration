package net.school.api.registration.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.school.api.registration.entity.Course;
import net.school.api.registration.entity.CourseRegistration;
import net.school.api.registration.entity.CourseRegistrationKey;
import net.school.api.registration.entity.Student;
import net.school.api.registration.exception.RegistrationServiceException;
import net.school.api.registration.repository.CourseRegistrationRepository;
import net.school.api.registration.service.CourseService;
import net.school.api.registration.service.RegistrationService;
import net.school.api.registration.service.StudentService;

@Service
public class RegistrationServiceImpl implements RegistrationService {
	private static final int MAX_NUMBER_OF_STUDENTS = 50;

	private static final int MAX_NUMBER_OF_COURSES = 5;

	private static final Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class);

	private final CourseRegistrationRepository courseRegistrationRepository;

	private final StudentService studentService;

	private final CourseService courseService;

	RegistrationServiceImpl(CourseRegistrationRepository courseRegistrationRepository, StudentService studentService,
			CourseService courseService) {
		this.courseRegistrationRepository = courseRegistrationRepository;
		this.studentService = studentService;
		this.courseService = courseService;
	}

	@Override
	@Transactional
	public CourseRegistration addStudentToCourse(CourseRegistrationKey courseRegistrationKey) {
		Student student = studentService.getStudent(courseRegistrationKey.getStudentId());
		Course course = courseService.getCourse(courseRegistrationKey.getCourseId());

		if (log.isDebugEnabled()) {
			log.debug("Student has: " + student.getCourses().size() + " courses");
			log.debug("Course has: " + course.getStudents() + " students");
		}

		if (student.getCourses() != null && student.getCourses().size() >= MAX_NUMBER_OF_COURSES) {
			throw new RegistrationServiceException(
					"Student cannot have more than " + MAX_NUMBER_OF_COURSES + " courses");
		}

		if (course.getStudents() != null && course.getStudents().size() >= MAX_NUMBER_OF_STUDENTS) {
			throw new RegistrationServiceException(
					"Course cannot have more than " + MAX_NUMBER_OF_STUDENTS + " students");
		}

		if (isStudentEnrolled(student, courseRegistrationKey.getCourseId())) {
			throw new RegistrationServiceException("Student is already enrolled to course");
		}

		CourseRegistration courseRegistration = new CourseRegistration(courseRegistrationKey);
		courseRegistration.setCourse(course);
		courseRegistration.setStudent(student);

		return courseRegistrationRepository.save(courseRegistration);
	}

	boolean isStudentEnrolled(Student student, Integer courseId) {
		return student.getCourses().stream().filter(c -> c.getId().getCourseId() == courseId).findAny().isPresent();
	}

}
