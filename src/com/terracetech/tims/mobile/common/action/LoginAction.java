package com.terracetech.tims.mobile.common.action;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.terracetech.secure.Base64;
import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.CheckUserExistManager;
import com.terracetech.tims.webmail.common.manager.LogoManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.common.vo.CommonLogoVO;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.StringUtils;

public class LoginAction extends BaseAction {

	private static final long serialVersionUID = 201002181450L;

	public Logger log = Logger.getLogger(this.getClass());
	
	private UserAuthManager userAuthManager = null;
	private SystemConfigManager systemManager = null;
	private String defaultDomain = null;
	private LogoManager logoManager = null;
	private CheckUserExistManager checkUserExistManager = null;
	public LoginAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}

	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}
	
	public void setLogoManager(LogoManager logoManager) {
		this.logoManager = logoManager;
	}
	public void setCheckUserExistManager(
			CheckUserExistManager checkUserExistManager) {
		this.checkUserExistManager = checkUserExistManager;
	}
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		
		String cryptMethod = systemManager.getCryptMethod();
		request.getSession().setAttribute(EnvConstants.COOKIE_ALGORITHM_METHOD, cryptMethod);
		I18nResources resource = getMessageResource("common");
		Locale locale = request.getLocale();
		if (locale != null) {
			String language = locale.getLanguage();
			if(language.indexOf("en") > -1){
				locale = new Locale("");
			}
			else if(language.indexOf("ja") > -1){
				locale = new Locale("jp");
			}
			
			resource.setChangeLocale(locale);
		}
		
		String foward = "fail";
		String failMessage = "";
		String email = request.getParameter("email");
		String pass = request.getParameter("pass");
		String encPass = request.getParameter("encPass");
		String savePasswd = request.getParameter("savePassCheck");
		String language = request.getParameter("language");
		
		if("savedpasstms".equalsIgnoreCase(pass) && encPass != null){			
			encPass = encPass.replaceAll(",","+");
			encPass = encPass.replaceAll("-","/");
			encPass = encPass.replaceAll("@","=");			
			pass = SecureUtil.decrypt(cryptMethod, "terrace-12345678", encPass);
		} else {
			encPass = "";
		}
		
		String attachPath = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
		String strLocalhost = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
		
		log.debug("email :" + ((email != null)?email:""));
		log.debug("pass :" + ((pass != null)?pass:""));
		log.debug("attachPath :" + attachPath);
		log.debug("strLocalhost :" + strLocalhost);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("attachPath", attachPath);
		paramMap.put("attachUrl", EnvConstants.getAttachSetting("attach.url"));
		paramMap.put("localUrl", strLocalhost);
		paramMap.put("logoType", "mobile");

    	try {
    		String id = null;
    		String domain = null;
    		if (StringUtils.isNotEmpty(email) && email.indexOf("@") > -1) {
    			
    			id = email.substring(0, email.lastIndexOf("@"));
    			domain = email.substring(email.lastIndexOf("@")+1);
    			
    			String sessionTime = systemManager.getSessionTimeConfig();        		
            	paramMap.put("cookieAlgorithm", systemManager.getCryptMethod());
        		AuthUser auth = userAuthManager.validateUser(request.getSession(), id, pass, domain);
        		boolean isChangePass = (auth.getAuthResult() == UserAuthManager.PASSWORD_CHANGE);
        		
        		if (auth.isAuthSuccess() || isChangePass) {
        			User allowUser = auth.getUser();
        			//allowUser.put(User.SESSION_CHECK_TIME, sessionTime);
        			//userAuthManager.doLoginProcess(request, response, allowUser,paramMap);
        			//LogManager.writeMailLogMsg(true,log, allowUser.get(User.EMAIL), request.getRemoteAddr(), "login");
        			foward = "success";
        			int domainSeq = Integer.parseInt(allowUser.get(User.MAIL_DOMAIN_SEQ));
        	                String accoutStatus = allowUser.get(User.ACCOUNT_STATUS);            
        	                
        	                if (isChangePass) {
        	                        foward = "noti";
        	                        request.setAttribute("language", language);
        	        				request.setAttribute("domainSeq", domainSeq);
        	        				request.setAttribute("userSeq", allowUser.get(User.MAIL_USER_SEQ));
        	                        request.setAttribute("userName", allowUser.get(User.USER_NAME));
        	                        request.setAttribute("notiType", "change");
        	                }
        			
        	                if ("dormant".equalsIgnoreCase(accoutStatus)) {
        	                    foward = "noti";
         	                    Map dormantMap = userAuthManager.readDormantMonth(domainSeq);
         	                    
        	                    if (dormantMap != null) {
                                        request.getSession().setAttribute("dormantUser", auth);
         	                        String manage = (String) dormantMap.get("long_term_manage");
        	                        String month = (String) dormantMap.get("long_term_month");
        	                        String changeMonth = (String) dormantMap.get("long_term_change_month");
        	                        request.setAttribute("manage", manage);
        	                        request.setAttribute("month", month);
        	                        request.setAttribute("changeMonth", changeMonth);
        	                        request.setAttribute("notiType", "dormant");
        	                        request.setAttribute("userName", allowUser.get(User.USER_NAME));
        	                    }
        	                } else {
        	                    allowUser.put(User.SESSION_CHECK_TIME, sessionTime);
        	                    String timestamp = checkUserExistManager.dupCheckProcess(allowUser.get(User.EMAIL),domainSeq);
        	        			allowUser.put(User.LOGIN_TIMESTAMP, timestamp);
        	                    userAuthManager.doLoginProcess(request, response, allowUser, paramMap);
        	                    LogManager.writeMailLogMsg(true, log, allowUser.get(User.EMAIL), request.getRemoteAddr(), "mobile_login"); //TCUSTOM-2155

        	                    if ("on".equalsIgnoreCase(savePasswd)) {
        	                        encPass = SecureUtil.encrypt(cryptMethod, "terrace-12345678", pass);
        	                        encPass = encPass.replaceAll("//+", ",");
        	                        encPass = encPass.replaceAll("///", "-");
        	                        encPass = encPass.replaceAll("=", "@");
        	                        encPass = URLEncoder.encode(encPass, "UTF-8");
        	        				encPass = encPass.replaceAll("\r", "").replaceAll("\n", "");
        	                        Cookie cookie = new Cookie("TSPASS", encPass);
        	                        cookie.setPath("/");
        	                        cookie.setMaxAge(365 * 24 * 60 * 60);
        	                        response.addCookie(cookie);
        	                    }
        	                }
        		} else {
        			String failMsg = userAuthManager.getFailProperty(auth.getAuthResult());
        			failMessage = resource.getMessage(failMsg);
        		}
    		} else {
    			failMessage = resource.getMessage("error.email");
    		}
    	} catch (Exception e) {
    		failMessage = resource.getMessage("error.msg.001");
    		log.error(e.getMessage(), e);
        }
    	Map<String, CommonLogoVO> commonLogoList = logoManager.readCommonLogoList(paramMap);
    	request.setAttribute("commonLogoList", commonLogoList);
    	request.setAttribute("failMessage", failMessage);
    	request.setAttribute("foward", foward);
    	
    	return foward;
	}

	public String getDefaultDomain() {
		return defaultDomain;
	}

	public void setDefaultDomain(String defaultDomain) {
		this.defaultDomain = defaultDomain;
	}
}
