package com.terracetech.tims.webmail.exception;

public class ClientAbortException extends Exception {

	private static final long serialVersionUID = 1L;

	public ClientAbortException() {
	}

	public ClientAbortException(String arg0) {
		super(arg0);
	}

	public ClientAbortException(Throwable arg0) {
		super(arg0);
	}

	public ClientAbortException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
