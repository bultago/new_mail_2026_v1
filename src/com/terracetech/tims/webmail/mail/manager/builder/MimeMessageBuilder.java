/**
 * MimeMessageBuilder.java 2008. 12. 1.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.builder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;

/**
 * <p>
 * <strong>MimeMessageBuilder.java</strong> Class Description
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
public abstract class MimeMessageBuilder {

	public void setRecipient(MimeMessage msg, SenderInfoBean info)
	throws MessagingException {
		
		info.setIasTo(TMailAddress.getParseEmailAddress(info.getTo(), info.getCharset()));
		info.setIasCc(TMailAddress.getParseEmailAddress(info.getCc(), info.getCharset()));
		info.setIasBcc(TMailAddress.getParseEmailAddress(info.getBcc(), info.getCharset()));
		info.setIasRcptto(TMailAddress.getParseEmailAddress(info.getRcptto(), info.getCharset()));		
		
		if(info.getIasRcptto() != null && info.getMaxRcpt() < info.getIasRcptto().length && !info.isAllowMaxRcpts()){
			throw new MessagingException("maxrcptOver");
		}		
		
		if(info.getTo() != null){
			msg.setRecipients(Message.RecipientType.TO, info.getIasTo());
		}
		if(info.getCc() != null){
			msg.setRecipients(Message.RecipientType.CC, info.getIasCc());
		}		
	}

	public void setFrom(MimeMessage msg, SenderInfoBean info)
			throws UnsupportedEncodingException, MessagingException {
				
		msg.setFrom(new InternetAddress(info.getSenderEmail(), info
					.getSenderName(), info.getCharset()));
	}

	public void setSubject(MimeMessage msg, String subject, String charset)
			throws MessagingException {
		msg.setSubject(subject, charset);
	}
	
	public void  setFlaged(MimeMessage msg, SenderInfoBean info) 
	throws MessagingException{
		
		if (!info.isSendDraft() && info.isReply()) {			
			TMailMessage tempMsg = info.getFlagMessages()[0];
			if(tempMsg != null){
				try {
					tempMsg.setFlag(javax.mail.Flags.Flag.ANSWERED, true);
					String reply_mid = tempMsg.getMessageID();
					info.setReplyMid(reply_mid);
					msg.setHeader("In-Reply-To", (reply_mid != null) ? reply_mid : "");
				} catch (Exception e) {
					LogManager.writeErr(this, e.getMessage(), e);
				}			
			}
		}
		
		if (!info.isSendDraft() && (info.isForward() || info.isForwardAttached())) {
			TMailMessage[] tempMsgs = info.getFlagMessages();
			if(tempMsgs != null){
				for (int i = 0; i < tempMsgs.length; i++) {
					try {
						tempMsgs[i].setFlags(new Flags("$CUSTOM"), true);
					} catch (Exception e) {
						LogManager.writeErr(this, e.getMessage(), e);
					}
				}
			}
		}
	}

	
	public abstract void setMessageHeader(MimeMessage msg, SenderInfoBean info)
			throws MessagingException, UnsupportedEncodingException;

	/**
	 * <p>
	 * </p>
	 * 
	 * @param sendMsg
	 * @param info
	 * @param content
	 * @return void
	 * @throws MessagingException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public abstract void setMessageContent(MimeMessage sendMsg, 
			SenderInfoBean info) throws MessagingException,
			UnsupportedEncodingException, IOException;
	/**
	 * <p>
	 * </p>
	 * 
	 * @param sendMsg
	 * @param date
	 * @return void
	 * @throws MessagingException
	 */
	public void setSendDate(MimeMessage sendMsg, Date date)
			throws MessagingException {
		sendMsg.setSentDate(date);
	}
	
	
	
}
