package com.terracetech.tims.webmail.exception;

public class SaveFailedException extends Exception {

	private static final long serialVersionUID = 20090406L;

	public SaveFailedException() {
	}

	public SaveFailedException(String message) {
		super(message);
	}

	public SaveFailedException(Throwable cause) {
		super(cause);
	}

	public SaveFailedException(String message, Throwable cause) {
		super(message, cause);
	}

}
