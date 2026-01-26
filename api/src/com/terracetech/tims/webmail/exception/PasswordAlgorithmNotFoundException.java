package com.terracetech.tims.webmail.exception;

public class PasswordAlgorithmNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PasswordAlgorithmNotFoundException() {
	}

	public PasswordAlgorithmNotFoundException(String arg0) {
		super(arg0);
	}

	public PasswordAlgorithmNotFoundException(Throwable arg0) {
		super(arg0);
	}

	public PasswordAlgorithmNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
