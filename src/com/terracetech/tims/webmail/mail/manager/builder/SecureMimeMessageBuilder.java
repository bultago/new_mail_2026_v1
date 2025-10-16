package com.terracetech.tims.webmail.mail.manager.builder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mail.ibean.MailSecureInfoBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.SecureContentMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.SecureFormMsgBodyCreator;
import com.terracetech.tims.webmail.mail.manager.body.SecureMimeMsgBodyCreator;

public class SecureMimeMessageBuilder extends MimeMessageBuilder {
	
	private MimeMessage contentMimeMessage = null;
	
	public SecureMimeMessageBuilder(MimeMessage contentMimeMessage) {
		this.contentMimeMessage = contentMimeMessage;
	}
	@Override
	public void setMessageContent(MimeMessage sendMsg, SenderInfoBean info)
			throws MessagingException, UnsupportedEncodingException,
			IOException {
		
		info.clearStack();		
		
		AbstractMsgBodyCreator secureFormCreator = new SecureFormMsgBodyCreator(info);
		AbstractMsgBodyCreator secureContentsCreator = new SecureContentMsgBodyCreator(info);
		AbstractMsgBodyCreator secureMimeCreator = new SecureMimeMsgBodyCreator(info,contentMimeMessage);
		secureFormCreator.setNext(secureMimeCreator)
						.setNext(secureContentsCreator);
				
		MimeMultipart mp = new MimeMultipart();
		secureFormCreator.generate(mp);
		sendMsg.setContent(mp);
	}

	@Override
	public void setMessageHeader(MimeMessage msg, SenderInfoBean info)
			throws MessagingException, UnsupportedEncodingException {
		
		MailSecureInfoBean secureInfo = info.getSecureInfo();
		String password = secureInfo.getSecureMailPassword();
		String cryptMethod = secureInfo.getCryptMethod(); 
		
		msg.setHeader("Reply-To", TMailAddress.getEncodeAddressString(info.getSenderEmail(),
				info.getSenderName(), info.getCharset()));
		msg.setHeader("X-Priority", (info.getPriority() != null) ? info
				.getPriority() : "3");
		
		try {
			cryptMethod = SecureUtil.encrypt(SymmetricCrypt.AES, 
					EnvConstants.ENCRYPT_KEY,
					cryptMethod);
			password = SecureUtil.encrypt(SymmetricCrypt.AES, 
					EnvConstants.ENCRYPT_KEY,
					password);
		} catch (Exception e) {
			throw new MessagingException("HEADER ENCRYPT ERROR");			
		}
		
		msg.setHeader("X-MIME-ENCODE", TMailUtility.IMAPFolderEncode(cryptMethod));
		msg.addHeader("X-SECURE-INFO", TMailUtility.IMAPFolderEncode(password));		
	}

}
