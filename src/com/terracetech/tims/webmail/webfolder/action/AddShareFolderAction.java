package com.terracetech.tims.webmail.webfolder.action;

import java.io.PrintWriter;
import java.util.Locale;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.MakeMessage;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;

public class AddShareFolderAction extends BaseAction {
	private WebfolderManager webfolderManager = null;
	private int[] fuid = null;
	
	public void setWebfolderManager(WebfolderManager webfolderManager) {
		this.webfolderManager = webfolderManager;
	}
	
	public String execute() throws Exception {
		I18nResources resource = getMessageResource("webfolder");
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		if (fuid != null) {
			for (int i=0; i< fuid.length; i++) {
				webfolderManager.addShareFolder(userSeq, fuid[i]);
			}
		}
		
		response.setHeader("Content-Type", "text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String msg = resource.getMessage("addtree.ok");
		String url = "/webfolder/webfolder.do";
	    out.print(MakeMessage.printAlertUrl(msg, url));
	    out.flush();
		
		return null;
	}

	public int[] getFuid() {
		return fuid;
	}

	public void setFuid(int[] fuid) {
		this.fuid = fuid;
	}
	
}
