package com.terracetech.tims.service.tms.vo;

public class CalendarAssetVO {

	private int categorySeq = 0;
	private int assetSeq = 0;
	private String categoryName = null;
	private String assetName = null;
	private String contect = null;
	
	public int getCategorySeq() {
		return categorySeq;
	}
	public void setCategorySeq(int categorySeq) {
		this.categorySeq = categorySeq;
	}
	public int getAssetSeq() {
		return assetSeq;
	}
	public void setAssetSeq(int assetSeq) {
		this.assetSeq = assetSeq;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getContect() {
		return contect;
	}
	public void setContect(String contect) {
		this.contect = contect;
	}
}
