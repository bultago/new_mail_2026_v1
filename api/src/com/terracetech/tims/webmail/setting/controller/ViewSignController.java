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
 * 서명 조회 Controller
 * 
 * 주요 기능:
 * 1. 사용자 서명 목록 조회
 * 2. 서명 설정 정보 제공
 * 
 * Struts2 Action: ViewSignAction
 * 변환 일시: 2025-10-20
 */
@Controller("viewSignController")
public class ViewSignController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 서명 조회
	 * 
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 사용자 서명 목록 조회
		List<SignVO> signList = getSignList(user);
		
		model.addAttribute("signList", signList);
		model.addAttribute("signCount", signList.size());
		
		return "success";
	}

	private List<SignVO> getSignList(User user) throws Exception {
		return settingManager.getSignList(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
