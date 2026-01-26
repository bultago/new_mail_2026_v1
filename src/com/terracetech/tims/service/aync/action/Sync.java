package com.terracetech.tims.service.aync.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.terracetech.tims.service.aync.SyncKey;
import com.terracetech.tims.service.aync.data.SyncData;
import com.terracetech.tims.service.aync.handler.SyncHandler;
import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mobile.manager.MobileSyncManager;
import com.terracetech.tims.webmail.mobile.vo.MobileSyncVO;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;


public class Sync extends AbstractSyncAction implements ISyncAction{
	
	private ISyncAction subCommand = null;
	
	private SyncData model = null;

	public void parseRequest(String xml) throws ParserConfigurationException, SAXException, IOException{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp=spf.newSAXParser();
		ByteArrayInputStream bis = new ByteArrayInputStream(xml.getBytes());
		SyncHandler handler = new SyncHandler();
		sp.parse(bis, handler);
		
		model = handler.getModel();
		
		if("Email".equals(model.getTarget())){
			subCommand = new EMailSync(model);
		}else if("Calendar".equals(model.getTarget())){
			subCommand = new CalendarSync(model);
		}else if("Contacts".equals(model.getTarget())){
			subCommand = new ContactsSync(model);
		}else{
			subCommand = new TaskSync(model);
		}
		subCommand.setUser(getUser());
		subCommand.parseRequest(xml);
	}

	public void process(String deviceId, String deviceType) {
		if(subCommand!=null){
			int userSeq = Integer.parseInt(getUser().get(User.MAIL_USER_SEQ));
			
			MobileSyncManager manager = (MobileSyncManager) ApplicationBeanUtil.getApplicationBean("mobileSyncManager");
			MobileSyncVO syncVo = manager.selectMobileSyncBySyncKey(userSeq, model.getSyncKey());
			if(syncVo == null){
				syncVo = SyncKey.getSyncKey(deviceId);
				model.setSyncKey(syncVo.getSyncKey());
				manager.updateDeviceData(getUser(), deviceId, deviceType, syncVo.getSyncKey());
			}
			subCommand.process(deviceId, deviceType);
		}
	}
	
	public void encodeResponse(WbxmlSerializer serializer) throws IOException, WbxmlException{
		if(subCommand!=null)
			subCommand.encodeResponse(serializer);
	}
}
