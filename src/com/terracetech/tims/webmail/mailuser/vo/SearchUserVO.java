package com.terracetech.tims.webmail.mailuser.vo;

import org.json.simple.JSONObject;

public class SearchUserVO {
	private int mailUserSeq, mailDomainSeq; 
	
	private String mailUid, userName, mailDomain;
	
	public int getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}

	public int getMailDomainSeq() {
		return mailDomainSeq;
	}

	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public String getMailUid() {
		return mailUid;
	}

	public void setMailUid(String mailUid) {
		this.mailUid = mailUid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMailDomain() {
		return mailDomain;
	}

	public void setMailDomain(String mailDomain) {
		this.mailDomain = mailDomain;
	}

	@Override
	public String toString() {
		StringBuffer tmpStr = new StringBuffer();
		tmpStr.append("\n");
		tmpStr.append("mailUserSeq = [").append(mailUserSeq).append("]");
		tmpStr.append("\n");
		tmpStr.append("mailDomainSeq = [").append(mailDomainSeq).append("]");
		tmpStr.append("\n");
		tmpStr.append("mailUid = [").append(mailUid).append("]");
		tmpStr.append("\n");
		tmpStr.append("userName = [").append(userName).append("]");
		tmpStr.append("\n");
		tmpStr.append("mailDomain = [").append(mailDomain).append("]");
		tmpStr.append("\n");

		return tmpStr.toString();
	}
	
	public JSONObject toJson() {
		JSONObject jObj = new JSONObject();
		jObj.put("mailUserSeq", mailUserSeq);
		jObj.put("mailDomainSeq", mailDomainSeq);
		jObj.put("mailUid", mailUid);
		jObj.put("userName", userName);
		jObj.put("mailDomain", mailDomain);		
		return jObj;		
	}
}