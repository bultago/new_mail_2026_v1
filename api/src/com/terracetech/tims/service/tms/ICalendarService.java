package com.terracetech.tims.service.tms;

import com.terracetech.tims.service.tms.vo.CalendarCondVO;
import com.terracetech.tims.service.tms.vo.CalendarDeleteCondVO;
import com.terracetech.tims.service.tms.vo.CalendarHolidayVO;
import com.terracetech.tims.service.tms.vo.CalendarInfoVO;
import com.terracetech.tims.service.tms.vo.CalendarSaveCondVO;
import com.terracetech.tims.service.tms.vo.CalendarVO;
import com.terracetech.tims.service.tms.vo.CalendarWriteVO;

public interface ICalendarService {
	
	public final static int SUCCESS = 1;
	
	public final static int FAILED = -1;
	
	public CalendarVO getMonthCalendar(CalendarCondVO calendarCondVo);
	
	public CalendarVO getWeekCalendar(CalendarCondVO calendarCondVo);
	
	public CalendarInfoVO getSchedule(CalendarCondVO calendarCondVo);
	
	public CalendarWriteVO getWriteCalendar(CalendarCondVO calendarCondVo);
	
	public int deleteCalendar(CalendarDeleteCondVO calendarDeleteCondVo);
	
	public CalendarHolidayVO[] readHolidayList(CalendarCondVO calendarCondVo);
	
	public int saveCalendar(CalendarSaveCondVO calendarSaveCondVO);
}
