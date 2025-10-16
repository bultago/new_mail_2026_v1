package com.terracetech.tims.webmail.mail.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.binary.Base64;

import com.sun.mail.imap.IMAPInputStream;
import com.sun.mail.imap.IMAPMessage;
import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class ReadNestedMessageAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1856899814661722550L;
	
	private MailManager mailManager = null;
	private SettingManager userSettingManager = null;
	private TMailMessage message;
	
	private String htmlContent;
	
	private TMailPart[] files;
	private TMailPart[] vcards;
	private String[] imageAttach;
	
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	public void setUserSettingManager(SettingManager userSettingManager) {
		this.userSettingManager = userSettingManager;
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
	
	public String execute() throws Exception{		
		String userId = user.get(User.MAIL_UID);
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		Locale locale = I18nConstants.getUserLocale(request);
		
		String sharedFlag = request.getParameter("sharedFlag");
		boolean isShared = (sharedFlag != null && sharedFlag.equals("shared"))?true:false;
		String sharedUserSeq = request.getParameter("sharedUserSeq");
		String sharedFolderName = request.getParameter("sharedFolderName");	
		
		String uid = request.getParameter("uid");
		String folder = request.getParameter("folder");	
		String part = request.getParameter("part");
		String nestedPart = request.getParameter("nestedPart");
		
		if (StringUtils.isEmpty(folder))
			folder = FolderHandler.INBOX;
		
		if(isShared){
			folder = sharedFolderName;
		}	
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder ufolder = null;
		MailMessageBean messageBean = null;
		Stack<String> nestedPartStatck = new Stack<String>();		
		try {
			if(uid == null)throw new Exception("USER["+userId+"] ReadNestedMessage UID is null!");
			if(folder == null)throw new Exception("USER["+userId+"] ReadNestedMessage FolderName is null!");
			
			if(nestedPart != null && nestedPart.length() > 0){
				String[] parts = nestedPart.split("\\|");
				for (int i = (parts.length-1); i >= 0; i--) {
					nestedPartStatck.push(parts[i]);
				}
			} else {
				nestedPart = part;
				nestedPartStatck.push(part);
			}			
			
			store = factory.connect(isShared, sharedUserSeq, request.getRemoteAddr(), user);
			ufolder = store.getFolder(folder);
			ufolder.open(false);
			
			TMailMessage sourceMessage = ufolder.getMessageByUID(Long.parseLong(uid),false);		
	        
	        TMailPart attachPart = getNestedPart(sourceMessage,factory.getSession(),nestedPartStatck);
			
			UserEtcInfoVO userSettingVo = userSettingManager.readUserEtcInfo(userSeq);
			MessageParserInfoBean parserInfo = getMessagePaserInfoBean();
			parserInfo.setHiddenImg(false);						
			parserInfo.setHiddenTag("on".equalsIgnoreCase(userSettingVo.getHiddenTag()));
			
			parserInfo.setLocale(locale);
			Object msg = attachPart.getContent();
			Enumeration enumer = null;	
			if (msg instanceof InputStream) {
		            msg = new MimeMessage(factory.getSession(), (InputStream) msg);
		    }else if(msg instanceof IMAPMessage){  
        		enumer = ((IMAPMessage)msg).getAllHeaderLines();            		
        	}else if(msg instanceof MimeMessage){
        		enumer = ((MimeMessage)msg).getAllHeaderLines();
        	}
			if(enumer!=null){
				if (enumer != null) {
					boolean isBase64 = false;
					StringBuffer base64String = new StringBuffer();
					while (enumer.hasMoreElements()) {
						byte[] bytes = ((String) enumer.nextElement()).getBytes();
						if(!isBase64)	isBase64 = Base64.isArrayByteBase64(bytes);
						if(!isBase64){
						}else{
							base64String.append(new String(bytes));
							base64String.append("\n");
						}
					}
					if(isBase64){
						ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decodeBase64(base64String.toString().getBytes()));
						msg = new MimeMessage(factory.getSession(),byteArrayInputStream);
					}


				}
			}
			messageBean = mailManager.getMessageBean(ufolder, 
						factory.getSession(), 
						(MimeMessage)msg, 
						parserInfo);
			
			message = messageBean.getMessage();			
			TMailPart[] htmlContentPart = messageBean.getBodyContent();
			htmlContent = (htmlContentPart != null && htmlContentPart.length > 0)?
					StringUtils.getCRLFEscape(htmlContentPart[0].getText()):"";			
			files = messageBean.getAttachContent();
			vcards = messageBean.getVcardContent();
			imageAttach = messageBean.getImageContent();
			
			request.setAttribute("to", message.getTo());
			request.setAttribute("cc", message.getCc());
			request.setAttribute("bcc", message.getBcc());
			request.setAttribute("messageBean", messageBean);
			request.setAttribute("folder", folder);
			request.setAttribute("uid", uid);
			request.setAttribute("orgPart", part);
			request.setAttribute("nestedPart", (nestedPart != null)?nestedPart:"");			
			request.setAttribute("sharedFlag", sharedFlag);
			request.setAttribute("sharedUserSeq", sharedUserSeq);
			request.setAttribute("sharedFolderName", sharedFolderName);
		} catch (Exception e) {			
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(e);
		} finally{
			try {
				if(ufolder !=null && ufolder.isOpen())
					ufolder.close(true);
				if(store !=null && store.isConnected())
					store.close();	
			} catch (Exception e2) {
				LogManager.writeErr(this, e2.getMessage(), e2);
			}
		}
		
		return "success";
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
	
	private MessageParserInfoBean getMessagePaserInfoBean() {		
		String attachesDir = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
		String hostStr = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort(); 
		MessageParserInfoBean infoBean = new MessageParserInfoBean();
		infoBean.setAttachesDir(attachesDir);
		infoBean.setAttachesUrl(EnvConstants.getAttachSetting("attach.url"));
		infoBean.setDefaultCharset(EnvConstants.getMailSetting("default.charset"));
		infoBean.setDefaultImg("/design/common/images/blank.gif");		
		infoBean.setStrLocalhost(hostStr);
		infoBean.setUserId(user.get(User.MAIL_UID));
		return infoBean;
	}	

}
