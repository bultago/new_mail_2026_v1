package com.terracetech.tims.webmail.addrbook.action;

import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;

public class PrivateAddressSaveAction extends BaseAction {

	private static final long serialVersionUID = 20081215L;

	private AddressBookManager manager;
	
	private String name = null;
	private String email = null;
	private String company = null;
	private String mobile = null;
	
	private int groupSeq;
	
	public void setGroupSeq(int groupSeq) {
		this.groupSeq = groupSeq;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setManager(AddressBookManager manager) {
		this.manager = manager;
	}

	public String execute() throws Exception {
		if (!checkPAID()) {
			return "paidError";
	    }
		
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		AddressBookMemberVO member = new AddressBookMemberVO();
		member.setUserSeq(userSeq);
		member.setMemberName(name);
		member.setMemberEmail(email);
		member.setCompanyName(company);
		member.setMobileNo(mobile);
		
		try {
			manager.savePrivateAddressMemberWithTransactional(member, groupSeq,
					domainSeq);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}

		return "success";
	}
}
