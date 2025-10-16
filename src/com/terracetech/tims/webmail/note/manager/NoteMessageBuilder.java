package com.terracetech.tims.webmail.note.manager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.TextMultipartMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.builder.MimeMessageBuilder;

public class NoteMessageBuilder extends MimeMessageBuilder {

	@Override
	public void setMessageContent(MimeMessage sendMsg, SenderInfoBean info)
			throws MessagingException, UnsupportedEncodingException,
			IOException {
		AbstractMsgBodyCreator contentCreator = new TextMultipartMsgBodyCreator(info);
		
		MimeMultipart mp = new MimeMultipart();
		contentCreator.generate(mp);
		sendMsg.setContent(mp);
	}

	@Override
	public void setMessageHeader(MimeMessage msg, SenderInfoBean info)
			throws MessagingException, UnsupportedEncodingException {
		msg.setHeader("Reply-To", TMailAddress.getEncodeAddressString(info.getSenderEmail(),
				info.getSenderName(), info.getCharset()));
		msg.setHeader("X-Priority", "3");		
	}
}
