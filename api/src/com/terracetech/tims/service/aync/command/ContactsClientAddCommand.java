package com.terracetech.tims.service.aync.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.terracetech.tims.service.aync.data.ContactData;
import com.terracetech.tims.service.aync.data.SyncData;
import com.terracetech.tims.service.aync.data.SyncResultData;
import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.service.tms.IContactService;
import com.terracetech.tims.service.tms.impl.Convert;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class ContactsClientAddCommand implements ICommand{
	
	private User user = null;
	
	private SyncData model = null;
	
	private List<SyncResultData> addData = new ArrayList<SyncResultData>();

	public ContactsClientAddCommand(User user, SyncData model) {
		this.user = user;
		this.model = model;
	}

	public void process(String deviceId, int windowSize) {
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		IContactService service = (IContactService) ApplicationBeanUtil.getApplicationBean("contactService");
		
		if(model.getAddClientDataList() != null){
			for (Object iSyncData : model.getAddClientDataList()) {
				//중복체크해야함
				if(iSyncData instanceof ContactData){
					ContactData data = (ContactData)iSyncData;
					
					String clientId = data.getClientId();
					if(StringUtils.isNotEmpty(clientId)){
						
						data.setDomainSeq(domainSeq);
						data.setUserSeq(userSeq);
						
						int serverId = service.readAddressMemberSeqByClientId(userSeq, clientId);
						if(serverId > 0){
							//이미 추가된 데이터 수정하는 경우
							service.modContact(Convert.convert(data));
						}else{
							service.addContact(Convert.convert(data));	
							serverId = service.readAddressMemberSeqByClientId(userSeq, clientId);
						}
						
						if(serverId > 0){
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
		return 0;
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
