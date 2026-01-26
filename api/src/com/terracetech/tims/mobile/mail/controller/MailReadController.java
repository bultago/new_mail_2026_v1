package com.terracetech.tims.mobile.mail.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.vo.MailVO;
import com.terracetech.tims.webmail.mailuser.User;

@Controller("mobileMailReadController")
public class MailReadController {
	@Autowired
	private MailManager mailManager;

	public String execute(
			@RequestParam(value = "mailId", required = false) String mailId,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		MailVO mail = mailManager.getMail(user.get(User.MAIL_UID), mailId);
		model.addAttribute("mail", mail);
		return "success";
	}
}
