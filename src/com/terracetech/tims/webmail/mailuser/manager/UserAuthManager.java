/**
 * UserAuthManager.java 2008  2008-09-26
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.manager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.terracetech.secure.SecureManager;
import com.terracetech.secure.crypto.PasswordUtil;
import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.policy.PeriodPolicy;
import com.terracetech.secure.policy.Policy;
import com.terracetech.secure.policy.SecurePolicy;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.dao.SystemConfigDao;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.CheckUserExistManager;
import com.terracetech.tims.webmail.common.manager.LogoManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.common.vo.LogoVO;
import com.terracetech.tims.webmail.common.vo.MailConfigVO;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.UserInfo;
import com.terracetech.tims.webmail.mailuser.dao.MailDomainDao;
import com.terracetech.tims.webmail.mailuser.dao.MailUserDao;
import com.terracetech.tims.webmail.plugin.pki.PKIAuthParamBean;
import com.terracetech.tims.webmail.plugin.pki.PKIAuthResultBean;
import com.terracetech.tims.webmail.plugin.pki.PKIManager;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.QuotaUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.util.Validation;

/**
 * <p><strong>UserAuthManager.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class UserAuthManager {   

	private MailUserManager mailUserManager = null;
	private LogoManager logoManager = null;	
	
	
    public final static int SUCCESS = 1;
	public final static int FAIL = -1;
	public final static int TIME_OUT = -10;
	public final static int NOT_FOUND = -20;
	public final static int NOT_ALLOWED_IP = -30;
	public final static int PASSWORD_FAIL = -40;
	public final static int PASSWORD_FAIL_CNT = -45;
	public final static int WEBMAIL_SERVICE_FAIL = -50;
	public final static int WEBMAIL_SERVICE_EXPIRE = -55;
	public final static int QUOTA_WARNING = -60;
	public final static int QUOTA_FULL = -70;
	public final static int NOT_AGREE = -80;
	public final static int SERVICE_STOP = -85;
	public final static int PASSWORD_LOCK = -90;
	public final static int PASSWORD_CHANGE = -95;
	
	public final static int PKI_ERROR = -901;
	public final static int PKI_NO_DN = -902;
	
	public final static String LOCALE_JP = "JP";
	public final static String LOCALE_KO = "KO";
	public final static String LOCALE_EN = "EN";
	
	public final static String LOCKING_TIME = "locking_";
	
	
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
	
	private static Set<String> allowedKeys = null;
	private static Map<String, String> allowedSmKeysMap = null;
	private static Map<String, String> allowedKeysMap = null;
	
	static {
		Set<String> tmpSet = new HashSet<String>(33);
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
		//tmpSet.add(User.MAIL_SERVICES);
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
		//tmpSet.add(User.SAVE_SEND_BOX);
		//tmpSet.add(User.RECEIVE_NOTI);
		//tmpSet.add(User.VCARD_ATTACH);
		//tmpSet.add(User.WRITE_MODE);
		//tmpSet.add(User.CHAR_SET);
		//tmpSet.add(User.HIDDEN_IMG);
		//tmpSet.add(User.SIGN_ATTACH);
		//tmpSet.add(User.AFTER_LOGIN);
		//tmpSet.add(User.HIDDEN_TAG);
		//tmpSet.add(User.NOTI_INTERVAL);
		tmpSet.add(User.USER_SKIN);
		tmpMap.put(User.SM_USER_SKIN, User.USER_SKIN);
		//tmpSet.add(User.FORWARDING_MODE);
		//tmpSet.add(User.AUTO_SAVE_MODE);
		//tmpSet.add(User.AUTO_SAVE_TERM);
		tmpSet.add(User.USER_NAME);
		tmpMap.put(User.SM_USER_NAME, User.USER_NAME);
		//tmpSet.add(User.USER_LANGUAGE);
		tmpSet.add(User.EMAIL);
		tmpMap.put(User.SM_EMAIL, User.EMAIL);
		tmpSet.add(User.LOCALE);
		tmpMap.put(User.SM_LOCALE, User.LOCALE);
		tmpSet.add(User.ACCESS_TIME);
		tmpMap.put(User.SM_ACCESS_TIME, User.ACCESS_TIME);
		tmpSet.add(User.IMAP_LOGIN_ARGS);
		tmpMap.put(User.SM_IMAP_LOGIN_ARGS, User.IMAP_LOGIN_ARGS);
		tmpSet.add(User.WEBFOLDER_LOGIN_ARGS);
		tmpMap.put(User.SM_WEBFOLDER_LOGIN_ARGS, User.WEBFOLDER_LOGIN_ARGS);
		tmpSet.add(User.MAX_FORWARDING);
		tmpMap.put(User.SM_MAX_FORWARDING, User.MAX_FORWARDING);
		tmpSet.add(User.SESSION_CHECK_TIME);
		tmpMap.put(User.SM_SESSION_CHECK_TIME, User.SESSION_CHECK_TIME);
		tmpSet.add(User.FIRST_LOGIN);
		tmpMap.put(User.SM_FIRST_LOGIN, User.FIRST_LOGIN);
		tmpSet.add(User.USE_ACTIVE_X);
		tmpMap.put(User.SM_USE_ACTIVE_X, User.USE_ACTIVE_X);
		tmpSet.add(User.ACTIVE_X);
		tmpMap.put(User.SM_ACTIVE_X, User.ACTIVE_X);
		tmpSet.add(User.RENDER_MODE);
		tmpMap.put(User.SM_RENDER_MODE, User.RENDER_MODE);
		tmpSet.add(User.NOTE_USE);
		tmpMap.put(User.SM_NOTE_USE, User.NOTE_USE);
		tmpSet.add(User.JSESSIONID);
		tmpMap.put(User.SM_JSESSIONID, User.JSESSIONID);
		tmpSet.add(User.LOGINIP);
		tmpMap.put(User.SM_LOGINIP, User.LOGINIP);
		tmpSet.add(User.LOGIN_TIMESTAMP);
		tmpMap.put(User.SM_LOGIN_TIMESTAMP, User.LOGIN_TIMESTAMP);
		tmpSet.add(User.MOBILE_UID);
	        tmpMap.put(User.SM_MOBILE_UID, User.MOBILE_UID);
		
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
	
	private SystemConfigDao systemConfigDao = null;
	
	public void setSystemConfigDao(SystemConfigDao systemConfigDao) {
		this.systemConfigDao = systemConfigDao;
	}
	
	private SettingSecureManager settingSecureManager = null;
	
	public void setSettingSecureManager(SettingSecureManager settingSecureManager) {
		this.settingSecureManager = settingSecureManager;
	}
	
    public void setMailUserManager(MailUserManager mailUserManager){
    	this.mailUserManager = mailUserManager;
    }
   
	public void setLogoManager(LogoManager logoManager) {
		this.logoManager = logoManager;
	}
	
	/**
	 * ������� �α����� �ϴ� ��쿡 ����Ѵ�.
	 * 
	 * @param session
	 * @param empno
	 * @param pass
	 * @param domain
	 * @return
	 * @throws ParseException
	 */
	public AuthUser validateUserByEmpno(HttpSession session, String empno, String pass, String domain) throws ParseException{
		
		String id = mailUserManager.searchUserIdByEmpno(empno, domain);
		
        if (id == null) {
        	AuthUser authUser = new AuthUser();
        	authUser.setAuthResult(NOT_FOUND);
        	return authUser;
        }
        
        return validateUser(session, id, pass, domain);
	}
	
	public AuthUser validateUser(PKIAuthParamBean pkiParam) throws Exception{
		AuthUser authUser = new AuthUser();
		PKIManager pkiManager = new PKIManager();
		PKIAuthResultBean pkiAuthInfo = pkiManager.getLoginCertificate(pkiParam);
		
		if(pkiAuthInfo.isError()){
			authUser.setAuthResult(PKI_ERROR);
			LogManager.writeErr(this,pkiAuthInfo.getErrorMsg());
			return authUser;
		}
		
		if(!pkiAuthInfo.isAuth()){
			authUser.setAuthResult(NOT_FOUND);			
			return authUser;
		}
		
		User user = mailUserManager.readUserAuthInfo(pkiAuthInfo.getUserDn());		
        if (user == null) { 
        	authUser.setAuthResult(NOT_FOUND);
        	return authUser;
        }		
		
        if (!checkUserAgree(user)) {
			authUser.setAuthResult(NOT_AGREE);
			return authUser;
		}
		if (!checkUserStatus(user)) {
			authUser.setAuthResult(SERVICE_STOP);
			return authUser;
		}
		
		if (!checkWebmailServiceExpire(user)) {
			authUser.setAuthResult(WEBMAIL_SERVICE_EXPIRE);
			return authUser;
		}
		
		MailConfigVO mailConfigVo = systemConfigDao.readConfig("use_activex");
		user.put(User.USE_ACTIVE_X, (mailConfigVo == null) ? "on" : mailConfigVo.getConfigValue());
		
		String activeXUse = user.get(User.ACTIVE_X);
		user.put(User.ACTIVE_X, ("enable".equals(activeXUse))?"T":"F");
		
		mailUserManager.setUserSystemInfo(user);
		
		/*
	     * TCUSTOM-2070 2016-10-28 도메인별 MAIL_SERVICES 값에 대해 인증처리 
	     */
		if (!checkDomainWebmailService(Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)))) {
			authUser.setAuthResult(WEBMAIL_SERVICE_FAIL);
			return authUser;
		}
		
		if (!checkWebmailService(user)) {
			authUser.setAuthResult(WEBMAIL_SERVICE_FAIL);
			return authUser;
		}
		authUser.setAuthResult(SUCCESS);
		authUser.setUser(user);
		return authUser;		
	}

	public AuthUser validateUser(HttpSession session, String id, String pass, String domain) throws ParseException{
		AuthUser authUser = new AuthUser();
		
		if (!checkAccessIP("")) {
			authUser.setAuthResult(NOT_ALLOWED_IP);
			return authUser;
		}
		
		User user = mailUserManager.readUserAuthInfo(id, domain);
		
        if (user == null) { 
        	authUser.setAuthResult(NOT_FOUND);
        	return authUser;
        }
        
        int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        int failedCount = mailUserManager.readUserPassFailCount(userSeq);
        
        Map<String, Policy> policyMap = settingSecureManager.readPasswordPoliciesMap("webmail");
        
        if(checkLocking(policyMap, session,userSeq, failedCount)){
        	SecurePolicy policy = (SecurePolicy)policyMap.get(SecurePolicy.NAME);
			authUser.setAccessDeniedTerm(policy.getAccessDeniedTerm());
			authUser.setFailCount(policy.getFailCount());
			
        	authUser.setAuthResult(PASSWORD_LOCK);
        	return authUser;
        }
        
     // 20130523 이터레이션 방어 정책 로그인 제한 문구 추가
		SecurePolicy policy = (SecurePolicy)policyMap.get(SecurePolicy.NAME);
		if(policy != null && policy.isPolicyUsed()){
			
			authUser.setFailCurrCount(failedCount+1);
			authUser.setAccessDeniedTerm(policy.getAccessDeniedTerm());
			authUser.setFailCount(policy.getFailCount());
			authUser.setAuthResult(PASSWORD_FAIL_CNT);
		}
        
		if (!checkPasswd(pass, user.get(User.MAIL_PASSWORD))) {
			authUser.setAuthResult(PASSWORD_FAIL);
			
			mailUserManager.updateUserPassFailCount(userSeq, failedCount+1);
			
			return authUser;
		}else{
			mailUserManager.updateUserPassFailCount(userSeq, 0);
		}
		
		if (!checkUserAgree(user)) {
			authUser.setAuthResult(NOT_AGREE);
			return authUser;
		}
		if (!checkUserStatus(user)) {
			authUser.setAuthResult(SERVICE_STOP);
			return authUser;
		}
		
		if (!checkWebmailServiceExpire(user)) {
			authUser.setAuthResult(WEBMAIL_SERVICE_EXPIRE);
			return authUser;
		}
		
		MailConfigVO mailConfigVo = systemConfigDao.readConfig("use_activex");
		user.put(User.USE_ACTIVE_X, (mailConfigVo == null) ? "on" : mailConfigVo.getConfigValue());
		
		String activeXUse = user.get(User.ACTIVE_X);
		user.put(User.ACTIVE_X, ("enable".equals(activeXUse))?"T":"F");
		
		user.put(User.MAIL_DOMAIN, domain);
		mailUserManager.setUserSystemInfo(user);
		
		/*
	     * TCUSTOM-2070 2016-10-28 도메인별 MAIL_SERVICES 값에 대해 인증처리 
	     */
		if (!checkDomainWebmailService(Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)))) {
			authUser.setAuthResult(WEBMAIL_SERVICE_FAIL);
			return authUser;
		}
		
		if (!checkWebmailService(user)) {
			authUser.setAuthResult(WEBMAIL_SERVICE_FAIL);
			return authUser;
		}
	
		
		if(checkPasswordChangePeriod(policyMap, user, userSeq)){
        	authUser.setAuthResult(PASSWORD_CHANGE);
        	authUser.setUser(user);
        	session.setAttribute(User.MAIL_PASSWORD, user.get(User.MAIL_PASSWORD));
        	return authUser;
        }
		authUser.setAuthResult(SUCCESS);
		authUser.setUser(user);
		return authUser;
	}
	
	//20130523 이터레이션 로그인 제한 문구
	private boolean checkPasswdCnt(Map<String, Policy> policyMap, HttpSession session) throws ParseException{
		if(session==null)
			return false;
		
		SecurePolicy policy = (SecurePolicy)policyMap.get(SecurePolicy.NAME);
		
		if(policy != null && policy.isPolicyUsed()){
			return true;
		}
		
		return false;
	}
	
	public AuthUser validateUser(String id, String pass, String domain) throws ParseException{
		AuthUser authUser = new AuthUser();
		
		if (!checkAccessIP("")) {
			authUser.setAuthResult(NOT_ALLOWED_IP);
			return authUser;
		}
		
		User user = mailUserManager.readUserAuthInfo(id, domain);
		
        if (user == null) { 
        	authUser.setAuthResult(NOT_FOUND);
        	return authUser;
        }
        
		if (!checkUserAgree(user)) {
			authUser.setAuthResult(NOT_AGREE);
			return authUser;
		}
		if (!checkUserStatus(user)) {
			authUser.setAuthResult(SERVICE_STOP);
			return authUser;
		}
		
		if (!checkWebmailServiceExpire(user)) {
			authUser.setAuthResult(WEBMAIL_SERVICE_EXPIRE);
			return authUser;
		}
		
		MailConfigVO mailConfigVo = systemConfigDao.readConfig("use_activex");
		user.put(User.USE_ACTIVE_X, (mailConfigVo == null) ? "on" : mailConfigVo.getConfigValue());
		
		String activeXUse = user.get(User.ACTIVE_X);
		user.put(User.ACTIVE_X, ("enable".equals(activeXUse))?"T":"F");
		
		user.put(User.MAIL_DOMAIN, domain);
		mailUserManager.setUserSystemInfo(user);
		
		if (!checkWebmailService(user)) {
			authUser.setAuthResult(WEBMAIL_SERVICE_FAIL);
			return authUser;
		}
		authUser.setAuthResult(SUCCESS);
		authUser.setUser(user);
		return authUser;
	}
	
	private boolean checkAccessIP(String ip){
		return true;
	}
	
	private boolean checkPasswordChangePeriod(Map<String, Policy> policyMap, User user, int userSeq) throws ParseException{
		
		if(policyMap==null)
			return false;
		
		if(policyMap.get(PeriodPolicy.NAME)==null)
			return false;
		
		PeriodPolicy policy = (PeriodPolicy)policyMap.get(PeriodPolicy.NAME);
		
		if(!policy.isPolicyUsed()){
			return false;
		}
		
		//last_pass_modify_time
		String modifyTime = user.get("LAST_PASS_MODIFY_TIME");
		String createTime = user.get("CREATE_TIME");
		
		
		
		String lastPwChangeTimeStr = null;
		if(StringUtils.isEmpty(modifyTime)){
			if(StringUtils.isEmpty(createTime)){
				return true;
			}else{
				lastPwChangeTimeStr = createTime;
			}
		}else{
			lastPwChangeTimeStr = modifyTime;
		}
		
		if (StringUtils.isNotEmpty(lastPwChangeTimeStr)) {
			lastPwChangeTimeStr = lastPwChangeTimeStr.substring(0, 8);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date lastPwChangeTime = sdf.parse(lastPwChangeTimeStr);
		
		
		if(policy!= null && policy.isPolicyUsed()){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -policy.getChangeTerm());
			
			if(lastPwChangeTime.compareTo(cal.getTime()) <= 0){
				return true;
			}
		}
		
		return false;
	}
	
	//TODO ��ŷ�ܿ� �������ɼ��� �־�� �Ѵ�.
	private boolean checkLocking(Map<String, Policy> policyMap, HttpSession session, int userSeq, int failedCount) throws ParseException{
		if(session==null)
			return false;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Date today = new Date();
		
		SecurePolicy policy = (SecurePolicy)policyMap.get(SecurePolicy.NAME);
		if(policy != null && policy.isPolicyUsed()){
			String lastLoginTime = mailUserManager.readMailUserConfig(userSeq, LOCKING_TIME);
			if(StringUtils.isNotEmpty(lastLoginTime)){
				Date date1 = sdf.parse(lastLoginTime);
				long gap = today.getTime() - date1.getTime();
				long minute = gap / 1000 / 60;
				if (policy.getAccessDeniedTerm() == 0 || policy.getAccessDeniedTerm() < minute) {
                	mailUserManager.deleteMailUserConfig(userSeq, LOCKING_TIME);
                    mailUserManager.updateUserPassFailCount(userSeq, 0);
                    return false;
                } else {
                    return true;
                }
			}else{
				int permissionCount = policy.getFailCount();
				if(permissionCount> 0 && permissionCount <=failedCount){
					mailUserManager.saveMailUserConfig(userSeq, LOCKING_TIME, sdf.format(today));
                    mailUserManager.updateUserPassFailCount(userSeq, 0);
					return true;
				}	
			}
			
		}
		
		return false;
	}
	
	public void doLoginProcess (HttpServletRequest request, 
			HttpServletResponse response, 
			User user, Map<String, String> paramMap) throws UnsupportedEncodingException {
		
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
		    for (Cookie cookie : cookies) {
		        if("JSESSIONID".equals(cookie.getName())){
		        	 user.put("JSESSIONID", cookie.getValue());
		        }
		    }
		}
		
		setUserInfo(request, user);
		makeAuthCookie(response, user, paramMap.get("cookieAlgorithm"));
		makeLogoInfoCookie(paramMap, response, user);
		makeLoginIpCookie(request, response, user);
	}
	
	public String getUserInfoStr(HttpServletRequest request, User user) 
	        throws UnsupportedEncodingException {
	        setUserInfo(request, user);
	        remakeUser(user);
	        return getCookieStr(user);
	}
	
	public Map readDormantMonth(int mailDomainSeq) {
		return mailUserManager.readDormantMonth(mailDomainSeq);
	}
	
	public void updateUserCookieProcess (HttpServletRequest request, HttpServletResponse response, 
			User user, String cookieAlgorithm) throws UnsupportedEncodingException {		
		makeAuthCookie(response, user, cookieAlgorithm);		
	}
	
	public void updateUserLogoCookieProcess (Map<String, String> paramMap, HttpServletResponse response, 
			User user) throws UnsupportedEncodingException {		
		makeLogoInfoCookie(paramMap, response, user);		
	}
	
	private void setUserInfo (HttpServletRequest request, User user) {		
		if(user.get(User.WEBMAIL_LOGIN_TIME) == null || 
				user.get(User.WEBMAIL_LOGIN_TIME).length() == 0){
			user.put(User.FIRST_LOGIN,"F");
		} else {
			user.put(User.FIRST_LOGIN,"N");
		}
		setEmail (user);
		setQuotaInfo(user);
		setWebLoginInfo(user);
		setWebfolderLoginInfo(user);
		setUserLocale(request, user);
		setAccessTime(user);
		modifyLastLoginTime(request, user);
	}
	
	private void setEmail(User user) {
		user.put(User.EMAIL, user.get(User.MAIL_UID)+ "@" + user.get(User.MAIL_DOMAIN));
	}
	
	private void makeAuthCookie (HttpServletResponse response, User user, String cookieAlgorithm) throws UnsupportedEncodingException {
		remakeUser(user);		
		makeCookie(response, getEncryptedStr(getCookieStr(user), cookieAlgorithm));
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
	
	private void setQuotaInfo(User user) {
		
		/*long BIG_ATTACH_QUOTA_SIZE = 
			Long.parseLong(EnvConstants.getAttachSetting("bigattach.maxMBSize")) * FileUtil.SIZE_MEGA;*/		
		//web folder quota
		user.put(User.WEB_FOLDER_QUOTA, this.calculQuota(0, user.get(User.WEB_FOLDER_QUOTA), user.get(User.WEB_FOLDER_ADD_QUOTA)));
		
		//big attach quota
		//user.put(User.BIG_ATTACH_QUOTA, this.calculQuota(BIG_ATTACH_QUOTA_SIZE,user.get(User.BIG_ATTACH_QUOTA), user.get(User.BIG_ATTACH_ADD_QUOTA)));
		
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
	
	private String calculQuota (long defaultValue, String baseValue, String addValue) {
		long returnValue = defaultValue;
		
		if (baseValue != null && !"".equals(baseValue))
			returnValue = Long.parseLong(baseValue);
		if (addValue != null && !"".equals(addValue))
			returnValue += Long.parseLong(addValue);
		
		return Long.toString(returnValue);
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
	
	private void modifyLastLoginTime (HttpServletRequest request, User user) {
		String loginTimeInfo = FormatUtil.getBasicDateStr()+"|"+request.getRemoteAddr();
		mailUserManager.updateLoginTime(user.get(User.MAIL_USER_SEQ), loginTimeInfo);
	}
	
	public boolean checkWebmailService (User user) {
		String serviceMode = user.get(User.MAIL_SERVICES);
		if (serviceMode == null)
			return false;
		
		return checkWebmailService(Integer.parseInt(serviceMode));
	}
	
	/*
     * TCUSTOM-2070 2016-10-28 도메인별 MAIL_SERVICES 값에 대해 인증처리 
     */
	public boolean checkDomainWebmailService (int mailDomainSeq) {
		int domainMailServices = mailUserManager.readMailServiesFromDomain(mailDomainSeq);
		if (domainMailServices == 0)
			return false;
		return (domainMailServices & SERVICE_WEBMAIL) == 16;
	}
	
	private boolean checkWebmailServiceExpire(User user) throws ParseException {
		
		String expire = user.get(User.ACCOUNT_EXPIRE_DATE);
		
		if (StringUtils.isEmpty(expire)) {
			return true;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String current = sdf.format(new Date());
		Date expireDate = sdf.parse(expire);
		Date currentDate = sdf.parse(current);
		
		if (currentDate.getTime() > expireDate.getTime()) {
			return false;
		}
		
		return true;
	}
	
	public boolean checkUserAgree(User user) {
		String registerStatus = user.get(User.REGISTER_STATUS);
		if ("agree".equalsIgnoreCase(registerStatus)) {
			return true;
		}
		
		return false;
	}
	
	public boolean checkUserStatus(User user) {
		String accountStatus = user.get(User.ACCOUNT_STATUS);
		if ("enabled".equalsIgnoreCase(accountStatus) || "dormant".equalsIgnoreCase(accountStatus)) {
			return true;
		}
		
		return false;
	}
	
	public boolean checkWebmailService (int serviceMode) {
		return (serviceMode & SERVICE_WEBMAIL) != 0;
	}
	
	private String escape(String str) {
		return str.replaceAll("&", "|A|");
    }
	
	private static String unescape(String str) {
		return str.replaceAll("\\|A\\|", "&");
    }
	
	private void makeCookie(HttpServletResponse response, String cookieStr) throws UnsupportedEncodingException {
		makeCookie(response, EnvConstants.getBasicSetting("cookie.name"), cookieStr);
	}
	
	private void makeCookie(HttpServletResponse response, String cookieName, String cookieStr) throws UnsupportedEncodingException {
		String cookieDomain = EnvConstants.getBasicSetting("cookie.domain");
		
		// url encoding
		cookieStr = URLEncoder.encode(cookieStr, "UTF-8");
		cookieStr = cookieStr.replaceAll("\r", "").replaceAll("\n", "");
		Cookie cookie = new Cookie(cookieName, cookieStr);
		if (!Validation.isNull(cookieDomain)) {
			cookie.setDomain(cookieDomain);
		}
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	
	public void deleteCookie(Set<String> cookieKeys, HttpServletRequest request, HttpServletResponse response) {
		String cookieDomain = EnvConstants.getBasicSetting("cookie.domain");
		
		try {			
			Cookie[] cookies = request.getCookies();
			
			for (Cookie cookie : cookies) {			
				if (cookieKeys.contains(cookie.getName())) {					
					if (!Validation.isNull(cookieDomain)) {
						cookie.setDomain(cookieDomain);
					}
					cookie.setMaxAge(0);					
					response.addCookie(cookie);
				}
			}			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
	}
	
	private static String getEncryptedStr (String orgStr, String cookieAlgorithm) {

		if(StringUtils.isEmpty(cookieAlgorithm)){
			cookieAlgorithm = "AES";
		}
		
		SecureManager.setCookieAlgorithm(cookieAlgorithm);
		String cryptedStr = SecureManager.getCryptedCookieStr(orgStr);

		LogManager.writeDebug(UserAuthManager.class, "Encrypted cookie String : " + cryptedStr);
		return cryptedStr;
	}
	
	private void remakeUser (User user) {
		
		for (String key : user.getKeys()) {
			if (allowedKeys.contains(key))
				continue;
			
			user.removeKey(key);
		}
	}
	
	private String getCookieStr (User user){
		// TODO ���� ���ȭ
		Set<String> escapeStrKeySet = new HashSet<String>();
		escapeStrKeySet.add(User.SENDER_NAME);
		escapeStrKeySet.add(User.USER_NAME);
		
		StringBuffer cookieStr = new StringBuffer();
		String[] keys = user.getKeys();
		String value = null;
		for (int i = 0, keyCnt = keys.length; i < keyCnt; i++) {
			if (i > 0)
				cookieStr.append("&");
			
			cookieStr.append(allowedSmKeysMap.get(keys[i])).append("=");
			value = user.get(keys[i]);
			if (escapeStrKeySet.contains(keys[i]))
				value = escape(value);
			
			cookieStr.append(value);
		}
		LogManager.writeDebug(this, "Org Cookie string : " + cookieStr);
		return cookieStr.toString();
	}
	
	private void setAccessTime (User user){
		user.put(User.ACCESS_TIME, Long.toString(System.currentTimeMillis() / 1000));
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
			case NOT_ALLOWED_IP:
				failMsg = "auth.fail.ip";
				break;
			case PASSWORD_FAIL:
				failMsg = "auth.fail.password";
				break;
			case TIME_OUT:
				failMsg = "auth.fail.timeout";
				break;
			case WEBMAIL_SERVICE_FAIL:
				failMsg = "auth.fail.service";
				break;
			case NOT_AGREE:
				failMsg = "auth.fail.notagree";
				break;
			case PASSWORD_LOCK:
				failMsg = "auth.fail.lock";
				break;
			case PASSWORD_CHANGE:
				failMsg = "auth.fail";
				break;
			case SERVICE_STOP:
				failMsg = "auth.fail.service";
				break;				
			case PKI_ERROR:
				failMsg = "auth.fail.pkierr";
				break;
			case PKI_NO_DN:
				failMsg = "auth.fail.pkinodn";
				break;
			case WEBMAIL_SERVICE_EXPIRE:
				failMsg = "auth.fail.service.expire";
				break;
			case PASSWORD_FAIL_CNT:
				failMsg = "auth.fail.password.cnt";
				break;
			default:
				break;
		}
		return failMsg;
	}
	
	public boolean checkPasswd(String pass, String storedPass) {
		if(!isClearText(storedPass)){

			if (storedPass.startsWith("{SHA256}")||storedPass.startsWith("{SHA512}")) {
				int endIdx = storedPass.indexOf("}");
				String algorithm = storedPass.startsWith("{SHA256}")?"{SHA-256}":"{SHA-512}";
				String pwdStr = storedPass.substring(endIdx + 1);
				storedPass = algorithm+pwdStr;
			}
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
		
		if(storedPass.startsWith("{SHA"))
			return false;
		
		if(storedPass.startsWith("{MD5}"))
			return false;

		if(storedPass.startsWith("{SSHA}"))
			return false;

		return true;
	}
	
	public String getCryptMethod(){
		String cryptMethod = systemConfigDao.getCryptMrthodInfo();
		if(cryptMethod == null){
			cryptMethod = "AES";
		} 
		return cryptMethod.toUpperCase();		
	}
	
	public static User getUser (HttpServletRequest request, String cookieValue) throws UnsupportedEncodingException {
		User user = null;		
				
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
		String crptedStr = cookieValue;				
		//url decode
		crptedStr = URLDecoder.decode(crptedStr, "UTF-8");
		
		SecureManager.setCookieAlgorithm(cryptMethod);
		String decrpedStr = SecureManager.getDeCryptedCookieStr(crptedStr);
		
		String[] items = decrpedStr.split("&");
		String key = null;
		for (String item : items) {
			String[] values = item.split("=");
			key = allowedKeysMap.get(values[0]);
			if(key != null){
				user.put(key, values.length > 1 ? unescape(values[1]) : null);
			}
		}
		
		return user;
		
	}
	
	public static User getUser (HttpServletRequest request) throws UnsupportedEncodingException {
		User user = null;
		
		Cookie[] cookies = request.getCookies();
		
		if (cookies == null)
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

		for (Cookie cookie : cookies) {
			if (EnvConstants.getBasicSetting("cookie.name").equals(cookie.getName())) {
				user = new UserInfo();
				String crptedStr = cookie.getValue();
				
				//url decode
				crptedStr = URLDecoder.decode(crptedStr, "UTF-8");
				
				SecureManager.setCookieAlgorithm(cryptMethod);
				String decrpedStr = SecureManager.getDeCryptedCookieStr(crptedStr);
				
				String[] items = decrpedStr.split("&");
				String key = null;
				for (String item : items) {
					String[] values = item.split("=");
					key = allowedKeysMap.get(values[0]);
					if(key != null){
						user.put(key, values.length > 1 ? unescape(values[1]) : null);
					}
				}
			}
		}
		return user;
	}
	
	public static User getUserByStr(String userStr) throws Exception{
	        User user = null;
	        
	        if (StringUtils.isEmpty(userStr)) {
	            return null;
	        }
	        user = new UserInfo();
	        SystemConfigManager systemManager = (SystemConfigManager) ApplicationBeanUtil.getApplicationBean("systemConfigManager");
	        String cryptMethod = systemManager.getCryptMethod();
	        userStr = SecureUtil.decrypt(cryptMethod, EnvConstants.ENCRYPT_KEY, userStr);
	        
	        userStr = unescape(userStr);
	        String[] items = userStr.split("&");
	        for (String item : items) {
	            String[] values = item.split("=");
	            user.put(allowedKeysMap.get(values[0]),
	                    values.length > 1 ? item.substring(item.indexOf("=") + 1, item.length()) : null);
	        }
	        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
	        MailUserDao mailUserDao = (MailUserDao) ApplicationBeanUtil.getApplicationBean("mailUserDao");
	        user.put(User.USER_NAME, mailUserDao.readMailUserName(mailUserSeq));
	        
	        return user;
	}
	
	// TCUSTOM-2063 2016-10-31  - session-timeout : 0 에서 Heap 에서 session clear 가 되지 않아 수정됨 - S
	public static void checkSessionTime(HttpServletRequest request){
		String stimeKey = EnvConstants.getBasicSetting("timeout.session.name");
		HttpSession session = request.getSession();		
		String checktime = FormatUtil.getSessionDateStr();		
		session.setAttribute(stimeKey, checktime);		
	}
	
	public static void setSessionTime(HttpServletResponse response){
		// url encoding
		Cookie cookie = new Cookie(EnvConstants.getBasicSetting("timeout.session.name"), FormatUtil.getBasicDateStr());
		if (!Validation.isNull(EnvConstants.getBasicSetting("cookie.domain"))) {
			cookie.setDomain(EnvConstants.getBasicSetting("cookie.domain"));
		}
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public static String getSessionTime(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;
		
		for (Cookie cookie : cookies) {
			if (EnvConstants.getBasicSetting("timeout.session.name").equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}
	
	public static void removeSessionTime(HttpServletRequest request, HttpServletResponse response){
		Set<String> cookieKeys = new HashSet<String>(1);
		String sessionCookie = EnvConstants.getBasicSetting("timeout.session.name");
		cookieKeys.add(sessionCookie);
		
		try {			
			Cookie[] cookies = request.getCookies();
			
			for (Cookie cookie : cookies) {			
				if (cookieKeys.contains(cookie.getName())) {					
					if (!Validation.isNull(EnvConstants.getBasicSetting("cookie.domain"))) {
						cookie.setDomain(EnvConstants.getBasicSetting("cookie.domain"));
					}
					cookie.setMaxAge(0);					
					response.addCookie(cookie);
				}
			}			
		} catch (Exception e) {
			LogManager.writeErr(e.getMessage(), e);
		}
	}
	// TCUSTOM-2063 2016-10-31  - session-timeout : 0 에서 Heap 에서 session clear 가 되지 않아 수정됨 - E
	
	public static Map<String, String> getLogoInfo (HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String, String> logoInfoMap = null;
		
		Cookie[] cookies = request.getCookies();
		
		if (cookies == null)
			return logoInfoMap;

		for (Cookie cookie : cookies) {
			if ((EnvConstants.getBasicSetting("cookie.name")+"Logo").equals(cookie.getName())) {
				logoInfoMap = new HashMap<String, String>();
				String cookieStr = cookie.getValue();
				
				//url decode
				cookieStr = URLDecoder.decode(cookieStr, "UTF-8");
				cookieStr = unescape(cookieStr);
				String[] items = cookieStr.split("&");
				
				for (String item : items) {
					String[] values = item.split("=");
					logoInfoMap.put(values[0], values.length > 1 ? values[1] : ""); 
				}
			}
		}
		return logoInfoMap;
	}
	
	public AuthUser validateSso(String id, String domain) throws ParseException{
		AuthUser authUser = new AuthUser();
		Map<String, Policy> policyMap = settingSecureManager.readPasswordPoliciesMap("webmail");
		
		if (id == null) {
			authUser.setAuthResult(NOT_FOUND);
        	return authUser;
		}
		
		User user = mailUserManager.readUserAuthInfo(id, domain);
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
        if (user == null || user.getKeys().length == 0) { 
        	authUser.setAuthResult(NOT_FOUND);
        	return authUser;
        }
        
        if (!checkUserAgree(user)) {
			authUser.setAuthResult(NOT_AGREE);
			return authUser;
		}
        
        if (!checkUserStatus(user)) {
			authUser.setAuthResult(SERVICE_STOP);
			return authUser;
		}
        
        if (!checkWebmailServiceExpire(user)) {
			authUser.setAuthResult(WEBMAIL_SERVICE_EXPIRE);
			return authUser;
		}
        
        MailConfigVO mailConfigVo = systemConfigDao.readConfig("use_activex");
		user.put(User.USE_ACTIVE_X, (mailConfigVo == null) ? "on" : mailConfigVo.getConfigValue());
		
        String activeXUse = user.get(User.ACTIVE_X);
		user.put(User.ACTIVE_X, ("enable".equals(activeXUse))?"T":"F");
		mailUserManager.setUserSystemInfo(user);
		
		/*
	     * TCUSTOM-2070 2016-10-28 도메인별 MAIL_SERVICES 값에 대해 인증처리 
	     */
		if (!checkDomainWebmailService(Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)))) {
			authUser.setAuthResult(WEBMAIL_SERVICE_FAIL);
			return authUser;
		}
		
		if (!checkWebmailService(user)) {
			authUser.setAuthResult(WEBMAIL_SERVICE_FAIL);
			return authUser;
		}
		
		if(checkPasswordChangePeriod(policyMap, user, userSeq)){
        	authUser.setAuthResult(PASSWORD_CHANGE);
        	authUser.setUser(user);
        	return authUser;
        }
		
		authUser.setAuthResult(SUCCESS);
		authUser.setUser(user);
		return authUser;
	}
	
	public void makeLogoInfoCookie(Map<String, String> paramMap, HttpServletResponse response, User user) {
		
		String cookieDomain = EnvConstants.getBasicSetting("cookie.domain");
		StringBuffer cookieBuffer = new StringBuffer();
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));		
		
		try {
			LogoVO logoVo = logoManager.getLogoInfo(mailDomainSeq, paramMap);
			
			if (logoVo != null) {
				cookieBuffer.append("title=").append((logoVo.getTitle() == null) ? "" : logoVo.getTitle()).append("&");
				cookieBuffer.append("copyright=").append((logoVo.getCopyright() == null) ? "" : logoVo.getCopyright().replaceAll("\\&", "!")).append("&");
				cookieBuffer.append("logoUrl=").append((logoVo.getLogoImgUrl() == null) ? "" : logoVo.getLogoImgUrl());
				
				String cookieStr = cookieBuffer.toString();
				
				cookieStr = URLEncoder.encode(cookieStr, "UTF-8");
				cookieStr = cookieStr.replaceAll("\r", "").replaceAll("\n", "");
				Cookie cookie = new Cookie(EnvConstants.getBasicSetting("cookie.name")+"Logo", cookieStr);
				if (!Validation.isNull(cookieDomain)) {
					cookie.setDomain(cookieDomain);
				}
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
	}
	public void makeLoginIpCookie(HttpServletRequest request, HttpServletResponse response, User user) {
		
		String cookieDomain = EnvConstants.getBasicSetting("cookie.domain");
		StringBuffer cookieBuffer = new StringBuffer();
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));		
		
		try {
			String cookieStr = request.getRemoteAddr();
			
			cookieStr = URLEncoder.encode(cookieStr, "UTF-8");
			Cookie cookie = new Cookie(user.LOGINIP, cookieStr);
			if (!Validation.isNull(cookieDomain)) {
				cookie.setDomain(cookieDomain);
			}
			cookie.setPath("/");
			response.addCookie(cookie);
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
	}
	public String afterLogoutPath(int mailDomainSeq) {
		List<String> paramList = new ArrayList<String>(1);
		paramList.add("after_logout");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainSeq",mailDomainSeq);
		paramMap.put("configNames",paramList);
		
		Map map = systemConfigDao.getDomainConfig(paramMap);
		String afterLogout = "";
		if(map != null && map.containsKey("after_logout")){
			afterLogout = (String)map.get("after_logout");
		}
		
		return afterLogout;
	}

}