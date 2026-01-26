package com.terracetech.tims.webmail.common.action;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;

public class ChangeUserAccountAction extends BaseAction {

	private static final long serialVersionUID = 3968279723841360484L;
	private MailUserManager mailUserManager = null;
	private UserAuthManager userAuthManager = null;
	private SystemConfigManager systemManager = null;

	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}
	
	public ChangeUserAccountAction() {
		setAuthCheck(false);
	}
	
	public String execute() throws Exception {	
		AuthUser dormantUser = (AuthUser)request.getSession().getAttribute("dormantUser");
		String cookieAlgorithm = (String)request.getSession().getAttribute(EnvConstants.COOKIE_ALGORITHM_METHOD);
		
		String forward = "success";
		try{
			int userSeq = Integer.parseInt(dormantUser.get(User.MAIL_USER_SEQ));		
			mailUserManager.changeUserAccount(userSeq,"enabled");
			
			String attachPath = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
			String strLocalhost = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
			
			Map<String, String> authParamMap = new HashMap<String, String>();
			authParamMap.put("attachPath", attachPath);
			authParamMap.put("attachUrl", EnvConstants.getAttachSetting("attach.url"));
			authParamMap.put("localUrl", strLocalhost);
			authParamMap.put("cookieAlgorithm", cookieAlgorithm);
			
			String sessionTime = systemManager.getSessionTimeConfig();
			dormantUser.getUser().put(User.SESSION_CHECK_TIME, sessionTime);
			userAuthManager.doLoginProcess(request, response, dormantUser.getUser(),authParamMap);
			
			request.setAttribute("user", dormantUser.getUser());			
		} catch (Exception e) {
			I18nResources msgResource = new I18nResources(new Locale(dormantUser.getUser().get(User.USER_LOCALE)), "common");
			forward = "error";			
			request.setAttribute("msg", msgResource.getMessage("dormant.account.error"));
			e.printStackTrace();
			LogManager.writeErr(this, e.getMessage(), e);
		}
		
				
		return forward;
	}
}
