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
import com.terracetech.tims.webmail.setting.vo.UserInfoVO;

/**
 * 사용자 정보 업데이트 Controller
 * 
 * 주요 기능:
 * 1. 사용자 정보 업데이트
 * 2. 전체 정보 갱신
 * 
 * Struts2 Action: UpdateUserInfoAction
 * 변환 일시: 2025-10-20
 */
@Controller("updateUserInfoController")
public class UpdateUserInfoController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 사용자 정보 업데이트
	 * 
	 * @param userInfo 사용자 정보 JSON
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "userInfo", required = false) String userInfo,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 사용자 정보 업데이트
		boolean updateResult = updateUserInfo(user, userInfo);
		
		if (updateResult) {
			model.addAttribute("result", "success");
			model.addAttribute("message", resource.getMessage("user.info.update.success"));
		} else {
			model.addAttribute("result", "fail");
			model.addAttribute("message", resource.getMessage("user.info.update.failed"));
		}
		
		return "success";
	}

	private boolean updateUserInfo(User user, String userInfo) throws Exception {
		UserInfoVO userInfoVo = new UserInfoVO();
		userInfoVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		userInfoVo.setUserEtcInfo(userInfo);
		
		return settingManager.updateUserInfo(userInfoVo);
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
