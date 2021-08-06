package net.school.api.registration.service;

import net.school.api.registration.entity.CourseRegistration;
import net.school.api.registration.entity.CourseRegistrationKey;

public interface RegistrationService {

	CourseRegistration addStudentToCourse(CourseRegistrationKey courseRegistrationKey);

}
