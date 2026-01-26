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
 * 서명 데이터 삭제 Controller
 * 
 * 주요 기능:
 * 1. 서명 데이터 삭제
 * 2. 서명 설정 제거
 * 
 * Struts2 Action: DeleteSignDataAction
 * 변환 일시: 2025-10-20
 */
@Controller("deleteSignDataController")
public class DeleteSignDataController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 서명 데이터 삭제
	 * 
	 * @param signId 서명 ID
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "signId", required = false) String signId,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 서명 데이터 삭제
		boolean deleteResult = deleteSignData(user, signId);
		
		if (deleteResult) {
			model.addAttribute("result", "success");
			model.addAttribute("message", resource.getMessage("sign.data.delete.success"));
		} else {
			model.addAttribute("result", "fail");
			model.addAttribute("message", resource.getMessage("sign.data.delete.failed"));
		}
		
		return "success";
	}

	private boolean deleteSignData(User user, String signId) throws Exception {
		return settingManager.deleteSignData(Integer.parseInt(signId), Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
