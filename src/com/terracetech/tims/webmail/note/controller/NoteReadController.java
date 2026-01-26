package com.terracetech.tims.webmail.note.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.note.manager.NoteManager;
import com.terracetech.tims.webmail.note.vo.NoteVO;

@Controller("noteReadController")
public class NoteReadController {
	@Autowired
	private NoteManager noteManager;

	public String execute(
			@RequestParam(value = "noteId", required = false) String noteId,
			HttpServletRequest request,
			Model model) throws Exception {
		User user = SessionUtil.getUser(request);
		NoteVO note = noteManager.getNote(Integer.parseInt(noteId));
		model.addAttribute("note", note);
		return "success";
	}
}
