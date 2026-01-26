package com.terracetech.tims.webmail.note.vo;

public class NotePolicyCondVO {

	private int mailUserSeq = 0;
	
	private int condTarget = 0;
	
	private String mailUid = null;
	
	private String userName = null;

	public int getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}

	public int getCondTarget() {
		return condTarget;
	}

	public void setCondTarget(int condTarget) {
		this.condTarget = condTarget;
	}

	public String getMailUid() {
		return mailUid;
	}

	public void setMailUid(String mailUid) {
		this.mailUid = mailUid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
