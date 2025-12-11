package com.terracetech.tims.service.aync.command;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

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

public class CalendarServerDelCommand implements ICommand{
	
	private User user = null;
	
	private MobileSyncVO syncVo = null;
	
	private List<SchedulerDataVO> list = null;
	
	private MobileSyncManager syncManager = null;
	
	public CalendarServerDelCommand(User user, MobileSyncVO syncVo) {
		this.user = user;
		this.syncVo = syncVo;
		
		syncManager = (MobileSyncManager) ApplicationBeanUtil.getApplicationBean("mobileSyncManager");
	}

	public void process(String deviceId, int windowSize) {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		SchedulerManager service = (SchedulerManager) ApplicationBeanUtil.getApplicationBean("schedulerManager");
		String syncTime = syncVo.getCalendarUpdateSyncTime();
		//syncTime이 없는 경우 3일 이전부터
		if(StringUtils.isEmpty(syncTime)){
			try {
				syncTime = DateUtil.findDateStr(-3);
				syncTime = syncTime + "000000";
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		list = service.getDeletedScheduleList(userSeq, syncTime, windowSize);
		updateCalendarSyncDate(deviceId);
	}
	
	private void updateCalendarSyncDate(String deviceId){
		if(list==null || list.size()==0){
			syncManager.updateCalendarUpdateSyncDate(user, deviceId, DateUtil.getBasicDateStr());
			return;
		}
		
		SchedulerDataVO scheduler = list.get(list.size()-1);
		if(StringUtils.isNotEmpty(scheduler.getModifyTime())){
			long value = Long.parseLong(scheduler.getModifyTime());
			value = value + 1;
			syncManager.updateCalendarDeleteSyncDate(user, deviceId, String.valueOf(value));	
		}else{
			syncManager.updateCalendarDeleteSyncDate(user, deviceId, DateUtil.getBasicDateStr());
		}
	}
	
	public int countSyncData(){
		if(list ==null)
			return 0;
		
		return list.size();
	}
	
	public void encodeResponse(WbxmlSerializer serializer) throws IOException, WbxmlException {
		if (list != null) {
			for (SchedulerDataVO vo : list) {
				serializer.openTag("AirSync", "Delete");
				serializer.textElement("AirSync", "ServerId", Integer.toString(vo.getSchedulerId()));

				serializer.closeTag();	
			}
		}
	}
	
}
