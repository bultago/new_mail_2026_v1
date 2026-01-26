package com.terracetech.tims.service;

public interface IErrorCode {

	public final String SUCCESS = "success";
	
	public final static String ERR_NOT_EXIST_USER = "ERROR_006";
	public final static String ERR_INVALID_DATA = "ERROR_005";
	public final static String ERR_PERMISSION = "ERROR_004";
	public final static String ERR_PROTOCOL_PARSING = "ERROR_003";
	public final static String ERR_NOT_EXIST_DOMAIN = "ERROR_002";
	public final static String ERR_USER_ID = "ERROR_001";
	public final static String ERR_UNKNOWN = "ERROR_000";

}
