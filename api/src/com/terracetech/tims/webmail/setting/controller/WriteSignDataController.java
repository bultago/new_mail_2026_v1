package com.terracetech.tims.webmail.setting.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.SignVO;

/**
 * 서명 데이터 작성 Controller
 * 
 * 주요 기능:
 * 1. 서명 데이터 작성 폼 제공
 * 2. 서명 작성 준비
 * 
 * Struts2 Action: WriteSignDataAction
 * 변환 일시: 2025-10-20
 */
@Controller("writeSignDataController")
public class WriteSignDataController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 서명 데이터 작성
	 * 
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 서명 작성 폼 데이터 준비
		prepareSignForm(user, model);
		
		return "success";
	}

	private void prepareSignForm(User user, Model model) throws Exception {
		// 기본 서명 정보 조회
		List<SignVO> existingSigns = settingManager.getSignList(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		
		model.addAttribute("existingSigns", existingSigns);
		model.addAttribute("maxSignCount", 5); // 최대 서명 개수
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}