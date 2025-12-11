package com.terracetech.tims.service.aync.command;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mortbay.log.Log;

import com.terracetech.tims.service.aync.data.ContactData;
import com.terracetech.tims.service.aync.data.SyncData;
import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.service.tms.IContactService;
import com.terracetech.tims.service.tms.impl.Convert;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;

public class ContactsClientDelCommand implements ICommand{
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private User user = null;
	
	private SyncData model = null;
	
	public ContactsClientDelCommand(User user, SyncData model) {
		this.user = user;
		this.model = model;
	}

	public void process(String deviceId, int windowSize) {
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		IContactService service = (IContactService) ApplicationBeanUtil.getApplicationBean("contactService");
		
		if(model.getDelClientDataList() != null){
			for (Object iSyncData : model.getDelClientDataList()) {
				if(iSyncData instanceof ContactData){
					ContactData data = (ContactData)iSyncData;
					
					int serverId = data.getMemberSeq();
					if(serverId != 0){
						data.setDomainSeq(domainSeq);
						data.setUserSeq(userSeq);
						
						log.info("ActiveSync cmd=ContactsClientDelCommand, serverId="+serverId);
						service.delContact(Convert.convert(data));
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
