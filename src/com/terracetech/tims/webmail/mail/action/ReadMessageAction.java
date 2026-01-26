package com.terracetech.tims.webmail.mail.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Stack;

import jakarta.mail.MessagingException;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.maxmind.geoip2.record.Country;
import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.ladmin.Protocol;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.log.PerformanceLogManager;
import com.terracetech.tims.webmail.common.manager.GeoIpManager;
import com.terracetech.tims.webmail.common.manager.LadminManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.exception.MailNotFoundException;
import com.terracetech.tims.webmail.mail.ibean.MailMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.IPUtils;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import com.terracetech.tims.webmail.util.StringUtils;

@SuppressWarnings("unchecked")
public class ReadMessageAction extends BaseAction {

	private static final long serialVersionUID = 20081215L;
	
	public Logger log = LoggerFactory.getLogger(this.getClass());
	
	private MailManager mailManager = null;
	private SettingManager userSettingManager = null;
	private SystemConfigManager systemConfigManager = null;
	private LadminManager ladminManager = null;
	private GeoIpManager geoIpManager = null;
	
	private TMailMessage message;
	
	private String htmlContent;
	
	private TMailPart[] files;
	private TMailPart[] vcards;
	private String[] imageAttach;
	private long preUid = -1L;
	private long nextUid = -1L;
	
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	public void setUserSettingManager(SettingManager userSettingManager) {
		this.userSettingManager = userSettingManager;
	}
	
	public void setSystemConfigManager(SystemConfigManager systemConfigManager) {
		this.systemConfigManager = systemConfigManager;
	}
	
	public void setLadminManager(LadminManager ladminManager) {
		this.ladminManager = ladminManager;
	}

	public void setGeoIpManager(GeoIpManager geoIpManager) {
		this.geoIpManager = geoIpManager;
	}

	public TMailMessage getMessage() {
		return message;
	}	

	public TMailPart[] getFiles() {
		return files;
	}
	
	public TMailPart[] getVcards() {
		return vcards;
	}
	
	public String[] getImageAttach() {
		return imageAttach;
	}
	
	public int getFilesLength() {
		int fileSize = 0;		
		
		if(files != null){
			fileSize += files.length;
		}
		
		if(vcards != null){
			fileSize += vcards.length;
		}
		
		return fileSize;
	}

	public String getHtmlContent() {
		return htmlContent;
	}
	
	public long getPreUid(){
		return preUid;
	}
	
	public long getNextUid(){
		return nextUid;
	}

	public String execute() throws Exception{
		
		String processMethod = request.getParameter("method");
		boolean isAjax = (processMethod != null && processMethod.equals("ajax"))?true:false;
		
		Object wdebug = context.getAttribute("PDEBUG");
		boolean isWDebug = false;
		StringBuffer debugStr = null;
		long dstime = 0;
		long detime = 0;
		Date dwbugDate = null;
		SimpleDateFormat dsdf = null;
		String agent = null;
		if(wdebug != null){
			isWDebug = "enable".equals((String)wdebug);
		}
		
		if(isWDebug && isAjax){
			agent = request.getHeader("user-agent").toUpperCase();
			dsdf = new SimpleDateFormat("mm:ss:SS");
			debugStr = new StringBuffer();
			dwbugDate = new Date();
			dstime = dwbugDate.getTime();
			debugStr.append("SERVER_START,");
			debugStr.append("NTIME,");
			debugStr.append(dsdf.format(dwbugDate));
		}
		
		I18nResources msgResource = getMessageResource();
		Locale locale = I18nConstants.getUserLocale(request);
		
		String userId = user.get(User.MAIL_UID);
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));		
		
		String sharedFlag = request.getParameter("sharedFlag");
		boolean isShared = (sharedFlag != null && sharedFlag.equals("shared"))?true:false;
		String sharedUserSeq = request.getParameter("sharedUserSeq");
		String sharedFolderName = request.getParameter("sharedFolderName");		
		
		String readType = request.getParameter("readType");
		String viewImg = request.getParameter("viewImg");
		String uid = request.getParameter("uid");
		String folder = request.getParameter("folder");
		String page = request.getParameter("page");
		
		String keyWord = request.getParameter("keyWord");
		String advancedSearch = request.getParameter("adv");
		String category = request.getParameter("category");		
		String flag = request.getParameter("flag");
		String sortBy = request.getParameter("sortBy");
		String sortDir = request.getParameter("sortDir");
		
		String spamAdmId = EnvConstants.getBasicSetting("spam.admin");
		String hamAdmId = EnvConstants.getBasicSetting("white.admin");
		boolean isError = false;
		String errorMsg = null;
		
		boolean isRuleAdmin = false;
		if(spamAdmId.equals(userId) || hamAdmId.equals(userId)){
			isRuleAdmin = true;
		}
		sharedFolderName  =	StringEscapeUtils.unescapeHtml(sharedFolderName);
		/*if (request.getMethod().equalsIgnoreCase("get")) {
			folder = StringUtils.getDecodingUTF(folder);
			sharedFolderName = StringUtils.getDecodingUTF(sharedFolderName);
			keyWord = StringUtils.getDecodingUTF(keyWord);
		}*/
		
		char flagType = (flag != null && flag.length() > 0)?flag.charAt(0):'x';
		boolean isPopupRead = (readType != null && (readType.equals("pop") || readType.equals("print")))?true:false;
		boolean isPrintRead = (readType != null && (readType.equals("print")))?true:false;
		
		if (StringUtils.isEmpty(folder))
			folder = FolderHandler.INBOX;
		
		if (StringUtils.isEmpty(sortBy))
			sortBy = "arrival";
		
		if (StringUtils.isEmpty(sortDir))
			sortDir = "desc";
		
		if(isShared){
			folder = sharedFolderName;			
		} else {
			sharedFlag = "user";
		}
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder ufolder = null;
		Protocol ladminProtocol = null;
		String mesageIntegrity = null; 
		boolean isHiddenImg = false;
		String folderType = null;
		float spamRateValue = 0;
		MailMessageBean messageBean = null;
		try {
			//User user = EnvConstants.getTestUser();			
			if(uid == null)throw new Exception("USER["+userId+"] ReadMessage UID is null!");
			if(folder == null)throw new Exception("USER["+userId+"] ReadMessage FolderName is null!");
			
			store = factory.connect(isShared, sharedUserSeq, request.getRemoteAddr(), user);			
			
			MessageSortInfoBean sortBean = new MessageSortInfoBean();
			sortBean.setSortBy(sortBy);
			sortBean.setSortDir(sortDir);
			if (StringUtils.isNotEmpty(advancedSearch)) {
				sortBean.setAdSearchCategory(category);
				sortBean.setAdvanceMode(true);
				sortBean.setAdSearchPattern(keyWord);
			} else {
				sortBean.setPattern(keyWord);				
			}	
			
			sortBean.setSearchFlag(flagType);		
			
			mailManager.setProcessResource(store, getMessageResource());
			ufolder = store.getFolder(folder);
			ufolder.open(true);
			long[] neighborUID = mailManager.getNeighborUID(ufolder, Long.parseLong(uid), sortBean);
			preUid = neighborUID[0];
			nextUid = neighborUID[1];
			
			UserEtcInfoVO userSettingVo = userSettingManager.readUserEtcInfo(userSeq);
			
			mesageIntegrity = systemConfigManager.getMailIntegrityInfo(); 
			isHiddenImg = "on".equalsIgnoreCase(userSettingVo.getHiddenImg());
			
			MessageParserInfoBean parserInfo = getMessagePaserInfoBean();			
			
			if(isPrintRead || "on".equalsIgnoreCase(viewImg)){
				parserInfo.setHiddenImg(false);
				isHiddenImg = false;
			} else {
				parserInfo.setHiddenImg(isHiddenImg);
			}
			
			parserInfo.setHiddenTag("on".equalsIgnoreCase(userSettingVo.getHiddenTag()));
			parserInfo.setLocale(locale);			
			messageBean = mailManager.getMessageBean(ufolder, Long.parseLong(uid),parserInfo);
			
			folderType = "normal";
			if(isShared){
				folderType = "shared";	
			} else {
				if(folder.equals(FolderHandler.INBOX)){
					folderType = "inbox";
				} else if(folder.equals(FolderHandler.SENT)){
					folderType = "sent";
				} else if(folder.equals(FolderHandler.DRAFTS)){
					folderType = "draft";
				} else if(folder.equals(FolderHandler.RESERVED)){
					folderType = "reserved";
				} else if(folder.equals(FolderHandler.SPAM)){
					folderType = "spam";
				} else if(folder.equals(FolderHandler.TRASH)){
					folderType = "trash";
				}
			}
			
			
			
			message = messageBean.getMessage();
			long mailSize = message.getSize();
			TMailPart[] htmlContentPart = messageBean.getBodyContent();
			StringBuffer newtext = new StringBuffer();
			if (htmlContentPart != null && htmlContentPart.length > 0) {
				for (int i=0; i<htmlContentPart.length; i++) {
					/*
					htmlContent = StringUtils.getCRLFEscape(htmlContentPart[i].getText());
					if (StringUtils.isNotEmpty(htmlContent)) {
						break;
					}
					*/
					
					// TCUSTOM-2229 20161020
					if(StringUtils.isNotEmpty(htmlContentPart[i].getText())){
						// TCUSTOM-2018 20161019
						newtext.append(getContentString(htmlContentPart[i]));
						break;
					}
					
				}
				htmlContent = newtext.toString();
			}
			files = messageBean.getAttachContent();
			vcards = messageBean.getVcardContent();
			imageAttach = messageBean.getImageContent();			
			
			if(isRuleAdmin && !isShared &&
					(!folder.equals(FolderHandler.SENT) &&
					!folder.equals(FolderHandler.DRAFTS) &&
					!folder.equals(FolderHandler.RESERVED) &&
					!folder.equals(FolderHandler.QUOTAVIOLATE))){
				
				ladminProtocol = new Protocol(user.get(User.MAIL_HOST));
				ladminManager.setResource(ladminProtocol);
				
				float[] spamRate = ladminManager.getSpamAndWhiteRate(user.get(User.MESSAGE_STORE), 
						folder, new String[]{Long.toString(message.getUid())});			
				
				spamRateValue = spamRate[0];				
				
			}			
			writeMailLog(true,"action_read_message", folder,"", message.getFromAddress(), mailSize, message.getSubject(), message.getMessageID());
			
		}catch(MailNotFoundException e){
			log.warn(e.getMessage());
			isError = true;
			errorMsg = msgResource.getMessage("mail.secure.msg.002");;
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			isError = true;
			errorMsg = e.getMessage();
		}
		String forward = null;
		JSONObject messageReadInfo = new JSONObject();
		
		boolean useGeoIp = "true".equalsIgnoreCase(EnvConstants.getUtilSetting("geoip.db.use"));
		try{
			if(isAjax){
				if(isError){
					messageReadInfo.put("readResult", "fail");
				} else {
					messageReadInfo.put("readResult", "success");
					JSONObject messageObj = messageBean.toJsonForRead();
					messageReadInfo.put("htmlContent", StringUtils.EscapeHTMLTag(htmlContent));			
					messageReadInfo.put("ruleAdmin", isRuleAdmin);
					messageReadInfo.put("readType", readType);
					messageReadInfo.put("hiddenImg", isHiddenImg);
					messageReadInfo.put("integrityUse", mesageIntegrity);
					messageReadInfo.put("sharedFlag", sharedFlag);
					messageReadInfo.put("sharedUserSeq", sharedUserSeq);
					messageReadInfo.put("sharedFolderName", sharedFolderName);
					messageReadInfo.put("folderType", folderType);
					messageReadInfo.put("spamRate", spamRateValue);
					messageReadInfo.put("spamAdmin", spamAdmId.equals(userId));
					messageReadInfo.put("hamAdmin", hamAdmId.equals(userId));
					messageReadInfo.put("preUid", getPreUid());
					messageReadInfo.put("nextUid", getNextUid());
					messageReadInfo.put("fileLength", getFilesLength());
					messageReadInfo.put("msgContent",messageObj);
					messageReadInfo.put("MDNCheck",Boolean.toString(messageBean.isMdnCheck()));
					messageReadInfo.put("useGeoIp", useGeoIp);
				}
				
				ResponseUtil.processResponse(response, messageReadInfo);
				
				if(isWDebug){			
					dwbugDate = new Date();
					detime = dwbugDate.getTime();			
					debugStr.append(",SERVER_END,");			
					debugStr.append("NTIME,");
					debugStr.append(dsdf.format(dwbugDate));
					debugStr.append(",TOTAL,");
					debugStr.append(dsdf.format(new Date(detime - dstime)));
					
					PerformanceLogManager.writeLog(remoteIp, user.get(User.EMAIL), agent, "READ_SERVER_DATA", debugStr.toString());
					
					debugStr = null;
					dstime = 0;
					detime = 0;
					dwbugDate = null;
					dsdf = null;
					agent = null;
				}
				
			} else {
				if(isError){
					if(isPopupRead){
						request.setAttribute("errMsg", errorMsg);
						return "alertclose";
					}else{
						throw new Exception(errorMsg);	
					}
					
				}
				
				if(isPopupRead){
					String localmail = systemConfigManager.getLocalMailConfig();
					boolean isLocalMail = ("enabled".equals(localmail));
					request.setAttribute("localmail", isLocalMail);
				}
			
				
				
				int messageCount = ufolder.getMessageCount();
				int unreadMessageCount = ufolder.getUnreadMessageCount();	
				messageReadInfo.put("folder", folder);
				messageReadInfo.put("keyWord", (keyWord == null) ? "" : keyWord);
				messageReadInfo.put("adv", (advancedSearch == null) ? "" : advancedSearch);
				messageReadInfo.put("category", (category == null) ? "" : category);
				messageReadInfo.put("flag", flag);
				messageReadInfo.put("sharedFlag", (sharedFlag == null) ? "" : sharedFlag);
				messageReadInfo.put("sharedUserSeq", (sharedUserSeq == null) ? "" : sharedUserSeq);
				messageReadInfo.put("sharedFolderName", (sharedFolderName == null) ? "" : sharedFolderName);
				messageReadInfo.put("page", (page == null) ? "" : page);
				messageReadInfo.put("sortBy", sortBy);
				messageReadInfo.put("sortDir", sortDir);
				
				request.setAttribute("to", message.getTo());
				request.setAttribute("cc", message.getCc());
				request.setAttribute("bcc", message.getBcc());	
				request.setAttribute("MDNCheck", messageBean.isMdnCheck());
				request.setAttribute("ruleAdmin", isRuleAdmin);
				request.setAttribute("readType", readType);
				request.setAttribute("hiddenImg", isHiddenImg);
				request.setAttribute("integrityUse", mesageIntegrity);
				request.setAttribute("sharedFlag", sharedFlag);
				request.setAttribute("sharedUserSeq", sharedUserSeq);
				request.setAttribute("sharedFolderName", sharedFolderName);
				request.setAttribute("folderType", folderType);
				request.setAttribute("spamRate", spamRateValue);
				request.setAttribute("spamAdmin", spamAdmId.equals(userId));
				request.setAttribute("hamAdmin", hamAdmId.equals(userId));
				
				request.setAttribute("uid", message.getUid());
				request.setAttribute("folder", message.getFolderEncName());
				request.setAttribute("readParam", messageReadInfo.toString());
				request.setAttribute("messageCount", messageCount);
				request.setAttribute("unreadMessageCount",unreadMessageCount);
				request.setAttribute("folderFullName",message.getFolderFullName());
				request.setAttribute("useGeoIp", useGeoIp);
				
				forward = "normalRead";
				if(isPrintRead){
					forward = "popupPrint";
				} else if(isPopupRead){
					forward = "popupRead";
				}
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if(ufolder !=null && ufolder.isOpen())
				ufolder.close(false);
			if(store !=null && store.isConnected())
				store.close();
		}
		return forward;
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
		infoBean.setDefaultCharset(EnvConstants.getMailSetting("default.charset"));
		infoBean.setDefaultImg("/design/common/images/blank.gif");		
		infoBean.setStrLocalhost(hostStr);
		infoBean.setUserId(user.get(User.MAIL_UID));
		infoBean.setImgResize(isImgResize);
		infoBean.setImgResizeWidth(resizeWidth);
		return infoBean;
	}	
	
	public String executePart() throws Exception{
		return execute();
	}
	
	// TCUSTOM-2418 20161019
	public String getContentString(TMailPart part)
		throws MessagingException, IOException {

			StringBuffer newtext = new StringBuffer();
			String content = part.getText();

			if(part.isMimeType("text/html")){
				StringReplaceUtil sr = new StringReplaceUtil();
				content = sr.replace(content, false, false);
				content = sr.replaceMsPasteWord(content); //20151203
				newtext.append(content);
			}else if(part.isMimeType("text/plain")){
				newtext.append(StringUtils.getCRLFEscape(content));
			}else if(part.isMimeType("message/delivery-status")){
				newtext.append(content);
			}else{
				newtext.append(content);
			}


			return newtext.toString();
	}
	
	public String executeViewIp() throws Exception {
		
		boolean useGeoIp = "true".equalsIgnoreCase(EnvConstants.getUtilSetting("geoip.db.use"));
		if (!useGeoIp) {
			return null;
		}
		
		String uid = request.getParameter("uid");
		String folder = request.getParameter("folder");
		String sharedFlag = request.getParameter("sharedFlag");
		boolean isShared = (sharedFlag != null && sharedFlag.equals("shared"))?true:false;
		String sharedUserSeq = request.getParameter("sharedUserSeq");
		String sharedFolderName = request.getParameter("sharedFolderName");
		
		sharedFolderName  =	StringEscapeUtils.unescapeHtml(sharedFolderName);

		if (StringUtils.isEmpty(folder)) {
			folder = FolderHandler.INBOX;			
		}
		if(isShared){
			folder = sharedFolderName;			
		} else {
			sharedFlag = "user";
		}
		
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		TMailFolder ufolder = null;
		JSONArray ipInfoArray = null;
		try {		
			store = factory.connect(isShared, sharedUserSeq, request.getRemoteAddr(), user);
			mailManager.setProcessResource(store, getMessageResource());
			ufolder = store.getFolder(folder);
			ufolder.open(false);
			TMailMessage message = ufolder.getMessageByUID(Long.parseLong(uid), false);
			String[] receivedHeaders = message.getHeader("Received");
			
			Stack<String> ipStack = IPUtils.parseIpPattern(receivedHeaders);
			
			if (!ipStack.isEmpty()) {
				ipInfoArray = makeGeoLocationInfo(ipStack);				
			}
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		} finally {
			if(ufolder !=null && ufolder.isOpen())
				ufolder.close(false);
			if(store !=null && store.isConnected())
				store.close();
		}
		
		ResponseUtil.processResponse(response, ipInfoArray);
		return null;
	}

	private JSONArray makeGeoLocationInfo(Stack<String> ipStack) {
		I18nResources resources = getMessageResource();
		JSONArray ipInfoArray = new JSONArray();
		JSONObject ipInfo = null;
		String localIpRanges = EnvConstants.getUtilSetting("local.ip.range");
		String[] ipRangeArray = localIpRanges.split("\\|");
		while (!ipStack.isEmpty()) {
			ipInfo = new JSONObject();
			String ip = ipStack.pop();
			String target = "(-)";
			if (IPUtils.isLocalIp(ip)) {
				target = resources.getMessage("mail.geoip.local");
			} else if (IPUtils.containIp(ipRangeArray, ip)) {
				target = resources.getMessage("mail.geoip.private");
			} else {
				Country country = geoIpManager.getCountry(ip);
				if (country != null) {
					target = country.getName()+" ("+country.getIsoCode()+")";
				}
			}
			ipInfo.put("ip", ip);
			ipInfo.put("country", target);
			ipInfoArray.add(ipInfo);
		}
		return ipInfoArray;
	}
}
