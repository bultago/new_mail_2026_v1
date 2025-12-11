/**
 * TMailTag.java 2009. 1. 12.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.mail.tag;

/**
 * <p><strong>TMailTag.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class TMailTag implements Comparable{
	private int id = -1;
	private String name = null;
	private String color = null;
	
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
	 * @return color 값 반환
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color 파라미터를 color값에 설정
	 */
	public void setColor(String color) {
		this.color = color;
	}
	
	public int compareTo(Object o) {
		TMailTag peer = (TMailTag)o;
		int val; 
		if (peer.getName().hashCode() < this.name.hashCode()){
			val = 1;
		}else if (peer.getName().hashCode() > this.name.hashCode()){
			val = -1;
		}else {
			val = 0;
		}
		return 0;
	}
	
	
}
