package com.terracetech.tims.webmail.setting.action;

import java.util.Locale;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SignManager;
import com.terracetech.tims.webmail.setting.vo.SignVO;

public class ModifySignAction extends BaseAction {
	
	private SignManager signManager = null;
	private String signApply = null;
	private String signLocation = null;

	public void setSignManager(SignManager signManager) {
		this.signManager = signManager;
	}
	
	public String execute() throws Exception {
		I18nResources resource = getMessageResource("setting");
		
		User user = UserAuthManager.getUser(request);
		
		SignVO signVo = new SignVO();
		signVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		signVo.setSignApply(signApply);
		signVo.setSignLocation(signLocation);
		
		String msg = null;
		try {
			signManager.updateSignInfo(signVo);
			msg = resource.getMessage("update.ok");
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("update.fail");
		}
		request.setAttribute("msg", msg);
		return "success";
	}

	public String getSignApply() {
		return signApply;
	}

	public void setSignApply(String signApply) {
		this.signApply = signApply;
	}

	public String getSignLocation() {
		return signLocation;
	}

	public void setSignLocation(String signLocation) {
		this.signLocation = signLocation;
	}
	
}
