package com.terracetech.tims.service.tms.vo;

public class ListCondVO {
	private String remoteIp = null;
	private String email = null;
	private String folderName = null;
	private int page = 0;
	private int pageBase = 0;
	private String locale = null;
	private String sortBy = "arrival";
	private String sortDir = "desc";
	private String category = null;
	private boolean advancedSearch = false;
	private char flagType;
	private String listType = null;
	private String tagId = null;
	private String keyWord = null;
	private String fromEmailPattern = null;
	private String toEmailPattern = null;
	private String operation = null;
	private String fromDate = null;
	private String toDate = null;
	
	public String getRemoteIp() {
		return remoteIp;
	}
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageBase() {
		return pageBase;
	}
	public void setPageBase(int pageBase) {
		this.pageBase = pageBase;
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
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public char getFlagType() {
		return flagType;
	}
	public void setFlagType(char flagType) {
		this.flagType = flagType;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public boolean isAdvancedSearch() {
		return advancedSearch;
	}
	public void setAdvancedSearch(boolean advancedSearch) {
		this.advancedSearch = advancedSearch;
	}
	public String getFromEmailPattern() {
		return fromEmailPattern;
	}
	public void setFromEmailPattern(String fromEmailPattern) {
		this.fromEmailPattern = fromEmailPattern;
	}
	public String getToEmailPattern() {
		return toEmailPattern;
	}
	public void setToEmailPattern(String toEmailPattern) {
		this.toEmailPattern = toEmailPattern;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

}
