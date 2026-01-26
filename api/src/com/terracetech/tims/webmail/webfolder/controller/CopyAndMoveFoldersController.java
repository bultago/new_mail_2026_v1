package com.terracetech.tims.webmail.webfolder.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;

@Controller("copyAndMoveFoldersController")
public class CopyAndMoveFoldersController {

	@Autowired
	private WebfolderManager webfolderManager;

	public String execute(
			@RequestParam(value = "folderIds", required = false) String[] folderIds,
			@RequestParam(value = "targetFolderId", required = false) String targetFolderId,
			@RequestParam(value = "action", required = false) String action,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		boolean result = "copy".equals(action) ? 
			webfolderManager.copyFolders(folderIds, Integer.parseInt(targetFolderId)) :
			webfolderManager.moveFolders(folderIds, Integer.parseInt(targetFolderId));
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
