package com.terracetech.tims.webmail.setting.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.AutoReplyVO;

@Controller("saveAutoReplyController")
public class SaveAutoReplyController {

	@Autowired
	private SettingManager settingManager;

	public String execute(
			@RequestParam(value = "autoReplyContent", required = false) String autoReplyContent,
			@RequestParam(value = "autoReplyEnabled", required = false) String autoReplyEnabled,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		AutoReplyVO autoReplyVo = new AutoReplyVO();
		autoReplyVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		autoReplyVo.setAutoReplyContent(autoReplyContent);
		autoReplyVo.setAutoReplyEnabled("true".equals(autoReplyEnabled));
		
		boolean result = settingManager.saveAutoReply(autoReplyVo);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
