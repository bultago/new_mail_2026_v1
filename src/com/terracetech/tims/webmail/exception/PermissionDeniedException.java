package com.terracetech.tims.webmail.exception;

public class PermissionDeniedException extends RuntimeException {
	private static final long serialVersionUID = 20090319L;
	
	public PermissionDeniedException() {
	}

	public PermissionDeniedException(String message) {
		super(message);
	}

	public PermissionDeniedException(Throwable cause) {
		super(cause);
	}

	public PermissionDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

}
