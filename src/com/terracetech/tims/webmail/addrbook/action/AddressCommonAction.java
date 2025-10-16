package com.terracetech.tims.webmail.addrbook.action;

import javax.servlet.http.HttpServletRequest;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.advice.TopMenu;

public class AddressCommonAction extends BaseAction{

	private static final long serialVersionUID = 20081229L;
	
	private String installLocale = null;
	
	public String getInstallLocale() {
		installLocale = EnvConstants.getBasicSetting("setup.state"); 
		
		return installLocale;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	@TopMenu
	public String loadPage() throws Exception{		
		return "load";
	}
}
