package com.terracetech.tims.webmail.addrbook.vo;

public class AddressBookReaderVO {

	private int addrbookSeq;
	
	private int userSeq;
	
	private String userEmail;
	
	private String userName;
	
	private String description = null;

	public int getAddrbookSeq() {
		return addrbookSeq;
	}

	public void setAddrbookSeq(int addrbookSeq) {
		this.addrbookSeq = addrbookSeq;
	}
	
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
