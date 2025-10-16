package com.terracetech.tims.webmail.webfolder.vo;

public class WebfolderQuotaVO {
	
	private int mailUserSeq = 0;
	
	private int mailDomainSeq = 0;
	
	private String mailUid = null;
	
	private String mailDomain = null;
	
	private String mailHost = null;
	
	private String messageStore = null;
	
	private String webfolderQuota = null;
	
	private String webfolderAddQuota = null;
	
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
	public String getWebfolderQuota() {
		return webfolderQuota;
	}
	public void setWebfolderQuota(String webfolderQuota) {
		this.webfolderQuota = webfolderQuota;
	}
	public String getWebfolderAddQuota() {
		return webfolderAddQuota;
	}
	public void setWebfolderAddQuota(String webfolderAddQuota) {
		this.webfolderAddQuota = webfolderAddQuota;
	}
	
}
