/**
 * RcptVO.java 2008. 12. 3.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.setting.vo;

import java.io.Serializable;

import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>RcptVO.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author jpjung
 * @since Tims7
 * @version 7.0 
 */
public class RcptVO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private int userSeq;
	
	private int rcptSeq;
	
	private String lastrcptEmail;
	
	private String lastrcptPersonnel;

	public int getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	
	public int getRcptSeq() {
		return rcptSeq;
	}

	public void setRcptSeq(int rcptSeq) {
		this.rcptSeq = rcptSeq;
	}

	public String getLastrcptEmail() {
		return lastrcptEmail;
	}

	public void setLastrcptEmail(String lastrcptEmail) {
		this.lastrcptEmail = lastrcptEmail;
	}

	public String getLastrcptPersonnel() {
		if(lastrcptPersonnel == null){
			lastrcptPersonnel = "";
		}
		return lastrcptPersonnel;
	}

	public void setLastrcptPersonnel(String lastrcptPersonnel) {
		this.lastrcptPersonnel = lastrcptPersonnel;
	}
	public String getAddress() {
		StringBuffer sb = new StringBuffer();
		if(lastrcptPersonnel != null && lastrcptPersonnel.length() > 0){
			sb.append("\"");
			
			sb.append(StringUtils.EscapeQuot(lastrcptPersonnel));
			
			sb.append("\" <");
			sb.append(lastrcptEmail);
			sb.append(">");
		} else {
			sb.append(lastrcptEmail);
		}
		
		return sb.toString();
	}
	
}
