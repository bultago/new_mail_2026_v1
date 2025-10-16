package com.terracetech.tims.webmail.plugin.securemail.softforum;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.AttachSignMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.VcardMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.builder.MimeMessageBuilder;

public class SoftforumSecureMimeMessageBuilder extends MimeMessageBuilder {

	@Override
	public void setMessageContent(MimeMessage sendMsg, SenderInfoBean info)
			throws MessagingException, UnsupportedEncodingException,
			IOException {
		// TODO Auto-generated method stub
		AbstractMsgBodyCreator softforumContentCreator = new SoftforumContentMsgBodyCreator(info);
		AbstractMsgBodyCreator softforumMimeCreator = new SoftforumSecureMimeMsgBodyCreator(info);
		AbstractMsgBodyCreator signCreator = new AttachSignMsgBodyCreator(info);		
		AbstractMsgBodyCreator vcardCreator = new VcardMsgBodyCreator(info);
		
		softforumContentCreator.setNext(softforumMimeCreator)
								.setNext(signCreator)
								.setNext(vcardCreator);
		
		MimeMultipart mp = new MimeMultipart();		
		softforumContentCreator.generate(mp);
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
