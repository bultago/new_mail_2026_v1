/**
 * TextMultipartMsgBodyCreator.java 2008. 12. 1.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.body;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.util.StringReplaceUtil;

/**
 * <p>
 * <strong>TextMultipartMsgBodyCreator.java</strong> Class Description
 * </p>
 * <p>
 * �ֿ伳��
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
public class TextMultipartMsgBodyCreator extends AbstractMsgBodyCreator {

	public TextMultipartMsgBodyCreator(SenderInfoBean info) {
		super(info);
	}

	public void execute(Multipart mp) throws NoSuchProviderException,
			MessagingException, UnsupportedEncodingException, IOException {
		
		MimeBodyPart comment = new MimeBodyPart();
		String txt = info.getContent();
		if(info.isSendDraft()){
			txt = txt.replaceAll("\\{tims_sign_pos\\}","");
		}
		if(info.isHtmlMode()){		
			MimeMultipart content_mp = new MimeMultipart("alternative");
			MimeBodyPart text = new MimeBodyPart();
			MimeBodyPart html = new MimeBodyPart();
			StringReplaceUtil replacer = new StringReplaceUtil();			
			
			String text_content = txt;
			String html_content = txt;	
			
			text_content = replacer.getHtmlToText(txt);
			html_content = txt;	
			
			html_content += info.getMdnStr();
			
			if(info.isBigAttach()){
				html_content += info.getBigAttachMailContents();
			}
	
			text.setContent(text_content, "text/plain; charset="
					+ info.getCharset());
			
			if (info.isRecvNoti()) {
            	html.setDescription("batchMime");
            }
			
			html
					.setContent(html_content, "text/html; charset="
							+ info.getCharset());
	
			//text.addHeader("Content-Transfer-Encoding", ("tms".equals(info.getSecureMailModule()) && info.isSecureMail())?"base64":"8bit");
			//html.addHeader("Content-Transfer-Encoding", ("tms".equals(info.getSecureMailModule()) && info.isSecureMail())?"base64":"8bit");
			 
			text.addHeader("Content-Transfer-Encoding", "base64");
			html.addHeader("Content-Transfer-Encoding", "base64");
	
			content_mp.addBodyPart(text);
			content_mp.addBodyPart(html);			
			comment.setContent(content_mp);			
	        
		} else {			
			comment.setContent(txt, "text/plain; charset="+info.getCharset());
	    	comment.addHeader("Content-Transfer-Encoding","8bit");
		}
		
		
		
    	mp.addBodyPart(comment);
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @see com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator#isAcceptable()
	 * @return
	 */
	public boolean isAcceptable() {
		return true;
	}

}
