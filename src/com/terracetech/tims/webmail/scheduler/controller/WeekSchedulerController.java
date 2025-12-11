package com.terracetech.tims.webmail.scheduler.controller;

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
 * 주간 스케줄러 Controller
 * 
 * 주요 기능:
 * 1. 주간 스케줄 조회
 * 2. 오늘 날짜 정보 제공
 * 3. 주간 첫날/마지막날 음력 계산
 * 4. 주간 스케줄 데이터 처리
 * 
 * Struts2 Action: WeekSchedulerAction
 * 변환 일시: 2025-10-20
 */
@Controller("weekSchedulerController")
public class WeekSchedulerController {

	@Autowired
	private SchedulerManager schedulerManager;

	/**
	 * 주간 스케줄러 조회
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
		
		// 주간 스케줄 정보 조회
		SchedulerVO schedulerVo = getWeekScheduler(dateInfo.getYear(), dateInfo.getMonth(), dateInfo.getDay());
		
		// 음력 정보 계산
		LunarInfo lunarInfo = calculateWeekLunar(schedulerVo);
		
		// Model에 데이터 추가
		model.addAttribute("schedulerVo", schedulerVo);
		model.addAttribute("firstLunar", lunarInfo.getFirstLunar());
		model.addAttribute("lastLunar", lunarInfo.getLastLunar());
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
	 * 주간 스케줄 조회
	 * 
	 * @param year 연도
	 * @param month 월
	 * @param day 일
	 * @return SchedulerVO
	 * @throws Exception
	 */
	private SchedulerVO getWeekScheduler(int year, int month, int day) throws Exception {
		// 주간 스케줄 조회
		SchedulerVO schedulerVo = schedulerManager.getWeekScheduler(year, month, day);
		
		// 오늘 날짜 정보 설정
		schedulerVo = schedulerManager.getTodayDate(schedulerVo);
		
		return schedulerVo;
	}

	/**
	 * 주간 음력 정보 계산
	 * 
	 * @param schedulerVo 스케줄 VO
	 * @return LunarInfo
	 * @throws Exception
	 */
	private LunarInfo calculateWeekLunar(SchedulerVO schedulerVo) throws Exception {
		LunarInfo lunarInfo = new LunarInfo();
		
		// 주간 첫날 음력 계산
		int firstLunar = schedulerManager.getLunar(
			schedulerVo.getFirstYear(), 
			schedulerVo.getFirstMonth(),
			schedulerVo.getFirstDay()
		);
		
		// 주간 마지막날 음력 계산
		int lastLunar = schedulerManager.getLunar(
			schedulerVo.getLastYear(), 
			schedulerVo.getLastMonth(),
			schedulerVo.getLastDay()
		);
		
		lunarInfo.setFirstLunar(firstLunar);
		lunarInfo.setLastLunar(lastLunar);
		
		return lunarInfo;
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

	/**
	 * 음력 정보 VO
	 */
	private static class LunarInfo {
		private int firstLunar;
		private int lastLunar;

		public int getFirstLunar() { return firstLunar; }
		public void setFirstLunar(int firstLunar) { this.firstLunar = firstLunar; }
		public int getLastLunar() { return lastLunar; }
		public void setLastLunar(int lastLunar) { this.lastLunar = lastLunar; }
	}
}
