package com.terracetech.tims.webmail.setting.action;

import java.util.Locale;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.setting.manager.SignManager;

public class WriteSignDataAction extends BaseAction {
	private SignManager signManager = null;

	public void setSignManager(SignManager signManager) {
		this.signManager = signManager;
	}
	
	public String execute() throws Exception {
		
		Locale locale = I18nConstants.getUserLocale(request);
		
		request.setAttribute("locale", locale.getLanguage());
		request.setAttribute("type", "write");
		
		return "success";
	}
}
