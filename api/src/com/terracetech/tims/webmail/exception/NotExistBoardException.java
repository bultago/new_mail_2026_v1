package com.terracetech.tims.webmail.exception;

public class NotExistBoardException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotExistBoardException() {
	}

	public NotExistBoardException(String arg0) {
		super(arg0);
	}

	public NotExistBoardException(Throwable arg0) {
		super(arg0);
	}

	public NotExistBoardException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
