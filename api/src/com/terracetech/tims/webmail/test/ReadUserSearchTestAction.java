package com.terracetech.tims.webmail.test;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.mail.manager.MailManager;

public class ReadUserSearchTestAction extends BaseAction {

	private static final long serialVersionUID = -8132656159600091214L;
	
	private int userSeq;
	
	private int domainSeq;
	
	private int maxResult;
	
	private String keyWord;
	
	private MailManager manager;
	
	private MailAddressBean[] emails = null;
	
	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	public ReadUserSearchTestAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	
	public void setDomainSeq(int domainSeq) {
		this.domainSeq = domainSeq;
	}
	
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public MailAddressBean[] getEmails() {
		return emails;
	}

	public void setManager(MailManager manager) {
		this.manager = manager;
	}

	public String execute() throws Exception{
	
		emails = manager.getUserMailAddressList(userSeq, domainSeq, "ko",  15, keyWord, true, false);
		
		return "success";
	}
}
