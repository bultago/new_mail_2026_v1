package com.terracetech.tims.service.aync.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.terracetech.tims.service.aync.SyncKey;
import com.terracetech.tims.service.aync.Wbxml;
import com.terracetech.tims.service.aync.data.GetItemEstimateData;
import com.terracetech.tims.service.aync.handler.GetItemEstimateHandler;
import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mobile.manager.MobileSyncManager;
import com.terracetech.tims.webmail.mobile.vo.MobileSyncVO;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;

/**
 * The GetItemEstimate command gets an estimate of the number of items in a
 * collection or folder on the server that have to be synchronized.
 * 
 * The <FilterType> applies to e-mail, calendar, and task collections. If a
 * filter type is specified, then the server sends an estimate of the items
 * within the filter specifications.
 * 
 * @author waitone
 * 
 */
public class GetItemEstimate extends AbstractSyncAction implements ISyncAction{
	
	private Logger log = Logger.getLogger(this.getClass());

	private GetItemEstimateData model = null;
	
	private int contactsEventCount = 0;
	
	private int calendarEventCount = 0;
	
	public void parseRequest(String xml) throws ParserConfigurationException, SAXException, IOException{
		
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp=spf.newSAXParser();
		ByteArrayInputStream bis = new ByteArrayInputStream(xml.getBytes());
		GetItemEstimateHandler handler = new GetItemEstimateHandler();
		sp.parse(bis, handler);
		
		model = handler.getModel();
	}
	
	public void process(String deviceId, String deviceType) {
		int userSeq = Integer.parseInt(getUser().get(User.MAIL_USER_SEQ));
		
		MobileSyncManager manager = (MobileSyncManager) ApplicationBeanUtil.getApplicationBean("mobileSyncManager");
		
		MobileSyncVO syncVo = manager.selectMobileSyncByDeviceId(userSeq, deviceId);
		if(syncVo == null){
			syncVo = SyncKey.getSyncKey(deviceId);
			manager.updateDeviceData(getUser(), deviceId, deviceType, syncVo.getSyncKey());
		}
		
		contactsEventCount = manager.countContactsEvent(userSeq, syncVo);
		calendarEventCount = manager.countCaledarEvent(userSeq, syncVo);
		
		log.debug("GetItemEstimate : userSeq="+userSeq + ", contactsEventCount="+ contactsEventCount + ", calendarEventCount="+calendarEventCount);
	}

	/**
	 * Status 1 : Success.
	 * Status 2 : A collection was invalid or one of the specified collection IDs was invalid.
	 * Status 3 : The synchronization state has not been primed.
	 * Status 4 : The specified synchronization key was invalid.
	 */
	public void encodeResponse(WbxmlSerializer serializer) throws IOException, WbxmlException {
		
		serializer.openTag("ItemEstimate", "GetItemEstimate");
		serializer.openTag("ItemEstimate", "Collections");
		
		serializer.openTag("ItemEstimate", "Collection");
		serializer.textElement("ItemEstimate", "Class", "Calendar");
		serializer.integerElement("ItemEstimate", "CollectionId", Wbxml.Calendar);
		serializer.integerElement("ItemEstimate", "Estimate", calendarEventCount);
		serializer.closeTag();
		
		serializer.openTag("ItemEstimate", "Collection");
		serializer.textElement("ItemEstimate", "Class", "Contacts");
		serializer.integerElement("ItemEstimate", "CollectionId", Wbxml.Contacts);
		serializer.integerElement("ItemEstimate", "Estimate", contactsEventCount);
		serializer.closeTag();
		
		serializer.openTag("ItemEstimate", "Collection");
		serializer.textElement("ItemEstimate", "Class", "Tasks");
		serializer.integerElement("ItemEstimate", "CollectionId", Wbxml.Tasks);
		serializer.integerElement("ItemEstimate", "Estimate", 0);
		serializer.closeTag();
		
		serializer.closeTag();
		serializer.closeTag();
	}

	public void updateDate(User user, String deviceId, String deviceType) {
	}
}
