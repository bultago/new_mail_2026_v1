package com.terracetech.tims.service.manager;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

import jakarta.activation.DataHandler;
import jakarta.mail.Flags;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.sun.mail.imap.IMAPInputStream;
import com.sun.mail.imap.IMAPMessage;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.service.IErrorCode;
import com.terracetech.tims.service.tms.vo.AttachFileVO;
import com.terracetech.tims.service.tms.vo.EmailAddressVO;
import com.terracetech.tims.service.tms.vo.FolderCondVO;
import com.terracetech.tims.service.tms.vo.FolderContentVO;
import com.terracetech.tims.service.tms.vo.FolderInfoVO;
import com.terracetech.tims.service.tms.vo.ListCondVO;
import com.terracetech.tims.service.tms.vo.ListContentVO;
import com.terracetech.tims.service.tms.vo.ListInfoVO;
import com.terracetech.tims.service.tms.vo.MailWorkCondVO;
import com.terracetech.tims.service.tms.vo.MailWorkResultVO;
import com.terracetech.tims.service.tms.vo.MdnContentVO;
import com.terracetech.tims.service.tms.vo.MdnInfoVO;
import com.terracetech.tims.service.tms.vo.MdnListVO;
import com.terracetech.tims.service.tms.vo.MdnRcptContentVO;
import com.terracetech.tims.service.tms.vo.MdnRecallVO;
import com.terracetech.tims.service.tms.vo.ReadCondVO;
import com.terracetech.tims.service.tms.vo.SendCondVO;
import com.terracetech.tims.service.tms.vo.SendResultVO;
import com.terracetech.tims.service.tms.vo.ViewContentVO;
import com.terracetech.tims.service.tms.vo.WriteCondVO;
import com.terracetech.tims.service.tms.vo.WriteResultVO;
import com.terracetech.tims.webmail.common.ByteArrayDataSource;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.ladmin.Protocol;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.LadminManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mail.ibean.MDNResponsesBean;
import com.terracetech.tims.webmail.mail.ibean.MDNResponsesRcptBean;
import com.terracetech.tims.webmail.mail.ibean.MailFolderBean;
import com.terracetech.tims.webmail.mail.ibean.MailMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MailSendResultBean;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MailWriteMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.ibean.MessageWriteInfoBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.BigattachManager;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.Image2Cid;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.SendMessageDirector;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mail.manager.builder.DraftMimeMessageBuilder;
import com.terracetech.tims.webmail.mail.manager.builder.GeneralMimeMessageBuilder;
import com.terracetech.tims.webmail.mail.manager.builder.MimeMessageBuilder;
import com.terracetech.tims.webmail.mail.manager.send.SendHandler;
import com.terracetech.tims.webmail.mail.vo.MailBigAttachVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.setting.manager.LastrcptManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.manager.SignManager;
import com.terracetech.tims.webmail.setting.manager.VCardManager;
import com.terracetech.tims.webmail.setting.vo.AttachInfoVO;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import com.terracetech.tims.webmail.util.StringUtils;

@SuppressWarnings("all")
public class MailServiceManager implements IMailServiceManager {
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private MailManager mailManager = null;	
	private LadminManager ladminManager = null;	
	private LastrcptManager rcptManager = null;	
	private SignManager signManager = null;	
	private VCardManager vcardManager = null;	
	private SettingManager userSettingManager = null;	
	private MailUserManager mailUserManager = null;	
	private BigattachManager bigAttachManager = null;
	private SystemConfigManager systemConfigManager = null;	
	
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	public void setLadminManager(LadminManager ladminManager) {
		this.ladminManager = ladminManager;
	}
	
	public void setRcptManager(LastrcptManager rcptManager) {
		this.rcptManager = rcptManager;
	}
	
	public void setSignManager(SignManager signManager) {
		this.signManager = signManager;
	}
	
	public void setVcardManager(VCardManager vcardManager) {
		this.vcardManager = vcardManager;
	}
	
	public void setUserSettingManager(SettingManager userSettingManager) {
		this.userSettingManager = userSettingManager;
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public void setBigAttachManager(BigattachManager bigAttachManager) {
		this.bigAttachManager = bigAttachManager;
	}
	
	public void setSystemConfigManager(SystemConfigManager systemConfigManager) {
		this.systemConfigManager = systemConfigManager;
	}

	public ListInfoVO doSimpleMailList(ListCondVO listVO){
		return doSimpleMailList(listVO, null);
		
	}
	public ListInfoVO doSimpleMailList(ListCondVO listVO, User user){
		
		String email = listVO.getEmail();
		String[] userParams = email.split("@");
		I18nResources msgResource = new I18nResources(new Locale(listVO.getLocale()));
		
		String folderName = listVO.getFolderName();
		String folderType = null;
		String sortBy = listVO.getSortBy();
		String sortDir = listVO.getSortDir();
		String remoteIp = listVO.getRemoteIp();
		String listType = listVO.getListType();
	        String tagIdStr = listVO.getTagId();
		
		if (StringUtils.isEmpty(folderName))
			folderName = FolderHandler.INBOX;		
		
		if (StringUtils.isEmpty(sortBy))
			sortBy = "arrival";
		
		if (StringUtils.isEmpty(sortDir))
			sortDir = "desc";
		
		
		if(StringUtils.isEmpty(remoteIp))
			remoteIp = "127.0.0.1";
		
		listType = (StringUtils.isEmpty(listType)) ? "mail" : listType;
		
		String folderEncodeName = TMailUtility.IMAPFolderEncode(folderName);
		String folderAliasName = null;
		String folderFullName = null;
		MessageSortInfoBean sortBean = new MessageSortInfoBean();
		ListInfoVO returnVos = new ListInfoVO();
		int page = listVO.getPage();
		int pageBase = listVO.getPageBase();
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		
		try {
			User connectionUser = null;
			if(user == null){
				MailUserManager mailUserManager = (MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");			
				connectionUser = mailUserManager.readUserConnectionInfo(userParams[0],userParams[1]);
			} else {
				connectionUser = user;
			}
			store = factory.connect(remoteIp, connectionUser);
			mailManager.setProcessResource(store, msgResource);
			
			UserEtcInfoVO userSettingVo = userSettingManager.readUserEtcInfo(Integer.parseInt(connectionUser.get(User.MAIL_USER_SEQ)));
			
			sortBean.setSortBy(listVO.getSortBy());
			sortBean.setSortDir(listVO.getSortDir());
		        if (listVO.isAdvancedSearch()) {
		            sortBean.setAdSearchCategory(listVO.getCategory());
		            sortBean.setAdvanceMode(true);
		            sortBean.setAdSearchPattern(listVO.getKeyWord());
		            sortBean.setAdFromEmailPattern(listVO.getFromEmailPattern());
		            sortBean.setAdToEmailPattern(listVO.getToEmailPattern());
		            sortBean.setOperation(listVO.getOperation());
		            sortBean.setFromDate(listVO.getFromDate());
		            sortBean.setToDate(listVO.getToDate());
		        } else {
		            sortBean.setPattern(listVO.getKeyWord());
		        }
		        sortBean.setSearchFlag(listVO.getFlagType());
		        sortBean.setPage(Integer.toString(page));
		        sortBean.setPageBase(Integer.toString(pageBase));

			MailSortMessageBean[] messageBeans = null;
		        int total = 0;
			int messageCount = 0;
			int unreadMessageCount = 0;
			if(folderName.equalsIgnoreCase("all")){			
				folderType = "all";				
				folderAliasName = msgResource.getMessage("folder.all");
				folderFullName = "all";
				folderEncodeName = null;
			} else {
				folderType = "normal";
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
				TMailFolder currentFolder = mailManager.getFolder(folderEncodeName);
				folderAliasName = currentFolder.getAlias4subFolder();
				folderFullName = currentFolder.getFullName();
				messageCount = currentFolder.getMessageCount();
				unreadMessageCount = currentFolder.getUnreadMessageCount();
			}						
			
		            
		        if (listType.equals("mail")) {
		            String[] exceptFolders = null;
		            if (folderName.equals("all")) {
		                if ("on".equalsIgnoreCase(userSettingVo.getSearchAllFolder())) {
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
		            
		            messageBeans = mailManager.getXSortMessageBeans(folderEncodeName, exceptFolders,
		                    sortBean);
		            total = mailManager.getXCommandTotal();
		            if (messageBeans == null && total > 0) {
		                int npages = (int) Math.ceil((double) total / pageBase);
		                if (0 < npages && npages < page) {
		                    page = npages;
		                }
		    
		                sortBean.setPage(Integer.toString(page));
		                messageBeans = mailManager.getXSortMessageBeans(folderEncodeName, exceptFolders, sortBean);
		            }
		        } else if (listType.equals("tag")) {
		            int tagId = Integer.parseInt(tagIdStr);

		            sortBean.setPage(Integer.toString(page));
		            sortBean.setPageBase(Integer.toString(pageBase));

		            messageBeans = mailManager.getTagSortMessageBeans(sortBean, tagId);
		            total = mailManager.getXCommandTotal();

		            if (messageBeans == null && total > 0) {
		                int npages = (int) Math.ceil((double) total / pageBase);
		                if (0 < npages && npages < page) {
		                    page = npages;
		                }

		                sortBean.setPage(Integer.toString(page));
		                messageBeans = mailManager.getTagSortMessageBeans(sortBean, tagId);
		            }

		            folderName = "all";
		        }
					
			returnVos.setFolderName(folderFullName);
			returnVos.setFolderAliasName(folderAliasName);
			returnVos.setPageNo(page);
			returnVos.setSearchCnt(pageBase);
			returnVos.setUnreadCnt(unreadMessageCount);
			returnVos.setTotalCnt((folderName.equalsIgnoreCase("all"))?total:messageCount);
			returnVos.setListContents(parseMailBeans(messageBeans,folderType));

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally {
			try {
				if(store !=null && store.isConnected())
					store.close();
			} catch (MessagingException ignore) {
				log.error(ignore.getMessage(), ignore);
			}
			
		}
		return returnVos;
	}

	private ListContentVO[] parseMailBeans(MailSortMessageBean[] beans, String folderType) 
	throws MessagingException{
		ListContentVO[] listContent = null;
		
		if(beans != null){
			listContent = new ListContentVO[beans.length];			
			String name = null;
			String email = null;			
			Date date = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			for (int i = 0 ; i < beans.length ; i++) {
				if(folderType.equals("sent") || 
					folderType.equals("reserved") ||
					folderType.equals("draft")){
					name = beans[i].getToName();
					email = beans[i].getToEmail();
					date = beans[i].getEnvelopeDate();
				} else {
					name = beans[i].getFromName();
					email = beans[i].getFromEmail();
					date = beans[i].getInternalDate();
				}
				
				listContent[i] = new ListContentVO();				
				listContent[i].setId(beans[i].getId());				
				listContent[i].setEmail(email);
				listContent[i].setName(name);
				listContent[i].setSubject(beans[i].getSubject());
				listContent[i].setDate((date != null)?dateFormat.format(date.getTime()):"");
				listContent[i].setSize(beans[i].getSize());
				listContent[i].setByteSize(beans[i].getByteSize());				
				listContent[i].setFlag(beans[i].getFlag());
				listContent[i].setFolderName(beans[i].getFolderName());
				listContent[i].setFolderEncName(beans[i].getFolderEncName());
				listContent[i].setFolderFullName(beans[i].getFolderFullName());
				listContent[i].setFolderDepthName(beans[i].getFolderDepthName());
				listContent[i].setPriority(beans[i].getPriority());
				listContent[i].setPreview(beans[i].getPreview());
		                listContent[i].setTagNameList(beans[i].getTagNameList());
			}
		}
		
		return listContent;		
	}
	
	public ViewContentVO doSimpleMailRead(ReadCondVO readVO) throws Exception{
		return doSimpleMailRead(readVO, null);
	}
	public ViewContentVO doSimpleMailRead(ReadCondVO readVO, User user) throws Exception{
		String email = readVO.getEmail();
		String[] userParams = email.split("@");
		I18nResources msgResource = new I18nResources(new Locale(readVO.getLocale()));
		String remoteIp = readVO.getRemoteIp();
		
		ViewContentVO viewVO = null;
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		
		String folderName = readVO.getFolder();
		TMailFolder ufolder = null;
		try {
			
			User connectionUser = null;
			if(user == null){
				MailUserManager mailUserManager = (MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");			
				connectionUser = mailUserManager.readUserConnectionInfo(userParams[0],userParams[1]);
			} else {
				connectionUser = user;
			}		
			
			store = factory.connect(remoteIp, connectionUser);
			mailManager.setProcessResource(store, msgResource);
			ufolder = mailManager.getFolder(folderName);	
			ufolder.open(true);
			
			MessageSortInfoBean sortBean = new MessageSortInfoBean();
			sortBean.setSortBy(readVO.getSortBy());
			sortBean.setSortDir(readVO.getSortDir());
			if (readVO.isAdvancedSearch()) {
				sortBean.setAdSearchCategory(readVO.getCategory());
				sortBean.setAdvanceMode(true);
				sortBean.setAdSearchPattern(readVO.getKeyWord());
				sortBean.setAdFromEmailPattern(readVO.getFromEmailPattern());
				sortBean.setAdToEmailPattern(readVO.getToEmailPattern());
			} else {
				sortBean.setPattern(readVO.getKeyWord());				
			}
			sortBean.setSearchFlag(readVO.getFlagType());
			
			long[] neighborUID = mailManager.getNeighborUID(ufolder, readVO.getUid(), sortBean);
			long preUid = neighborUID[0];
			long nextUid = neighborUID[1];
			
			MessageParserInfoBean infoBean = new MessageParserInfoBean();
			
			infoBean.setAttachesUrl(EnvConstants.getAttachSetting("attach.url"));
			infoBean.setDefaultCharset(EnvConstants.getMailSetting("default.charset"));
			infoBean.setDefaultImg("/design/common/images/blank.gif");
			infoBean.setUserId(userParams[0]);
			infoBean.setAttachesDir(readVO.getAttachDir());
			infoBean.setStrLocalhost(readVO.getLocalURL());
			infoBean.setHiddenImg(readVO.isViewImage());
			
			
			MailMessageBean messageBean = mailManager.getMessageBean(ufolder, readVO.getUid(),infoBean);
			viewVO = parseMailContents(messageBean);
			viewVO.setFolderName(ufolder.getAlias4subFolder());
			viewVO.setFolderEncName(ufolder.getEncName());
			viewVO.setPreUid(preUid);
			viewVO.setNextUid(nextUid);
			viewVO.setMdnCheck(Boolean.toString(messageBean.isMdnCheck()));
			 
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}finally {
			try {
				if(ufolder !=null && ufolder.isOpen())
					ufolder.close(true);
				if(store !=null && store.isConnected())
					store.close();
			} catch (MessagingException ignore) {
				log.error(ignore.getMessage(), ignore);
			}
			
		}
		return viewVO;
	}
	
	private ViewContentVO  parseMailContents(MailMessageBean messageBean) throws Exception{
		ViewContentVO viewVO = new ViewContentVO();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		TMailMessage message = messageBean.getMessage();
		viewVO.setUid(message.getUid());
		viewVO.setSubject(message.getSubject());
		if (message.getFrom() == null) {
			viewVO.setFrom(null);
		} else {
			viewVO.setFrom(getEmailAddress(message.getFrom())[0]);
		}
		viewVO.setTos(getEmailAddress(message.getTo()));
		viewVO.setCcs(getEmailAddress(message.getCc()));
		Date receiveDate = message.getMessage().getReceivedDate();
	        String receiveDateFormat = (receiveDate == null) ? "" : dateFormat.format(receiveDate);
	        viewVO.setDate(receiveDateFormat);
	        Date sentDate = message.getMessage().getSentDate();
	        String sentDateFormat = (sentDate == null) ? "" : dateFormat.format(sentDate);
	        viewVO.setDate(receiveDateFormat);
	        viewVO.setSentDate(sentDateFormat);
		viewVO.setSize(messageBean.getByteSize());
		viewVO.setPriority(messageBean.getPriority());		
		TMailPart[] htmlContentPart = messageBean.getBodyContent();
		
		StringBuffer newtext = new StringBuffer();
		String htmlContent = null;
		String encoding = null;
		String txtContent = null;
		if (htmlContentPart != null && htmlContentPart.length > 0) {
			for (int i=0; i<htmlContentPart.length; i++) {
				// TCUSTOM-2229 20161020
				if(StringUtils.isNotEmpty(htmlContentPart[i].getText())){
					// TCUSTOM-2018 20161019
					newtext.append(getContentString(htmlContentPart[i]));
					break;
				}
				
			}
			htmlContent = newtext.toString();
		}
		/*
		String encoding = (htmlContentPart != null && htmlContentPart.length > 0)?htmlContentPart[0].getCharset():MimeUtility.getDefaultJavaCharset();
		String htmlContent = (htmlContentPart != null && htmlContentPart.length > 0)?
				StringUtils.getCRLFEscape(htmlContentPart[0].getText()):"";
		String txtContent = (htmlContentPart != null && htmlContentPart.length > 1)?
						StringUtils.getCRLFEscape(htmlContentPart[1].getText()):htmlContent;
		*/
		viewVO.setContents(htmlContent);
		viewVO.setTextContents(txtContent);
		viewVO.setBodyEncoding(encoding);
		viewVO.setMessageId(message.getMessageID());
		List<AttachFileVO> attachList = new ArrayList<AttachFileVO>();
		TMailPart[] files = messageBean.getAttachContent();
		AttachFileVO tempVO = null;
		AttachFileVO[] listTempVO = null;
		if(files != null){
			for (TMailPart tMailPart : files) {
				tempVO = new AttachFileVO();
				tempVO.setDepth(tMailPart.getPath());
				tempVO.setName(tMailPart.getFileName());
				tempVO.setSize(tMailPart.getSize75());
				tempVO.setType("file");
				attachList.add(tempVO);
			}
		}
		
		TMailPart[] vcards = messageBean.getVcardContent();
		if(vcards != null){
			for (TMailPart tMailPart : vcards) {
				tempVO = new AttachFileVO();
				tempVO.setDepth(tMailPart.getPath());
				tempVO.setName(tMailPart.getFileName());
				tempVO.setSize(tMailPart.getSize75());
				tempVO.setType("vcard");
				attachList.add(tempVO);
			}
		}
		
		if(attachList.size() > 0){
			listTempVO = new AttachFileVO[attachList.size()];
			attachList.toArray(listTempVO);
			viewVO.setAttachs(listTempVO);
		}
		
		return viewVO;
	}
	
	private EmailAddressVO[] getEmailAddress(InternetAddress[] iaddrs){
		EmailAddressVO[] addrs = null;
		if(iaddrs != null){
			addrs = new EmailAddressVO[iaddrs.length];
			for (int i = 0 ; i < iaddrs.length ; i++) {
				addrs[i] = new EmailAddressVO();
				addrs[i].setPersonal(iaddrs[i].getPersonal());
				addrs[i].setAddress(iaddrs[i].getAddress());
			}
		}
		
		return addrs;
	}
	
	public AttachFileVO doSimpleMailDownLoadAttach(ReadCondVO readVO) throws Exception{
		return doSimpleMailDownLoadAttach(readVO, null);
	}
	
	public AttachFileVO doSimpleMailDownLoadAttach(ReadCondVO readVO,User user) throws Exception{
		AttachFileVO attachFileVO = new AttachFileVO();
		
		String email = readVO.getEmail();
		String[] userParams = email.split("@");
		I18nResources msgResource = new I18nResources(new Locale(readVO.getLocale()));
		String remoteIp = readVO.getRemoteIp();
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		
		String folderName = readVO.getFolder();
		TMailFolder ufolder = null;
		try {
			
			User connectionUser = null;
			if(user == null){
				MailUserManager mailUserManager = (MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");			
				connectionUser = mailUserManager.readUserConnectionInfo(userParams[0],userParams[1]);
			} else {
				connectionUser = user;
			}		
			
			store = factory.connect(remoteIp, connectionUser);
			mailManager.setProcessResource(store, msgResource);
			ufolder = mailManager.getFolder(folderName);	
			ufolder.open(true);
			
			TMailMessage message = ufolder.getMessageByUID(readVO.getUid(),false);
			Stack<String> nestedPartStatck = new Stack<String>();			
			nestedPartStatck.push(readVO.getAttachPath());
			
			TMailPart attachPart = getNestedPart(message,factory.getSession(),nestedPartStatck);
			boolean isAttachRFC822 = attachPart.isMimeType("message/rfc822");
			boolean isVcard = attachPart.isMimeType("text/x-vcard");
			InputStream in = null;
			String fileName = null;
			
			fileName = attachPart.getFileName();				
			in = (!isAttachRFC822 && !isVcard) ? attachPart.getInputStream() : null;
			//fileName = StringUtils.getDownloadFileName(fileName, "");
			
			attachFileVO.setName(fileName);
			attachFileVO.setSize(attachPart.getSize());
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				
				if(isAttachRFC822){
					Object msg = attachPart.getContent();
					Enumeration enumer = null;					
                	            	if(msg instanceof IMAPMessage){            		
                	            		enumer = ((IMAPMessage)msg).getAllHeaderLines();            		
                	            	} else if(msg instanceof MimeMessage){
                	            		enumer = ((MimeMessage)msg).getAllHeaderLines();
                	            	}
                	            	
                	            	if(enumer != null){
                	            		String header = "";
                	            		while (enumer.hasMoreElements()) {		
                		
                			                header += (String)enumer.nextElement()+ '\n';
                			            }
                		
                	            		header += '\n';	            		
                	            		out.write(header.getBytes());
                	            	}
                	            	if(msg instanceof IMAPMessage){
                	            		in = ((IMAPMessage)msg).getRawInputStream();
                	            	} else if(msg instanceof MimeMessage){
                	            		in = ((MimeMessage)msg).getRawInputStream();
                	            	}
	            		            	
				}
				
		                
		                if (isVcard) {
		                    String content = attachPart.getTextContent();
		                    String charset = ("ko".equalsIgnoreCase(EnvConstants.getBasicSetting("setup.state"))) ? "euc-kr"
		                            : "shift-jis";
		                    in = new ByteArrayInputStream(content.getBytes(charset));
		                }
				
				byte[] buffer = new byte[1024 * 1024];
	                        int n;
	            
				while ((n = in.read(buffer, 0, buffer.length)) != -1) {
	            	            out.write(buffer, 0, n);
	                        }
				attachFileVO.setFiledata(out.toByteArray());
				
			} catch (Exception ex) {
				LogManager.writeErr(this, ex.getMessage(), ex);
			} finally {
				out.close();
				in.close();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}finally {
			try {
				if(ufolder !=null && ufolder.isOpen())
					ufolder.close(true);
				if(store !=null && store.isConnected())
					store.close();
			} catch (MessagingException ignore) {
				log.error(ignore.getMessage(), ignore);
			}
			
		}
		
		return attachFileVO;
	}
	
	private TMailPart getNestedPart(TMailMessage sourceMessage,
			Session session,
			Stack<String> nestedPartStatck) throws Exception{
		TMailPart returnPart = null;
		if(!nestedPartStatck.empty()){			
			String part = nestedPartStatck.pop();		
			StringTokenizer st = new StringTokenizer(part, ":");
			int[] part2 = null;
	        if(st.countTokens() > 0){
				part2 = new int[st.countTokens()];
		        for (int i = 0; i < part2.length; i++) {
		            part2[i] = Integer.parseInt(st.nextToken());
		        }
	        } else {
	        	part2 = new int[1];
	        	part2[0] = Integer.parseInt(part);
	        }	        
	        
        	returnPart = new TMailPart(part,sourceMessage.getPart(part2));
        	if(!nestedPartStatck.empty()){
        		Object msg = returnPart.getContent();
        		TMailMessage message = null;
        		if(msg instanceof IMAPInputStream){
        			message = new TMailMessage(new MimeMessage(session,(IMAPInputStream)msg));
        		} else {
        			message = new TMailMessage((MimeMessage)msg);
        		}
        		returnPart = getNestedPart(message,session,nestedPartStatck);
        	}	        
		}
        
        return returnPart;		
	}
	
	public SendResultVO doSimpleMailSend(SendCondVO sendVO) throws Exception{
		return doSimpleMailSend(sendVO, null);
	}
	
	public SendResultVO doSimpleMailSend(SendCondVO sendVO, User user) throws Exception{		
		SendResultVO sendResultVO = new SendResultVO();
		String remoteIp = sendVO.getRemoteIp();
		String userEmail = sendVO.getSenderEmail();
		String[] userParams = userEmail.split("@");
		
		String strTo = getRcptEmail(sendVO.getToAddr());
		String strCc = getRcptEmail(sendVO.getCcAddr());
		String strBcc = getRcptEmail(sendVO.getBccAddr());
		String strRcptto = ((strTo != null) ? strTo : "")
							+ ((strCc != null) ? ","+strCc : "")
							+ ((strBcc != null) ? ","+strBcc : "");
		strRcptto = strRcptto.trim();
		SenderInfoBean info = new SenderInfoBean();
		int maxRcpt = 100;
		
		String locale = sendVO.getLocale();
		locale = (locale != null)?locale:"en";
		I18nResources msgResource = new I18nResources(new Locale(locale));
		
		String localhost = sendVO.getLocalhost();
		String mdnHost = sendVO.getMdnHost();
		
		mdnHost = (mdnHost != null)?mdnHost:localhost;
		String port = systemConfigManager.readMdnPort();
		if(StringUtils.isNotEmpty(port)){
			mdnHost = mdnHost.substring(0,mdnHost.lastIndexOf(":"));
			mdnHost += port;
		}
		
		mdnHost += EnvConstants.getMailSetting("mdn.action");
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		Protocol ladminProtocol  = null;
		try{
			
			User connectionUser = null;
			if(user == null){
				MailUserManager mailUserManager = (MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");			
				connectionUser = mailUserManager.readUserConnectionInfo(userParams[0],userParams[1]);
			} else {
				connectionUser = user;
			}
			
			if(connectionUser ==null){
				sendResultVO.setErrorOccur(true);
				sendResultVO.setErrorMsg(IErrorCode.ERR_NOT_EXIST_USER);
				return sendResultVO;
			}
			
			int userSeq = Integer.parseInt(connectionUser.get(User.MAIL_USER_SEQ));
			int domainSeq = Integer.parseInt(connectionUser.get(User.MAIL_DOMAIN_SEQ));
			String contents = sendVO.getContent();
			contents = (contents != null)?contents:"";
			String encode = sendVO.getEncode();
			encode = (encode != null)?encode:"UTF-8";
			String sendType = sendVO.getSendType();			
			sendVO.setSendType((sendType != null)?sendType:"normal");
			String editorMode = sendVO.getEditMode();
			editorMode = (editorMode != null)?editorMode:"html";
			
			Image2Cid cid = new Image2Cid();
			
			boolean isAutoSave = false;
			if ("autoSave".equalsIgnoreCase(sendType)) {
			    isAutoSave = true;
			    sendType = "draft";
			}
		            
		        info.setAutoSave(isAutoSave);
		        info.setSendType(sendType);
			info.setSendFlag((sendVO.getSendFlag()!= null)?sendVO.getSendFlag():"normal");
			info.setMaxRcpt(maxRcpt);
			info.setMdnUrl(mdnHost);
			info.setLocalUrl(localhost);
			
			info.setTo(strTo);
			info.setCc(strCc);
			info.setBcc(strBcc);
			info.setRcptto(strRcptto);
			info.setCharset(encode);
			
			info.setSubject(sendVO.getSubject());
			info.setImage2cid(cid.getImage2Cid());
			info.setPriority((sendVO.isPriority())?"2":"3");			
			
			//info.setAttlist((sendVO.getAttachListStr() != null)?sendVO.getAttachListStr():saveAttachFiles(sendVO));
			
			info.setSenderEmail(userEmail);
			info.setSenderName(sendVO.getSenderName());
			
			info.setRecvNoti(sendVO.isReceivnoti());
			info.setOneSend(sendVO.isOnesend());
			info.setSaveSent(sendVO.isSavesent());
			info.setHtmlMode("html".equals(editorMode));			
			info.setFolder(sendVO.getFolder());
			
			info.setReservYear(sendVO.getReservYear());
			info.setReservMonth(sendVO.getReservMonth());
			info.setReservDay(sendVO.getReservDay());
			info.setReservHour(sendVO.getReservHour());
			info.setReservMin(sendVO.getReservMin());
			
			info.setUids(sendVO.getUid());
			info.setDraftMid(sendVO.getDraftMid());
			
			info.setRemoteIp(remoteIp);		
			info.setUser(connectionUser);
			
			if(!info.isSendDraft() && !info.isOneSend()){
				info.setOneSend(checkFoceOnesend(domainSeq,strRcptto,connectionUser));
			}
			
			if(sendVO.isSignUse()){
				contents = contents + ((info.isHtmlMode())?"<br/>{tims_sign_pos}":"\n\r {tims_sign_pos}");
				info.setAttachSign(true);
				info.setSignData(signManager.getSignData(userSeq, sendVO.getSignSeq()));
			}
			
			if(sendVO.isVcard()){
				info.setAttachVcard(true);
				String vcard = vcardManager.getVcardString(userSeq);
				if(StringUtils.isNotEmpty(vcard))	{
					info.setVcard(vcard);	
				}				
			}
			
			if( StringUtils.isEmpty(info.getBigAttachMailContents())){
				info.setBigAttachMailContents("");
			}
			info.setContent(contents);			
			
			store = factory.connect(remoteIp, connectionUser);
			Map<String, String> configMap = mailUserManager.readUserSetting(domainSeq, userSeq);
			configMap.put("email", connectionUser.get(User.EMAIL));
			settingAttachInfo(sendVO,info,msgResource,configMap, store, factory);
			
			if(!store.isConnected()){
				store = factory.connect(remoteIp, connectionUser);
			}
			ladminProtocol = new Protocol(connectionUser.get(User.MAIL_HOST));
			ladminManager.setResource(ladminProtocol,msgResource);			
			info.setLadminManager(ladminManager);
			
			MimeMessageBuilder builder = null;
			if(info.isSendDraft()){
				builder = new DraftMimeMessageBuilder();
			} else {
				builder = new GeneralMimeMessageBuilder();
			}			
			
			SendMessageDirector director = new SendMessageDirector(builder, info);			
			MailSendResultBean sendResult = director.sendProcess(store, factory);
			sendResultVO.setMessageId(sendResult.getSaveMid());
			sendResultVO.setErrorOccur(sendResult.isErrorOccur());
			sendResultVO.setErrorMsg(sendResult.getErrorMessage());
			sendResultVO.setSendAddrs(sendResult.getAllVaildAddress());
			sendResultVO.setInvalidAddrs(sendResult.getInvalidAddress());
			sendResultVO.setSendType(sendVO.getSendType());
			
			if(sendVO.getSendType().equals("reserved")){
				sendResultVO.setSendFolderName(msgResource.getMessage("folder.reserved"));
			} else if(sendVO.getSendType().equals("draft")){
				sendResultVO.setSendFolderName(msgResource.getMessage("folder.drafts"));
			} else if(sendVO.getSendType().equals("normal")){
				sendResultVO.setSendFolderName(msgResource.getMessage("folder.sent"));
			}
			
			String delMid = sendVO.getDraftMid();
			if(delMid != null && delMid.length() > 0){
				TMailFolder draftFolder = store.getFolder(FolderHandler.DRAFTS);
				deleteTempMessage(draftFolder, delMid,user.get(User.MAIL_USER_SEQ));
			}
			
			if (!isAutoSave) {
        			rcptManager.saveLastRcpt(Integer.parseInt(connectionUser.get(User.MAIL_USER_SEQ)), 
        									sendResult.getSendAddressList());
        			writeSendMailLog(connectionUser.get(User.EMAIL), remoteIp, sendResult, info);
			}
		}catch(NullPointerException e) {
			log.error(e.getMessage(), e);
			sendResultVO.setErrorOccur(true);
			sendResultVO.setErrorMsg(e.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			sendResultVO.setErrorOccur(true);
			sendResultVO.setErrorMsg(e.getMessage());
		}finally{			
			try {
				if(store != null && store.isConnected()){
					store.close();
				}				
			} catch (Exception ignore) {
			}
			
			if(ladminProtocol != null){
				ladminProtocol.logout();
			}
		}		
	
		info.dispose();		
		
		return sendResultVO;
	}
	
	private boolean checkFoceOnesend(int domainSeq, String strRcptto, User user) {
		Map forceOnesendConfig = systemConfigManager.getForceEachSendConfig(domainSeq);
		boolean onesendCheck = false;
		boolean isCheckRcptMax = (Boolean)forceOnesendConfig.get("forceEachUse");
		int checkRcptSize = 0; 
		String forceEachRcptCnt = (String) forceOnesendConfig.get("forceEachRcptCnt");
		if(forceEachRcptCnt != null &&  forceEachRcptCnt.length() > 0){
			checkRcptSize = Integer.parseInt((String)forceOnesendConfig.get("forceEachRcptCnt"));
		}
		boolean isDomainCheck = (Boolean)forceOnesendConfig.get("forceEachOutDomain");
		String userDomain = "@"+user.get(User.MAIL_DOMAIN);
		
		if(strRcptto != null){
			String[] rcpt = strRcptto.split("[;,\r\n]");
			if(isCheckRcptMax && (rcpt.length >= checkRcptSize)){
				onesendCheck = true;			
			}
			if(!onesendCheck && isDomainCheck){
				for (int i = 0; i < rcpt.length; i++) {
					if(rcpt[i].indexOf(userDomain) < 0){
						onesendCheck = true;
						break;
					}
				}
			}
		}
		return onesendCheck;
	}
	
	private void deleteTempMessage(TMailFolder draftsFolder, String msgId,
			String userSeq) throws MessagingException {
		draftsFolder.open(true);
		long uid = draftsFolder.xsearchMID(msgId);
		if(uid > -1){
			TMailMessage msg = draftsFolder.getMessageByUID(uid,false);
			
			draftsFolder.setFlags(new TMailMessage[]{msg}, new Flags(
						Flags.Flag.DELETED), true);		
			draftsFolder.close(true);
		}
	}

	private void settingAttachInfo(SendCondVO sendVO,
			SenderInfoBean info,						
			I18nResources msgResource, 
			Map<String, String> configMap,
			TMailStore store,
			TMailStoreFactory factory) {
		
		boolean isBigAttachUse = sendVO.isBigAttachUse();		
		configMap = getAttachConfig(configMap);		
		long maxAttachSize = Long.parseLong(configMap.get("attach_maxsize")) * 1024 * 1024;
		long maxBigAttachSize = Long.parseLong(configMap.get("bigattach_maxsize")) * 1024 * 1024;		
		
		
		String bigAttachExpireDay = configMap.get("bigattach_expireday");
		String fileListStr = null;			
		StringBuffer bigAttachFileContent = new StringBuffer();
		String startDate = null;
		String endDate = null;
		List<String> linkList = null;
		int bigAttachCnt = 0;
		int normailAttachCnt = 0;
		Map<String, String> paramMap = null;			
		
		if(isBigAttachUse){
			paramMap = new HashMap<String, String>();
			Calendar calendar = new GregorianCalendar();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			calendar.setTime(new Date());
			startDate = sdf.format(new Date());
			calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(bigAttachExpireDay));
			endDate = sdf.format(calendar.getTime());
			calendar = null;
			sdf = null;
			
			paramMap.put("email", configMap.get("email"));
			paramMap.put("expiredays",bigAttachExpireDay);
			paramMap.put("regdate", Long.toString(new Date().getTime()));
			
			linkList = new ArrayList<String>();
							
			bigAttachFileContent.append("<br><br><table width='100%' border=0 cellspacing=0 cellpadding=0>");
			bigAttachFileContent.append("<tr height=27>");
			bigAttachFileContent.append("<td colspan=3 style='padding:4 0 0 10; border-top-width:0; border-right-width:0; border-bottom-width:1; border-left-width:0; border-color:rgb(216,216,216); border-style:solid;'>");
			bigAttachFileContent.append("<b><span style='font-size:13px;'>");
			bigAttachFileContent.append(msgResource.getMessage("bigattach.06"));
			bigAttachFileContent.append("</span></b>&nbsp;&nbsp;&nbsp;");
			bigAttachFileContent.append("<span style='font-size:12px;color:#666666;'>( ");
			bigAttachFileContent.append(msgResource.getMessage("bigattach.07", new Object[]{bigAttachExpireDay}));
			bigAttachFileContent.append(")</span>");
			bigAttachFileContent.append("</td><tr height=27 bgcolor=#FAFAFA style='font-size:13px;font-weight:bold'>");
			bigAttachFileContent.append("<td align=center style='padding:4 0 0 10; border-top-width:0; border-right-width:0; border-bottom-width:1; border-left-width:0; border-color:rgb(216,216,216); border-style:solid;'>");
			bigAttachFileContent.append(msgResource.getMessage("bigattach.03"));
			bigAttachFileContent.append("</td><td width=150 align=center style='padding:4 0 0 10; border-top-width:0; border-right-width:0; border-bottom-width:1; border-left-width:0; border-color:rgb(216,216,216); border-style:solid;'>");
			bigAttachFileContent.append(msgResource.getMessage("bigattach.04"));
			bigAttachFileContent.append("</td><td width=150 align=center style='padding:4 10 0 0; border-top-width:0; border-right-width:0; border-bottom-width:1; border-left-width:0; border-color:rgb(216,216,216); border-style:solid;'>");
			bigAttachFileContent.append(msgResource.getMessage("bigattach.09"));
			bigAttachFileContent.append("</td></tr>");
		}
		
		try {
			AttachFileVO[] files = sendVO.getAttachList();			
			if(files !=null){
				String tmpDir = EnvConstants.getBasicSetting("tmpdir");
				StringBuffer fileList = new StringBuffer();
				String fileUid = null;
				List<String> pathList = new ArrayList<String>();
				for (AttachFileVO attachFileVO : files) {
					BufferedOutputStream output = null;
					try {
						byte[] fileData = attachFileVO.getFiledata();
						File file = null;
						if(fileData !=null && fileData.length>1){
							if(isBigAttachUse && maxBigAttachSize < fileData.length){
								throw new Exception("Max BigAttachSize Over FileName["+attachFileVO.getName()+"]");
							} else if(maxAttachSize < fileData.length){
								throw new Exception("Max AttachSize Over FileName["+attachFileVO.getName()+"]");										
							}
							
							file = new File(tmpDir + EnvConstants.DIR_SEPARATOR + attachFileVO.getName());
							output = new BufferedOutputStream(new FileOutputStream(file));							
							output.write(fileData,0,fileData.length);
							output.flush();
							attachFileVO.setSize(fileData.length);
						} else if(attachFileVO.isUploaded()){
							file = new File(tmpDir + EnvConstants.DIR_SEPARATOR + attachFileVO.getName());
							if(!file.exists())continue;
							attachFileVO.setSize(file.length());
						} else if(attachFileVO.getDepth() != null){							
							pathList.add(attachFileVO.getName() + "|" +attachFileVO.getDepth());
						}
							
						if(isBigAttachUse && "huge".equals(attachFileVO.getType())){
							
							fileUid = saveBigAttachFile(info,sendVO.getRemoteIp(),file,attachFileVO,paramMap);
							bigAttachFileContent.append("<tr height=29 style='font-size:12px'>");
							// FILEINFO
							bigAttachFileContent.append("<td style='padding:4 0 0 20; border-top-width:0; border-right-width:0; border-bottom-width:1; border-left-width:0; border-color:rgb(216,216,216); border-style:solid;'>");
							bigAttachFileContent.append("<a href='{tims_bigattach_link_"+bigAttachCnt+"}' target='_self'><span style='text-decoration : none; color=#666666;'>");
							bigAttachFileContent.append(attachFileVO.getName());
							bigAttachFileContent.append(" (");
							bigAttachFileContent.append(FormatUtil.toUnitString(attachFileVO.getSize()));
							bigAttachFileContent.append(")</span></a></td>");            	
							// DATE
							bigAttachFileContent.append("<td width=150 align=center style='padding:4 0 0 10; border-top-width:0; border-right-width:0; border-bottom-width:1; border-left-width:0; border-color:rgb(216,216,216); border-style:solid;'>");
							bigAttachFileContent.append(startDate);
							bigAttachFileContent.append(" ~ ");
							bigAttachFileContent.append(endDate);
							bigAttachFileContent.append("</td>");
    	
							// DONWLINK
							bigAttachFileContent.append("<td width=150 align=center style='padding:4 10 0 0; border-top-width:0; border-right-width:0; border-bottom-width:1; border-left-width:0; border-color:rgb(216,216,216); border-style:solid;'>");
							bigAttachFileContent.append("<a href='{tims_bigattach_link_"+bigAttachCnt+"}' target='_self'><span style='text-decoration : none; color=#02338D;'>");
							bigAttachFileContent.append("["+msgResource.getMessage("bigattach.05")+"]</span></a>");
							bigAttachFileContent.append("</td>");
							
							
							linkList.add("email="+configMap.get("email")+"&uid="+fileUid);
							bigAttachCnt++;
						} else {									
							fileList.append(attachFileVO.getName());
							fileList.append("\t");
							fileList.append(attachFileVO.getName());
							fileList.append("\n");									
						}
						
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}finally{
						try {
							output.close();
							if(store.isConnected())store.close();
						} catch (Exception ignore) {
						}
					}
				}
				
				if(sendVO.getSendType().equalsIgnoreCase("forward")){					
					if(pathList.size() > 0){						
						TMailFolder folder = store.getFolder(sendVO.getFolder());
						folder.open(true);
						
						TMailMessage message = folder.getMessageByUID(Long.parseLong(sendVO.getUid()),false);
						Stack<String> nestedPartStatck = new Stack<String>();
						int total = pathList.size();
						TMailPart attachPart = null;
						File file = null;
						String[] fileParams = null;
						String fileName = null;
						String tempFileName = null;
						BufferedOutputStream output = null;
						InputStream in = null;
						for (int i = 0; i < total; i++) {
							nestedPartStatck.clear();
							fileParams = pathList.get(i).split("\\|");
							nestedPartStatck.push(fileParams[1]);
							attachPart = getNestedPart(message,factory.getSession(),nestedPartStatck);
							boolean isAttachRFC822 = attachPart.isMimeType("message/rfc822");							
							
							fileName = fileParams[0];
							
							tempFileName = "attach_"+Long.toString(System.nanoTime()) + "_"+configMap.get("email") + ".u";
							file = new File(tmpDir + EnvConstants.DIR_SEPARATOR + tempFileName);
							output = new BufferedOutputStream(new FileOutputStream(file));						
							
							try {
								
								in = (!isAttachRFC822)?attachPart.getInputStream():null;	
								if(isAttachRFC822){
									Object msg = attachPart.getContent();
									Enumeration enumer = null;					
					            	if(msg instanceof IMAPMessage){            		
					            		enumer = ((IMAPMessage)msg).getAllHeaderLines();            		
					            	} else if(msg instanceof MimeMessage){
					            		enumer = ((MimeMessage)msg).getAllHeaderLines();
					            	}
					            	
					            	if(enumer != null){
					            		String header = "";
					            		while (enumer.hasMoreElements()) {
							                header += (String)enumer.nextElement()+ '\n';
							            }						
					            		header += '\n';	            		
					            		output.write(header.getBytes());
					            	}
					            	if(msg instanceof IMAPMessage){
					            		in = ((IMAPMessage)msg).getRawInputStream();
					            	} else if(msg instanceof MimeMessage){
					            		in = ((MimeMessage)msg).getRawInputStream();
					            	}	            	
								}
								
								byte[] buffer = new byte[1024 * 1024];
					            int n;
					            
								while ((n = in.read(buffer, 0, buffer.length)) != -1) {
									output.write(buffer, 0, n);
					            }
								
								
							} catch (Exception ex) {
								LogManager.writeErr(this, ex.getMessage(), ex);
							} finally {
								output.close();
								in.close();
							}
							
							fileList.append(tempFileName);
							fileList.append("\t");
							fileList.append(fileName);
							fileList.append("\n");
						}
						
						folder.close(true);
					}					
				}
				fileListStr = fileList.toString();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if(isBigAttachUse){
			bigAttachFileContent.append("</table>");
			info.setBigAttach(true);
			String[] linkListStrs = new String[linkList.size()];
			linkList.toArray(linkListStrs);
			info.setBigAttachLink(linkListStrs);			
			info.setBigAttachMailContents(bigAttachFileContent.toString());
		}
		
		if(sendVO.getAttachListStr() != null){
			fileListStr = (fileListStr != null && fileListStr.length() > 0)?
					fileListStr+sendVO.getAttachListStr():sendVO.getAttachListStr();
		}
		info.setAttlist(fileListStr);
				
	}
	
	private Map<String, String> getAttachConfig(Map<String, String> configMap){
		
		if(!configMap.containsKey("bigattach_expireday")){
			configMap.put("bigattach_expireday", "7");
		} else if(!configMap.containsKey("bigattach_download")){
			configMap.put("bigattach_download", "10");
		} else if(!configMap.containsKey("bigattach_maxsize")){
			configMap.put("bigattach_maxsize", "500");			
		} else if(!configMap.containsKey("attach_maxsize")){
			configMap.put("attach_maxsize", "10");
		}
		
		return configMap;
	}
	
	private String saveBigAttachFile(SenderInfoBean info,
									String remoteAddr, 
									File attFile, 
									AttachFileVO attachFileVO,
									Map<String, String> paramMap) throws Exception{		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		
		User user = info.getUser(); 		
		Map<String, String> connectionInfoMap =  bigAttachManager.getBigAttachConnectInfo(user);
		store = factory.connect(remoteAddr,connectionInfoMap);
		MimeMessage message = new MimeMessage(factory.getSession());				
		
		TMailFolder folder = store.getFolder(FolderHandler.BIGATTACHHOME);
		if(!folder.exists()){
			folder.create();
		}		

		// Set DB
		MailBigAttachVO vo = new MailBigAttachVO();				
		vo.setAttachFlag("");
		vo.setDownloadCount(0);				
		vo.setFileName(attachFileVO.getName());
		vo.setFolderPath(user.get(User.MESSAGE_STORE) + FolderHandler.BIGATTACH);
		vo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));				
		vo.setFileSize(Long.toString(attachFileVO.getSize()));
		
		bigAttachManager.uploadBigattachFile(vo, paramMap, folder, message, new File[]{attFile});
		return vo.getMessageUid();		
	}
	
	private String getRcptEmail(String[] emails){
		String emailAddr = "";
		String email_trim = null;
		String[] email_spilit = null;
		String tmp = null;
		if(emails != null){
			for(int i = 0; i < emails.length; i++) {
				
				email_trim = TMailAddress.getTrimAddress(emails[i]);
				
				email_spilit = email_trim.split("<");
				if(email_spilit.length != 1){
					if(emailAddr.indexOf(email_spilit[1]) > -1){
						continue;
					}
					tmp = email_spilit[0].replaceAll("\"","").trim();
					if(tmp.length() > 0){
						email_trim="\""+tmp+"\"<"+email_spilit[1];
					} else {
						email_trim=email_spilit[1].replace(">", "");
					}
				} else {
					if(emailAddr.indexOf(email_trim) > -1){
						continue;
					}
				}
				
				emailAddr += email_trim;
				
				if((i+1) != emails.length) {			
					emailAddr += ",";
				}
							
			}
		}
		email_trim = null;
		email_spilit = null;
		tmp = null;
		
		return emailAddr; 
	
	}
	
	public WriteResultVO doSimpleMailWrite(WriteCondVO writeVO) throws Exception{		
		return doSimpleMailWrite(writeVO, null);
	}
	
	public WriteResultVO doSimpleMailWrite(WriteCondVO writeVO, User user) throws Exception{
		
		String userEmail = writeVO.getUserEmail();
		String[] userParams = userEmail.split("@");
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		WriteResultVO writeResultVO = null;
		try {
			User connectionUser = null;
			MailUserManager mailUserManager = (MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");
			if(user == null){
				connectionUser = mailUserManager.readUserConnectionInfo(userParams[0],userParams[1]);
			} else {
				connectionUser = user;
			}
			
			I18nResources msgResource = new I18nResources(new Locale(writeVO.getLocale()));
			
			store = factory.connect(writeVO.getRemoteIp(), connectionUser);
			mailManager.setProcessResource(store, msgResource);			
			
			int mailDomainSeq = Integer.parseInt(connectionUser.get(User.MAIL_DOMAIN_SEQ));
			int mailUserSeq = Integer.parseInt(connectionUser.get(User.MAIL_USER_SEQ));
			
			Map<String, String> configMap = mailUserManager.readUserSetting(mailDomainSeq,mailUserSeq);
			
			MessageWriteInfoBean writeInfo = new MessageWriteInfoBean();
			writeInfo.setMobileMode(writeVO.isMobileMode());
			writeInfo.setWriteType(writeVO.getWriteType());
			writeInfo.setUids(writeVO.getUids());
			writeInfo.setFolderName(writeVO.getFolderName());
			
			writeInfo.setReqTo(writeVO.getReqTo());
			writeInfo.setReqCc(writeVO.getReqCc());
			writeInfo.setReqBcc(writeVO.getReqBcc());
			writeInfo.setReqSubject(writeVO.getReqSubject());
			writeInfo.setReqContent(writeVO.getReqContent());
						
			writeInfo.setForwardingMode(writeVO.getForwardingMode());			
			writeInfo.setSignInside(writeVO.isSignInside());
			
			UserEtcInfoVO userSettingVo = userSettingManager.readUserEtcInfo(mailUserSeq);	
			MailWriteMessageBean writeBean = mailManager.getWriteSettingBean(writeInfo, user);
			
			String senderName = userSettingVo.getSenderName();
			String senderEmail = userSettingVo.getSenderEmail();
			writeBean.setEditorMode(userSettingVo.getWriteMode());
			writeBean.setSaveSent(userSettingVo.getSaveSendBox());
			writeBean.setReceiveNoti(userSettingVo.getReceiveNoti());
			writeBean.setEncoding(userSettingVo.getCharSet());			
			writeBean.setVcardAttach(userSettingVo.getVcardAttach());
			writeBean.setSenderName((senderName == null || 
									senderName.trim().length() == 0)?
									user.get(User.USER_NAME):senderName);
			writeBean.setSenderEmail((senderEmail == null || 
									senderEmail.trim().length() == 0)?
									user.get(User.EMAIL):userSettingVo.getSenderEmail());
			
			writeBean.setSignAttach(getSignApply(configMap));
			
			writeResultVO = parseWriteBean(writeBean,configMap);			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		
		return writeResultVO;
	}
	
	private WriteResultVO parseWriteBean(MailWriteMessageBean writeBean,Map<String, String> configMap){
		WriteResultVO writeResultVO = null;
		
		writeResultVO = new WriteResultVO();
		writeResultVO.setTo(writeBean.getTo());
		writeResultVO.setCc(writeBean.getCc());
		writeResultVO.setBcc(writeBean.getBcc());
		writeResultVO.setSubject(writeBean.getSubject());
		writeResultVO.setEditorMode(writeBean.getEditorMode());
		writeResultVO.setEncoding(writeBean.getEncoding());
		writeResultVO.setSignAttach(writeBean.getSignAttach());
		writeResultVO.setForwardingMode(writeBean.getForwardingMode());
		writeResultVO.setSenderEmail(writeBean.getSenderEmail());
		writeResultVO.setSenderName(writeBean.getSenderName());
		writeResultVO.setSignLocation(getSignLocation(configMap));	
		writeResultVO.setReceiveNoti(writeBean.getReceiveNoti());
		writeResultVO.setDraftMsgId(writeBean.getDraftMsgId());
		writeResultVO.setSaveSent(writeBean.getSaveSent());
		writeResultVO.setVcardAttach(writeBean.getVcardAttach());
		writeResultVO.setUidsValue(writeBean.getUidsValue());
		writeResultVO.setWriteType(writeBean.getWriteType());
		writeResultVO.setSendFlag(writeBean.getSendFlag());
		writeResultVO.setFolderName(writeBean.getFolderName());
		
		writeResultVO.setHtmlContent(writeBean.getHtmlContent());
		writeResultVO.setTextContent(writeBean.getTextContent());
		
		String textContents = writeBean.getTextNormalContent();
		String htmlContents = writeBean.getHtmlNormalContent();	
		textContents = (textContents !=null && textContents.trim().length() > 0)?textContents:htmlContents;
		writeResultVO.setHtmlNormalContent(htmlContents);		
		writeResultVO.setTextNormalContent(textContents);
		
				
		return writeResultVO;
	}
	
	private String getSignApply(Map<String, String> configMap){
		
		String signApply = "F";
		if(configMap != null && configMap.containsKey("sign_apply")){
			signApply = (String)configMap.get("sign_apply");
		}
		return signApply;
	}
	
	private String getSignLocation(Map<String, String> configMap){
		
		String signLocation = "outside";
		if(configMap != null && configMap.containsKey("sign_location")){
			signLocation = (String)configMap.get("sign_location");
		}
		return signLocation;
	}
	
	public MailWorkResultVO doSimpleMailWork(MailWorkCondVO workVO){
		return doSimpleMailWork(workVO, null);
	}
	
	public MailWorkResultVO doSimpleMailWork(MailWorkCondVO workVO, User user){
		String userEmail = workVO.getUserEmail();
		String[] userParams = userEmail.split("@");
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources msgResource = new I18nResources(new Locale(workVO.getLocale()));
		MailWorkResultVO workResultVO = null;
		try {
			User connectionUser = null;
			MailUserManager mailUserManager = (MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");
			if(user == null){
				connectionUser = mailUserManager.readUserConnectionInfo(userParams[0],userParams[1]);
			} else {
				connectionUser = user;
			}		
			try {			
				store = factory.connect(workVO.getRemoteIp(), connectionUser);
			} catch (Exception e) {			
				throw new Exception(msgResource.getMessage("error.imapconn"));
			}
			workVO.setUserEmail(connectionUser.get(User.EMAIL));			
			mailManager.setProcessResource(store, msgResource);			
			try {				
				workResultVO = processMessageWork(workVO);
			}catch (Exception e) {
				log.error(e.getMessage(), e);
				throw e;
			} finally{
				if(store !=null && store.isConnected())
					store.close();
			}
			
		}catch (Exception e) {
			if(workResultVO == null){
				workResultVO = new MailWorkResultVO();
			}
			workResultVO.setErrorOccur(true);
			workResultVO.setErrorMessage(e.getMessage());
		}
		
		return workResultVO;
	}
	
	private MailWorkResultVO processMessageWork(MailWorkCondVO workVO) throws Exception{		
		int JOBSEQ = -1;
		String workStr = "";
		String workType = workVO.getWorkMode();
		Map paramMap = null;
		if(("delete").equals(workType)){
			workStr = "action_message_delete";
			JOBSEQ = 0;
		} else if(("copy").equals(workType)){
			workStr = "action_message_copy";
			JOBSEQ = 1;
		} else if(("move").equals(workType)){
			workStr = "action_message_move";
			JOBSEQ = 2;
		} else if(("flag").equals(workType)){
			workStr = "action_message_flag";
			JOBSEQ = 3;
			paramMap = new HashMap();
			paramMap.put("flagType", workVO.getFlagType());
			paramMap.put("flagUse", workVO.isFlagUse());
		} else if(("empty").equals(workType)){
			workStr = "empty_folder";
			JOBSEQ = 4;			
		}
		
		
		MailWorkResultVO workResultVO = new MailWorkResultVO();		
		long[] uids = FormatUtil.parseStringToLong(workVO.getUid());
		String[] folderNames = workVO.getFolderName();		
		String errorFolder = "";
		
		
		try {
			if(uids != null && folderNames != null){
				if(folderNames.length > 1){
					long[] multiUid = null;
					for (int i = 0; i < folderNames.length; i++) {
						multiUid = new long[]{uids[i]};
						processMessageHandle(JOBSEQ, multiUid, folderNames[i],workVO.getTargetFolderName(),paramMap);
						errorFolder = folderNames[i];
						writeMailLog(false,workVO.getUserEmail(),workVO.getRemoteIp(),workStr, folderNames[i], "", Long.toString(uids[i]));
					}
				} else {
					processMessageHandle(JOBSEQ, uids, folderNames[0],workVO.getTargetFolderName(),paramMap);					
					errorFolder = folderNames[0];
					writeMailLog(false,workVO.getUserEmail(),workVO.getRemoteIp(),workStr, folderNames[0], "", StringUtils.getLongsToString(uids));
				}
			}						
			workResultVO.setWorkType(workType);
		} catch (Exception e) {
			e.printStackTrace();
			LogManager.writeErr(log, workStr + " folder["+errorFolder+"]");
			throw new Exception(workStr + "error folder["+errorFolder+"]");
		}
		
		return workResultVO;
	}
	
	private void processMessageHandle(int JOBSEQ, long[] uids, 
			String folderName,
			String targetFolderName,
			Map paramMap) 
	throws Exception{
		final int DELETE = 0;
		final int COPY = 1;
		final int MOVE = 2;
		final int FLAG = 3;
		final int EMPTY = 4;
		
		switch (JOBSEQ) {
		case DELETE:
			mailManager.deleteMessage(uids, folderName);
			break;
		case COPY:
			mailManager.copyMessage(uids, folderName, targetFolderName);
			break;
		case MOVE:
			mailManager.moveMessage(uids, folderName, targetFolderName);
			break;			
		case FLAG:
			mailManager.switchMessagesFlags(uids, folderName, (String)paramMap.get("flagType"), (Boolean)paramMap.get("flagUse"));
			break;			
		case EMPTY:
			mailManager.emptyFolder(folderName);
			break;
		}
	}
	
		
	
	public void writeMailLog(boolean isJobLog, String email, String remoteIp, 
								String action, String folder, String toAddrs, 
		String fromAddrs, long mailSize, String subject, String mid){
	
		LogManager.writeMailLogMsg(isJobLog, log, email, 
				remoteIp, action, 
				TMailUtility.IMAPFolderEncode(folder.replaceAll(" ", "/")),
				toAddrs, 
				fromAddrs, mailSize, EnvConstants.DEFAULT_CHARSET, subject, mid);			
	}
	
	public void writeMailLog(boolean isJobLog, String email, String remoteIp, String action, String toFolder, 
		String fromFolder, String uid){
	
		fromFolder = (fromFolder != null)?toFolder:"";
		toFolder = (toFolder != null)?toFolder:"";
		LogManager.writeMailLogMsg(isJobLog, log, email,remoteIp, action, 
				TMailUtility.IMAPFolderEncode(toFolder.replaceAll(" ", "/")), 
				TMailUtility.IMAPFolderEncode(fromFolder.replaceAll(" ", "/")),uid);	
	}
	
	public void writeMailLog(boolean isJobLog, String email, String remoteIp, String action){
		LogManager.writeMailLogMsg(isJobLog, log, email,remoteIp, action);			
	}
	
	public void writeSendMailLog(String email, String remoteIp, MailSendResultBean sendResult, SenderInfoBean info){
		if(sendResult != null && !sendResult.isErrorOccur()){
			String action = "";
			String folder = "";
			boolean isJobLog = false;
			
			if(info.isSaveSent()){
				folder = "Sent";
			}
			if("normal".equals(sendResult.getSendResultType())){
				action = "action_message_send";
				isJobLog = true;
			} else if("reserved".equals(sendResult.getSendResultType())){
				action = "action_message_reserved";
				folder = "Reserved";
				isJobLog = false;
			} else if("draft".equals(sendResult.getSendResultType())){
				action = "action_message_draft";
				folder = "Drafts";
				isJobLog = true;
			} else if("batch".equals(sendResult.getSendResultType())){
				action = "action_message_send";
				isJobLog = true;
				writeMailLog(false, email, remoteIp, "action_message_batch", folder, 
								sendResult.getAllVaildAddress(), "", 
								sendResult.getMailSize(), info.getSubject(), sendResult.getSaveMid());
			}
			writeMailLog(isJobLog, email, remoteIp, action, folder, 
					sendResult.getAllVaildAddress(), "", 
					sendResult.getMailSize(), info.getSubject(), sendResult.getSaveMid());
						
		}
	}
	public FolderInfoVO doSimpleMailFolder(FolderCondVO folderCond) throws Exception{
		return doSimpleMailFolder(folderCond,null);
	}
	
	public FolderInfoVO doSimpleMailFolder(FolderCondVO folderCond, User user) throws Exception{
		String userEmail = folderCond.getUserEmail();
		String[] userParams = userEmail.split("@");
		FolderInfoVO folderInfo = new FolderInfoVO();
		try {
			
			I18nResources msgResource = new I18nResources(new Locale(folderCond.getLocale()));
			User connectionUser = null;
			TMailStore store = null;
			TMailStoreFactory factory = new TMailStoreFactory();
			
			MailUserManager mailUserManager = (MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");
			if(user == null){
				connectionUser = mailUserManager.readUserConnectionInfo(userParams[0],userParams[1]);
			} else {
				connectionUser = user;
			}		
						
			store = factory.connect(folderCond.getRemoteIp(), connectionUser);
			
			int userSeq = Integer.parseInt(connectionUser.get(User.MAIL_USER_SEQ));			
			mailManager.setProcessResource(store, msgResource);			
			try {
				folderInfo.setDefaultFolders(parseFolderInfo(mailManager.getFolderList(EnvConstants.DEFAULT_FOLDER, false, userSeq)));
				folderInfo.setUserFolders(parseFolderInfo(mailManager.getFolderList(EnvConstants.USER_FOLDER, false, userSeq)));
			}catch (Exception e) {
				log.error(e.getMessage(), e);
				throw e;
			} finally{
				if(store !=null && store.isConnected())
					store.close();
			}
			
		}catch (Exception e) {
			LogManager.writeErr(log, e.getMessage(), e);
		}
		
		return folderInfo;
	}
	
	public FolderContentVO[] parseFolderInfo(MailFolderBean[] folderBeans) throws Exception{
		FolderContentVO[] folderInfoVOs = null;
		
		if(folderBeans != null){
			folderInfoVOs = new FolderContentVO[folderBeans.length];
			for (int i = 0; i < folderBeans.length; i++) {
				folderInfoVOs[i] = new FolderContentVO();
				folderInfoVOs[i].setFullFolderName(folderBeans[i].getFullName());
				folderInfoVOs[i].setFolderName(folderBeans[i].getName());
				folderInfoVOs[i].setEncFolderName(folderBeans[i].getEncName());
				folderInfoVOs[i].setDepth(folderBeans[i].getDepth());
				folderInfoVOs[i].setTotal(folderInfoVOs[i].getTotal());
				folderInfoVOs[i].setUnseen(folderInfoVOs[i].getUnseen());
			}
			
		}
		
		return folderInfoVOs;		
	}
	
	public FolderContentVO doSimpleMailFolderContent(FolderCondVO folderCond) throws Exception{
		return doSimpleMailFolderContent(folderCond,null);
	}
	
	public FolderContentVO doSimpleMailFolderContent(FolderCondVO folderCond, User user) throws Exception{
		String userEmail = folderCond.getUserEmail();
		String[] userParams = userEmail.split("@");
		FolderContentVO folderContentVO = new FolderContentVO();
		try {
			
			I18nResources msgResource = new I18nResources(new Locale(folderCond.getLocale()));
			User connectionUser = null;
			TMailStore store = null;
			TMailStoreFactory factory = new TMailStoreFactory();
			
			MailUserManager mailUserManager = (MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");
			if(user == null){
				connectionUser = mailUserManager.readUserConnectionInfo(userParams[0],userParams[1]);
			} else {
				connectionUser = user;
			}		
						
			store = factory.connect(folderCond.getRemoteIp(), connectionUser);
			
			int userSeq = Integer.parseInt(connectionUser.get(User.MAIL_USER_SEQ));			
			mailManager.setProcessResource(store, msgResource);			
			try {
				TMailFolder folder = mailManager.getFolder(folderCond.getFolderName());
				folderContentVO.setDepth(folder.getFolderDepth());
				folderContentVO.setEncFolderName(folder.getEncName());
				folderContentVO.setFolderName(folder.getName());
				folderContentVO.setTotal(folder.getMessageCount());
				folderContentVO.setUnseen(folder.getUnreadMessageCount());
			}catch (Exception e) {
				log.error(e.getMessage(), e);
				throw e;
			} finally{
				if(store !=null && store.isConnected())
					store.close();
			}
			
		}catch (Exception e) {
			LogManager.writeErr(log, e.getMessage(), e);
		}
		
		return folderContentVO;
	}
	
	public MdnListVO doSimpleMdnList(ListCondVO listVO) throws Exception{
		return doSimpleMdnList(listVO, null);
	}
	
	public MdnListVO doSimpleMdnList(ListCondVO listVO, User user) throws Exception{
		String email = listVO.getEmail();
		String[] userParams = email.split("@");
		I18nResources msgResource = new I18nResources(new Locale(listVO.getLocale()));
		
		String folderName = FolderHandler.SENT;
		
		String sortBy = listVO.getSortBy();
		String sortDir = listVO.getSortDir();
		String remoteIp = listVO.getRemoteIp();
				
		if (StringUtils.isEmpty(sortBy))
			sortBy = "arrival";
		
		if (StringUtils.isEmpty(sortDir))
			sortDir = "desc";		
		
		if(StringUtils.isEmpty(remoteIp))
			remoteIp = "127.0.0.1";
		
		
		String[] exceptFolders = null;
		
		exceptFolders = new String[4];
		exceptFolders[0] = FolderHandler.RESERVED;
		exceptFolders[1] = FolderHandler.DRAFTS;
		exceptFolders[2] = FolderHandler.SPAM;
		exceptFolders[3] = FolderHandler.TRASH;		
		
		MessageSortInfoBean sortBean = new MessageSortInfoBean();
		MdnListVO mdnListVO = null;
		MdnInfoVO[] mdnVO = null;
		
		int page = listVO.getPage();
		int pageBase = listVO.getPageBase();
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		
		try {
			User connectionUser = null;
			if(user == null){
				MailUserManager mailUserManager = (MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");			
				connectionUser = mailUserManager.readUserConnectionInfo(userParams[0],userParams[1]);
			} else {
				connectionUser = user;
			}
			store = factory.connect(remoteIp, connectionUser);
			mailManager.setProcessResource(store, msgResource);
			
			sortBean.setSortBy(listVO.getSortBy());
			sortBean.setSortDir(listVO.getSortDir());			
			sortBean.setPage(Integer.toString(page));
			sortBean.setPageBase(Integer.toString(pageBase));
			sortBean.setPattern(listVO.getKeyWord());
			
			int total = mailManager.getSortTotal(store, folderName, sortBean);
			
			int npages = (int) Math.ceil((double) total / pageBase);
			if (0 < npages && npages < page) {
				page = npages;				
			}			
			sortBean.setPage(Integer.toString(page));
			sortBean.setPageBase(Integer.toString(pageBase));	
			
			MailMessageBean[] messageBeans = mailManager.getMessageBeans(store, folderName, sortBean);
			mdnVO = parseMdnInfo(messageBeans);
			
			mdnListVO = new MdnListVO();
			mdnListVO.setMdnList(mdnVO);
			mdnListVO.setTotalCnt(total);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally {
			try {
				if(store !=null && store.isConnected())
					store.close();
			} catch (MessagingException ignore) {
				log.error(ignore.getMessage(), ignore);
			}
			
		}
		
		return mdnListVO;
	}
	
	private MdnInfoVO[] parseMdnInfo(MailMessageBean[] mailBeans) throws Exception{
		MdnInfoVO[] mdnInfos = null;
		if(mailBeans != null){
			mdnInfos = new MdnInfoVO[mailBeans.length];
			for (int i = 0; i < mailBeans.length; i++) {
				mdnInfos[i] = new MdnInfoVO();
				mdnInfos[i].setDate(mailBeans[i].getSendDateForList());
				mdnInfos[i].setSubject(mailBeans[i].getSubject());
				mdnInfos[i].setReadCnt(mailBeans[i].getMDNReadCount());
				mdnInfos[i].setMdnCnt(mailBeans[i].getMDNCount());
				mdnInfos[i].setTo(mailBeans[i].getSendToSimple());
				mdnInfos[i].setUid(mailBeans[i].getId());
				if (mailBeans[i].getMDNCount() == 1) {
					mdnInfos[i].setCode(mailBeans[i].getMDNResponses()[0].getCode());
					mdnInfos[i].setRdate(mailBeans[i].getMDNResponses()[0].getSentDate3());
				}
			}
			
		}
		
		return mdnInfos;
	}
	
	public MdnContentVO doSimpleMdnView(ReadCondVO readVO) throws Exception{
		return doSimpleMdnView(readVO, null);
	}
	public MdnContentVO doSimpleMdnView(ReadCondVO readVO, User user) throws Exception{
		String email = readVO.getEmail();
		String[] userParams = email.split("@");
		I18nResources msgResource = new I18nResources(new Locale(readVO.getLocale()));
		String remoteIp = readVO.getRemoteIp();
		
		MdnContentVO mdnVO = null;
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		
		String folderName = FolderHandler.SENT;
		TMailFolder ufolder = null;
		try {
			
			User connectionUser = null;
			if(user == null){
				MailUserManager mailUserManager = (MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");			
				connectionUser = mailUserManager.readUserConnectionInfo(userParams[0],userParams[1]);
			} else {
				connectionUser = user;
			}		
			
			store = factory.connect(remoteIp, connectionUser);
			mailManager.setProcessResource(store, msgResource);
			ufolder = store.getFolder(folderName);	
			ufolder.open(true);
			
			MessageSortInfoBean sortBean = new MessageSortInfoBean();
			sortBean.setSortBy(readVO.getSortBy());
			sortBean.setSortDir(readVO.getSortDir());
			if (readVO.isAdvancedSearch()) {
				sortBean.setAdSearchCategory(readVO.getCategory());
				sortBean.setAdvanceMode(true);
				sortBean.setAdSearchPattern(readVO.getKeyWord());
				sortBean.setAdFromEmailPattern(readVO.getFromEmailPattern());
				sortBean.setAdToEmailPattern(readVO.getToEmailPattern());
			} else {
				sortBean.setPattern(readVO.getKeyWord());				
			}
			 
			long[] neighborUID = mailManager.getNeighborUID(ufolder, readVO.getUid(), sortBean);
			
			MailUserManager mailUserManager = (MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager"); 
			Map<String,Long> localDomainMap = mailUserManager.getLocalDomainMap();
			
			MDNResponsesBean mdnBean = 
				mailManager.getMDNResponsesContent(folderName, Long.toString(readVO.getUid()), 
						localDomainMap, Integer.toString(readVO.getPage()), 
						Integer.toString(readVO.getPageBase()), "");
						
			mdnVO = parseMdnContent(mdnBean);
			mdnVO.setPreUid(neighborUID[0]);
			mdnVO.setNextUid(neighborUID[1]);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}finally {
			try {
				if(ufolder !=null && ufolder.isOpen())
					ufolder.close(true);
				if(store !=null && store.isConnected())
					store.close();
			} catch (MessagingException ignore) {
				log.error(ignore.getMessage(), ignore);
			}
			
		}
		return mdnVO;
	}
	
	private MdnContentVO parseMdnContent(MDNResponsesBean mdnBean) throws Exception{
		MdnContentVO mdnVO = null;
		if(mdnBean != null){
			mdnVO = new MdnContentVO();
			mdnVO.setMdnResponseTotal(mdnBean.getMdnResponseTotal());
			mdnVO.setMdnResponsePage(mdnBean.getMdnResponsePage());
			mdnVO.setMessageTitle(mdnBean.getMessageTitle());
			mdnVO.setMessageID(mdnBean.getMessageID());
			mdnVO.setSendDate(mdnBean.getSendDate());
			mdnVO.setCountTotal(mdnBean.getCountTotal());
			mdnVO.setCountRead(mdnBean.getCountRead());
			mdnVO.setCountUnseen(mdnBean.getCountUnseen());
			mdnVO.setCountFail(mdnBean.getCountFail());
			mdnVO.setCountRecall(mdnBean.getCountRecall());
			mdnVO.setCountEtc(mdnBean.getCountEtc());
			mdnVO.setRcptVos(parseMdnRcptContent(mdnBean.getRcptVos()));			
		}
		
		return mdnVO;
	}
	
	private MdnRcptContentVO[] parseMdnRcptContent(MDNResponsesRcptBean[] rcptBeans) throws Exception{
		MdnRcptContentVO[] rcptContents = null;
		if(rcptBeans != null){
			rcptContents = new MdnRcptContentVO[rcptBeans.length];
			for (int i = 0; i < rcptContents.length; i++) {
				rcptContents[i] = new MdnRcptContentVO();
				rcptContents[i].setAddress(rcptBeans[i].getAddress());
				rcptContents[i].setPersonal(rcptBeans[i].getPersonal());
				rcptContents[i].setProcessDate(rcptBeans[i].getProcessDate());
				rcptContents[i].setMessage(rcptBeans[i].getMessage());
				rcptContents[i].setStatus(rcptBeans[i].getStatus());
				rcptContents[i].setCode(rcptBeans[i].getCode());
				rcptContents[i].setLocalDomain(rcptBeans[i].isLocalDomain());
			}
		}
		
		return rcptContents;
	}
	
	public void doSimpleMdnRecall(MdnRecallVO mdnRecallVO) throws Exception {
		doSimpleMdnRecall(mdnRecallVO, null);
	}
	
	public void doSimpleMdnRecall(MdnRecallVO mdnRecallVO, User user) throws Exception {
		
		String email = mdnRecallVO.getEmail();
		String[] userParams = email.split("@");
		I18nResources msgResource = new I18nResources(new Locale(mdnRecallVO.getLocale()));
		
		String recallEmails = mdnRecallVO.getRecallEmails();
		String[] recallEmailArray = recallEmails.split("\\|");
		
		String mid = mdnRecallVO.getMid();
		String remoteAddr = mdnRecallVO.getRemoteIp();
		
		TMailStore store = null;
		TMailStore rstore = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		
		try {
			Map<String,Long> localDomainMap = mailUserManager.getLocalDomainMap();
			String emailVals[] = null;
			User recallUser = null;
			TMailFolder folder = null;
			List<String> failList = new ArrayList<String>();			
			List<String> successList = new ArrayList<String>();
			String domainName = null;
			String userId = null;
			long tmpSeq = 0;
			
			for (String rcptEmail : recallEmailArray) {
				emailVals = rcptEmail.split("@");
				userId = emailVals[0];
				domainName = emailVals[1];				
				if(localDomainMap.containsKey(domainName)){
					tmpSeq = localDomainMap.get(emailVals[1]);
					domainName = mailUserManager.getDoaminName((int)tmpSeq);
				}
				recallUser = mailUserManager.readUserAuthInfo(userId, domainName);
				if(recallUser == null){
					userId = mailUserManager.getAlternateId(userId, domainName);
					if(userId != null){
						recallUser = mailUserManager.readUserAuthInfo(userId, domainName);
					}
				}				
				
				try {
					rstore = factory.connect(remoteAddr, recallUser);
					folder = rstore.getDefaultFolder();					
					folder.xrecall(mid);
					successList.add(rcptEmail);
				} catch (Exception e) {
					LogManager.writeErr(this, e.getMessage(), e);
					failList.add(rcptEmail);
					continue;
				} finally {
					if(rstore != null && rstore.isConnected())
						rstore.close();					
				}				
			}
			
			User connectionUser = null;
			if(user == null){
				MailUserManager mailUserManager = (MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");			
				connectionUser = mailUserManager.readUserConnectionInfo(userParams[0],userParams[1]);
			} else {
				connectionUser = user;
			}
			
			store = factory.connect(remoteAddr, connectionUser);
			mailManager.setProcessResource(store, msgResource);
			folder = store.getDefaultFolder();
			for (String rcptEmail : successList) {				
				long time = System.currentTimeMillis() / 1000;					
				try {						
					folder.xsetMDN(mid, rcptEmail, "", time, "1");
				} catch(MessagingException e) {
					LogManager.writeErr(this, e.getMessage(), e);
				}				
			}
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}finally {
			if(store !=null && store.isConnected())
				store.close();
		}
	}
	
	public void doSimpleMdnSendMail(ReadCondVO readCondVO) throws Exception {
	    doSimpleMdnSendMail(readCondVO, null);
	}
	    
        public void doSimpleMdnSendMail(ReadCondVO readVO, User user) throws Exception {
            String email = readVO.getEmail();
            String[] userParams = email.split("@");
            I18nResources msgResource = new I18nResources(new Locale(readVO.getLocale()));
            String remoteIp = readVO.getRemoteIp();
            TMailStore store = null;
            TMailStoreFactory factory = new TMailStoreFactory();
            TMailFolder folder = null;
            try {

                User connectionUser = null;
                if (user == null) {
                    MailUserManager mailUserManager = (MailUserManager) ApplicationBeanUtil
                            .getApplicationBean("mailUserManager");
                    connectionUser = mailUserManager.readUserConnectionInfo(userParams[0], userParams[1]);
                } else {
                    connectionUser = user;
                }
	            
                store = factory.connect(remoteIp, connectionUser);
                mailManager.setProcessResource(store, msgResource);
                folder = store.getFolder(readVO.getFolder());
                folder.open(true);
                TMailMessage message = folder.getMessageByUID(readVO.getUid(), true);
                message.setMDNAddresses(message.getHeader("Disposition-Notification-To"));
                MimeMessage mdnmsg = new MimeMessage(factory.getSession());
                String subject = message.getSubject();
                subject = "[" + msgResource.getMessage("mail.mdn.read") + "] "
                        + ((subject != null) ? subject : msgResource.getMessage("header.nosubject"));

	            mdnmsg.setFrom(new InternetAddress(email));
	            mdnmsg.setRecipients(Message.RecipientType.TO, message.getMDNAddresses());
	            mdnmsg.setSentDate(new Date());
	            mdnmsg.setHeader("References", message.getMessageID());
	            mdnmsg.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));

	            MimeMultipart mp = new MimeMultipart();
	            MimeBodyPart part1 = new MimeBodyPart();
	            MimeBodyPart part2 = new MimeBodyPart();

	            mp.setSubType("report; report-type=disposition-notification");

	            String s1 = msgResource.getMessage("mail.mdn.response.001")+ "<" + email + ">";
	            String s2 = msgResource.getMessage("mail.mdn.response.002")+": " + message.getSentDate();
	            String s3 = msgResource.getMessage("mail.mdn.response.003")+ message.getReceivedDate();

	            part1.setContent(s1 + "\n" + s2 + "\n" + s3, "text/plain; charset=utf-8");
	                
	            String fr = "Final-Recipient: rfc822;" + email;
	            String omid = "Original-Message-ID: " + message.getMessageID();; 
	            String dpt = "Disposition: manual-action/MDN-sent-manually; displayed";

	            part2.setContent(fr + "\n" + omid + "\n" + dpt, "message/disposition-notification");
	            mp.addBodyPart(part1);
	            mp.addBodyPart(part2);
	            mdnmsg.setContent(mp);

	            mp.addBodyPart(part1);
	            mp.addBodyPart(part2);
	            mdnmsg.setContent(mp);

	            SendHandler.simpleSendMessage(factory.getSession(), new MimeMessage[] { mdnmsg },
	                    new InternetAddress[][] { message.getMDNAddresses() });

	            folder.close(true);
	        } catch (Exception e) {
	            log.error(e.getMessage(), e);
	            throw e;
	        } finally {
	            try {
	                if (folder != null && folder.isOpen()) {
	                        folder.close(true);
	                }
	                if (store != null && store.isConnected()) {
	                    store.close();
	                }
	            } catch (MessagingException ignore) {
	            	log.error(ignore.getMessage(), ignore);
	            }

	        }
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
}




