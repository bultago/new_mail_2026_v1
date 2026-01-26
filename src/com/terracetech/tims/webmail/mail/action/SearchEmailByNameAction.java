package com.terracetech.tims.webmail.mail.action;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.persistent.DataSourceManager;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class SearchEmailByNameAction extends BaseAction {

	private static final long serialVersionUID = 1067764383215924346L;
	
	private MailManager mailManager = null;
	private SettingManager userSettingManager = null;
	
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	public void setUserSettingManager(SettingManager userSettingManager) {
		this.userSettingManager = userSettingManager;
	}
	
	public String execute() throws Exception {
		String orgSearch = request.getParameter("notOrg");
		boolean isNotOrgSearch = ("T".equals(orgSearch))?true:false;
		String keyword = request.getParameter("keyword");
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		String databaseType = DataSourceManager.getDatabaseType();
		if("postgresql".equalsIgnoreCase(databaseType)){
			keyword = (keyword != null)?StringUtils.EscapeSqlCharByPostgresql(keyword):"";
		}else{
			keyword = (keyword != null)?StringUtils.EscapeSqlChar(keyword):"";
		}
		JSONArray addrList = new JSONArray();
		JSONObject jObj = new JSONObject();
		try {
			MailAddressBean[] alllastRcpts = 
				mailManager.getUserMailAddressList(userSeq, domainSeq, 
						user.get(User.LOCALE),15, keyword,true,isNotOrgSearch);
			
			
			if(alllastRcpts.length > 0){
				for (MailAddressBean mailAddressBean : alllastRcpts) {					
					addrList.add(mailAddressBean.getAddress());				
				}
			}
			
			jObj.put("addrList", addrList);
			
			ResponseUtil.processResponse(response, jObj);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		} finally {
			addrList = null;
			jObj = null;			
		}
		
		return null;
	}

}
