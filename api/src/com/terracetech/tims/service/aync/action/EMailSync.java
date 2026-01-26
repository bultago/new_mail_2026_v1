package com.terracetech.tims.service.aync.action;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.terracetech.tims.service.aync.data.SyncData;
import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.webmail.mailuser.User;

public class EMailSync extends AbstractSyncAction implements ISyncAction {
	
	private SyncData model = null;
	
	public EMailSync(SyncData model) {
		this.model = model;
	}

	public void parseRequest(String xml) throws ParserConfigurationException, SAXException, IOException{
		//skip
	}

	public void process(String deviceId, String deviceType) {

	}

	public void encodeResponse(WbxmlSerializer serializer) throws IOException,WbxmlException {
		serializer.openTag("AirSync", "Sync");
		serializer.openTag("AirSync", "Collections");
		serializer.openTag("AirSync", "Collection");
		serializer.textElement("AirSync", "Class", "Email");
		serializer.textElement("AirSync", "SyncKey", model.getSyncKey());
		serializer.textElement("AirSync", "CollectionId", "15");
		serializer.integerElement("AirSync", "Status", 1);
		
		serializer.closeTag();
		serializer.closeTag();
		serializer.closeTag();
	}
	
	public void updateDate(User user, String deviceId, String deviceType) {
	}
}
