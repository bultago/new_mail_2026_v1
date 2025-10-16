package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;

public class SyncMailESBVO implements Serializable{

	private static final long serialVersionUID = 20091102L;

	/**
	 * Message UID
	 */
	private String msgUID;
	

	/**
	 * MTR-Key
	 */
	private String mtrKey;
	
	/**
	 * 발신자 email 주소
	 */
	private String senderEmail;
	
	/**
	 * 메일 개봉시간
	 */
	private String readTime;
	
	/**
	 * 메일 존재여부
	 */
	private boolean isExist;

	public String getMsgUID() {
		return msgUID;
	}

	public void setMsgUID(String msgUID) {
		this.msgUID = msgUID;
	}

	public String getMtrKey() {
		return mtrKey;
	}

	public void setMtrKey(String mtrKey) {
		this.mtrKey = mtrKey;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getReadTime() {
		return readTime;
	}

	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}

	public boolean isExist() {
		return isExist;
	}

	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}
}
