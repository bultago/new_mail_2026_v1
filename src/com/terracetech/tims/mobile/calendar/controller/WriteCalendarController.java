package com.terracetech.tims.mobile.calendar.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;

@Controller("mobileWriteCalendarController")
public class WriteCalendarController {
	@Autowired
	private SchedulerManager schedulerManager;

	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		model.addAttribute("userId", user.get(User.MAIL_UID));
		return "success";
	}
}
