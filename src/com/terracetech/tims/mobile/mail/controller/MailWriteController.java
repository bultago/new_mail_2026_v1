package com.terracetech.tims.mobile.mail.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;

@Controller("mobileMailWriteController")
public class MailWriteController {
	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		model.addAttribute("userId", user.get(User.MAIL_UID));
		return "success";
	}
}