package com.terracetech.tims.service.tms.vo;

import jakarta.servlet.http.HttpServletRequest;

public class BbsContentReplyVO {
	private int bbsId = 0;
	
	private int mailDomainSeq = 0;
	
	private int contentId = 0;
	
	private int replyId = 0;
	
	private String content = null;
	
	private int creatorSeq = 0;
	
	private String creatorName = null;
	
	private String createTime = null;
	
	private String ip = null;
	
	private HttpServletRequest request = null;
	
	private String email = "";
	
	private boolean bbsAdmin = false;
	
	
	

	public boolean isBbsAdmin() {
		return bbsAdmin;
	}

	public void setBbsAdmin(boolean bbsAdmin) {
		this.bbsAdmin = bbsAdmin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public int getBbsId() {
		return bbsId;
	}

	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}

	public int getMailDomainSeq() {
		return mailDomainSeq;
	}

	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	public int getReplyId() {
		return replyId;
	}

	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCreatorSeq() {
		return creatorSeq;
	}

	public void setCreatorSeq(int creatorSeq) {
		this.creatorSeq = creatorSeq;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
