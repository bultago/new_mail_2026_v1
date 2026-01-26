package com.terracetech.tims.webmail.setting.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.ForwardingInfoVO;

@Controller("saveForwardController")
public class SaveForwardController {

	@Autowired
	private SettingManager settingManager;

	public String execute(
			@RequestParam(value = "forwardAddr", required = false) String forwardAddr,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		ForwardVO forwardVo = new ForwardVO();
		forwardVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		forwardVo.setForwardAddr(forwardAddr);
		
		boolean result = settingManager.saveForward(forwardVo);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
