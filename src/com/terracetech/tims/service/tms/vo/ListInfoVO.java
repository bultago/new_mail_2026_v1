package com.terracetech.tims.service.tms.vo;

import java.io.Serializable;

public class ListInfoVO implements Serializable{
	
	private static final long serialVersionUID = 20091102L;
	
	private int unreadCnt = 0;
	private int totalCnt = 0;
	private int searchCnt = 0;
	private int pageNo = 0;
	private String folderName = null;
	private String folderAliasName = null;
	
	private boolean errorOccur = false;
	private String errorMsg = null;
	
	public int getUnreadCnt() {
		return unreadCnt;
	}
	public void setUnreadCnt(int unreadCnt) {
		this.unreadCnt = unreadCnt;
	}
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	public int getSearchCnt() {
		return searchCnt;
	}
	public void setSearchCnt(int searchCnt) {
		this.searchCnt = searchCnt;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public String getFolderAliasName() {
		return folderAliasName;
	}
	public void setFolderAliasName(String folderAliasName) {
		this.folderAliasName = folderAliasName;
	}
	public ListContentVO[] getListContents() {
		return listContents;
	}
	public void setListContents(ListContentVO[] listContents) {
		this.listContents = listContents;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public boolean isErrorOccur() {
		return errorOccur;
	}
	public void setErrorOccur(boolean errorOccur) {
		this.errorOccur = errorOccur;
	}	
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	private ListContentVO[] listContents = null;
	
	
}
