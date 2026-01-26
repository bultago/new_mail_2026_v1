package com.terracetech.tims.webmail.setting.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.FilterVO;

/**
 * 필터 저장 Controller
 * 
 * 주요 기능:
 * 1. 필터 규칙 저장
 * 2. 필터 설정 영구 저장
 * 
 * Struts2 Action: SaveFilterAction
 * 변환 일시: 2025-10-20
 */
@Controller("saveFilterController")
public class SaveFilterController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 필터 저장
	 * 
	 * @param filterName 필터명
	 * @param filterRule 필터 규칙
	 * @param filterAction 필터 동작
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "filterName", required = false) String filterName,
			@RequestParam(value = "filterRule", required = false) String filterRule,
			@RequestParam(value = "filterAction", required = false) String filterAction,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 필터 저장
		boolean saveResult = saveFilter(user, filterName, filterRule, filterAction);
		
		if (saveResult) {
			model.addAttribute("result", "success");
			model.addAttribute("message", resource.getMessage("filter.save.success"));
		} else {
			model.addAttribute("result", "fail");
			model.addAttribute("message", resource.getMessage("filter.save.failed"));
		}
		
		return "success";
	}

	private boolean saveFilter(User user, String filterName, String filterRule, String filterAction) throws Exception {
		FilterVO filterVo = new FilterVO();
		filterVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		filterVo.setFilterName(filterName);
		filterVo.setFilterRule(filterRule);
		filterVo.setFilterAction(filterAction);
		
		return settingManager.saveFilter(filterVo);
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
