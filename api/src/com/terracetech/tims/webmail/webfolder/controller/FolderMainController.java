package com.terracetech.tims.webmail.webfolder.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;
import com.terracetech.tims.webmail.webfolder.vo.FolderVO;

@Controller("folderMainController")
public class FolderMainController {

	@Autowired
	private WebfolderManager webfolderManager;

	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		FolderVO rootFolder = webfolderManager.getRootFolder(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		model.addAttribute("rootFolder", rootFolder);
		return "success";
	}
}
