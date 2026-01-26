package com.terracetech.tims.webmail.mail.vo;

public class MailSearchFolderVO {
    
    private int searchFolderSeq;

    private String searchFolderName;

    private String searchTargetFolder;

    private String searchType;

    private String searchValue;

    private Integer mailUserSeq;

	public int getSearchFolderSeq() {
		return searchFolderSeq;
	}

	public void setSearchFolderSeq(int searchFolderSeq) {
		this.searchFolderSeq = searchFolderSeq;
	}

	public String getSearchFolderName() {
		return searchFolderName;
	}

	public void setSearchFolderName(String searchFolderName) {
		this.searchFolderName = searchFolderName;
	}

	public String getSearchTargetFolder() {
		return searchTargetFolder;
	}

	public void setSearchTargetFolder(String searchTargetFolder) {
		this.searchTargetFolder = searchTargetFolder;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public Integer getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(Integer mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}


}