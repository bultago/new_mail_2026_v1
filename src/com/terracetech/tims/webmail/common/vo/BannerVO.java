package com.terracetech.tims.webmail.common.vo;

public class BannerVO {
	
	private int mailDomainSeq = 0;
	
	private byte[] bannerHeader = null;
	
	private byte[] bannerTail = null;
	
	private String bannerApply = null;
	
	private String bannerHeaderUrl = null;
	
	private String bannerTailUrl = null;

	public int getMailDomainSeq() {
		return mailDomainSeq;
	}

	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public byte[] getBannerHeader() {
		return bannerHeader;
	}

	public void setBannerHeader(byte[] bannerHeader) {
		this.bannerHeader = bannerHeader;
	}

	public byte[] getBannerTail() {
		return bannerTail;
	}

	public void setBannerTail(byte[] bannerTail) {
		this.bannerTail = bannerTail;
	}

	public String getBannerApply() {
		return bannerApply;
	}

	public void setBannerApply(String bannerApply) {
		this.bannerApply = bannerApply;
	}

	public String getBannerHeaderUrl() {
		return bannerHeaderUrl;
	}

	public void setBannerHeaderUrl(String bannerHeaderUrl) {
		this.bannerHeaderUrl = bannerHeaderUrl;
	}

	public String getBannerTailUrl() {
		return bannerTailUrl;
	}

	public void setBannerTailUrl(String bannerTailUrl) {
		this.bannerTailUrl = bannerTailUrl;
	}
	
	
}
