package com.terracetech.tims.webmail.home.vo;

public class MailHomeLayoutVO {

	private int layoutSeq;
	
	private String layoutName;
	
	private String layoutUse;
	
	private int portletCount;
	
	public Integer getLayoutSeq() {
		return layoutSeq;
	}

	public void setLayoutSeq(int layoutSeq) {
		this.layoutSeq = layoutSeq;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public String getLayoutUse() {
		return layoutUse;
	}

	public void setLayoutUse(String layoutUse) {
		this.layoutUse = layoutUse;
	}

	public int getPortletCount() {
		return portletCount;
	}

	public void setPortletCount(int portletCount) {
		this.portletCount = portletCount;
	}
	
}