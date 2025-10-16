package com.terracetech.tims.jmobile.common.manager;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.terracetech.secure.SecureManager;
import com.terracetech.secure.crypto.PasswordUtil;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.UserInfo;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.QuotaUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class UserAuthManager {

	public final static int SUCCESS = 1;
	public final static int FAIL = -1;
	public final static int TIME_OUT = -10;
	public final static int NOT_FOUND = -20;
	public final static int NOT_ALLOWED_IP = -30;
	public final static int PASSWORD_FAIL = -40;
	public final static int WEBMAIL_SERVICE_FAIL = -50;
	public final static int WEBMAIL_SERVICE_EXPIRE = -55;
	public final static int QUOTA_WARNING = -60;
	public final static int QUOTA_FULL = -70;
	public final static int NOT_AGREE = -80;
	public final static int SERVICE_STOP = -85;
	public final static int PASSWORD_LOCK = -90;
	public final static int PASSWORD_CHANGE = -95;
	
//  [ums | fax | pop | imap | webmail | security | voice | virus | smtpauth]
	public final static int SERVICE_UMS			= 1 << 0;
	public final static int SERVICE_FAX			= 1 << 1;
	public final static int SERVICE_POP			= 1 << 2;
	public final static int SERVICE_IMAP		= 1 << 3;
	public final static int SERVICE_WEBMAIL		= 1 << 4;
	public final static int SERVICE_SECURITY	= 1 << 5;
	public final static int SERVICE_VOICE		= 1 << 6;
	public final static int SERVICE_VIRUS		= 1 << 7;
	public final static int SERVICE_SMTPAUTH	= 1 << 8;
	
	public final static String LOCALE_JP = "JP";
	public final static String LOCALE_KO = "KO";
	public final static String LOCALE_EN = "EN";
	
	private static Set<String> allowedKeys = null;
	private static Map<String, String> allowedSmKeysMap = null;
	private static Map<String, String> allowedKeysMap = null;
	
	static {
		Set<String> tmpSet = new HashSet<String>(32);
		Map<String, String> tmpSmMap = new HashMap<String, String>();
		Map<String, String> tmpMap = new HashMap<String, String>();
		
		tmpSet.add(User.MAIL_USER_SEQ);
		tmpMap.put(User.SM_MAIL_USER_SEQ, User.MAIL_USER_SEQ);
		tmpSet.add(User.MAIL_UID);
		tmpMap.put(User.SM_MAIL_UID, User.MAIL_UID);
		tmpSet.add(User.MAIL_DOMAIN_SEQ);
		tmpMap.put(User.SM_MAIL_DOMAIN_SEQ, User.MAIL_DOMAIN_SEQ);
		tmpSet.add(User.MAIL_GROUP_SEQ);
		tmpMap.put(User.SM_MAIL_GROUP_SEQ, User.MAIL_GROUP_SEQ);
		tmpSet.add(User.MAIL_HOST);
		tmpMap.put(User.SM_MAIL_HOST, User.MAIL_HOST);
		tmpSet.add(User.MESSAGE_STORE);
		tmpMap.put(User.SM_MESSAGE_STORE, User.MESSAGE_STORE);
		tmpSet.add(User.MAIL_DOMAIN);
		tmpMap.put(User.SM_MAIL_DOMAIN, User.MAIL_DOMAIN);
		tmpSet.add(User.MAIL_QUOTA);
		tmpMap.put(User.SM_MAIL_QUOTA, User.MAIL_QUOTA);
		tmpSet.add(User.WEB_FOLDER_QUOTA);
		tmpMap.put(User.SM_WEB_FOLDER_QUOTA, User.WEB_FOLDER_QUOTA);
		tmpSet.add(User.BIG_ATTACH_QUOTA);
		tmpMap.put(User.SM_BIG_ATTACH_QUOTA, User.BIG_ATTACH_QUOTA);
		tmpSet.add(User.PAGE_LINE_CNT);
		tmpMap.put(User.SM_PAGE_LINE_CNT, User.PAGE_LINE_CNT);
		tmpSet.add(User.WEBMAIL_LOGIN_TIME);
		tmpMap.put(User.SM_WEBMAIL_LOGIN_TIME, User.WEBMAIL_LOGIN_TIME);
		tmpSet.add(User.USER_NAME);
		tmpMap.put(User.SM_USER_NAME, User.USER_NAME);
		tmpSet.add(User.EMAIL);
		tmpMap.put(User.SM_EMAIL, User.EMAIL);
		tmpSet.add(User.LOCALE);
		tmpMap.put(User.SM_LOCALE, User.LOCALE);
		tmpSet.add(User.IMAP_LOGIN_ARGS);
		tmpMap.put(User.SM_IMAP_LOGIN_ARGS, User.IMAP_LOGIN_ARGS);
		tmpSet.add(User.WEBFOLDER_LOGIN_ARGS);
		tmpMap.put(User.SM_WEBFOLDER_LOGIN_ARGS, User.WEBFOLDER_LOGIN_ARGS);
		
		allowedKeys = Collections.unmodifiableSet(tmpSet);
		allowedKeysMap = Collections.unmodifiableMap(tmpMap);
		
		Iterator<String> keys = allowedKeysMap.keySet().iterator();
	    while (keys.hasNext()) {
	    	String key = keys.next();
	    	String value = allowedKeysMap.get(key);
	    	tmpSmMap.put(value, key);
	    }
	    allowedSmKeysMap = Collections.unmodifiableMap(tmpSmMap);
		
		tmpSet = null;
		tmpMap = null;
		tmpSmMap = null;
	}
	
	private MailUserManager mailUserManager = null;
	
	public void setMailUserManager(MailUserManager mailUserManager){
	    this.mailUserManager = mailUserManager;
	}

	public static User getUser (HttpServletRequest request) throws UnsupportedEncodingException {
		User user = null;
		
		Object userObj = request.getSession().getAttribute(EnvConstants.getBasicSetting("cookie.name"));
		
		if (userObj == null)
			return user;
		
		String cryptMethod = null;
		try {
			cryptMethod = (String) request.getSession().getAttribute(EnvConstants.COOKIE_ALGORITHM_METHOD);
			if(StringUtils.isEmpty(cryptMethod)){
				SystemConfigManager systemManager = (SystemConfigManager) ApplicationBeanUtil.getApplicationBean("systemConfigManager");
				cryptMethod = systemManager.getCryptMethod();
				request.getSession().setAttribute(EnvConstants.COOKIE_ALGORITHM_METHOD, cryptMethod);
			}
		} catch (Exception e) {
			LogManager.writeErr(UserAuthManager.class, e.getMessage(), e);
			return null;
		}
		
		if(StringUtils.isEmpty(cryptMethod)){
			return null;
		}

		user = new UserInfo();
		String crptedStr = (String) userObj;
		SecureManager.setCookieAlgorithm(cryptMethod);
		String decrpedStr = SecureManager.getDeCryptedCookieStr(crptedStr);
		String[] items = decrpedStr.split("&");
		
		for (String item : items) {
			String[] values = item.split("=");
			user.put(allowedKeysMap.get(values[0]), values.length > 1 ? values[1] : null); 
		}
		return user;
	}
	
	public AuthUser validateUser(String id, String pass, String domain) throws ParseException{
		AuthUser authUser = new AuthUser();
		
		User user = mailUserManager.readUserAuthInfo(id, domain);
		
        if (user == null) { 
        	authUser.setAuthResult(NOT_FOUND);
        	return authUser;
        }
        
		if (!checkPasswd(pass, user.get(User.MAIL_PASSWORD))) {
			authUser.setAuthResult(PASSWORD_FAIL);
			
			return authUser;
		}
		
		user.put(User.MAIL_DOMAIN, domain);
		mailUserManager.setUserSystemInfo(user);
		
		authUser.setAuthResult(SUCCESS);
		authUser.setUser(user);
		return authUser;
	}
	
	public void doLoginProcess (HttpServletRequest request, User user, String authAlgorithm) {
		setUserInfo(request, user);
		makeAuthData(request, user, authAlgorithm);
	}
	
	public void doLogoutProcess(HttpServletRequest request){
		request.getSession().removeAttribute(EnvConstants.getBasicSetting("cookie.name"));
	}
	
	private void setUserInfo (HttpServletRequest request, User user) {		
		setEmail (user);
		setQuotaInfo(user);
		setWebLoginInfo(user);
		setWebfolderLoginInfo(user);
		setUserLocale(request, user);
		modifyLastLoginTime(request, user);
	}
	
	public boolean checkPasswd(String pass, String storedPass) {
		if(!isClearText(storedPass)){
			return PasswordUtil.certify(pass, storedPass).isAuthSuccessed();
		}
		
        return storedPass.equals(pass);
    }
	
	public static boolean isClearText(String storedPass){
		if(storedPass.startsWith("{AES}"))
			return false;
		
		if(storedPass.startsWith("{CLEARTEXT}"))
			return false;
		
		if(storedPass.startsWith("{TWOFISH}"))
			return false;
		
		if(storedPass.startsWith("{CRYPT}"))
			return false;
		
		if(storedPass.startsWith("{SHA}"))
			return false;
		
		if(storedPass.startsWith("{MD5}"))
			return false;
		
		return true;
	}
	
	private void makeAuthData(HttpServletRequest request, User user, String authAlgorithm) {
		remakeUser(user);
		makeSessionAuthData(request, getEncryptedStr(getAuthStr(user), authAlgorithm));
	}
	
	private void makeSessionAuthData(HttpServletRequest request, String encAuthStr) {
		request.getSession().setAttribute(EnvConstants.getBasicSetting("cookie.name"), encAuthStr);
	}
	
	private void remakeUser (User user) {
		
		for (String key : user.getKeys()) {
			if (allowedKeys.contains(key))
				continue;
			
			user.removeKey(key);
		}
	}
	
	private String getAuthStr (User user){
		StringBuffer authStr = new StringBuffer();
		String[] keys = user.getKeys();
		String value = null;
		for (int i = 0, keyCnt = keys.length; i < keyCnt; i++) {
			if (i > 0)
				authStr.append("&");
			
			authStr.append(allowedSmKeysMap.get(keys[i])).append("=");
			value = user.get(keys[i]);
			authStr.append(value);
		}
		LogManager.writeDebug(this, "Org Auth String : " + authStr);
		return authStr.toString();
	}
	
	private static String getEncryptedStr (String orgStr, String encAlgorithm) {

		if(StringUtils.isEmpty(encAlgorithm)){
			encAlgorithm = "AES";
		}
		
		SecureManager.setCookieAlgorithm(encAlgorithm);
		String cryptedStr = SecureManager.getCryptedCookieStr(orgStr);

		LogManager.writeDebug(UserAuthManager.class, "Encrypted Auth String : " + cryptedStr);
		return cryptedStr;
	}
	
	private void setEmail(User user) {
		user.put(User.EMAIL, user.get(User.MAIL_UID)+ "@" + user.get(User.MAIL_DOMAIN));
	}
	
	private void setQuotaInfo(User user) {
		user.put(User.WEB_FOLDER_QUOTA, QuotaUtil.calculQuota(0, user.get(User.WEB_FOLDER_QUOTA), user.get(User.WEB_FOLDER_ADD_QUOTA)));
		
		String delim = " ";
		user.put(User.QUOTA_STR, QuotaUtil.getQuotaStr(user));
        
        long webfolderQuotaValues = Long.parseLong(user.get(User.WEB_FOLDER_QUOTA));
        StringBuffer webfolderQuotaValueStr = new StringBuffer();
        webfolderQuotaValueStr
        			.append(webfolderQuotaValues / FileUtil.SIZE_MEGA).append(delim)
        			.append(webfolderQuotaValues % FileUtil.SIZE_MEGA).append(delim)
        			.append("100000 0 90 50");
        
        user.put(User.WEBFOLDER_QUOTA_STR, webfolderQuotaValueStr.toString());
	}
	
	private void setWebLoginInfo (User user) {
		String delim = " ";		
		
		String store = user.get(User.MESSAGE_STORE);
		String quotaStr = user.get(User.QUOTA_STR);
		
		user.put(User.IMAP_LOGIN_ARGS, store + delim + quotaStr + delim + 
				user.get(User.MAIL_USER_SEQ) + delim + user.get(User.MAIL_DOMAIN_SEQ));
	}
	
	private void setWebfolderLoginInfo (User user) {
		String delim = " ";		
		
		String store = user.get(User.MESSAGE_STORE);
		String quotaStr = user.get(User.WEBFOLDER_QUOTA_STR);
		
		user.put(User.WEBFOLDER_LOGIN_ARGS, store + 
				EnvConstants.getBasicSetting("webfolder.home") + delim + 
				quotaStr + delim + 
				user.get(User.MAIL_USER_SEQ) + delim + user.get(User.MAIL_DOMAIN_SEQ));
	}
	
	private void setUserLocale(HttpServletRequest request, User user) {

		String paramLan = request.getParameter("language");
		String systemLan = ((Locale)request.getLocale()).getLanguage();
		String configLan = user.get(User.USER_LOCALE);
		
		String userlocale = LOCALE_KO;
		String[] languages = {paramLan, configLan, paramLan, systemLan };
		for (String lan : languages) {
			if(lan != null && lan.length() > 0) {
				userlocale = lan;
				break;
			}
		}

		user.put(User.LOCALE, userlocale);
	}
	
	private void modifyLastLoginTime (HttpServletRequest request, User user) {
		String loginTimeInfo = FormatUtil.getBasicDateStr()+"|"+request.getRemoteAddr();
		mailUserManager.updateLoginTime(user.get(User.MAIL_USER_SEQ), loginTimeInfo);
	}
	
	public String getFailProperty (int authResult) {
		String failMsg = null;
		
		switch (authResult) {
			case FAIL:
				failMsg = "auth.fail";
				break;
			case NOT_FOUND:
				failMsg = "auth.fail.notfound";
				break;
			default:
				break;
		}
		return failMsg;
	}
}

