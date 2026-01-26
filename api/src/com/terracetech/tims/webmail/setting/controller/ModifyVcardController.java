package com.terracetech.tims.webmail.setting.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.VCardVO;

@Controller("modifyVcardController")
public class ModifyVcardController {

	@Autowired
	private SettingManager settingManager;

	public String execute(
			@RequestParam(value = "vcardData", required = false) String vcardData,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		VcardVO vcardVo = new VcardVO();
		vcardVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		vcardVo.setVcardData(vcardData);
		
		boolean result = settingManager.updateVcard(vcardVo);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
