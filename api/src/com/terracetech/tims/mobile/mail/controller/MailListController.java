package com.terracetech.tims.mobile.mail.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.vo.MailVO;
import com.terracetech.tims.webmail.mailuser.User;

@Controller("mobileMailListController")
public class MailListController {
	@Autowired
	private MailManager mailManager;

	public String execute(
			@RequestParam(value = "folderName", defaultValue = "INBOX") String folderName,
			@RequestParam(value = "page", defaultValue = "1") int page,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		List<MailVO> mailList = mailManager.getMailList(user.get(User.MAIL_UID), folderName, page);
		model.addAttribute("mailList", mailList);
		return "success";
	}
}
