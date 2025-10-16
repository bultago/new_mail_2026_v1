package com.terracetech.tims.service.tms.vo;

public class MdnRecallVO {
	
	private long uid = 0;
	private String remoteIp = null;
	private String locale = null;
	private String mid = null;
	private String email = null;
	private String recallEmails = null;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getRemoteIp() {
		return remoteIp;
	}
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRecallEmails() {
		return recallEmails;
	}
	public void setRecallEmails(String recallEmails) {
		this.recallEmails = recallEmails;
	}
	
}
