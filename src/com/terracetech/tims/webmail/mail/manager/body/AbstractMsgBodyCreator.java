/**
 * Test.java 2008. 12. 1.
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

import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;

/**
 * 
 * <p>
 * <strong>AbstractMsgBodyCreator.java</strong> Class Description
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
public abstract class AbstractMsgBodyCreator {

	private AbstractMsgBodyCreator next;
	
	protected SenderInfoBean info;

	public AbstractMsgBodyCreator(SenderInfoBean info) {
		this.info = info;
	}

	public AbstractMsgBodyCreator setNext(AbstractMsgBodyCreator next) {
		this.next = next;
		return next;
	}

	public void generate(Multipart mp) throws NoSuchProviderException,
			MessagingException, UnsupportedEncodingException, IOException {
		if (isAcceptable()) {
			execute(mp);
		}

		if (next != null) {
			next.generate(mp);
		} else {
			fail();
		}

	}

	public abstract boolean isAcceptable();

	public abstract void execute( Multipart mp)
			throws NoSuchProviderException, MessagingException,
			UnsupportedEncodingException, IOException;

	protected void fail() {
	}

}
