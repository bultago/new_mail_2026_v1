package com.terracetech.tims.webmail.exception;

public class UnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public UnauthorizedException() {
	}

	public UnauthorizedException(String arg0) {
		super(arg0);
	}

	public UnauthorizedException(Throwable arg0) {
		super(arg0);
	}

	public UnauthorizedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
