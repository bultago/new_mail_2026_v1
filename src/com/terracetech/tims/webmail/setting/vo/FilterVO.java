package com.terracetech.tims.webmail.setting.vo;

public class FilterVO {
	private int userSeq;
	private String apply = "off";
	private FilterCondVO[] conds;
	
	public int getUserSeq() {
		return userSeq;
	}
	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	public String getApply() {
		return apply;
	}
	public void setApply(String apply) {
		this.apply = apply;
	}
	public FilterCondVO[] getConds() {
		return conds;
	}
	public void setConds(FilterCondVO[] conds) {
		this.conds = conds;
	}
}
