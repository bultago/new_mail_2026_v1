package com.terracetech.tims.webmail.setting.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.ForwardingInfoVO;

@Controller("viewForwardController")
public class ViewForwardController {

	@Autowired
	private SettingManager settingManager;

	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		ForwardVO forward = settingManager.getForward(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		model.addAttribute("forward", forward);
		return "success";
	}
}
