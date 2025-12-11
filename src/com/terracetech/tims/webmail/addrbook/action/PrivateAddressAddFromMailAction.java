package com.terracetech.tims.webmail.addrbook.action;

import java.io.PrintWriter;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.exception.SaveFailedException;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class PrivateAddressAddFromMailAction extends BaseAction {
	
	private static final long serialVersionUID = 20081215L;
	
	public Logger log = LoggerFactory.getLogger(this.getClass());
	
	private AddressBookManager manager;
	
	public void setManager(AddressBookManager manager) {
		this.manager = manager;
	}
	
	public String execute() throws Exception {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		JSONObject jObj = new JSONObject();
		String[] emailList = request.getParameterValues("addrs");
		
		try {
			manager.setResource(getMessageResource("addr"));
			
			if(emailList != null){				
				AddressBookMemberVO member = null;
				for (String str : emailList) {
					log.debug("add privateAddress : " + str);
					
					String[] value = str.split("\\|");					
					
					member = new AddressBookMemberVO();
					member.setUserSeq(userSeq);
					member.setMemberName(value[0]);
					member.setMemberEmail(value[1]);
					member.setGroupSeq(Integer.parseInt(value[2]));
					manager.savePrivateAddressMemberWithTransactional(member, Integer.parseInt(value[2]), domainSeq);
				}
			}
			jObj.put("result","success");
		} catch (Exception e) {
			log.error(e.getMessage(), e);			
			jObj.put("result","fail");
			jObj.put("errMsg",e.getMessage());
			
		}
		
		
		ResponseUtil.processResponse(response, jObj);
		
		return null;
	}
	
	public String executePart() throws Exception{
		
		return execute();
	}
}
