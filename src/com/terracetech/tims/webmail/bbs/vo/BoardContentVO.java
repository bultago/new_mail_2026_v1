package com.terracetech.tims.webmail.bbs.vo;

import com.terracetech.tims.mail.TMailPart;

public class BoardContentVO {
	
	private int bbsId = 0;
	
	private int mailDomainSeq = 0;
	
	private int isNotice = 0;
	
	private int contentId = 0;
	
	private int parentId = 0;
	
	private int depth = 0;
	
	private int orderNo = 0;
	
	private String subject = null;
	
	private int attCnt = 0;
	
	private int refCnt = 0;
	
	private byte[] content = null;
	
	private int creatorSeq = 0;
	
	private String creatorName = null;
	
	private String createTime = null;
	
	private String ip = null;
	
	private String msgId = null;
	
	private String htmlContent = null;
	
	private String textContent = null;

	private TMailPart[] attachFiles = null;
	
	private long bbsUid = 0;
	
	private boolean creator = false;
	
	private String contentType = null;

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

	public int getIsNotice() {
		return isNotice;
	}

	public void setIsNotice(int isNotice) {
		this.isNotice = isNotice;
	}

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getAttCnt() {
		return attCnt;
	}

	public void setAttCnt(int attCnt) {
		this.attCnt = attCnt;
	}

	public int getRefCnt() {
		return refCnt;
	}

	public void setRefCnt(int refCnt) {
		this.refCnt = refCnt;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
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

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}
	
	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	
	public TMailPart[] getAttachFiles() {
		return attachFiles;
	}

	public void setAttachFiles(TMailPart[] attachFiles) {
		this.attachFiles = attachFiles;
	}
	
	public long getBbsUid() {
		return bbsUid;
	}

	public void setBbsUid(long bbsUid) {
		this.bbsUid = bbsUid;
	}

	public boolean isCreator() {
		return creator;
	}

	public void setCreator(boolean creator) {
		this.creator = creator;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	// 추가 필드 (service.tms와 호환성, 2025-10-23)
	private String bbsName;
	private String bbsType;
	private boolean bbsAdmin;
	private boolean bbsCreator;
	private boolean isReply;
	private String email;
	private String searchType;
	private int currentPage;
	
	public String getBbsName() {
		return bbsName;
	}
	
	public void setBbsName(String bbsName) {
		this.bbsName = bbsName;
	}
	
	public String getBbsType() {
		return bbsType;
	}
	
	public void setBbsType(String bbsType) {
		this.bbsType = bbsType;
	}
	
	public boolean isBbsAdmin() {
		return bbsAdmin;
	}
	
	public void setBbsAdmin(boolean bbsAdmin) {
		this.bbsAdmin = bbsAdmin;
	}
	
	public boolean isBbsCreator() {
		return bbsCreator;
	}
	
	public void setBbsCreator(boolean bbsCreator) {
		this.bbsCreator = bbsCreator;
	}
	
	public boolean isReply() {
		return isReply;
	}
	
	public void setReply(boolean isReply) {
		this.isReply = isReply;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSearchType() {
		return searchType;
	}
	
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}
