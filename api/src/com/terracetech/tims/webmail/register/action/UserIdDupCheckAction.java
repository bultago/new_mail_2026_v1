package com.terracetech.tims.webmail.register.action;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class UserIdDupCheckAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;

	private MailUserManager mailUserManager = null;
	
	private int mailDomainSeq = 0;
	private String userId = null;

	public UserIdDupCheckAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public String execute() throws Exception {
		String paid = request.getParameter("paid");
		if (!paid.equals(request.getSession().getId())) {
            return "paidError";
        }
		JSONObject jsonObj = new JSONObject();
		boolean isExist = false;
		
		userId = userId.toLowerCase();
		
		isExist = mailUserManager.readUserIdDupCheck(mailDomainSeq, userId);
		
		jsonObj.put("isExist", isExist);
		
		ResponseUtil.processResponse(response, jsonObj);

		return null;
	}

	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public void setUserId(String userId) {
		this.userId = userId.toLowerCase();
	}
	
}
