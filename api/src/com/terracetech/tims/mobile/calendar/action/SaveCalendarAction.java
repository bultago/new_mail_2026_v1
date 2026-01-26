package com.terracetech.tims.mobile.calendar.action;

import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.CalendarService;
import com.terracetech.tims.service.tms.vo.CalendarSaveCondVO;

public class SaveCalendarAction extends BaseAction {

	private static final long serialVersionUID = 6797734051746983224L;
	
	private int schedulerId = 0;
	private String type = null;
	private String startDate = null;
	private String endDate = null;
	private String allDay = null;
	private String title = null;
	private String location = null;
	private String content = null;
	private String checkShare = null;
	private String shareValue = null;
	private String sendMail = null;
	private String checkAsset = null;
	private String assetReserveValue = null;
	private String contect = null;
	private String checkSelfTarget = null;
	private String[] selfTargetList = null;
	
	private String repeatFlag = null;
	private String repeatTerm = null;
	private String repeatEndDate = null;
	private String ignoreTime = null;
	
	private CalendarService calendarService = null;

	public void setCalendarService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
	
	public String execute() throws Exception {
		String forward = "moveMonth"; 
		String listType = request.getParameter("listType");
		forward = ("week".equals(listType))?"moveWeek":forward;
		
		CalendarSaveCondVO calendarSaveCondVO = new CalendarSaveCondVO();
		calendarSaveCondVO.setSchedulerId(schedulerId);
		calendarSaveCondVO.setType(type);
		calendarSaveCondVO.setStartDate(startDate);
		calendarSaveCondVO.setEndDate(endDate);
		calendarSaveCondVO.setAllDay(allDay);
		calendarSaveCondVO.setTitle(title);
		calendarSaveCondVO.setLocation(location);
		calendarSaveCondVO.setContent(content);
		calendarSaveCondVO.setCheckShare(checkShare);
		calendarSaveCondVO.setShareValue(shareValue);
		calendarSaveCondVO.setSendMail(sendMail);
		calendarSaveCondVO.setCheckAsset(checkAsset);
		calendarSaveCondVO.setAssetReserveValue(assetReserveValue);
		calendarSaveCondVO.setContect(contect);
		calendarSaveCondVO.setCheckSelfTarget(checkSelfTarget);
		calendarSaveCondVO.setSelfTargetList(selfTargetList);
		
		calendarSaveCondVO.setRepeatFlag(repeatFlag);
		calendarSaveCondVO.setRepeatTerm(repeatTerm);
		calendarSaveCondVO.setRepeatEndDate(repeatEndDate);
		calendarSaveCondVO.setIgnoreTime(ignoreTime);
		
		calendarService.saveCalendar(calendarSaveCondVO, user);
		
		return forward;
	}

	public void setSchedulerId(int schedulerId) {
		this.schedulerId = schedulerId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setAllDay(String allDay) {
		this.allDay = allDay;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCheckShare(String checkShare) {
		this.checkShare = checkShare;
	}

	public void setShareValue(String shareValue) {
		this.shareValue = shareValue;
	}

	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
	}

	public void setCheckAsset(String checkAsset) {
		this.checkAsset = checkAsset;
	}

	public void setAssetReserveValue(String assetReserveValue) {
		this.assetReserveValue = assetReserveValue;
	}

	public void setContect(String contect) {
		this.contect = contect;
	}

	public void setCheckSelfTarget(String checkSelfTarget) {
		this.checkSelfTarget = checkSelfTarget;
	}

	public void setSelfTargetList(String[] selfTargetList) {
		this.selfTargetList = selfTargetList;
	}

	public void setRepeatFlag(String repeatFlag) {
		this.repeatFlag = repeatFlag;
	}

	public void setRepeatTerm(String repeatTerm) {
		this.repeatTerm = repeatTerm;
	}

	public void setRepeatEndDate(String repeatEndDate) {
		this.repeatEndDate = repeatEndDate;
	}

	public void setIgnoreTime(String ignoreTime) {
		this.ignoreTime = ignoreTime;
	}
}
