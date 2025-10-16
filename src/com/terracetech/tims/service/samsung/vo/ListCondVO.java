package com.terracetech.tims.service.samsung.vo;

public class ListCondVO {
	private String email = null;
	private String folderName = null;
	private int page = 0;
	private int pageBase = 0;
	private String locale = null;
	private String sortBy = "arrival";
	private String sortDir = "desc";
	private char flagType;
	
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
	
	
	
	
}
