package com.terracetech.tims.service.tms.vo;

public class BbsContentCondVO {
	
	private int bbsId = 0;
	private int currentPage = 0;
	private int noticeCount = 0;
	private int pageBase = 0;
	private String email = null;
	private String searchType = null;
	private String keyWord = null;
	private String remoteIp = null;
	
	public int getBbsId() {
		return bbsId;
	}
	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getNoticeCount() {
		return noticeCount;
	}
	public void setNoticeCount(int noticeCount) {
		this.noticeCount = noticeCount;
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
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getRemoteIp() {
		return remoteIp;
	}
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	public int getPageBase() {
		return pageBase;
	}
	public void setPageBase(int pageBase) {
		this.pageBase = pageBase;
	}
}
