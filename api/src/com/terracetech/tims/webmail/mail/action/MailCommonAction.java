/**
 * MailAction.java 2008. 12. 8.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.action;

import java.util.Iterator;
import com.terracetech.tims.webmail.common.log.LogManager;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;

/**
 * <p><strong>MailAction.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MailCommonAction extends BaseAction{


	private static final long serialVersionUID = -7846471574203478641L;
	
	private SystemConfigManager systemConfigManager = null;	
	private UserAuthManager userAuthManager = null;
	private SettingManager userSettingManager = null;
	
	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}
	
	public void setSystemConfigManager(SystemConfigManager systemConfigManager) {
		this.systemConfigManager = systemConfigManager;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	
	public void setUserSettingManager(SettingManager userSettingManager) {
		this.userSettingManager = userSettingManager;
	}

	/* (non-Javadoc)
	 * @see com.terracetech.tims.webmail.mail.action.IHaveTopmenu#loadPage()
	 */
	public String loadAjaxPage() throws Exception {		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int groupSeq = Integer.parseInt(user.get(User.MAIL_GROUP_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String email = user.get(User.EMAIL);
		String vMode = request.getParameter("vmode");
		if (!(("mhome".equalsIgnoreCase(vMode)) || ("intro".equalsIgnoreCase(vMode)) || ("domain".equalsIgnoreCase(vMode)))) {
		    vMode = null;
        }

		vMode = (vMode != null)?vMode:"mwork";
		
		String workName = request.getParameter("workName");
		if (!(("list".equalsIgnoreCase(workName)) || ("golist".equalsIgnoreCase(workName)) || ("golistparam".equalsIgnoreCase(workName)) || ("write".equalsIgnoreCase(workName)) || ("unseen".equalsIgnoreCase(workName)))) {
                    workName = null;
                }
		workName = (workName != null)?workName:"list";		
		
		if ("domain".equalsIgnoreCase(vMode)) {		   
		    vMode = systemConfigManager.readDomainAfterLogin(domainSeq);
		    if ("mail".equalsIgnoreCase(vMode)) {		        
			vMode = "mwork";			
		    } else if("manage".equalsIgnoreCase(vMode)) {
		        return "manage";
		    }
		}
		
		Map map = request.getParameterMap();
		Iterator iterator = map.keySet().iterator();
		StringBuffer sb = new StringBuffer();
		sb.append("{vmode:");
		sb.append("\"" + StringEscapeUtils.escapeHtml(vMode) + "\",");
		sb.append("workName:");
		sb.append("\"" + StringEscapeUtils.escapeHtml(workName) + "\"");

        String key = null;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            if (!key.equals("vmode") && !key.equals("workName")) {
                sb.append(",");
                sb.append(StringEscapeUtils.escapeHtml(key));
                sb.append(":");
                sb.append("\"" + StringEscapeUtils.escapeHtml(((String[]) map.get(key))[0]) + "\"");
            }
        }
        sb.append("}");
				
		Map<String, String> searchConfigMap = systemConfigManager.getMailAdvanceSearchConfig();
		String mailSearchConfig = "{bodySearch:'" + StringEscapeUtils.escapeHtml(searchConfigMap.get("bodySearch")) + "',attachSearch:'"
                + StringEscapeUtils.escapeHtml(searchConfigMap.get("attachSearch")) + "'}";
		
		String localmail = systemConfigManager.getLocalMailConfig();
		
		boolean isFirstLogin = "F".equals(user.get(User.FIRST_LOGIN));		
		if(isFirstLogin){
			TMailStore store = null;
			TMailStoreFactory factory = new TMailStoreFactory();
			try {
				store = factory.connect(request.getRemoteAddr(), user);				
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
			} finally {
				Thread.sleep(3000);
				if(store != null){
					store.close();	
				}
			}
			user.put(User.FIRST_LOGIN, "N");
			userAuthManager.updateUserCookieProcess(request, response, 
					user, systemConfigManager.getCryptMethod());
		}
		
		UserEtcInfoVO vo = userSettingManager.readUserEtcInfo(userSeq);
		boolean isPopupWriteMode = ("popup".equals(vo.getComposeMode()));
		boolean isWriteNoti = ("enable".equals(vo.getWriteNoti()));
		boolean isLocalMail = ("enabled".equals(localmail));
		boolean isSearchAllFolder = ("on".equalsIgnoreCase(vo.getSearchAllFolder()));
		String[] vaultValues = systemConfigManager.archiveUseInfoApi(domainSeq, groupSeq, email);
		request.setAttribute("popupWrite", isPopupWriteMode);
		request.setAttribute("writeNoti", isWriteNoti);
		request.setAttribute("localmail", isLocalMail);
		request.setAttribute("vmode", vMode);
		request.setAttribute("workName", workName);
		request.setAttribute("workParam", sb.toString());
		request.setAttribute("isSearchAllFolder", isSearchAllFolder);
		request.setAttribute("mailSearchConfig", mailSearchConfig);
		request.setAttribute("useArchive", "on".equalsIgnoreCase(vaultValues[0]));
		request.setAttribute("archiveSSOUrl", vaultValues[1]);
		return "load";
	}
	
	
	public String loadHtmlPage() throws Exception {		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));		
		
		String forward = null;
		String vMode = request.getParameter("vmode");		
		vMode = (vMode != null)?vMode:"mwork";
		String workName = request.getParameter("workName");
		workName = (workName != null)?workName:"list";
		
		if("domain".equalsIgnoreCase(vMode)){
			vMode = systemConfigManager.readDomainAfterLogin(domainSeq);
			if("mail".equalsIgnoreCase(vMode)){
				vMode = "mwork";
			} else if("manage".equalsIgnoreCase(vMode)){
				return "manage";
			}
		}
		
		Map map = request.getParameterMap();
		Iterator iterator = map.keySet().iterator();
		StringBuffer sb = new StringBuffer();		
		String key = null;		
		while (iterator.hasNext()) {
			key = (String) iterator.next();			
			if(!key.equals("vmode") && !key.equals("workName")){
				sb.append(cleanXSS(key));
				sb.append("");
				sb.append("'"+cleanXSS((((String[])map.get(key))[0]))+"'");
			}			
		}
		sb.append("}");
		
		boolean isFirstLogin = "F".equals(user.get(User.FIRST_LOGIN));		
		if(isFirstLogin){
			TMailStore store = null;
			TMailStoreFactory factory = new TMailStoreFactory();
			try {
				store = factory.connect(request.getRemoteAddr(), user);				
			} catch (Exception e) {		
				LogManager.writeErr(this, e.getMessage(), e);
			} finally {
				Thread.sleep(3000);
				if(store != null){
					store.close();	
				}
			}
			user.put(User.FIRST_LOGIN, "N");
			userAuthManager.updateUserCookieProcess(request, response, 
					user, systemConfigManager.getCryptMethod());
		}		
		
		if(vMode.equalsIgnoreCase("mhome") || 
				vMode.equalsIgnoreCase("intro")){			
			forward = "home";
		} else if(vMode.equalsIgnoreCase("mwork")){
			if(workName.equalsIgnoreCase("list")){
				request.setAttribute("folder", "Inbox");
				forward = "list";
			} else if(workName.equalsIgnoreCase("golist")||workName.equalsIgnoreCase("golistparam")){
				forward = "list";
			} else if(workName.equalsIgnoreCase("write")){
				forward = "write";
			}
		}
		
		return forward;
	}

	private String cleanXSS(String value) {          
	     value = value.replaceAll("<", "").replaceAll(">", "");
	     value = value.replaceAll("\\(", "").replaceAll("\\)", "");
	     value = value.replaceAll("'", "");
	     value = value.replaceAll("eval\\((.*)\\)", "");
	     value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
	     value = value.replaceAll("script", "");
	     return value;
	 }
}
