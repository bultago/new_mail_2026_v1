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
import com.terracetech.tims.webmail.setting.vo.FilterVO;

/**
 * 필터 조회 Controller
 * 
 * 주요 기능:
 * 1. 사용자 필터 목록 조회
 * 2. 필터 설정 정보 제공
 * 
 * Struts2 Action: ViewFilterAction
 * 변환 일시: 2025-10-20
 */
@Controller("viewFilterController")
public class ViewFilterController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 필터 조회
	 * 
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 사용자 필터 목록 조회
		List<FilterVO> filterList = getFilterList(user);
		
		model.addAttribute("filterList", filterList);
		model.addAttribute("filterCount", filterList.size());
		
		return "success";
	}

	private List<FilterVO> getFilterList(User user) throws Exception {
		return settingManager.getFilterList(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
