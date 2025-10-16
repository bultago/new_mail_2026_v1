package com.terracetech.tims.webmail.mail.action;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import com.sun.mail.imap.IMAPBodyPart;
import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.MessageParser;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.MessageUtil;

public class ReadSecureMessageAction extends BaseAction {
	
	private static final long serialVersionUID = 6180881495218484571L;
	
	private MailUserManager userManager = null;
	private MailManager mailManager = null;
	
	private TMailPart[] files;
	private TMailPart[] vcards;
	private String[] imageAttach;	
	private String htmlContent;	
	private TMailMessage message;
	
	public ReadSecureMessageAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	/**
	 * @param userManager 파라미터를 userManager값에 설정
	 */
	public void setUserManager(MailUserManager userManager) {
		this.userManager = userManager;
	}
	
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
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
		String forward = "success";
		Locale locale = I18nConstants.getUserLocale(request);
		
		String param = request.getParameter("param");
		String password = request.getParameter("password");
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources msgResource = getMessageResource();
		String resultMessage = null;
		boolean isError = false;
		String subject = null;
		try{
			param = SecureUtil.decrypt(SymmetricCrypt.AES,EnvConstants.ENCRYPT_KEY,param);
			Map<String, String> paramMap = getParamMap(param);
			int domainSeq = Integer.parseInt(paramMap.get("domainseq"));
			int userSeq = Integer.parseInt(paramMap.get("userseq"));
			
			
			User readUser = userManager.readUserMailConnectionInfo(userSeq, domainSeq);		
			readUser.put(User.IMAP_LOGIN_ARGS, paramMap.get("arg"));
			this.user = readUser;
			store = factory.connect(request.getRemoteAddr(), readUser);
			
			mailManager.setProcessResource(store, msgResource);
			
			MessageParserInfoBean parserInfo = getMessagePaserInfoBean();	
			parserInfo.setLocale(locale);
			String folder = paramMap.get("folder");
			String msgId = paramMap.get("msgid");
			
			MailMessageBean messageBean = null;
			TMailFolder readFolder = null;
			TMailMessage message = null;
			int uid = 0;
			try {
				readFolder = store.getFolder(folder);
				readFolder.open(true);
				uid = readFolder.xsearchMID(msgId);
				
				
				message = readFolder.getMessageByUID(uid, true);
				message.setDirectRead(true);
	
				MessageParser mParser = new MessageParser();			
			
				messageBean = mParser.parseMessage(message, parserInfo);
			} catch (Exception e) {
				resultMessage = msgResource.getMessage("mail.secure.msg.002");
				throw new Exception("MAIL NOT EXIST");		
			}			
			
			subject = messageBean.getMessage().getSubject();
			TMailPart[] attachPart = messageBean.getAttachContent();			
			message = messageBean.getMessage();			
			
			String encPassword = message.getHeaderString("X-SECURE-INFO");
			String cryptMethod = message.getHeaderString("X-MIME-ENCODE");
			TMailPart mailPart = null;
			boolean checkMsg = false;		
			String val = null;
			for (int i = 0; i < attachPart.length; i++) {
				val = attachPart[i].getFileName();
				if(val.equals("TSM_SECURE_MIME.TSM")){										
					checkMsg = true;
					mailPart = attachPart[i]; 
					break;
				}
			}
			
			if(!checkMsg){
				resultMessage = msgResource.getMessage("mail.secure.msg.002");
				throw new Exception("MAIL NOT EXIST");		
			}
			
			if(encPassword == null){
				resultMessage = msgResource.getMessage("mail.secure.msg.002");
				throw new Exception("PASSWORD NOT EXIST");				
			}			
			encPassword = SecureUtil.decrypt(SymmetricCrypt.AES,EnvConstants.ENCRYPT_KEY,
					TMailUtility.IMAPFolderDecode(encPassword));			
			
			if(!password.equals(encPassword)){
				resultMessage = msgResource.getMessage("mail.secure.msg.001");
				throw new Exception("PASSWORD NOT MATCH");
			}
			
			if(cryptMethod == null){
				resultMessage = msgResource.getMessage("mail.secure.msg.004");
				throw new Exception("CRYPT METHOD NOT EXIST");				
			}
			String decodeCryptMethod = SecureUtil.decrypt(SymmetricCrypt.AES,EnvConstants.ENCRYPT_KEY,
					TMailUtility.IMAPFolderDecode(cryptMethod));
						
			
			byte[] mimeByte = null;		
			try {
				mimeByte = MessageUtil.getMimeByte(((IMAPBodyPart)mailPart.getPart()).getInputStream());
			} catch (Exception e1) {
				LogManager.writeErr(this, e1.getMessage(), e1);				
				resultMessage = msgResource.getMessage("mail.secure.msg.004");
				throw new Exception("MIME GET BYTE ERROR");
			} 
			
			byte[] decryptContents = null;
	        try {
	        	decryptContents = SecureUtil.decryptToByte(decodeCryptMethod, EnvConstants.ENCRYPT_KEY, new String(mimeByte));
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);				
				resultMessage = msgResource.getMessage("mail.secure.msg.004");
				throw new Exception("MIME ENCRYPT ERROR");
			}
			
			MimeMessage parseMessage = 
				new MimeMessage(factory.getSession(),new ByteArrayInputStream(decryptContents));
			
			parseMessage.saveChanges();
			
			messageBean = mailManager.parseMessage(new TMailMessage(parseMessage), parserInfo);
			
			message = messageBean.getMessage();
			htmlContent = messageBean.getBodyContent()[0].getText();
			files = messageBean.getAttachContent();
			vcards = messageBean.getVcardContent();
			imageAttach = messageBean.getImageContent();
								
			request.setAttribute("folderName", readFolder.getEncName());
			request.setAttribute("uid", uid);
			request.setAttribute("cryptMethod",cryptMethod);
			request.setAttribute("domainParam",domainSeq);
			request.setAttribute("userParam",userSeq);
			request.setAttribute("subject",subject);			
			
			readFolder.close(true);
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isError = true;
			if(resultMessage == null){
				resultMessage = msgResource.getMessage("mail.secure.msg.004");
			}
		}finally{
			if(store !=null && store.isConnected())
				store.close();
		}
		
		if(isError){
			forward = "alertback";
			request.setAttribute("errMsg", resultMessage);
		}		
		
		return forward;
	}
	
	private Map<String, String> getParamMap(String paramStr){
		Map<String, String> map = null;
		
		if(paramStr != null){
			map = new HashMap<String, String>();
			String[] paramValues = paramStr.split(";");
			String[] values = null;
			for (String value : paramValues) {
				values = value.split(":");
				map.put(values[0], values[1]);
			}			
		}		
		
		return map;
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
