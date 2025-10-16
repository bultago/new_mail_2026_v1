package com.terracetech.tims.service.aync.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.terracetech.tims.webmail.util.StringUtils;

public class CalendarClientAddCommand implements ICommand{
	
	private User user = null;
	
	private SyncData model = null;
	
	private List<SyncResultData> addData = new ArrayList<SyncResultData>();

	public CalendarClientAddCommand(User user, SyncData model) {
		this.user = user;
		this.model = model;
	}

	public void process(String deviceId, int windowSize) {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		SchedulerManager service = (SchedulerManager) ApplicationBeanUtil.getApplicationBean("schedulerManager");
		
		if(model.getAddClientDataList() != null){
			for (Object iSyncData : model.getAddClientDataList()) {
				//중복체크해야함
				if(iSyncData instanceof CalendarData){
					CalendarData data = (CalendarData)iSyncData;
					
					String clientId = data.getClientId();
					if(StringUtils.isNotEmpty(clientId)){
						
						data.setMailUserSeq(userSeq);
						
						Map<String, Object> resultMap = service.saveSchedule(Convert.convert(data));
						Integer serverId = (Integer)resultMap.get("schedulerId");
						
						if(serverId != 0){
							SyncResultData result = new SyncResultData();
							result.setClientId(clientId);
							result.setServerId(String.valueOf(serverId));
							result.setStatus(1);
							
							addData.add(result);
						}
					}
						
				}
			}	
		}
	}
	
	public int countSyncData(){
		if(addData ==null)
			return 0;
		
		return addData.size();
	}
	
	public void encodeResponse(WbxmlSerializer serializer) throws IOException, WbxmlException {
		if(addData.size() > 0){
			serializer.openTag("AirSync", "Responses");		
			for (SyncResultData result : addData) {
				
				serializer.openTag("AirSync", "Add");		
				serializer.textElement("AirSync", "ClientId", result.getClientId());
				serializer.textElement("AirSync", "ServerId", result.getServerId());
				serializer.integerElement("AirSync", "Status", result.getStatus());
				
				serializer.closeTag();
			}
			serializer.closeTag();
		}
	}
	
}
