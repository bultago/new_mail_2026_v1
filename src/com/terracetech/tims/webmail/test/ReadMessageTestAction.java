package com.terracetech.tims.webmail.test;

import java.util.Locale;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.ladmin.Protocol;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.LadminManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mail.ibean.MailMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
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

public class ReadMessageTestAction extends BaseAction {

	private static final long serialVersionUID = -8132656159600091214L;

	public ReadMessageTestAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	
	private MailManager mailManager = null;
	private SettingManager userSettingManager = null;
	private SystemConfigManager systemConfigManager = null;
	private LadminManager ladminManager = null;
		
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
	
	String uSeq;
	public String execute() throws Exception{
		
		uSeq = request.getParameter("userSeq");
		String dSeq = request.getParameter("domainSeq");
		
		int userSeq = Integer.parseInt(uSeq);		
		Locale locale = I18nConstants.getUserLocale(request);		
		
		String readType = request.getParameter("readType");
		String viewImg = request.getParameter("viewImg");
		String uid = request.getParameter("uid");
		String folder = request.getParameter("folder");
		
		String keyWord = request.getParameter("keyWord");
		String advancedSearch = request.getParameter("adv");
		String category = request.getParameter("category");		
		String flag = request.getParameter("flag");
		String sortBy = request.getParameter("sortBy");
		String sortDir = request.getParameter("sortDir");		
		
		boolean isRuleAdmin = false;		
		
		char flagType = (flag != null && flag.length() > 0)?flag.charAt(0):'x';
		boolean isPopupRead = (readType != null && (readType.equals("pop") || readType.equals("print")))?true:false;
		boolean isPrintRead = (readType != null && (readType.equals("print")))?true:false;
		
		if (StringUtils.isEmpty(folder))
			folder = FolderHandler.INBOX;
		
		if (StringUtils.isEmpty(sortBy))
			sortBy = "arrival";
		
		if (StringUtils.isEmpty(sortDir))
			sortDir = "desc";		
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder ufolder = null;
		Protocol ladminProtocol = null;
		try {
			//User user = EnvConstants.getTestUser();
			MailUserManager mailUserManager = 
				(MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");
			
			User connectionUser = mailUserManager.readUserOtherInfo(Integer.parseInt(uSeq), Integer.parseInt(dSeq));
			
			store = factory.connect(request.getRemoteAddr(), connectionUser);						
			
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
			
			String mesageIntegrity = systemConfigManager.getMailIntegrityInfo(); 
			boolean isHiddenImg = "on".equalsIgnoreCase(userSettingVo.getHiddenImg());
			
			MessageParserInfoBean parserInfo = getMessagePaserInfoBean();			
			
			if("on".equalsIgnoreCase(viewImg)){
				parserInfo.setHiddenImg(false);
				isHiddenImg = false;
			} else {
				parserInfo.setHiddenImg(isHiddenImg);
			}
			
			parserInfo.setHiddenTag("on".equalsIgnoreCase(userSettingVo.getHiddenTag()));
			parserInfo.setLocale(locale);			
			MailMessageBean messageBean = mailManager.getMessageBean(ufolder, Long.parseLong(uid),parserInfo);
			
			String folderType = "normal";
			
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
			
			
			request.setAttribute("hiddenImg", isHiddenImg);
			request.setAttribute("integrityUse", mesageIntegrity);			
			request.setAttribute("folderType", folderType);
			
			message = messageBean.getMessage();
			long mailSize = message.getSize();
			htmlContent = StringUtils.getCRLFEscape(messageBean.getBodyContent()[0].getText());
			files = messageBean.getAttachContent();
			vcards = messageBean.getVcardContent();
			imageAttach = messageBean.getImageContent();
			
			request.setAttribute("MDNCheck", messageBean.isMdnCheck());
			
			
			if(isRuleAdmin && 
					(!folder.equals(FolderHandler.SENT) &&
					!folder.equals(FolderHandler.DRAFTS) &&
					!folder.equals(FolderHandler.RESERVED) &&
					!folder.equals(FolderHandler.QUOTAVIOLATE))){
				
				ladminProtocol = new Protocol(user.get(User.MAIL_HOST));
				ladminManager.setResource(ladminProtocol);
				
				float[] spamRate = ladminManager.getSpamAndWhiteRate(user.get(User.MESSAGE_STORE), 
						folder, new String[]{Long.toString(message.getUid())});			
				
				request.setAttribute("spamRate", spamRate[0]);				
				
			}
			request.setAttribute("ruleAdmin", isRuleAdmin);
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}finally{
			if(ufolder !=null && ufolder.isOpen())
				ufolder.close(true);
			if(store !=null && store.isConnected())
				store.close();
		}
		
		request.setAttribute("readType", readType);
		
		String forward = "normalRead";		
		
		return forward;
	}

	private MessageParserInfoBean getMessagePaserInfoBean() {		
		String attachesDir = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
		String hostStr = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort(); 
		MessageParserInfoBean infoBean = new MessageParserInfoBean();
		infoBean.setAttachesDir(attachesDir);
		infoBean.setAttachesUrl(EnvConstants.getAttachSetting("attach.url"));
		infoBean.setDefaultCharset(EnvConstants.getMailSetting("default.charset"));
		infoBean.setDefaultImg("/design/common/images/blank.gif");		
		infoBean.setStrLocalhost(hostStr);
		infoBean.setUserId(uSeq);
		return infoBean;
	}
	
	
}
