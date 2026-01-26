package com.terracetech.tims.webmail.mailuser.action;

import java.util.HashSet;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class AutoLogoutAction {
	public Logger log = LoggerFactory.getLogger(this.getClass());

	public String execute(HttpServletRequest request, HttpServletResponse response, String id, String pass, String domain) throws Exception {
		UserAuthManager userAuthManager = (UserAuthManager) ApplicationBeanUtil.getApplicationBean("userAuthManager");
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
		
		request.setAttribute("cookieDomain", EnvConstants.getBasicSetting("cookie.domain"));
		request.setAttribute("cookieNameObj", "{cookies:['"+mainCookie+"','"+logoCookie+"']}");		
		request.setAttribute("timeout", timeout);
		request.setAttribute("stime", stime);
		request.setAttribute("language", language);
		
		return "success";
	}
	
	public String executeByEmpno(HttpServletRequest request, HttpServletResponse response, String empno, String pass, String domain) throws Exception {
		UserAuthManager userAuthManager = (UserAuthManager) ApplicationBeanUtil.getApplicationBean("userAuthManager");
		MailUserManager mailUserManager = (MailUserManager) ApplicationBeanUtil.getApplicationBean("mailUserManager");
		
		String id = mailUserManager.searchUserIdByEmpno(empno, domain);
		if(StringUtils.isEmpty(id))
			return userAuthManager.getFailProperty(UserAuthManager.FAIL);
		
		return execute(request, response, id, pass, domain);
	}
}
