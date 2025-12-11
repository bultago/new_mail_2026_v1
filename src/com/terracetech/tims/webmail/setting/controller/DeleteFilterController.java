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
 * 필터 삭제 Controller
 * 
 * 주요 기능:
 * 1. 필터 규칙 삭제
 * 2. 필터 설정 제거
 * 
 * Struts2 Action: DeleteFilterAction
 * 변환 일시: 2025-10-20
 */
@Controller("deleteFilterController")
public class DeleteFilterController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 필터 삭제
	 * 
	 * @param filterId 필터 ID
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "filterId", required = false) String filterId,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 필터 삭제
		boolean deleteResult = deleteFilter(user, filterId);
		
		if (deleteResult) {
			model.addAttribute("result", "success");
			model.addAttribute("message", resource.getMessage("filter.delete.success"));
		} else {
			model.addAttribute("result", "fail");
			model.addAttribute("message", resource.getMessage("filter.delete.failed"));
		}
		
		return "success";
	}

	private boolean deleteFilter(User user, String filterId) throws Exception {
		return settingManager.deleteFilter(Integer.parseInt(filterId), Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
