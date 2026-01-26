package com.terracetech.tims.mobile.calendar.action;

import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.CalendarService;
import com.terracetech.tims.service.tms.vo.CalendarCondVO;
import com.terracetech.tims.service.tms.vo.CalendarInfoVO;
import com.terracetech.tims.service.tms.vo.CalendarWriteVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class WriteCalendarAction extends BaseAction {

	private static final long serialVersionUID = -4096165530067738183L;
	
	private int schedulerId = 0;
	private String date = null;
	private String type = null;
	
	private CalendarWriteVO calendarWriteVo = null;
	private CalendarInfoVO calendarInfoVo = null;
	private CalendarService calendarService = null;
	
	public void setCalendarService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
	
	public String execute() throws Exception {
		String listType = request.getParameter("listType");		
		CalendarCondVO calendarCondVo = new CalendarCondVO();
		calendarCondVo.setDate(date);
		
		calendarWriteVo = calendarService.getWriteCalendar(calendarCondVo, user);
		request.setAttribute("listType", listType);
		
		return "success";
	}
	
	public String modify() throws Exception {		
		CalendarCondVO calendarCondVo = new CalendarCondVO();
		calendarCondVo.setDate(date);
		calendarCondVo.setSchedulerId(schedulerId);
		calendarCondVo.setType(type);
		
		calendarWriteVo = calendarService.getWriteCalendar(calendarCondVo, user);
		calendarInfoVo = calendarService.getSchedule(calendarCondVo, user);
		calendarInfoVo.setContent(StringUtils.html2Text(calendarInfoVo.getContent()));
		return "success";
	}

	public void setSchedulerId(int schedulerId) {
		this.schedulerId = schedulerId;
	}

	public CalendarWriteVO getCalendarWriteVo() {
		return calendarWriteVo;
	}

	public CalendarInfoVO getCalendarInfoVo() {
		return calendarInfoVo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setType(String type) {
		this.type = type;
	}
}
