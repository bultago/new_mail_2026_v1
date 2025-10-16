package com.terracetech.tims.jmobile.common.action;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.jmobile.common.manager.UserAuthManager;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;

public class LoginAction extends BaseAction {

	private UserAuthManager userAuthManager = null;
	private SystemConfigManager systemManager = null;
	private MailUserManager mailUserManager = null;

	public LoginAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}

	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}

	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public String execute() throws Exception {
		
		I18nResources resource = getMessageResource("common");
		I18nResources jresource = getMessageResource("jmail");
		String foward = "fail";
		String failMessage = "";
		
		String email = request.getParameter("email");
		String pass = request.getParameter("pass");	
				
		log.debug("email : " + ((email != null)?email:""));
		log.debug("pass : " + ((pass != null)?pass:""));		
		String alertTitle = "";
		boolean isError = false;
		try {
			String id,domain;
			String values[] = null;
			if(email.indexOf("@") > -1){
				values = email.split("@");
				id = values[0];
				domain = values[1];
			} else {				 
				id = email;
				domain = mailUserManager.readDefaultDomain();
			}
			AuthUser auth = userAuthManager.validateUser(id, pass, domain);
			if (auth.isAuthSuccess()) {
				User allowUser = auth.getUser();
				userAuthManager.doLoginProcess(request, allowUser, systemManager.getCryptMethod());
				foward = "success";
				LogManager.writeMailLogMsg(true,log, allowUser.get(User.EMAIL), 
						request.getRemoteAddr(), "login");
			} else {
				String failMsg = userAuthManager.getFailProperty(auth.getAuthResult());
				failMessage = resource.getMessage(failMsg);
				alertTitle = jresource.getMessage("error.login");
				isError = true;
			}
		} catch (Exception e) {
			failMessage = resource.getMessage("error.msg.001");
    		log.error(e.getMessage(), e);
    		alertTitle = jresource.getMessage("error.login");
    		isError = true;
		}
		
		if(isError){
			request.setAttribute("alertmsg", failMessage);
			request.setAttribute("alerttitle", alertTitle);			
		}
		
		return foward;
	}
}
