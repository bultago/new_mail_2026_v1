package com.terracetech.tims.webmail.note.action;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.note.manager.NoteManager;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class SearchUserListAction extends BaseAction {
	
	private NoteManager noteManager = null;
	private String keyword = null;

	public void setNoteManager(NoteManager noteManager) {
		this.noteManager = noteManager;
	}
	
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {

		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		keyword = (keyword != null)?StringUtils.EscapeSqlChar(keyword):"";
		
		JSONArray addrList = new JSONArray();
		JSONObject jObj = new JSONObject();
		List<MailAddressBean> mailAddressList = null;
		
		try {
			mailAddressList = noteManager.readDomainUserList(mailDomainSeq, keyword, true);
			
			if(mailAddressList != null && mailAddressList.size() > 0){
				for (MailAddressBean mailAddressBean : mailAddressList) {					
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

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
}
