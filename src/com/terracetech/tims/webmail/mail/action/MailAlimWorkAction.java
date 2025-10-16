package com.terracetech.tims.webmail.mail.action;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class MailAlimWorkAction extends BaseAction {
	public MailAlimWorkAction() {
		setAuthCheck(false);
	}
	
	private static final long serialVersionUID = -2263582124737967264L;
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	public String systemInfoWork() throws Exception{
		String cryptMethod = (String) request.getSession().getAttribute(EnvConstants.COOKIE_ALGORITHM_METHOD);		
		JSONObject jObj = new JSONObject();
		jObj.put("crytMethod", cryptMethod);		
		ResponseUtil.processResponse(response, jObj);
		
		return null;
	}
	
	public String mailAuthWork(){
		
		
		return null;
	}
	
	public String mailContentsWork(){
		
		
		return null;
	}
}
