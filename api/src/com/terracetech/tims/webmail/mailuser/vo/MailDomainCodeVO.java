package com.terracetech.tims.webmail.mailuser.vo;

public class MailDomainCodeVO {
	
	private int mailDomainSeq = 0;
	
	private String codeClass = null;
	
	private String codeLocale = null;
	
	private String code = null;
	
	private String codeName = null;

	public int getMailDomainSeq() {
		return mailDomainSeq;
	}

	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public String getCodeClass() {
		return codeClass;
	}

	public void setCodeClass(String codeClass) {
		this.codeClass = codeClass;
	}

	public String getCodeLocale() {
		return codeLocale;
	}

	public void setCodeLocale(String codeLocale) {
		this.codeLocale = codeLocale;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	
}
