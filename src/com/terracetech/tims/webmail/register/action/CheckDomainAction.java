package com.terracetech.tims.webmail.register.action;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class CheckDomainAction extends BaseAction {

	private MailUserManager mailUserManager = null;
	private String domain = null;
	
	public CheckDomainAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		boolean isSuccess = true;
		String msg = "";
		
		try {	
			if(StringUtils.isNotEmpty(domain)){
				int domainSeq = mailUserManager.searchMailDomainSeq(domain);
				if (domainSeq <= 0) {
					isSuccess = false;
					msg = "empty";
				}
			} else {
				isSuccess = false;
				msg = "empty";
			}
		}catch (Exception e) {
			isSuccess = false;
			msg = "error";
		}
		
		jsonObj.put("isSuccess", isSuccess);
		jsonObj.put("msg", msg);
		ResponseUtil.processResponse(response, jsonObj);
		
		return null;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
}
