package com.terracetech.tims.webmail.mailuser.action;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;



import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;

public class LogoutAction extends BaseAction {

	private static final long serialVersionUID = 5500255675458918101L;
	
	UserAuthManager userAuthManager = null;
	
	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}
	
	public String execute() throws Exception {
		String language = request.getParameter("language") == null ? "ko" : request.getParameter("language"); 
		String timeout = request.getParameter("timeout");
		String stime = request.getParameter("stime");
		timeout = (timeout != null)?timeout:"off";
		
		Set<String> cookieKeys = new HashSet<String>(3);
		String mainCookie = EnvConstants.getBasicSetting("cookie.name");
		String logoCookie = EnvConstants.getBasicSetting("cookie.name")+"Logo";		
		cookieKeys.add(mainCookie);
		cookieKeys.add(logoCookie);
		
		/*
		HttpSession session = request.getSession();
		if(session != null){
			session.removeAttribute(EnvConstants.getBasicSetting("timeout.session.name"));
		}
		*/
		// TCUSTOM-2063 2016-10-31 - session-timeout : 0 에서 Heap 에서 session clear 가 되지 않아 수정됨
		UserAuthManager.removeSessionTime(request, response);		
		userAuthManager.deleteCookie(cookieKeys, request, response);
		
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		String logoutPath = userAuthManager.afterLogoutPath(mailDomainSeq);
		
		request.setAttribute("cookieDomain", EnvConstants.getBasicSetting("cookie.domain"));
		request.setAttribute("cookieNameObj", "{cookies:['"+mainCookie+"','"+logoCookie+"']}");		
		request.setAttribute("timeout", timeout);
		request.setAttribute("stime", stime);
		request.setAttribute("logoutPath", logoutPath);
		request.setAttribute("language", language);
		
		request.getSession().removeAttribute("dormantUser");
		
		writeMailLog(true,"logout");
		
		return "success";
	}
}
