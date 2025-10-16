/**
 * SsoForm.java 2009. 1. 30.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.vo;


/**
 * <p><strong>SsoInfoVO.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author 
 * @since Tims7
 * @version 7.0 
 */
public class SsoInfoVO {


	private boolean applied;
	private String method, ssoKey, algorithm;
	
	private String key;
	
	public boolean isApplied() {
		return applied;
	}
	public void setApplied(boolean applied) {
		this.applied = applied;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getSsoKey() {
		return ssoKey;
	}
	public void setSsoKey(String ssoKey) {
		this.ssoKey = ssoKey;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@Override
	public String toString() {
		StringBuffer tmpStr = new StringBuffer();
		tmpStr.append("\n");
		tmpStr.append("applied = [").append(applied).append("]");
		tmpStr.append("\n");
		tmpStr.append("method = [").append(method).append("]");
		tmpStr.append("\n");
		tmpStr.append("ssoKey = [").append(ssoKey).append("]");
		tmpStr.append("\n");
		tmpStr.append("alrotithm = [").append(algorithm).append("]");
		tmpStr.append("\n");
	
		return tmpStr.toString();
	}
}