package com.terracetech.tims.webmail.common.vo;

public class VirusCheckVO{
	private boolean isSuccess = true;
	
	private String attachList = null;
	
	private String checkResultMsg = null;
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getAttachList() {
		return attachList;
	}
	public void setAttachList(String attachList) {
		this.attachList = attachList;
	}
	public String getCheckResultMsg() {
		return checkResultMsg;
	}
	public void setCheckResultMsg(String checkResultMsg) {
		this.checkResultMsg = checkResultMsg;
	}	
}