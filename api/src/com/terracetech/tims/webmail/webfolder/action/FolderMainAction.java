package com.terracetech.tims.webmail.webfolder.action;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;

public class FolderMainAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private String uid = null;
	
	public String execute() throws Exception {
		User user = UserAuthManager.getUser(request);
		uid = user.get(User.MAIL_UID);
		
		return "success";
	}

	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
}
