package com.terracetech.tims.webmail.scheduler.action;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.exception.UserAuthException;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.action.AutoLoginAction;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;

public class SchedulerOutlookSsoAction extends SchedulerOutlookBaseAction {

	private static final long serialVersionUID = 20091218L;

	public String execute() throws Exception {
		
		I18nResources resource = new I18nResources(I18nConstants.getBundleUserLocale(request), "common");
		String returnPass = "success";
		String msg = "";
		
		try {
			if (!isCheckAuth()) {
				throw new UserAuthException();
			}
			String result = "success";
			User user = UserAuthManager.getUser(request);
			if(user == null || getUserId().equals(user.get(User.MAIL_UID))){
				AutoLoginAction loginProcess = new AutoLoginAction();
				result = loginProcess.execute(request, response, getUserId(), null, getMailDomain());
			}
			if (!"success".equals(result)) {
				msg = resource.getMessage(result);
				returnPass = "fail";
			}
		}catch (UserAuthException e) {
			msg = resource.getMessage("auth.fail");
			returnPass = "fail";
		}catch (Exception e) {
			msg = resource.getMessage("error.msg.001");
			returnPass = "fail";
		}
		
		request.setAttribute("url", "/dynamic/scheduler/schedulerCommon.do");
		request.setAttribute("errMsg", msg);
		request.setAttribute("actionType", "close");
		
		return returnPass;
	}
}
