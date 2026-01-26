package com.terracetech.tims.webmail.mailuser.action;

import java.io.PrintWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Cipher;
import jakarta.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.ExtPartConstants;
import com.terracetech.tims.webmail.common.MakeMessage;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.CheckUserExistManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.plugin.pki.PKIAuthParamBean;

public class LoginAction extends BaseAction {
	public Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final long serialVersionUID = 3524773920732540358L;
	
	private final static String DEFAULT_RETURN_PATH = "/common/welcome.do";
	private String returnPath = DEFAULT_RETURN_PATH;	
	
	private UserAuthManager userAuthManager = null;
	private SystemConfigManager systemManager = null;
	private CheckUserExistManager checkUserExistManager = null;
	String result = "fail";
	String returnMessage = "success";
	
	public LoginAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	public void setCheckUserExistManager(
			CheckUserExistManager checkUserExistManager) {
		this.checkUserExistManager = checkUserExistManager;
	}
	
	public String execute() throws Exception {
		LogManager.printElapsedTime("login", "LoginAction START");
		
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
		String loginMode = request.getParameter("loginMode");
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		String domain = request.getParameter("domain");
		
		/* Login ID, PW Parameter RSA Encrypt S */
		boolean loginParamterRSAUse = "true".equalsIgnoreCase(EnvConstants.getMailSetting("login.rsa.encrypt.use"));
		if(loginParamterRSAUse){
			PrivateKey privateKey = (PrivateKey) request.getSession().getAttribute("__rsaPrivateKey__");
			request.getSession().removeAttribute("__rsaPrivateKey__"); // 키의 재사용을 막는다. 항상 새로운 키를 받도록 강제.
	        if (privateKey == null) {
	        	response.setHeader("Content-Type", "text/html; charset=UTF-8");
	        	PrintWriter out = response.getWriter();
	        	String msg = resource.getMessage("login.rsa.fail.no.privatekey");
	        	out.print(MakeMessage.printAlertUrl(msg, returnPath));
    		    out.flush();
    		    return null;
	            //throw new RuntimeException("암호화 비밀키 정보를 찾을 수 없습니다.");
	        }
	        String securedId = request.getParameter("securedId");
	        String securedPassword = request.getParameter("securedPassword");
	        try {
	            String userId = decryptRsa(privateKey, securedId);
	            String password = decryptRsa(privateKey, securedPassword);
	            id = userId;
	            pass = password;
	        } catch (Exception ex) {
	        	ex.printStackTrace();
	        	response.setHeader("Content-Type", "text/html; charset=UTF-8");
	        	PrintWriter out = response.getWriter();
	        	String msg = resource.getMessage("login.rsa.fail.descrypt");
	        	out.print(MakeMessage.printAlertUrl(msg, returnPath));
    		    out.flush();
    		    return null;
	            //throw new ServletException(ex.getMessage(), ex);
	        }
		}
		/* Login ID, PW Parameter RSA Encrypt E */
				
		String attachPath = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
		String strLocalhost = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
		
		id = id.toLowerCase();
		domain = domain.toLowerCase();
		
		log.debug("id :" + ((id != null)?id:""));
		log.debug("domain :" + ((domain != null)?domain:""));
		log.debug("attachPath :" + attachPath);
		log.debug("strLocalhost :" + strLocalhost);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("attachPath", attachPath);
		paramMap.put("attachUrl", EnvConstants.getAttachSetting("attach.url"));
		paramMap.put("localUrl", strLocalhost);
		
		String sessionTime = systemManager.getSessionTimeConfig();
		request.getSession().setAttribute(EnvConstants.COOKIE_ALGORITHM_METHOD, systemManager.getCryptMethod());
    	paramMap.put("cookieAlgorithm", systemManager.getCryptMethod());

    	try {
    		AuthUser auth= null;
    		if("pki".equalsIgnoreCase(loginMode)){
    			auth = userAuthManager.validateUser(getPKIParamBean());
    		} else {    			
    			auth = userAuthManager.validateUser(request.getSession(), id, pass, domain);
    		}
    		LogManager.printElapsedTime("login", "userAuthManager.validateUser");
    		
    		boolean isChangePass = (auth.getAuthResult() == UserAuthManager.PASSWORD_CHANGE);
    		if (auth.isAuthSuccess() || isChangePass) {
    			User allowUser = auth.getUser();
    			// ���� �α��ν� �н����� ����
    			int domainSeq = Integer.parseInt(allowUser.get(User.MAIL_DOMAIN_SEQ));    			
    			String accoutStatus = allowUser.get(User.ACCOUNT_STATUS);
    			String userName = allowUser.get(User.USER_NAME);
    			request.setAttribute("mailDomainSeq", allowUser.get(User.MAIL_DOMAIN_SEQ));
    			request.setAttribute("mailUserSeq", allowUser.get(User.MAIL_USER_SEQ));
    			request.setAttribute("mailUid", allowUser.get(User.MAIL_UID));
    			request.setAttribute("userName", userName);
    			
    			if(isChangePass){
    				request.getSession().setAttribute("allowUser", allowUser);
    				return "change";
    			}
    			
    			if ("dormant".equalsIgnoreCase(accoutStatus)) {
    				@SuppressWarnings("rawtypes")
					Map dormantMap = userAuthManager.readDormantMonth(domainSeq);    				
    				if (dormantMap != null) {
    					request.getSession().setAttribute("dormantUser", auth);
    					String manage = (String)dormantMap.get("long_term_manage");
    					String month = (String)dormantMap.get("long_term_month");
    					String changeMonth = (String)dormantMap.get("long_term_change_month");
    					request.setAttribute("manage", manage);
    					request.setAttribute("month", month);
    					request.setAttribute("changeMonth", changeMonth);
    				}
    				return "dormant";
    			}

    			allowUser.put(User.SESSION_CHECK_TIME, sessionTime);
    			
    			
    			//duplication login check 
    			int mailDomainSeq = Integer.parseInt(allowUser.get(User.MAIL_DOMAIN_SEQ));
    			String timestamp = checkUserExistManager.dupCheckProcess(allowUser.get(User.EMAIL),mailDomainSeq);
    			allowUser.put(User.LOGIN_TIMESTAMP, timestamp);
    			
    			userAuthManager.doLoginProcess(request, response, allowUser,paramMap);
    			boolean isFirstLogin = "F".equals(allowUser.get(User.FIRST_LOGIN));
    			request.setAttribute("user", allowUser);//���� ���ȭ
    			LogManager.writeMailLogMsg(true,log, allowUser.get(User.EMAIL), 
    					request.getRemoteAddr(), "login");
    			if((isFirstLogin && systemManager.isPasswordChange(domainSeq))||(auth.getAuthResult()==UserAuthManager.PASSWORD_CHANGE)){
    				return "change";
    			}
    		} else {
    			String failMsg = userAuthManager.getFailProperty(auth.getAuthResult());
    			returnMessage = "fail," + failMsg;
    			response.setHeader("Content-Type", "text/html; charset=UTF-8");
    			PrintWriter out = response.getWriter();
    			
    			String msg = resource.getMessage(failMsg);
    			
    			if(auth.getAuthResult()==UserAuthManager.PASSWORD_FAIL_CNT){
    				String password_fail_val[] = new String[3];
    				password_fail_val[0] = Integer.toString(auth.getFailCurrCount());
    				password_fail_val[1] = Integer.toString(auth.getFailCount());
    				password_fail_val[2] = Integer.toString(auth.getAccessDeniedTerm());
    				
    				
    				msg = resource.getMessage(failMsg, password_fail_val);
    			}else if(auth.getAuthResult()==UserAuthManager.PASSWORD_LOCK){
    				String password_lock_val[] = new String[2];
    				password_lock_val[0] =  Integer.toString(auth.getFailCount());
    				password_lock_val[1] = Integer.toString(auth.getAccessDeniedTerm());
    				
    				msg = resource.getMessage(failMsg, password_lock_val);
    			}else{
    				msg = resource.getMessage(failMsg);
    			}
    			
    			
    		    out.print(MakeMessage.printAlertUrl(msg, returnPath));
    		    out.flush();
    		    
    		    return null;
    		}
    		log.debug(auth);
    		result = returnMessage;
    		returnPath = "success";
    		
    		LogManager.printElapsedTime("login", "LoginAction END");
    		return returnPath;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		
	}
	
	private PKIAuthParamBean getPKIParamBean(){
		PKIAuthParamBean paramBean = new PKIAuthParamBean();
		if(ExtPartConstants.VENDER_SOFTFORUM == ExtPartConstants.getPKIVender()){		
			paramBean.setSignedText(request.getParameter("pkiSignText"));
		} else if(ExtPartConstants.VENDER_INITECH_V7 == ExtPartConstants.getPKIVender()){			
			paramBean.setUserDN(request.getParameter("_shttp_client_cert_subject_"));
		} else if(ExtPartConstants.VENDER_KERIS == ExtPartConstants.getPKIVender()){
			paramBean.setSignedText(request.getParameter("pkiSignText"));
		}
		return paramBean;
	}

	
	
	public String getResult() {
		return result;
	}
	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}
	
	private String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
        return decryptedValue;
    }
	
	/**
     * 16진 문자열을 byte 배열로 변환한다.
     */
	private static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[]{};
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte)Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }
}