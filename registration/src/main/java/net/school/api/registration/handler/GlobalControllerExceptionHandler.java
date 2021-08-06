package net.school.api.registration.handler;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import net.school.api.registration.exception.CourseNotFoundException;
import net.school.api.registration.exception.DuplicateEntityException;
import net.school.api.registration.exception.RegistrationServiceException;
import net.school.api.registration.exception.StudentNotFoundException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

	@ExceptionHandler(StudentNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<String> handleStudentNotFound(RuntimeException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CourseNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<String> handleCourseNotFound(RuntimeException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> constraintViolationException(ConstraintViolationException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RegistrationServiceException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<String> handleRegistrationServiceException(RegistrationServiceException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(DuplicateEntityException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<String> handleDuplicateEntityException(DuplicateEntityException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
	}

}
