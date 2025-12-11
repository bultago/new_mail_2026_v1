package com.terracetech.tims.webmail.scheduler.controller;

import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.FormatUtil;

/**
 * 스케줄러 공통 기능 Controller
 * 
 * 주요 기능:
 * 1. 스케줄러 페이지 로드
 * 2. 사용자 정보 조회
 * 3. 기본 설정 정보 제공
 * 4. 날짜/시간 정보 제공
 * 
 * Struts2 Action: SchedulerCommonAction
 * 변환 일시: 2025-10-20
 */
@Controller("schedulerCommonController")
public class SchedulerCommonController {

	@Autowired
	private MailUserManager mailUserManager;

	/**
	 * 스케줄러 페이지 로드
	 * 
	 * @param type 스케줄러 타입
	 * @param calendarYear 달력 연도
	 * @param calendarMonth 달력 월
	 * @param calendarDay 달력 일
	 * @param scheduleId 스케줄 ID
	 * @param calendarStartDate 시작 날짜
	 * @param calendarEndDate 종료 날짜
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "load"
	 * @throws Exception
	 */
	public String loadPage(
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "calendarYear", defaultValue = "0") int calendarYear,
			@RequestParam(value = "calendarMonth", defaultValue = "0") int calendarMonth,
			@RequestParam(value = "calendarDay", defaultValue = "0") int calendarDay,
			@RequestParam(value = "scheduleId", defaultValue = "0") int scheduleId,
			@RequestParam(value = "calendarStartDate", required = false) String calendarStartDate,
			@RequestParam(value = "calendarEndDate", required = false) String calendarEndDate,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "scheduler");
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		// 기본 설정 정보 조회
		SchedulerInfo schedulerInfo = getSchedulerInfo(user, mailUserSeq);
		
		// 달력 정보 설정
		CalendarInfo calendarInfo = getCalendarInfo(calendarYear, calendarMonth, calendarDay);
		
		// Model에 데이터 추가
		model.addAttribute("type", type);
		model.addAttribute("calendarYear", calendarInfo.getYear());
		model.addAttribute("calendarMonth", calendarInfo.getMonth());
		model.addAttribute("calendarDay", calendarInfo.getDay());
		model.addAttribute("scheduleId", scheduleId);
		model.addAttribute("calendarStartDate", calendarStartDate);
		model.addAttribute("calendarEndDate", calendarEndDate);
		model.addAttribute("installLocale", schedulerInfo.getInstallLocale());
		model.addAttribute("userName", schedulerInfo.getUserName());
		model.addAttribute("mobileNo", schedulerInfo.getMobileNo());
		model.addAttribute("currentTime", schedulerInfo.getCurrentTime());
		
		return "load";
	}

	/**
	 * 스케줄러 정보 조회
	 * 
	 * @param user 사용자 정보
	 * @param mailUserSeq 사용자 시퀀스
	 * @return SchedulerInfo
	 * @throws Exception
	 */
	private SchedulerInfo getSchedulerInfo(User user, int mailUserSeq) throws Exception {
		SchedulerInfo info = new SchedulerInfo();
		
		info.setInstallLocale(EnvConstants.getBasicSetting("setup.state"));
		info.setUserName(user.get(User.USER_NAME));
		info.setMobileNo(mailUserManager.readUserInfoMobileNo(mailUserSeq));
		info.setCurrentTime(FormatUtil.getBasicDateStr());
		
		return info;
	}

	/**
	 * 달력 정보 설정
	 * 
	 * @param calendarYear 연도
	 * @param calendarMonth 월
	 * @param calendarDay 일
	 * @return CalendarInfo
	 */
	private CalendarInfo getCalendarInfo(int calendarYear, int calendarMonth, int calendarDay) {
		CalendarInfo info = new CalendarInfo();
		
		// 기본값 설정 (현재 날짜)
		Date now = new Date();
		if (calendarYear == 0) {
			calendarYear = now.getYear() + 1900;
		}
		if (calendarMonth == 0) {
			calendarMonth = now.getMonth() + 1;
		}
		if (calendarDay == 0) {
			calendarDay = now.getDate();
		}
		
		info.setYear(calendarYear);
		info.setMonth(calendarMonth);
		info.setDay(calendarDay);
		
		return info;
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
	 * 스케줄러 정보 VO
	 */
	private static class SchedulerInfo {
		private String installLocale;
		private String userName;
		private String mobileNo;
		private String currentTime;

		public String getInstallLocale() { return installLocale; }
		public void setInstallLocale(String installLocale) { this.installLocale = installLocale; }
		public String getUserName() { return userName; }
		public void setUserName(String userName) { this.userName = userName; }
		public String getMobileNo() { return mobileNo; }
		public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }
		public String getCurrentTime() { return currentTime; }
		public void setCurrentTime(String currentTime) { this.currentTime = currentTime; }
	}

	/**
	 * 달력 정보 VO
	 */
	private static class CalendarInfo {
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
