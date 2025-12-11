package com.terracetech.tims.webmail.setting.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;

/**
 * 사용자 정보 인증 체크 Controller
 * 
 * 주요 기능:
 * 1. 사용자 정보 인증 체크
 * 2. 인증 결과 반환
 * 
 * Struts2 Action: CheckUserInfoAuthAction
 * 변환 일시: 2025-10-20
 */
@Controller("checkUserInfoAuthController")
public class CheckUserInfoAuthController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 사용자 정보 인증 체크
	 * 
	 * @param authCode 인증 코드
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" or "fail"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "authCode", required = false) String authCode,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 인증 체크 수행
		boolean authResult = performAuthCheck(user, authCode);
		
		if (authResult) {
			model.addAttribute("result", "success");
			model.addAttribute("message", resource.getMessage("auth.check.success"));
		} else {
			model.addAttribute("result", "fail");
			model.addAttribute("message", resource.getMessage("auth.check.failed"));
		}
		
		return authResult ? "success" : "fail";
	}

	private boolean performAuthCheck(User user, String authCode) throws Exception {
		return settingManager.checkUserInfoAuth(Integer.parseInt(user.get(User.MAIL_USER_SEQ)), authCode);
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
