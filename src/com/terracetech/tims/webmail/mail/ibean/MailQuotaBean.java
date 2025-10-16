/**
 * MailQuotaBean.java 2008. 12. 9.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.ibean;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.util.FormatUtil;

/**
 * <p><strong>MailQuotaBean.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MailQuotaBean {
	long usage = 0L;
	long limit = 0L;
	int percent = 0;
	
	public MailQuotaBean(){}
	
	public MailQuotaBean(long usage, long limit){
		this.usage = usage;
		this.limit = limit;
	}
	
	
	/**
	 * @return usage 값 반환
	 */
	public long getUsage() {
		return usage;
	}
	
	public String getUsageUnit() {
		return FormatUtil.toUnitString(usage, 2);
	}
	/**
	 * @param usage 파라미터를 usage값에 설정
	 */
	public void setUsage(long usage) {
		this.usage = usage;
	}
	/**
	 * @return limit 값 반환
	 */
	public long getLimit() {
		return limit;
	}
	
	public String getLimitUnit() {
		return FormatUtil.toUnitString(limit, 2);
	}
	/**
	 * @param limit 파라미터를 limit값에 설정
	 */
	public void setLimit(long limit) {
		this.limit = limit;
	}
	/**
	 * @return percent 값 반환
	 */
	public int getPercent() {
		if(percent <= 0){
			 int pval = (int) ((double) ((usage/1024) * 1024 * 10) / limit * 100);
			percent = (int) java.lang.Math.round((double) pval / 10);
		}
		if(percent > 100){
			percent = 100;
		}
		return percent;
	}	
	/**
	 * @param percent 파라미터를 percent값에 설정
	 */
	public void setPercent(int percent) {
		this.percent = percent;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJson(){
		JSONObject jObj = new JSONObject();
		jObj.put("usage",FormatUtil.toUnitString(usage, 2));
		jObj.put("limit",FormatUtil.toUnitString(limit, 2));
		jObj.put("percent",percent);
		
		return jObj;
	}
	
	public boolean isOverQuota(){
		if(getPercent() == 100){
			return true;
		} else {
			return false;
		}		
	}
	
	
}
