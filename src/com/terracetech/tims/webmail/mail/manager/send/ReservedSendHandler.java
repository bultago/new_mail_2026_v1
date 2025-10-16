/**
 * ReservedSendHandler.java 2009. 3. 3.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.send;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailSendResultBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.builder.TempMimeMessageBuilder;

/**
 * <p><strong>ReservedSendHandler.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class ReservedSendHandler extends SendHandler {

	/**
	 * <p></p>
	 *
	 * @param session
	 * @param info
	 * @param sendResult
	 */
	public ReservedSendHandler(Session session, SenderInfoBean info,
			MailSendResultBean sendResult,TMailStore store) {
		super(session, info, sendResult, store);	
	}

	/**
	 * <p></p>
	 *
	 * @see com.terracetech.tims.webmail.mail.manager.send.SendHandler#sendMailMessage(javax.mail.internet.MimeMessage, com.terracetech.tims.mail.TMailFolder)
	 * @param msg
	 * @param folder
	 * @return 
	 */
	@Override
	public MailSendResultBean sendMailMessage(MimeMessage msg,
			TMailFolder folder) {
		
		try {
			InternetAddress[] rcptto = info.getIasRcptto();
			
			if(info.isRecvNoti()){		
				info.setMdnStr(getMDNString(rcptto[0], "mail"));
			}
			
			TempMimeMessageBuilder builder = new TempMimeMessageBuilder();
			MimeMessage tmpMime = new MimeMessage(session);
			builder.setFrom(tmpMime, info);
			builder.setRecipient(tmpMime, info);						
			builder.setFlaged(tmpMime, info);					
			builder.setSendDate(tmpMime, info.getSendDate());					
			builder.setMessageHeader(tmpMime, info);
			builder.setSubject(tmpMime, info.getSubject(), info.getCharset());
			builder.setMessageContent(tmpMime, info);
			tmpMime.saveChanges();						
			tmpMime.setHeader("Message-ID", "<"+info.getMessageId()+">");
			sourceMime = tmpMime;
			
			sendResult.setMailSize(getMessageSize(tmpMime));			
			sendResult.setSendResultType("reserved");
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			errorMessage = e.getMessage();
			sendResult.setErrorOccur(true);
		}
		info.setSaveSent(false);
		setSendResult();				
		
		return sendResult;
	}

}
