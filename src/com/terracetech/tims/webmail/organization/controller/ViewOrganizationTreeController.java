package com.terracetech.tims.webmail.organization.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.organization.manager.OrganizationManager;
import com.terracetech.tims.webmail.organization.vo.OrganizationVO;

@Controller("viewOrganizationTreeController")
public class ViewOrganizationTreeController {
	@Autowired
	private OrganizationManager organizationManager;

	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		List<OrganizationVO> orgTree = organizationManager.getOrganizationTree(Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)));
		model.addAttribute("orgTree", orgTree);
		return "success";
	}
}
