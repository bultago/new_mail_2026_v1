package com.terracetech.tims.webmail.note.action;

import java.security.InvalidParameterException;
import java.util.Locale;
import java.util.Map;

import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMDNResponse;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.note.manager.NoteHandler;
import com.terracetech.tims.webmail.note.manager.NoteManager;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class NoteReadAction extends BaseAction {
	
	private NoteManager noteManager = null;
	
	private String folderName = null;
	private String uid = null;
	private String flag = null;
	private String keyWord = null;
	
	public void setNoteManager(NoteManager noteManager) {
		this.noteManager = noteManager;
	}

	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		
		I18nResources msgResource = getMessageResource("common");
		Locale locale = I18nConstants.getUserLocale(request);
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		JSONObject result = new JSONObject();
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int mailGroupSeq = Integer.parseInt(user.get(User.MAIL_GROUP_SEQ));
		
		char flagType = (flag != null && flag.length() > 0)?flag.charAt(0):'x';
		folderName = StringUtils.isEmpty(folderName) ? NoteHandler.INBOX : folderName;
		long preUid = 0;
		long nextUid = 0;
		MailMessageBean messageBean = null;
		String htmlContent = "";
		int maxSaveCount = 0;
		int maxSaveDate = 0;
		int messageCount = 0;
		int unseenCount = 0;
		String folderAliasName = "";
		try {
			if (StringUtils.isEmpty(uid)) {
				throw new InvalidParameterException();
			}
			
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
			
			sortBean.setSearchFlag(flagType);
			
			maxSaveCount = noteManager.readNoteSaveCount(mailDomainSeq, mailGroupSeq, mailUserSeq);
			maxSaveDate = noteManager.readNoteSaveDate(mailDomainSeq, mailGroupSeq, mailUserSeq);
			
			Map<String, String> confMap = noteManager.getNoteConnectInfo(user);
			store = factory.connect(remoteIp, confMap);
			noteManager.noteInit(store, msgResource);
			
			TMailFolder folder = noteManager.getFolder(folderName);
			folder.open(true);
			
			long[] neighborUID = noteManager.getNeighborUIDs(Long.parseLong(uid), folder, sortBean);
			preUid = neighborUID[0];
			nextUid = neighborUID[1];
			
			MessageParserInfoBean parserInfo = getMessagePaserInfoBean();
			parserInfo.setLocale(locale);

			messageBean = noteManager.readNoteMessage(folder, Long.parseLong(uid), parserInfo);
			
			TMailPart[] htmlContentPart = messageBean.getBodyContent();
			htmlContent = (htmlContentPart != null && htmlContentPart.length > 0)?
					StringUtils.getCRLFEscape(htmlContentPart[0].getText()):"";
					
			htmlContent = htmlContent.replaceAll("\n", "<br/>");
			
			messageCount = folder.getMessageCount();
			unseenCount = folder.getUnreadMessageCount();
			folderAliasName = folder.getAlias();
			
			folder.close(true);
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}finally {
			if (store != null && store.isConnected()) {
				store.close();
			}
		}
		
		TMailMessage message = messageBean.getMessage();
		TMailMDNResponse[] mdnResponses = message.getMDNResponses();
		String mdnCode = "";
		if (mdnResponses != null && mdnResponses.length > 0) {
			mdnCode = mdnResponses[0].getCode();
		}
		
		String messageId = message.getMessageID();
		messageId = messageId.replaceAll("<", "");
		messageId = messageId.replaceAll(">", "");
		
		request.setAttribute("preUid", preUid);
		request.setAttribute("nextUid", nextUid);
		request.setAttribute("message",message);
		request.setAttribute("messageId",messageId);
		request.setAttribute("mdnCode",mdnCode);
		request.setAttribute("htmlContent",htmlContent);
		request.setAttribute("folderName",folderName);
		request.setAttribute("maxSaveCount",maxSaveCount);
		request.setAttribute("maxSaveDate",maxSaveDate);
		request.setAttribute("messageCount", messageCount);
		request.setAttribute("unseenCount", unseenCount);
		request.setAttribute("folderAliasName", folderAliasName);
		request.setAttribute("keyWord", keyWord);
		request.setAttribute("flag", flag);
		request.setAttribute("escapeFrom",StringUtils.EscapeMailParam(TMailAddress.getAddressString(message.getFrom())));
		request.setAttribute("escapeTo",StringUtils.EscapeMailParam(TMailAddress.getAddressString(message.getTo())));
		
		return "success";
	}
	
	private MessageParserInfoBean getMessagePaserInfoBean() {
		String resizeChk = EnvConstants.getBasicSetting("inline.image.resizing");
		String resizeWStr = EnvConstants.getBasicSetting("inline.image.limit.width");
		boolean isImgResize = Boolean.parseBoolean((resizeChk != null)?resizeChk:"false");
		int resizeWidth = Integer.parseInt((resizeWStr != null)?resizeWStr:"650");
		
		String attachesDir = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
		String hostStr = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort(); 
		MessageParserInfoBean infoBean = new MessageParserInfoBean();
		infoBean.setAttachesDir(attachesDir);
		infoBean.setAttachesUrl(EnvConstants.getAttachSetting("attach.url"));
		infoBean.setDefaultCharset("UTF-8");
		infoBean.setDefaultImg("/design/common/images/blank.gif");		
		infoBean.setStrLocalhost(hostStr);
		infoBean.setUserId(user.get(User.MAIL_UID));
		infoBean.setImgResize(isImgResize);
		infoBean.setImgResizeWidth(resizeWidth);
		return infoBean;
	}	

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
}
