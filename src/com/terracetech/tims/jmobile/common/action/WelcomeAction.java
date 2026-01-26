package com.terracetech.tims.jmobile.common.action;

import java.util.Stack;

import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.util.StringUtils;

@SuppressWarnings("all")
public class WelcomeAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1621990142706754240L;
	private SystemConfigManager systemManager = null;
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager  = systemManager;
	}

	public WelcomeAction(boolean authcheck) {
		setAuthCheck(authcheck);
	}
	
	public String execute() throws Exception {		
		
		String forward = "login";
		String loginLocale = request.getParameter("loginLocale");
		loginLocale = StringUtils.isEmpty(loginLocale) ? "" : loginLocale;
		
		String mobileMailUse = systemManager.getMobileMailConfig();
		boolean isMobileMailUse = "enabled".equals(mobileMailUse);		
		
		if(isMobileMailUse){
			if (user == null) {
				request.setAttribute("loginLocale", loginLocale);
			} else {
				try{
					Object o = request.getSession().getAttribute("jobStack");
					Stack jobStack = null;
					if(o != null){
						jobStack = (Stack)o;
						jobStack.clear();
					} else {
						jobStack = new Stack();
					}
					request.getSession().setAttribute("jobStack", jobStack);
					savePreAction("menu", null);
					forward = "main";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			forward = "notservice";
		}
		return forward;
	}
}
