package com.terracetech.tims.webmail.organization.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.organization.manager.OrganizationManager;

@Controller("viewOrganizationListJsonController")
public class ViewOrganizationListJsonController {
	@Autowired
	private OrganizationManager organizationManager;

	public String execute(
			@RequestParam(value = "orgId", required = false) String orgId,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		String orgListJson = organizationManager.getOrganizationListJson(Integer.parseInt(orgId));
		model.addAttribute("orgListJson", orgListJson);
		return "success";
	}
}
