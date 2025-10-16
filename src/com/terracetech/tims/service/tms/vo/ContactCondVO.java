package com.terracetech.tims.service.tms.vo;

public class ContactCondVO {

	private int domainSeq;
	private int userSeq = 0;
	private int bookSeq;
	private int groupSeq;
	private int memberSeq;
	
	private int currentPage;
	private int maxResult;
	
	private String addrType;
	private String startChar;
	private String sortBy;
	private String sortDir;
	
	private String searchType;
	private String keyWord;
	
	private String userEmail = null;
	
	public int getDomainSeq() {
		return domainSeq;
	}
	public void setDomainSeq(int domainSeq) {
		this.domainSeq = domainSeq;
	}
	public int getUserSeq() {
		return userSeq;
	}
	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	public int getBookSeq() {
		return bookSeq;
	}
	public void setBookSeq(int bookSeq) {
		this.bookSeq = bookSeq;
	}
	public int getGroupSeq() {
		return groupSeq;
	}
	public void setGroupSeq(int groupSeq) {
		this.groupSeq = groupSeq;
	}
	public int getMemberSeq() {
		return memberSeq;
	}
	public void setMemberSeq(int memberSeq) {
		this.memberSeq = memberSeq;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getMaxResult() {
		return maxResult;
	}
	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}
	public String getStartChar() {
		return startChar;
	}
	public void setStartChar(String startChar) {
		this.startChar = startChar;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public String getSortDir() {
		return sortDir;
	}
	public void setSortDir(String sortDir) {
		this.sortDir = sortDir;
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
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
        public String getAddrType() {
            return addrType;
        }
        public void setAddrType(String addrType) {
            this.addrType = addrType;
        }
}
