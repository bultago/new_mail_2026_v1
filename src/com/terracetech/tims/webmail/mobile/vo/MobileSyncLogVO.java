package com.terracetech.tims.webmail.mobile.vo;

public class MobileSyncLogVO {
	private int logSeq;
	
	private int mailUserSeq;
	
	private String eventType;

	private String target;

	private String targetValue;

	private String updateTime;

	public int getLogSeq() {
		return logSeq;
	}

	public void setLogSeq(int logSeq) {
		this.logSeq = logSeq;
	}

	public int getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}