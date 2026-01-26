package com.terracetech.tims.webmail.addrbook.vo;

public class AddressBookAuthVO {

	private String creatorAuth;
	
	private String readAuth;
	
	private String writeAuth;

	public String getCreatorAuth() {
		return creatorAuth;
	}

	public void setCreatorAuth(String creatorAuth) {
		this.creatorAuth = creatorAuth;
	}

	public String getReadAuth() {
		return readAuth;
	}

	public void setReadAuth(String readAuth) {
		this.readAuth = readAuth;
	}

	public String getWriteAuth() {
		return writeAuth;
	}

	public void setWriteAuth(String writeAuth) {
		this.writeAuth = writeAuth;
	}
	
}
