package com.terracetech.tims.service.aync.command;

import java.io.IOException;

import com.terracetech.tims.service.aync.data.CalendarData;
import com.terracetech.tims.service.aync.data.SyncData;
import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;

public class CalendarClientDelCommand implements ICommand{
	
	
	private SyncData model = null;
	
	public CalendarClientDelCommand(User user, SyncData model) {
		this.model = model;
	}

	public void process(String deviceId, int windowSize) {
		
		SchedulerManager service = (SchedulerManager) ApplicationBeanUtil.getApplicationBean("schedulerManager");
		
		if(model.getDelClientDataList() != null){
			for (Object iSyncData : model.getDelClientDataList()) {
				if(iSyncData instanceof CalendarData){
					CalendarData data = (CalendarData)iSyncData;
					
					int serverId = data.getSchedulerId();
					if(serverId != 0){
						service.deleteSchedule(serverId, "off");
					}
				}
			}
		}
	}
	
	public int countSyncData(){
		return 0;
	}
	
	public void encodeResponse(WbxmlSerializer serializer) throws IOException, WbxmlException {
	}
	
}
