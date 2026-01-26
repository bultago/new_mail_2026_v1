package com.terracetech.tims.webmail.register.utils;

public interface ICheckSsn {
	
	public String SUCCESS = "001";
	
	public String FAILED = "002";

	public String checkSsn(String domain, String ssn, String userName);
}
