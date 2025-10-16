/**
 * ForwardingInfoVO.java 2008. 12. 2.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.setting.vo;

/**
 * <p><strong>ForwardingInfoVO.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class ForwardingInfoVO {

	private int userSeq;
	/**
	 * NONE
	 * FOWARDING
	 * FOWARDONLY
	 */
	private String forwardingMode;
	private String[] forwardingAddress;
	public int getUserSeq() {
		return userSeq;
	}
	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	public String getForwardingMode() {
		return forwardingMode;
	}
	public void setForwardingMode(String forwardingMode) {
		this.forwardingMode = forwardingMode;
	}
	public String[] getForwardingAddress() {
		return forwardingAddress;
	}
	public void setForwardingAddress(String[] forwardingAddress) {
		this.forwardingAddress = forwardingAddress;
	}
}
