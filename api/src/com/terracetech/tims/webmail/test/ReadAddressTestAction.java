package com.terracetech.tims.webmail.test;

import java.util.List;

import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.common.BaseAction;

public class ReadAddressTestAction extends BaseAction {

	private static final long serialVersionUID = -8132656159600091214L;
	
	private int userSeq;
	
	private int maxResult;
	
	private AddressBookManager manager;
	
	private List<AddressBookMemberVO> members;
	
	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	public ReadAddressTestAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}

	public List<AddressBookMemberVO> getMembers() {
		return members;
	}
	
	public void setManager(AddressBookManager manager) {
		this.manager = manager;
	}

	public String execute() throws Exception{
	
		members = manager.readPrivateMemberListByIndex(userSeq, 0, "all", 1, maxResult, null, null);
		
		return "success";
	}
}
