package com.terracetech.tims.webmail.test;

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
import com.terracetech.tims.webmail.common.manager.LadminManager;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class ListMessageTestAction extends BaseAction {
	
	private static final long serialVersionUID = -3048031569868191205L;
	private MailManager mailManager = null;
	private SettingManager userSettingManager = null;
	
	private TMailMessage[] messages = null;
	private MailSortMessageBean[] messageBeans = null;
	private LadminManager ladminManager = null;
	private TMailFolder currentFolder = null;
	private PageManager pageManager = null;
	private String sortBy = null;
	private String sortDir = null;
	
	public ListMessageTestAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}

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
	 * @return sortBy 값 반환
	 */
	public String getSortBy() {
		return sortBy;
	}

	/**
	 * @return sortDir 값 반환
	 */
	public String getSortDir() {
		return sortDir;
	}

	public String execute() throws Exception {
		
		
		String uSeq = request.getParameter("userSeq");
		String dSeq = request.getParameter("domainSeq");				
		
		
				
		int userSeq = Integer.parseInt(uSeq);		
		I18nResources msgResource = getMessageResource("mail");
		
		String viewMode = request.getParameter("vmode");
		viewMode = (viewMode != null)?viewMode:"h";				
						
		
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
		
		boolean isRuleAdmin = false;			
		
		char flagType = (flag != null && flag.length() > 0)?flag.charAt(0):'x';
		
		String pageBase = null;
		String[] exceptFolders = null;
		String folderEncodeName = null;
		
		page = (page == null)?"1":page;
		listType = (listType != null)?listType:"mail";
		boolean isPortlet = (portlet != null && "on".equals(portlet))?true:false;
		
		if (StringUtils.isEmpty(folderName))
			folderName = FolderHandler.INBOX;		
		
		if (StringUtils.isEmpty(sortBy))
			sortBy = "arrival";
		
		if (StringUtils.isEmpty(sortDir))
			sortDir = "desc";		
		
		folderEncodeName = TMailUtility.IMAPFolderEncode(folderName);				
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		Protocol ladminProtocol = null;
		try {
			//User user = EnvConstants.getTestUser();
			
			MessageSortInfoBean sortBean = new MessageSortInfoBean();
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
			int total = 0;
			pageBase = "100";
//			if(isPortlet){
//				page="1";
//				pageBase = "8";
//			} else {				
//				pageBase = Integer.toString(userSettingVo.getPageLineCnt());
//			}
			
			MailUserManager mailUserManager = 
				(MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");
			
			User connectionUser = mailUserManager.readUserOtherInfo(Integer.parseInt(uSeq), Integer.parseInt(dSeq));
			
			store = factory.connect(request.getRemoteAddr(), connectionUser);			
			
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
			
			
			if(isRuleAdmin && 
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
			
			
			String folderAliasName = null;
			int messageCount = 0;
			int unreadMessageCount = 0;
			String folderType = "normal";
			
			if(folderName.equalsIgnoreCase("all")){			
				folderType = "all";				
				folderAliasName = msgResource.getMessage("folder.all");
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
				
				currentFolder = mailManager.getFolder(folderEncodeName);
				
				folderAliasName = currentFolder.getAlias4subFolder();
				messageCount = currentFolder.getMessageCount();
				unreadMessageCount = currentFolder.getUnreadMessageCount();				
			}
			
			
			
			request.setAttribute("folderType", folderType);			
			request.setAttribute("folderName", folderAliasName);
			request.setAttribute("messageCount", messageCount);
			request.setAttribute("unreadMessageCount", unreadMessageCount);
			request.setAttribute("currentPage", page);
			request.setAttribute("pageBase", pageBase);
			request.setAttribute("total", total);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			messages = new TMailMessage[] {};
		} finally {
			if(store !=null && store.isConnected())
				store.close();
			
			if(ladminProtocol != null){
				ladminProtocol.logout();
			}
		}
		
		request.setAttribute("part", portletPart);
		request.setAttribute("viewMode", viewMode);
		
		String forward = "mailMailList";		
//		if(isPortlet){
//			forward = "mailPortletList";
//		} 
		
		return forward;
	}
}
