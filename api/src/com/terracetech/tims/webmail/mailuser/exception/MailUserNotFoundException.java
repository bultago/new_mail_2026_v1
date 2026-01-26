package com.terracetech.tims.webmail.mailuser.exception;

public class MailUserNotFoundException extends Exception {

	public MailUserNotFoundException() {
	}

	public MailUserNotFoundException(String message) {
		super(message);
	}

	public MailUserNotFoundException(Throwable cause) {
		super(cause);
	}

	public MailUserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
