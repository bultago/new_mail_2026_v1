
package com.terracetech.tims.service.tms.vo;

public class CalendarInfoVO {
	
	private int schedulerId = 0;
	
	private int mailUserSeq = 0;
	
	private int drowStartDate = 0;
	
	private int drowEndDate = 0;
	
	private String date = null;
	
	private String drowStartTime = null;
	
	private String drowEndTime = null;
	
	private String startDate = null;
	
	private String endDate = null;
	
	private String title = null;
	
	private String location = null;
	
	private String content = null;
	
	private String allDay = null;
	
	private String holiday = null;
	
	private String repeatFlag = null;
	
	private String repeatTerm = null;
	
	private String repeatEndDate = null;

	private CalendarShareVO share = null;
	
	private CalendarAssetVO[] assetList = null;
	
	private boolean isModify = true;
	
	private String outlookSync = null;
	
	public int getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(int schedulerId) {
		this.schedulerId = schedulerId;
	}

	public int getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public int getDrowStartDate() {
		return drowStartDate;
	}

	public void setDrowStartDate(int drowStartDate) {
		this.drowStartDate = drowStartDate;
	}

	public int getDrowEndDate() {
		return drowEndDate;
	}

	public void setDrowEndDate(int drowEndDate) {
		this.drowEndDate = drowEndDate;
	}

	public String getDrowStartTime() {
		return drowStartTime;
	}

	public void setDrowStartTime(String drowStartTime) {
		this.drowStartTime = drowStartTime;
	}

	public String getDrowEndTime() {
		return drowEndTime;
	}

	public void setDrowEndTime(String drowEndTime) {
		this.drowEndTime = drowEndTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAllDay() {
		return allDay;
	}

	public void setAllDay(String allDay) {
		this.allDay = allDay;
	}

	public String getHoliday() {
		return holiday;
	}

	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	public String getRepeatFlag() {
		return repeatFlag;
	}

	public void setRepeatFlag(String repeatFlag) {
		this.repeatFlag = repeatFlag;
	}

	public String getRepeatTerm() {
		return repeatTerm;
	}

	public void setRepeatTerm(String repeatTerm) {
		this.repeatTerm = repeatTerm;
	}

	public String getRepeatEndDate() {
		return repeatEndDate;
	}

	public void setRepeatEndDate(String repeatEndDate) {
		this.repeatEndDate = repeatEndDate;
	}

	public CalendarShareVO getShare() {
		return share;
	}

	public void setShare(CalendarShareVO share) {
		this.share = share;
	}

	public CalendarAssetVO[] getAssetList() {
		return assetList;
	}

	public void setAssetList(CalendarAssetVO[] assetList) {
		this.assetList = assetList;
	}

	public boolean isModify() {
		return isModify;
	}

	public void setModify(boolean isModify) {
		this.isModify = isModify;
	}

	public String getOutlookSync() {
		return outlookSync;
	}

	public void setOutlookSync(String outlookSync) {
		this.outlookSync = outlookSync;
	}
	
}
