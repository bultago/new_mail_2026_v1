package com.terracetech.tims.webmail.scheduler.action;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;

public class SchedulerOutlookBaseAction extends ActionSupport implements
ServletRequestAware, ServletResponseAware, Preparable{

	private static final long serialVersionUID = 20091215L;
	public HttpServletRequest request = null;
	public HttpServletResponse response = null;
	
	public Logger log = Logger.getLogger(this.getClass());
	
	private MailUserManager mailUserManager = null;
	
	private String authKey = null;
	private int mailDomainSeq = 0;
	private int mailUserSeq = 0;
	private String mailDomain = null;
	private String userId = null;
	
	private boolean checkAuth = false;

	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}

	public void prepare() throws Exception {
		StringTokenizer st = null;
		try {
			authKey = request.getParameter("authKey");
			String decriptKey = SecureUtil.decrypt(SymmetricCrypt.AES, "terrace-12345678", authKey);
			if (decriptKey.indexOf("@") != -1) {
				st = new StringTokenizer(decriptKey, "@");
				if (st.countTokens() == 2) {
					userId = st.nextToken();
					mailDomain = st.nextToken();
					
					mailDomainSeq = mailUserManager.searchMailDomainSeq(mailDomain);
					mailUserSeq = mailUserManager.readUserSeq(userId, mailDomainSeq);
					
					checkAuth = true;
				}
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;	
	}
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;		
	}

	public int getMailDomainSeq() {
		return mailDomainSeq;
	}

	public int getMailUserSeq() {
		return mailUserSeq;
	}

	public String getMailDomain() {
		return mailDomain;
	}

	public String getUserId() {
		return userId;
	}

	public boolean isCheckAuth() {
		return checkAuth;
	}

}
