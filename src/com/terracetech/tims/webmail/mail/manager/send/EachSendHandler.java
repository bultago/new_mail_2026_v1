/**
 * IndividualHandler.java 2009. 2. 27.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.send;

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
import com.terracetech.tims.webmail.mail.manager.builder.TempMimeMessageBuilder;

/**
 * <p><strong>IndividualHandler.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class EachSendHandler extends SendHandler {

	/**
	 * <p></p>
	 *
	 * @param session
	 * @param info
	 * @param sendResult
	 */
	public EachSendHandler(Session session, SenderInfoBean info,
			MailSendResultBean sendResult,TMailStore store) {
		super(session, info, sendResult, store);
		// TODO Auto-generated constructor stub
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
		SMTPMessage sendMsgs = null;
		try {			
			InternetAddress[] rcptto = info.getIasRcptto();		
			InternetAddress[] iaddrs = new InternetAddress[1];
			
			//sendMsgs = new SMTPMessage[rcptto.length];
			MimeMessage tmpMime = null;
			TempMimeMessageBuilder builder = new TempMimeMessageBuilder();
			int addressCnt = rcptto.length;
			for (int i = 0; i < rcptto.length; i++) {					
				
				if(info.isRecvNoti()){
					info.setMdnStr(getMDNString(rcptto[i], "mail"));
				} 
				
				tmpMime = new MimeMessage(session);					
				builder.setFrom(tmpMime, info);				
				builder.setRecipient(tmpMime,rcptto[i]);				
				builder.setFlaged(tmpMime, info);					
				builder.setSendDate(tmpMime, info.getSendDate());					
				builder.setMessageHeader(tmpMime, info);
				builder.setSubject(tmpMime, info.getSubject(), info.getCharset());
				builder.setMessageContent(tmpMime, info);
				tmpMime.saveChanges();
				tmpMime.setHeader("Message-ID", "<"+info.getMessageId()+">");
				
				iaddrs[0] = rcptto[i];
				
				sendMsgs = new SMTPMessage(tmpMime);
				sendOne(sendMsgs,iaddrs,addressCnt);
				sendMsgs = null;
			}
			
			//send(sendMsgs,rcptto);
			sendResult.setMailSize(getMessageSize(msg));	
			sendResult.setSendResultType("normal");
			setSendResult();
			if(!sendResult.isErrorOccur() && info.isRecvNoti()){
				info.setSaveSent(true);
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
		
		setSendResult();
		sourceMime = msg;
		return sendResult;
	}

}
