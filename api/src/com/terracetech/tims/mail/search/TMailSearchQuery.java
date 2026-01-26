/**
 * TMailSearchQuery.java 2009. 1. 13.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.mail.search;

/**
 * <p><strong>TMailSearchQuery.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class TMailSearchQuery implements Comparable{
	private int id = -1;
	private String name = null;
	private String query = null;
	
	/**
	 * @return id 값 반환
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id 파라미터를 id값에 설정
	 */
	public void setId(int id) {
		this.id = id;
	}	
	/**
	 * @return name 값 반환
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name 파라미터를 name값에 설정
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return query 값 반환
	 */
	public String getQuery() {
		return query;
	}
	/**
	 * @param query 파라미터를 query값에 설정
	 */
	public void setQuery(String query) {
		this.query = query;
	}
	
	public int compareTo(Object o) {
		TMailSearchQuery peer = (TMailSearchQuery)o;		
		int val; 
		if (peer.getName().hashCode() < this.name.hashCode()){
			val = 1;
		}else if (peer.getName().hashCode() > this.name.hashCode()){
			val = -1;
		}else {
			val = 0;
		}

		return val;
	}
	
	
}
