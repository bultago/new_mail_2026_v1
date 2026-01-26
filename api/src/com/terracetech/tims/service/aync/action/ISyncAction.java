package com.terracetech.tims.service.aync.action;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.webmail.mailuser.User;

public interface ISyncAction {
	
	public void setUser(User user);

	public void parseRequest(String xml) throws ParserConfigurationException, SAXException, IOException;
	
	public void process(String deviceId, String deviceType);
	
	public void encodeResponse(WbxmlSerializer serializer)  throws IOException, WbxmlException;
}
