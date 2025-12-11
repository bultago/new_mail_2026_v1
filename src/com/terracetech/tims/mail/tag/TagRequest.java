/**
 * TagQuery.java 2009. 1. 12.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.mail.tag;

import com.terracetech.tims.mail.TMailFolder;

/**
 * <p><strong>TagQuery.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class TagRequest {
	private int cmd = -1;
	private int page = 0;
	private int pageBase = 0;
	private String searchPattern = null;
	private String storeFolderName = null;
	private boolean messageStoreTag = false;
	private String storeUid = null;
	private String order = null;
	private TMailTag currentTag = null;
	private TMailTag newTag = null;
	private String[] workIds = null;
	
	
	/**
	 * <p></p>
	 *
	 */
	public TagRequest(int cmd) {
		this.cmd = cmd;		
	}
	
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
	 * @return searchPattern 값 반환
	 */
	public String getSearchPattern() {
		searchPattern = (searchPattern != null)? searchPattern : "";
		return searchPattern;
	}
	/**
	 * @param searchPattern 파라미터를 searchPattern값에 설정
	 */
	public void setSearchPattern(String searchPattern) {
		this.searchPattern = searchPattern;
	}
	/**
	 * @return storeFolderName 값 반환
	 */
	public String getStoreFolderName() {
		return storeFolderName;
	}
	/**
	 * @param storeFolderName 파라미터를 storeFolderName값에 설정
	 */
	public void setStoreFolderName(String storeFolderName) {
		this.storeFolderName = storeFolderName;
	}
	/**
	 * @return storeUid 값 반환
	 */
	public String getStoreUid() {
		return storeUid;
	}
	/**
	 * @param storeUid 파라미터를 storeUid값에 설정
	 */
	public void setStoreUid(long[] uids) {
		StringBuffer sb = new StringBuffer();
		if(uids != null){
			int size = uids.length;
			for (int i = 0; i < size; i++) {				
				sb.append(Long.toString(uids[i]));				
				if(i < size-1){
					sb.append(",");
				}				
			}
			
			this.storeUid = sb.toString();
		}
	}	
	/**
	 * @return cmd 값 반환
	 */
	public int getCmd() {
		return cmd;
	}
	
	/**
	 * @return messageStoreTag 값 반환
	 */
	public boolean isMessageStoreTag() {
		return messageStoreTag;
	}

	/**
	 * @param messageStoreTag 파라미터를 messageStoreTag값에 설정
	 */
	public void setMessageStoreTag(boolean messageStoreTag) {
		this.messageStoreTag = messageStoreTag;
	}

	/**
	 * @return order 값 반환
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param order 파라미터를 order값에 설정
	 */
	public void setOrder(int key, int direction) {
		switch (key) {
		case TMailFolder.SORT_ARRIVAL:
			this.order = "ARRIVAL";
			break;
		case TMailFolder.SORT_CC:
			this.order = "CC";
			break;
		case TMailFolder.SORT_DATE:
			this.order = "DATE";
			break;
		case TMailFolder.SORT_FROM:
			this.order = "FROM";
			break;
		case TMailFolder.SORT_SIZE:
			this.order = "SIZE";
			break;
		case TMailFolder.SORT_SUBJECT:
			this.order = "SUBJECT";
			break;
		case TMailFolder.SORT_TO:
			this.order = "TO";
			break;
		}

		if (direction == TMailFolder.SORT_DESCENDING) {
			this.order = "REVERSE " + this.order;
		}
	}

	/**
	 * @return currentTag 값 반환
	 */
	public TMailTag getCurrentTag() {
		return currentTag;
	}

	/**
	 * @param currentTag 파라미터를 currentTag값에 설정
	 */
	public void setCurrentTag(TMailTag currentTag) {
		this.currentTag = currentTag;
	}

	/**
	 * @return newTag 값 반환
	 */
	public TMailTag getNewTag() {
		return newTag;
	}

	/**
	 * @param newTag 파라미터를 newTag값에 설정
	 */
	public void setNewTag(TMailTag newTag) {
		this.newTag = newTag;
	}

	/**
	 * @return workNames 값 반환
	 */
	public String getWorkIds() {
		String ids = null;
		if(workIds != null){
			StringBuffer sb = new StringBuffer();
			int size = workIds.length;
			for (int i = 0; i < size; i++) {
				sb.append(workIds[i]);
				if(i < size-1){
					sb.append(",");
				}				
			}
			ids = sb.toString();
		}		
		return ids;
	}

	/**
	 * @param workNames 파라미터를 workNames값에 설정
	 */
	public void setWorkIds(String[] workIds) {
		this.workIds = workIds;
	}
	
	
	
}
