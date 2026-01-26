package com.terracetech.tims.webmail.setting.action;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.ExtPartConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.plugin.pki.PKIAuthParamBean;
import com.terracetech.tims.webmail.plugin.pki.PKIAuthResultBean;
import com.terracetech.tims.webmail.plugin.pki.PKIManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserInfoVO;

public class UpdatePKISignAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1600331099716782288L;
	
	private SettingManager settingManager = null;
	
	public void setSettingManager(SettingManager settingManager){
		this.settingManager = settingManager;
		
	}
	
	public String execute() throws Exception{
		I18nResources resource = getMessageResource("setting");
		String msg = null;
		try{

			int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
			PKIManager pkiManager = new PKIManager();

			PKIAuthResultBean resultBean = pkiManager.getLoginCertificate(getPKIParamBean());
			//PKIAuthResultBean resultBean = pkiManager.getRegistCertificate(getPKIParamBean());
			
			settingManager.modifyPKIUserDN(mailUserSeq,resultBean.getUserDn());
			
			msg = resource.getMessage("update.ok");
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("update.fail");
		}
		
		request.setAttribute("msg", msg);
		request.setAttribute("url", "/setting/viewPKIUpdate.do");
		return "success";
	}
	
	private PKIAuthParamBean getPKIParamBean(){
		PKIAuthParamBean paramBean = new PKIAuthParamBean();
		if(ExtPartConstants.VENDER_SOFTFORUM == ExtPartConstants.getPKIVender()){
			/*
			paramBean.setSignedText(request.getParameter("pkiSignText"));
			paramBean.setSignedVid(request.getParameter("pkiVidText"));
			paramBean.setSsn(request.getParameter("ssn").replaceAll("-", ""));
			*/
			paramBean.setSignedText(request.getParameter("pkiSignText"));
		} else if(ExtPartConstants.VENDER_INITECH_V7 == ExtPartConstants.getPKIVender()){
			paramBean.setUserDN(request.getParameter("_shttp_client_cert_subject_"));
		} else if(ExtPartConstants.VENDER_KERIS == ExtPartConstants.getPKIVender()){
			paramBean.setSignedText(request.getParameter("pkiSignText"));			
			paramBean.setSsn(request.getParameter("ssn").replaceAll("-", ""));
		}
		return paramBean;
	}
}
