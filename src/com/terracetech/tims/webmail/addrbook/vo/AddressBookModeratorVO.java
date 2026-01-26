package com.terracetech.tims.webmail.addrbook.vo;

public class AddressBookModeratorVO {

	private int addrbookSeq;
	
	private int userSeq;
	
	private String userEmail;
	
	private String userName;
	
	private String description = null;
	
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getAddrbookSeq() {
		return addrbookSeq;
	}

	public void setAddrbookSeq(int addrbookSeq) {
		this.addrbookSeq = addrbookSeq;
	}

	public int getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}

	public String getDescription() {
		return description;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
