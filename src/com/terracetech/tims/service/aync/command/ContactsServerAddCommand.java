package com.terracetech.tims.service.aync.command;

import java.io.IOException;
import java.util.List;

import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mobile.manager.MobileSyncManager;
import com.terracetech.tims.webmail.mobile.vo.MobileSyncVO;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.DateUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class ContactsServerAddCommand implements ICommand{
	
	private User user = null;
	
	private MobileSyncVO syncVo = null;
	
	private List<AddressBookMemberVO> memberList= null;
	
	private AddressBookManager manager = null;
	
	private MobileSyncManager syncManager = null;
	
	public ContactsServerAddCommand(User user, MobileSyncVO syncVo) {
		this.user = user;
		this.syncVo = syncVo;
		
		manager = (AddressBookManager) ApplicationBeanUtil.getApplicationBean("addressBookManager");
		syncManager = (MobileSyncManager) ApplicationBeanUtil.getApplicationBean("mobileSyncManager");
	}

	public void process(String deviceId, int windowSize) {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		memberList = manager.readAddContactsByDate(userSeq, syncVo.getContactsInsertSyncTime(), windowSize);
		updateContactsSyncDate(deviceId);
	}
	
	private void updateContactsSyncDate(String deviceId){
		if(memberList==null || memberList.size()==0){
			syncManager.updateContactsInsertSyncDate(user, deviceId, DateUtil.getFullDateStr());
			return;
		}
		
		AddressBookMemberVO member = memberList.get(memberList.size()-1);
		if(StringUtils.isNotEmpty(member.getAddTime())){
			syncManager.updateContactsInsertSyncDate(user, deviceId, member.getAddTime());	
		}else{
			//사용자를 가져갔는데, 추가한 날짜가 없는 경우
			syncManager.updateContactsInsertSyncDate(user, deviceId, DateUtil.getFullDateStr());
		}
	}
	
	public int countSyncData(){
		if(memberList==null)
			return 0;
		
		return memberList.size();
	}
	
	public void encodeResponse(WbxmlSerializer serializer) throws IOException, WbxmlException {
		if(memberList != null){
			
			for (AddressBookMemberVO member : memberList) {
				serializer.openTag("AirSync", "Add");		
				serializer.textElement("AirSync", "ServerId", String.valueOf(member.getMemberSeq()));
				serializer.openTag("AirSync", "ApplicationData");
				
				serializer.textElement("Contacts", "FileAs", StringUtils.getValue(member.getMemberName(), ""));
				serializer.textElement("Contacts", "FirstName", StringUtils.getValue(member.getFirstName(), ""));
				serializer.textElement("Contacts", "LastName", StringUtils.getValue(member.getLastName(), ""));
				serializer.textElement("Contacts", "Email1Address", StringUtils.getValue(member.getMemberEmail(), ""));
				serializer.textElement("Contacts", "CompanyName", StringUtils.getValue(member.getCompanyName(), ""));
				serializer.textElement("Contacts", "Department", StringUtils.getValue(member.getDepartmentName(), ""));
				serializer.textElement("Contacts", "MobilePhoneNumber", StringUtils.getValue(member.getMobileNo(), ""));
				
				serializer.textElement("Contacts", "HomeCountry", StringUtils.getValue(member.getHomeCountry(), ""));
				serializer.textElement("Contacts", "HomeCity", StringUtils.getValue(member.getHomeCity(), ""));
				serializer.textElement("Contacts", "HomeState", StringUtils.getValue(member.getHomeState(), ""));
				serializer.textElement("Contacts", "HomeStreet", StringUtils.getValue(member.getHomeStreet(), ""));
				serializer.textElement("Contacts", "HomePostalCode", StringUtils.getValue(member.getHomePostalCode(), ""));
				
				serializer.textElement("Contacts", "BusinessCountry", StringUtils.getValue(member.getOfficeCountry(), ""));
				serializer.textElement("Contacts", "BusinessCity", StringUtils.getValue(member.getOfficeCity(), ""));
				serializer.textElement("Contacts", "BusinessState", StringUtils.getValue(member.getOfficeState(), ""));
				serializer.textElement("Contacts", "BusinessStreet", StringUtils.getValue(member.getOfficeStreet(), ""));
				serializer.textElement("Contacts", "BusinessPostalCode", StringUtils.getValue(member.getOfficePostalCode(), ""));
				serializer.textElement("Contacts", "BusinessPhoneNumber", StringUtils.getValue(member.getOfficeTel(), ""));
				serializer.textElement("Contacts", "BusinessFaxNumber", StringUtils.getValue(member.getOfficeFax(), ""));
				
				serializer.closeTag();
				serializer.closeTag();
			}
		}
	}
	
}
