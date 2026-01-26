package com.terracetech.tims.service.tms.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.service.tms.ICalendarService;
import com.terracetech.tims.service.tms.vo.CalendarAssetCondVO;
import com.terracetech.tims.service.tms.vo.CalendarAssetVO;
import com.terracetech.tims.service.tms.vo.CalendarCondVO;
import com.terracetech.tims.service.tms.vo.CalendarDeleteCondVO;
import com.terracetech.tims.service.tms.vo.CalendarHolidayVO;
import com.terracetech.tims.service.tms.vo.CalendarInfoVO;
import com.terracetech.tims.service.tms.vo.CalendarSaveCondVO;
import com.terracetech.tims.service.tms.vo.CalendarShareVO;
import com.terracetech.tims.service.tms.vo.CalendarVO;
import com.terracetech.tims.service.tms.vo.CalendarWriteVO;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerAssetCategoryVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerAssetVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerHolidayVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerVO;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class CalendarService implements ICalendarService {

	private SchedulerManager schedulerManager = null;

	public void setSchedulerManager(SchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
	}
	
	public CalendarVO getMonthCalendar(CalendarCondVO calendarCondVo) {
		return getMonthCalendar(calendarCondVo, null);
	}
	
	public CalendarVO getMonthCalendar(CalendarCondVO calendarCondVo, User user) {
		
		String date = calendarCondVo.getDate();
		String current = FormatUtil.getBasicDateStr();
		
		int year = 0;
		int month = 0;
		int day = 0;
		if (StringUtils.isEmpty(date)) {
			year = Integer.parseInt(current.substring(0, 4));
			month = Integer.parseInt(current.substring(4, 6));
			day = Integer.parseInt(current.substring(6, 8));
		} else {
			year = Integer.parseInt(date.substring(0, 4));
			month = Integer.parseInt(date.substring(4, 6));
			day = Integer.parseInt(date.substring(6, 8));
			if (day == 0) {
				day = 1;
			}
		}
		
		SchedulerVO monthInfo = schedulerManager.getMonthScheduler(year, month);
		List<String> dateList = monthInfo.getMonthDayList();
		String[] dateArray = new String[dateList.size()];
		dateList.toArray(dateArray);
		
		CalendarVO calendarVo = new CalendarVO();
		calendarVo.setType("month");
		calendarVo.setToday(current.substring(0, 4)+""+current.substring(4, 6)+""+current.substring(6, 8));
		calendarVo.setThisday(makeStringDate(year, month, day));
		calendarVo.setPreday(makeStringDate(monthInfo.getPrevYear(), monthInfo.getPrevMonth(), monthInfo.getPrevDay()));
		calendarVo.setNextday(makeStringDate(monthInfo.getNextYear(), monthInfo.getNextMonth(), monthInfo.getNextDay()));
		calendarVo.setDateList(dateArray);
		
		String email = calendarCondVo.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		int mailUserSeq = Integer.parseInt(authUser.get(User.MAIL_USER_SEQ));
		
		List<SchedulerDataVO> schedulerDataList = schedulerManager.getMonthScheduleList(year, month, mailDomainSeq, mailUserSeq, email);
		
		if (schedulerDataList != null && schedulerDataList.size() > 0) {
			CalendarInfoVO[] calendarInfoVOs = new CalendarInfoVO[schedulerDataList.size()];
			for (int i=0; i <schedulerDataList.size(); i++) {
				calendarInfoVOs[i] = convertInfoVo(schedulerDataList.get(i));
			}
			calendarVo.setCalendarInfos(calendarInfoVOs);
		}
		
		return calendarVo;
	}
	
	public CalendarVO getWeekCalendar(CalendarCondVO calendarCondVo) {
		return getWeekCalendar(calendarCondVo, null);
	}
	
	@SuppressWarnings("unchecked")
	public CalendarVO getWeekCalendar(CalendarCondVO calendarCondVo, User user) {
		
		String date = calendarCondVo.getDate();
		String current = FormatUtil.getBasicDateStr();
		
		int year = 0;
		int month = 0;
		int day = 0;
		if (StringUtils.isEmpty(date)) {
			year = Integer.parseInt(current.substring(0, 4));
			month = Integer.parseInt(current.substring(4, 6));
			day = Integer.parseInt(current.substring(6, 8));
		} else {
			year = Integer.parseInt(date.substring(0, 4));
			month = Integer.parseInt(date.substring(4, 6));
			day = Integer.parseInt(date.substring(6, 8));
		}
		
		SchedulerVO weekInfo = schedulerManager.getWeekScheduler(year, month, day);
		List<String> dateList = weekInfo.getWeekDayList();
		String[] dateArray = new String[dateList.size()];
		dateList.toArray(dateArray);
		
		CalendarVO calendarVo = new CalendarVO();
		calendarVo.setType("week");
		calendarVo.setToday(current.substring(0, 4)+""+current.substring(4, 6)+""+current.substring(6, 8));
		calendarVo.setThisday(makeStringDate(year, month, day));
		calendarVo.setPreday(makeStringDate(weekInfo.getPrevYear(), weekInfo.getPrevMonth(), weekInfo.getPrevDay()));
		calendarVo.setNextday(makeStringDate(weekInfo.getNextYear(), weekInfo.getNextMonth(), weekInfo.getNextDay()));
		calendarVo.setDateList(dateArray);
		
		String email = calendarCondVo.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		int mailUserSeq = Integer.parseInt(authUser.get(User.MAIL_USER_SEQ));
		
		List<SchedulerDataVO> schedulerDataList = schedulerManager.getWeekScheduleList(year, month, day, mailDomainSeq, mailUserSeq, email);
		
		if (schedulerDataList != null && schedulerDataList.size() > 0) {
			CalendarInfoVO[] calendarInfoVOs = new CalendarInfoVO[schedulerDataList.size()];
			for (int i=0; i <schedulerDataList.size(); i++) {
				calendarInfoVOs[i] = convertInfoVo(schedulerDataList.get(i));
			}
			calendarVo.setCalendarInfos(calendarInfoVOs);
		}
		
		return calendarVo;
	}

	public CalendarInfoVO getSchedule(CalendarCondVO calendarCondVo) {
		return getSchedule(calendarCondVo, null);
	}
			
	public CalendarInfoVO getSchedule(CalendarCondVO calendarCondVo, User user) {

		String email = calendarCondVo.getEmail();		
		User authUser = getAuthUser(email, user);		
		email = authUser.get(User.EMAIL);
		
		I18nResources resource = new I18nResources(new Locale(user.get(User.LOCALE)),"scheduler");
		
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		int mailUserSeq = Integer.parseInt(authUser.get(User.MAIL_USER_SEQ));
		String userLocale = authUser.get(User.LOCALE);
		String installLocale = EnvConstants.getBasicSetting("setup.state");
		String orgLocale = userLocale;
		if ("jp".equals(installLocale)) {
			orgLocale = "ko".equals(userLocale) ? "en" : userLocale;
		} else if ("ko".equals(installLocale)) {
			orgLocale = "jp".equals(userLocale) ? "en" : userLocale;
		}
		
		SchedulerDataVO schedulerDataVo = schedulerManager.getSchedule(mailDomainSeq, calendarCondVo.getSchedulerId(), orgLocale, resource.getMessage("scheduler.share.target.domain"));
		
		CalendarInfoVO infoVo = convertInfoVo(schedulerDataVo);
		infoVo.setDate(calendarCondVo.getDate());		
		CalendarShareVO shareVo = convertShareVo(schedulerDataVo);		
		if(shareVo != null){
			infoVo.setModify((shareVo.getMailUserSeq() == mailUserSeq));
		}
		CalendarAssetVO[] assetList = convertAssetVo(schedulerDataVo);
		infoVo.setShare(shareVo);
		infoVo.setAssetList(assetList);
		
		return infoVo;
	}
	
	public CalendarWriteVO getWriteCalendar(CalendarCondVO calendarCondVo) {
		return getWriteCalendar(calendarCondVo, null);
	}
	
	public CalendarWriteVO getWriteCalendar(CalendarCondVO calendarCondVo, User user) {
		
		String email = calendarCondVo.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		
		String date = calendarCondVo.getDate();
		if (StringUtils.isEmpty(date)) {
			date = FormatUtil.getBasicDateStr().substring(0,8);
		}
		
		CalendarWriteVO calendarWriteVo = new CalendarWriteVO();
		calendarWriteVo.setCurrent(FormatUtil.getBasicDateStr());
		calendarWriteVo.setThisdate(date);
		calendarWriteVo.setSdate(date);
		calendarWriteVo.setEdate(date);
		calendarWriteVo.setType(calendarCondVo.getType());
		
		Calendar cal = Calendar.getInstance();
		int minute = cal.get(Calendar.MINUTE);
		int hour = 0;
		if (minute <= 30) {
			hour = cal.get(Calendar.HOUR_OF_DAY);
			calendarWriteVo.setStime(((hour<10) ? "0"+hour : hour) +"30");
			cal.add(Calendar.HOUR, 1);
			hour = cal.get(Calendar.HOUR_OF_DAY);
			calendarWriteVo.setEtime(((hour<10) ? "0"+hour : hour) +"30");
		} else {
			cal.add(Calendar.HOUR, 1);
			hour = cal.get(Calendar.HOUR_OF_DAY);
			calendarWriteVo.setStime(((hour<10) ? "0"+hour : hour) +"00");
			cal.add(Calendar.HOUR, 1);
			hour = cal.get(Calendar.HOUR_OF_DAY);
			calendarWriteVo.setEtime(((hour<10) ? "0"+hour : hour) +"00");
		}
		
		int mailUserSeq = Integer.parseInt(authUser.get(User.MAIL_USER_SEQ));
		
		List<SchedulerShareVO> shareGroupList = schedulerManager.getShareGroupList(mailUserSeq);
		if (shareGroupList != null && shareGroupList.size() > 0) {
			CalendarShareVO[] shareList = new CalendarShareVO[shareGroupList.size()];
			for (int i=0; i<shareGroupList.size(); i++) {
				shareList[i] = convertShareVo(shareGroupList.get(i));
			}
			calendarWriteVo.setShareList(shareList);
		}
		
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		
		List<SchedulerAssetCategoryVO> assetCategoryList = schedulerManager.readSchedulerAssetCategoryList(mailDomainSeq);
		
		List<CalendarAssetVO> assetList = new ArrayList<CalendarAssetVO>();
		if (assetCategoryList != null && assetCategoryList.size() > 0) {
			List<SchedulerAssetVO> schedulerAssetList = null;
			CalendarAssetVO calendarAssetVO = null;
			for (SchedulerAssetCategoryVO schedulerAssetCategoryVO : assetCategoryList) {
				schedulerAssetList = schedulerAssetCategoryVO.getAssetList();
				if (schedulerAssetList != null && schedulerAssetList.size() > 0) {
					for (SchedulerAssetVO schedulerAssetVO : schedulerAssetList) {
						calendarAssetVO = new CalendarAssetVO();
						calendarAssetVO.setCategorySeq(schedulerAssetCategoryVO.getCategorySeq());
						calendarAssetVO.setCategoryName(schedulerAssetCategoryVO.getCategoryName());
						calendarAssetVO.setAssetSeq(schedulerAssetVO.getAssetSeq());
						calendarAssetVO.setAssetName(schedulerAssetVO.getAssetName());
						assetList.add(calendarAssetVO);
					}
				}
			}
		}
		
		if (assetList != null && assetList.size() > 0) {
			CalendarAssetVO[] assetArray = new CalendarAssetVO[assetList.size()];
			assetList.toArray(assetArray);
			calendarWriteVo.setAssetList(assetArray);
		}
		
		return calendarWriteVo;
	}
	
	public int deleteCalendar(CalendarDeleteCondVO calendarDeleteCondVo) {
		return deleteCalendar(calendarDeleteCondVo, null);
	}
			
	public int deleteCalendar(CalendarDeleteCondVO calendarDeleteCondVo, User user) {
		
		String email = calendarDeleteCondVo.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		
		int schedulerId = calendarDeleteCondVo.getSchedulerId();
		String repeatFlag = calendarDeleteCondVo.getRepeatFlag();
		String deleteType = calendarDeleteCondVo.getDeleteType();
		
		String deleteResult = null;
			
		if ("on".equals(repeatFlag) && "only".equals(deleteType)) {
			String repeatStartDate = calendarDeleteCondVo.getDate();
			deleteResult = schedulerManager.deleteRepeatSchedule(schedulerId, repeatStartDate);
		} else {
			String share = "off";
			boolean isShare = schedulerManager.isShareSchedule(schedulerId);
			if (isShare) share = "on";
			deleteResult = schedulerManager.deleteSchedule(schedulerId, share);
		}
		
		int ISSUCCESS = ("success".equals(deleteResult)) ? SUCCESS : FAILED;
		
		if (ISSUCCESS == SUCCESS) {
			CalendarCondVO calendarCondVO = new CalendarCondVO();
			calendarCondVO.setSchedulerId(schedulerId);
			CalendarInfoVO calendarInfoVO = getSchedule(calendarCondVO, user);
			if (calendarInfoVO != null && calendarInfoVO.getShare() != null) {
				sendMailProcess(authUser, "delete", calendarInfoVO);
			}
		}
		
		return ISSUCCESS;
	}
	
	public CalendarHolidayVO[] readHolidayList(CalendarCondVO calendarCondVo) {
		return readHolidayList(calendarCondVo, null);
	}
	
	public CalendarHolidayVO[] readHolidayList(CalendarCondVO calendarCondVo, User user) {
		
		String email = calendarCondVo.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		
		String date = calendarCondVo.getDate();
		if (StringUtils.isEmpty(date)) {
			date = FormatUtil.getBasicDateStr().substring(0,8);
		}
		
		String firstDay = date.substring(0,6)+"01";
		int thisYear = Integer.parseInt(firstDay.substring(0,4));
		
		CalendarHolidayVO[] calendarHolidayArray = null;
		                  
		List<SchedulerHolidayVO> holidayList = schedulerManager.getHoliday(mailDomainSeq, thisYear, firstDay);
		
		if (holidayList != null && holidayList.size() > 0) {
			calendarHolidayArray = new CalendarHolidayVO[holidayList.size()];
			for (int i=0; i<holidayList.size(); i++) {
				calendarHolidayArray[i] = convertHolidayVo(holidayList.get(i));
			}
		}
		return calendarHolidayArray;
	}
	
	public JSONArray readJsonAssetUseList(CalendarAssetCondVO calendarAssetCondVO, User user) {
		
		String email = calendarAssetCondVO.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		
		return schedulerManager.readJsonAssetUseList(mailDomainSeq, calendarAssetCondVO.getSchedulerId(), calendarAssetCondVO.getAssetSeqs(), calendarAssetCondVO.getStartDate(), calendarAssetCondVO.getEndDate());
	}
	
	public int saveCalendar(CalendarSaveCondVO calendarSaveCondVO) {
		return saveCalendar(calendarSaveCondVO, null);
	}
	
	public int saveCalendar(CalendarSaveCondVO calendarSaveCondVO, User user) {
		String email = calendarSaveCondVO.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		
		SchedulerDataVO schedulerDataVo = convertSaveVo(calendarSaveCondVO);
		schedulerDataVo.setMailUserSeq(Integer.parseInt(authUser.get(User.MAIL_USER_SEQ)));
		
		Map<String, Object> resultMap = null;
		String type = "save";
		int schedulerId = schedulerDataVo.getSchedulerId();
		if (schedulerId > 0) {
			if ("only".equals(calendarSaveCondVO.getType())) {
				schedulerDataVo.setCreateTime(FormatUtil.getBasicDateStr());
				schedulerDataVo.setModifyTime(FormatUtil.getBasicDateStr());
				schedulerDataVo.setOutlookSync("unsync");
				schedulerDataVo.setRepeatFlag("off");
				schedulerDataVo.setRepeatTerm("");
				schedulerDataVo.setRepeatEndDate("");
				resultMap = schedulerManager.saveSchedule(schedulerDataVo);
				if ("success".equals(resultMap.get("isSuccess"))) {
					schedulerManager.saveRepeatIgnore(schedulerId, schedulerDataVo.getIgnoreTime());
				}
			} else {
				resultMap = schedulerManager.modifySchedule(schedulerDataVo);
			}
			type = "modify";
		} else {
			resultMap = schedulerManager.saveSchedule(schedulerDataVo);
		}

		int ISSUCCESS = ("success".equals(resultMap.get("isSuccess"))) ? SUCCESS : FAILED;
		
		if (ISSUCCESS == SUCCESS && ("on".equals(calendarSaveCondVO.getCheckShare()) && "on".equals(calendarSaveCondVO.getSendMail()))) {
			CalendarCondVO calendarCondVO = new CalendarCondVO();
			calendarCondVO.setSchedulerId(schedulerDataVo.getSchedulerId());
			CalendarInfoVO calendarInfoVO = getSchedule(calendarCondVO, user);
			sendMailProcess(authUser, type, calendarInfoVO);
		}
		
		return ISSUCCESS;
	}
	
	public void sendMailProcess(User user, String type, CalendarInfoVO calendarInfoVO) {
		
		I18nResources msgResource = new I18nResources(new Locale(user.get(User.LOCALE)));
		I18nResources sMsgResource = new I18nResources(new Locale(user.get(User.LOCALE)),"scheduler");
		int schedulerId = calendarInfoVO.getSchedulerId();
		int shareSeq = 0;
		
		String title = calendarInfoVO.getTitle();
		String titleMsg = null;
		String titleContentMsg = null;
		if ("save".equals(type)) {
			titleMsg = "scheduler.share.sendmail.002";
			titleContentMsg = "scheduler.share.sendmail.005";
		} else if ("modify".equals(type)) {
			titleMsg = "scheduler.share.sendmail.003";
			titleContentMsg = "scheduler.share.sendmail.006";
		} else if ("delete".equals(type)) {
			titleMsg = "scheduler.share.sendmail.004";
			titleContentMsg = "scheduler.share.sendmail.007";
		}
		String subject = sMsgResource.getMessage(titleMsg, new Object[]{title});
		String subjectContent = sMsgResource.getMessage(titleContentMsg, new Object[]{title});
		subjectContent = "<p><span style='font-size:15px'><b>"+subjectContent+"</b></span></p>";
		String commonTh = "<th style='background:none repeat scroll 0 0 #F7F7F7;border-right:1px solid #E8E8E8;padding:5px;text-align:right;width:70px; white-space:nowrap'>#{MESSAGE}</th>";
		
		StringBuffer sb = new StringBuffer();
		sb.append("<table cellspacing='0' cellpadding='0' style='border:1px solid #E8E8E8;width:100%;font-size:12px;'>");
		sb.append("<tr>"+commonTh.replace("#{MESSAGE}", sMsgResource.getMessage("scheduler.search.subject")));
		sb.append("<td style='padding:5px;'><b><span>"+title+"</span></b></td></tr>");
		String startDateFormat = makeStringDate(calendarInfoVO.getStartDate());
		String endDateFormat = makeStringDate(calendarInfoVO.getEndDate());
		String startTimeFormat = makeStringTime(sMsgResource.getMessage("scheduler.am"), sMsgResource.getMessage("scheduler.pm"), calendarInfoVO.getStartDate().substring(8));
		String endTimeFormat = makeStringTime(sMsgResource.getMessage("scheduler.am"), sMsgResource.getMessage("scheduler.pm"), calendarInfoVO.getEndDate().substring(8));
		if (!"on".equals(calendarInfoVO.getAllDay())) {
			startDateFormat = startDateFormat+" "+startTimeFormat;
			endDateFormat = endDateFormat+" "+endTimeFormat;
		}
		sb.append("<tr>"+commonTh.replace("#{MESSAGE}", sMsgResource.getMessage("scheduler.term")));
		sb.append("<td style='padding:5px;'><span style='color:#006ECF;'>"+startDateFormat+" ~ "+endDateFormat+"</span></td></tr>");
		
		if ("on".equals(calendarInfoVO.getRepeatFlag())) {
			String repeatMessage = getRepeatMessage(calendarInfoVO.getRepeatTerm(), calendarInfoVO.getRepeatEndDate(), sMsgResource);
			sb.append("<tr>"+commonTh.replace("#{MESSAGE}", sMsgResource.getMessage("scheduler.repeat.control")));
			sb.append("<td style='padding:5px;'><span style='color: red; font-size: 11px;'>"+repeatMessage+"</span></td></tr>");
		}
		
		CalendarAssetVO[] assetArray = calendarInfoVO.getAssetList();
		if (assetArray != null && assetArray.length > 0) {
			String assetList = "";
			String assetContect = "";
			for (CalendarAssetVO asset : assetArray) {
				assetList += "["+asset.getCategoryName().trim()+"-"+asset.getAssetName().trim()+"] ";
				assetContect = asset.getContect();
			}
			sb.append("<tr>"+commonTh.replace("#{MESSAGE}", sMsgResource.getMessage("scheduler.asset.001")));
			sb.append("<td style='padding:5px;'><span style='color: green; font-size: 11px;'>"+assetList+"</span></td></tr>");
			sb.append("<tr>"+commonTh.replace("#{MESSAGE}", sMsgResource.getMessage("scheduler.asset.006")));
			sb.append("<td style='padding:5px;'><span style='color: green; font-size: 11px;'>"+assetContect+"</span></td></tr>");
		}
		
		if (calendarInfoVO.getShare() != null) {
			String shareSeqStr = calendarInfoVO.getShare().getShareValue();
			shareSeq = (StringUtils.isNotEmpty(shareSeqStr)) ? Integer.parseInt(shareSeqStr) : shareSeq;
			sb.append("<tr>"+commonTh.replace("#{MESSAGE}", sMsgResource.getMessage("scheduler.asset.009")));
			sb.append("<td style='padding:5px;'><span style='color: green; font-size: 11px; word-wrap: break-word;'>"+calendarInfoVO.getShare().getUserName()+"</span></td></tr>");
		
			String[] targets = calendarInfoVO.getShare().getShareTargets();
			if (targets != null && targets.length > 0) {
				String targetList = "";
				int index = 0;
				for (String target : targets) {
					if (StringUtils.isEmpty(target)) {
						continue;
					}
					if (index == 0) {
						targetList += target;
					} else {
						targetList += ","+target;
					}
					index++;
				}
				sb.append("<tr>"+commonTh.replace("#{MESSAGE}", sMsgResource.getMessage("scheduler.asset.010")));
				sb.append("<td style='padding:5px;'><span style='color: green; font-size: 11px;'>"+targetList+"</span></td></tr>");
			}
			if (StringUtils.isNotEmpty(calendarInfoVO.getShare().getShareName())) {
				sb.append("<tr>"+commonTh.replace("#{MESSAGE}", sMsgResource.getMessage("scheduler.share.title")));
				sb.append("<td style='padding:5px;'><span style='color: green; font-size: 11px;'>"+calendarInfoVO.getShare().getShareName()+"</span></td></tr>");
			}
		}
		
		sb.append("<tr>"+commonTh.replace("#{MESSAGE}", sMsgResource.getMessage("scheduler.location")));
		sb.append("<td style='padding:5px;'><span>"+(calendarInfoVO.getLocation()==null?"":calendarInfoVO.getLocation())+"</span></td></tr>");
		sb.append("<tr>"+commonTh.replace("#{MESSAGE}", sMsgResource.getMessage("scheduler.search.content")));
		sb.append("<td style='padding:5px;'><textarea readonly='readonly' style='width: 100%; height: 120px; background-color: white; border: 0px none;font-size:12px;'>"+(calendarInfoVO.getContent()!=null?calendarInfoVO.getContent().replaceAll("<br/>", "\n"):"")+"</textarea></td></tr>");		
		sb.append("</table>");
		
		String content = sb.toString();
		content = getStyle(user.get(User.LOCALE))+"<div class='TerraceMsg'><div style='word-wrap: break-word;'>"+subjectContent+content+"</div></div>";
		schedulerManager.sendMailProcess(user, msgResource, type, schedulerId, shareSeq, subject, content);
	}
	
	private String getStyle(String locale) {
		String font = (!"jp".equalsIgnoreCase(locale)) ? "Dotum, Arial, Verdana, Sans-Serif":"MS PGothic,Osaka, Sans-serif";
		String style = "<style type='text/css'>\n"+ ".TerraceMsg { font-size: 12px; font-family:"+
						font+";line-height:17px;}\n"+ ".Bold { font-weight: bold; }\n"+ "</style>";
		return style;
	}
	
	private String getRepeatMessage(String repeatTerm, String repeatEndDate, I18nResources msgResource) {
		String every = msgResource.getMessage("scheduler.repeat.every");
		String at = msgResource.getMessage("scheduler.repeat.at");
		String repeatType = repeatTerm.substring(0,2);
		String repeatMessage = "";
		String repeatValue1 = "";
		String repeatValue2 = "";
		String repeatValue3 = "";
		int numValue = 0;
		if ("01".equals(repeatType)) {
			repeatValue1 = repeatTerm.substring(4);
			numValue = Integer.parseInt(repeatValue1);
			repeatMessage += numValue+msgResource.getMessage("scheduler.day");
			repeatMessage += every;
		}
		else if ("02".equals(repeatType)) {
			repeatValue1 = repeatTerm.substring(2,4);
			numValue = Integer.parseInt(repeatValue1);
			repeatValue2 = repeatTerm.substring(4);
			repeatMessage += numValue+ msgResource.getMessage("scheduler.week");
			repeatMessage += every+" ";				
			
			String repeatSubValue = "";
			for (int i=0; i < repeatValue2.length()/2; i++) {
				repeatSubValue = repeatValue2.substring(i*2, (i*2)+2);
				if (i > 0) {
					repeatMessage += ",";
				}
				if("01".equals(repeatSubValue)){
					repeatMessage += msgResource.getMessage("scheduler.date.sunday");
				} else if("02".equals(repeatSubValue)){
					repeatMessage += msgResource.getMessage("scheduler.date.monday");
				} else if("03".equals(repeatSubValue)){
					repeatMessage += msgResource.getMessage("scheduler.date.tuesday");
				} else if("04".equals(repeatSubValue)){
					repeatMessage += msgResource.getMessage("scheduler.date.wednesday");
				} else if("05".equals(repeatSubValue)){
					repeatMessage += msgResource.getMessage("scheduler.date.thursday");
				} else if("06".equals(repeatSubValue)){
					repeatMessage += msgResource.getMessage("scheduler.date.friday");
				} else if("07".equals(repeatSubValue)){
					repeatMessage += msgResource.getMessage("scheduler.date.saturday");
				}
			}
		}
		else if ("03".equals(repeatType)) {
			int repeatLength = repeatTerm.length();
			repeatValue1 = repeatTerm.substring(2,4);
			numValue = Integer.parseInt(repeatValue1);
			repeatMessage += numValue+ msgResource.getMessage("scheduler.months");
			repeatMessage += every+" ";
			if (repeatLength == 6) {
				repeatValue2 = repeatTerm.substring(4);
				numValue = Integer.parseInt(repeatValue2);
				repeatMessage += numValue+ msgResource.getMessage("scheduler.day");
				repeatMessage += at;
			}
			else if (repeatLength == 8){
				repeatValue2 = repeatTerm.substring(4,6);
				repeatValue3 = repeatTerm.substring(6);					
				if("01".equals(repeatValue2)){
					repeatMessage += msgResource.getMessage("scheduler.date.first");
				} else if("02".equals(repeatValue2)){
					repeatMessage += msgResource.getMessage("scheduler.date.second");
				} else if("03".equals(repeatValue2)){
					repeatMessage += msgResource.getMessage("scheduler.date.third");
				} else if("04".equals(repeatValue2)){
					repeatMessage += msgResource.getMessage("scheduler.date.fourth");
				} else if("05".equals(repeatValue2)){
					repeatMessage += msgResource.getMessage("scheduler.date.fifth");
				}
				repeatMessage += " ";					
				
				if("01".equals(repeatValue3)){
					repeatMessage += msgResource.getMessage("scheduler.date.sunday");
				} else if("02".equals(repeatValue3)){
					repeatMessage += msgResource.getMessage("scheduler.date.monday");
				} else if("03".equals(repeatValue3)){
					repeatMessage += msgResource.getMessage("scheduler.date.tuesday");
				} else if("04".equals(repeatValue3)){
					repeatMessage += msgResource.getMessage("scheduler.date.wednesday");
				} else if("05".equals(repeatValue3)){
					repeatMessage += msgResource.getMessage("scheduler.date.thursday");
				} else if("06".equals(repeatValue3)){
					repeatMessage += msgResource.getMessage("scheduler.date.friday");
				} else if("07".equals(repeatValue3)){
					repeatMessage += msgResource.getMessage("scheduler.date.saturday");
				}
				repeatMessage += at;
			}
		}
		else if ("04".equals(repeatType)) {
			repeatValue1 = repeatTerm.substring(2,4);
			repeatValue2 = repeatTerm.substring(4);
			repeatMessage += msgResource.getMessage("scheduler.every.year")+" ";
			numValue = Integer.parseInt(repeatValue1);
			repeatMessage += numValue+msgResource.getMessage("scheduler.month");
			repeatMessage += " ";
			numValue = Integer.parseInt(repeatValue2);
			repeatMessage += numValue+msgResource.getMessage("scheduler.day");
			repeatMessage += at;
		}
		
		if (StringUtils.isNotEmpty(repeatEndDate) && repeatEndDate.trim().length() == 8) {
			String repeatEndDateFormat = makeStringDate(repeatEndDate);
			repeatMessage +=" ("+msgResource.getMessage("scheduler.repeat.enddate")+":"+repeatEndDateFormat+")";
		}
		
		return repeatMessage;
	}
	
	private String makeStringDate(int year, int month, int day) {
		return year +""+ ((month >= 10) ? month : "0"+month) +""+ ((day >= 10) ? day : "0"+day);
	}
	
	private String makeStringDate(String date) {
		return date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
	}
	
	private String makeStringTime(int time) {
		String timeStr = Integer.toString(time);
		if (StringUtils.isNotEmpty(timeStr)) {
			if (timeStr.length() == 3) {
				timeStr = "0" + timeStr;
			} else if (time == 0) {
				timeStr = "0000";
			} else if (time == 30) {
				timeStr = "0030";
			}
		}
		return timeStr;
	}
	
	private String makeStringTime(String am, String pm, String time) {
		String hourStr = time.substring(0,2);
		String minStr = time.substring(2,4);
		String ampm = am;
		int hour = Integer.parseInt(hourStr);
		if (hour > 12) {
			hour = hour - 12;
			if (hour < 10) {
				hourStr = "0"+hour;
			}
			ampm = pm;
		} else if (hour == 12) {
			ampm = pm;
		}
		return ampm+" "+hour+":"+minStr;
	}
	
	private CalendarInfoVO convertInfoVo(SchedulerDataVO schedulerDataVO) {
		CalendarInfoVO calendarInfoVO = new CalendarInfoVO();
		calendarInfoVO.setSchedulerId(schedulerDataVO.getSchedulerId());
		calendarInfoVO.setMailUserSeq(schedulerDataVO.getMailUserSeq());
		calendarInfoVO.setTitle(schedulerDataVO.getTitle());
		calendarInfoVO.setContent(StringUtils.isEmpty(schedulerDataVO.getContent()) ? schedulerDataVO.getContent() : schedulerDataVO.getContent().replaceAll("\\n", "<br/>"));
		calendarInfoVO.setLocation(schedulerDataVO.getLocation());
		calendarInfoVO.setHoliday(schedulerDataVO.getHoliday());
		calendarInfoVO.setAllDay(schedulerDataVO.getAllDay());
		calendarInfoVO.setStartDate(schedulerDataVO.getStartDate());
		calendarInfoVO.setEndDate(schedulerDataVO.getEndDate());
		calendarInfoVO.setDrowStartDate(schedulerDataVO.getDrowStartDate());
		calendarInfoVO.setDrowEndDate(schedulerDataVO.getDrowEndDate());
		calendarInfoVO.setDrowStartTime(makeStringTime(schedulerDataVO.getDrowStartTime()));
		calendarInfoVO.setDrowEndTime(makeStringTime(schedulerDataVO.getDrowEndTime()));
		calendarInfoVO.setRepeatFlag(schedulerDataVO.getRepeatFlag());
		calendarInfoVO.setRepeatTerm(schedulerDataVO.getRepeatTerm());
		calendarInfoVO.setRepeatEndDate(schedulerDataVO.getRepeatEndDate());
		calendarInfoVO.setOutlookSync(schedulerDataVO.getOutlookSync());
		
		return calendarInfoVO;
	}
	
	private CalendarShareVO convertShareVo(SchedulerDataVO schedulerDataVO) {
		CalendarShareVO shareVO = null;
		if ("on".equals(schedulerDataVO.getCheckShare())) {
			shareVO = new CalendarShareVO();
			
			shareVO.setMailUserSeq(schedulerDataVO.getMailUserSeq());
			shareVO.setShareSeq(schedulerDataVO.getShareSeq());
			shareVO.setShareName(schedulerDataVO.getShareName());
			shareVO.setShareColor(schedulerDataVO.getShareColor());
			shareVO.setUserName(schedulerDataVO.getUserName());
			shareVO.setShareValue(schedulerDataVO.getShareValue());
			JSONArray targetList = schedulerDataVO.getShareTagetNameList();
			if (targetList != null && targetList.size() > 0) {
				String[] targetArray = new String[targetList.size()];
				targetList.toArray(targetArray);
				shareVO.setShareTargets(targetArray);
			}
			JSONArray selfList = schedulerDataVO.getShareSelfTargetList();
				if (selfList != null && selfList.size() > 0) {
				String[] selfArray = new String[selfList.size()];
				selfList.toArray(selfArray);
				shareVO.setSelfTargets(selfArray);
			}
		}
		return shareVO;
	}
	
	private CalendarShareVO convertShareVo(SchedulerShareVO schedulerShareVO) {
		CalendarShareVO calendarShareVO = new CalendarShareVO();
		calendarShareVO.setShareSeq(schedulerShareVO.getShareSeq());
		calendarShareVO.setShareName(schedulerShareVO.getShareName());

		return calendarShareVO;
	}
	
	private CalendarAssetVO[] convertAssetVo(SchedulerDataVO schedulerDataVO) {
		
		CalendarAssetVO[] assetVOArray = null;
		CalendarAssetVO assetVO = null;
		if ("on".equals(schedulerDataVO.getCheckAsset())) {
			JSONArray assetPlanList = schedulerDataVO.getAssetPlanInfoList();
			assetVOArray = new CalendarAssetVO[assetPlanList.size()];
			JSONObject asset = null;
			for (int i=0; i<assetPlanList.size(); i++) {
				asset = (JSONObject)assetPlanList.get(i);
				assetVO = new CalendarAssetVO();
				assetVO.setCategorySeq((Integer)asset.get("categorySeq"));
				assetVO.setAssetSeq((Integer)asset.get("assetSeq"));
				assetVO.setCategoryName((String)asset.get("categoryName"));
				assetVO.setAssetName((String)asset.get("assetName"));
				assetVO.setContect((String)asset.get("contect"));
				assetVOArray[i] = assetVO;
			}
		}
		return assetVOArray;
	}
	
	private SchedulerDataVO convertSaveVo(CalendarSaveCondVO calendarSaveCondVO) {
		SchedulerDataVO schedulerDataVO = new SchedulerDataVO();
		schedulerDataVO.setSchedulerId(calendarSaveCondVO.getSchedulerId());
		schedulerDataVO.setStartDate(calendarSaveCondVO.getStartDate());
		schedulerDataVO.setEndDate(calendarSaveCondVO.getEndDate());
		schedulerDataVO.setTitle(calendarSaveCondVO.getTitle());
		schedulerDataVO.setLocation(calendarSaveCondVO.getLocation());
		schedulerDataVO.setContent(calendarSaveCondVO.getContent());		
		schedulerDataVO.setAllDay((calendarSaveCondVO.getAllDay()!= null)?calendarSaveCondVO.getAllDay():"off");
		schedulerDataVO.setCheckShare(calendarSaveCondVO.getCheckShare());
		schedulerDataVO.setShareValue(calendarSaveCondVO.getShareValue());
		schedulerDataVO.setCheckAsset(calendarSaveCondVO.getCheckAsset());
		schedulerDataVO.setAssetReserveValue(calendarSaveCondVO.getAssetReserveValue());
		schedulerDataVO.setContect(calendarSaveCondVO.getContect());
		schedulerDataVO.setCheckSelfTarget(calendarSaveCondVO.getCheckSelfTarget());
		schedulerDataVO.setSelfTargetList(calendarSaveCondVO.getSelfTargetList());
		
		schedulerDataVO.setRepeatFlag(calendarSaveCondVO.getRepeatFlag());
		schedulerDataVO.setRepeatTerm(calendarSaveCondVO.getRepeatTerm());
		schedulerDataVO.setRepeatEndDate(calendarSaveCondVO.getRepeatEndDate());
		schedulerDataVO.setIgnoreTime(calendarSaveCondVO.getIgnoreTime());
		
		return schedulerDataVO;
	}
	
	private CalendarHolidayVO convertHolidayVo(SchedulerHolidayVO schedulerHolidayVo) {
		CalendarHolidayVO calendarHolidayVo = new CalendarHolidayVO();
		calendarHolidayVo.setHolidayDate(schedulerHolidayVo.getHolidayDate());
		calendarHolidayVo.setHolidayName(schedulerHolidayVo.getHolidayName());
		calendarHolidayVo.setHoliday(schedulerHolidayVo.getHoliday());
		
		return calendarHolidayVo;
	}
	
	private User getAuthUser(String email, User user) {
		User authUser = null;
		if (user == null) {
			String[] data = email.split("@");
			MailUserManager mailUserManager = (MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");
			authUser = mailUserManager.readUserAuthInfo(data[0], data[1]);
		} else {
			authUser = user;
		}
		
		return authUser;
	}
}
