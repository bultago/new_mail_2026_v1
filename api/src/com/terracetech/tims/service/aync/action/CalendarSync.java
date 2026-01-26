package com.terracetech.tims.service.aync.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.terracetech.tims.service.aync.SyncKey;
import com.terracetech.tims.service.aync.Wbxml;
import com.terracetech.tims.service.aync.command.CalendarClientAddCommand;
import com.terracetech.tims.service.aync.command.CalendarClientDelCommand;
import com.terracetech.tims.service.aync.command.CalendarClientModCommand;
import com.terracetech.tims.service.aync.command.CalendarServerAddCommand;
import com.terracetech.tims.service.aync.command.CalendarServerDelCommand;
import com.terracetech.tims.service.aync.command.CalendarServerModCommand;
import com.terracetech.tims.service.aync.command.ICommand;
import com.terracetech.tims.service.aync.data.SyncData;
import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mobile.manager.MobileSyncManager;
import com.terracetech.tims.webmail.mobile.vo.MobileSyncVO;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;

public class CalendarSync extends AbstractSyncAction implements ISyncAction{
	private SyncData model = null;
	private MobileSyncVO syncVo = null;
	
	private List<ICommand> clientCommandList = new ArrayList<ICommand>();
	private List<ICommand> serverCommandList = new ArrayList<ICommand>();
	
	public CalendarSync(SyncData model) {
		this.model = model;
	}

	public void parseRequest(String xml) throws ParserConfigurationException, SAXException, IOException {
		// Skip
	}

	public void process(String deviceId, String deviceType) {
		int userSeq = Integer.parseInt(getUser().get(User.MAIL_USER_SEQ));
		
		MobileSyncManager manager = (MobileSyncManager) ApplicationBeanUtil.getApplicationBean("mobileSyncManager");
		syncVo = manager.selectMobileSyncBySyncKey(userSeq, model.getSyncKey());
		if(syncVo == null){
			syncVo = SyncKey.getSyncKey(deviceId);
			manager.cleanCalendarSyncDate(userSeq, deviceId);
		}
		
		serverCommandList.add(new CalendarServerAddCommand(getUser(), syncVo));	
		serverCommandList.add(new CalendarServerModCommand(getUser(), syncVo));
		serverCommandList.add(new CalendarServerDelCommand(getUser(), syncVo));
		
		clientCommandList.add(new CalendarClientAddCommand(getUser(), model));
		clientCommandList.add(new CalendarClientModCommand(getUser(), model));
		clientCommandList.add(new CalendarClientDelCommand(getUser(), model));
		
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
	
	public void encodeResponse(WbxmlSerializer serializer) throws IOException,WbxmlException {
		serializer.openTag("AirSync", "Sync");
		serializer.openTag("AirSync", "Collections");
		serializer.openTag("AirSync", "Collection");
		serializer.textElement("AirSync", "Class", "Calendar");
		serializer.textElement("AirSync", "SyncKey", syncVo.getSyncKey());
		serializer.integerElement("AirSync", "CollectionId", Wbxml.Calendar);
		serializer.integerElement("AirSync", "Status", 1);
		
		for (ICommand command : clientCommandList) {
			command.encodeResponse(serializer);
		}
		
		int count = 0;
		for (ICommand command : serverCommandList) {
			count = count + command.countSyncData();
		}
		
		if(count >= 1){
			serializer.openTag("AirSync", "Commands");
			
			for (ICommand command : serverCommandList) {
				command.encodeResponse(serializer);
			}
			
			serializer.closeTag();	
		}
		
		
		
		serializer.closeTag();
		serializer.closeTag();
		serializer.closeTag();
		serializer.getXmlWriter().toString();
	}
}
