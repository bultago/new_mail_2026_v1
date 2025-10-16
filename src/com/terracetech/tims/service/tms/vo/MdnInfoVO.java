package com.terracetech.tims.service.tms.vo;

public class MdnInfoVO {
	private long uid = 0;
	private String subject = null;
	private String to = null;
	private String date = null;
	private String code = null;
	private String rdate = null;
	private int readCnt = 0;
	private	int mdnCnt = 0;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRdate() {
		return rdate;
	}
	public void setRdate(String rdate) {
		this.rdate = rdate;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getReadCnt() {
		return readCnt;
	}
	public void setReadCnt(int readCnt) {
		this.readCnt = readCnt;
	}	
	public int getMdnCnt() {
		return mdnCnt;
	}
	public void setMdnCnt(int mdnCnt) {
		this.mdnCnt = mdnCnt;
	}
	

	
}
