/**
 * MailDomainVO.java 2008. 12. 9.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.vo;

/**
 * <p><strong>MailDomainVO.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class MailDomainVO {
	private int 
		mailDomainSeq, mailServices;
	
	private String 
		mailDomain, mailDomainName, serviceStartTime, serviceEndTime, 
		domainStatus, maxUserCount, currentUserCount, passwordMethod, 
		defaultPspam, filterApply, urlAddress;
	
	public int getMailDomainSeq() {
		return mailDomainSeq;
	}
	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}
	public int getMailServices() {
		return mailServices;
	}
	public void setMailServices(int mailServices) {
		this.mailServices = mailServices;
	}
	public String getMailDomain() {
		return mailDomain;
	}
	public void setMailDomain(String mailDomain) {
		this.mailDomain = mailDomain;
	}
	public String getMailDomainName() {
		return mailDomainName;
	}
	public void setMailDomainName(String mailDomainName) {
		this.mailDomainName = mailDomainName;
	}
	public String getServiceStartTime() {
		return serviceStartTime;
	}
	public void setServiceStartTime(String serviceStartTime) {
		this.serviceStartTime = serviceStartTime;
	}
	public String getServiceEndTime() {
		return serviceEndTime;
	}
	public void setServiceEndTime(String serviceEndTime) {
		this.serviceEndTime = serviceEndTime;
	}
	public String getDomainStatus() {
		return domainStatus;
	}
	public void setDomainStatus(String domainStatus) {
		this.domainStatus = domainStatus;
	}
	public String getMaxUserCount() {
		return maxUserCount;
	}
	public void setMaxUserCount(String maxUserCount) {
		this.maxUserCount = maxUserCount;
	}
	public String getCurrentUserCount() {
		return currentUserCount;
	}
	public void setCurrentUserCount(String currentUserCount) {
		this.currentUserCount = currentUserCount;
	}
	public String getPasswordMethod() {
		return passwordMethod;
	}
	public void setPasswordMethod(String passwordMethod) {
		this.passwordMethod = passwordMethod;
	}
	public String getDefaultPspam() {
		return defaultPspam;
	}
	public void setDefaultPspam(String defaultPspam) {
		this.defaultPspam = defaultPspam;
	}
	public String getFilterApply() {
		return filterApply;
	}
	public void setFilterApply(String filterApply) {
		this.filterApply = filterApply;
	}
	public String getUrlAddress() {
		return urlAddress;
	}
	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
	}
}
