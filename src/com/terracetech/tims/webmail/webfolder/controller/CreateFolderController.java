package com.terracetech.tims.webmail.webfolder.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;
import com.terracetech.tims.webmail.webfolder.vo.FolderVO;

@Controller("createFolderController")
public class CreateFolderController {

	@Autowired
	private WebfolderManager webfolderManager;

	public String execute(
			@RequestParam(value = "parentFolderId", required = false) String parentFolderId,
			@RequestParam(value = "folderName", required = false) String folderName,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		FolderVO folderVo = new FolderVO();
		folderVo.setParentFolderId(Integer.parseInt(parentFolderId));
		folderVo.setFolderName(folderName);
		folderVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		
		boolean result = webfolderManager.createFolder(folderVo);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
