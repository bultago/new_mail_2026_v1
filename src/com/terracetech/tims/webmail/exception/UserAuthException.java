package com.terracetech.tims.webmail.exception;

public class UserAuthException extends Exception {

	private static final long serialVersionUID = 1L;

	public UserAuthException() {
	}

	public UserAuthException(String message) {
		super(message);
	}

	public UserAuthException(Throwable cause) {
		super(cause);
	}

	public UserAuthException(String message, Throwable cause) {
		super(message, cause);
	}

}
