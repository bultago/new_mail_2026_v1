package com.terracetech.tims.webmail.addrbook.action;

import java.util.List;

import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;

public class ViewSharedAddressBookListAction extends BaseAction{

	private static final long serialVersionUID = 20081215L;
	
	private AddressBookManager manager;
	
	private List<AddressBookVO> bookList = null;
	
	public List<AddressBookVO> getBookList() {
		return bookList;
	}

	public void setManager(AddressBookManager manager) {
		this.manager = manager;
	}
	
	public String execute() throws Exception{
		
		return "success";
	}
	
	public String executePart() throws Exception{
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		bookList = manager.readAddressBookList(userSeq, domainSeq);
		return "success";
	}
}
