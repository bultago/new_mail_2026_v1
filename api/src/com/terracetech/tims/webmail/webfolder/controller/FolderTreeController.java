package com.terracetech.tims.webmail.webfolder.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;
import com.terracetech.tims.webmail.webfolder.vo.FolderVO;

@Controller("folderTreeController")
public class FolderTreeController {

	@Autowired
	private WebfolderManager webfolderManager;

	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		List<FolderVO> folderTree = webfolderManager.getFolderTree(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		model.addAttribute("folderTree", folderTree);
		return "success";
	}
}
