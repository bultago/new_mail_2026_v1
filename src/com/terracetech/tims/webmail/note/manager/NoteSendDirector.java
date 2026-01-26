package com.terracetech.tims.webmail.note.manager;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.builder.MimeMessageBuilder;
import com.terracetech.tims.webmail.mail.manager.builder.TempMimeMessageBuilder;
import com.terracetech.tims.webmail.mail.manager.send.SendHandler;

public class NoteSendDirector {

	private MimeMessageBuilder builder;
	private SenderInfoBean info;
	private MimeMessage sourceMessage;
	
	public NoteSendDirector(MimeMessageBuilder builder, SenderInfoBean info) {		
		this.builder = builder;
		this.info = info;
	}
	
	

	public MimeMessage getSourceMessage() {
		return sourceMessage;
	}
}
