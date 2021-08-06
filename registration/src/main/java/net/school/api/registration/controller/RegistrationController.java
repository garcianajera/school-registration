package net.school.api.registration.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.school.api.registration.entity.CourseRegistration;
import net.school.api.registration.entity.CourseRegistrationKey;
import net.school.api.registration.service.RegistrationService;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

	private final RegistrationService registrationService;

	RegistrationController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}

	@PostMapping
	CourseRegistration registerCourse(@RequestBody CourseRegistrationKey courseRegistrationKey) {
		return registrationService.addStudentToCourse(courseRegistrationKey);
	}

}
