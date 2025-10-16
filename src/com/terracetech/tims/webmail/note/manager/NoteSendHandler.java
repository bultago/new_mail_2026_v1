package com.terracetech.tims.webmail.note.manager;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.webmail.mail.ibean.MailSendResultBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.send.SendHandler;

public class NoteSendHandler extends SendHandler {

	public NoteSendHandler(Session session, SenderInfoBean info,
			MailSendResultBean sendResult) {
		super(session, info, sendResult, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public MailSendResultBean sendMailMessage(MimeMessage msg,
			TMailFolder folder) {
		// TODO Auto-generated method stub
		return null;
	}

}
