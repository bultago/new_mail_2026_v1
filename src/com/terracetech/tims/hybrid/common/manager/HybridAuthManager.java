package com.terracetech.tims.hybrid.common.manager;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.tims.hybrid.common.HybridAuth;
import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.dao.HybridMobileDao;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.dao.SystemConfigDao;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.common.vo.MailConfigVO;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.AccessCheckUtil;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.util.Validation;

public class HybridAuthManager {
    private static final Logger logger = LoggerFactory.getLogger(HybridAuthManager.class);
    
    public final static String HYBRID_SSO_KEY = "TMSHYBRID";
    
    private UserAuthManager userAuthManager = null;
    private SystemConfigManager systemManager = null;
    private HybridMobileDao hybridMobileDao = null;
    
    public void setUserAuthManager(UserAuthManager userAuthManager) {
        this.userAuthManager = userAuthManager;
    }

    public void setSystemManager(SystemConfigManager systemManager) {
        this.systemManager = systemManager;
    }

    public void setHybridMobileDao(HybridMobileDao hybridMobileDao) {
        this.hybridMobileDao = hybridMobileDao;
    }

    public HybridAuth validateUser(HttpSession session, String email, String uuid, String pass) throws Exception {
        HybridAuth hybridAuth = new HybridAuth();

        if (StringUtils.isEmpty(uuid) || !Validation.isValidateEmail(email)) {
            hybridAuth.setAuthResult(HybridErrorCode.NOT_ALLOW);
            return hybridAuth;
        }
        
        String[] authData = email.split("@");
        String id = authData[0];
        String domain = authData[1];
        
        AuthUser auth = userAuthManager.validateUser(session, id, pass, domain);
        hybridAuth.convertAuthCode(auth);
        
        return hybridAuth;
    }
    
    public boolean useMobileLicense() {
    	MailConfigVO config = systemManager.getMailConfig("mobile");
    	if (config == null) return false;
    	return "enabled".equalsIgnoreCase(config.getConfigValue());
    }
    
    public boolean allowDeviceAppVersion(String appVer, String deviceType, String devicever) {
    	
    	if (StringUtils.isEmpty(appVer) || StringUtils.isEmpty(deviceType) || StringUtils.isEmpty(devicever)) {
    		return false;
    	}
    	try {
	    	String versionInfo = EnvConstants.getUtilSetting("mobile.version.limit");
	    	if (versionInfo == null) return false;
	    	String[] checkTarget = versionInfo.split("\\|");
	    	if (checkTarget == null || checkTarget.length > 3) {
	    		return false;
	    	}
	    	String appVersion = checkTarget[0].split("=")[1];
	    	appVersion = makeVersionFormat(appVersion);
	    	appVer = makeVersionFormat(appVer);
	    	float inputAppVerNum = Float.parseFloat(appVer);
	    	float storeAppVerNum = Float.parseFloat(appVersion);
	    	if (storeAppVerNum > inputAppVerNum) {
	    		return false;
	    	}
	    	
	    	String deviceVersion = "0";
	    	if ("iphone".equalsIgnoreCase(deviceType)) {
	    		deviceVersion = checkTarget[1].split("=")[1];
	    	} else if ("android".equalsIgnoreCase(deviceType)) {
	    		deviceVersion = checkTarget[2].split("=")[1];
	    	}
	    	devicever = makeVersionFormat(devicever);
	    	deviceVersion = makeVersionFormat(deviceVersion);
	    	float inputDeviceVerNum = Float.parseFloat(devicever);
	    	float storeDeviceVerNum = Float.parseFloat(deviceVersion);
	    	if (storeDeviceVerNum > inputDeviceVerNum) {
	    		return false;
	    	}
	    	
    	} catch (Exception e) {
    	        logger.error("Error occurred", e);
    		return false;
		}
    	return true;
    }
    
    private String makeVersionFormat(String version) {
    	int versionIdx = version.indexOf(".");
    	if (versionIdx > -1) {
	    	String unit = version.substring(0, versionIdx);
	    	String mod = version.substring(versionIdx);
	    	mod = mod.replaceAll("\\.", "");
	    	version = unit+"."+mod;
    	}
    	return version;
    }
    
    public String doLoginProcess(HttpServletRequest request, String uuid, User user) throws Exception {
        String cryptMethod = systemManager.getCryptMethod();
        
        String authKey = user.get(User.MAIL_USER_SEQ)+"|"+uuid;
        authKey = SecureUtil.encrypt(cryptMethod, EnvConstants.ENCRYPT_KEY, authKey);
        authKey = authKey.trim();
        authKey = StringUtils.convertStringToHex(authKey);
        user.put(User.MOBILE_UID, uuid);
        String authValue = userAuthManager.getUserInfoStr(request, user);
        authValue = SecureUtil.encrypt(cryptMethod, EnvConstants.ENCRYPT_KEY, authValue);
        authValue = authValue.trim();
        hybridMobileDao.deleteHybridAuth(authKey);
        hybridMobileDao.insertHybridAuth(authKey, authValue);
        
        return authKey;
    }
    
    public void makeSsoCookie(String authKey, HttpServletResponse response) throws Exception {
        String cryptMethod = systemManager.getCryptMethod();
        String ssoKey = SecureUtil.encrypt(cryptMethod, EnvConstants.ENCRYPT_KEY, authKey);
        ssoKey = URLEncoder.encode(ssoKey, "UTF-8");
        Cookie cookie = new Cookie(HybridAuthManager.HYBRID_SSO_KEY, ssoKey);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    
    public static User getUser(String authKey) throws Exception{
        User user = null;
        if (StringUtils.isEmpty(authKey)) {
            return null;
        }
        
        try {
            HybridMobileDao hybridMobileDao = (HybridMobileDao)ApplicationBeanUtil.getApplicationBean("hybridMobileDao");
            String authValue = hybridMobileDao.readHybridAuth(authKey);
            
            user = UserAuthManager.getUserByStr(authValue);
        } catch (Exception e) {
            logger.error("Error occurred", e);
        }
        
        return user;
    }

    public static boolean checkMobieAccess(User user) {
        
        SystemConfigDao systemConfigDao = (SystemConfigDao)ApplicationBeanUtil.getApplicationBean("systemConfigDao");
        Map<String, String> webAccessMap = systemConfigDao.getWebAccessConfig();
        String accessType = webAccessMap.get(AccessCheckUtil.WEB_ACCESS_TYPE);
        if (!"part".equalsIgnoreCase(accessType)) {
            return true;
        }
        
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        HybridMobileDao hybridMobileDao = (HybridMobileDao)ApplicationBeanUtil.getApplicationBean("hybridMobileDao");
        String accessConfig = hybridMobileDao.readMobileAccessConfig(mailDomainSeq);
        if ("off".equalsIgnoreCase(accessConfig)) {
            return false;
        }
        
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        Map<String, String> mobileKeyMap = hybridMobileDao.readUserMobileAccessKey(mailUserSeq);
        String uniqueKey = user.get(User.MOBILE_UID);
        if (mobileKeyMap == null || mobileKeyMap.isEmpty() || !mobileKeyMap.containsKey(uniqueKey)) {
            return false;
        }
        
        return true;
    }

    public static String checkMobileSso(HttpServletRequest request) throws Exception {
        
        String authKey = null;
        
        Cookie[] cookies = request.getCookies();

        if (cookies == null)
            return null;
        
        try {
            for (Cookie cookie : cookies) {
                if (HYBRID_SSO_KEY.equals(cookie.getName())) {
                    String crptedStr = cookie.getValue();

                    crptedStr = URLDecoder.decode(crptedStr, "UTF-8");
    
                    SystemConfigManager systemManager = (SystemConfigManager) ApplicationBeanUtil.getApplicationBean("systemConfigManager");
                    String cryptMethod = systemManager.getCryptMethod();
                    authKey = SecureUtil.decrypt(cryptMethod, EnvConstants.ENCRYPT_KEY, crptedStr);
                }
            }
            } catch (Exception e) {
                logger.error("Error occurred", e);
            }
        
        return authKey;
    }
    
    public static void deleteMobileCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        
        if (cookies == null)
            return;
        
        try {
            for (Cookie cookie : cookies) {
                if (HYBRID_SSO_KEY.equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred", e);
        }
    }
}
