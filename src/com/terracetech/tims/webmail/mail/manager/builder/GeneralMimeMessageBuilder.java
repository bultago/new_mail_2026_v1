/**
 * GeneralMimeMessageBuilder.java 2008. 12. 1.
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

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.AttachBannerMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.AttachFileMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.AttachForwardMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.AttachSignMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.AttachUploadedImageMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.BigAttachFileMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.LetterPaperMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.TextMultipartMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.VcardMsgBodyCreator;

/**
 * <p>
 * <strong>GeneralMimeMessageBuilder.java</strong> Class Description
 * </p>
 * <p>
 * 주요설명
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
public class GeneralMimeMessageBuilder extends MimeMessageBuilder {	

	public void setMessageHeader(MimeMessage msg, SenderInfoBean info)
			throws MessagingException, UnsupportedEncodingException {		
		
		msg.setHeader("Reply-To", TMailAddress.getEncodeAddressString(info.getSenderEmail(),
				info.getSenderName(), info.getCharset()));
		msg.setHeader("X-Priority", (info.getPriority() != null) ? info
				.getPriority() : "3");		
	}
	
	public void setMessageContent(
			MimeMessage sendMsg,
			SenderInfoBean info) throws MessagingException,
			UnsupportedEncodingException, IOException {
		
		
		AbstractMsgBodyCreator signCreator = new AttachSignMsgBodyCreator(info);		
		AbstractMsgBodyCreator attachCreator = new AttachFileMsgBodyCreator(info);
		AbstractMsgBodyCreator bigAttachCreator = new BigAttachFileMsgBodyCreator(info);
		AbstractMsgBodyCreator forwardCreator = new AttachForwardMsgBodyCreator(info);
		AbstractMsgBodyCreator uploadImgCreator = new AttachUploadedImageMsgBodyCreator(info);
		AbstractMsgBodyCreator vcardCreator = new VcardMsgBodyCreator(info);
		AbstractMsgBodyCreator letterPaperCreator = new LetterPaperMsgBodyCreator(info);
		AbstractMsgBodyCreator contentCreator = new TextMultipartMsgBodyCreator(info);
		AbstractMsgBodyCreator bannerCreator = new AttachBannerMsgBodyCreator(info);
		
		signCreator.setNext(attachCreator)
						.setNext(bigAttachCreator)
						.setNext(forwardCreator)
						.setNext(uploadImgCreator)
						.setNext(vcardCreator)
						.setNext(letterPaperCreator)
						.setNext(bannerCreator)
						.setNext(contentCreator);
		
		
		MimeMultipart mp = new MimeMultipart();		
		signCreator.generate(mp);
		sendMsg.setContent(mp);		
		
	}
}
