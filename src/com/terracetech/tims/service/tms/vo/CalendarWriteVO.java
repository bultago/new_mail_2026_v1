package com.terracetech.tims.service.tms.vo;

public class CalendarWriteVO {

	private String type = null;
	private String current = null;
	private String thisdate = null;
	
	private String sdate = null;
	private String edate = null;
	private String stime = null;
	private String etime = null;
	
	private CalendarShareVO[] shareList = null;
	private CalendarAssetVO[] assetList = null;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCurrent() {
		return current;
	}
	public void setCurrent(String current) {
		this.current = current;
	}
	public String getThisdate() {
		return thisdate;
	}
	public void setThisdate(String thisdate) {
		this.thisdate = thisdate;
	}
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getEtime() {
		return etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}
	public CalendarShareVO[] getShareList() {
		return shareList;
	}
	public void setShareList(CalendarShareVO[] shareList) {
		this.shareList = shareList;
	}
	public CalendarAssetVO[] getAssetList() {
		return assetList;
	}
	public void setAssetList(CalendarAssetVO[] assetList) {
		this.assetList = assetList;
	}
}
