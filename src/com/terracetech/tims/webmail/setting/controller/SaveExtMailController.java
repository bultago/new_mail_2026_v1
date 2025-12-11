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
import com.terracetech.tims.webmail.setting.vo.ExtMailVO;

@Controller("saveExtMailController")
public class SaveExtMailController {

	@Autowired
	private SettingManager settingManager;

	public String execute(
			@RequestParam(value = "extMailAddr", required = false) String extMailAddr,
			@RequestParam(value = "extMailName", required = false) String extMailName,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		ExtMailVO extMailVo = new ExtMailVO();
		extMailVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		extMailVo.setExtMailAddr(extMailAddr);
		extMailVo.setExtMailName(extMailName);
		
		boolean result = settingManager.saveExtMail(extMailVo);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
