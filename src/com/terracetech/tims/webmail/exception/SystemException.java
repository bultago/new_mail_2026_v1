package com.terracetech.tims.webmail.exception;

public class SystemException extends RuntimeException {

	private static final long serialVersionUID = 20090319L;

	public SystemException() {
	}

	public SystemException(String arg0) {
		super(arg0);
	}

	public SystemException(Throwable arg0) {
		super(arg0);
	}

	public SystemException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
