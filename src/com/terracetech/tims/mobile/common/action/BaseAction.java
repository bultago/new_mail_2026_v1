package com.terracetech.tims.mobile.common.action;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.mail.MailAuthenticationException;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.hybrid.common.manager.HybridAuthManager;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.home.manager.MailHomeManager;
import com.terracetech.tims.webmail.home.vo.MailMenuLayoutVO;
import com.terracetech.tims.webmail.mail.ibean.MailFolderBean;
import com.terracetech.tims.webmail.mail.ibean.MailQuotaBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.AccessCheckUtil;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class BaseAction extends ActionSupport implements
ServletRequestAware, ServletResponseAware, Preparable {	

	private static final long serialVersionUID = 201002250947L;
	public static final int ONLY_FOLDER = 1;
	public static final int ONLY_QUOTA = 2;
	public static final int FOLDER_AND_QUOTA = 3;
	
	public Logger log = LoggerFactory.getLogger(this.getClass());
	
	public HttpServletRequest request = null;
	public ServletContext context = null;
	public HttpServletResponse response = null;
	public User user = null;
	public String remoteIp = null;
	
	public MailMenuLayoutVO[] topMenus = null;
	
	private boolean authCheck = true;
	
	/**
	* <p>Request���� ����</p>
	*
	* @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
	* @param arg0 
	*/
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;	
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
	
	public HttpServletRequest getRequest(){
		return request;
	}
	
	public void prepare() throws Exception {		
		log.debug("BaseAction.prepare : " + this.getClass());		
		
		this.context = request.getSession().getServletContext();
		String authKey = null;
		try {
		        log.debug("BaseAction.cookie : " + request.getSession().getAttribute(EnvConstants.COOKIE_ALGORITHM_METHOD) );
			String url = request.getRequestURI();
			if (!AccessCheckUtil.isHybridUrl(url)) {
			    HybridAuthManager.deleteMobileCookie(request, response);
			}
			authKey = HybridAuthManager.checkMobileSso(request);
		            
			if (StringUtils.isNotEmpty(authKey)) {
			    this.user = HybridAuthManager.getUser(authKey);
			    if (this.user == null) {
			        this.user = UserAuthManager.getUser(request);
			    }
			} else {
			    this.user = UserAuthManager.getUser(request);                
			}
			this.remoteIp = request.getRemoteAddr();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		log.debug("BaseAction.authCheck : " + authCheck);
		if(authCheck){
			log.debug("BaseAction.user : " + user);
			if(this.user == null){
				log.debug("BaseAction.NOT EXIST USER");
				throw new MailAuthenticationException("NOT EXIST USER");	
			}
			
			if (StringUtils.isNotEmpty(authKey) && !HybridAuthManager.checkMobieAccess(user)) {
			    response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		            
			if (StringUtils.isNotEmpty(authKey)) {
			    request.setAttribute("isHybrid", true);
			}
		}
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
	
	
	public void setAuthCheck(boolean authCheck){
		this.authCheck = authCheck;
	}	
	
	
	public void makeFolderInfo(int typeNum) throws Exception {
		if(user != null){
			TMailStoreFactory factory = new TMailStoreFactory();
			TMailStore store = null;
			try {
				store = factory.connect(remoteIp, user);

				int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
				MailManager mailManager = (MailManager)ApplicationBeanUtil.getApplicationBean("mailManager");
				mailManager.setProcessResource(store, new I18nResources(new Locale(user.get(User.LOCALE))));
				
				if (typeNum == ONLY_FOLDER || typeNum == FOLDER_AND_QUOTA) {
					MailFolderBean[] defaultFolders = mailManager.getFolderList(EnvConstants.DEFAULT_FOLDER, false, mailUserSeq);
					MailFolderBean[] userFolders = mailManager.getFolderList(EnvConstants.USER_FOLDER, false, mailUserSeq);
					request.setAttribute("defaultFolders", defaultFolders);
					request.setAttribute("userFolders", userFolders);
				}
				if (typeNum == ONLY_QUOTA || typeNum == FOLDER_AND_QUOTA) {
					MailQuotaBean mailQuota = mailManager.getQuotaRootInfo(FolderHandler.INBOX);
					request.setAttribute("mailQuota", mailQuota);
				}
			}catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				try {
					if(store !=null && store.isConnected()) {
						store.close();
					}
				} catch (MessagingException ignore) {
					LogManager.writeErr(this, ignore.getMessage(), ignore);
				}
			}
		}
		else{
			log.debug("BaseAction.NOT EXIST USER");
			throw new MailAuthenticationException("NOT EXIST USER");	
		}
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
                            
                            request.setAttribute("menus", topMenus);
                    }       
            }
            
            return topMenus;
        }
        
        public void getMenuStatus() throws Exception{
                getMenus();
                readMenusStatusJson(topMenus);
        }
        @SuppressWarnings("unchecked")
        private void readMenusStatusJson(MailMenuLayoutVO[] menus){
            request.setAttribute("mailUse", "");
            request.setAttribute("addrUse", "");
            request.setAttribute("calendarUse", "");
            request.setAttribute("bbsUse", "");
                if(menus != null){
                        for (MailMenuLayoutVO mailMenuLayoutVO : menus) {
                            if("mail".equals(mailMenuLayoutVO.getMenuId())){
                                request.setAttribute("mailUse", "on");
                                continue;
                            }
                            if("addr".equals(mailMenuLayoutVO.getMenuId())){
                                request.setAttribute("addrUse", "on");
                                continue;
                            }
                            if("calendar".equals(mailMenuLayoutVO.getMenuId())){
                                request.setAttribute("calendarUse", "on");
                                continue;
                            }
                            if("bbs".equals(mailMenuLayoutVO.getMenuId())){
                                request.setAttribute("bbsUse", "on");
                                continue;
                            }                                                                
                        }
                }              
        }

}