package com.terracetech.tims.webmail.bbs.manager;

import java.util.Arrays;
import java.util.Vector;

import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.mail.ibean.MailFolderBean;

public class BbsHandler {
	
	private TMailStore store = null;
	
	public BbsHandler(TMailStore store) {
		this.store = store;
	}
	
	public TMailFolder getBbsImapFolder(int bbsId) throws MessagingException {
		
		TMailFolder folder = store.getFolder(Integer.toString(bbsId));
		
		return folder;
	}

}
