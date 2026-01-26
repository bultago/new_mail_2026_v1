package com.terracetech.tims.webmail.mailuser.action;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.vo.SearchUserVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringReplacer;
import com.terracetech.tims.webmail.util.StringUtils;

public class SearchMailUserAction extends BaseAction {
	
	private MailUserManager mailUserManager = null;
	private List<SearchUserVO> userList = null;
	private StringReplacer sr = null;
	
	public List<SearchUserVO> getUserList() {
		return userList;
	}

	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}

	private static final long serialVersionUID = -6015742825161659699L;
	
	public void searchUser()throws Exception{
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		String searchName = request.getParameter("searchName");
		String searchId = request.getParameter("searchId");
		String equal = request.getParameter("equal");
		sr = new StringReplacer();
		
		if (StringUtils.isNotEmpty(searchId)) {
			searchId = searchId.toLowerCase();
		}
		
		searchId = sr.replaceSQLInjection(searchId);
		searchName = sr.replaceSQLInjection(searchName);
		
		try{
			userList = mailUserManager.searchMailUser(searchId, searchName, mailDomainSeq, equal);
		}catch (Exception e){
			LogManager.writeErr(this, e);
		}
	}
	
	public String getJsonUserList()throws Exception{
		searchUser();
		JSONObject jObj = new JSONObject();
		if(userList != null && userList.size() > 0){
			JSONArray list = new JSONArray();
			for (SearchUserVO vo : userList) {
				list.add(vo.toJson());
			}
			
			jObj.put("userList", list);
		}	
		
		ResponseUtil.processResponse(response, jObj);
		
		return null;
	}

}
