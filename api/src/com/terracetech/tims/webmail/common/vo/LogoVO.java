package com.terracetech.tims.webmail.common.vo;

public class LogoVO {
	private int mailDomainSeq = 0;
	
	private String logoImgName = null;
	
	private byte[] logoImg = null;
	
	private String logoImgUrl = null;
	
	private String title = null;
	
	private String copyright = null;

	public int getMailDomainSeq() {
		return mailDomainSeq;
	}

	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public String getLogoImgName() {
		return logoImgName;
	}

	public void setLogoImgName(String logoImgName) {
		this.logoImgName = logoImgName;
	}

	public byte[] getLogoImg() {
		return logoImg;
	}

	public void setLogoImg(byte[] logoImg) {
		this.logoImg = logoImg;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getLogoImgUrl() {
		return logoImgUrl;
	}

	public void setLogoImgUrl(String logoImgUrl) {
		this.logoImgUrl = logoImgUrl;
	}
	
}
