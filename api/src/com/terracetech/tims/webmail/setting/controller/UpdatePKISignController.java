package com.terracetech.tims.webmail.setting.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.PKISignVO;

@Controller("updatePKISignController")
public class UpdatePKISignController {

	@Autowired
	private SettingManager settingManager;

	public String execute(
			@RequestParam(value = "pkiSignData", required = false) String pkiSignData,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		PKISignVO pkiSignVo = new PKISignVO();
		pkiSignVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		pkiSignVo.setPkiSignData(pkiSignData);
		
		boolean result = settingManager.updatePKISign(pkiSignVo);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
