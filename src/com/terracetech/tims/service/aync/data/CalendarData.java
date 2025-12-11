package com.terracetech.tims.service.aync.data;

import java.text.ParseException;

import com.terracetech.tims.webmail.util.DateUtil;
import com.terracetech.tims.webmail.util.StringUtils;


public class CalendarData implements ISyncData {
	
	private String clientId = null;

	private int schedulerId = 0;
	private int mailUserSeq = 0;
	private String startDate = null;
	private String endDate = null;
	private String title = null;
	private String location = null;
	private String content = null;
	private String allDay = null;
	private String holiday = null;
	private String lunar = null;
	private String repeatFlag = null;
	private String repeatTerm = null;
	private String repeatEndDate = null;
	private String createTime = null;
	private String modifyTime = null;
	private String checkShare = null;
	private String shareName = null;
	private String shareValue = null;
	private String shareColor = null;
	private String outlookSync = null;
	private String ignoreTime = null;
	private String userName = null;
	
	private String checkAsset = null;
	private String assetReserveValue = null;
	private String contect = null;
	
	private int shareSeq = 0;
	private int planSize = 0;
	private int timeSize = 0;
	
	private int drowStartDate = 0;
	private int drowEndDate = 0;
	private int drowStartTime = 0;
	private int drowEndTime = 0;
	
	public int getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(int schedulerId) {
		this.schedulerId = schedulerId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
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

	public String getLunar() {
		return lunar;
	}

	public void setLunar(String lunar) {
		this.lunar = lunar;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getCheckShare() {
		return checkShare;
	}

	public void setCheckShare(String checkShare) {
		this.checkShare = checkShare;
	}

	public String getShareName() {
		return shareName;
	}

	public void setShareName(String shareName) {
		this.shareName = shareName;
	}

	public String getShareValue() {
		return shareValue;
	}

	public void setShareValue(String shareValue) {
		this.shareValue = shareValue;
	}

	public String getShareColor() {
		return shareColor;
	}

	public void setShareColor(String shareColor) {
		this.shareColor = shareColor;
	}

	public String getOutlookSync() {
		return outlookSync;
	}

	public void setOutlookSync(String outlookSync) {
		this.outlookSync = outlookSync;
	}

	public String getIgnoreTime() {
		return ignoreTime;
	}

	public void setIgnoreTime(String ignoreTime) {
		this.ignoreTime = ignoreTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCheckAsset() {
		return checkAsset;
	}

	public void setCheckAsset(String checkAsset) {
		this.checkAsset = checkAsset;
	}

	public String getAssetReserveValue() {
		return assetReserveValue;
	}

	public void setAssetReserveValue(String assetReserveValue) {
		this.assetReserveValue = assetReserveValue;
	}

	public String getContect() {
		return contect;
	}

	public void setContect(String contect) {
		this.contect = contect;
	}

	public int getShareSeq() {
		return shareSeq;
	}

	public void setShareSeq(int shareSeq) {
		this.shareSeq = shareSeq;
	}

	public int getPlanSize() {
		return planSize;
	}

	public void setPlanSize(int planSize) {
		this.planSize = planSize;
	}

	public int getTimeSize() {
		return timeSize;
	}

	public void setTimeSize(int timeSize) {
		this.timeSize = timeSize;
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

	public int getDrowStartTime() {
		return drowStartTime;
	}

	public void setDrowStartTime(int drowStartTime) {
		this.drowStartTime = drowStartTime;
	}

	public int getDrowEndTime() {
		return drowEndTime;
	}

	public void setDrowEndTime(int drowEndTime) {
		this.drowEndTime = drowEndTime;
	}
	
	private String recurrenceType = null;
	
	private String recurrenceInterval = null;
	
	private String dayofWeek = null;
	
	private String dayOfMonth = null;
	
	private String weekOfMonth = null;
	
	private String monthOfYear = null;
	
	public String getRecurrenceType() {
		return recurrenceType;
	}

	/**
	 * <pre>
	 * 0 : Recurs daily
	 * 1 : Recurs weekly
	 * 2 : Recurs monthly
	 * 3 : Recurs monthly on the nth day
	 * 5 : Recurs yearly
	 * 6 : Recurs yearly on the nth day
	 * </pre>
	 */
	public void setRecurrenceType(String recurrenceType) {
		this.recurrenceType = recurrenceType;
	}
	
	public String getRecurrenceInterval() {
		return recurrenceInterval;
	}

	public void setRecurrenceInterval(String recurrenceInterval) {
		this.recurrenceInterval = recurrenceInterval;
	}
	
	public String getDayofWeek() {
		return dayofWeek;
	}

	public void setDayofWeek(String dayofWeek) {
		this.dayofWeek = dayofWeek;
	}
	

	public String getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	
	public String getWeekOfMonth() {
		return weekOfMonth;
	}

	public void setWeekOfMonth(String weekOfMonth) {
		this.weekOfMonth = weekOfMonth;
	}
	
	public String getMonthOfYear() {
		return monthOfYear;
	}

	public void setMonthOfYear(String monthOfYear) {
		this.monthOfYear = monthOfYear;
	}

	public void calculateRepeatTeram(){
		if(!"on".equals(getRepeatFlag())){
			setRepeatTerm("");
			return;
		}
		
		String repeatTerm = getRecurrenceType();
		String interval = getRecurrenceInterval();
		
		if("0".equals(repeatTerm)){
			//010103
			setRepeatTerm("01010" + interval);
		}else if("1".equals(repeatTerm)){
			//020603
			if(interval.length()==1)
				interval = "0" + interval;
			setRepeatTerm("02" + interval + DateUtil.convertDayofWeekForTms(getDayofWeek()));
		}else if("2".equals(repeatTerm)){
			/*
			 * Recurs monthly 030203
			 */
			if(interval.length()==1)
				interval = "0" + interval;
			String dayOfMonth = getDayOfMonth();
			if(dayOfMonth.length()==1)
				dayOfMonth = "0" + dayOfMonth;
			
			setRepeatTerm("03" + interval + dayOfMonth);
		}else if("3".equals(repeatTerm)){
			/*
			 * 03010203 -> 1개월마다 둘째주 화요일
			 */
			if(interval.length()==1)
				interval = "0" + interval;
			String week = getWeekOfMonth();
			if(week.length()==1)
				week = "0" + week;
			
			setRepeatTerm("03" + interval + week + DateUtil.convertDayofWeekForTms(getDayofWeek()));
		}else if("5".equals(repeatTerm)){
			/*
			 *040501 
			 */
			String month = getMonthOfYear();
			
			if(month.length()==1)
				month = "0" + month;
			
			String dayOfMonth = getDayOfMonth();
			if(dayOfMonth.length()==1)
				dayOfMonth = "0" + dayOfMonth;
			
			//매년 반복은 하루만 있어야하는데, 모바일기기에서 2틀에 거쳐서 데이타가 온다.
			setEndDate(getStartDate());
		
			setRepeatTerm("04" + month+ dayOfMonth);
		}
		
	}

	public void setDataValue(String key, String value){
		
		if ("ServerId".equalsIgnoreCase(key)){
			setSchedulerId(Integer.parseInt(value));
		}else if ("ClientId".equalsIgnoreCase(key)){
			setClientId(value);
		}else if ("StartTime".equalsIgnoreCase(key)){
			//20100621T070203Z
			try {
				setStartDate(DateUtil.getTmsFormat(value, "yyyyMMddHH00"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else if ("EndTime".equalsIgnoreCase(key)){
			try {
				setEndDate(DateUtil.getTmsFormat(value, "yyyyMMddHH00"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else if ("Subject".equalsIgnoreCase(key)){
			setTitle(value);
		}else if ("Location".equalsIgnoreCase(key)){
			setLocation(value);
		}else if ("LastName".equalsIgnoreCase(key)){
			setContent(value);
		}else if ("AllDayEvent".equalsIgnoreCase(key)){
			if("1".equals(value)){
				setAllDay("on");	
			}
		}else if ("Recurrence".equalsIgnoreCase(key)){
			setRepeatFlag("on");
		}else if ("Recurrence_Type".equalsIgnoreCase(key)){
			setRecurrenceType(value);
		}else if ("Recurrence_Interval".equalsIgnoreCase(key)){
			setRecurrenceInterval(value);
		}else if ("Recurrence_DayOfWeek".equalsIgnoreCase(key)){
			setDayofWeek(value);
		}else if ("Recurrence_DayOfMonth".equalsIgnoreCase(key)){
			setDayOfMonth(value);
		}else if ("Recurrence_WeekOfMonth".equalsIgnoreCase(key)){
			setWeekOfMonth(value);
		}else if ("Recurrence_MonthOfYear".equalsIgnoreCase(key)){
			setMonthOfYear(value);
		}else if ("Recurrence_Until".equalsIgnoreCase(key)){
			try {
				setRepeatEndDate(DateUtil.getTmsFormat(value, "yyyyMMdd"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
