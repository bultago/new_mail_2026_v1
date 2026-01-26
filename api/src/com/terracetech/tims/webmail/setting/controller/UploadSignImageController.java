package com.terracetech.tims.webmail.setting.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;

/**
 * 서명 이미지 업로드 Controller
 * 
 * 주요 기능:
 * 1. 서명 이미지 업로드
 * 2. 이미지 파일 처리
 * 
 * Struts2 Action: UploadSignImageAction
 * 변환 일시: 2025-10-20
 */
@Controller("uploadSignImageController")
public class UploadSignImageController {

	@Autowired
	private SettingManager settingManager;

	/**
	 * 서명 이미지 업로드
	 * 
	 * @param imageFile 이미지 파일
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		// 서명 이미지 업로드
		String uploadResult = uploadSignImage(user, imageFile);
		
		if (uploadResult != null) {
			model.addAttribute("result", "success");
			model.addAttribute("imagePath", uploadResult);
			model.addAttribute("message", resource.getMessage("sign.image.upload.success"));
		} else {
			model.addAttribute("result", "fail");
			model.addAttribute("message", resource.getMessage("sign.image.upload.failed"));
		}
		
		return "success";
	}

	private String uploadSignImage(User user, MultipartFile imageFile) throws Exception {
		if (imageFile != null && !imageFile.isEmpty()) {
			return settingManager.uploadSignImage(Integer.parseInt(user.get(User.MAIL_USER_SEQ)), imageFile);
		}
		return null;
	}

	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}
}
