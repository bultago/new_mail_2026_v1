package com.terracetech.tims.mobile.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;

@Controller("mobileLoginController")
public class LoginController {
	@Autowired
	private UserAuthManager userAuthManager;

	public String execute(
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "password", required = false) String password,
			HttpServletRequest request,
			Model model) throws Exception {
		boolean loginResult = userAuthManager.authenticate(userId, password);
		model.addAttribute("result", loginResult ? "success" : "fail");
		return "success";
	}
}
