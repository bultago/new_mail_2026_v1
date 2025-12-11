package com.terracetech.tims.mobile.calendar.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;

@Controller("mobileWeekCalendarController")
public class WeekCalendarController {
	@Autowired
	private SchedulerManager schedulerManager;

	public String execute(
			@RequestParam(value = "startDate", required = false) String startDate,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		List<SchedulerDataVO> scheduleList = schedulerManager.getWeekSchedule(Integer.parseInt(user.get(User.MAIL_USER_SEQ)), startDate);
		model.addAttribute("scheduleList", scheduleList);
		return "success";
	}
}
