package com.terracetech.tims.mobile.common.action;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.manager.LogoManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.common.vo.CommonLogoVO;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.BrowserUtil;
import com.terracetech.tims.webmail.util.CookieUtils;

public class WelcomeAction extends BaseAction {
	
	private static final long serialVersionUID = -5162449530740318748L;

	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public Logger log = Logger.getLogger(this.getClass());
	
	private MailUserManager mailUserManager = null;
	private SystemConfigManager systemManager = null;
	private LogoManager logoManager = null;
	
	public WelcomeAction(boolean authcheck) {
		setAuthCheck(authcheck);
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}
	public void setLogoManager(LogoManager logoManager) {
		this.logoManager = logoManager;
	}
	
	public String execute() throws Exception{
		
		String forward = "normalWelcome";			
		HttpSession session = request.getSession();
		
		String installLocale = EnvConstants.getBasicSetting("setup.state");
		String agent = request.getHeader("user-agent");
		String mailMode = null;
		String mobileMailUse = systemManager.getMobileMailConfig();
		boolean isMobileMailUse = "enabled".equals(mobileMailUse);
		boolean isMobile = BrowserUtil.isMoblieBrowser(agent);
		if(isMobile){
			Object object = session.getAttribute("mailMode");
			if (object!= null) {
				mailMode = (String)object;
			}
		} else {
			CookieUtils cookieUtil = new CookieUtils(request);
			mailMode = cookieUtil.getValue("TSMODE");
		}
		
		if (user == null) {
			String defaultDomain = mailUserManager.readDefaultDomain();
			
			if (isMobileMailUse && 
					("mobile".equalsIgnoreCase(mailMode) || 
					(isMobile && !"pc".equalsIgnoreCase(mailMode)))) {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("attachPath", context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir"));
				paramMap.put("attachUrl", EnvConstants.getAttachSetting("attach.url"));
				paramMap.put("localUrl", request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort());
				paramMap.put("logoType", "mobile");
				Map<String, CommonLogoVO> commonLogoList = logoManager.readCommonLogoList(paramMap);

				request.setAttribute("installLocale", installLocale);
				request.setAttribute("commonLogoList", commonLogoList);
				forward = "mobileLogin";
			} 
						
			request.setAttribute("mobileMailUse", isMobileMailUse);
			request.setAttribute("defaultDomain",defaultDomain);
			request.setAttribute("installLocale", installLocale);
			
		} else {
			if (isMobileMailUse && 
					("mobile".equalsIgnoreCase(mailMode) || 
					(isMobile && !"pc".equalsIgnoreCase(mailMode)))) {
				forward = "mobileIntro";
			}
		}		
		return forward;		
	}

}
