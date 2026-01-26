package com.terracetech.tims.webmail.mailuser.exception;

public class MailUserAuthenticateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8487682762751200369L;
	
	public MailUserAuthenticateException() {
	}

	public MailUserAuthenticateException(String message) {
		super(message);
	}

	public MailUserAuthenticateException(Throwable cause) {
		super(cause);
	}

	public MailUserAuthenticateException(String message, Throwable cause) {
		super(message, cause);
	}

}
