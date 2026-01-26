package com.terracetech.tims.webmail.note.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.vo.MailUserVO;

@Controller("searchUserListController")
public class SearchUserListController {
	@Autowired
	private MailUserManager mailUserManager;

	public String execute(
			@RequestParam(value = "searchKeyword", required = false) String searchKeyword,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		List<MailUserVO> userList = mailUserManager.searchUser(searchKeyword);
		model.addAttribute("userList", userList);
		return "success";
	}
}
