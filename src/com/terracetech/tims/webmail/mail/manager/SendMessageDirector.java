/**
 * MessageDirector.java 2008. 12. 1.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailSendResultBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.builder.MimeMessageBuilder;
import com.terracetech.tims.webmail.mail.manager.builder.SecureMimeMessageBuilder;
import com.terracetech.tims.webmail.mail.manager.send.BatchSendHandler;
import com.terracetech.tims.webmail.mail.manager.send.EachSendHandler;
import com.terracetech.tims.webmail.mail.manager.send.NormalSendHandler;
import com.terracetech.tims.webmail.mail.manager.send.ReservedSendHandler;
import com.terracetech.tims.webmail.mail.manager.send.SendHandler;
import com.terracetech.tims.webmail.plugin.securemail.initech.InitechSecureMimeMessageBuilder;
import com.terracetech.tims.webmail.plugin.securemail.softforum.SoftforumSecureMimeMessageBuilder;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p>
 * <strong>MessageDirector.java</strong> Class Description
 * </p>
 * <p>
 * �ֿ伳��
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
public class SendMessageDirector {

	private MimeMessageBuilder builder;
	private SenderInfoBean info;	
	private MailSendResultBean sendResult;

	public SendMessageDirector(MimeMessageBuilder builder, SenderInfoBean info) {		
		this.builder = builder;
		this.info = info;
	}
	
	public MimeMessage mimeBuilderProcess(Session session) throws Exception {
		MimeMessage sendMsg = new MimeMessage(session);
				
		try {
			builder.setFrom(sendMsg, info);
			builder.setFlaged(sendMsg, info);
			builder.setSendDate(sendMsg, info.getSendDate());
			builder.setSubject(sendMsg, info.getSubject(), info.getCharset());
			builder.setMessageContent(sendMsg, info);
			builder.setRecipient(sendMsg, info);
			builder.setMessageHeader(sendMsg, info);
			
			sendMsg.saveChanges();			
			String mid = sendMsg.getMessageID();
			if(mid.charAt(0) == '<' && mid.charAt(mid.length()-1) == '>') {
				mid = mid.substring(1, mid.length()-1);
			}
			info.setMessageId(mid);
			
		} catch (MessagingException e) {
			LogManager.writeErr(this, e.getMessage(), e);
			sendResult.setErrorOccur(true);
			if(e instanceof AddressException){
				sendResult.setInvalidAddress(((AddressException)e).getRef());				
			}
			sendResult.setErrorMessage(e.getMessage());
		}		

		return sendMsg;
	}
	
	private MimeMessageBuilder getInstanceSecureBuilder(MimeMessage sourceMessage){		
		MimeMessageBuilder sbuilder = null;
		if(EnvConstants.S_MAIL_DEFAULT.equals(info.getSecureMailModule())){
			info.setSaveSent(true);
			sbuilder = new SecureMimeMessageBuilder(sourceMessage);			
		} else if(EnvConstants.S_MAIL_INITECH.equals(info.getSecureMailModule())){
			sbuilder = new InitechSecureMimeMessageBuilder();			
		} else if(EnvConstants.S_MAIL_SOFTFORUM.equals(info.getSecureMailModule())){
			sbuilder = new SoftforumSecureMimeMessageBuilder();			
		}
		return sbuilder;
	}
	
	public MimeMessage secureMailBuilderProcess(Session session, MimeMessage sourceMessage) throws Exception {
		MimeMessage secureMsg = new MimeMessage(session);		
		MimeMessageBuilder sbuilder = getInstanceSecureBuilder(sourceMessage);
				
		try {						
			sbuilder.setFrom(secureMsg, info);
			sbuilder.setFlaged(secureMsg, info);
			sbuilder.setSendDate(secureMsg, info.getSendDate());
			sbuilder.setSubject(secureMsg, info.getSubject(), info.getCharset());
			sbuilder.setMessageContent(secureMsg, info);
			sbuilder.setRecipient(secureMsg, info);
			sbuilder.setMessageHeader(secureMsg, info);			
			secureMsg.saveChanges();
			secureMsg.setHeader("Message-ID", "<"+info.getMessageId()+">");
			
		} catch (MessagingException e) {
			LogManager.writeErr(this, e.getMessage(), e);
			sendResult.setErrorOccur(true);
			if(e instanceof AddressException){
				sendResult.setInvalidAddress(((AddressException)e).getRef());				
			}
			sendResult.setErrorMessage(e.getMessage());
		}		

		return secureMsg;
	}
	
	public MailSendResultBean sendProcess(TMailStore mstore, TMailStoreFactory factory) throws Exception {		
		
		Session session = factory.getSession();
		sendResult = new MailSendResultBean();
		
		sendResult.setTo(info.getTo());
		sendResult.setCc(info.getCc());
		sendResult.setBcc(info.getBcc());
		
		
		TMailFolder pFolder = mstore.getFolder(FolderHandler.SENT);
		TMailFolder folder = null;
		
		if(!info.isSendDraft() && 
				(info.isReply() || 
				info.isForward() || 
				info.isForwardAttached())){
			String folderName = info.getFolder();		
			String uids = info.getUids();
			folder = (folderName != null && folderName.length() > 0)?mstore.getFolder(folderName):null;
			if(folder != null){
				folder.open(true);			
				long[] fuids = StringUtils.getStringsToLongs(uids.split(","));			
				info.setFlagMessages(folder.getMessagesByUID(fuids));
			}
		}
		
		MimeMessage sourceMessage = null;	
		try {
			if(info.isSecureMail()){
				if("tms".equals(info.getSecureMailModule())){
					sourceMessage = mimeBuilderProcess(session);
				}
				sourceMessage = secureMailBuilderProcess(session, sourceMessage);
			} else {
				sourceMessage = mimeBuilderProcess(session);
			}
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			sendResult.setErrorOccur(true);
			sendResult.setErrorMessage(e.getMessage());
		}				
		
		try{
			SendHandler sender = null;
			if(!info.isSendDraft() && !sendResult.isErrorOccur()){				
				sender = getSenderHandlerInstance(session,mstore);			
				sendResult = sender.sendMailMessage(sourceMessage, pFolder);
				sourceMessage = sender.getSourceMimeMessage();
			}
			
			if(!mstore.isConnected()){
				mstore = factory.connect(info.getRemoteIp(),info.getUser());
			}
			
			if(info.getBcc() != null && !info.isReserved()){
				sourceMessage.setRecipients(Message.RecipientType.BCC, info.getIasBcc());
			}
			
			sendedProcess(pFolder, mstore, sourceMessage);		
			sendResult.setSaveMid(sourceMessage.getMessageID());
			
			if(folder != null && folder.isOpen()){
				folder.close(true);
			}
			if(pFolder != null && pFolder.isOpen()){
				pFolder.close(true);
			}
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw e;
		}
		return sendResult;
	}
	
	private SendHandler getSenderHandlerInstance(Session session,TMailStore mstore){		
		if(info.isReserved()){
			return new ReservedSendHandler(session, info, sendResult,mstore);
		}else if(info.isBatchSend()){				
			return new BatchSendHandler(session, info, sendResult, mstore);
		} else if(info.isOneSend()){
			return new EachSendHandler(session, info, sendResult, mstore);
		} else {
			return new NormalSendHandler(session, info, sendResult, mstore);	
		}		
	}
	
	private void sendedProcess(TMailFolder pFolder, TMailStore mstore, MimeMessage sourceMessage) 
	throws Exception{
		if(!sendResult.isErrorOccur()){
			if(info.isSendDraft()){				
				pFolder = mstore.getFolder(FolderHandler.DRAFTS);				
				pFolder.appendMessages(new TMailMessage[]{new TMailMessage(sourceMessage)});
				sendResult.setSendType("draft");
				sendResult.setSendResultType("draft");
				sendResult.setMailSize(sourceMessage.getSize());
			} else if(info.isReserved()){
				pFolder = mstore.getFolder(FolderHandler.RESERVED);				
				pFolder.xreserve(info.getSendDate(), new TMailMessage(sourceMessage));
				sendResult.	setSendType("reserved");				
			} else if(info.isSaveSent()){				
				pFolder = mstore.getFolder(FolderHandler.SENT);				
				pFolder.appendMessages(new TMailMessage[]{new TMailMessage(sourceMessage)});				
				pFolder.close(true);
				/*
				pFolder = mstore.getFolder(FolderHandler.SENT);		
				pFolder.open(true);
				int auid = pFolder.xsearchMID(sourceMessage.getMessageID());
				MessageHandler messageHandler = 
					(MessageHandler)ApplicationBeanUtil.getApplicationBean("messageHandler");
				messageHandler.setFolder(pFolder);
				messageHandler.switchFlags(new long[]{auid}, MessageHandler.SEEN_FLAG, true);
				*/
				sendResult.	setSendType("savesent");
			} 
		} else {
			if(sourceMessage != null){
				pFolder = mstore.getFolder(FolderHandler.DRAFTS);
				pFolder.appendMessages(new TMailMessage[]{new TMailMessage(sourceMessage)});
			}
		}	
	}
}
