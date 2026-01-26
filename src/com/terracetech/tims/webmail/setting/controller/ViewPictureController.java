package com.terracetech.tims.webmail.setting.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.PictureVO;

@Controller("viewPictureController")
public class ViewPictureController {

	@Autowired
	private SettingManager settingManager;

	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		PictureVO picture = settingManager.getPicture(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		model.addAttribute("picture", picture);
		return "success";
	}
}
