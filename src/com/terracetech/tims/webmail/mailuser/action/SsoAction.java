package com.terracetech.tims.webmail.mailuser.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.manager.CheckUserExistManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.exception.UnSupportedSSOException;
import com.terracetech.tims.webmail.exception.UserNotFoundException;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.mailuser.sso.SsoManager;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * http://localhost:8080/common/sso.do?d=1&ssoParam=mailUid=jpjung,domain=terracetech.com
 * http://localhost:8080/common/sso.do?d=1&ssoParam=ssn=1075319,domain=terracetech.com
 * http://localhost:8080/common/sso.do?d=1&ssoParam=MURINW1kakRFZ1d5Q05GeWFDZU04TVRrVmNmc0RjNGhwREo1K2t0NlF3T2lMb0Q5bjJjZDhSVlN6RnNZWUhPbQo=
 * @author waitone
 *
 */
public class SsoAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {

	private static final long serialVersionUID = 3524773920732540358L;
	
	public Logger log = Logger.getLogger(this.getClass());
	
	private final static String DEFAULT_RETURN_PATH = "/welcome.do";
	private String returnPath = DEFAULT_RETURN_PATH;
	
	protected HttpServletRequest request = null;
	protected HttpServletResponse response = null;
	protected UserAuthManager userAuthManager = null;
	protected SsoManager ssoManager = null;
	protected SystemConfigManager systemManager = null;
	private CheckUserExistManager checkUserExistManager = null;
	String result = "fail";
	String returnMessage = "success sso";
	
	private String url;
	
	public String execute() throws Exception {
		
		ServletContext context = request.getSession().getServletContext();
		HttpSession session = request.getSession();
		AuthUser auth = null;
		try {
			auth= ssoManager.validate(request);	
		}catch (UnSupportedSSOException e) {
			log.warn(e.getMessage(), e);
			String failMsg = userAuthManager.getFailProperty(UserAuthManager.WEBMAIL_SERVICE_FAIL);
			returnMessage = "fail," + failMsg;
			return "success";
		}catch (UserNotFoundException e) {
			log.error(e.getMessage(), e);
			String failMsg = userAuthManager.getFailProperty(UserAuthManager.NOT_FOUND);
			returnMessage = "fail," + failMsg;
			return "success";
		}
		
		String attachPath = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
		String strLocalhost = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
		String accoutStatus = auth.getUser().get(User.ACCOUNT_STATUS);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("attachPath", attachPath);
		paramMap.put("attachUrl", EnvConstants.getAttachSetting("attach.url"));
		paramMap.put("localUrl", strLocalhost);
		paramMap.put("cookieAlgorithm", systemManager.getCryptMethod());
		
		log.debug("SsoAction.attachPath : "+ attachPath);
		log.debug("SsoAction.attachUrl : "+ EnvConstants.getAttachSetting("attach.url"));
		log.debug("SsoAction.localUrl : "+ strLocalhost);
		log.debug("SsoAction.cookieAlgorithm : "+ systemManager.getCryptMethod());
		
		if (auth.isAuthSuccess() || auth.getAuthResult()==UserAuthManager.PASSWORD_CHANGE) {
			log.debug("SsoAction.auth.isAuthSuccess() : ");
			String sessionTime = systemManager.getSessionTimeConfig();
			auth.getUser().put(User.SESSION_CHECK_TIME, sessionTime);
			int mailDomainSeq = Integer.parseInt(auth.getUser().get(User.MAIL_DOMAIN_SEQ));
			
			//휴면계정 처리로직
			if ("dormant".equalsIgnoreCase(accoutStatus)) {
				Map dormantMap = userAuthManager.readDormantMonth(mailDomainSeq);    				
				if (dormantMap != null) {
					request.getSession().setAttribute("dormantUser", auth);
					String manage = (String)dormantMap.get("long_term_manage");
					String month = (String)dormantMap.get("long_term_month");
					String changeMonth = (String)dormantMap.get("long_term_change_month");
					request.setAttribute("manage", manage);
					request.setAttribute("month", month);
					request.setAttribute("changeMonth", changeMonth);
				}
				return "dormant";
			}
			
			//duplication login check
			String timestamp = checkUserExistManager.dupCheckProcess(auth.getUser().get(User.EMAIL),mailDomainSeq);
			auth.getUser().put(User.LOGIN_TIMESTAMP, timestamp);
			
			userAuthManager.doLoginProcess(request, response, auth.getUser(),paramMap);
			session.setAttribute(EnvConstants.COOKIE_ALGORITHM_METHOD, systemManager.getCryptMethod());
			request.setAttribute("user", auth.getUser());//���� ���ȭ			
			session.setAttribute("SSO_AUTH", "T");
			Map<String, String> menuMap = systemManager.getTopAndLeftMenuUse(
											Integer.parseInt(auth.getUser().get(User.MAIL_DOMAIN_SEQ)));
			session.setAttribute("topMenuUse", menuMap.get("ssotopmenu"));
			session.setAttribute("leftMenuUse", menuMap.get("leftmenu"));
			
			//패스워드 변경 주기 도달시 처리
			if(auth.getAuthResult()==UserAuthManager.PASSWORD_CHANGE){
				User allowUser = auth.getUser();
				String userName = allowUser.get(User.USER_NAME);
				request.setAttribute("mailDomainSeq", allowUser.get(User.MAIL_DOMAIN_SEQ));
				request.setAttribute("mailUserSeq", allowUser.get(User.MAIL_USER_SEQ));
				request.setAttribute("userId", allowUser.get(User.MAIL_UID));
				request.setAttribute("userName", userName);			
//				request.setAttribute("user", allowUser);
				return "change";
			}
		} else {
			String failMsg = userAuthManager.getFailProperty(auth.getAuthResult());
			returnMessage = "fail," + failMsg;	
			log.debug("SsoAction.auth.isAuthFailed() : "+ failMsg);
		}
		result = returnMessage;
		if(StringUtils.isNotEmpty(request.getParameter("mailbox"))){
			log.debug("SsoAction.mailbox : "+ request.getParameter("mailbox"));
			request.setAttribute("url", "/dynamic/mail/mailCommon.do?workName=golist&folder="+request.getParameter("mailbox"));
			returnPath = "mail";
		}else if(StringUtils.isNotEmpty(request.getParameter("mode"))){
			log.debug("SsoAction.mode : "+ request.getParameter("mode"));
			if("write".equalsIgnoreCase(request.getParameter("mode"))){
				if(StringUtils.isNotEmpty(request.getParameter("to"))){
					request.setAttribute("url", "/dynamic/mail/writeMessage.do?wmode=popup&wtype=normal&to="+request.getParameter("to"));
				}else{
					request.setAttribute("url", "/dynamic/mail/writeMessage.do?wmode=popup&wtype=normal");	
				}
			}else if("read".equalsIgnoreCase(request.getParameter("mode"))){
				request.setAttribute("url", "/mail/readSimpleMessage.do?readType=pop&folder="+request.getParameter("folder")+"&uid="+request.getParameter("uid"));
			}else if("webfolder".equalsIgnoreCase(request.getParameter("mode"))){
				request.setAttribute("url", "/webfolder/webfolder.do");	
			}else if("nomenu".equalsIgnoreCase(request.getParameter("mode"))){
				request.setAttribute("url", "/common/welcome.do?tmenu=disable");	
			}else if("empty".equalsIgnoreCase(request.getParameter("mode"))){
				return null;	
			}
			
			returnPath = "mail";
		}else if(StringUtils.isNotEmpty(request.getParameter("url"))){
			log.debug("SsoAction.url : "+ request.getParameter("url"));
			request.setAttribute("url", StringUtils.replace(request.getParameter("url"), ",", "&"));
			returnPath = "forward";
		}else{
			returnPath = "success";
		}
		
		
		return returnPath;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getResult() {
		return result;
	}
	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}

	public void setSsoManager(SsoManager ssoManager) {
		this.ssoManager = ssoManager;
	}
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}
	public void setCheckUserExistManager(
			CheckUserExistManager checkUserExistManager) {
		this.checkUserExistManager = checkUserExistManager;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}