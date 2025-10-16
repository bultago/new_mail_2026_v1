package com.terracetech.tims.service.mcnc.vo;

import java.io.Serializable;

public class MailBoxWDO implements Serializable{

	private static final long serialVersionUID = -7846094469721163275L;
	
	private String userID;	
	private String companyID;
	
	private BoxWDO	box;	
	private String 	boxPath;
	private String[] subFolders;
	private boolean hasUnreadMail;
	private int unreadMailCount = 0;
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getCompanyID() {
		return companyID;
	}
	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}
	public BoxWDO getBox() {
		return box;
	}
	public void setBox(BoxWDO box) {
		this.box = box;
	}
	public boolean isHasUnreadMail() {
		return hasUnreadMail;
	}
	public void setHasUnreadMail(boolean hasUnreadMail) {
		this.hasUnreadMail = hasUnreadMail;
	}
	public int getUnreadMailCount() {
		return unreadMailCount;
	}
	public void setUnreadMailCount(int unreadMailCount) {
		this.unreadMailCount = unreadMailCount;
	}
	public String[] getSubFolders() {
		return subFolders;
	}
	public void setSubFolders(String[] subFolders) {
		this.subFolders = subFolders;
	}
	public String getBoxPath() {
		return boxPath;
	}
	public void setBoxPath(String boxPath) {
		this.boxPath = boxPath;
	}
	
}
