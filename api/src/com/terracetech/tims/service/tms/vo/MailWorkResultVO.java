package com.terracetech.tims.service.tms.vo;

public class MailWorkResultVO {
	
	private String workType = null;
	private boolean errorOccur = false;
	private String errorMessage = null;
	
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	public boolean isErrorOccur() {
		return errorOccur;
	}
	public void setErrorOccur(boolean errorOccur) {
		this.errorOccur = errorOccur;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
	

}
