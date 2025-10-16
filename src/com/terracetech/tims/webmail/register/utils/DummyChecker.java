package com.terracetech.tims.webmail.register.utils;

public class DummyChecker implements ICheckSsn {

	public String checkSsn(String domain, String ssn, String userName) {
		return SUCCESS;
	}

}
