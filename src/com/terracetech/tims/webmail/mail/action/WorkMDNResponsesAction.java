/**
 * ViewMDNResponsesAction.java 2008. 12. 8.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MDNResponsesBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mail.manager.send.SendHandler;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>ViewMDNResponsesAction.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author jpjung
 * @since Tims7
 * @version 7.0 
 */
public class WorkMDNResponsesAction extends BaseAction {

	private static final long serialVersionUID = 20081209L;
	
	private MailManager mailManager = null;
	private MailUserManager mailUserManager = null;
	
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}

	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	

	public String viewMDNResponses() throws Exception {
		boolean isError = false;
		String uid = request.getParameter("uid");
		String page = request.getParameter("page");
		String pattern = request.getParameter("pattern");
		String mdnListPage = request.getParameter("mdnlistpage");
		String mdnListPattern = request.getParameter("mdnlistpattern");
		
		/*if (request.getMethod().equalsIgnoreCase("get")) {
			pattern = StringUtils.getDecodingUTF(pattern);
			mdnListPattern = StringUtils.getDecodingUTF(mdnListPattern);
		}*/		
		
		
		String folderName = FolderHandler.SENT;
			
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		try {
			if(uid == null)throw new Exception("viewMDNResponses UID is null!");			
			store = factory.connect(request.getRemoteAddr(), user);	
			mailManager.setProcessResource(store, getMessageResource());
			Map<String,Long> localDomainMap = mailUserManager.getLocalDomainMap();
			
			String pageBase = user.get(User.PAGE_LINE_CNT);
			page = (page != null)?page:"1";
			pattern = (pattern != null)?pattern:"";
						
			
			MDNResponsesBean mdnBean = 
				mailManager.getMDNResponsesContent(folderName, uid, 
						localDomainMap, page, pageBase, pattern);
			
			request.setAttribute("mdnBean", mdnBean);
			request.setAttribute("pageBase", pageBase);
			request.setAttribute("uid", uid);
			request.setAttribute("mdnListPage", mdnListPage);
			request.setAttribute("mdnListPattern", mdnListPattern);
			request.setAttribute("keyWord", pattern);
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isError = true;
		} finally { 
			if(store !=null && store.isConnected())
				store.close();
		}
		
		return (isError)?"subError":"success";
	}
	
	public String recallMDNResponses() throws Exception {
		String uid = request.getParameter("uid");
		String mid = request.getParameter("mid");
		String recallEmailStr = request.getParameter("rmailStr");		
		String[] recallEmails = recallEmailStr.split("\\|");
		String remoteAddr = request.getRemoteAddr();
		
		if(mid.charAt(0) == '<' && mid.charAt(mid.length()-1) == '>') {
			mid = mid.substring(1, mid.length()-1);
		}		
			
		TMailStore store = null;
		TMailStore rstore = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		try {
			
			if(uid == null)throw new Exception("recallMDNResponses UID is null!");
			if(mid == null)throw new Exception("recallMDNResponses Message ID is null!");
			
			Map<String,Long> localDomainMap = mailUserManager.getLocalDomainMap();
			String emailVals[] = null;
			User recallUser = null;
			TMailFolder folder = null;
			List<String> failList = new ArrayList<String>();			
			List<String> successList = new ArrayList<String>();
			String domainName = null;
			String userId = null;
			long tmpSeq = 0;			
			for (String rcptEmail : recallEmails) {
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
			
			store = factory.connect(request.getRemoteAddr(), user);
			folder = store.getDefaultFolder();
			for (String rcptEmail : successList) {				
				long time = System.currentTimeMillis() / 1000;					
				try {						
					folder.xsetMDN(mid, rcptEmail, "", time, "1");
				} catch(MessagingException e) {
					LogManager.writeErr(this, e.getMessage(), e);
				}				
			}
			
						
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		} finally { 
			if(store !=null && store.isConnected())
				store.close();
		}
		
		request.setAttribute("uid", uid);
		
		return "success";
	}
	
	
	public String sendMDNResponses() throws Exception {
		String userId = user.get(User.MAIL_UID);
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		String sharedFlag = request.getParameter("sharedFlag");
		boolean isShared = (sharedFlag != null && sharedFlag.equals("shared"))?true:false;
		String sharedUserSeq = request.getParameter("sharedUserSeq");
		String sharedFolderName = request.getParameter("sharedFolderName");	
		
		String email = null;
		String uid = request.getParameter("uid");
		String folderName = request.getParameter("folder");		
		
		
		User connectionUser = null;
		if(isShared){
			connectionUser = mailUserManager.readUserMailConnectionInfo(Integer.parseInt(sharedUserSeq), domainSeq);		
			email = connectionUser.get(User.EMAIL);
			folderName = sharedFolderName;
		} else {
			connectionUser = user;
			email = user.get(User.EMAIL);			 
		}
		
		I18nResources msgResource = getMessageResource();
			
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder folder = null;
		try {
			if(uid == null)throw new Exception("USER["+userId+"] sendMDNResponses UID is null!");
			if(folderName == null)throw new Exception("USER["+userId+"] sendMDNResponses FolderName is null!");
			if(connectionUser == null)throw new Exception("USER["+userId+"] connectionUser is null!");
			
			store = factory.connect(remoteIp, connectionUser);
			folder = store.getFolder(folderName);
			folder.open(true);
			TMailMessage message = folder.getMessageByUID(Long.parseLong(uid), true);
			if(message == null)throw new Exception("["+userId+"]MDN Send Reply Message is null!");
			message.setMDNAddresses(message.getHeader("Disposition-Notification-To"));
			
			MimeMessage mdnmsg = new MimeMessage(factory.getSession());
	        String subject = message.getSubject();

	        mdnmsg.setFrom(new InternetAddress(email));
	        mdnmsg.setRecipients(Message.RecipientType.TO,
	            message.getMDNAddresses());
	        mdnmsg.setSentDate(new Date());
	        mdnmsg.setHeader("References", message.getMessageID());
	        mdnmsg.setSubject("[" + msgResource.getMessage("mail.mdn.read") +
	            "] " + ((subject != null)? subject :
	            	msgResource.getMessage("header.nosubject")));

	        MimeMultipart mp = new MimeMultipart();
	        MimeBodyPart part1 = new MimeBodyPart();
	        MimeBodyPart part2 = new MimeBodyPart();

	        mp.setSubType("report; report-type=disposition-notification");

	        String s1 = msgResource.getMessage("mail.mdn.response.001")+ "<" + email + ">";
	        String s2 = msgResource.getMessage("mail.mdn.response.002")+": " + message.getSentDate();
	        String s3 = msgResource.getMessage("mail.mdn.response.003")+ message.getReceivedDate();

	        part1.setContent(s1 + "\n" + s2 + "\n" + s3, "text/plain; charset=euc-kr");
	        
	        String fr = "Final-Recipient: rfc822;" + email;
	        String omid = "Original-Message-ID: " + message.getMessageID();; 
	        String dpt = "Disposition: manual-action/MDN-sent-manually; displayed";

	        part2.setContent(fr + "\n" + omid + "\n" + dpt, "message/disposition-notification");
	        mp.addBodyPart(part1);
	        mp.addBodyPart(part2);
	        mdnmsg.setContent(mp);
	        
	        SendHandler.simpleSendMessage(factory.getSession(), 
	        		new MimeMessage[]{mdnmsg}, 
	        		new InternetAddress[][]{message.getMDNAddresses()});
	        
	        folder.close(true);
										
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		} finally {
			if(folder !=null && folder.isOpen())
				folder.close(false);			
			if(store !=null && store.isConnected())
				store.close();
		}
		
		return null;
	}
	
	


	
}
