package com.terracetech.tims.service.tms.impl;

import com.terracetech.tims.service.IErrorCode;
import com.terracetech.tims.service.manager.IMailServiceManager;
import com.terracetech.tims.service.tms.IMailService;
import com.terracetech.tims.service.tms.vo.ListCondVO;
import com.terracetech.tims.service.tms.vo.ListInfoVO;
import com.terracetech.tims.service.tms.vo.ReadCondVO;
import com.terracetech.tims.service.tms.vo.SendCondVO;
import com.terracetech.tims.service.tms.vo.SendResultVO;
import com.terracetech.tims.service.tms.vo.ViewContentVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;


public class MailService implements IMailService {
	
	
	private IMailServiceManager mailServiceManager = null;	
	
	private SettingManager settingManager = null;
	
	private String remoteIp = null;
	
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public void setMailServiceManager(IMailServiceManager mailServiceManager) {
		this.mailServiceManager = mailServiceManager;
	}
	
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}

	public ListInfoVO getContentsMailList(ListCondVO listVO)throws Exception{	
		listVO.setRemoteIp(remoteIp);
		if(!settingManager.isApiAccessAllow(remoteIp)){
			ListInfoVO result = new ListInfoVO();
			result.setErrorMsg(IErrorCode.ERR_PERMISSION);
			
			return result;
		}
		
		try {
			return mailServiceManager.doSimpleMailList(listVO);	
		} catch (Exception e) {
			ListInfoVO result = new ListInfoVO();
			result.setErrorMsg(IErrorCode.ERR_UNKNOWN);
			
			return result;
		}
				
	}
	
	public ListInfoVO getContentsMailList(ListCondVO listVO, User user)throws Exception{	
		listVO.setRemoteIp(remoteIp);
		return mailServiceManager.doSimpleMailList(listVO, user);		
	}
	
	public ViewContentVO getViewMailContent(ReadCondVO readVO) throws Exception{		
		readVO.setRemoteIp(remoteIp);
		if(!settingManager.isApiAccessAllow(remoteIp)){
			ViewContentVO result = new ViewContentVO();
			result.setErrorMsg(IErrorCode.ERR_PERMISSION);
			
			return result;
		}
		try {
			return mailServiceManager.doSimpleMailRead(readVO);	
		} catch (Exception e) {
			ViewContentVO result = new ViewContentVO();
			result.setErrorMsg(IErrorCode.ERR_UNKNOWN);
			
			return result;
		}
		
	}
	
	public ViewContentVO getViewMailContent(ReadCondVO readVO, User user) throws Exception{		
		readVO.setRemoteIp(remoteIp);
		return mailServiceManager.doSimpleMailRead(readVO, user);
	}
	
	public SendResultVO sendMailMessage(SendCondVO sendVO) throws Exception{
		sendVO.setRemoteIp(remoteIp);
		if(!settingManager.isApiAccessAllow(remoteIp)){
			SendResultVO result = new SendResultVO();
			result.setErrorMsg(IErrorCode.ERR_PERMISSION);
			
			return result;
		}
		
		try {
			return mailServiceManager.doSimpleMailSend(sendVO);	
		} catch (Exception e) {
			SendResultVO result = new SendResultVO();
			result.setErrorMsg(IErrorCode.ERR_UNKNOWN);
			
			return result;
		}
	}
	
	public SendResultVO sendMailMessage(SendCondVO sendVO, User user) throws Exception{
		sendVO.setRemoteIp(remoteIp);
		return mailServiceManager.doSimpleMailSend(sendVO,user);
	}
}
