package com.terracetech.tims.webmail.plugin.securemail.initech;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.AttachSignMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.LetterPaperMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.SecureContentMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.TextMultipartMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.builder.MimeMessageBuilder;

public class InitechSecureMimeMessageBuilder extends MimeMessageBuilder {	
	
	@Override
	public void setMessageContent(MimeMessage sendMsg, SenderInfoBean info)
			throws MessagingException, UnsupportedEncodingException,
			IOException {
		
		info.clearStack();		
		AbstractMsgBodyCreator signCreator = new AttachSignMsgBodyCreator(info);		
		AbstractMsgBodyCreator contentCreator = new TextMultipartMsgBodyCreator(info);
		AbstractMsgBodyCreator letterPaperCreator = new LetterPaperMsgBodyCreator(info);		
		AbstractMsgBodyCreator secureContentsCreator = new SecureContentMsgBodyCreator(info);		
		AbstractMsgBodyCreator initechContentCreator = new InitechSecureMimeMsgBodyCreator(info);
		
		signCreator.setNext(contentCreator)
					.setNext(letterPaperCreator)					
					.setNext(secureContentsCreator)
					.setNext(initechContentCreator);
				
		MimeMultipart mp = new MimeMultipart();
		signCreator.generate(mp);
		sendMsg.setContent(mp);
		
		sendMsg.saveChanges();
		if(info.isRecvNoti()){
			String mid = sendMsg.getMessageID();
			if(mid.charAt(0) == '<' && mid.charAt(mid.length()-1) == '>') {
				mid = mid.substring(1, mid.length()-1);
			}
			info.setMessageId(mid);
		}
	}

	@Override
	public void setMessageHeader(MimeMessage msg, SenderInfoBean info)
			throws MessagingException, UnsupportedEncodingException {
		
		msg.setHeader("Reply-To", TMailAddress.getEncodeAddressString(info.getSenderEmail(),
				info.getSenderName(), info.getCharset()));
		msg.setHeader("X-Priority", (info.getPriority() != null) ? info
				.getPriority() : "3");		
				
	}

}
