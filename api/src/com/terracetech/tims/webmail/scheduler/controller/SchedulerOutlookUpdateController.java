package com.terracetech.tims.webmail.scheduler.controller;

import java.util.ArrayList;
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
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * Outlook 업데이트 Controller
 * 
 * 주요 기능:
 * 1. Outlook 스케줄 업데이트
 * 2. 스케줄 생성/수정/삭제 처리
 * 3. 반복 스케줄 처리
 * 4. JSON 응답 생성
 * 
 * Struts2 Action: SchedulerOutlookUpdateAction
 * 변환 일시: 2025-10-20
 */
@Controller("schedulerOutlookUpdateController")
public class SchedulerOutlookUpdateController {

	@Autowired
	private SchedulerManager schedulerManager;

	/**
	 * Outlook 업데이트 처리
	 * 
	 * @param scheduledata 스케줄 데이터 (JSON 형태)
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "scheduledata", required = false) String scheduledata,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "scheduler");
		
		// 인증 체크
		validateAuthentication(user);
		
		// JSON 응답 객체 생성
		JSONObject workResult = new JSONObject();
		JSONObject syncResultList = new JSONObject();
		JSONArray successSyncId = new JSONArray();
		JSONArray failSyncId = new JSONArray();
		
		try {
			// 스케줄 데이터 처리
			if (StringUtils.isNotEmpty(scheduledata)) {
				processScheduleData(scheduledata, user, successSyncId, failSyncId);
			}
			
			// 동기화 결과 설정
			syncResultList.put("success", successSyncId);
			syncResultList.put("fail", failSyncId);
			
			// 작업 결과 설정
			workResult.put("syncResult", syncResultList);
			workResult.put("status", "success");
			
		} catch (Exception e) {
			// 에러 처리
			workResult.put("status", "error");
			workResult.put("message", resource.getMessage("outlook.update.error"));
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
	 * 스케줄 데이터 처리
	 * 
	 * @param scheduledata 스케줄 데이터 JSON 문자열
	 * @param user 사용자 정보
	 * @param successSyncId 성공 ID 목록
	 * @param failSyncId 실패 ID 목록
	 * @throws Exception
	 */
	private void processScheduleData(String scheduledata, User user, JSONArray successSyncId, JSONArray failSyncId) throws Exception {
		JSONObject scheduleObj = (JSONObject) JSONValue.parse(scheduledata);
		
		if (scheduleObj != null) {
			// 생성 처리
			if (scheduleObj.containsKey("create")) {
				processCreateSchedule(scheduleObj, user, successSyncId, failSyncId);
			}
			
			// 수정 처리
			if (scheduleObj.containsKey("update")) {
				processUpdateSchedule(scheduleObj, user, successSyncId, failSyncId);
			}
			
			// 삭제 처리
			if (scheduleObj.containsKey("delete")) {
				processDeleteSchedule(scheduleObj, user, successSyncId, failSyncId);
			}
		}
	}

	/**
	 * 스케줄 생성 처리
	 * 
	 * @param scheduleObj 스케줄 JSON 객체
	 * @param user 사용자 정보
	 * @param successSyncId 성공 ID 목록
	 * @param failSyncId 실패 ID 목록
	 * @throws Exception
	 */
	private void processCreateSchedule(JSONObject scheduleObj, User user, JSONArray successSyncId, JSONArray failSyncId) throws Exception {
		JSONObject jsonSchedule = (JSONObject) scheduleObj.get("create");
		SchedulerDataVO schedulerDataVo = convertJsonToVo(jsonSchedule, Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		
		try {
			schedulerManager.saveSchedule(schedulerDataVo);
			
			// 반복 스케줄 무시 목록 처리
			List<String> repeatIgnoreList = schedulerDataVo.getIgnoreTimeList();
			if (repeatIgnoreList != null && repeatIgnoreList.size() > 0) {
				processRepeatIgnoreList(repeatIgnoreList, schedulerDataVo.getScheduleId());
			}
			
			successSyncId.add(schedulerDataVo.getScheduleId());
			
		} catch (Exception e) {
			failSyncId.add(schedulerDataVo.getScheduleId());
		}
	}

	/**
	 * 스케줄 수정 처리
	 * 
	 * @param scheduleObj 스케줄 JSON 객체
	 * @param user 사용자 정보
	 * @param successSyncId 성공 ID 목록
	 * @param failSyncId 실패 ID 목록
	 * @throws Exception
	 */
	private void processUpdateSchedule(JSONObject scheduleObj, User user, JSONArray successSyncId, JSONArray failSyncId) throws Exception {
		JSONArray updateArray = (JSONArray) scheduleObj.get("update");
		
		if (updateArray != null && updateArray.size() > 0) {
			for (Object item : updateArray) {
				JSONObject jsonSchedule = (JSONObject) item;
				SchedulerDataVO schedulerDataVo = convertJsonToVo(jsonSchedule, Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
				
				try {
					schedulerManager.updateSchedule(schedulerDataVo);
					successSyncId.add(schedulerDataVo.getScheduleId());
				} catch (Exception e) {
					failSyncId.add(schedulerDataVo.getScheduleId());
				}
			}
		}
	}

	/**
	 * 스케줄 삭제 처리
	 * 
	 * @param scheduleObj 스케줄 JSON 객체
	 * @param user 사용자 정보
	 * @param successSyncId 성공 ID 목록
	 * @param failSyncId 실패 ID 목록
	 * @throws Exception
	 */
	private void processDeleteSchedule(JSONObject scheduleObj, User user, JSONArray successSyncId, JSONArray failSyncId) throws Exception {
		JSONArray deleteArray = (JSONArray) scheduleObj.get("delete");
		
		if (deleteArray != null && deleteArray.size() > 0) {
			for (Object item : deleteArray) {
				String scheduleId = (String) item;
				
				try {
					schedulerManager.deleteSchedule(Integer.parseInt(scheduleId));
					successSyncId.add(scheduleId);
				} catch (Exception e) {
					failSyncId.add(scheduleId);
				}
			}
		}
	}

	/**
	 * JSON을 SchedulerDataVO로 변환
	 * 
	 * @param jsonSchedule JSON 스케줄 객체
	 * @param userSeq 사용자 시퀀스
	 * @return SchedulerDataVO
	 */
	private SchedulerDataVO convertJsonToVo(JSONObject jsonSchedule, int userSeq) {
		SchedulerDataVO vo = new SchedulerDataVO();
		
		vo.setScheduleId(jsonSchedule.get("id") != null ? Integer.parseInt(jsonSchedule.get("id").toString()) : 0);
		vo.setSubject((String) jsonSchedule.get("subject"));
		vo.setContent((String) jsonSchedule.get("content"));
		vo.setStartDate((String) jsonSchedule.get("startDate"));
		vo.setEndDate((String) jsonSchedule.get("endDate"));
		vo.setCreatorSeq(userSeq);
		
		// 반복 스케줄 설정
		if (jsonSchedule.get("repeatType") != null) {
			vo.setRepeatType((String) jsonSchedule.get("repeatType"));
		}
		
		return vo;
	}

	/**
	 * 반복 스케줄 무시 목록 처리
	 * 
	 * @param repeatIgnoreList 무시 목록
	 * @param scheduleId 스케줄 ID
	 * @throws Exception
	 */
	private void processRepeatIgnoreList(List<String> repeatIgnoreList, int scheduleId) throws Exception {
		for (String ignoreTime : repeatIgnoreList) {
			// 반복 스케줄 무시 시간 처리 로직 구현
			schedulerManager.addRepeatIgnoreTime(scheduleId, ignoreTime);
		}
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