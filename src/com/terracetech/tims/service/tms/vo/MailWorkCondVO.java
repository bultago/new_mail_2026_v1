package com.terracetech.tims.service.tms.vo;

public class MailWorkCondVO {
	
	private String remoteIp = null;
	private String userEmail = null;
	private String locale = null;
	private String workMode = null;
	private String[] uid = null;
	private String[] folderName = null;
	private String targetFolderName = null;	
	private String flagType = null;
	private boolean flagUse = false;
	
	
	public String getRemoteIp() {
		return remoteIp;
	}
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getWorkMode() {
		return workMode;
	}
	public void setWorkMode(String workMode) {
		this.workMode = workMode;
	}
	public String[] getUid() {
		return uid;
	}
	public void setUid(String[] uid) {
		this.uid = uid;
	}
	public String[] getFolderName() {
		return folderName;
	}
	public void setFolderName(String[] folderName) {
		this.folderName = folderName;
	}
	public String getTargetFolderName() {
		return targetFolderName;
	}
	public void setTargetFolderName(String targetFolderName) {
		this.targetFolderName = targetFolderName;
	}
	public String getFlagType() {
		return flagType;
	}
	public void setFlagType(String flagType) {
		this.flagType = flagType;
	}
	public boolean isFlagUse() {
		return flagUse;
	}
	public void setFlagUse(boolean flagUse) {
		this.flagUse = flagUse;
	}
	
	
	
}
