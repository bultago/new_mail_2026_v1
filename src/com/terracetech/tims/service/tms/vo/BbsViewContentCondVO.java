package com.terracetech.tims.service.tms.vo;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class BbsViewContentCondVO {
	
	private int bbsId = 0;
	private int contentId = 0;
	private int parentId = 0;
	private int orderNo = 0;
	private String bbsType = null;
	private String email = null;
	private String keyWord = null;
	private HttpServletRequest request = null;
	private String searchType = null;
	private int currentPage = 0;
	private int pageBase = 0;
	private ServletContext context = null;
	
	
	public ServletContext getContext() {
		return context;
	}
	public void setContext(ServletContext context) {
		this.context = context;
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
	public int getPageBase() {
		return pageBase;
	}
	public void setPageBase(int pageBase) {
		this.pageBase = pageBase;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getBbsId() {
		return bbsId;
	}
	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
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
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public String getBbsType() {
		return bbsType;
	}
	public void setBbsType(String bbsType) {
		this.bbsType = bbsType;
	}
	
	
}
