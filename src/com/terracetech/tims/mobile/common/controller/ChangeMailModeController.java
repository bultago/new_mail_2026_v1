package com.terracetech.tims.mobile.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;

@Controller("changeMailModeController")
public class ChangeMailModeController {
	public String execute(
			@RequestParam(value = "mode", required = false) String mode,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		request.getSession().setAttribute("mailMode", mode);
		model.addAttribute("result", "success");
		return "success";
	}
}
