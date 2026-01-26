/**
 * SortRequest.java 2009. 1. 13.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.mail.sort;

import org.eclipse.angus.mail.iap.Argument;
import com.terracetech.tims.mail.TMailFolder;

/**
 * <p><strong>SortRequest.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class SortRequest {
	private int page = 0;
	private int pageBase = 0;
	private int orderKey = TMailFolder.SORT_ARRIVAL;
	private int orderDirection = TMailFolder.SORT_DESCENDING;
	private String orderValue = null;
	private Argument condition = null;
	private String[] exceptFolders = null;
	private String searchFolder = null;
	private boolean relationSearch = false;
	private String relationFolderName = null;
	private String relationUid = null;
	
	/**
	 * @return page 값 반환
	 */
	public int getPage() {
		return page;
	}
	/**
	 * @param page 파라미터를 page값에 설정
	 */
	public void setPage(int page) {
		this.page = page;
	}
	/**
	 * @return pageBase 값 반환
	 */
	public int getPageBase() {
		return pageBase;
	}
	/**
	 * @param pageBase 파라미터를 pageBase값에 설정
	 */
	public void setPageBase(int pageBase) {
		this.pageBase = pageBase;
	}
	/**
	 * @return orderKey 값 반환
	 */
	public int getOrderKey() {
		return orderKey;
	}
	/**
	 * @param orderKey 파라미터를 orderKey값에 설정
	 */
	public void setOrderKey(int orderKey) {
		this.orderKey = orderKey;
	}
	/**
	 * @return orderDirection 값 반환
	 */
	public int getOrderDirection() {
		return orderDirection;
	}
	/**
	 * @param orderDirection 파라미터를 orderDirection값에 설정
	 */
	public void setOrderDirection(int orderDirection) {
		this.orderDirection = orderDirection;
	}
	
	public String getOrderValue(){
		switch (this.orderKey) {
		case TMailFolder.SORT_ARRIVAL:
			this.orderValue = "ARRIVAL";
			break;
		case TMailFolder.SORT_CC:
			this.orderValue = "CC";
			break;
		case TMailFolder.SORT_DATE:
			this.orderValue = "DATE";
			break;
		case TMailFolder.SORT_FROM:
			this.orderValue = "FROM";
			break;
		case TMailFolder.SORT_SIZE:
			this.orderValue = "SIZE";
			break;
		case TMailFolder.SORT_SUBJECT:
			this.orderValue = "SUBJECT";
			break;
		case TMailFolder.SORT_TO:
			this.orderValue = "TO";
			break;
		}
		
		if (this.orderDirection == TMailFolder.SORT_DESCENDING) {
			this.orderValue = "REVERSE " + this.orderValue;
		}
		
		return this.orderValue;
	}
	/**
	 * @return condition 값 반환
	 */
	public Argument getCondition() {
		if(condition == null){
			condition = new Argument();
		}
		return condition;
	}
	/**
	 * @param condition 파라미터를 condition값에 설정
	 */
	public void setCondition(Argument condition) {
		this.condition = condition;
	}
	/**
	 * @return exceptFolders 값 반환
	 */
	public String[] getExceptFolders() {
		return exceptFolders;
	}
	/**
	 * @param exceptFolders 파라미터를 exceptFolders값에 설정
	 */
	public void setExceptFolders(String[] exceptFolders) {
		this.exceptFolders = exceptFolders;
	}
	/**
	 * @return searchFolder 값 반환
	 */
	public String getSearchFolder() {
		return searchFolder;
	}
	/**
	 * @param searchFolder 파라미터를 searchFolder값에 설정
	 */
	public void setSearchFolder(String searchFolder) {
		this.searchFolder = searchFolder;
	}
	public boolean isRelationSearch() {
		return relationSearch;
	}
	public void setRelationSearch(boolean relationSearch) {
		this.relationSearch = relationSearch;
	}
	public String getRelationFolderName() {
		return relationFolderName;
	}
	public void setRelationFolderName(String relationFolderName) {
		this.relationFolderName = relationFolderName;
	}
	public String getRelationUid() {
		return relationUid;
	}
	public void setRelationUid(String relationUid) {
		this.relationUid = relationUid;
	}
	
	
	
}
