package com.terracetech.tims.webmail.addrbook.action;

import java.util.List;

import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookGroupVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;

public class PrivateAddressAddAction extends BaseAction {

	private static final long serialVersionUID = 20081215L;
	
	private List<AddressBookGroupVO> groups;
	
	private AddressBookManager manager;
	
	public void setManager(AddressBookManager manager) {
		this.manager = manager;
	}
	
	public List<AddressBookGroupVO> getGroups() {
		return groups;
	}

	public String execute() throws Exception {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		groups = manager.getPrivateGroupList(userSeq);
		
		return "success";
	}
	
	public String executePart() throws Exception{
		
		return "success";
	}
}
