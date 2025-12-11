package com.terracetech.tims.webmail.plugin.securemail.softforum;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeUtility;

import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.ByteArrayDataSource;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator;

public class SoftforumSecureMimeMsgBodyCreator extends AbstractMsgBodyCreator {
	public SoftforumSecureMimeMsgBodyCreator(SenderInfoBean info) {
		super(info);
	}
	@Override
	public void execute(Multipart mp) throws NoSuchProviderException,
			MessagingException, UnsupportedEncodingException, IOException {
		
		MimeBodyPart mbp = new MimeBodyPart();
		
		mbp.setDataHandler(new DataHandler(new ByteArrayDataSource(info.getBodyContent().getBytes())));

		String fileName = "SECURE_MAIL.html";
		String esc_name = MimeUtility.encodeText(fileName,
				info.getCharset(), "B");

		mbp.setHeader("Content-Type", TMailUtility
				.getMIMEContentType(fileName));
		mbp.setFileName(esc_name);
		mbp.addHeader("Content-Transfer-Encoding", "base64");
		mp.addBodyPart(mbp);
		
		info.pushStack(mbp);
	}

	@Override
	public boolean isAcceptable() {
		return true;
	}

}
