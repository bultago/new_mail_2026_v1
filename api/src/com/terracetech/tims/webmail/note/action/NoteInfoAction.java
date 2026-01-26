package com.terracetech.tims.webmail.note.action;

import java.util.Map;

import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.note.manager.NoteManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class NoteInfoAction extends BaseAction {
	
	private NoteManager noteManager = null;
	private SettingManager userSettingManager = null;
	
	public void setNoteManager(NoteManager noteManager) {
		this.noteManager = noteManager;
	}
	
	public void setUserSettingManager(SettingManager userSettingManager) {
		this.userSettingManager = userSettingManager;
	}
	
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		I18nResources msgResource = getMessageResource("common");
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		Map<String, String> confMap = noteManager.getNoteConnectInfo(user);
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		JSONObject jObj = new JSONObject();
		boolean isSuccess = true;
		try {
			store = factory.connect(remoteIp, confMap);
			noteManager.noteInit(store, msgResource);
			
			UserEtcInfoVO userSettingVo = userSettingManager.readUserEtcInfo(mailUserSeq);
			
			jObj.put("unSeenCount", noteManager.readNoteUnseenCount());
			jObj.put("alrimInteval",(userSettingVo != null)?userSettingVo.getNotiInterval():"0");
		}catch (Exception e) {
			isSuccess = false;
		}finally {
			if (store != null && store.isConnected()) {
				store.close();
			}
		}
		jObj.put("isSuccess", isSuccess);
		
		ResponseUtil.processResponse(response, jObj);
		return null;
	}
}
