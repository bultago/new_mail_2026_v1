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

@Controller("mobileBbsContentSaveReplyController")
public class BbsContentSaveReplyController {
	@Autowired
	private BbsManager bbsManager;

	public String execute(
			@RequestParam(value = "parentContentId", required = false) String parentContentId,
			@RequestParam(value = "replyContent", required = false) String replyContent,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		BoardContentVO replyVo = new BoardContentVO();
		replyVo.setParentContentId(Integer.parseInt(parentContentId));
		replyVo.setContent(replyContent);
		replyVo.setWriterSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		boolean result = bbsManager.saveBbsContentReply(replyVo);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
