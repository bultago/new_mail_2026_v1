package com.terracetech.tims.webmail.home.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.home.manager.MailHomeManager;
import com.terracetech.tims.webmail.home.vo.MailHomePortletVO;
import com.terracetech.tims.webmail.mailuser.User;

public class MailHomeViewAction extends BaseAction {

	private static final long serialVersionUID = 20090109L;
	
	private MailHomeManager manager;
	
	Map<String, MailHomePortletVO> customMap = null;
	
	public void setManager(MailHomeManager manager) {
		this.manager = manager;
	}
	
	public Map<String, MailHomePortletVO> getCustomMap() {
		return customMap;
	}

	public String execute() throws Exception{
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String forward = "home";
		String afterLogin = manager.readAfterLogin(userSeq);
		
		request.setAttribute("loginTimeInfo", user.get(User.WEBMAIL_LOGIN_TIME));
		
		if(afterLogin ==null)
			return "intro";
		
		if(afterLogin.indexOf("intro") > -1){
			return "intro";
		}
		
		customMap = new HashMap<String, MailHomePortletVO>();
		List<MailHomePortletVO> list = manager.readLayoutPortlet(userSeq);
		for (int i = 0; i < list.size(); i++) {
			MailHomePortletVO vo = list.get(i);
			customMap.put("portlet"+vo.getLocation(), vo);
		}
		
		if(customMap.size()==0){
			forward = "intro";
		}		
		
		return forward;
	}
}
