package com.terracetech.tims.webmail.mail.vo;

public class SharedFolderUserVO {
	int userSeq = 0;
	int folderUid = 0;
	String userId = null;
	String userName = null;
	
	public int getUserSeq() {
		return userSeq;
	}
	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	public int getFolderUid() {
		return folderUid;
	}
	public void setFolderUid(int folderUid) {
		this.folderUid = folderUid;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}	
}
