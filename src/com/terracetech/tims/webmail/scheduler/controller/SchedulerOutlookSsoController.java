package com.terracetech.tims.webmail.scheduler.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.exception.UserAuthException;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;

/**
 * Outlook SSO 인증 Controller
 * 
 * 주요 기능:
 * 1. Outlook SSO 인증 처리
 * 2. 자동 로그인 처리
 * 3. 사용자 인증 체크
 * 4. 에러 처리
 * 
 * Struts2 Action: SchedulerOutlookSsoAction
 * 변환 일시: 2025-10-20
 */
@Controller("schedulerOutlookSsoController")
public class SchedulerOutlookSsoController {

	@Autowired
	private UserAuthManager userAuthManager;

	/**
	 * Outlook SSO 인증 처리
	 * 
	 * @param userId 사용자 ID
	 * @param mailDomain 메일 도메인
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" or "fail"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "mailDomain", required = false) String mailDomain,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// I18n 리소스 조회
		I18nResources resource = new I18nResources(I18nConstants.getBundleUserLocale(request), "common");
		
		String returnResult = "success";
		String message = "";
		
		try {
			// 인증 체크
			if (!isCheckAuth(request)) {
				throw new UserAuthException();
			}
			
			// SSO 인증 처리
			String result = processSsoAuthentication(userId, mailDomain, request);
			
			if (!"success".equals(result)) {
				message = resource.getMessage(result);
				returnResult = "fail";
			}
			
		} catch (UserAuthException e) {
			message = resource.getMessage("auth.fail");
			returnResult = "fail";
		} catch (Exception e) {
			message = resource.getMessage("error.msg.001");
			returnResult = "fail";
		}
		
		// Model에 결과 추가
		model.addAttribute("url", "/dynamic/scheduler/schedulerCommon.do");
		model.addAttribute("errMsg", message);
		model.addAttribute("actionType", "close");
		
		return returnResult;
	}

	/**
	 * 인증 체크
	 * 
	 * @param request HttpServletRequest
	 * @return 인증 체크 여부
	 */
	private boolean isCheckAuth(HttpServletRequest request) {
		// 인증 체크 로직 구현
		User user = SessionUtil.getUser(request);
		return user != null;
	}

	/**
	 * SSO 인증 처리
	 * 
	 * @param userId 사용자 ID
	 * @param mailDomain 메일 도메인
	 * @param request HttpServletRequest
	 * @return 처리 결과
	 * @throws Exception
	 */
	private String processSsoAuthentication(String userId, String mailDomain, HttpServletRequest request) throws Exception {
		User currentUser = SessionUtil.getUser(request);
		
		// 사용자 체크
		if (currentUser == null || userId.equals(currentUser.get(User.MAIL_UID))) {
			// 자동 로그인 처리
			return performAutoLogin(userId, mailDomain, request);
		}
		
		return "success";
	}

	/**
	 * 자동 로그인 수행
	 * 
	 * @param userId 사용자 ID
	 * @param mailDomain 메일 도메인
	 * @param request HttpServletRequest
	 * @return 로그인 결과
	 * @throws Exception
	 */
	private String performAutoLogin(String userId, String mailDomain, HttpServletRequest request) throws Exception {
		// 자동 로그인 로직 구현
		// 실제 구현에서는 AutoLoginAction의 execute 메서드 로직을 구현
		
		// 임시 구현
		User user = userAuthManager.authenticateUser(userId, null);
		if (user != null) {
			// 세션에 사용자 정보 저장
			request.getSession().setAttribute("user", user);
			return "success";
		}
		
		return "login.failed";
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
}