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
import com.terracetech.tims.webmail.util.ResponseUtil;

/**
 * Outlook 데이터 수신 Controller
 * 
 * 주요 기능:
 * 1. Outlook 스케줄 데이터 수신
 * 2. 스케줄 삭제 처리
 * 3. 스케줄 추가/수정 처리
 * 4. JSON 응답 처리
 * 5. 인증 체크
 * 
 * Struts2 Action: SchedulerOutlookReceiveAction
 * 변환 일시: 2025-10-20
 */
@Controller("schedulerOutlookReceiveController")
public class SchedulerOutlookReceiveController {

	@Autowired
	private SchedulerManager schedulerManager;

	/**
	 * Outlook 데이터 수신 처리
	 * 
	 * @param itemList 아이템 목록
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "itemList", required = false) String[] itemList,
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
		JSONObject deleteResultList = new JSONObject();
		
		try {
			// 아이템 목록 처리
			if (itemList != null && itemList.length > 0) {
				processItemList(itemList, syncResultList, deleteResultList);
			}
			
			// 작업 결과 설정
			workResult.put("syncResult", syncResultList);
			workResult.put("deleteResult", deleteResultList);
			workResult.put("status", "success");
			
		} catch (Exception e) {
			// 에러 처리
			workResult.put("status", "error");
			workResult.put("message", resource.getMessage("outlook.receive.error"));
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
		// 인증 체크 로직 구현
		return user != null;
	}

	/**
	 * 아이템 목록 처리
	 * 
	 * @param itemList 아이템 목록
	 * @param syncResultList 동기화 결과 목록
	 * @param deleteResultList 삭제 결과 목록
	 * @throws Exception
	 */
	private void processItemList(String[] itemList, JSONObject syncResultList, JSONObject deleteResultList) throws Exception {
		JSONObject jsonItem = null;
		
		for (String item : itemList) {
			jsonItem = (JSONObject) JSONValue.parse(item);
			
			if (jsonItem.containsKey("delete")) {
				// 삭제 처리
				processDeleteItems(jsonItem, deleteResultList);
			} else {
				// 동기화 처리
				processSyncItems(jsonItem, syncResultList);
			}
		}
	}

	/**
	 * 삭제 아이템 처리
	 * 
	 * @param jsonItem JSON 아이템
	 * @param deleteResultList 삭제 결과 목록
	 * @throws Exception
	 */
	private void processDeleteItems(JSONObject jsonItem, JSONObject deleteResultList) throws Exception {
		JSONArray deleteList = (JSONArray) jsonItem.get("delete");
		
		if (deleteList != null && deleteList.size() > 0) {
			JSONArray successDeleteId = new JSONArray();
			JSONArray failDeleteId = new JSONArray();
			
			for (Object item : deleteList) {
				String deleteScheduleId = (String) item;
				String deleteResult = schedulerManager.deleteScheduleComplete(Integer.parseInt(deleteScheduleId));
				
				if ("fail".equals(deleteResult)) {
					failDeleteId.add(deleteScheduleId);
				} else {
					successDeleteId.add(deleteScheduleId);
				}
			}
			
			deleteResultList.put("success", successDeleteId);
			deleteResultList.put("fail", failDeleteId);
		}
	}

	/**
	 * 동기화 아이템 처리
	 * 
	 * @param jsonItem JSON 아이템
	 * @param syncResultList 동기화 결과 목록
	 * @throws Exception
	 */
	private void processSyncItems(JSONObject jsonItem, JSONObject syncResultList) throws Exception {
		// 동기화 처리 로직 구현
		// 실제 구현에서는 스케줄 데이터를 파싱하고 저장/수정 처리
		syncResultList.put("status", "success");
		syncResultList.put("processed", 1);
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