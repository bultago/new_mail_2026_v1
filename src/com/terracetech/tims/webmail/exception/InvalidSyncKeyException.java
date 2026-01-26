package com.terracetech.tims.webmail.exception;

public class InvalidSyncKeyException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidSyncKeyException() {
	}

	public InvalidSyncKeyException(String arg0) {
		super(arg0);
	}

	public InvalidSyncKeyException(Throwable arg0) {
		super(arg0);
	}

	public InvalidSyncKeyException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
