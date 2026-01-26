package com.terracetech.tims.webmail.note.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.note.manager.NoteManager;
import com.terracetech.tims.webmail.note.vo.NoteSettingVO;

@Controller("noteSettingController")
public class NoteSettingController {
	@Autowired
	private NoteManager noteManager;

	public String execute(
			@RequestParam(value = "noteSetting", required = false) String noteSetting,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		NoteSettingVO noteSettingVo = new NoteSettingVO();
		noteSettingVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		noteSettingVo.setNoteSetting(noteSetting);
		boolean result = noteManager.saveNoteSetting(noteSettingVo);
		model.addAttribute("result", result ? "success" : "fail");
		return "success";
	}
}
