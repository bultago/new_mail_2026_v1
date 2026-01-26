package com.terracetech.tims.webmail.setting.action;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;

public class ViewUserInfoAuthAction extends BaseAction {
	
	private SettingManager settingManager = null;
	private String uid = null;
	private String email = null;
	private String name = null;

	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
	
	public String execute() throws Exception {
		
		User user = UserAuthManager.getUser(request);
		
		uid = user.get(User.MAIL_UID);
		email = uid + "@" +user.get(User.MAIL_DOMAIN);
		name = user.get(User.USER_NAME);
		
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {			
			if ("PSID".contains(cookie.getName())) {					
				cookie.setPath("/");
				cookie.setMaxAge(0);					
				response.addCookie(cookie);
				break;
			}
		}
		
		/* Login ID, PW Parameter RSA Encrypt S */
		HttpSession session = request.getSession();
		boolean loginParamterRSAUse = "true".equalsIgnoreCase(EnvConstants.getMailSetting("login.rsa.encrypt.use"));
		request.setAttribute("loginParamterRSAUse", String.valueOf(loginParamterRSAUse));
		if(loginParamterRSAUse){
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            
            KeyPair keyPair = generator.genKeyPair();
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 세션에 공개키의 문자열을 키로하여 개인키를 저장한다.
            session.setAttribute("__rsaChangePasswordPrivateKey__", privateKey);

            // 공개키를 문자열로 변환하여 JavaScript RSA 라이브러리 넘겨준다.
            RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

            String publicKeyModulus = publicSpec.getModulus().toString(16);
            String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

            request.setAttribute("publicKeyModulus", publicKeyModulus);
            request.setAttribute("publicKeyExponent", publicKeyExponent);
		}
		/* Login ID, PW Parameter RSA Encrypt E */
		
		return "success";
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
