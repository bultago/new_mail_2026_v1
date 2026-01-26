package com.terracetech.tims.webmail.note.action;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.exception.InvalidParameterException;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.note.manager.NoteHandler;
import com.terracetech.tims.webmail.note.manager.NoteManager;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class NoteMdnAction extends BaseAction {

	private String[] uids = null;
	private String messageId = null;
	private String email = null;
	
	private NoteManager noteManager = null;
	private MailUserManager mailUserManager = null;

	public void setNoteManager(NoteManager noteManager) {
		this.noteManager = noteManager;
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}

	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		I18nResources msgResource = getMessageResource("common");
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		Map<String, String> confMap = noteManager.getNoteConnectInfo(user);
		boolean isSuccess = true;
		JSONObject result = new JSONObject();
		JSONArray mdnArray = new JSONArray();
		try {
			
			if (uids == null) {
				throw new InvalidParameterException();
			}
			
			long[] longUids = new long[uids.length];
			for (int i=0; i<uids.length; i++) {
				longUids[i] = Long.parseLong(uids[i]);
			}
			
			store = factory.connect(remoteIp, confMap);
			noteManager.noteInit(store, msgResource);
			
			List<Map<String, Object>> mdnList = noteManager.getMdnMessageList(longUids);
			
			if (mdnList != null) {
				
				JSONObject mdnInfo = null;
				for (Map<String, Object> mdnObj : mdnList) {
					mdnInfo = new JSONObject();
					mdnInfo.put("messageId", mdnObj.get("messageId"));
					mdnInfo.put("uid", mdnObj.get("uid"));
					mdnInfo.put("code", mdnObj.get("code"));
					mdnInfo.put("date", mdnObj.get("date"));
					mdnArray.add(mdnInfo);
				}
			}

		} catch (Exception e) {
			isSuccess = false;
			LogManager.writeErr(this, e.getMessage(), e);
		} finally {
			if (store != null && store.isConnected()) {
				store.close();
			}
		}
		
		result.put("isSuccess", isSuccess);
		result.put("mdnList", mdnArray);
		
		ResponseUtil.processResponse(response, result);
		
		return null;
	}
	
	public String recallMdn() throws Exception {
		
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore myStore = null;
		TMailStore recallStore = null;
		
		JSONObject result = new JSONObject();
		boolean isSuccess = true;
		
		try {
			if (email == null || email.indexOf('@') == -1) {
				throw new InvalidParameterException();
			}
			
			if (StringUtils.isEmpty(messageId)) {
				throw new InvalidParameterException();
			}
			
			email = TMailAddress.getEmailAddress(email);
			
			String[] parts = email.split("@");
			
			int userSeq = mailUserManager.readUserSeq(parts[0], parts[1]);
			if (userSeq < 1) {
				throw new InvalidParameterException();
			}
			
			int domainSeq = mailUserManager.searchMailDomainSeq(parts[1]);
			
			Map<String, String> connectMap = noteManager.getNoteConnectInfo(userSeq, domainSeq);
			recallStore = factory.connect(remoteIp, connectMap);
			TMailFolder folder = recallStore.getFolder(NoteHandler.INBOX);
			folder.xrecall(messageId);
			
			myStore = factory.connect(remoteIp, noteManager.getNoteConnectInfo(user));
			TMailFolder myFolder = myStore.getFolder(NoteHandler.SENT);
			long time = System.currentTimeMillis() / 1000;
			myFolder.xsetMDN(messageId, email, "", time, "1");
			
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isSuccess = false;
		}finally {
			if (myStore != null && myStore.isConnected()) {
				myStore.close();
			}
			if (recallStore != null && recallStore.isConnected()) {
				recallStore.close();
			}
		}
		
		result.put("isSuccess", isSuccess);
		ResponseUtil.processResponse(response, result);
		
		return null;
	}

	public void setUids(String[] uids) {
		this.uids = uids;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
