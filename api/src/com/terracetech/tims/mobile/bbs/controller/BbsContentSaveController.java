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

@Controller("mobileBbsContentSaveController")
public class BbsContentSaveController {
	@Autowired
	private BbsManager bbsManager;

	public String execute(
			@RequestParam(value = "bbsId", required = false) String bbsId,
			@RequestParam(value = "subject", required = false) String subject,
			@RequestParam(value = "content", required = false) String content,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		BoardContentVO contentVo = new BoardContentVO();
		contentVo.setBbsId(Integer.parseInt(bbsId));
		contentVo.setSubject(subject);
		contentVo.setContent(content);
		contentVo.setWriterSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		boolean result = bbsManager.saveBbsContent(contentVo);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
