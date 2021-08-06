package net.school.api.registration.exception;

public class DuplicateEntityException extends RuntimeException {

	private static final long serialVersionUID = 4402038647113261392L;

	public DuplicateEntityException(String message) {
		super(message);
	}

}
