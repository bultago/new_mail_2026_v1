package com.terracetech.tims.webmail.exception;

public class ProtocolConnectException extends RuntimeException {

	private static final long serialVersionUID = 20090401L;

	public ProtocolConnectException() {
	}

	public ProtocolConnectException(String arg0) {
		super(arg0);
	}

	public ProtocolConnectException(Throwable arg0) {
		super(arg0);
	}

	public ProtocolConnectException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
