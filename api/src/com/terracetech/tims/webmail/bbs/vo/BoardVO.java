package com.terracetech.tims.webmail.bbs.vo;

public class BoardVO {
	
	private int bbsId;
	
	private int bbsUpId;
	
	private int mailDomainSeq;
	
	private int mailUserSeq;
	
	private String bbsType = null; 
	
	private String bbsName = null; 
	
	private String readAuth = "LOGIN"; 
	
  	private String writeAuth = "ADMIN";
  	
  	private int pagePerCnt = 0;
  	
  	private int attMaxCnt = 0;
  	
  	private int attMaxSize = 10; 
  	
  	private int agingDay = 0; 
  	
  	private int creatorSeq;
  	
  	private String createTime = null; 
  	
  	private String description = null;
  	
  	private String bbsFullId = null;
  	
	public int getBbsId() {
		return bbsId;
	}
	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}
	public int getBbsUpId() {
		return bbsUpId;
	}
	public void setBbsUpId(int bbsUpId) {
		this.bbsUpId = bbsUpId;
	}
	public int getMailDomainSeq() {
		return mailDomainSeq;
	}
	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}
	public int getMailUserSeq() {
		return mailUserSeq;
	}
	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}
	public String getBbsType() {
		return bbsType;
	}
	public void setBbsType(String bbsType) {
		this.bbsType = bbsType;
	}
	public String getBbsName() {
		return bbsName;
	}
	public void setBbsName(String bbsName) {
		this.bbsName = bbsName;
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
	public int getPagePerCnt() {
		return pagePerCnt;
	}
	public void setPagePerCnt(int pagePerCnt) {
		this.pagePerCnt = pagePerCnt;
	}
	public int getAttMaxCnt() {
		return attMaxCnt;
	}
	public void setAttMaxCnt(int attMaxCnt) {
		this.attMaxCnt = attMaxCnt;
	}
	public int getAttMaxSize() {
		return attMaxSize;
	}
	public void setAttMaxSize(int attMaxSize) {
		this.attMaxSize = attMaxSize;
	}
	public int getAgingDay() {
		return agingDay;
	}
	public void setAgingDay(int agingDay) {
		this.agingDay = agingDay;
	}
	public int getCreatorSeq() {
		return creatorSeq;
	}
	public void setCreatorSeq(int creatorSeq) {
		this.creatorSeq = creatorSeq;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBbsFullId() {
		return bbsFullId;
	}
	public void setBbsFullId(String bbsFullId) {
		this.bbsFullId = bbsFullId;
	}
  	
}
