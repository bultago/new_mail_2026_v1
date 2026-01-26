package com.terracetech.tims.webmail.organization.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.vo.MailUserVO;
import com.terracetech.tims.webmail.organization.manager.OrganizationManager;

@Controller("viewOrganizationMemberController")
public class ViewOrganizationMemberController {
	@Autowired
	private OrganizationManager organizationManager;

	public String execute(
			@RequestParam(value = "orgId", required = false) String orgId,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		List<MailUserVO> memberList = organizationManager.getOrganizationMembers(Integer.parseInt(orgId));
		model.addAttribute("memberList", memberList);
		return "success";
	}
}
