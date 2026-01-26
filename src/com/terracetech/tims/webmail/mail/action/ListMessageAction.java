package com.terracetech.tims.webmail.mail.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.ladmin.Protocol;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.log.PerformanceLogManager;
import com.terracetech.tims.webmail.common.manager.LadminManager;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

@SuppressWarnings("unchecked")
public class ListMessageAction extends BaseAction {	

	private static final long serialVersionUID = 20081215L;
	private MailManager mailManager = null;
	private SettingManager userSettingManager = null;
	
	private TMailMessage[] messages = null;
	private MailSortMessageBean[] messageBeans = null;
	private LadminManager ladminManager = null;
	private TMailFolder currentFolder = null;
	private PageManager pageManager = null;
	private String sortBy = null;
	private String sortDir = null;
	private PageManager pm = new PageManager();

	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	public void setUserSettingManager(SettingManager userSettingManager) {
		this.userSettingManager = userSettingManager;
	}
	
	public void setLadminManager(LadminManager ladminManager) {
		this.ladminManager = ladminManager;
	}

	public TMailMessage[] getMessages() {
		return messages;
	}
	
	public MailSortMessageBean[] getMessageBeans() {
		return messageBeans;
	}
	
	public TMailFolder getCurrentFolder() {
		return currentFolder;
	}
	
	public PageManager getPageManager(){
		return pageManager;		
	}
	
	/**
	 * @return sortBy �� ��ȯ
	 */
	public String getSortBy() {
		return sortBy;
	}

	/**
	 * @return sortDir �� ��ȯ
	 */
	public String getSortDir() {
		return sortDir;
	}

	public String execute() throws Exception {
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
		
		Locale locale = new Locale(user.get(User.LOCALE));
		boolean isError = false;
		String userId = user.get(User.MAIL_UID);		
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));		
		I18nResources msgResource = getMessageResource("mail");
		
		String viewMode = request.getParameter("vmode");
		viewMode = (viewMode != null)?viewMode:"h";		
		
		String sharedFlag = request.getParameter("sharedFlag");
		boolean isShared = (sharedFlag != null && sharedFlag.equals("shared"))?true:false;
		String sharedUserSeq = request.getParameter("sharedUserSeq");
		String sharedFolderName = request.getParameter("sharedFolderName");				
		
		String keyWord = request.getParameter("keyWord");
		String page = request.getParameter("page");
		String advancedSearch = request.getParameter("adv");
		String category = request.getParameter("category");
		String folderName = request.getParameter("folder");
		
		String portlet = request.getParameter("portlet");
		String portletPart = request.getParameter("part");
		
		String listType = request.getParameter("listType");
		String tagIdStr = request.getParameter("tagId");
		
		String flag = request.getParameter("flag");
		String fromAddr = request.getParameter("fromaddr");
		String toAddr = request.getParameter("toaddr");
		String fromDate = request.getParameter("sdate");
		String toDate = request.getParameter("edate");		
		sortBy = request.getParameter("sortBy");
		sortDir = request.getParameter("sortDir");		
		
		/*if (request.getMethod().equalsIgnoreCase("get")) {
			System.out.println("::::::::::["+folderName+"]:::::::");
			folderName = StringUtils.getDecodingUTF(folderName);
			sharedFolderName = StringUtils.getDecodingUTF(sharedFolderName);
			keyWord = StringUtils.getDecodingUTF(keyWord);
			if (StringUtils.isNotEmpty(advancedSearch)) {
				fromAddr = StringUtils.getDecodingUTF(fromAddr);
				toAddr = StringUtils.getDecodingUTF(toAddr);
			}
		}*/

		String spamAdmId = EnvConstants.getBasicSetting("spam.admin");
		String hamAdmId = EnvConstants.getBasicSetting("white.admin");
		boolean isRuleAdmin = false;
		if(spamAdmId.equals(userId) || hamAdmId.equals(userId)){
			isRuleAdmin = true;
		}		
		
		char flagType = (flag != null && flag.length() > 0)?flag.charAt(0):'x';
		
		String pageBase = null;
		String[] exceptFolders = null;
		String folderEncodeName = null;
		
		page = (page == null)?"1":page;
		listType = (listType != null)?listType:"mail";
		boolean isPortlet = (portlet != null && "on".equals(portlet))?true:false;
		boolean isUnseen = false;
		
		if (StringUtils.isEmpty(folderName))
			folderName = FolderHandler.INBOX;		
		
		if (StringUtils.isEmpty(sortBy))
			sortBy = "arrival";
		
		if (StringUtils.isEmpty(sortDir))
			sortDir = "desc";
		
		
		if(isShared){
			folderEncodeName = sharedFolderName;
		} else {
			folderEncodeName = TMailUtility.IMAPFolderEncode(folderName);
		}		
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		Protocol ladminProtocol = null;
		
		String folderAliasName = null;
		int messageCount = 0;
		int unreadMessageCount = 0;
		String folderType = "normal";
		int total = 0;
		String folderFullName = "";
		keyWord = StringUtils.escapeImapQuote(keyWord);
		try {
			//User user = EnvConstants.getTestUser();
			
			MessageSortInfoBean sortBean = new MessageSortInfoBean();
			sortBean.setUserEmail(user.get(User.EMAIL));
			sortBean.setSortBy(sortBy);
			sortBean.setSortDir(sortDir);
			if (StringUtils.isNotEmpty(advancedSearch)) {
				sortBean.setAdSearchCategory(category);
				sortBean.setAdvanceMode(true);
				sortBean.setAdFromEmailPattern(fromAddr);
				sortBean.setAdToEmailPattern(toAddr);
				sortBean.setFromDate(fromDate);
				sortBean.setToDate(toDate);
				sortBean.setAdSearchPattern(keyWord);				
			} else {
				sortBean.setPattern(keyWord);				
			}	
			
			sortBean.setSearchFlag(flagType);
			
			UserEtcInfoVO userSettingVo = userSettingManager.readUserEtcInfo(userSeq);	
			
			if(isPortlet){
				page="1";
				pageBase = "8";
			} else {				
				pageBase = Integer.toString(userSettingVo.getPageLineCnt());
			}
			
			
			store = factory.connect(isShared, sharedUserSeq, request.getRemoteAddr(), user);			
			
			mailManager.setProcessResource(store, getMessageResource());
			
			if(listType.equals("mail")){
				
				if(folderName.equals("all")){
					folderEncodeName = null;
					
					if("on".equalsIgnoreCase(userSettingVo.getSearchAllFolder())){
						exceptFolders = new String[2];
						exceptFolders[0] = FolderHandler.RESERVED;
						exceptFolders[1] = FolderHandler.DRAFTS;
					} else {
						exceptFolders = new String[4];
						exceptFolders[0] = FolderHandler.RESERVED;
						exceptFolders[1] = FolderHandler.DRAFTS;
						exceptFolders[2] = FolderHandler.SPAM;
						exceptFolders[3] = FolderHandler.TRASH;						
					}
				}			
				
				sortBean.setPage(page);
				sortBean.setPageBase(pageBase);			
				
				messageBeans = mailManager.getXSortMessageBeans(folderEncodeName, exceptFolders, sortBean);
				total = mailManager.getXCommandTotal();
				
				if(messageBeans == null && total > 0){
					int npages = (int) Math.ceil((double) total / Integer.parseInt(pageBase));
					if (0 < npages && npages < Integer.parseInt(page)) {
						page = Integer.toString(npages);				
					}
					
					sortBean.setPage(page);
					messageBeans = mailManager.getXSortMessageBeans(folderEncodeName, exceptFolders, sortBean);
				}			
								
			} else if(listType.equals("tag")){
				int tagId = Integer.parseInt(tagIdStr);				
				
				sortBean.setPage(page);
				sortBean.setPageBase(pageBase);
				
				messageBeans = mailManager.getTagSortMessageBeans(sortBean, tagId);
				total = mailManager.getXCommandTotal();
							
				if(messageBeans == null && total > 0){
					int npages = (int) Math.ceil((double) total / Integer.parseInt(pageBase));
					if (0 < npages && npages < Integer.parseInt(page)) {
						page = Integer.toString(npages);				
					}
					
					sortBean.setPage(page);
					messageBeans = mailManager.getTagSortMessageBeans(sortBean, tagId);
				}
								
				
				folderName = "all";
			}
			
			
			if(isRuleAdmin && !isShared &&
					(!folderName.equals("all") &&
					!folderName.equals(FolderHandler.SENT) &&
					!folderName.equals(FolderHandler.DRAFTS) &&
					!folderName.equals(FolderHandler.RESERVED) &&
					!folderName.equals(FolderHandler.QUOTAVIOLATE))){
				
				if(messageBeans != null){					
					ladminProtocol = new Protocol(user.get(User.MAIL_HOST));
					ladminManager.setResource(ladminProtocol);
					int msgSize = messageBeans.length;
					String[] uids = new String[msgSize];
					for (int i = 0; i < msgSize; i++) {
						uids[i] = Long.toString(messageBeans[i].getId());
					}
					float[] spamRates = 
						ladminManager.getSpamAndWhiteRate(
								user.get(User.MESSAGE_STORE), folderName,uids);
					
					for (int i = 0; i < msgSize; i++) {
						messageBeans[i].setSpamRate(spamRates[i]);
					}
				}
			}
			
			if(folderName.equalsIgnoreCase("all")){			
				folderType = "all";				
				folderAliasName = msgResource.getMessage("folder.all");
				folderFullName = "all";
			} else {
				if(isShared){
					folderType = "shared";	
				} else {						
					if(folderName.equals(FolderHandler.INBOX)){
						folderType = "inbox";
					} else if(folderName.equals(FolderHandler.SENT)){
						folderType = "sent";
					} else if(folderName.equals(FolderHandler.DRAFTS)){
						folderType = "draft";
					} else if(folderName.equals(FolderHandler.RESERVED)){
						folderType = "reserved";
					} else if(folderName.equals(FolderHandler.SPAM)){
						folderType = "spam";
					} else if(folderName.equals(FolderHandler.TRASH)){
						folderType = "trash";
					} else if(folderName.equals(FolderHandler.QUOTAVIOLATE)){
						folderType = "quotaviolate";
					}
				}				
				currentFolder = mailManager.getFolder(folderEncodeName);
				folderFullName = currentFolder.getFullName();
				
				folderAliasName = currentFolder.getAlias4subFolder();
				messageCount = currentFolder.getMessageCount();
				unreadMessageCount = currentFolder.getUnreadMessageCount();				
			}
			
			if(isPortlet && sortBean.isUnseenFlag()){
				isUnseen = true;
				folderAliasName = msgResource.getMessage("menu.quick.unread");
			}
		} catch (Exception e) {
			isError = true;
			LogManager.writeErr(this, e.getMessage(), e);
		} finally {
			if(store !=null && store.isConnected())
				store.close();
			
			if(ladminProtocol != null){
				ladminProtocol.logout();
			}
		}
		
		keyWord = StringUtils.unescapeImapQuote(keyWord);
		boolean enableCcview = userSettingManager.enableCcview(Integer.parseInt( user.get(User.MAIL_DOMAIN_SEQ)));
		String forward = null;
		JSONObject messageListinfo = new JSONObject();
		
		messageListinfo.put("folderType", folderType);			
		messageListinfo.put("folderName", folderAliasName);
		messageListinfo.put("folderFullName", folderFullName);
		messageListinfo.put("messageCount", messageCount);
		messageListinfo.put("unreadMessageCount", unreadMessageCount);
		messageListinfo.put("currentPage", page);
		messageListinfo.put("pageBase", pageBase);
		messageListinfo.put("total", total);
		messageListinfo.put("enableCcview", enableCcview);
		
		if(isAjax){			
			JSONArray messageList = new JSONArray();
			if(messageBeans != null){
				for (int i = 0; i < messageBeans.length; i++) {
					messageList.add(messageBeans[i].toJson(user.get(User.EMAIL)));
				}
			}
			messageListinfo.put("viewMode", StringEscapeUtils.escapeHtml(viewMode));
			messageListinfo.put("messageList", messageList);
			messageListinfo.put("sortBy", sortBy);
			messageListinfo.put("sortDir", sortDir);
			ResponseUtil.processResponse(response, messageListinfo);
			
			if(isWDebug){			
				dwbugDate = new Date();
				detime = dwbugDate.getTime();			
				debugStr.append(",SERVER_END,");			
				debugStr.append("NTIME,");
				debugStr.append(dsdf.format(dwbugDate));
				debugStr.append(",TOTAL,");
				debugStr.append(dsdf.format(new Date(detime - dstime)));
				
				PerformanceLogManager.writeLog(remoteIp, user.get(User.EMAIL), agent, "LIST_SERVER_DATA", debugStr.toString());
				
				debugStr = null;
				dstime = 0;
				detime = 0;
				dwbugDate = null;
				dsdf = null;
				agent = null;
			}
		} else {
			if(!isError){
				forward = "mailMailList";			
				if(isPortlet){			
					forward = "mailPortletList";
					request.setAttribute("unseen", isUnseen);		
					request.setAttribute("folderName", folderAliasName);
					request.setAttribute("messageCount", messageCount);
					request.setAttribute("unreadMessageCount", unreadMessageCount);
					request.setAttribute("currentPage", page);
					request.setAttribute("pageBase", pageBase);
					request.setAttribute("total", total);		
					request.setAttribute("part", portletPart);
					request.setAttribute("viewMode", viewMode);
				}
				request.setAttribute("email", user.get(User.EMAIL));
				request.setAttribute("listType", listType);
				request.setAttribute("tagId", tagIdStr);
				request.setAttribute("folderType", folderType);
				request.setAttribute("folderFullName", folderFullName);
				request.setAttribute("page", page);
				request.setAttribute("advancedSearch", advancedSearch);
				request.setAttribute("category", category);
				request.setAttribute("fromaddr", fromAddr);
				request.setAttribute("toaddr", toAddr);
				request.setAttribute("sdate", fromDate);
				request.setAttribute("edate", toDate);
				request.setAttribute("keyWord", keyWord);
				request.setAttribute("flag", flag);
				request.setAttribute("sharedFlag", sharedFlag);
				request.setAttribute("sharedUserSeq", sharedUserSeq);
				request.setAttribute("sharedFolderName", sharedFolderName);
				request.setAttribute("enableCcview", enableCcview);
				request.setAttribute("mailInfo", messageListinfo.toString());
			} else {
				forward = "subError";
			}
		}
		
		return forward;
	}
	
	
	public String executePart() throws Exception {
		return execute();
	}
	
	public String executeRelationMessage ()  throws Exception {
		
		String userId = user.get(User.MAIL_UID);		
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));		
		I18nResources msgResource = getMessageResource("mail");
		
		String sharedFlag = request.getParameter("sharedFlag");
		boolean isShared = (sharedFlag != null && sharedFlag.equals("shared"))?true:false;
		String sharedUserSeq = request.getParameter("sharedUserSeq");
		String sharedFolderName = request.getParameter("sharedFolderName");				
		
		String keyWord = request.getParameter("keyWord");
		String page = request.getParameter("page");		
		String folderName = request.getParameter("folder");
		String uid = request.getParameter("uid");
		String flag = request.getParameter("flag");
		
		String sortBy = request.getParameter("sortBy");
		String sortDir = request.getParameter("sortDir");
		
		String pageBase = null;
		String[] exceptFolders = null;
		String folderEncodeName = null;
		
		page = (page == null)?"1":page;
		pageBase = "5";
		
		if (StringUtils.isEmpty(folderName))
			folderName = FolderHandler.INBOX;		
		
		if (StringUtils.isEmpty(sortBy))
			sortBy = "arrival";
		
		if (StringUtils.isEmpty(sortDir))
			sortDir = "desc";		
		
		if(isShared){
			folderEncodeName = sharedFolderName;
		} else {
			folderEncodeName = TMailUtility.IMAPFolderEncode(folderName);
		}
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();		
	
		
		int total = 0;
		
		char flagType = (flag != null && flag.length() > 0)?flag.charAt(0):'x';
		try{
			MessageSortInfoBean sortBean = new MessageSortInfoBean();
			sortBean.setSortBy(sortBy);
			sortBean.setSortDir(sortDir);
			sortBean.setPattern(keyWord);			
			sortBean.setSearchFlag(flagType);
			sortBean.setPage(page);
			sortBean.setPageBase(pageBase);
			UserEtcInfoVO userSettingVo = userSettingManager.readUserEtcInfo(userSeq);
			if("on".equalsIgnoreCase(userSettingVo.getSearchAllFolder())){
				exceptFolders = new String[2];
				exceptFolders[0] = FolderHandler.RESERVED;
				exceptFolders[1] = FolderHandler.DRAFTS;
			} else {
				exceptFolders = new String[4];
				exceptFolders[0] = FolderHandler.RESERVED;
				exceptFolders[1] = FolderHandler.DRAFTS;
				exceptFolders[2] = FolderHandler.SPAM;
				exceptFolders[3] = FolderHandler.TRASH;						
			}
						
			store = factory.connect(isShared, sharedUserSeq, request.getRemoteAddr(), user);
			mailManager.setProcessResource(store, getMessageResource());
			
			
			messageBeans = mailManager.getXSortRelationMessageBeans(null, exceptFolders, sortBean, 
					folderEncodeName, uid);
			total = mailManager.getXCommandTotal();
			
			if(messageBeans == null && total > 0){
				int npages = (int) Math.ceil((double) total / Integer.parseInt(pageBase));
				if (0 < npages && npages < Integer.parseInt(page)) {
					page = Integer.toString(npages);				
				}
				
				sortBean.setPage(page);
				messageBeans = mailManager.getXSortRelationMessageBeans(null, exceptFolders, sortBean,folderEncodeName, uid);
			}	
			
			
		} catch (Exception e) {
			e.printStackTrace();
			LogManager.writeErr(this, e.getMessage(), e);
		} finally {
			if(store !=null && store.isConnected())
				store.close();		
			
		}	
		
		pm.setPage(Integer.parseInt(page));
		pm.initParameter(total, Integer.parseInt(pageBase), 5);	
		
		JSONObject messageListinfo = new JSONObject();
		messageListinfo.put("page", page);
		messageListinfo.put("isFistPage", pm.isFirstPage());
		messageListinfo.put("isLastPage", pm.isLastPage());
		messageListinfo.put("pageBase", pageBase);
		messageListinfo.put("total", total);
		messageListinfo.put("ruid", uid);
		messageListinfo.put("rfolderName", folderName);
		messageListinfo.put("sortDir", sortDir);		
		JSONArray messageList = new JSONArray();
		if(messageBeans != null){
			for (int i = 0; i < messageBeans.length; i++) {
				messageList.add(messageBeans[i].toJson(user.get(User.EMAIL)));
			}
		}
		messageListinfo.put("messageList", messageList);
		
		ResponseUtil.processResponse(response, messageListinfo);
		
		return null;
	}

	
}
