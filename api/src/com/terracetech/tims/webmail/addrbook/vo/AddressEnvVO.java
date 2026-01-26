package com.terracetech.tims.webmail.addrbook.vo;

public class AddressEnvVO {
    
    private int mailDomainSeq;

    private int privateAdrMaxMember;

    private int privateAdrMaxGroup;

    private String sharedAdrCreatorType;

    private int sharedAdrMaxAdr;

    private int sharedAdrMaxMember;

    private int sharedAdrMaxGroup;

	public int getMailDomainSeq() {
		return mailDomainSeq;
	}

	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public int getPrivateAdrMaxMember() {
		return privateAdrMaxMember;
	}

	public void setPrivateAdrMaxMember(int privateAdrMaxMember) {
		this.privateAdrMaxMember = privateAdrMaxMember;
	}

	public int getPrivateAdrMaxGroup() {
		return privateAdrMaxGroup;
	}

	public void setPrivateAdrMaxGroup(int privateAdrMaxGroup) {
		this.privateAdrMaxGroup = privateAdrMaxGroup;
	}

	public String getSharedAdrCreatorType() {
		return sharedAdrCreatorType;
	}

	public void setSharedAdrCreatorType(String sharedAdrCreatorType) {
		this.sharedAdrCreatorType = sharedAdrCreatorType;
	}

	public int getSharedAdrMaxAdr() {
		return sharedAdrMaxAdr;
	}

	public void setSharedAdrMaxAdr(int sharedAdrMaxAdr) {
		this.sharedAdrMaxAdr = sharedAdrMaxAdr;
	}

	public int getSharedAdrMaxMember() {
		return sharedAdrMaxMember;
	}

	public void setSharedAdrMaxMember(int sharedAdrMaxMember) {
		this.sharedAdrMaxMember = sharedAdrMaxMember;
	}

	public int getSharedAdrMaxGroup() {
		return sharedAdrMaxGroup;
	}

	public void setSharedAdrMaxGroup(int sharedAdrMaxGroup) {
		this.sharedAdrMaxGroup = sharedAdrMaxGroup;
	}

}