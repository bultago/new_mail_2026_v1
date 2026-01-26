package com.terracetech.tims.webmail.addrbook.action;

import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;

public class ViewAddressMemberAction extends BaseAction {

	private static final long serialVersionUID = 20081215L;
	
	private AddressBookMemberVO member;
	
	private AddressBookManager manager;
	
	private int memberSeq;
	
	private int bookSeq;
	
	private String paneMode;
	
	public int getBookSeq() {
		return bookSeq;
	}

	public void setBookSeq(int bookSeq) {
		this.bookSeq = bookSeq;
	}

	public void setMemberSeq(int memberSeq) {
		this.memberSeq = memberSeq;
	}

	public void setManager(AddressBookManager manager) {
		this.manager = manager;
	}
	
	public void setMember(AddressBookMemberVO member) {
		this.member = member;
	}

	public AddressBookMemberVO getMember() {
		return member;
	}
	
	public String getPaneMode() {
		return paneMode;
	}

	public void setPaneMode(String paneMode) {
		this.paneMode = paneMode;
	}

	public String execute() throws Exception {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		member = manager.readPrivateAddressMember(userSeq, memberSeq);
		
		return "success";
	}
	
	public String executePart() throws Exception{
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		if(memberSeq!=0){
			if(getBookSeq()==0)
				member = manager.readPrivateAddressMember(userSeq, memberSeq);
			else
				member = manager.readSharedAddressMember(bookSeq, memberSeq);	
		}
		
		if(paneMode == null || "h".equals(paneMode))
			return "tab";
		
		return "success";
	}
}
