package com.terracetech.tims.webmail.setting.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.ZipcodeVO;

@Controller("viewZipcodeController")
public class ViewZipcodeController {

	@Autowired
	private SettingManager settingManager;

	public String execute(
			@RequestParam(value = "searchKeyword", required = false) String searchKeyword,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		List<ZipcodeVO> zipcodeList = settingManager.getZipcodeList(searchKeyword);
		model.addAttribute("zipcodeList", zipcodeList);
		return "success";
	}
}
