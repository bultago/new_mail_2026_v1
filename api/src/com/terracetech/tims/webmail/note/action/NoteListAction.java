package com.terracetech.tims.webmail.note.action;

import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.note.manager.NoteHandler;
import com.terracetech.tims.webmail.note.manager.NoteManager;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class NoteListAction extends BaseAction {

	private NoteManager noteManager = null;
	private String folderName = null;
	private String flag = null;
	private String keyWord = null;
	private int page = 0;

	public void setNoteManager(NoteManager noteManager) {
		this.noteManager = noteManager;
	}
	
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		I18nResources msgResource = getMessageResource("common");
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		Map<String, String> confMap = noteManager.getNoteConnectInfo(user);
		
		String pageBase = user.get(User.PAGE_LINE_CNT);
		if (StringUtils.isEmpty(pageBase)) {
			pageBase = "15";
		}
		
		if (StringUtils.isEmpty(folderName)) {
			folderName = NoteHandler.INBOX;
		}
		
		char flagType = (flag != null && flag.length() > 0)?flag.charAt(0):'x';
		
		if (page == 0) {
			page = 1;
		}
		
		int total = 0;
		int messageCount = 0;
		int unseenCount = 0;
		String folderAliasName = "";
		MailSortMessageBean[] messageBeans = null;
		try {
			MessageSortInfoBean sortBean = new MessageSortInfoBean();
			
			sortBean.setUserEmail(user.get(User.EMAIL));
			sortBean.setSortBy("arrival");
			sortBean.setSortDir("desc");
			
			sortBean.setAdSearchCategory("b");
			sortBean.setAdvanceMode(true);
			sortBean.setOperation("or");
			sortBean.setAdFromEmailPattern(keyWord);
			sortBean.setAdToEmailPattern(keyWord);
			sortBean.setAdSearchPattern(keyWord);
			
			sortBean.setIsNote(true);
			
			sortBean.setSearchFlag(flagType);
			
			
			sortBean.setPage(Integer.toString(page));
			sortBean.setPageBase(pageBase);	
			
			store = factory.connect(remoteIp, confMap);
			noteManager.noteInit(store, msgResource);
			messageBeans = noteManager.getXSortMessageBeans(folderName, sortBean);
			total = noteManager.getXCommandTotal();
			
			if(messageBeans == null && total > 0){
				int npages = (int) Math.ceil((double) total / Integer.parseInt(pageBase));
				if (0 < npages && npages < page) {
					page = npages;				
				}
				sortBean.setPage(Integer.toString(page));
				messageBeans = noteManager.getXSortMessageBeans(folderName, sortBean);
			}
			
			TMailFolder folder = noteManager.getFolder(folderName);
			messageCount = folder.getMessageCount();
			unseenCount = folder.getUnreadMessageCount();
			folderAliasName = folder.getAlias();
		
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}finally {
			if (store != null && store.isConnected()) {
				store.close();
			}
		}
		
		JSONArray messageList = new JSONArray();
		if(messageBeans != null){
			for (int i = 0; i < messageBeans.length; i++) {
				messageList.add(messageBeans[i].toJson(null));
			}
		}
		
		JSONObject messageListinfo = new JSONObject();
		messageListinfo.put("messageList", messageList);
		messageListinfo.put("folderName", StringEscapeUtils.escapeHtml(folderName));
		messageListinfo.put("folderAliasName", folderAliasName);
		messageListinfo.put("messageCount", messageCount);
		messageListinfo.put("unseenCount", unseenCount);
		messageListinfo.put("page", page);
		messageListinfo.put("pageBase", pageBase);
		messageListinfo.put("total", total);
		
		ResponseUtil.processResponse(response, messageListinfo);
		
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

	public void setPage(int page) {
		this.page = page;
	}
	
}
