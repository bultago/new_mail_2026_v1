package com.terracetech.tims.webmail.setting.action;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.PSpamRuleVO;


public class ViewSpamRuleAction extends BaseAction{
	
	private static final long serialVersionUID = 20090109L;
	
	private SettingManager manager = null;
	
	private PSpamRuleVO rule;

	public void setManager(SettingManager manager) {
		this.manager = manager;
	}
	
	public PSpamRuleVO getRule() {
		return rule;
	}

	public String execute() throws Exception{
		
		User user = UserAuthManager.getUser(request);
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		rule = manager.readSpamRule(mailUserSeq);

		return "success";
	}
}
