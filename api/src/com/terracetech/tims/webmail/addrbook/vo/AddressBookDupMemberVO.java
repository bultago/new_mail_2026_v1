package com.terracetech.tims.webmail.addrbook.vo;

public class AddressBookDupMemberVO {
	
	private String memberName;
	
	private String memberEmail;
	
	private String orgCode;
	
	private int userSeq;
	
	private String dupType;
	
	private int dupCnt;
	
	private int groupSeq;
	
	private String addrAddType;
	
	public String getAddrAddType() {
		return addrAddType;
	}

	public void setAddrAddType(String addrAddType) {
		this.addrAddType = addrAddType;
	}

	public int getGroupSeq() {
		return groupSeq;
	}

	public void setGroupSeq(int groupSeq) {
		this.groupSeq = groupSeq;
	}

	public int getDupCnt() {
		return dupCnt;
	}

	public void setDupCnt(int dupCnt) {
		this.dupCnt = dupCnt;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public int getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}

	public String getDupType() {
		return dupType;
	}

	public void setDupType(String dupType) {
		this.dupType = dupType;
	}
	
	

}
