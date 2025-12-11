package com.terracetech.tims.webmail.webfolder.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;

@Controller("deleteShareFolderController")
public class DeleteShareFolderController {

	@Autowired
	private WebfolderManager webfolderManager;

	public String execute(
			@RequestParam(value = "shareFolderIds", required = false) String[] shareFolderIds,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		boolean result = webfolderManager.deleteShareFolder(shareFolderIds);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
