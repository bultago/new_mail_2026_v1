package com.terracetech.tims.webmail.scheduler.controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import jakarta.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebFolderUtils;

/**
 * Outlook 인증 Controller
 * 
 * 주요 기능:
 * 1. Outlook 계정 인증
 * 2. 암호화된 인증 정보 처리
 * 3. 인증 토큰 생성
 * 4. JSON 응답 처리
 * 
 * Struts2 Action: SchedulerOutlookAuthAction
 * 변환 일시: 2025-10-20
 */
@Controller("schedulerOutlookAuthController")
public class SchedulerOutlookAuthController {

	@Autowired
	private UserAuthManager userAuthManager;

	/**
	 * Outlook 인증 처리
	 * 
	 * @param email 이메일
	 * @param pass 비밀번호
	 * @param enc 암호화 여부
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "pass", required = false) String pass,
			@RequestParam(value = "enc", required = false) String enc,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		Locale locale = I18nConstants.getUserLocale(request);
		I18nResources resource = new I18nResources(locale, "common");

		// 인증 정보 처리
		AuthResult authResult = processOutlookAuth(email, pass, enc, user, resource);
		
		// JSON 응답 생성
		JSONObject jsonResponse = createAuthResponse(authResult);
		
		// Model에 응답 추가
		model.addAttribute("response", jsonResponse.toJSONString());
		
		return "success";
	}

	/**
	 * Outlook 인증 처리
	 * 
	 * @param email 이메일
	 * @param pass 비밀번호
	 * @param enc 암호화 여부
	 * @param user 사용자 정보
	 * @param resource I18n 리소스
	 * @return AuthResult
	 * @throws Exception
	 */
	private AuthResult processOutlookAuth(String email, String pass, String enc, User user, I18nResources resource) throws Exception {
		AuthResult result = new AuthResult();
		
		try {
			// 암호화된 정보 복호화
			String decryptedEmail = decryptAuthInfo(email, enc);
			String decryptedPass = decryptAuthInfo(pass, enc);
			
			// 사용자 인증 처리
			AuthUser auth = authenticateUser(decryptedEmail, decryptedPass);
			
			if (auth != null) {
				result.setSuccess(true);
				result.setMessage(resource.getMessage("outlook.auth.success"));
				result.setAuthToken(generateAuthToken(auth));
			} else {
				result.setSuccess(false);
				result.setMessage(resource.getMessage("outlook.auth.failed"));
			}
			
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage(resource.getMessage("outlook.auth.error"));
		}
		
		return result;
	}

	/**
	 * 인증 정보 복호화
	 * 
	 * @param encryptedData 암호화된 데이터
	 * @param enc 암호화 여부
	 * @return 복호화된 데이터
	 * @throws Exception
	 */
	private String decryptAuthInfo(String encryptedData, String enc) throws Exception {
		if (StringUtils.isEmpty(encryptedData)) {
			return "";
		}
		
		if ("true".equals(enc)) {
			// 암호화된 데이터 복호화
			SymmetricCrypt crypt = new SymmetricCrypt();
			return crypt.decrypt(encryptedData);
		}
		
		return encryptedData;
	}

	/**
	 * 사용자 인증
	 * 
	 * @param email 이메일
	 * @param password 비밀번호
	 * @return AuthUser
	 * @throws Exception
	 */
	private AuthUser authenticateUser(String email, String password) throws Exception {
		// 사용자 인증 로직 구현
		return userAuthManager.authenticateUser(email, password);
	}

	/**
	 * 인증 토큰 생성
	 * 
	 * @param auth 사용자 인증 정보
	 * @return 인증 토큰
	 */
	private String generateAuthToken(AuthUser auth) {
		// 인증 토큰 생성 로직 구현
		return SecureUtil.generateToken(auth.getUserId());
	}

	/**
	 * 인증 응답 JSON 생성
	 * 
	 * @param authResult 인증 결과
	 * @return JSONObject
	 */
	private JSONObject createAuthResponse(AuthResult authResult) {
		JSONObject response = new JSONObject();
		response.put("success", authResult.isSuccess());
		response.put("message", authResult.getMessage());
		if (authResult.getAuthToken() != null) {
			response.put("authToken", authResult.getAuthToken());
		}
		return response;
	}

	/**
	 * 인증 결과 VO
	 */
	private static class AuthResult {
		private boolean success;
		private String message;
		private String authToken;

		public boolean isSuccess() { return success; }
		public void setSuccess(boolean success) { this.success = success; }
		public String getMessage() { return message; }
		public void setMessage(String message) { this.message = message; }
		public String getAuthToken() { return authToken; }
		public void setAuthToken(String authToken) { this.authToken = authToken; }
	}
}
