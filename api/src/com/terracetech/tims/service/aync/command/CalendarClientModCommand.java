package com.terracetech.tims.service.aync.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.terracetech.tims.service.aync.data.CalendarData;
import com.terracetech.tims.service.aync.data.ContactData;
import com.terracetech.tims.service.aync.data.SyncData;
import com.terracetech.tims.service.aync.data.SyncResultData;
import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.service.tms.impl.Convert;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;

public class CalendarClientModCommand implements ICommand{
	
	private SyncData model = null;
	
	private List<SyncResultData> modData = new ArrayList<SyncResultData>();
	
	public CalendarClientModCommand(User user, SyncData model) {
		this.model = model;
	}

	public void process(String deviceId, int windowSize) {
		SchedulerManager service = (SchedulerManager) ApplicationBeanUtil.getApplicationBean("schedulerManager");
		
		if(model.getModClientDataList() != null){
			for (Object iSyncData : model.getModClientDataList()) {
				if(iSyncData instanceof ContactData){
					CalendarData data = (CalendarData)iSyncData;
					
					String clientId = data.getClientId();
					int serverId = data.getSchedulerId();
					if(serverId != 0){
						service.modifySchedule(Convert.convert(data));
						
						SyncResultData result = new SyncResultData();
						result.setClientId(clientId);
						result.setServerId(String.valueOf(serverId));
						result.setStatus(1);
						
						modData.add(result);
					}
				}
			}
		}
	}
	
	public int countSyncData(){
		return 0;
	}
	
	public void encodeResponse(WbxmlSerializer serializer) throws IOException, WbxmlException {
		if(modData.size() > 0){
			serializer.openTag("AirSync", "Responses");		
			for (SyncResultData result : modData) {
				
				serializer.openTag("AirSync", "Change");		
				serializer.textElement("AirSync", "ClientId", result.getClientId());
				serializer.textElement("AirSync", "ServerId", result.getServerId());
				serializer.integerElement("AirSync", "Status", result.getStatus());
				
				serializer.closeTag();
			}
			serializer.closeTag();
		}
	}
	
}
