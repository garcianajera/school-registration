package net.school.api.registration.exception;

public class StudentNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5213706438621423854L;

	public StudentNotFoundException(Integer id) {
		super("Could not find student with id: " + id);
	}

}
