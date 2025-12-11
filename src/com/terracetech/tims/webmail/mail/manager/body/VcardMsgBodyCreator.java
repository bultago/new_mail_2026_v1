/**
 * VcardMsgBodyCreator.java 2008. 12. 4.
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

import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.internet.MimeBodyPart;

import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;

/**
 * <p>
 * <strong>VcardMsgBodyCreator.java</strong> Class Description
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
public class VcardMsgBodyCreator extends AbstractMsgBodyCreator {

	public VcardMsgBodyCreator(SenderInfoBean info) {
		super(info);
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @see com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator#execute(java.lang.String,
	 *      javax.mail.Multipart)
	 * @param content
	 * @param mp
	 * @throws NoSuchProviderException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@Override
	public void execute(Multipart mp)
			throws NoSuchProviderException, MessagingException,
			UnsupportedEncodingException, IOException {

		MimeBodyPart mbp = new MimeBodyPart();
		
		if(info.getVcard() != null){
			mbp.setContent(info.getVcard(), "text/x-vcard; name=vcard.vcf; charset="
							+ info.getCharset());			
			mbp.setFileName("vcard.vcf");
			mp.addBodyPart(mbp);
			
			info.pushStack(mbp);
		}
		
	}

	/**
	 * <p>
	 * </p>
	 *�ӽú������϶��� VCard�� Set�ϸ� �ȵȴ�. ��߼۽� VCard�� 2���� �ɼ� �ִ�.
	 * 
	 * @see com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator#isAcceptable()
	 * @return
	 */
	@Override
	public boolean isAcceptable() {
		return info.isAttachVcard();
	}
	
}
