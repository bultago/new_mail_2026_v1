package com.terracetech.tims.service.aync.command;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.terracetech.tims.service.aync.util.CalendarSerializer;
import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mobile.manager.MobileSyncManager;
import com.terracetech.tims.webmail.mobile.vo.MobileSyncVO;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.DateUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class CalendarServerAddCommand implements ICommand{
	
	private User user = null;
	
	private MobileSyncVO syncVo = null;
	
	private List<SchedulerDataVO> list = null;
	
	private MobileSyncManager syncManager = null;
	
	public CalendarServerAddCommand(User user, MobileSyncVO syncVo) {
		this.user = user;
		this.syncVo = syncVo;
		
		syncManager = (MobileSyncManager) ApplicationBeanUtil.getApplicationBean("mobileSyncManager");
	}

	public void process(String deviceId, int windowSize) {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		SchedulerManager service = (SchedulerManager) ApplicationBeanUtil.getApplicationBean("schedulerManager");
		
		String syncTime = syncVo.getCalendarInsertSyncTime();
		
		list = service.getScheduleListAfterSyncTime(userSeq, syncTime, windowSize);
		updateCalendarSyncDate(deviceId);
	}
	
	private void updateCalendarSyncDate(String deviceId){
		if(list==null || list.size()==0){
			syncManager.updateCalendarInsertSyncDate(user, deviceId, DateUtil.getBasicDateStr());
			return;
		}
		
		SchedulerDataVO scheduler = list.get(list.size()-1);
		if(StringUtils.isNotEmpty(scheduler.getCreateTime())){
			long value = Long.parseLong(scheduler.getCreateTime());
			value = value + 1;
			syncManager.updateCalendarInsertSyncDate(user, deviceId, String.valueOf(value));	
		}else{
			syncManager.updateCalendarInsertSyncDate(user, deviceId, DateUtil.getBasicDateStr());
		}
	}
	
	public int countSyncData(){
		if(list ==null)
			return 0;
		
		return list.size();
	}
	
	public void encodeResponse(WbxmlSerializer writer) throws IOException, WbxmlException {
		CalendarSerializer serializer = new CalendarSerializer(writer);
		
		if(list != null && list.size() > 0){
			
			for (SchedulerDataVO vo : list) {
				serializer.openTag("AirSync", "Add");	
				
				serializer.textElement("AirSync", "ServerId", String.valueOf(vo.getSchedulerId()));
				serializer.openTag("AirSync", "ApplicationData");
				
				serializer.textElement("Calendar", "Subject", vo.getTitle());
				String startDate = DateUtil.getActiveSyncFormat(vo.getStartDate() + "00");
				if(StringUtils.isNotEmpty(startDate)){
					serializer.textElement("Calendar", "DTStamp", startDate);	
					serializer.textElement("Calendar", "StartTime", startDate);
				}
				
				String endDate = DateUtil.getActiveSyncFormat(vo.getEndDate() + "00");
				if(StringUtils.isNotEmpty(endDate)){
					serializer.textElement("Calendar", "EndTime", endDate);	
				}
				
				if("on".equals(vo.getAllDay())){
					serializer.integerElement("Calendar", "AllDayEvent", 1);
				}
				
				if("on".equals(vo.getRepeatFlag())){
					serializer.writeRepeatTerm(vo.getRepeatTerm(), vo.getRepeatEndDate());
				}
				
				serializer.closeTag();
				serializer.closeTag();
			}
		}
	}
}
