package com.terracetech.tims.service.tms.vo;

public class CalendarVO {

	private String date = null;
	
	private String type = null;
	
	private String today = null;
	
	private String thisday = null;
	
	private String preday = null;
	
	private String nextday = null;
	
	private String[] dateList = null;
	
	private CalendarInfoVO[] calendarInfos = null;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}

	public String getThisday() {
		return thisday;
	}

	public void setThisday(String thisday) {
		this.thisday = thisday;
	}

	public String getPreday() {
		return preday;
	}

	public void setPreday(String preday) {
		this.preday = preday;
	}

	public String getNextday() {
		return nextday;
	}

	public void setNextday(String nextday) {
		this.nextday = nextday;
	}

	public String[] getDateList() {
		return dateList;
	}

	public void setDateList(String[] dateList) {
		this.dateList = dateList;
	}

	public CalendarInfoVO[] getCalendarInfos() {
		return calendarInfos;
	}

	public void setCalendarInfos(CalendarInfoVO[] calendarInfos) {
		this.calendarInfos = calendarInfos;
	}

}
