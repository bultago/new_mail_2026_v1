package com.terracetech.tims.webmail.bbs.vo;

public class BoardQuotaVO {
	
	private int mailUserSeq = 0;
	
	private int mailDomainSeq = 0;
	
	private String mailUid = null;
	
	private String mailDomain = null;
	
	private String mailHost = null;
	
	private String messageStore = null;
	
	private String mailQuota = null;
	
	private String mailAddQuota = null;
	
	private String quotaWarningMode = null;
	
	private String quotaWarningRatio = null;
	
	private String quotaOverlookRatio = null;

	public int getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}

	public int getMailDomainSeq() {
		return mailDomainSeq;
	}

	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public String getMailUid() {
		return mailUid;
	}

	public void setMailUid(String mailUid) {
		this.mailUid = mailUid;
	}

	public String getMailDomain() {
		return mailDomain;
	}

	public void setMailDomain(String mailDomain) {
		this.mailDomain = mailDomain;
	}

	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public String getMessageStore() {
		return messageStore;
	}

	public void setMessageStore(String messageStore) {
		this.messageStore = messageStore;
	}

	public String getMailQuota() {
		return mailQuota;
	}

	public void setMailQuota(String mailQuota) {
		this.mailQuota = mailQuota;
	}

	public String getMailAddQuota() {
		return mailAddQuota;
	}

	public void setMailAddQuota(String mailAddQuota) {
		this.mailAddQuota = mailAddQuota;
	}

	public String getQuotaWarningMode() {
		return quotaWarningMode;
	}

	public void setQuotaWarningMode(String quotaWarningMode) {
		this.quotaWarningMode = quotaWarningMode;
	}

	public String getQuotaWarningRatio() {
		return quotaWarningRatio;
	}

	public void setQuotaWarningRatio(String quotaWarningRatio) {
		this.quotaWarningRatio = quotaWarningRatio;
	}

	public String getQuotaOverlookRatio() {
		return quotaOverlookRatio;
	}

	public void setQuotaOverlookRatio(String quotaOverlookRatio) {
		this.quotaOverlookRatio = quotaOverlookRatio;
	}
	
}
