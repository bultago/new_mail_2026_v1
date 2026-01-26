package com.terracetech.tims.service.tms.vo;

import java.io.Serializable;

public class ContactInfoVO implements Serializable{
	
	private static final long serialVersionUID = 20100308L;

	private int totalCount = 0;
	
	private ContactCondVO cond = null;
	
	private ContactBookVO bookInfo = null;
	
	private ContactGroupVO groupInfo = null;
	
	private ContactMemberVO[] memberList= null;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public ContactCondVO getCond() {
		return cond;
	}

	public void setCond(ContactCondVO cond) {
		this.cond = cond;
	}

	public ContactMemberVO[] getMemberList() {
		return memberList;
	}

	public void setMemberList(ContactMemberVO[] memberList) {
		this.memberList = memberList;
	}

	public ContactGroupVO getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(ContactGroupVO groupInfo) {
		this.groupInfo = groupInfo;
	}

	public ContactBookVO getBookInfo() {
		return bookInfo;
	}

	public void setBookInfo(ContactBookVO bookInfo) {
		this.bookInfo = bookInfo;
	}
	
}
