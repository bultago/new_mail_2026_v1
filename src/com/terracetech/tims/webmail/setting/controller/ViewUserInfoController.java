package com.terracetech.tims.webmail.setting.controller;

import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.secure.policy.AllowPolicy;
import com.terracetech.secure.policy.LengthPolicy;
import com.terracetech.secure.policy.NotAllowPolicy;
import com.terracetech.secure.policy.Policy;
import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.manager.SettingSecureManager;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainCodeVO;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserInfoVO;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * 사용자 정보 조회 Controller
 * 
 * 주요 기능:
 * 1. 사용자 정보 조회
 * 2. RSA 키 생성
 * 3. 암호화 설정
 * 4. 도메인 코드 조회
 * 5. 보안 정책 적용
 * 
 * Struts2 Action: ViewUserInfoAction
 * 변환 일시: 2025-10-20
 */
@Controller("viewUserInfoController")
public class ViewUserInfoController {

	@Autowired
	private SettingManager settingManager;
	
	@Autowired
	private MailUserManager mailUserManager;
	
	@Autowired
	private SystemConfigManager systemManager;
	
	@Autowired
	private SettingSecureManager settingSecureManager;

	/**
	 * 사용자 정보 조회
	 * 
	 * @param authCheck 인증 체크
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "authCheck", required = false) String authCheck,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		Locale locale = I18nConstants.getBundleUserLocale(request);
		I18nResources resource = new I18nResources(locale, "setting");
		
		// 사용자 정보 VO 생성
		UserInfoVO userInfoVo = getUserInfo(user);
		
		// RSA 키 생성
		RSAKeyInfo rsaKeyInfo = generateRSAKeys();
		
		// 암호화 설정 조회
		EncryptionInfo encryptionInfo = getEncryptionInfo(user);
		
		// 도메인 코드 목록 조회
		List<MailDomainCodeVO> passCodeList = getDomainCodeList(user);
		
		// 보안 정책 적용
		SecurityPolicyInfo securityPolicy = applySecurityPolicies(user, request);
		
		// Model에 데이터 추가
		model.addAttribute("userInfoVo", userInfoVo);
		model.addAttribute("passCodeList", passCodeList);
		model.addAttribute("authCheck", authCheck);
		model.addAttribute("rsaPublicKey", rsaKeyInfo.getPublicKey());
		model.addAttribute("rsaPrivateKey", rsaKeyInfo.getPrivateKey());
		model.addAttribute("encryptionInfo", encryptionInfo);
		model.addAttribute("securityPolicy", securityPolicy);
		
		return "success";
	}

	/**
	 * 사용자 정보 VO 생성
	 * 
	 * @param user 사용자 정보
	 * @return UserInfoVO
	 * @throws Exception
	 */
	private UserInfoVO getUserInfo(User user) throws Exception {
		UserInfoVO userInfoVo = new UserInfoVO();
		
		// 기본 사용자 정보 설정
		userInfoVo.setMailUid(user.get(User.MAIL_UID));
		userInfoVo.setUserName(user.get(User.USER_NAME));
		userInfoVo.setMailDomain(user.get(User.MAIL_DOMAIN));
		userInfoVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		
		// 추가 사용자 정보 조회
		UserInfoVO additionalInfo = settingManager.getUserInfo(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		if (additionalInfo != null) {
			userInfoVo.setUserEtcInfo(additionalInfo.getUserEtcInfo());
			userInfoVo.setMobileNo(additionalInfo.getMobileNo());
			userInfoVo.setTelNo(additionalInfo.getTelNo());
		}
		
		return userInfoVo;
	}

	/**
	 * RSA 키 생성
	 * 
	 * @return RSAKeyInfo
	 * @throws Exception
	 */
	private RSAKeyInfo generateRSAKeys() throws Exception {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		KeyPair keyPair = keyGen.generateKeyPair();
		
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		
		// 공개키를 Base64로 인코딩
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
		
		String publicKeyStr = SecureUtil.encodeBase64(publicKey.getEncoded());
		String privateKeyStr = SecureUtil.encodeBase64(privateKey.getEncoded());
		
		return new RSAKeyInfo(publicKeyStr, privateKeyStr);
	}

	/**
	 * 암호화 설정 조회
	 * 
	 * @param user 사용자 정보
	 * @return EncryptionInfo
	 * @throws Exception
	 */
	private EncryptionInfo getEncryptionInfo(User user) throws Exception {
		EncryptionInfo encryptionInfo = new EncryptionInfo();
		
		// 시스템 암호화 설정 조회
		String encryptionEnabled = EnvConstants.getBasicSetting("encryption.enabled");
		encryptionInfo.setEncryptionEnabled("true".equals(encryptionEnabled));
		
		// 사용자별 암호화 설정 조회
		if (encryptionInfo.isEncryptionEnabled()) {
			String userEncryptionKey = settingSecureManager.getUserEncryptionKey(user.get(User.MAIL_USER_SEQ));
			encryptionInfo.setUserEncryptionKey(userEncryptionKey);
		}
		
		return encryptionInfo;
	}

	/**
	 * 도메인 코드 목록 조회
	 * 
	 * @param user 사용자 정보
	 * @return 도메인 코드 목록
	 * @throws Exception
	 */
	private List<MailDomainCodeVO> getDomainCodeList(User user) throws Exception {
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		return mailUserManager.readMailDomainCodeList(domainSeq);
	}

	/**
	 * 보안 정책 적용
	 * 
	 * @param user 사용자 정보
	 * @param request HttpServletRequest
	 * @return SecurityPolicyInfo
	 * @throws Exception
	 */
	private SecurityPolicyInfo applySecurityPolicies(User user, HttpServletRequest request) throws Exception {
		SecurityPolicyInfo securityPolicy = new SecurityPolicyInfo();
		
		// 비밀번호 정책 설정
		Policy passwordPolicy = new LengthPolicy(8, 20);
		passwordPolicy = new NotAllowPolicy(passwordPolicy, new String[]{"123456", "password", "admin"});
		securityPolicy.setPasswordPolicy(passwordPolicy);
		
		// 허용된 문자 정책
		Policy allowPolicy = new AllowPolicy("^[a-zA-Z0-9@._-]+$");
		securityPolicy.setAllowPolicy(allowPolicy);
		
		// 세션 보안 설정
		HttpSession session = request.getSession();
		securityPolicy.setSessionTimeout(session.getMaxInactiveInterval());
		
		return securityPolicy;
	}

	/**
	 * I18n 리소스 조회
	 * 
	 * @param user User
	 * @param module 모듈명
	 * @return I18nResources
	 */
	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}

	/**
	 * RSA 키 정보 VO
	 */
	private static class RSAKeyInfo {
		private String publicKey;
		private String privateKey;

		public RSAKeyInfo(String publicKey, String privateKey) {
			this.publicKey = publicKey;
			this.privateKey = privateKey;
		}

		public String getPublicKey() { return publicKey; }
		public String getPrivateKey() { return privateKey; }
	}

	/**
	 * 암호화 정보 VO
	 */
	private static class EncryptionInfo {
		private boolean encryptionEnabled;
		private String userEncryptionKey;

		public boolean isEncryptionEnabled() { return encryptionEnabled; }
		public void setEncryptionEnabled(boolean encryptionEnabled) { this.encryptionEnabled = encryptionEnabled; }
		public String getUserEncryptionKey() { return userEncryptionKey; }
		public void setUserEncryptionKey(String userEncryptionKey) { this.userEncryptionKey = userEncryptionKey; }
	}

	/**
	 * 보안 정책 정보 VO
	 */
	private static class SecurityPolicyInfo {
		private Policy passwordPolicy;
		private Policy allowPolicy;
		private int sessionTimeout;

		public Policy getPasswordPolicy() { return passwordPolicy; }
		public void setPasswordPolicy(Policy passwordPolicy) { this.passwordPolicy = passwordPolicy; }
		public Policy getAllowPolicy() { return allowPolicy; }
		public void setAllowPolicy(Policy allowPolicy) { this.allowPolicy = allowPolicy; }
		public int getSessionTimeout() { return sessionTimeout; }
		public void setSessionTimeout(int sessionTimeout) { this.sessionTimeout = sessionTimeout; }
	}
}
