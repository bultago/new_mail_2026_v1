package com.terracetech.tims.service.aync.command;

import java.io.IOException;
import java.util.List;

import com.terracetech.tims.service.aync.util.WbxmlException;
import com.terracetech.tims.service.aync.util.WbxmlSerializer;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mobile.manager.MobileSyncManager;
import com.terracetech.tims.webmail.mobile.vo.MobileSyncLogVO;
import com.terracetech.tims.webmail.mobile.vo.MobileSyncVO;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.DateUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class ContactsServerDelCommand implements ICommand{
	
	private User user = null;
	
	private MobileSyncVO syncVo = null;
	
	private List<AddressBookMemberVO> memberList= null;
	
	private AddressBookManager manager = null;
	
	private MobileSyncManager syncManager = null;
	
	public ContactsServerDelCommand(User user, MobileSyncVO syncVo) {
		this.user = user;
		this.syncVo = syncVo;
		
		manager = (AddressBookManager) ApplicationBeanUtil.getApplicationBean("addressBookManager");
		syncManager = (MobileSyncManager) ApplicationBeanUtil.getApplicationBean("mobileSyncManager");
	}

	public void process(String deviceId, int windowSize) {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		memberList = manager.readDelContactsByDate(userSeq, syncVo.getContactsUpdateSyncTime(), 100);
		updateContactsSyncDate(deviceId);
	}
	
	private void updateContactsSyncDate(String deviceId){
		if(memberList==null || memberList.size()==0){
			syncManager.updateContactsDeleteSyncDate(user, deviceId, DateUtil.getFullDateStr());
			
			return;
		}
		
		AddressBookMemberVO member = memberList.get(memberList.size()-1);
		if(StringUtils.isNotEmpty(member.getDelTime())){
			syncManager.updateContactsDeleteSyncDate(user, deviceId, member.getDelTime());	
		}else{
			syncManager.updateContactsDeleteSyncDate(user, deviceId, DateUtil.getFullDateStr());
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
				serializer.openTag("AirSync", "Delete");
				serializer.textElement("AirSync", "ServerId", String.valueOf(member.getMemberSeq()));

				serializer.closeTag();	
			}
		}
	}
	
}
