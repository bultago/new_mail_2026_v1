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


public class DeleteFilterAction extends BaseAction{
	
	private static final long serialVersionUID = 20090112L;
	
	private SettingManager manager = null;
	
	private String[] rules = null;
	private int rule = 0;
	
	public void setManager(SettingManager manager) {
		this.manager = manager;
	}
	
	public void setRules(String[] rules) {
		this.rules = rules;
	}

	public void setRule(int rule) {
		this.rule = rule;
	}

	public String execute() throws Exception{
		I18nResources resource = getMessageResource("setting");
		
		User user = UserAuthManager.getUser(request);
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String msg = null;
		int[] ruleData = null;
		try {
			if (rule > 0) {
				ruleData = new int[1];
				ruleData[0] = rule;
			} else {
				if (rules != null && rules.length > 0) {
					ruleData = new int[rules.length];
					for (int i=0; i< rules.length; i++) {
						StringTokenizer st = new StringTokenizer(rules[i], "-|-");
						String index = st.nextToken();
						ruleData[i] = Integer.parseInt(index);
					}
				}
			}
			
			manager.deleteFilterCond(mailUserSeq, ruleData);
			msg = resource.getMessage("del.ok");
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("del.fail");
		}
		request.setAttribute("msg", msg);
		return "success";
	}
}
