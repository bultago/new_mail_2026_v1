package com.terracetech.tims.service.aync.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.terracetech.tims.service.aync.command.ContactsClientAddCommand;
import com.terracetech.tims.service.aync.command.ContactsClientDelCommand;
import com.terracetech.tims.service.aync.command.ContactsClientModCommand;
import com.terracetech.tims.service.aync.command.ContactsServerAddCommand;
import com.terracetech.tims.service.aync.command.ContactsServerDelCommand;
import com.terracetech.tims.service.aync.command.ContactsServerModCommand;
import com.terracetech.tims.service.aync.command.ICommand;
import com.terracetech.tims.service.aync.data.SyncData;
import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mobile.manager.MobileSyncManager;
import com.terracetech.tims.webmail.mobile.vo.MobileSyncVO;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;

public class ContactsSync extends AbstractSyncAction implements ISyncAction {
	private SyncData model = null;
	private MobileSyncVO syncVo = null;
	
	private List<ICommand> clientCommandList = new ArrayList<ICommand>();
	private List<ICommand> serverCommandList = new ArrayList<ICommand>();
	
	public ContactsSync(SyncData model) {
		this.model = model;
		
	}
	
	public void parseRequest(String xml) throws ParserConfigurationException,SAXException, IOException {
		//skip		
	}

	public void process(String deviceId, String deviceType) {
		int userSeq = Integer.parseInt(getUser().get(User.MAIL_USER_SEQ));
		
		MobileSyncManager manager = (MobileSyncManager) ApplicationBeanUtil.getApplicationBean("mobileSyncManager");
		syncVo = manager.selectMobileSyncBySyncKey(userSeq, model.getSyncKey());
		if(syncVo == null){
			return;
		}
		
		serverCommandList.add(new ContactsServerAddCommand(getUser(), syncVo));		
		serverCommandList.add(new ContactsServerModCommand(getUser(), syncVo));
		serverCommandList.add(new ContactsServerDelCommand(getUser(), syncVo));
		
		clientCommandList.add(new ContactsClientAddCommand(getUser(), model));		
		clientCommandList.add(new ContactsClientModCommand(getUser(), model));
		clientCommandList.add(new ContactsClientDelCommand(getUser(), model));
		
		int windowSize = model.getCount()==0? 5 : model.getCount();
		for (ICommand command : serverCommandList) {
			if(windowSize==0)
				break;
			
			command.process(deviceId, windowSize);
			windowSize = windowSize - command.countSyncData();
		}
		
		for (ICommand command : clientCommandList) {
			command.process(deviceId, 0);
		}
	}
	
	public void encodeResponse(WbxmlSerializer serializer) throws IOException, WbxmlException {
		serializer.openTag("AirSync", "Sync");
		serializer.openTag("AirSync", "Collections");
		serializer.openTag("AirSync", "Collection");
		serializer.textElement("AirSync", "Class", "Contacts");
		serializer.textElement("AirSync", "SyncKey", model.getSyncKey());
		serializer.textElement("AirSync", "CollectionId", "7");
		
		if(syncVo == null){
			serializer.integerElement("AirSync", "Status", 9);	
		}else{
			serializer.integerElement("AirSync", "Status", 1);
			
			for (ICommand command : clientCommandList) {
				command.encodeResponse(serializer);
			}
			
			int count = 0;
			for (ICommand command : serverCommandList) {
				count = count + command.countSyncData();
			}
			
			if(count >=1){
				serializer.openTag("AirSync", "Commands");
				for (ICommand command : serverCommandList) {
					command.encodeResponse(serializer);
				}
				serializer.closeTag();	
			}
			
		}
		
		serializer.closeTag();
		serializer.closeTag();
		serializer.closeTag();
		serializer.getXmlWriter().toString();
	}

	public void updateDate(User user, String deviceId, String deviceType) {
	}

	
}
