package com.terracetech.tims.service.aync.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.mortbay.util.ajax.Continuation;
import org.mortbay.util.ajax.ContinuationSupport;
import org.xml.sax.SAXException;

import com.terracetech.tims.service.aync.Wbxml;
import com.terracetech.tims.service.aync.data.PingData;
import com.terracetech.tims.service.aync.handler.PingHandler;
import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mobile.manager.MobileSyncManager;
import com.terracetech.tims.webmail.mobile.manager.SyncListener;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;

/**
 * <pre>
 * The Ping command is used to request that the server monitor specified folders for changes 
 * that would require the client to resynchronize
 * </pre>
 * 
 * @author waitone
 *
 */
public class Ping extends AbstractSyncAction implements ISyncAction {

	private int heartbeatInterval = 0;
	
	private String deviceId = null;
	
	private HttpServletRequest req = null;
	
	private Object result = null;
	
	public Ping(HttpServletRequest req, String deviceId) {
		this.req = req;
		this.deviceId = deviceId;
	}

	/**
	 * Class : Email, Calendar, Contacts, Tasks, Notes
	 */
	public void parseRequest(String xml) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp=spf.newSAXParser();
		ByteArrayInputStream bis = new ByteArrayInputStream(xml.getBytes());
		PingHandler handler = new PingHandler();
		sp.parse(bis, handler);
		
		PingData model = handler.getModel();
	}

	public void process(String deviceId, String deviceType) {
		Continuation continuation = ContinuationSupport.getContinuation(req, this);
		
		if(heartbeatInterval < 300)
            heartbeatInterval = 300;
		
		req.setAttribute(Ping.class.getName(), this);
		
		SyncListener.addListener(getUser(), deviceId, continuation);
		continuation.suspend(heartbeatInterval * 1000);
		result = SyncListener.getChangeData(getUser(), deviceId);
		SyncListener.removeListener(getUser(), deviceId);
	}


	/**
	 * <pre>
	 * <Ping>
	 * 		<Status>1</Status>
	 * 		<Folders><Folders>
	 * 		<HeartbeatInterval>1</HeartbeatInterval>
	 * 		<MaxFolders>1</MaxFolders>
	 * </Ping>
	 * </pre>
	 * 
	 * Status 1 : The heartbeat interval expired before any changes occurred in the folders being monitored.
	 * Status 2 : Changes occurred in at least one of the monitored folders. The response specifies the changed folders. 
	 * Status 3 : The Ping command request omitted required parameters.
	 * Status 4 : Syntax error in Ping command request.
	 * Status 7 : Folder hierarchy sync required.
	 * Status 8 : An error occurred on the server.
	 * 
	 */
	public void encodeResponse(WbxmlSerializer serializer) throws IOException, WbxmlException {
		int status = 0;
		
		if(result == null){
			status = 1;
		}else{
			status = 2;
		}
		
		serializer.openTag("Ping", "Ping");
		serializer.integerElement("Ping", "Status", status);
		if(status ==2){
			serializer.openTag("Ping", "Folders");
			serializer.integerElement("Ping", "Folder", Wbxml.Contacts);
			serializer.integerElement("Ping", "Folder", Wbxml.Calendar);
			serializer.integerElement("Ping", "Folder", Wbxml.Tasks);
			serializer.closeTag();
		}
		serializer.closeTag();
	}
	
	public void updateDate(User user, String deviceId, String deviceType) {
		MobileSyncManager manager = (MobileSyncManager) ApplicationBeanUtil.getApplicationBean("mobileSyncManager");
		manager.updatePingSyncDate(user, deviceId);
	}
}
