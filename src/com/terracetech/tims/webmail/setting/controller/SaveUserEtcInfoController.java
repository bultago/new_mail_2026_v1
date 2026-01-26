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
 * 사용자 기타 정보 저장 Controller
 * 
 * 주요 기능:
 * 1. 사용자 기타 정보 저장
 * 2. 설정 정보 영구 저장
 * 
 * Struts2 Action: SaveUserEtcInfoAction
 * 변환 일시: 2025-10-20
 */
@Controller("saveUserEtcInfoController")
public class SaveUserEtcInfoController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 사용자 기타 정보 저장
	 * 
	 * @param userEtcInfo 기타 정보 JSON
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "userEtcInfo", required = false) String userEtcInfo,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 사용자 기타 정보 저장
		boolean saveResult = saveUserEtcInfo(user, userEtcInfo);
		
		if (saveResult) {
			model.addAttribute("result", "success");
			model.addAttribute("message", resource.getMessage("user.etc.info.save.success"));
		} else {
			model.addAttribute("result", "fail");
			model.addAttribute("message", resource.getMessage("user.etc.info.save.failed"));
		}
		
		return "success";
	}

	private boolean saveUserEtcInfo(User user, String userEtcInfo) throws Exception {
		UserInfoVO userInfoVo = new UserInfoVO();
		userInfoVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		userInfoVo.setUserEtcInfo(userEtcInfo);
		
		return settingManager.saveUserEtcInfo(userInfoVo);
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
