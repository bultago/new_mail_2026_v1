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

@Controller("mobileMailSendController")
public class MailSendController {
	@Autowired
	private MailManager mailManager;

	public String execute(
			@RequestParam(value = "to", required = false) String to,
			@RequestParam(value = "subject", required = false) String subject,
			@RequestParam(value = "content", required = false) String content,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		MailVO mailVo = new MailVO();
		mailVo.setFrom(user.get(User.MAIL_UID));
		mailVo.setTo(to);
		mailVo.setSubject(subject);
		mailVo.setContent(content);
		boolean result = mailManager.sendMail(mailVo);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
