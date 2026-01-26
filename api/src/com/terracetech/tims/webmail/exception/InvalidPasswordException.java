package com.terracetech.tims.webmail.exception;

public class InvalidPasswordException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidPasswordException() {
	}

	public InvalidPasswordException(String arg0) {
		super(arg0);
	}

	public InvalidPasswordException(Throwable arg0) {
		super(arg0);
	}

	public InvalidPasswordException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
