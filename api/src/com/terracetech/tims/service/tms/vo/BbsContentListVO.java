package com.terracetech.tims.service.tms.vo;

public class BbsContentListVO {

	private int bbsId = 0;
	private int isNotice = 0;
	private int contentId = 0;
	private int parentId = 0;
	private int depth = 0;
	private int orderNo = 0;
	private int attCnt = 0;
	private int refCnt = 0;
	private int creatorSeq = 0;
	private String createTime = null;
	private String subject = null;
	private String creatorName = null;
	
	public int getBbsId() {
		return bbsId;
	}
	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
}
