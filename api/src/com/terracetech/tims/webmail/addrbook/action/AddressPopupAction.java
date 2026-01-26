package com.terracetech.tims.webmail.addrbook.action;

import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;

public class AddressPopupAction extends BaseAction {
	
	private AddressBookManager manager = null;


	public void setManager(AddressBookManager manager) {
		this.manager = manager;
	}

	public String execute() throws Exception {
		
		return "success";
	}
}
