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
 * 서명 데이터 수정 Controller
 * 
 * 주요 기능:
 * 1. 서명 데이터 수정
 * 2. 서명 설정 업데이트
 * 
 * Struts2 Action: ModifySignDataAction
 * 변환 일시: 2025-10-20
 */
@Controller("modifySignDataController")
public class ModifySignDataController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 서명 데이터 수정
	 * 
	 * @param signId 서명 ID
	 * @param signName 서명명
	 * @param signContent 서명 내용
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "signId", required = false) String signId,
			@RequestParam(value = "signName", required = false) String signName,
			@RequestParam(value = "signContent", required = false) String signContent,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 서명 데이터 수정
		boolean updateResult = updateSignData(user, signId, signName, signContent);
		
		if (updateResult) {
			model.addAttribute("result", "success");
			model.addAttribute("message", resource.getMessage("sign.data.modify.success"));
		} else {
			model.addAttribute("result", "fail");
			model.addAttribute("message", resource.getMessage("sign.data.modify.failed"));
		}
		
		return "success";
	}

	private boolean updateSignData(User user, String signId, String signName, String signContent) throws Exception {
		SignVO signVo = new SignVO();
		signVo.setSignId(Integer.parseInt(signId));
		signVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		signVo.setSignName(signName);
		signVo.setSignContent(signContent);
		
		return settingManager.updateSignData(signVo);
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
