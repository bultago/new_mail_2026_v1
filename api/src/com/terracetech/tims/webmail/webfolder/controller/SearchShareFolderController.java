package com.terracetech.tims.webmail.webfolder.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderShareVO;

@Controller("searchShareFolderController")
public class SearchShareFolderController {

	@Autowired
	private WebfolderManager webfolderManager;

	public String execute(
			@RequestParam(value = "searchKeyword", required = false) String searchKeyword,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		List<ShareFolderVO> shareFolderList = webfolderManager.searchShareFolder(searchKeyword);
		model.addAttribute("shareFolderList", shareFolderList);
		return "success";
	}
}
