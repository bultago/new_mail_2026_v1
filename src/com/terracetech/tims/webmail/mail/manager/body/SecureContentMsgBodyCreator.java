package com.terracetech.tims.webmail.mail.manager.body;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;

import com.terracetech.tims.webmail.mail.ibean.MailSecureInfoBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.StringReplaceUtil;

public class SecureContentMsgBodyCreator extends AbstractMsgBodyCreator {
	
	public SecureContentMsgBodyCreator(SenderInfoBean info) {
		super(info);
	}
	
	@Override
	public void execute(Multipart mp) throws NoSuchProviderException,
			MessagingException, UnsupportedEncodingException, IOException {
		
		if(info.isEtcSecureMail()){
			info.setBodyContent(info.getContent());
		}		
		MailSecureInfoBean secureInfo = info.getSecureInfo();
		String formFile = secureInfo.getContentFile();
		MimeBodyPart mbp = new MimeBodyPart();
		String formContents = new String(FileUtil.readFile(new File(formFile)),"UTF-8");		
		formContents = formContents.replaceAll("\\{LOCAL_HOST\\}",info.getLocalUrl());
		formContents = formContents.replaceAll("\\{SECURE_HINT\\}", 
				secureInfo.getSecureMailHint());		
		formContents = secureInfo.getSecureMailMsg(formContents);	
		if(info.isHtmlMode()){					
			String textContents = getContentTEXTForm();			
			MimeMultipart content_mp = new MimeMultipart("alternative");
			MimeBodyPart text = new MimeBodyPart();
			MimeBodyPart html = new MimeBodyPart();
			StringReplaceUtil replacer = new StringReplaceUtil();
			
			//TODO SECURE TEXT MSG
			
			String text_content = replacer.getTextToHTML(textContents);		
			String html_content = formContents.trim();		
			
			text.setContent(text_content, "text/plain; charset="
					+ info.getCharset());
			html
					.setContent(html_content, "text/html; charset="
							+ info.getCharset());
	
			text.addHeader("Content-Transfer-Encoding", "8bit");
			html.addHeader("Content-Transfer-Encoding", "8bit");
	
			content_mp.addBodyPart(text);
			content_mp.addBodyPart(html);
			mbp.setContent(content_mp);
		} else {
			mbp.setContent(formContents, "text/plain; charset="+info.getCharset());
			mbp.addHeader("Content-Transfer-Encoding","8bit");
		}
		
		
		mp.addBodyPart(mbp);		
		info.setContent(formContents);

	}

	@Override
	public boolean isAcceptable() {		
		return info.isSecureMail();
	}
	
	public String getContentTEXTForm() {
		String contentForm = "this message is securemail.\n"
			+ "please open attachement html file";

		return contentForm;
	}

}
