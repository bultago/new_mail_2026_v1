package com.terracetech.tims.jmobile.common.action;

import com.terracetech.tims.jmobile.common.manager.UserAuthManager;

public class LogoutAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2945683261756160372L;
	private UserAuthManager userAuthManager = null;
	
	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}

	public String execute(){
		userAuthManager.doLogoutProcess(request);
		request.getSession().removeAttribute("jobStack");
		return "success";
	}
}
