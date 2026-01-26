package com.terracetech.tims.mobile.common.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;

import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.BrowserUtil;

public class ChangeMailModeAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2268320847645566079L;

	private UserAuthManager userAuthManager = null;
	
	private String mailMode = null;
	
	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}
	
	public ChangeMailModeAction(boolean authcheck) {
		setAuthCheck(authcheck);
	}
	
	public String execute() throws Exception {
		
		String agent = request.getHeader("user-agent");
		boolean isMobile = BrowserUtil.isMoblieBrowser(agent);
		String language = request.getParameter("language") == null ? "ko" : request.getParameter("language");
		HttpSession session = request.getSession();
		if(isMobile){		
			session.setAttribute("mailMode", mailMode);
		} else {
			mailMode = mailMode.replaceAll("\r", "").replaceAll("\n", "");
			Cookie cookie = new Cookie("TSMODE",mailMode);
			cookie.setPath("/");
			cookie.setMaxAge(365 * 24 *  60 * 60);
			response.addCookie(cookie);
		}
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("attachPath", context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir"));
		paramMap.put("attachUrl", EnvConstants.getAttachSetting("attach.url"));
		paramMap.put("localUrl", request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort());
		
		if (!"pc".equalsIgnoreCase(mailMode)) {
			paramMap.put("logoType", "mobile");
		}
		if(user != null){
			userAuthManager.updateUserLogoCookieProcess(paramMap, response, user);
		}
		session.setAttribute("language", language);
		
		return "success";
	}

	public void setMailMode(String mailMode) {
		this.mailMode = mailMode;
	}
	
}
