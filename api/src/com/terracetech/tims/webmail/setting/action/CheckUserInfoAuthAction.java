package com.terracetech.tims.webmail.setting.action;

import java.net.URLEncoder;
import java.security.PrivateKey;

import javax.crypto.Cipher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.util.AESUtils;
import com.terracetech.tims.webmail.util.StringUtils;

public class CheckUserInfoAuthAction extends BaseAction {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3251111698388040607L;
	private SettingManager settingManager = null;
	private String passwd = null;
	private String email = null;

	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
	
	public String execute() throws Exception {		
		
		passwd = request.getParameter("passwd");
		//passwd = AESUtils.decryptPass(passwd);
		
		/* Login ID, PW Parameter RSA Encrypt S */
		boolean loginParamterRSAUse = "true".equalsIgnoreCase(EnvConstants.getMailSetting("login.rsa.encrypt.use"));
		if(loginParamterRSAUse){
			PrivateKey privateKey = (PrivateKey) request.getSession().getAttribute("__rsaChangePasswordPrivateKey__");
			request.getSession().removeAttribute("__rsaChangePasswordPrivateKey__"); // 키의 재사용을 막는다. 항상 새로운 키를 받도록 강제.
	        if (privateKey == null) {
	            //throw new RuntimeException("암호화 비밀키 정보를 찾을 수 없습니다.");
	        }
	        String securedPassword = request.getParameter("securedPassword");
	        try {
	        	passwd = decryptRsa(privateKey, securedPassword);
	        } catch (Exception ex) {
	            throw new ServletException(ex.getMessage(), ex);
	        }
		}
		/* Login ID, PW Parameter RSA Encrypt E */
		email = request.getParameter("email");
		I18nResources resource = getMessageResource("setting");
		
		User user = UserAuthManager.getUser(request);
		boolean isSuccess = false;
		String returnPath = "";
		try {
			isSuccess = settingManager.checkUserPass(Integer.parseInt(user.get(User.MAIL_USER_SEQ)), passwd);
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		
		if (isSuccess) {
			returnPath = "success";
			String encodeEmail = SecureUtil.encrypt(SymmetricCrypt.AES, "terrace-12345678", email);
			String cookieDomain = EnvConstants.getBasicSetting("cookie.domain");

			encodeEmail = URLEncoder.encode(encodeEmail, "UTF-8");
			encodeEmail = encodeEmail.replaceAll("\r", "").replaceAll("\n", "");
			Cookie cookie = new Cookie("PSID", encodeEmail);
			if (StringUtils.isNotEmpty(cookieDomain)) {
				cookie.setDomain(cookieDomain);
			}
			cookie.setPath("/");
			response.addCookie(cookie);
		} else {
			String msg = resource.getMessage("conf.alert.userinfo.authfail");
			request.setAttribute("msg", msg);
			returnPath = "fail";
		}
		
		return returnPath;
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
