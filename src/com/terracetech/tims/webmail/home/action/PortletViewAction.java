package com.terracetech.tims.webmail.home.action;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.home.manager.MailHomeManager;
import com.terracetech.tims.webmail.home.vo.MailHomePortletVO;
import com.terracetech.tims.webmail.mailuser.User;

public class PortletViewAction extends BaseAction{

	private static final long serialVersionUID = 20090109L;
	
	private MailHomeManager manager;
	
	private String ps = null;
	
	public void setPs(String ps) {
		this.ps = ps;
	}

	public void setManager(MailHomeManager manager) {
		this.manager = manager;
	}
	
	public String execute() throws Exception{
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		MailHomePortletVO portlet = manager.readLayoutPortlet(domainSeq, ps);
		
		//TODO 파라미터 교체할거 있으면 여기서 해야한다.
		request.setAttribute("url", portlet.getPortletUrl());
		
		return "success";
	}
}
