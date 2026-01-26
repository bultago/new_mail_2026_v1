package com.terracetech.tims.webmail.scheduler.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.exception.UserAuthException;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * Outlook 동기화 Controller
 * 
 * 주요 기능:
 * 1. Outlook 스케줄 동기화
 * 2. 스케줄 데이터 조회 및 변환
 * 3. 동기화 결과 처리
 * 4. JSON 응답 생성
 * 
 * Struts2 Action: SchedulerOutlookSyncAction
 * 변환 일시: 2025-10-20
 */
@Controller("schedulerOutlookSyncController")
public class SchedulerOutlookSyncController {

	@Autowired
	private SchedulerManager schedulerManager;

	/**
	 * Outlook 동기화 처리
	 * 
	 * @param startDate 시작 날짜
	 * @param syncType 동기화 타입
	 * @param scheduleId 스케줄 ID (JSON 형태)
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "syncType", required = false) String syncType,
			@RequestParam(value = "scheduleId", required = false) String scheduleId,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "scheduler");
		
		// 인증 체크
		validateAuthentication(user);
		
		// JSON 응답 객체 생성
		JSONObject workResult = new JSONObject();
		JSONObject jsonSchedulerIds = null;
		
		try {
			// 스케줄 ID 처리
			if (StringUtils.isNotEmpty(scheduleId)) {
				jsonSchedulerIds = processScheduleIds(scheduleId);
			}
			
			// 동기화 처리
			JSONObject syncResult = performSync(startDate, syncType, jsonSchedulerIds);
			
			// 작업 결과 설정
			workResult.put("syncResult", syncResult);
			workResult.put("status", "success");
			
		} catch (Exception e) {
			// 에러 처리
			workResult.put("status", "error");
			workResult.put("message", resource.getMessage("outlook.sync.error"));
		}
		
		// Model에 응답 추가
		model.addAttribute("response", workResult.toJSONString());
		
		return "success";
	}

	/**
	 * 인증 체크
	 * 
	 * @param user 사용자 정보
	 * @throws UserAuthException
	 */
	private void validateAuthentication(User user) throws UserAuthException {
		if (!isCheckAuth(user)) {
			throw new UserAuthException();
		}
	}

	/**
	 * 인증 체크 여부
	 * 
	 * @param user 사용자 정보
	 * @return 인증 체크 여부
	 */
	private boolean isCheckAuth(User user) {
		return user != null;
	}

	/**
	 * 스케줄 ID 처리
	 * 
	 * @param scheduleId 스케줄 ID JSON 문자열
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject processScheduleIds(String scheduleId) throws Exception {
		return (JSONObject) JSONValue.parse(scheduleId);
	}

	/**
	 * 동기화 수행
	 * 
	 * @param startDate 시작 날짜
	 * @param syncType 동기화 타입
	 * @param jsonSchedulerIds 스케줄 ID JSON 객체
	 * @return 동기화 결과
	 * @throws Exception
	 */
	private JSONObject performSync(String startDate, String syncType, JSONObject jsonSchedulerIds) throws Exception {
		JSONObject syncResult = new JSONObject();
		
		if (jsonSchedulerIds != null && jsonSchedulerIds.containsKey("schedulerIds")) {
			// 특정 스케줄 ID들로 동기화
			JSONArray scheduleIdArray = (JSONArray) jsonSchedulerIds.get("schedulerIds");
			
			if (scheduleIdArray != null && scheduleIdArray.size() > 0) {
				int[] schedulerIds = convertToIntArray(scheduleIdArray);
				List<SchedulerDataVO> scheduleList = schedulerManager.getSchedulerListByIds(schedulerIds);
				
				// 스케줄 데이터를 Outlook 형식으로 변환
				JSONArray outlookData = convertToOutlookFormat(scheduleList);
				syncResult.put("data", outlookData);
				syncResult.put("count", scheduleList.size());
			}
		} else {
			// 전체 동기화
			List<SchedulerDataVO> scheduleList = schedulerManager.getSchedulerListByDateRange(startDate);
			
			// 스케줄 데이터를 Outlook 형식으로 변환
			JSONArray outlookData = convertToOutlookFormat(scheduleList);
			syncResult.put("data", outlookData);
			syncResult.put("count", scheduleList.size());
		}
		
		syncResult.put("syncType", syncType);
		syncResult.put("status", "success");
		
		return syncResult;
	}

	/**
	 * JSON 배열을 int 배열로 변환
	 * 
	 * @param scheduleIdArray 스케줄 ID JSON 배열
	 * @return int 배열
	 */
	private int[] convertToIntArray(JSONArray scheduleIdArray) {
		int[] schedulerIds = new int[scheduleIdArray.size()];
		for (int i = 0; i < scheduleIdArray.size(); i++) {
			schedulerIds[i] = Integer.parseInt((String) scheduleIdArray.get(i));
		}
		return schedulerIds;
	}

	/**
	 * 스케줄 데이터를 Outlook 형식으로 변환
	 * 
	 * @param scheduleList 스케줄 목록
	 * @return Outlook 형식 JSON 배열
	 */
	private JSONArray convertToOutlookFormat(List<SchedulerDataVO> scheduleList) {
		JSONArray outlookData = new JSONArray();
		
		for (SchedulerDataVO schedule : scheduleList) {
			JSONObject outlookItem = new JSONObject();
			
			// Outlook 형식으로 데이터 변환
			outlookItem.put("id", schedule.getScheduleId());
			outlookItem.put("subject", schedule.getSubject());
			outlookItem.put("startDate", schedule.getStartDate());
			outlookItem.put("endDate", schedule.getEndDate());
			outlookItem.put("content", schedule.getContent());
			
			outlookData.add(outlookItem);
		}
		
		return outlookData;
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
}