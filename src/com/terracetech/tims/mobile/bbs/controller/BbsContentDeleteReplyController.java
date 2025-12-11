package com.terracetech.tims.mobile.bbs.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;

@Controller("mobileBbsContentDeleteReplyController")
public class BbsContentDeleteReplyController {
	@Autowired
	private BbsManager bbsManager;

	public String execute(
			@RequestParam(value = "replyId", required = false) String replyId,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		boolean result = bbsManager.deleteBbsContentReply(Integer.parseInt(replyId));
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
