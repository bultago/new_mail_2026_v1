package com.terracetech.tims.service.aync.action;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.terracetech.tims.service.aync.data.SyncData;
import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;

public class TaskSync extends AbstractSyncAction implements ISyncAction{
	private SyncData model = null;
	
	public TaskSync(SyncData model) {
		this.model = model;
	}

	public void parseRequest(String xml) throws ParserConfigurationException, SAXException, IOException {
		// Skip
	}

	public void process(String deviceId, String deviceType) {
		// Skip
	}
	
	public void encodeResponse(WbxmlSerializer serializer) throws IOException,WbxmlException {
		serializer.openTag("AirSync", "Sync");
		serializer.openTag("AirSync", "Collections");
		serializer.openTag("AirSync", "Collection");
		serializer.textElement("AirSync", "Class", "Tasks");
		serializer.textElement("AirSync", "SyncKey", model.getSyncKey());
		serializer.textElement("AirSync", "CollectionId", "6");
		serializer.integerElement("AirSync", "Status", 1);
		
		serializer.closeTag();
		serializer.closeTag();
		serializer.closeTag();
	}
	
}
