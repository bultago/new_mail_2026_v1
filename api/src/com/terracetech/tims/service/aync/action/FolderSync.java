package com.terracetech.tims.service.aync.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.terracetech.tims.service.aync.SyncKey;
import com.terracetech.tims.service.aync.Wbxml;
import com.terracetech.tims.service.aync.data.FolderSyncData;
import com.terracetech.tims.service.aync.handler.FolderSyncHandler;
import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mobile.manager.MobileSyncManager;
import com.terracetech.tims.webmail.mobile.vo.MobileSyncVO;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;


public class FolderSync extends AbstractSyncAction implements ISyncAction{

	protected String clientKey;
	protected String serverKey;
	
	private FolderSyncData model;

	public FolderSync() {
		clientKey = "0";
	}

	public void parseRequest(String xml) throws ParserConfigurationException, SAXException, IOException{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp=spf.newSAXParser();
		ByteArrayInputStream bis = new ByteArrayInputStream(xml.getBytes());
		FolderSyncHandler handler = new FolderSyncHandler();
		sp.parse(bis, handler);
		
		model = handler.getModel();
	}

	public void process(String deviceId, String deviceType) {
		int userSeq = Integer.parseInt(getUser().get(User.MAIL_USER_SEQ));
		
		MobileSyncVO syncVo = SyncKey.getSyncKey(deviceId);
		serverKey = syncVo.getSyncKey();

		if(model.getSyncKey().equals("0")){
			MobileSyncManager manager = (MobileSyncManager) ApplicationBeanUtil.getApplicationBean("mobileSyncManager");
			manager.cleanCalendarSyncDate(userSeq, deviceId);
			manager.cleanContactsSyncDate(userSeq, deviceId);	
		}
	}
	
	/**
	 * <pre>
	 * Count : the number of added, deleted, and updated folders on the server since the last folder synchronization.
	 * ServerId : element specifies the server-unique identifier for a folder on the server.
	 * Type : element specifies the type of the folder that was added or updated (renamed or moved) on the server.
	 * </pre> 
	 * 
	 * <ul>
	 * 	<li> 1 : User-created folder</li>
	 * 	<li> 2 : Default Inbox folder</li>
	 * 	<li> 3 : Default Drafts folder</li>
	 * 	<li> 4 : Default Deleted Items folder</li>
	 * 	<li> 5 : Default Sent Items folder</li>
	 * 	<li> 6 : Default Outbox folder</li>
	 * 	<li> 7 : Default Tasks folder</li>
	 * 	<li> 8 : Default Calendar folder</li>
	 * 	<li> 9 : Default Contacts folder</li>
	 * 	<li> 10 : Default Notes folder</li>
	 * 	<li> 11 : Default Journal folder</li>
	 * 	<li> 12 : User-created Mail folder</li>
	 * 	<li> 13 : User-created Calendar folder</li>
	 * 	<li> 14 : User-created Contacts folder</li>
	 * 	<li> 15 : User-created Tasks folder</li>
	 * 	<li> 16 : User-created journal folder</li>
	 * 	<li> 17 : User-created Notes folder</li>
	 * 	<li> 18 : Unknown folder type</li>
	 * 	<li> 19 : Recipient information cache</li>
	 * 
	 * 
	 */
	public void encodeResponse(WbxmlSerializer serializer) throws IOException, WbxmlException{
		
		serializer.openTag("FolderHierarchy", "FolderSync");
		
		serializer.integerElement("FolderHierarchy", "Status", 1);
		serializer.textElement("FolderHierarchy", "SyncKey", serverKey);
		serializer.openTag("FolderHierarchy", "Changes");
		serializer.integerElement("FolderHierarchy", "Count", 3);
		
		serializer.openTag("FolderHierarchy", "Add");
		serializer.integerElement("FolderHierarchy", "ServerId", Wbxml.Contacts);
		serializer.textElement("FolderHierarchy", "ParentId", "0");
		serializer.textElement("FolderHierarchy", "DisplayName", "Contacts");
		serializer.integerElement("FolderHierarchy", "Type", 9);
		serializer.closeTag();
		
		serializer.openTag("FolderHierarchy", "Add");
		serializer.integerElement("FolderHierarchy", "ServerId", Wbxml.Calendar);
		serializer.textElement("FolderHierarchy", "ParentId", "0");
		serializer.textElement("FolderHierarchy", "DisplayName", "Calendar");
		serializer.integerElement("FolderHierarchy", "Type", 8);
		serializer.closeTag();
		
		serializer.openTag("FolderHierarchy", "Add");
		serializer.integerElement("FolderHierarchy", "ServerId", Wbxml.Tasks);
		serializer.textElement("FolderHierarchy", "ParentId", "0");
		serializer.textElement("FolderHierarchy", "DisplayName", "Tasks");
		serializer.integerElement("FolderHierarchy", "Type", 7);
		serializer.closeTag();
		
		serializer.closeTag();
		serializer.closeTag();
		
	}
	
	public void updateDate(User user, String deviceId, String deviceType) {
		if("validate".equals(deviceId)){
			return;
		}
		MobileSyncManager manager = (MobileSyncManager) ApplicationBeanUtil.getApplicationBean("mobileSyncManager");
		manager.updateDeviceData(user, deviceId, deviceType, serverKey);
	}
}
