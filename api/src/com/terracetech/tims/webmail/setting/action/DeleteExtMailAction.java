package com.terracetech.tims.webmail.setting.action;

import java.util.Locale;
import java.util.StringTokenizer;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;


public class DeleteExtMailAction extends BaseAction{
	
	private static final long serialVersionUID = 20090112L;
	
	private SettingManager manager = null;
	
	private String[] rules = null;
	
	private String rule = null;
	
	public void setManager(SettingManager manager) {
		this.manager = manager;
	}
	
	public void setRule(String rule) {
		this.rule = rule;
	}

	public void setRules(String[] rules) {
		this.rules = rules;
	}
	
	public String execute() throws Exception{
		I18nResources resource = getMessageResource("setting");
		
		User user = UserAuthManager.getUser(request);
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String msg = null;
		try {
			if (rule != null && rule.length() > 0) {
				rules = new String[1];
				rules[0] = rule;
			}
			
			if (rules != null && rules.length > 0) {
				for (int i=0; i<rules.length; i++) {
					StringTokenizer st = new StringTokenizer(rules[i], "|");
					manager.deletePop3(mailUserSeq, st.nextToken(), st.nextToken());
				}
			}
			msg = resource.getMessage("del.ok");
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("del.fail");
		}
		request.setAttribute("msg", msg);
		return "success";
	}
}
