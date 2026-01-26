package com.terracetech.tims.mobile.bbs.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;

@Controller("mobileBbsContentListController")
public class BbsContentListController {
	@Autowired
	private BbsManager bbsManager;

	public String execute(
			@RequestParam(value = "bbsId", required = false) String bbsId,
			@RequestParam(value = "page", defaultValue = "1") int page,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		List<BoardContentVO> contentList = bbsManager.getBbsContentList(Integer.parseInt(bbsId), page);
		model.addAttribute("contentList", contentList);
		return "success";
	}
}
