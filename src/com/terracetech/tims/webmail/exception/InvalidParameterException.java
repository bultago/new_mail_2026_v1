package com.terracetech.tims.webmail.exception;

public class InvalidParameterException extends RuntimeException {

	private static final long serialVersionUID = 20090302L;

	public InvalidParameterException() {
		super();
	}

	public InvalidParameterException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidParameterException(String arg0) {
		super(arg0);
	}

	public InvalidParameterException(Throwable arg0) {
		super(arg0);
	}

}
