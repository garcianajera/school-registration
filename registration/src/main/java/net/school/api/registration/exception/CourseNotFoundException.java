package net.school.api.registration.exception;

public class CourseNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5798089052898293652L;

	public CourseNotFoundException(Integer id) {
		super("Could not find course with id: " + id);
	}

}
