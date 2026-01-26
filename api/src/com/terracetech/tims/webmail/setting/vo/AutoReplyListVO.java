package com.terracetech.tims.webmail.setting.vo;

public class AutoReplyListVO {
	
	private int userSeq;
	
	private String replyAddress = null;

	public int getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}

	public String getReplyAddress() {
		return replyAddress;
	}

	public void setReplyAddress(String replyAddress) {
		this.replyAddress = replyAddress;
	}
	
}
