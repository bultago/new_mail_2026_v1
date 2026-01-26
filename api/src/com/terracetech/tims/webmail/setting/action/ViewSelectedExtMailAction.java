package com.terracetech.tims.webmail.setting.action;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.Pop3VO;
import com.terracetech.tims.webmail.util.StringUtils;

public class ViewSelectedExtMailAction extends BaseAction {

	private SettingManager manager = null;
	
	private String[] rules = null;
	
	private List<Pop3VO> pop3List = null;
	
	public void setManager(SettingManager manager) {
		this.manager = manager;
	}
	public void setRules(String[] rules) {
		this.rules = rules;
	}
	
	public List<Pop3VO> getPop3List() {
		return pop3List;
	}
	
	public String execute() throws Exception {
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		if (rules != null && rules.length > 0) {
			pop3List = new ArrayList<Pop3VO>();
			for (int i=0; i<rules.length; i++) {
				StringTokenizer st = new StringTokenizer(rules[i], "|");
				if (st.countTokens() == 2) {
					Pop3VO pop3Vo = new Pop3VO();
					pop3Vo = manager.readPop3(mailUserSeq, st.nextToken(), st.nextToken());
					pop3Vo.setPop3Boxname(StringUtils.IMAPFolderDecode(pop3Vo.getPop3Boxname()));
					pop3List.add(pop3Vo);
				}
			}
		}
		
		return "success";
	}
}
