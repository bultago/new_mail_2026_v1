package com.terracetech.tims.webmail.setting.action;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.DefineForwardingInfoVO;
import com.terracetech.tims.webmail.setting.vo.ForwardingInfoVO;

public class DeleteDefineForwardAction extends BaseAction {
	private int[] defineCheck;	
	private SettingManager manager = null;
	
	public int[] getDefineCheck() {
		return defineCheck;
	}
	public void setDefineCheck(int[] defineCheck) {
		this.defineCheck = defineCheck;
	}	
	public SettingManager getManager() {
		return manager;
	}
	public void setManager(SettingManager manager) {
		this.manager = manager;
	}
	public String execute() throws Exception {	
		I18nResources resource = getMessageResource("setting");		
		User user = UserAuthManager.getUser(request);
		String msg = null;
		try{
			this.manager.deleteDefineForwading(this.defineCheck);
			msg = resource.getMessage("del.ok");
		}catch(Exception e){
			e.printStackTrace();
			msg = resource.getMessage("del.fail");
		}
		request.setAttribute("msg", msg);		
		return "success";
	}
}