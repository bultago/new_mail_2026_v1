package com.terracetech.tims.webmail.common.action;

import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.manager.CheckUserExistManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.ResponseUtil;

@SuppressWarnings("all")
public class CheckSessionTimeoutAction extends BaseAction {

	private static final long serialVersionUID = 8964319326324533330L;
	private CheckUserExistManager checkUserExistManager = null;
	public void setCheckUserExistManager(
			CheckUserExistManager checkUserExistManager) {
		this.checkUserExistManager = checkUserExistManager;
	}
	
	public CheckSessionTimeoutAction(boolean authCheck) {
		setAuthCheck(authCheck);
	}
	
	
	public String execute() throws Exception{
		long stimePeriod = Long.parseLong(user.get(User.SESSION_CHECK_TIME));
		JSONObject jObj = new JSONObject();
		//HttpSession session = request.getSession(true);
		String currentCheckTime = FormatUtil.getBasicDateStr();
		// TCUSTOM-2063 2016-10-31 - session-timeout : 0 에서 Heap 에서 session clear 가 되지 않아 수정됨
		//String checkWorkTime = (String)session.getAttribute(EnvConstants.getBasicSetting("timeout.session.name"));
		String checkWorkTime = UserAuthManager.getSessionTime(request);
		
		long ctime = Long.parseLong(currentCheckTime);
		long wtime = (checkWorkTime != null)?Long.parseLong(checkWorkTime):0;		
		
		if(wtime != 0 && (ctime - wtime) >= (stimePeriod*60)){
			jObj.put("isOverSession", true);
		} else {
			jObj.put("isOverSession", false);
		}
		
		ResponseUtil.processResponse(response, jObj);
		return null;
	}
	
	public String health() throws Exception {
		JSONObject jObj = new JSONObject();
		boolean isDuplicateCheckOn = true;
		boolean isUserLogin = false;
		User user = UserAuthManager.getUser(request);
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		Map<String, Boolean> checkLoginUserList = checkUserExistManager.checkLoginUserList(user.get(User.EMAIL), user.get(User.LOGIN_TIMESTAMP),mailDomainSeq);
		
		if(checkLoginUserList.containsKey("isDuplicateCheckOn")){
			isDuplicateCheckOn = checkLoginUserList.get("isDuplicateCheckOn");
		}
		if(checkLoginUserList.containsKey("isUserLogin")){
			isUserLogin = checkLoginUserList.get("isUserLogin");
		}
		jObj.put("isDuplicateCheckOn", isDuplicateCheckOn);
		jObj.put("isUserLogin", isUserLogin);
		ResponseUtil.processResponse(response, jObj);
		return null;
	}

}
