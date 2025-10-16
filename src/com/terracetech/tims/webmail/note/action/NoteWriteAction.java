package com.terracetech.tims.webmail.note.action;

import com.terracetech.tims.webmail.common.BaseAction;

public class NoteWriteAction extends BaseAction {
	
	private String to = null;
	
	public String execute() throws Exception {
		
		return "success";
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
}
