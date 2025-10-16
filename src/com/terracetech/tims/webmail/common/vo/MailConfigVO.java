/**
 * MailConfigVO.java 2009. 1. 7.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.common.vo;

/**
 * <p><strong>MailConfigVO.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class MailConfigVO {

	public final static String CONFIG_NAME = "CONFIG_NAME";
	public final static String CONFIG_VALUE = "CONFIG_VALUE";
	
	private String configName, configValue;

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	@Override
	public String toString() {
		StringBuffer tmpStr = new StringBuffer();
		tmpStr.append("\n");
		tmpStr.append("configName = [").append(configName).append("]");
		tmpStr.append("\n");
		tmpStr.append("configValue = [").append(configValue).append("]");
		tmpStr.append("\n");

		return tmpStr.toString();
	}
	
	
}