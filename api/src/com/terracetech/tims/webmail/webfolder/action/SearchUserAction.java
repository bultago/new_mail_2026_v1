package com.terracetech.tims.webmail.webfolder.action;

import java.io.PrintWriter;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderShareVO;

public class SearchUserAction extends BaseAction {
	private WebfolderManager webfolderManager = null;
	private String searchType = null;
	private String keyWord = null;
	
	public void setWebfolderManager(WebfolderManager webfolderManager) {
		this.webfolderManager = webfolderManager;
	}

	public String execute() throws Exception {

		String uid = user.get(User.MAIL_UID);
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		JSONObject resultSet = new JSONObject();
		JSONArray searchUserList = new JSONArray();
		
		if (StringUtils.isNotEmpty(keyWord)) {
			keyWord = keyWord.toLowerCase();
		}
		
		try {	
			List<WebfolderShareVO> userList = webfolderManager.getMemberList(searchType, keyWord, uid, mailDomainSeq);
			if (userList != null && userList.size() > 0) {
				for (int i = 0; i < userList.size(); i++) {
					JSONObject searchUser = new JSONObject();
					searchUser.put("userSeq", userList.get(i).getMailUserSeq());
					searchUser.put("uid", userList.get(i).getUid());
					searchUser.put("name", userList.get(i).getName());
					searchUser.put("domain", userList.get(i).getDomain());
					searchUserList.add(searchUser);
				}
			}
			resultSet.put("member", searchUserList);
			resultSet.put("result", "success");
		}
		catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			resultSet.put("result", "error");
		}
		
		response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.addHeader("Cache-Control","no-store");
		response.addHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
		out.println(resultSet);
					
		return null;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
}
