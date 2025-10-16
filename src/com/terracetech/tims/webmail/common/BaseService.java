/**
 * BaseService.java 2008. 12. 15.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.common;

/**
 * <p><strong>BaseService.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import xecure.servlet.XecureConfig;
import xecure.servlet.XecureServlet;
import xecure.servlet.XecureServletConfigException;
import xecure.servlet.XecureServletException;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.exception.UserAuthException;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.SessionUtil;

public class BaseService {

	public HttpServletRequest request = null;
	public HttpServletResponse response = null;	
	public ServletContext context = null;	
	public User user = null;
	public String remoteIp = null;
	public Logger log = Logger.getLogger(this.getClass());
	
	public TMailStore getStore(TMailStoreFactory factory,String remodeAddr)throws Exception{
		 TMailStore store= factory.connect(remodeAddr, user);
		 store.getDefaultFolder();
		 return store;
	}
	

	public void loadHttpResource() throws UserAuthException{
		WebContext ctx = WebContextFactory.get();
				
		this.user = null;
		try {
			parseXcureWebRequest();
			this.user = UserAuthManager.getUser(ctx.getHttpServletRequest());
			
			if(this.user != null){
				// TCUSTOM-2063 2016-10-31 - session-timeout : 0 에서 Heap 에서 session clear 가 되지 않아 수정됨
				UserAuthManager.setSessionTime(ctx.getHttpServletResponse());
			}			
		} catch (Exception e) {
			throw new UserAuthException(e);
		}
		
		context = ctx.getServletContext();
		request = ctx.getHttpServletRequest();
		response = ctx.getHttpServletResponse();
		
		this.remoteIp = request.getRemoteAddr();
		
		Map<String, String> covertCookieMap = covertCookieMap(request.getCookies());
		if(covertCookieMap.containsKey("LOGINIP")){
			String loginIp = "";
			try {
				loginIp = URLEncoder.encode(request.getRemoteAddr(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if(!loginIp.equals(covertCookieMap.get("LOGINIP"))){
				throw new UserAuthException();
        	}
		}else{
			throw new UserAuthException();
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
		LogManager.writeMailLogMsg(isJobLog, log, user.get(User.EMAIL), 
				remoteIp, action, TMailUtility.IMAPFolderEncode(folder), toAddrs, 
				user.get(User.EMAIL), mailSize, 
				EnvConstants.DEFAULT_CHARSET, 
				subject,mid);	
	}
	
	public void writeMailLog(boolean isJobLog, String action, String toFolder, 
			String fromFolder, String uid){		
		fromFolder = (fromFolder != null)?fromFolder:"";
		toFolder = (toFolder != null)?toFolder:"";
		LogManager.writeMailLogMsg(isJobLog, log, user.get(User.EMAIL), 
				remoteIp, action, 
				TMailUtility.IMAPFolderEncode(toFolder.replaceAll(" ", "/")), 
				TMailUtility.IMAPFolderEncode(fromFolder.replaceAll(" ", "/")),uid);	
	}
	
	public void writeMailLog(boolean isJobLog, String action, String toFolder, 
			String fromFolder, String toAddrs, String uid){
		fromFolder = (fromFolder != null)?fromFolder:"";
		toFolder = (toFolder != null)?toFolder:"";
		LogManager.writeMailLogMsg(isJobLog, log, user.get(User.EMAIL), 
				remoteIp, action, toAddrs, user.get(User.EMAIL), 
				TMailUtility.IMAPFolderEncode(toFolder.replaceAll(" ", "/")), 
				TMailUtility.IMAPFolderEncode(fromFolder.replaceAll(" ", "/")),uid ,"");	
	}
	
	
	
	public void writeMailLog(boolean isJobLog, String action){
		LogManager.writeMailLogMsg(isJobLog,log, user.get(User.EMAIL), 
				remoteIp, action);	
	}
	
	private void parseXcureWebRequest() throws Exception{
		if(ExtPartConstants.isXecureWebUse()){
			String qValue = request.getParameter("q");		
			if (qValue != null && !"".equals(qValue)){
				try {
					XecureServlet xecure = new XecureServlet(new XecureConfig(), request, response);	
					request = xecure.request;
					response = xecure.response;
				} catch (XecureServletException e) {
					throw new Exception("XecureServlet ERROR!!",e); 
				} catch (XecureServletConfigException e) {
					throw new Exception("XecureServletConfig ERROR!!",e);
				}
			}
		}
	}
}
