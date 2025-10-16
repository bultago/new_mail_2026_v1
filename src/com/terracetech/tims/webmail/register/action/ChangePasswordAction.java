package com.terracetech.tims.webmail.register.action;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.servlet.http.HttpSession;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;

public class ChangePasswordAction extends BaseAction {
	
	private MailUserManager mailUserManager = null;
	private String userId = null;
	private String mailDomain = null;
	private int mailDomainSeq = 0;
	private int mailUserSeq = 0;
	
	public ChangePasswordAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}

	public String execute() throws Exception {
		
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

	public int getMailDomainSeq() {
		return mailDomainSeq;
	}

	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public int getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMailDomain() {
		return mailDomain;
	}

	public void setMailDomain(String mailDomain) {
		this.mailDomain = mailDomain;
	}
	
}
