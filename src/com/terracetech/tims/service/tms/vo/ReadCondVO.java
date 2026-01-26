package com.terracetech.tims.service.tms.vo;


public class ReadCondVO {
	
	private String locale = null;
	private String email = null;
	private String remoteIp = null;
	private long uid = 0;
	private String folder = null;
	private boolean isViewImage = false;
	private String localURL = null;
	private String attachDir = null;
	private String sortBy = null;
	private String sortDir = null;
	private boolean advancedSearch = false;
	private String category = null;
	private String keyWord = null;
	private String fromEmailPattern = null;
	private String toEmailPattern = null;
	private char flagType;
	private int pageBase = 0;
	private int page = 0;
	private String attachPath = null;
	
	
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRemoteIp() {
		return remoteIp;
	}
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public boolean isViewImage() {
		return isViewImage;
	}
	public void setViewImage(boolean isViewImage){
		this.isViewImage = isViewImage;
	}
	public String getLocalURL() {
		return localURL;
	}
	public void setLocalURL(String localURL) {
		this.localURL = localURL;
	}
	public String getAttachDir() {
		return attachDir;
	}
	public void setAttachDir(String attachDir) {
		this.attachDir = attachDir;
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
	public boolean isAdvancedSearch() {
		return advancedSearch;
	}
	public void setAdvancedSearch(boolean advancedSearch) {
		this.advancedSearch = advancedSearch;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public char getFlagType() {
		return flagType;
	}
	public void setFlagType(char flagType) {
		this.flagType = flagType;
	}
	public int getPageBase() {
		return pageBase;
	}
	public String getAttachPath() {
		return attachPath;
	}
	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}
	public void setPageBase(int pageBase) {
		this.pageBase = pageBase;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
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
	
	
}
