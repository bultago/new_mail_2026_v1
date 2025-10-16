package com.terracetech.tims.service.mcnc.vo;

import java.io.Serializable;

public class MailBriefWDO implements Serializable{

	private static final long serialVersionUID = -4953786419080987347L;
	
	private int mailSize;
	private ContactWDO sender;
	private int importance;
	private String title;
	private int mailType;	 
	private boolean hasAttachments;
	private String UID;
	private String receivedDate;
	private String receivedTime;
	private int security;		
	private String displayName;	
	private PayloadWDO[] payload;
	
	public int getMailSize() {
		return mailSize;
	}
	public void setMailSize(int mailSize) {
		this.mailSize = mailSize;
	}
	public int getImportance() {
		return importance;
	}
	public void setImportance(int importance) {
		this.importance = importance;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getMailType() {
		return mailType;
	}
	public void setMailType(int mailType) {
		this.mailType = mailType;
	}
	public boolean getHasAttachments() {
		return hasAttachments;
	}
	public void setHasAttachments(boolean hasAttachments) {
		this.hasAttachments = hasAttachments;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public String getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getReceivedTime() {
		return receivedTime;
	}
	public void setReceivedTime(String receivedTime) {
		this.receivedTime = receivedTime;
	}
	public int getSecurity() {
		return security;
	}
	public void setSecurity(int security) {
		this.security = security;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public ContactWDO getSender() {
		return sender;
	}
	public void setSender(ContactWDO sender) {
		this.sender = sender;
	}
	public PayloadWDO[] getPayload() {
		return payload;
	}
	public void setPayload(PayloadWDO[] payload) {
		this.payload = payload;
	}
	
}
