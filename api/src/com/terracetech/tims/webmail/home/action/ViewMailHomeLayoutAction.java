package com.terracetech.tims.webmail.home.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.home.manager.MailHomeManager;
import com.terracetech.tims.webmail.home.vo.MailHomeLayoutVO;
import com.terracetech.tims.webmail.home.vo.MailHomePortletVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;

public class ViewMailHomeLayoutAction extends BaseAction{

	private static final long serialVersionUID = 20090109L;
	
	private MailHomeManager manager;
	
	private MailHomeLayoutVO layout = null;
	
	private Map<Integer, MailHomePortletVO> customMap = null;
	
	private MailHomePortletVO[] portlets;
	
	public void setManager(MailHomeManager manager) {
		this.manager = manager;
	}
	
	public MailHomePortletVO[] getPortlets() {
		return portlets;
	}
	
	public MailHomeLayoutVO getLayout() {
		return layout;
	}

	public Map<Integer, MailHomePortletVO> getCustomMap() {
		return customMap;
	}

	public String execute() throws Exception{
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		layout = manager.readLayout();
		
		customMap = new HashMap<Integer, MailHomePortletVO>();
		List<MailHomePortletVO> list = manager.readLayoutPortlet(userSeq);
		for (int i = 0; i < list.size(); i++) {
			MailHomePortletVO vo = list.get(i);
			customMap.put(Integer.valueOf(vo.getLocation()), vo);
		}
		
		portlets = manager.readPortlets(domainSeq);	
		
		request.setAttribute("homeSetting", manager.getMailHomeSetting(Integer.parseInt(user.get(User.MAIL_USER_SEQ))));		
		
		return "success";
	}
}
