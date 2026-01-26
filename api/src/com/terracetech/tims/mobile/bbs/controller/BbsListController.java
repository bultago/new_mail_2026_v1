package com.terracetech.tims.mobile.bbs.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;

@Controller("mobileBbsListController")
public class BbsListController {
	@Autowired
	private BbsManager bbsManager;

	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		List<BoardVO> bbsList = bbsManager.getBbsList(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		model.addAttribute("bbsList", bbsList);
		return "success";
	}
}
