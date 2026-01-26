package com.terracetech.tims.webmail.setting.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;

@Controller("deleteLastrcptController")
public class DeleteLastrcptController {

	@Autowired
	private SettingManager settingManager;

	public String execute(
			@RequestParam(value = "lastrcptId", required = false) String lastrcptId,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		boolean result = settingManager.deleteLastrcpt(Integer.parseInt(lastrcptId), Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
