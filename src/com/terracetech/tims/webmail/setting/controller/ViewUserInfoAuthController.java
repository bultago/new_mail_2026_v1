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
 * 사용자 정보 인증 조회 Controller
 * 
 * 주요 기능:
 * 1. 사용자 정보 인증 조회
 * 2. 인증 상태 확인
 * 
 * Struts2 Action: ViewUserInfoAuthAction
 * 변환 일시: 2025-10-20
 */
@Controller("viewUserInfoAuthController")
public class ViewUserInfoAuthController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 사용자 정보 인증 조회
	 * 
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 사용자 인증 정보 조회
		boolean authStatus = checkUserAuthStatus(user);
		
		model.addAttribute("authStatus", authStatus);
		model.addAttribute("userId", user.get(User.MAIL_UID));
		
		return "success";
	}

	private boolean checkUserAuthStatus(User user) throws Exception {
		return settingManager.checkUserAuthStatus(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
