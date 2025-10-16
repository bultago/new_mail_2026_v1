package com.terracetech.tims.service.tms.vo;

public class CalendarAssetCondVO {

	private String email = null;
	
	private String startDate = null;
	
	private String endDate = null;
	
	private int schedulerId = 0;
	
	private int[] assetSeqs = null;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(int schedulerId) {
		this.schedulerId = schedulerId;
	}

	public int[] getAssetSeqs() {
		return assetSeqs;
	}

	public void setAssetSeqs(int[] assetSeqs) {
		this.assetSeqs = assetSeqs;
	}
	
}
