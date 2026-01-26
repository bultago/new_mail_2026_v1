/**
 * SearchRequest.java 2009. 1. 13.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.mail.search;

/**
 * <p><strong>SearchRequest.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class SearchRequest {
	private int cmd = -1;
	private int page = 0;
	private int pageBase = 0;
	private String pattern = null;
	private TMailSearchQuery currentQuery = null;
	private TMailSearchQuery newQuery = null;
	private String[] workIds = null;
	
	/**
	 * <p></p>
	 *
	 */
	public SearchRequest(int cmd) {
		this.cmd = cmd;
	}
	
	/**
	 * @return cmd 값 반환
	 */
	public int getCmd() {
		return cmd;
	}
	/**
	 * @param cmd 파라미터를 cmd값에 설정
	 */
	public void setCmd(int cmd) {
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
	 * @return pettern 값 반환
	 */
	public String getPattern() {
		return pattern;
	}
	/**
	 * @param pettern 파라미터를 pettern값에 설정
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	/**
	 * @return currentQuery 값 반환
	 */
	public TMailSearchQuery getCurrentQuery() {
		return currentQuery;
	}
	/**
	 * @param currentQuery 파라미터를 currentQuery값에 설정
	 */
	public void setCurrentQuery(TMailSearchQuery currentQuery) {
		this.currentQuery = currentQuery;
	}
	/**
	 * @return newQuery 값 반환
	 */
	public TMailSearchQuery getNewQuery() {
		return newQuery;
	}
	/**
	 * @param newQuery 파라미터를 newQuery값에 설정
	 */
	public void setNewQuery(TMailSearchQuery newQuery) {
		this.newQuery = newQuery;
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
