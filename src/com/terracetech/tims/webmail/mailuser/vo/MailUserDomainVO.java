/**
 * MailUserDomainVO.java 2008  2008-09-26
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.vo;

/**
 * <p><strong>MailUserDomainVO.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li>사용자의 도메인 정보 를 가져오는VO. 도메인에 관한 기본 정보를 표현</li>
 * <li>도메인명, 서비스 시작 시간, 패스워드 암호화 방식, 현재 사용자수 메일서비스 등의 옵션 항목 포함</li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MailUserDomainVO {
	/**
	 * <p>메일 도메인 명</p>
	 */
	private String mailDomain = null;
	/**
	 * <p>메일 도메인 별칭</p>
	 */
	private String mailDomainName = null;
	/**
	 * <p>서비스 시작 날짜</p>
	 */
	private String serviceStartTime = null;
	/**
	 * <p>서비스 종료 날짜</p>
	 */
	private String serviceEndTime = null;
	/**
	 * <p>도메인 사용 여부</p>
	 * <p>속성 : enabled / disabled</p>
	 */
	private String domainStatus = null;
	/**
	 * <p>최대 사용자수</p>
	 */
	private String maxUserCount = null;
	/**
	 * <p>현재 사용자수</p>
	 */
	private String currentUserCount = null;
	/**
	 * <p>비밀번호 암호화 방식</p>
	 */	
	private String passwordMethod = null;
	/**
	 * <p>사용자가 사용하는 메일 서비스 종류. 숫자를 이용하여 설정. 설정시 각 서비스 숫자 값을 합산하여 정보를 설정</p>
	 * <p>속성 : ums(1) fax(2) pop(4) imap(8) webmail(16) security(32) voice(64) virus(128) smtpauth(256)</p>
	 */
	private String mailServices = null;
	/**
	 * <p>스팸 메일 정책 설정</p>
	 */
	private String defaultPspam = null;
	/**
	 * <p>도메인 필터 사용.</p>
	 */
	private String filterApply = null;
	/**
	 * @return mailDomain 값 반환
	 */
	public String getMailDomain() {
		return mailDomain;
	}
	/**
	 * @param mailDomain 파라미터를 mailDomain값에 설정
	 */
	public void setMailDomain(String mailDomain) {
		this.mailDomain = mailDomain;
	}
	/**
	 * @return mailDomainName 값 반환
	 */
	public String getMailDomainName() {
		return mailDomainName;
	}
	/**
	 * @param mailDomainName 파라미터를 mailDomainName값에 설정
	 */
	public void setMailDomainName(String mailDomainName) {
		this.mailDomainName = mailDomainName;
	}
	/**
	 * @return serviceStartTime 값 반환
	 */
	public String getServiceStartTime() {
		return serviceStartTime;
	}
	/**
	 * @param serviceStartTime 파라미터를 serviceStartTime값에 설정
	 */
	public void setServiceStartTime(String serviceStartTime) {
		this.serviceStartTime = serviceStartTime;
	}
	/**
	 * @return serviceEndTime 값 반환
	 */
	public String getServiceEndTime() {
		return serviceEndTime;
	}
	/**
	 * @param serviceEndTime 파라미터를 serviceEndTime값에 설정
	 */
	public void setServiceEndTime(String serviceEndTime) {
		this.serviceEndTime = serviceEndTime;
	}
	/**
	 * @return domainStatus 값 반환
	 */
	public String getDomainStatus() {
		return domainStatus;
	}
	/**
	 * @param domainStatus 파라미터를 domainStatus값에 설정
	 */
	public void setDomainStatus(String domainStatus) {
		this.domainStatus = domainStatus;
	}
	/**
	 * @return maxUserCount 값 반환
	 */
	public String getMaxUserCount() {
		return maxUserCount;
	}
	/**
	 * @param maxUserCount 파라미터를 maxUserCount값에 설정
	 */
	public void setMaxUserCount(String maxUserCount) {
		this.maxUserCount = maxUserCount;
	}
	/**
	 * @return currentUserCount 값 반환
	 */
	public String getCurrentUserCount() {
		return currentUserCount;
	}
	/**
	 * @param currentUserCount 파라미터를 currentUserCount값에 설정
	 */
	public void setCurrentUserCount(String currentUserCount) {
		this.currentUserCount = currentUserCount;
	}
	/**
	 * @return passwordMethod 값 반환
	 */
	public String getPasswordMethod() {
		return passwordMethod;
	}
	/**
	 * @param passwordMethod 파라미터를 passwordMethod값에 설정
	 */
	public void setPasswordMethod(String passwordMethod) {
		this.passwordMethod = passwordMethod;
	}
	/**
	 * @return mailServices 값 반환
	 */
	public String getMailServices() {
		return mailServices;
	}
	/**
	 * @param mailServices 파라미터를 mailServices값에 설정
	 */
	public void setMailServices(String mailServices) {
		this.mailServices = mailServices;
	}
	/**
	 * @return defaultPspam 값 반환
	 */
	public String getDefaultPspam() {
		return defaultPspam;
	}
	/**
	 * @param defaultPspam 파라미터를 defaultPspam값에 설정
	 */
	public void setDefaultPspam(String defaultPspam) {
		this.defaultPspam = defaultPspam;
	}
	/**
	 * @return filterApply 값 반환
	 */
	public String getFilterApply() {
		return filterApply;
	}
	/**
	 * @param filterApply 파라미터를 filterApply값에 설정
	 */
	public void setFilterApply(String filterApply) {
		this.filterApply = filterApply;
	}

	
	
}
