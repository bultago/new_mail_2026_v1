package com.terracetech.tims.webmail.setting.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.ExtMailVO;

@Controller("viewSelectedExtMailController")
public class ViewSelectedExtMailController {

	@Autowired
	private SettingManager settingManager;

	public String execute(
			@RequestParam(value = "extMailId", required = false) String extMailId,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		ExtMailVO extMail = settingManager.getExtMail(Integer.parseInt(extMailId));
		model.addAttribute("extMail", extMail);
		return "success";
	}
}
