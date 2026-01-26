package com.terracetech.tims.mobile.calendar.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;

@Controller("mobileDeleteCalendarController")
public class DeleteCalendarController {
	@Autowired
	private SchedulerManager schedulerManager;

	public String execute(
			@RequestParam(value = "scheduleId", required = false) String scheduleId,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		boolean result = schedulerManager.deleteSchedule(Integer.parseInt(scheduleId));
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
