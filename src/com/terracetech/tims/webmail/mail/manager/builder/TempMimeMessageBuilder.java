/**
 * TempMimeMessageBuilder.java 2009. 2. 27.
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
import java.util.Stack;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.TextMultipartMsgBodyCreator;

/**
 * <p><strong>TempMimeMessageBuilder.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class TempMimeMessageBuilder extends MimeMessageBuilder {

	/**
	 * <p></p>
	 *
	 * @see com.terracetech.tims.webmail.mail.manager.builder.MimeMessageBuilder#setFlaged(com.terracetech.tims.webmail.mail.ibean.SenderInfoBean, javax.mail.internet.MimeMessage)
	 * @param info
	 * @param sendMsg
	 * @throws MessagingException 
	 */
	@Override
	public void setFlaged(MimeMessage sendMsg, SenderInfoBean info)
			throws MessagingException {
		if (info.isReply()) {
			sendMsg.setHeader("In-Reply-To", info.getReplyMid());			
		}
	}

	/**
	 * <p></p>
	 *
	 * @see com.terracetech.tims.webmail.mail.manager.builder.MimeMessageBuilder#setMessageContent(javax.mail.internet.MimeMessage, com.terracetech.tims.webmail.mail.ibean.SenderInfoBean, java.lang.StringBuffer)
	 * @param sendMsg
	 * @param info
	 * @param content
	 * @return
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 * @throws IOException 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setMessageContent(MimeMessage sendMsg, SenderInfoBean info) throws MessagingException,
			UnsupportedEncodingException, IOException {
		
		MimeMultipart mp = new MimeMultipart();
		Stack stack = info.getStack();
		Stack cloneStack = (Stack)stack.clone();
		boolean isSecureMode = info.isSecureMail();
		MimeBodyPart mbp = null;
		String[] header = null;
		
		AbstractMsgBodyCreator contentCreator = new TextMultipartMsgBodyCreator(info);
		contentCreator.execute(mp);
		
		while(!cloneStack.isEmpty()){
			mbp = (MimeBodyPart)cloneStack.pop();
			if(isSecureMode && "tms".equals(info.getSecureMailModule())){
				header = mbp.getHeader("X-SECURE-MIMEPART");
				if(header == null){
					mp.addBodyPart(mbp);
				}				
			} else {
				mp.addBodyPart(mbp);
			}		
		}
		mbp = null;			
		
		sendMsg.setContent(mp);		
	}

	/**
	 * <p></p>
	 *
	 * @see com.terracetech.tims.webmail.mail.manager.builder.MimeMessageBuilder#setMessageHeader(javax.mail.internet.MimeMessage, com.terracetech.tims.webmail.mail.ibean.SenderInfoBean)
	 * @param msg
	 * @param info
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException 
	 */
	@Override
	public void setMessageHeader(MimeMessage msg, SenderInfoBean info)
			throws MessagingException, UnsupportedEncodingException {
		
		if(info.isReserved()){
			msg.setHeader("X-TERRACE-TO", TMailAddress.getReservedRcptToAddress(info.getRcptto(), info.getCharset()));			
			msg.setHeader("X-TERRACE-MDN", (info.isRecvNoti())?info.getMessageId():"off");
			msg.setHeader("X-TERRACE-INDIVIDUAL", (info.isOneSend())?"on":"off");
			msg.setHeader("X-TERRACE-SAVESENT", (info.isSaveSent())?"on":"off");
			msg.setHeader("X-TERRACE-RESERVED", info.getReservDateStr());
		}
		
		msg.setHeader("Reply-To", TMailAddress.getEncodeAddressString(info.getSenderEmail(),
				info.getSenderName(), info.getCharset()));
		msg.setHeader("X-Priority", (info.getPriority() != null) ? info
				.getPriority() : "3");
	}

	/**
	 * <p></p>
	 *
	 * @see com.terracetech.tims.webmail.mail.manager.builder.MimeMessageBuilder#setRecipient(javax.mail.internet.MimeMessage, com.terracetech.tims.webmail.mail.ibean.SenderInfoBean)
	 * @param msg
	 * @param info
	 * @throws MessagingException 
	 */
	@Override
	public void setRecipient(MimeMessage msg, SenderInfoBean info)
			throws MessagingException {	
		
		msg.setRecipients(Message.RecipientType.TO, info.getIasTo());
		if(info.getCc() != null){
			msg.setRecipients(Message.RecipientType.CC, info.getIasCc());
		}
	}
	
	public void setRecipient(MimeMessage msg, InternetAddress to)
	throws MessagingException {
		msg.setRecipients(Message.RecipientType.TO, new InternetAddress[]{to});		
	}

}
