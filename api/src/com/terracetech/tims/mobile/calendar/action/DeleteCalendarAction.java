package com.terracetech.tims.mobile.calendar.action;

import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.CalendarService;
import com.terracetech.tims.service.tms.vo.CalendarCondVO;
import com.terracetech.tims.service.tms.vo.CalendarDeleteCondVO;
import com.terracetech.tims.service.tms.vo.CalendarInfoVO;

public class DeleteCalendarAction extends BaseAction {

	private static final long serialVersionUID = 5283802699127421002L;
	
	private int schedulerId = 0;
	private String date = null;
	private String repeatFlag = null;
	private String deleteType = null;
	
	private CalendarService calendarService = null;
	
	public void setCalendarService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
	
	public String execute() throws Exception {
		
		CalendarDeleteCondVO calendarDeleteCondVo = new CalendarDeleteCondVO();
		calendarDeleteCondVo.setDate(date);
		calendarDeleteCondVo.setSchedulerId(schedulerId);
		calendarDeleteCondVo.setRepeatFlag(repeatFlag);
		calendarDeleteCondVo.setDeleteType(deleteType);
		
		calendarService.deleteCalendar(calendarDeleteCondVo, user);
	
		return "success";
	}
	
	public String question() throws Exception {

		CalendarCondVO calendarCondVo = new CalendarCondVO();
		calendarCondVo.setSchedulerId(schedulerId);
		calendarCondVo.setDate(date);
		
		CalendarInfoVO calendarInfo = calendarService.getSchedule(calendarCondVo, user);
		if (calendarInfo != null) {
			repeatFlag = calendarInfo.getRepeatFlag();
		}
		if("delete".equalsIgnoreCase(calendarInfo.getOutlookSync())){
			return "delete";
		}

		return "success";
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public int getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(int schedulerId) {
		this.schedulerId = schedulerId;
	}

	public void setRepeatFlag(String repeatFlag) {
		this.repeatFlag = repeatFlag;
	}

	public String getRepeatFlag() {
		return repeatFlag;
	}

	public void setDeleteType(String deleteType) {
		this.deleteType = deleteType;
	}
	
}
