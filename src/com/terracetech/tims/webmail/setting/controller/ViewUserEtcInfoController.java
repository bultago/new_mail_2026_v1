package com.terracetech.tims.webmail.setting.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserInfoVO;

/**
 * 사용자 기타 정보 조회 Controller
 * 
 * 주요 기능:
 * 1. 사용자 기타 정보 조회
 * 2. 추가 설정 정보 제공
 * 
 * Struts2 Action: ViewUserEtcInfoAction
 * 변환 일시: 2025-10-20
 */
@Controller("viewUserEtcInfoController")
public class ViewUserEtcInfoController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 사용자 기타 정보 조회
	 * 
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 사용자 기타 정보 조회
		UserInfoVO userEtcInfo = getUserEtcInfo(user);
		
		model.addAttribute("userEtcInfo", userEtcInfo);
		
		return "success";
	}

	private UserInfoVO getUserEtcInfo(User user) throws Exception {
		return settingManager.getUserEtcInfo(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
