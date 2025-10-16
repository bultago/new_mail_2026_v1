package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;

public class HeaderHelperCSVO implements Serializable{

	private static final long serialVersionUID = 20091102L;
	
	/**
	 *  원하는 제목 string (UTF-8)
	 */
	private String subject = null;
	
	/**
	 * 메시지 종류
	 *  personal or official
	 */
	private String msgType = null;

	/**
	 * 메일메시지 본문
	 */
	private boolean bHtmlContentCheckiFileCount;
	
	/**
	 * 첨부파일갯수
	 */
	private int iFileCount;
	
	/**
	 *  GMT+9
	 */
	private String timeZone = null;

	/**
	 * Summer time 적용여부
	 */
	private boolean isDst = false;
	
	/**
	 * 발신자 메일주소
	 */
	private String senderMailAddr = null;
	
	/**
	 * INBOX, Sent, Draft, Trash, Spam, MyFolder, MyFolder/…
	 */
	private String folderName = null;

	/**
	 * 발신자 첨부파일 분리여부
	 */
	private boolean bExternal = false;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public boolean isbHtmlContentCheckiFileCount() {
		return bHtmlContentCheckiFileCount;
	}

	public void setbHtmlContentCheckiFileCount(boolean bHtmlContentCheckiFileCount) {
		this.bHtmlContentCheckiFileCount = bHtmlContentCheckiFileCount;
	}

	public int getiFileCount() {
		return iFileCount;
	}

	public void setiFileCount(int iFileCount) {
		this.iFileCount = iFileCount;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public boolean isDst() {
		return isDst;
	}

	public void setDst(boolean isDst) {
		this.isDst = isDst;
	}

	public String getSenderMailAddr() {
		return senderMailAddr;
	}

	public void setSenderMailAddr(String senderMailAddr) {
		this.senderMailAddr = senderMailAddr;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public boolean isbExternal() {
		return bExternal;
	}

	public void setbExternal(boolean bExternal) {
		this.bExternal = bExternal;
	}
	
}
