package com.terracetech.tims.mobile.calendar.action;

import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.CalendarService;
import com.terracetech.tims.service.tms.vo.CalendarCondVO;
import com.terracetech.tims.service.tms.vo.CalendarInfoVO;

public class ViewCalendarAction extends BaseAction {

	private static final long serialVersionUID = -1061478436218461194L;
	private String date = null;
	private int schedulerId = 0;
	private int mailUserSeq = 0;
	private String target = null;
	private CalendarInfoVO calendarInfo = null;
	
	private CalendarService calendarService = null;
	
	public void setCalendarService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}

	public String execute() throws Exception {
		CalendarCondVO calendarCondVo = new CalendarCondVO();
		calendarCondVo.setDate(date);
		calendarCondVo.setSchedulerId(schedulerId);
		
		calendarInfo = calendarService.getSchedule(calendarCondVo, user);
		if("delete".equalsIgnoreCase(calendarInfo.getOutlookSync())){
			return "delete";
		}
		return "success";
	}

	public void setDate(String date) {
		this.date = date;
	}

	public CalendarInfoVO getCalendarInfo() {
		return calendarInfo;
	}

	public void setSchedulerId(int schedulerId) {
		this.schedulerId = schedulerId;
	}

	public int getMailUserSeq() {
		return mailUserSeq;
	}
	
	public String getTarget() {
            return target;
        }
    
        public void setTarget(String target) {
                this.target = target;
        }
}
