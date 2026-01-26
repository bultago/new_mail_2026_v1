package com.terracetech.tims.webmail.note.action;

import java.util.Map;

import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.exception.OverCountException;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.note.manager.NoteHandler;
import com.terracetech.tims.webmail.note.manager.NoteManager;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class NoteAllSelectAction extends BaseAction {
	
	private NoteManager noteManager = null;
	
	private String folderName = null;
	private String flag = null;
	private String keyWord = null;
	private String type = null;

	public void setNoteManager(NoteManager noteManager) {
		this.noteManager = noteManager;
	}
	
	public String execute() throws Exception {
		I18nResources msgResource = getMessageResource("common");
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		Map<String, String> confMap = noteManager.getNoteConnectInfo(user);
		boolean isSuccess = true;
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int mailGroupSeq = Integer.parseInt(user.get(User.MAIL_GROUP_SEQ));
		
		String page = "0";
		String pageBase = "0";
		
		if (StringUtils.isEmpty(folderName)) {
			folderName = NoteHandler.INBOX;
		}
		
		char flagType = (flag != null && flag.length() > 0)?flag.charAt(0):'x';
		MailSortMessageBean[] messageBeans = null;
		int noteSaveCount = 0;
		String msg = "";
		try {
			MessageSortInfoBean sortBean = new MessageSortInfoBean();
			
			sortBean.setSortBy("arrival");
			sortBean.setSortDir("desc");

			sortBean.setAdSearchCategory("b");
			sortBean.setAdvanceMode(true);
			sortBean.setOperation("or");
			sortBean.setAdFromEmailPattern(keyWord);
			sortBean.setAdToEmailPattern(keyWord);
			sortBean.setAdSearchPattern(keyWord);
			
			sortBean.setSearchFlag(flagType);
			
			sortBean.setPage(page);
			sortBean.setPageBase(pageBase);
			
			store = factory.connect(remoteIp, confMap);
			noteManager.noteInit(store, msgResource);
			messageBeans = noteManager.getXSortMessageBeans(folderName, sortBean);
			
			int noteSize = messageBeans.length;
			long[] noteUids = new long[noteSize];
			
			for (int i = 0; i < noteSize; i++) {
				noteUids[i] = messageBeans[i].getId();			
			}
			
			TMailFolder folder = noteManager.getFolder(folderName);
			if ("delete".equalsIgnoreCase(type)) {
				noteManager.deleteNoteMessage(noteUids, folder);
			} else if ("save".equalsIgnoreCase(type)) {
				noteSaveCount = noteManager.readNoteSaveCount(mailDomainSeq, mailGroupSeq, mailUserSeq);
				TMailFolder saveFolder = noteManager.getFolder(NoteHandler.SAVE);
				int currentMessageCount = saveFolder.getMessageCount();
				if (noteSize+currentMessageCount > noteSaveCount) {
					throw new OverCountException();
				}
				noteManager.moveMessageToFolder(noteUids, folderName, NoteHandler.SAVE);
			}
		} 
		catch (OverCountException e) {
			isSuccess = false;
			msg = msgResource.getMessage("note.msg.048", new Object[]{noteSaveCount});
		}
		catch (Exception e) {
			isSuccess = false;
		} finally {
			if (store != null && store.isConnected()) {
				store.close();
			}
		}

		JSONObject result = new JSONObject();
		result.put("isSuccess", isSuccess);
		result.put("msg", msg);
		ResponseUtil.processResponse(response, result);
		
		return null;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public void setType(String type) {
		this.type = type;
	}
}
