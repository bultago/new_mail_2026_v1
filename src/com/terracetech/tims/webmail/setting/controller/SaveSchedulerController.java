package com.terracetech.tims.webmail.setting.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.SchedulerVO;

@Controller("saveSchedulerController")
public class SaveSchedulerController {

	@Autowired
	private SettingManager settingManager;

	public String execute(
			@RequestParam(value = "schedulerData", required = false) String schedulerData,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		SchedulerVO schedulerVo = new SchedulerVO();
		schedulerVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		schedulerVo.setSchedulerData(schedulerData);
		
		boolean result = settingManager.saveScheduler(schedulerVo);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
