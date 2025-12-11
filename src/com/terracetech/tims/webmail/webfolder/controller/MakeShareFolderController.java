package com.terracetech.tims.webmail.webfolder.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderShareVO;

@Controller("makeShareFolderController")
public class MakeShareFolderController {

	@Autowired
	private WebfolderManager webfolderManager;

	public String execute(
			@RequestParam(value = "folderId", required = false) String folderId,
			@RequestParam(value = "shareUserIds", required = false) String[] shareUserIds,
			HttpServletRequest request,
			Model model) throws Exception {
		
		User user = SessionUtil.getUser(request);
		ShareFolderVO shareFolderVo = new ShareFolderVO();
		shareFolderVo.setFolderId(Integer.parseInt(folderId));
		shareFolderVo.setOwnerUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		shareFolderVo.setShareUserIds(shareUserIds);
		
		boolean result = webfolderManager.makeShareFolder(shareFolderVo);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
