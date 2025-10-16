/**
 * NormalSendHandler.java 2009. 2. 27.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.send;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.StoreClosedException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPMessage;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailSendResultBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mail.manager.builder.MimeMessageBuilder;
import com.terracetech.tims.webmail.mail.manager.builder.TempMimeMessageBuilder;

/**
 * <p><strong>NormalSendHandler.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class NormalSendHandler extends SendHandler {

	/**
	 * <p></p>
	 *
	 * @param session
	 * @param info
	 * @param sendResult
	 */
	public NormalSendHandler(Session session, SenderInfoBean info,
			MailSendResultBean sendResult, TMailStore store) {
		super(session, info, sendResult, store);
	}

	/**
	 * <p></p>
	 *
	 * @see com.terracetech.tims.webmail.mail.manager.send.SendHandler#sendMailMessage(javax.mail.internet.MimeMessage, java.lang.String)
	 * @param msg
	 * @param content
	 * @return 
	 */
	@Override
	public MailSendResultBean sendMailMessage(MimeMessage msg, TMailFolder folder) {		
		SMTPMessage[] sendMsgs = null;
		try {
			MimeMessage tmpMime = null;
			MimeMessageBuilder builder = null;
			if(info.isRecvNoti()){
				
				SMTPMessage sendMsg = null;
				InternetAddress[] iaddrs = new InternetAddress[1];
				
				info.setSaveSent(true);				
				InternetAddress[] rcptto = info.getIasRcptto();			
				
				sendMsgs = new SMTPMessage[rcptto.length];
				tmpMime = null;
				builder = new TempMimeMessageBuilder();
				int addressCnt = rcptto.length;	
				for (int i = 0; i < rcptto.length; i++) {					
					
					info.setMdnStr(getMDNString(rcptto[i], "mail"));
					
					tmpMime = new MimeMessage(session);					
					builder.setFrom(tmpMime, info);
					builder.setRecipient(tmpMime, info);
					builder.setFlaged(tmpMime, info);					
					builder.setSendDate(tmpMime, info.getSendDate());					
					builder.setMessageHeader(tmpMime, info);
					builder.setSubject(tmpMime, info.getSubject(), info.getCharset());
					builder.setMessageContent(tmpMime, info);						
					tmpMime.saveChanges();
					tmpMime.setHeader("Message-ID", "<"+info.getMessageId()+">");
					
					iaddrs[0] = rcptto[i];
					sendMsg = new SMTPMessage(tmpMime);
					sendMsg.setEnvelopeFrom(info.getUserEmail());
					sendOne(sendMsg,iaddrs,addressCnt);
					sendMsg = null;
					
					
					//sendMsgs[i] = new SMTPMessage(tmpMime);
					//sendMsgs[i].setEnvelopeFrom(info.getUserEmail());
				}
				//System.out.println("====================[("+info.getUserEmail()+")SEND MTA START:"+new Date()+"]=========================");
				//send(sendMsgs,rcptto);
				//System.out.println("====================[("+info.getUserEmail()+")SEND MTA END:"+new Date()+"]=========================");
				
			} else {
				
				if(info.isSecureMail() && "tms".equals(info.getSecureMailModule())){
					builder = new TempMimeMessageBuilder();
					tmpMime = new MimeMessage(session);					
					builder.setFrom(tmpMime, info);
					builder.setRecipient(tmpMime, info);
					builder.setFlaged(tmpMime, info);					
					builder.setSendDate(tmpMime, info.getSendDate());					
					builder.setMessageHeader(tmpMime, info);
					builder.setSubject(tmpMime, info.getSubject(), info.getCharset());
					builder.setMessageContent(tmpMime, info);						
					tmpMime.saveChanges();
					tmpMime.setHeader("Message-ID", "<"+info.getMessageId()+">");
				} else {
					tmpMime = msg;
				}
				
				sendMsgs = new SMTPMessage[1];
				sendMsgs[0] = new SMTPMessage(tmpMime);
				sendMsgs[0].setEnvelopeFrom(info.getUserEmail());
				
				send(sendMsgs[0],info.getIasRcptto());							
			}
			
			sendResult.setMailSize(getMessageSize(msg));			
			sendResult.setSendResultType("normal");
			setSendResult();
			if(!sendResult.isErrorOccur() && info.isRecvNoti()){
				try {
					folder.xcommand("NOOP", null);
				} catch (StoreClosedException e) {
					store = (new TMailStoreFactory()).connect(info.getRemoteIp(), info.getUser());
					folder = store.getFolder(FolderHandler.SENT);
				}
				setMDNFlag(validAddress, folder);
			}
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			errorMessage = e.getMessage();
			sendResult.setErrorOccur(true);
		} finally {
			sendMsgs = null;
		}
		
		if(sendResult.isErrorOccur() && info.isRecvNoti()){
			try {
				info.setSaveSent(false);
				delMDNFlag(folder);
			} catch (MessagingException e) {
				LogManager.writeErr(this, e.getMessage(), e);
			}
		}		
		
		sourceMime = msg;
		return sendResult;
	}

}
