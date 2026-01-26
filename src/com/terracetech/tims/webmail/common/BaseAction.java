/**
 * BaseAction.java 2008. 12. 8.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.common;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.simple.JSONObject;
import org.springframework.mail.MailAuthenticationException;

// Xecure 보안 모듈 - 더 이상 사용 안함 (2025-10-23)
// import xecure.servlet.XecureConfig;
// import xecure.servlet.XecureServlet;
// import xecure.servlet.XecureServletConfigException;
// import xecure.servlet.XecureServletException;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.common.vo.MailConfigVO;
import com.terracetech.tims.webmail.home.manager.MailHomeManager;
import com.terracetech.tims.webmail.home.vo.MailMenuLayoutVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p>
 * <strong>BaseAction.java</strong> Class Description
 * </p>
 * <p>
 * �ֿ伳��
 * </p>
 * <ul>
 * <li>Action�� ���� �޼ҵ���� ���� �س��� Ŭ���� </li>
 * </ul>
 * 
 * @author sshyun
 * @since Tims7
 * @version 7.0
 */
public class BaseAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware, Preparable {	
	
	private static final long serialVersionUID = -7583020235241749743L;
	
	public Logger log = LoggerFactory.getLogger(this.getClass());
	
	public HttpServletRequest request = null;
	public ServletContext context = null;
	public HttpServletResponse response = null;
	public User user = null;
	public String remoteIp = null;
	public MailMenuLayoutVO[] topMenus = null;
	
	private boolean authCheck = true;
	
	private String paid = null;
	
	/**
	 * <p>Request���� ����</p>
	 *
	 * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
	 * @param arg0 
	 */
	public void setServletRequest(HttpServletRequest request) {
		if (!(request instanceof MultiPartRequestWrapper)) {
			HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request) {
				private final String EVENTS = "(?i)onload|onunload|onchange|onsubmit|onreset|onerror"
						+ "|onselect|onblur|onfocus|onkeydown|onkeypress|onkeyup"
						+ "|onclick|ondblclick|onmousedown|onmousemove|onmouseout|onmouseover|onmouseup";
				private final String XSS_REGEX_ONEVENT = "(" + EVENTS + ".*)=s*(\"|')[^\"|']*(\"|')";
				private final String XSS_REGEX_ONEVENT_OTHER = "(" + EVENTS + ".*)";
				private final String XSS_REGEX_SCRIPT = "<(\\/?)(script)([^<>]*)>";

				public String getParameter(String name) {
					String value = super.getParameter(name);
					value = filterParamString(value);
					return value;
				}

				@SuppressWarnings("unchecked")
				public Map<String, String[]> getParameterMap() {
					Map<String, String[]> rawMap = super.getParameterMap();
					Map<String, String[]> filteredMap = new HashMap<String, String[]>(rawMap.size());
					Set<String> keys = rawMap.keySet();
					for (String key : keys) {
						String[] rawValue = rawMap.get(key);
						String[] filteredValue = filterStringArray(rawValue);
						filteredMap.put(key, filteredValue);
					}
					return filteredMap;
				}

				protected String[] filterStringArray(String[] rawValue) {
					String[] filteredArray = new String[rawValue.length];
					for (int i = 0; i < rawValue.length; i++) {
						filteredArray[i] = filterParamString(rawValue[i]);
					}
					return filteredArray;
				}

				public String[] getParameterValues(String name) {
					String[] rawValues = super.getParameterValues(name);
					if (rawValues == null)
						return null;
					String[] filteredValues = new String[rawValues.length];
					for (int i = 0; i < rawValues.length; i++) {
						filteredValues[i] = filterParamString(rawValues[i]);
					}
					return filteredValues;
				}

				protected String filterParamString(String rawValue) {
					if (rawValue == null) {
						return null;
					}
					rawValue = rawValue.replaceAll(XSS_REGEX_ONEVENT, "");
					rawValue = rawValue.replaceAll(XSS_REGEX_ONEVENT_OTHER, "");
					rawValue = rawValue.replaceAll(XSS_REGEX_SCRIPT, "");
					return rawValue;
				}
			};
			this.request = requestWrapper;
		} else {
			this.request = request;
		}
	}	
	/**
	 * <p>Response ���� ����</p>
	 *
	 * @see org.apache.struts2.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 * @param arg0 
	 */
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;		
	}
	
	public void prepare() throws Exception {		
		log.debug("BaseAction.prepare : " + this.getClass());
		parseXcureWebRequest();
		
		this.context = request.getSession().getServletContext();			
		try {
			log.debug("BaseAction.cookie : " + request.getSession().getAttribute(EnvConstants.COOKIE_ALGORITHM_METHOD) );
			this.user = UserAuthManager.getUser(request);
			this.remoteIp = request.getRemoteAddr();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		log.debug("BaseAction.authCheck : " + authCheck);
		if(authCheck){
			log.debug("BaseAction.user : " + user);
			if(this.user != null){
				HttpSession session = request.getSession();
				Map<String, String> covertCookieMap = covertCookieMap(request.getCookies());
				if(covertCookieMap.containsKey("LOGINIP")){
					String loginIp = URLEncoder.encode(request.getRemoteAddr(), "UTF-8");
					if(!loginIp.equals(covertCookieMap.get("LOGINIP"))){
						session.setAttribute("authStatus", "logout");
		        	}else{
		        		session.removeAttribute("authStatus");
		        	}
				}else{
					session.setAttribute("authStatus", "logout");
				}
				int sesstionCheckTime = Integer.parseInt(user.get(User.SESSION_CHECK_TIME));
				log.debug("BaseAction.sesstionCheckTime : " + sesstionCheckTime);
				if(sesstionCheckTime > 0){
					// TCUSTOM-2063 2016-10-31 - session-timeout : 0 에서 Heap 에서 session clear 가 되지 않아 수정됨
					UserAuthManager.setSessionTime(response);					
				}				
			} else {
				log.debug("BaseAction.NOT EXIST USER");
				throw new MailAuthenticationException("NOT EXIST USER");			
			}
		}
	}
	private Map<String,String> covertCookieMap(Cookie[] cookies){
		Map<String,String> returnCookieMap = new HashMap<String, String>();
		if (cookies != null) {
		    for (Cookie cookie : cookies) {
		    	returnCookieMap.put(cookie.getName(),cookie.getValue());
		    }
		}
		return returnCookieMap;
	}
	
	/**
	 * <p>�⺻ �޼��� ����� ��ȯ</p>
	 *
	 * @return I18nResources
	 */
	public I18nResources getMessageResource(){		
		return new I18nResources(I18nConstants.getBundleUserLocale(request));
	}
	
	/**
	 * <p>������ �޼��� ����� ��ȯ</p>
	 *
	 * @param bundle
	 * @return I18nResources
	 */
	public I18nResources getMessageResource(String bundle){
		return new I18nResources(I18nConstants.getBundleUserLocale(request), bundle);
	}
	
	public void writeMailLog(boolean isJobLog, String action, String folder, String toAddrs, 
			String fromAddrs, long mailSize, String subject, String mid){
		if(user != null){
			LogManager.writeMailLogMsg(isJobLog, log, user.get(User.EMAIL), 
					remoteIp, action, 
					TMailUtility.IMAPFolderEncode(folder.replaceAll(" ", "/")),
					toAddrs, 
					fromAddrs, mailSize, EnvConstants.DEFAULT_CHARSET, subject, mid);	
		}
			
	}
	
	public void writeMailLog(boolean isJobLog, String action, String toFolder, 
			String fromFolder, String uid){
		if(user != null){
			fromFolder = (fromFolder != null)?fromFolder:"";
			toFolder = (toFolder != null)?toFolder:"";
			LogManager.writeMailLogMsg(isJobLog, log, user.get(User.EMAIL),remoteIp, action, 
					TMailUtility.IMAPFolderEncode(toFolder.replaceAll(" ", "/")), 
					TMailUtility.IMAPFolderEncode(fromFolder.replaceAll(" ", "/")),uid);	
		}
			
	}
	
	public void writeMailLog(boolean isJobLog, String action){
		if(user != null){
			LogManager.writeMailLogMsg(isJobLog, log, user.get(User.EMAIL),remoteIp, action);	
		}
			
	}
	
	public void writeWebfolderLog(boolean isJobLog, String action, String fromFolder, String toFolder, String toAddrs, 
			String fromAddrs, long mailSize, String subject, String mid){
		if(user != null){
			LogManager.writeWebfolderLogMsg(isJobLog, log, user.get(User.EMAIL), 
					remoteIp, action, 
					TMailUtility.IMAPFolderEncode(fromFolder.replaceAll(" ", "/")),
					TMailUtility.IMAPFolderEncode(toFolder.replaceAll(" ", "/")),
					toAddrs, 
					fromAddrs, mailSize, EnvConstants.DEFAULT_CHARSET, subject, mid);	
		}
			
	}
	
	public void writeWebfolderLog(boolean isJobLog, String action, String toFolder, 
			String fromFolder, String uid){
		if(user != null){
			fromFolder = (fromFolder != null)?fromFolder:"";
			toFolder = (toFolder != null)?toFolder:"";
			LogManager.writeWebfolderLogMsg(isJobLog, log, user.get(User.EMAIL),remoteIp, action, 
					TMailUtility.IMAPFolderEncode(toFolder.replaceAll(" ", "/")), 
					TMailUtility.IMAPFolderEncode(fromFolder.replaceAll(" ", "/")),uid);	
		}
			
	}
	
	public void setAuthCheck(boolean authCheck){
		this.authCheck = authCheck;
	}

	public MailMenuLayoutVO[] getMenus() throws Exception{
		if(topMenus != null)
			return topMenus;
		
		User user = UserAuthManager.getUser(request);
		if(user !=null && topMenus == null){			
			int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
			int groupSeq = Integer.parseInt(user.get(User.MAIL_GROUP_SEQ));
			MailHomeManager homeManager = (MailHomeManager) ApplicationBeanUtil
					.getApplicationBean("homeManager");
			
			if(homeManager != null){
				Locale locale = new Locale(user.get(User.LOCALE));
				I18nResources resource = new I18nResources(locale,"common");
				
				topMenus = homeManager.readMenusList(resource, domainSeq, groupSeq);
				
				if (topMenus != null && topMenus.length > 0) {
					boolean orgInclude = false;
					for (int i=0; i<topMenus.length; i++) {
						if("org".equals(topMenus[i].getMenuId())) {
							orgInclude = true;
						}
						
						if ("mail".equals(topMenus[i].getMenuId())) {
							if (("html").equals(user.get(User.RENDER_MODE))) {
								topMenus[i].setMenuUrl("/mail/listMessage.do");
							}
						}
					}
					if (orgInclude && !getOrgUse()) {
						MailMenuLayoutVO[] newMenus = new MailMenuLayoutVO[topMenus.length-1];
						int count=0;
						for (int i=0; i<topMenus.length; i++) {
							if("org".equals(topMenus[i].getMenuId())) {
								continue;
							} else {
								newMenus[count++] = topMenus[i];
							}
						}
						topMenus = newMenus;
					}
				}
				
				request.setAttribute("menus", topMenus);
			}	
		}
		
		return topMenus;
	}
	
	public String getMenuStatus() throws Exception{
		getMenus();
		return(topMenus != null)?readMenusStatusJson(topMenus):"{}";
	}
	
	@SuppressWarnings("unchecked")
	private String readMenusStatusJson(MailMenuLayoutVO[] menus){
		JSONObject json = new JSONObject();		
		if(menus != null){
			for (MailMenuLayoutVO mailMenuLayoutVO : menus) {
				json.put(mailMenuLayoutVO.getMenuId(), mailMenuLayoutVO.getMenuApply());				
			}
		}		
		return json.toString();
	}
	
	public String getSkin() throws Exception {
		String skin = "default";
		User user = UserAuthManager.getUser(request);
		if(user != null){
			int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
			SystemConfigManager systemConfigManager = (SystemConfigManager) ApplicationBeanUtil.getApplicationBean("systemConfigManager");
			skin = systemConfigManager.readSkin(domainSeq);
		}

		return skin;
	}
	
	public boolean getOrgUse() throws Exception {
		SystemConfigManager systemConfigManager = (SystemConfigManager) ApplicationBeanUtil.getApplicationBean("systemConfigManager");
		MailConfigVO orgInfo = systemConfigManager.getMailConfig("org");
		boolean orgUse = false;
		if (orgInfo != null && "enabled".equalsIgnoreCase(orgInfo.getConfigValue())) {
			orgUse = true;
		}
		
		return orgUse;
	}
	
	private void parseXcureWebRequest() throws Exception{
		// Xecure 보안 모듈 - 더 이상 사용 안함 (2025-10-23)
		// if(ExtPartConstants.isXecureWebUse()){
		// 	String qValue = request.getParameter("q");			
		// 	if (qValue != null && !"".equals(qValue)){
		// 		try {
		// 			XecureServlet xecure = new XecureServlet(new XecureConfig(), request, response);
		// 			request = xecure.request;					
		// 			response = xecure.response;					
		// 		} catch (XecureServletException e) {
		// 			throw new Exception("XecureServlet ERROR!!",e); 
		// 		} catch (XecureServletConfigException e) {
		// 			throw new Exception("XecureServletConfig ERROR!!",e);
		// 		}
		// 	}
		// }
	}
	
	public HttpServletRequest getRequest(){
		return request;
	}
	public void setPaid(String paid) {
		this.paid = paid;
	}
	public boolean checkPAID() {
	        boolean isSuccess = false;
	        if (StringUtils.isEmpty(this.paid)) {
	            return false;
	        }
	        if (this.paid.equals(request.getSession().getId())) {
	            isSuccess = true;
	        }
	        return isSuccess;
	}
	//모바일과 웹에 공통 URL인 경우
	public boolean checkPAIDAndMobile() {
		boolean isSuccess = false;
		if (StringUtils.isEmpty(this.paid)) {
			return true;
		}
		if (this.paid.equals(request.getSession().getId())) {
			isSuccess = true;
		}
		return isSuccess;
	}
}
