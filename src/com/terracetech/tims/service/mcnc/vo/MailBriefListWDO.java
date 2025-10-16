package com.terracetech.tims.service.mcnc.vo;

import java.io.Serializable;

public class MailBriefListWDO implements Serializable{
	
	private static final long serialVersionUID = 4099496766719750016L;
	
	private MailBriefWDO[] mails;
	private String currentPageNo;
	private String lastMailID;		
	
	public MailBriefWDO[] getMails() {
		return mails;
	}
	public void setMails(MailBriefWDO[] mails) {
		this.mails = mails;
	}
	public String getCurrentPageNo() {
		return currentPageNo;
	}
	public void setCurrentPageNo(String currentPageNo) {
		this.currentPageNo = currentPageNo;
	}
	public String getLastMailID() {
		return lastMailID;
	}
	public void setLastMailID(String lastMailID) {
		this.lastMailID = lastMailID;
	}
}
