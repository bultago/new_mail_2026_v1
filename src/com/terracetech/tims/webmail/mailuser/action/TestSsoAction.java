package com.terracetech.tims.webmail.mailuser.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.mailuser.sso.SsoManager;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class TestSsoAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {

	private static final long serialVersionUID = 3524773920732540358L;
	
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private UserAuthManager userAuthManager = null;
	private SsoManager ssoManager = null;
	
	String returnMessage = "success";
	
	public String execute() throws Exception {

		String method = request.getParameter("testSsoMethod");
		String algorithm = request.getParameter("testAlgorithm");
		
		AuthUser auth= ssoManager.test(algorithm, method + "Sso", request);
		
		if (!auth.isAuthSuccess()) {
			String failMsg = userAuthManager.getFailProperty(auth.getAuthResult());
			returnMessage = "fail," + failMsg;
		}
		ResponseUtil.makeAjaxMessage(response, returnMessage);
		return null;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}

	public void setSsoManager(SsoManager ssoManager) {
		this.ssoManager = ssoManager;
	}
}