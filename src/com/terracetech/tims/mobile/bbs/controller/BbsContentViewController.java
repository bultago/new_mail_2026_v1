package com.terracetech.tims.mobile.bbs.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;

@Controller("mobileBbsContentViewController")
public class BbsContentViewController {
	@Autowired
	private BbsManager bbsManager;

	public String execute(
			@RequestParam(value = "contentId", required = false) String contentId,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		BoardContentVO content = bbsManager.getBbsContent(Integer.parseInt(contentId));
		model.addAttribute("content", content);
		return "success";
	}
}
