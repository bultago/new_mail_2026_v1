package com.terracetech.tims.webmail.webfolder.vo;

import java.util.ArrayList;
import java.util.List;

public class WebfolderVO {
	
	private List partnerList = new ArrayList();
	
	private int currentPage=0;
	
	private int countPerPage=0;
	
	private int startPage=0;
	
	private int endPage=0;
	
	private int topNo=0;
	
	private String searchItem = null;
	
	private String searchType = null;
	
	private String sortType = null;
	
	private String sortItem = null;
	
	public List getPartnerList() {
		return partnerList;
	}
	public void setPartnerList(List partnerList) {
		this.partnerList = partnerList;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getCountPerPage() {
		return countPerPage;
	}
	public void setCountPerPage(int countPerPage) {
		this.countPerPage = countPerPage;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public int getTopNo() {
		return topNo;
	}
	public void setTopNo(int topNo) {
		this.topNo = topNo;
	}
	public String getSearchItem() {
		return searchItem;
	}
	public void setSearchItem(String searchItem) {
		this.searchItem = searchItem;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	public String getSortItem() {
		return sortItem;
	}
	public void setSortItem(String sortItem) {
		this.sortItem = sortItem;
	}	
}
