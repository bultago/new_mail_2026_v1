package com.terracetech.tims.mobile.calendar.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;

@Controller("mobileSaveCalendarController")
public class SaveCalendarController {
	@Autowired
	private SchedulerManager schedulerManager;

	public String execute(
			@RequestParam(value = "subject", required = false) String subject,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "content", required = false) String content,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		SchedulerDataVO scheduleVo = new SchedulerDataVO();
		scheduleVo.setSubject(subject);
		scheduleVo.setStartDate(startDate);
		scheduleVo.setEndDate(endDate);
		scheduleVo.setContent(content);
		scheduleVo.setCreatorSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		boolean result = schedulerManager.saveSchedule(scheduleVo);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
