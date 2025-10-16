package com.terracetech.tims.webmail.mailuser.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.MakeMessage;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;

public class LoginByEmpnoAction extends BaseAction {
	public Logger log = Logger.getLogger(this.getClass());
	
	private static final long serialVersionUID = 3524773920732540358L;
	
	private final static String DEFAULT_RETURN_PATH = "/common/welcome.do";
	private String returnPath = DEFAULT_RETURN_PATH;	
	
	private UserAuthManager userAuthManager = null;
	private SystemConfigManager systemManager = null;
	
	String result = "fail";
	String returnMessage = "success";
	
	public LoginByEmpnoAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public String execute() throws Exception {		
		I18nResources resource = getMessageResource("common");
		// TODO 추후 리펙토링
		String empno = request.getParameter("empno");
		String pass = request.getParameter("pass");
		String domain = request.getParameter("domain");
		String attachPath = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
		String strLocalhost = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("attachPath", attachPath);
		paramMap.put("attachUrl", EnvConstants.getAttachSetting("attach.url"));
		paramMap.put("localUrl", strLocalhost);
		
		String sessionTime = systemManager.getSessionTimeConfig();
		request.getSession().setAttribute(EnvConstants.COOKIE_ALGORITHM_METHOD, systemManager.getCryptMethod());
    	paramMap.put("cookieAlgorithm", systemManager.getCryptMethod());
		
		AuthUser auth= userAuthManager.validateUserByEmpno(request.getSession(), empno, pass, domain);
		if (auth.isAuthSuccess()) {
			User allowUser = auth.getUser();
			allowUser.put(User.SESSION_CHECK_TIME, sessionTime);
			userAuthManager.doLoginProcess(request, response, allowUser,paramMap);
			
			request.setAttribute("user", allowUser);//변수 상수화
			
			LogManager.writeMailLogMsg(true,log, allowUser.get(User.EMAIL), 
					request.getRemoteAddr(), "login");
			// TODO 로그 구현
		} else {
			if(auth.getAuthResult()==UserAuthManager.PASSWORD_CHANGE){
				User allowUser = auth.getUser();
				request.setAttribute("userName", allowUser.get(User.USER_NAME));
				request.setAttribute("domainSeq", allowUser.get(User.MAIL_DOMAIN_SEQ));
				request.setAttribute("userSeq", allowUser.get(User.MAIL_USER_SEQ));
				return "change";
			}
			
			String failMsg = userAuthManager.getFailProperty(auth.getAuthResult());
			returnMessage = "fail," + failMsg;
			response.setHeader("Content-Type", "text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();			
			String msg = resource.getMessage(failMsg);
		    out.print(MakeMessage.printAlertUrl(msg, returnPath));
		    out.flush();
		    
		    return null;
			
			// TODO 로그 구현
		}
		System.out.println(auth);
		result = returnMessage;
		returnPath = "success";
		return returnPath;
	}

	public String getResult() {
		return result;
	}
	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}
	
}