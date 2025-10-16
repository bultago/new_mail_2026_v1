package com.terracetech.tims.hybrid.common.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.hybrid.common.exception.HybridAuthException;
import com.terracetech.tims.hybrid.common.manager.HybridAuthManager;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.common.vo.MailConfigVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware, Preparable {

    public Logger log = Logger.getLogger(this.getClass());
    public HttpServletRequest request = null;
    public ServletContext context = null;
    public HttpServletResponse response = null;
    public String remoteIp = null;
    public User user = null;
    public String authKey = null;
    private boolean authCheck = true;
    
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }
    
    public HttpServletRequest getRequest() {
        return request;
    }

    public void prepare() throws Exception {
        log.debug("HybridBaseAction.prepare : " + this.getClass());

        this.context = request.getSession().getServletContext();
        try {
            this.remoteIp = request.getRemoteAddr();
            if (authCheck) {
                authKey = request.getParameter("authKey");
                if (StringUtils.isEmpty(authKey)) {
                    throw new HybridAuthException();
                }
                user = HybridAuthManager.getUser(authKey);
                if (user == null) {
                    throw new HybridAuthException();
                }
                
                if (!HybridAuthManager.checkMobieAccess(user)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            }
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
    public void setAuthCheck(boolean authCheck) {
        this.authCheck = authCheck;
    }
    /**
     * <p>
     * �⺻ �޼��� ����� ��ȯ
     * </p>
     * 
     * @return I18nResources
     */
    public I18nResources getMessageResource() {
        return new I18nResources(I18nConstants.getBundleUserLocale(request));
    }

    /**
     * <p>
     * ������ �޼��� ����� ��ȯ
     * </p>
     * 
     * @param bundle
     * @return I18nResources
     */
    public I18nResources getMessageResource(String bundle) {
        return new I18nResources(I18nConstants.getBundleUserLocale(request), bundle);
    }
    
    public String readMailNotiMode() throws Exception {
        return "link";
    }

}
