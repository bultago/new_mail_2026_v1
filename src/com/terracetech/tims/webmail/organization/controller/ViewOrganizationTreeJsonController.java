package com.terracetech.tims.webmail.organization.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.organization.manager.OrganizationManager;

@Controller("viewOrganizationTreeJsonController")
public class ViewOrganizationTreeJsonController {
	@Autowired
	private OrganizationManager organizationManager;

	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		String orgTreeJson = organizationManager.getOrganizationTreeJson(Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)));
		model.addAttribute("orgTreeJson", orgTreeJson);
		return "success";
	}
}
