package com.terracetech.tims.webmail.setting.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.PSpamRuleVO;

/**
 * 스팸 규칙 조회 Controller
 * 
 * 주요 기능:
 * 1. 스팸 필터 규칙 조회
 * 2. 스팸 차단 설정 조회
 * 
 * Struts2 Action: ViewSpamRuleAction
 * 변환 일시: 2025-10-20
 */
@Controller("viewSpamRuleController")
public class ViewSpamRuleController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 스팸 규칙 조회
	 * 
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 스팸 규칙 조회
		PSpamRuleVO spamRule = getSpamRule(user);
		
		model.addAttribute("spamRule", spamRule);
		
		return "success";
	}

	private PSpamRuleVO getSpamRule(User user) throws Exception {
		return settingManager.getSpamRule(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
