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

@Controller("mobileMonthCalendarController")
public class MonthCalendarController {
	@Autowired
	private SchedulerManager schedulerManager;

	public String execute(
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "month", required = false) String month,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		List<SchedulerDataVO> scheduleList = schedulerManager.getMonthSchedule(Integer.parseInt(user.get(User.MAIL_USER_SEQ)), year, month);
		model.addAttribute("scheduleList", scheduleList);
		return "success";
	}
}
