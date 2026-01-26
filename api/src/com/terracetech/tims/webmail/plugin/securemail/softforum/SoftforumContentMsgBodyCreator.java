package com.terracetech.tims.webmail.plugin.securemail.softforum;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;

import com.terracetech.tims.webmail.common.ExtPartConstants;
import com.terracetech.tims.webmail.mail.ibean.MailSecureInfoBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.StringReplaceUtil;

public class SoftforumContentMsgBodyCreator extends AbstractMsgBodyCreator {
	public SoftforumContentMsgBodyCreator(SenderInfoBean info) {
		super(info);
	}
	@Override
	public void execute(Multipart mp) throws NoSuchProviderException,
			MessagingException, UnsupportedEncodingException, IOException {
		
		String makeMode = ExtPartConstants.getExtPartConfig("softforum.express.type");
		
		if(info.isEtcSecureMail()){
			info.setBodyContent(info.getContent());
		}		
		MailSecureInfoBean secureInfo = info.getSecureInfo();
		MimeMultipart content_mp = new MimeMultipart("alternative");
		MimeBodyPart text = new MimeBodyPart();
		MimeBodyPart html = new MimeBodyPart();
		String text_content = null;
		String html_content = null;
		String textContents = null;
		String htmlContents = null;
		
		String formHtmlFile = null;
		
		StringReplaceUtil replacer = new StringReplaceUtil();
		
		if("PKI".equalsIgnoreCase(makeMode)){			
			formHtmlFile = secureInfo.getContentPKIFile();			
			htmlContents = new String(FileUtil.readFile(new File(formHtmlFile)),"UTF-8");
			htmlContents = secureInfo.getSecureMailMsg(htmlContents);
			htmlContents = htmlContents.replaceAll("\\{LOCAL_HOST\\}",info.getLocalUrl());
		} else if("PASSWD".equalsIgnoreCase(makeMode)){
			formHtmlFile = secureInfo.getContentFile();			
			htmlContents = new String(FileUtil.readFile(new File(formHtmlFile)),"UTF-8");
			htmlContents = htmlContents.replaceAll("\\{LOCAL_HOST\\}",info.getLocalUrl());
			htmlContents = htmlContents.replaceAll("\\{SECURE_HINT\\}", 
					secureInfo.getSecureMailHint());
			htmlContents = secureInfo.getSecureMailMsg(htmlContents);
		}
		textContents = getContentTEXTForm();	
		MimeBodyPart mbp = new MimeBodyPart();						
		//TODO SECURE TEXT MSG
		text_content = replacer.getTextToHTML(textContents);
		
		if(info.isAttachSign()){
			htmlContents = htmlContents + "<br>{tims_sign_pos}<br>";
			textContents = textContents + "\n{tims_sign_pos}\n\n";
		}
		html_content = htmlContents.trim();	
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
		
		mp.addBodyPart(mbp);		
		info.setContent(htmlContents);

	}

	@Override
	public boolean isAcceptable() {
		return true;
	}
	
	public String getContentTEXTForm() {
		String contentForm = "this message is securemail.\n"
			+ "please open attachement html file";

		return contentForm;
	}

}
