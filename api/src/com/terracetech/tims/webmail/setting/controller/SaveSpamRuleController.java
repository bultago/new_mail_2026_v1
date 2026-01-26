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
import com.terracetech.tims.webmail.setting.vo.PSpamRuleVO;

/**
 * 스팸 규칙 저장 Controller
 * 
 * 주요 기능:
 * 1. 스팸 필터 규칙 저장
 * 2. 스팸 차단 설정
 * 
 * Struts2 Action: SaveSpamRuleAction
 * 변환 일시: 2025-10-20
 */
@Controller("saveSpamRuleController")
public class SaveSpamRuleController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 스팸 규칙 저장
	 * 
	 * @param spamRule 스팸 규칙
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "spamRule", required = false) String spamRule,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 스팸 규칙 저장
		boolean saveResult = saveSpamRule(user, spamRule);
		
		if (saveResult) {
			model.addAttribute("result", "success");
			model.addAttribute("message", resource.getMessage("spam.rule.save.success"));
		} else {
			model.addAttribute("result", "fail");
			model.addAttribute("message", resource.getMessage("spam.rule.save.failed"));
		}
		
		return "success";
	}

	private boolean saveSpamRule(User user, String spamRule) throws Exception {
		PSpamRuleVO spamRuleVo = new PSpamRuleVO();
		spamRuleVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		spamRuleVo.setSpamRule(spamRule);
		
		return settingManager.saveSpamRule(spamRuleVo);
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
