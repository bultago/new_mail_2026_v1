package com.terracetech.tims.webmail.webfolder.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderShareVO;

@Controller("shareFolderController")
public class ShareFolderController {

	@Autowired
	private WebfolderManager webfolderManager;

	public String execute(HttpServletRequest request, Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		List<ShareFolderVO> shareFolderList = webfolderManager.getShareFolderList(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		model.addAttribute("shareFolderList", shareFolderList);
		return "success";
	}
}
