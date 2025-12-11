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
import com.terracetech.tims.webmail.setting.vo.SignVO;

/**
 * 서명 데이터 저장 Controller
 * 
 * 주요 기능:
 * 1. 서명 데이터 저장
 * 2. 서명 설정 영구 저장
 * 
 * Struts2 Action: SaveSignDataAction
 * 변환 일시: 2025-10-20
 */
@Controller("saveSignDataController")
public class SaveSignDataController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 서명 데이터 저장
	 * 
	 * @param signName 서명명
	 * @param signContent 서명 내용
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "signName", required = false) String signName,
			@RequestParam(value = "signContent", required = false) String signContent,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 서명 데이터 저장
		boolean saveResult = saveSignData(user, signName, signContent);
		
		if (saveResult) {
			model.addAttribute("result", "success");
			model.addAttribute("message", resource.getMessage("sign.data.save.success"));
		} else {
			model.addAttribute("result", "fail");
			model.addAttribute("message", resource.getMessage("sign.data.save.failed"));
		}
		
		return "success";
	}

	private boolean saveSignData(User user, String signName, String signContent) throws Exception {
		SignVO signVo = new SignVO();
		signVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		signVo.setSignName(signName);
		signVo.setSignContent(signContent);
		
		return settingManager.saveSignData(signVo);
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
