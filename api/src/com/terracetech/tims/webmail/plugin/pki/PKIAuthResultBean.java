package com.terracetech.tims.webmail.plugin.pki;

public class PKIAuthResultBean {
	private boolean isAuth = false;
	private boolean isError = false;
	private String userDn = null;
	private String vid = null;
	private int errorCode = 0;	
	private String errorMsg = null;
	public boolean isAuth() {
		return isAuth;
	}
	public void setAuth(boolean isAuth) {
		this.isAuth = isAuth;
	}
	public boolean isError() {
		return isError;
	}
	public void setError(boolean isError) {
		this.isError = isError;
	}	
	public String getUserDn() {
		return userDn;
	}
	public void setUserDn(String userDn) {
		this.userDn = userDn;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return "(ERROR CODE:"+errorCode+")(ERROR MSG:"+errorMsg+")";
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
	
}
