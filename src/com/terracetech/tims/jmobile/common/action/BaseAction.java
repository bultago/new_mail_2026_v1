package com.terracetech.tims.jmobile.common.action;

import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.mail.MailAuthenticationException;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.jmobile.common.manager.UserAuthManager;
import com.terracetech.tims.jmobile.common.vo.PreworkJobBean;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mail.ibean.MailFolderBean;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;

public class BaseAction extends ActionSupport implements
ServletRequestAware, ServletResponseAware, Preparable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3333583830866247967L;

    public Logger log = LoggerFactory.getLogger(this.getClass());
	
	public HttpServletRequest request = null;
	public ServletContext context = null;
	public HttpServletResponse response = null;
	public User user = null;
	public String remoteIp = null;
	public int MAIL_CONTENTS_SIZE = 6144;
	
	private boolean authCheck = true;
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;	
	}
	
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;		
	}
	
	public HttpServletRequest getRequest(){
		return request;
	}
	
	public void setAuthCheck(boolean authCheck){
		this.authCheck = authCheck;
	}
	
	public I18nResources getMessageResource(String bundle){
		String locale = "jp";
		if(user != null){
			locale = user.get(User.LOCALE);
		}
		return getMeI18nResources(locale,bundle);
	}
	
	public I18nResources getMeI18nResources(String locale, String bundle){
		return new I18nResources(new Locale(locale), bundle);
	}
	
	public void prepare() throws Exception {		
		log.debug("SimpleBaseAction.prepare : " + this.getClass());		
		
		this.context = request.getSession().getServletContext();		
		try {
			log.debug("SimpleBaseAction.session : " + request.getSession().getAttribute(EnvConstants.COOKIE_ALGORITHM_METHOD) );
			this.user = UserAuthManager.getUser(request);
			this.remoteIp = request.getRemoteAddr();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		log.debug("SimpleBaseAction.authCheck : " + authCheck);
		if(authCheck){
			log.debug("SimpleBaseAction.user : " + user);
			if(this.user == null){
				log.debug("SimpleBaseAction.NOT EXIST USER");
				throw new MailAuthenticationException("NOT EXIST USER");	
			}
		}
	}
	
	public void makeFolderInfo() throws Exception {
		if(user != null){
			TMailStoreFactory factory = new TMailStoreFactory();
			TMailStore store = null;
			try {
				store = factory.connect(remoteIp, user);

				int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
				MailManager mailManager = (MailManager)ApplicationBeanUtil.getApplicationBean("mailManager");
				mailManager.setProcessResource(store, new I18nResources(new Locale(user.get(User.LOCALE))));
				
				MailFolderBean[] defaultFolders = mailManager.getFolderList(EnvConstants.DEFAULT_FOLDER, false, mailUserSeq);
				MailFolderBean[] userFolders = mailManager.getFolderList(EnvConstants.USER_FOLDER, false, mailUserSeq);
				request.setAttribute("defaultFolders", defaultFolders);
				request.setAttribute("userFolders", userFolders);
			}catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				try {
					if(store !=null && store.isConnected()) {
						store.close();
					}
				} catch (MessagingException ignore) {}
			}
		}
		else{
			log.debug("BaseAction.NOT EXIST USER");
			throw new MailAuthenticationException("NOT EXIST USER");	
		}
	}
	
	@SuppressWarnings("all")
	public void savePreAction(String action, Map paramMap){
		Object o = request.getSession().getAttribute("jobStack");
		Stack jobStack = null;
		if(o != null){
			jobStack = (Stack)o;
			if(jobStack.size() > 0){
				PreworkJobBean preJob = (PreworkJobBean)jobStack.pop();
				if(!action.equals(preJob.getActionName()))jobStack.push(preJob);				
			}
		} else {
			jobStack = new Stack();			
		}
		jobStack.push(new PreworkJobBean(action, paramMap));
		request.getSession().setAttribute("jobStack", jobStack);
	}
	
	public void saveCurrentAction(String actionName){
		request.getSession().setAttribute("currentJob",actionName);
	}
}
