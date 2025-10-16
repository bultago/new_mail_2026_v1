package com.terracetech.tims.webmail.mailuser.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.CheckUserExistManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class AutoLoginAction {
	public Logger log = Logger.getLogger(this.getClass());

	public String execute(HttpServletRequest request, HttpServletResponse response, String id, String pass, String domain) throws Exception {
		
		UserAuthManager userAuthManager = (UserAuthManager) ApplicationBeanUtil.getApplicationBean("userAuthManager");
		SystemConfigManager systemManager = (SystemConfigManager) ApplicationBeanUtil.getApplicationBean("systemConfigManager");
		CheckUserExistManager checkUserExistManager = (CheckUserExistManager) ApplicationBeanUtil.getApplicationBean("checkUserExistManager");
		ServletContext context = request.getSession().getServletContext();
		
		String attachPath = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
		String strLocalhost = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("attachPath", attachPath);
		paramMap.put("attachUrl", EnvConstants.getAttachSetting("attach.url"));
		paramMap.put("localUrl", strLocalhost);
		
		log.debug("AutoLoginAction.attachPath : "+ attachPath);
		log.debug("AutoLoginAction.attachUrl : "+ EnvConstants.getAttachSetting("attach.url"));
		log.debug("AutoLoginAction.localUrl : "+ strLocalhost);
		
		String sessionTime = systemManager.getSessionTimeConfig();
		request.getSession().setAttribute(EnvConstants.COOKIE_ALGORITHM_METHOD, systemManager.getCryptMethod());
    	paramMap.put("cookieAlgorithm", systemManager.getCryptMethod());
    	log.debug("AutoLoginAction.cookieAlgorithm : "+ paramMap.get("cookieAlgorithm"));
    	
		AuthUser auth= userAuthManager.validateUser(id, pass, domain);
		log.debug("AutoLoginAction.auth.isAuthSuccess() : "+ auth.isAuthSuccess());
		if (auth.isAuthSuccess()) {
			User allowUser = auth.getUser();
			allowUser.put(User.SESSION_CHECK_TIME, sessionTime);
			int mailDomainSeq = Integer.parseInt(allowUser.get(User.MAIL_DOMAIN_SEQ));
			String timestamp = checkUserExistManager.dupCheckProcess(allowUser.get(User.EMAIL),mailDomainSeq);
			allowUser.put(User.LOGIN_TIMESTAMP, timestamp);
			userAuthManager.doLoginProcess(request, response, allowUser,paramMap);
			request.setAttribute("user", allowUser);//변수 상수화
			log.debug("AutoLoginAction.allowUser : "+ allowUser);
			
			LogManager.writeMailLogMsg(true,log, allowUser.get(User.EMAIL), 
					request.getRemoteAddr(), "login");
			return "success";
		} else {
			
			String failMsg = userAuthManager.getFailProperty(auth.getAuthResult());
		    
		    return failMsg;
		}
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
