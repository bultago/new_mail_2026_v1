package com.terracetech.tims.jmobile.mail.action;

import java.util.Map;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.jmobile.common.action.BaseAction;
import com.terracetech.tims.service.manager.MailServiceManager;
import com.terracetech.tims.service.tms.vo.SendCondVO;
import com.terracetech.tims.service.tms.vo.SendResultVO;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;

public class MailSendAction extends BaseAction {

	private static final long serialVersionUID = -7929288608994292192L;
	
	private MailServiceManager mailServiceManager = null;
	
	public void setMailServiceManager(MailServiceManager mailServiceManager) {
		this.mailServiceManager = mailServiceManager;
	}
	
	public String execute() throws Exception {
		I18nResources msgResource = getMessageResource("jmail");
		try{
			
			SendCondVO sendVO = new SendCondVO();
			String signSeq = request.getParameter("signSeq");			
			String sendType = null;
			Map map = request.getParameterMap();
			if(map.containsKey("sendDraft")){
				sendType = "draft";
			} else {
				sendType = "normal";
			}
			
			sendVO.setSendType(sendType);
			sendVO.setLocale(user.get(User.LOCALE));
		
			sendVO.setToAddr(getAddrArray(request.getParameter("to")));
			sendVO.setCcAddr(getAddrArray(request.getParameter("cc")));
			sendVO.setBccAddr(getAddrArray(request.getParameter("bcc")));
			
			sendVO.setSubject(request.getParameter("subject"));
			sendVO.setEncode("UTF-8");	
			
			sendVO.setReceivnoti("on".equalsIgnoreCase(request.getParameter("receivnoti")));
			sendVO.setOnesend("on".equalsIgnoreCase(request.getParameter("onesend")));
			sendVO.setSavesent("on".equalsIgnoreCase(request.getParameter("savesent")));			
			sendVO.setSignUse("on".equalsIgnoreCase(request.getParameter("signUse")));
			sendVO.setSignSeq((signSeq != null)?Integer.parseInt(signSeq):-1);			
			 
			sendVO.setContent(request.getParameter("content"));
			sendVO.setSenderEmail(user.get(User.EMAIL));			
			sendVO.setAttachListStr(request.getParameter("attachList"));
			
			String remoteIP = request.getRemoteAddr();		
			sendVO.setRemoteIp(remoteIP);		
			String port = EnvConstants.getBasicSetting("web.port");		
			port = (port != null && port.length() > 0)?port:Integer.toString(request.getServerPort());
			String mdnHost = EnvConstants.getMailSetting("mdn.host");
			String localhost = request.getScheme() + "://" 
					+ request.getServerName() + ":" + port;	
			mdnHost = (mdnHost != null)?mdnHost:localhost;						
			sendVO.setMdnHost(mdnHost);
			sendVO.setLocalhost(localhost);
			
			sendVO.setEditMode("text");
			sendVO.setCharset("UTF-8");
			sendVO.setUid(request.getParameter("uid"));
			sendVO.setDraftMid(request.getParameter("draftMid"));		
			SendResultVO sendResultVO = mailServiceManager.doSimpleMailSend(sendVO, user);
			if(!sendResultVO.isErrorOccur()){				
				request.setAttribute("alerttitle", msgResource.getMessage("work.success"));
				request.setAttribute("alertmsg", msgResource.getMessage("send.success"));			
			} else {
				request.setAttribute("alerttitle", msgResource.getMessage("error.title"));
				request.setAttribute("alertmsg", msgResource.getMessage("send.fail")+"<br>"+msgResource.getMessage("send.savedraft"));
			}
			
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			request.setAttribute("alerttitle", msgResource.getMessage("error.title"));
			request.setAttribute("alertmsg", msgResource.getMessage("send.fail"));			
		}
		
		return "success";
	}
	
	
	private String[] getAddrArray(String addr){
		String[] addrArray = null;
		if(addr != null && !addr.trim().equals("")){	
			addrArray = addr.split("[;,\r\n]");			
		}		
		return addrArray;
	}

}
