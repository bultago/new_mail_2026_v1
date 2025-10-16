package com.terracetech.tims.webmail.mail.action;

import com.terracetech.tims.service.tms.impl.MailService;
import com.terracetech.tims.service.tms.portlet.HtmlPortletService;
import com.terracetech.tims.service.tms.portlet.IMailPortletService;
import com.terracetech.tims.service.tms.portlet.XmlPortletService;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailPortletAction extends BaseAction {

	private static final long serialVersionUID = 20091218L;
	
	private MailService service;
	
	private MailUserManager mailUserManager = null;
	
	private SettingManager settingManager = null;
	
	public void setService(MailService service) {
		this.service = service;
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}

	public void prepare() throws Exception{
		String mode = request.getParameter("mode");
		if("send".equalsIgnoreCase(mode)){
		}else{
			super.prepare();
		}
	}

	/**
	 * mode : list, read, write
	 */
	public String execute() throws Exception{
		String remoteIp = request.getRemoteAddr();
		if(settingManager.isApiAccessAllow(remoteIp)){
			String mode = request.getParameter("mode");
			String viewType = request.getParameter("vtype");
			IMailPortletService portletService = null;
			if("xml".equals(viewType)){
				portletService = new XmlPortletService(service); 
			}else{
				portletService = new HtmlPortletService(service); 
			} 
			
			if(StringUtils.isNotEmpty(mode)){
				if("list".equalsIgnoreCase(mode)){
					return portletService.doSimpleMailList(request, response);
				}else if("read".equalsIgnoreCase(mode)){
					return portletService.doSimpleMailRead(request, response);
				}else if("send".equalsIgnoreCase(mode)){
					String sender = request.getParameter("sender");
					if(StringUtils.isEmpty(sender))
						return portletService.doReturnError(request, response, "mail-send");
					
					String[] senderInfo = sender.split("@");
					if(senderInfo==null || senderInfo.length !=2)
						return portletService.doReturnError(request, response, "mail-send");
					
					User user = mailUserManager.readUserAuthInfo(senderInfo[0], senderInfo[1]);
					if(user==null)
						return portletService.doReturnError(request, response, "mail-send");
					
					return portletService.doSimpleMailSend(request, response, user);
				}
			}
		}		
		return null;
	}
}
