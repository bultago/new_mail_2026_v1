package com.terracetech.tims.webmail.scheduler.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerVO;

/**
 * 진행률 스케줄러 Controller
 * 
 * 주요 기능:
 * 1. 진행률 스케줄 조회
 * 2. 주간 날짜 목록 조회
 * 3. 일일 스케줄 조회
 * 4. 오늘 날짜 정보 제공
 * 
 * Struts2 Action: ProgressSchedulerAction
 * 변환 일시: 2025-10-20
 */
@Controller("progressSchedulerController")
public class ProgressSchedulerController {

	@Autowired
	private SchedulerManager schedulerManager;

	/**
	 * 진행률 스케줄러 조회
	 * 
	 * @param year 연도
	 * @param month 월
	 * @param day 일
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "year", defaultValue = "0") int year,
			@RequestParam(value = "month", defaultValue = "0") int month,
			@RequestParam(value = "day", defaultValue = "0") int day,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "scheduler");
		
		// 날짜 정보 설정
		DateInfo dateInfo = getDateInfo(year, month, day);
		
		// 진행률 주간 날짜 목록 조회
		List weekDateList = getProgressDateList(dateInfo.getYear(), dateInfo.getMonth(), dateInfo.getDay());
		
		// 오늘 날짜 정보 설정
		SchedulerVO schedulerVo = getTodaySchedulerInfo();
		
		// Model에 데이터 추가
		model.addAttribute("weekDateList", weekDateList);
		model.addAttribute("schedulerVo", schedulerVo);
		model.addAttribute("year", dateInfo.getYear());
		model.addAttribute("month", dateInfo.getMonth());
		model.addAttribute("day", dateInfo.getDay());
		
		return "success";
	}

	/**
	 * 일일 스케줄 조회
	 * 
	 * @param year 연도
	 * @param month 월
	 * @param day 일
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String executeDay(
			@RequestParam(value = "year", defaultValue = "0") int year,
			@RequestParam(value = "month", defaultValue = "0") int month,
			@RequestParam(value = "day", defaultValue = "0") int day,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "scheduler");
		
		// 날짜 정보 설정
		DateInfo dateInfo = getDateInfo(year, month, day);
		
		// 일일 스케줄 조회
		SchedulerVO schedulerVo = getDayScheduler(dateInfo.getYear(), dateInfo.getMonth(), dateInfo.getDay());
		
		// Model에 데이터 추가
		model.addAttribute("schedulerVo", schedulerVo);
		model.addAttribute("year", dateInfo.getYear());
		model.addAttribute("month", dateInfo.getMonth());
		model.addAttribute("day", dateInfo.getDay());
		
		return "success";
	}

	/**
	 * 날짜 정보 설정
	 * 
	 * @param year 연도
	 * @param month 월
	 * @param day 일
	 * @return DateInfo
	 */
	private DateInfo getDateInfo(int year, int month, int day) {
		DateInfo dateInfo = new DateInfo();
		
		// 기본값 설정 (현재 날짜)
		java.util.Date now = new java.util.Date();
		if (year == 0) {
			year = now.getYear() + 1900;
		}
		if (month == 0) {
			month = now.getMonth() + 1;
		}
		if (day == 0) {
			day = now.getDate();
		}
		
		dateInfo.setYear(year);
		dateInfo.setMonth(month);
		dateInfo.setDay(day);
		
		return dateInfo;
	}

	/**
	 * 진행률 주간 날짜 목록 조회
	 * 
	 * @param year 연도
	 * @param month 월
	 * @param day 일
	 * @return 주간 날짜 목록
	 * @throws Exception
	 */
	private List getProgressDateList(int year, int month, int day) throws Exception {
		return schedulerManager.getProgressDateList(year, month, day);
	}

	/**
	 * 오늘 스케줄 정보 조회
	 * 
	 * @return SchedulerVO
	 * @throws Exception
	 */
	private SchedulerVO getTodaySchedulerInfo() throws Exception {
		SchedulerVO schedulerVo = new SchedulerVO();
		schedulerVo = schedulerManager.getTodayDate(schedulerVo);
		return schedulerVo;
	}

	/**
	 * 일일 스케줄 조회
	 * 
	 * @param year 연도
	 * @param month 월
	 * @param day 일
	 * @return SchedulerVO
	 * @throws Exception
	 */
	private SchedulerVO getDayScheduler(int year, int month, int day) throws Exception {
		SchedulerVO schedulerVo = schedulerManager.getDayScheduler(year, month, day);
		schedulerVo = schedulerManager.getTodayDate(schedulerVo);
		return schedulerVo;
	}

	/**
	 * I18n 리소스 조회
	 * 
	 * @param user User
	 * @param module 모듈명
	 * @return I18nResources
	 */
	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}

	/**
	 * 날짜 정보 VO
	 */
	private static class DateInfo {
		private int year;
		private int month;
		private int day;

		public int getYear() { return year; }
		public void setYear(int year) { this.year = year; }
		public int getMonth() { return month; }
		public void setMonth(int month) { this.month = month; }
		public int getDay() { return day; }
		public void setDay(int day) { this.day = day; }
	}
}
