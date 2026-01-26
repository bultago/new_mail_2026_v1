package com.terracetech.tims.webmail.organization.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.organization.manager.OrganizationManager;

@Controller("organizationCommonController")
public class OrganizationCommonController {
	@Autowired
	private OrganizationManager organizationManager;

	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		model.addAttribute("userId", user.get(User.MAIL_UID));
		return "success";
	}
}
