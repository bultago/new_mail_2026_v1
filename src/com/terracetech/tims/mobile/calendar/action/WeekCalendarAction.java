package com.terracetech.tims.mobile.calendar.action;

import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.CalendarService;
import com.terracetech.tims.service.tms.vo.CalendarCondVO;
import com.terracetech.tims.service.tms.vo.CalendarHolidayVO;
import com.terracetech.tims.service.tms.vo.CalendarVO;
import com.terracetech.tims.webmail.common.log.LogManager;

public class WeekCalendarAction extends BaseAction {

	private static final long serialVersionUID = -397684145288645712L;
	private String date = null;
	private CalendarVO calendar = null;
	private CalendarHolidayVO[] calendarHolidayList = null;
	
	private CalendarService calendarService = null;
	
	public void setCalendarService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
	
	public String execute() throws Exception {
		
		CalendarCondVO calendarCondVo = new CalendarCondVO();
		calendarCondVo.setDate(date);
		try{
		calendar = calendarService.getWeekCalendar(calendarCondVo, user);
		calendarHolidayList = calendarService.readHolidayList(calendarCondVo, user);
		}catch(Exception e){
			 LogManager.writeErr(this, e.getMessage(), e);
		}
		return "success";
	}
	
	public CalendarVO getCalendar() {
		return calendar;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public CalendarHolidayVO[] getCalendarHolidayList() {
		return calendarHolidayList;
	}
}
