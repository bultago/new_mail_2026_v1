package com.terracetech.tims.webmail.note.action;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.exception.OverCountException;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.note.manager.NoteHandler;
import com.terracetech.tims.webmail.note.manager.NoteManager;
import com.terracetech.tims.webmail.note.vo.NotePolicyCondVO;
import com.terracetech.tims.webmail.note.vo.NotePolicyVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class NoteWorkAction extends BaseAction {
	
	private NoteManager noteManager = null;
	private MailUserManager mailUserManager = null;
	
	private String folderName = null;
	private String[] uids = null;
	private String[] emails = null;

	public void setNoteManager(NoteManager noteManager) {
		this.noteManager = noteManager;
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}

	public String execute() throws Exception {
		
		I18nResources msgResource = getMessageResource("common");
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		JSONObject result = new JSONObject();
		boolean isSuccess = true;
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int mailGroupSeq = Integer.parseInt(user.get(User.MAIL_GROUP_SEQ));
		String msg = "";
		int noteSaveCount = 0;
		try {
			
			Map<String, String> confMap = noteManager.getNoteConnectInfo(user);
			
			folderName = (StringUtils.isEmpty(folderName)) ? NoteHandler.INBOX : folderName;
			
			if (uids == null || uids.length == 0) {
				throw new InvalidParameterException();
			}
			
			int uidsLength = uids.length;
			
			long[] longUids = new long[uidsLength];
			for (int i=0; i<uidsLength; i++) {
				longUids[i] = Long.parseLong(uids[i]);
			}
			
			store = factory.connect(remoteIp, confMap);
			noteManager.noteInit(store, msgResource);
			
			TMailFolder folder = noteManager.getFolder(NoteHandler.SAVE);
			int currentMessageCount = folder.getMessageCount();
			
			noteSaveCount = noteManager.readNoteSaveCount(mailDomainSeq, mailGroupSeq, mailUserSeq);
			
			if (uidsLength+currentMessageCount > noteSaveCount) {
				throw new OverCountException();
			}

			noteManager.moveMessageToFolder(longUids, folderName, NoteHandler.SAVE);
		}
		catch (OverCountException e) {
			isSuccess = false;
			msg = msgResource.getMessage("note.msg.048", new Object[]{noteSaveCount});
		}
		catch (Exception e) {
			isSuccess = false;
			msg = msgResource.getMessage("error.msg.001");
		}finally {
			if (store != null && store.isConnected()) {
				store.close();
			}
		}
		
		result.put("isSuccess", isSuccess);
		result.put("msg", msg);
		
		ResponseUtil.processResponse(response, result);
		
		return null;
	}
	
	public String deleteNote() throws Exception {
		
		I18nResources msgResource = getMessageResource("common");
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		JSONObject result = new JSONObject();
		boolean isSuccess = true;
		
		try {
			
			Map<String, String> confMap = noteManager.getNoteConnectInfo(user);
			
			folderName = (StringUtils.isEmpty(folderName)) ? NoteHandler.INBOX : folderName;
			
			if (uids == null || uids.length == 0) {
				throw new InvalidParameterException();
			}
			
			long[] longUids = new long[uids.length];
			for (int i=0; i<uids.length; i++) {
				longUids[i] = Long.parseLong(uids[i]);
			}
			
			store = factory.connect(remoteIp, confMap);
			noteManager.noteInit(store, msgResource);

			TMailFolder folder = noteManager.getFolder(folderName);
			noteManager.deleteNoteMessage(longUids, folder);
		}catch (Exception e) {
			isSuccess = false;
		}finally {
			if (store != null && store.isConnected()) {
				store.close();
			}
		}
		
		result.put("isSuccess", isSuccess);
		
		ResponseUtil.processResponse(response, result);
		
		return null;
	}
	
	public String rejectNote() throws Exception {
		
		JSONObject result = new JSONObject();
		boolean isSuccess = true;
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String msg = "";
		try {

			if (emails == null || emails.length == 0) {
				throw new InvalidParameterException();
			}
			
			String email = null;
			String userId = null;
			String domain = null;
			int userSeq = 0;
			Map<String, Integer> userSeqMap = new HashMap<String, Integer>();
			String userSeqStr = null;
			for (int i=0; i<emails.length; i++) {
				email = emails[i];
				userId = TMailAddress.getId(email);
				domain = TMailAddress.getDomain(email);
				domain = domain.replace("@", "");
				
				userSeq = mailUserManager.readUserSeq(userId, domain);
				
				if (userSeq <= 0) {
					msg = email +"\n";
					continue;
				}
				
				userSeqStr = Integer.toString(userSeq);
				if (!userSeqMap.containsKey(userSeqStr)) {
					userSeqMap.put(userSeqStr, userSeq);
				}
			}
			
			if (!userSeqMap.isEmpty()) {
				NotePolicyVO notePolicyVo = new NotePolicyVO();
				notePolicyVo.setMailUserSeq(mailUserSeq);
				notePolicyVo.setPolicyType("blackOnly");
				
				NotePolicyVO beforeNotePolicyVo = noteManager.readNotePolicy(mailUserSeq);
				if (beforeNotePolicyVo != null) {
					noteManager.modifyNotePolicy(notePolicyVo);
				} else {
					noteManager.saveNotePolicy(notePolicyVo);
				}
				
				List<NotePolicyCondVO> noteCondResultList = noteManager.readNotePolicyCondList(mailUserSeq);
				if ("blackOnly".equalsIgnoreCase(beforeNotePolicyVo.getPolicyType()) && noteCondResultList.size() > 0) {
					String beforeUserSeqStr = null;
					for (NotePolicyCondVO noteCondResult : noteCondResultList) {
						beforeUserSeqStr = Integer.toString(noteCondResult.getCondTarget());
						if (!userSeqMap.containsKey(beforeUserSeqStr)) {
							userSeqMap.put(beforeUserSeqStr, noteCondResult.getCondTarget());
						}
					}
				}
				
				Integer[] userSeqList = userSeqMap.values().toArray(new Integer[userSeqMap.size()]);
				noteManager.deleteNotePolicyCond(mailUserSeq);
				NotePolicyCondVO notePolicyCondVo = null;
				for (int i=0; i <userSeqList.length; i++) {
					notePolicyCondVo = new NotePolicyCondVO();
					notePolicyCondVo.setMailUserSeq(mailUserSeq);
					notePolicyCondVo.setCondTarget(userSeqList[i]);
					noteManager.saveNotePolicyCond(notePolicyCondVo);
				}
			}
		}catch (Exception e) {
			isSuccess = false;
		}
		
		result.put("isSuccess", isSuccess);
		result.put("msg", StringEscapeUtils.escapeHtml(msg));
		
		ResponseUtil.processResponse(response, result);
		
		return null;
	}

	public void setUids(String[] uids) {
		this.uids = uids;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public void setEmails(String[] emails) {
		this.emails = emails;
	}
}
