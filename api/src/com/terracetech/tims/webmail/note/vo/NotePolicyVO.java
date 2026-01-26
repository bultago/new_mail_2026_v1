package com.terracetech.tims.webmail.note.vo;

public class NotePolicyVO {
	
	private int mailUserSeq = 0;

	private String policyType = null;

	public int getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
}
