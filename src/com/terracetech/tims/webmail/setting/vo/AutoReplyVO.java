package com.terracetech.tims.webmail.setting.vo;

public class AutoReplyVO {
	
	private int userSeq;
	
	private String autoReplyMode = null;
	
	private String autoReplyInclude = null;
	
	private String autoReplyStartTime = null;
	
	private String autoReplyEndTime = null;
	
	private String autoReplyText = null;

	private String autoReplySubject = null;
	
	private String activeMode = null;

	public int getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}

	public String getAutoReplyMode() {
		return autoReplyMode;
	}

	public void setAutoReplyMode(String autoReplyMode) {
		this.autoReplyMode = autoReplyMode;
	}

	public String getAutoReplyInclude() {
		return autoReplyInclude;
	}

	public void setAutoReplyInclude(String autoReplyInclude) {
		this.autoReplyInclude = autoReplyInclude;
	}

	public String getAutoReplyStartTime() {
		return autoReplyStartTime;
	}

	public void setAutoReplyStartTime(String autoReplyStartTime) {
		this.autoReplyStartTime = autoReplyStartTime;
	}

	public String getAutoReplyEndTime() {
		return autoReplyEndTime;
	}

	public void setAutoReplyEndTime(String autoReplyEndTime) {
		this.autoReplyEndTime = autoReplyEndTime;
	}

	public String getAutoReplyText() {
		return autoReplyText;
	}

	public void setAutoReplyText(String autoReplyText) {
		this.autoReplyText = autoReplyText;
	}

	public String getActiveMode() {
		return activeMode;
	}

	public void setActiveMode(String activeMode) {
		this.activeMode = activeMode;
	}

	public String getAutoReplySubject() {
		return autoReplySubject;
	}

	public void setAutoReplySubject(String autoReplySubject) {
		this.autoReplySubject = autoReplySubject;
	}
}
