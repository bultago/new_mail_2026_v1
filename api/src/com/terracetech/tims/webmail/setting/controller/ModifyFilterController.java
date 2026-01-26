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
 * 필터 수정 Controller
 * 
 * 주요 기능:
 * 1. 필터 규칙 수정
 * 2. 필터 설정 업데이트
 * 
 * Struts2 Action: ModifyFilterAction
 * 변환 일시: 2025-10-20
 */
@Controller("modifyFilterController")
public class ModifyFilterController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 필터 수정
	 * 
	 * @param filterId 필터 ID
	 * @param filterName 필터명
	 * @param filterRule 필터 규칙
	 * @param filterAction 필터 동작
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "filterId", required = false) String filterId,
			@RequestParam(value = "filterName", required = false) String filterName,
			@RequestParam(value = "filterRule", required = false) String filterRule,
			@RequestParam(value = "filterAction", required = false) String filterAction,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 필터 수정
		boolean updateResult = updateFilter(user, filterId, filterName, filterRule, filterAction);
		
		if (updateResult) {
			model.addAttribute("result", "success");
			model.addAttribute("message", resource.getMessage("filter.modify.success"));
		} else {
			model.addAttribute("result", "fail");
			model.addAttribute("message", resource.getMessage("filter.modify.failed"));
		}
		
		return "success";
	}

	private boolean updateFilter(User user, String filterId, String filterName, String filterRule, String filterAction) throws Exception {
		FilterVO filterVo = new FilterVO();
		filterVo.setFilterId(Integer.parseInt(filterId));
		filterVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		filterVo.setFilterName(filterName);
		filterVo.setFilterRule(filterRule);
		filterVo.setFilterAction(filterAction);
		
		return settingManager.updateFilter(filterVo);
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
