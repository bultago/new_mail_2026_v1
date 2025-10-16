package com.terracetech.tims.webmail.addrbook.vo;

public class AddressBookVO {

	private int addrbookSeq;
	
	private int mailDomainSeq;
	
	private String addrbookName = null;
	
	private String readerType = null;
	
	private String description = null;

	private int creatorSeq;

	private String userName = null;
	
	private int readAuth;
	
	private int writeAuth;

	public int getAddrbookSeq() {
		return addrbookSeq;
	}

	public void setAddrbookSeq(int addrbookSeq) {
		this.addrbookSeq = addrbookSeq;
	}

	public String getAddrbookName() {
		return addrbookName;
	}

	public void setAddrbookName(String addrbookName) {
		this.addrbookName = addrbookName;
	}

	public String getReaderType() {
		return readerType;
	}

	public void setReaderType(String readerType) {
		this.readerType = readerType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getReadAuth() {
		return readAuth;
	}

	public void setReadAuth(int readAuth) {
		this.readAuth = readAuth;
	}

	public int getWriteAuth() {
		return writeAuth;
	}

	public void setWriteAuth(int writeAuth) {
		this.writeAuth = writeAuth;
	}

	public int getMailDomainSeq() {
		return mailDomainSeq;
	}

	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public int getCreatorSeq() {
		return creatorSeq;
	}

	public void setCreatorSeq(int creatorSeq) {
		this.creatorSeq = creatorSeq;
	}
	
}
