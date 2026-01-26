/**
 * DraftMimeMessageBuilder.java 2009. 3. 2.
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

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;

import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.AttachFileMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.AttachForwardMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.AttachSignMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.AttachUploadedImageMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.TextMultipartMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.VcardMsgBodyCreator;

/**
 * <p><strong>DraftMimeMessageBuilder.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class DraftMimeMessageBuilder extends MimeMessageBuilder {


	/**
	 * <p></p>
	 *
	 * @see com.terracetech.tims.webmail.mail.manager.builder.MimeMessageBuilder#setMessageContent(javax.mail.internet.MimeMessage, com.terracetech.tims.webmail.mail.ibean.SenderInfoBean)
	 * @param sendMsg
	 * @param info
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 * @throws IOException 
	 */
	@Override
	public void setMessageContent(MimeMessage sendMsg, SenderInfoBean info)
			throws MessagingException, UnsupportedEncodingException,
			IOException {
		
		AbstractMsgBodyCreator attachCreator = new AttachFileMsgBodyCreator(info);		
		AbstractMsgBodyCreator uploadImgCreator = new AttachUploadedImageMsgBodyCreator(info);
		AbstractMsgBodyCreator contentCreator = new TextMultipartMsgBodyCreator(info);
		
		attachCreator.setNext(attachCreator)						
						.setNext(uploadImgCreator)
						.setNext(contentCreator);		
		
		MimeMultipart mp = new MimeMultipart();		
		attachCreator.generate(mp);
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
		
		if(info.getIasBcc() != null){
			String bccStr = TMailAddress.getString(info.getIasBcc());
			msg.setHeader("X-Bcc", bccStr);
		}
		
		if(info.isReply()){
			msg.setHeader("X-SENDFLAG", "REPLY");
		} else if(info.isForward()){
			msg.setHeader("X-SENDFLAG", "FORWARD");
		} else if(info.isForwardAttached()){
			msg.setHeader("X-SENDFLAG", "FORWARDATTACHED");
		}
		
		if(!info.isReply() &&
				(info.isForward() ||
				info.isForwardAttached()) &&
				info.isSendDraft() && 
				info.getUids() != null){
			msg.setHeader("X-FUID", info.getUids());
			msg.setHeader("X-FFNAME", MimeUtility.encodeText(info.getFolder()));
		}
	}	

}
