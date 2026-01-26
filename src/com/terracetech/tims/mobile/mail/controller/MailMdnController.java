package com.terracetech.tims.mobile.mail.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mailuser.User;

@Controller("mobileMailMdnController")
public class MailMdnController {
	@Autowired
	private MailManager mailManager;

	public String execute(
			@RequestParam(value = "mailId", required = false) String mailId,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		boolean result = mailManager.sendMdn(user.get(User.MAIL_UID), mailId);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
