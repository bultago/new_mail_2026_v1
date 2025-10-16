/**
 * MailUserGroupVO.java 2008  2008-09-26
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.vo;

/**
 * <p><strong>MailUserGroupVO.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li>사용자가 속한 그룹의 정보를 가져오기 위한 VO. 그룹단위의 설정부분을 가져온다.</li>
 * <li>그룹명, 쿼터 관련 속성, 메일함 유효기간 설정, 기타 사용자 default 설정 정보를 포함.</li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MailUserGroupVO {
	
	/**
	 * <p>그룹명 정보</p>
	 */
	private String mailGroup = null;
	/**
	 * <p>그룹 별칭</p>
	 */
	private String mailGroupName = null;
	/**
	 * <p>그룹 기본 쿼터 용량. '쿼터용량S, 카운터수C' 형태로 저장.</p>
	 */
	private String quota = null;
	/**
	 * <p>그룹 웹폴더 쿼터 정보</p>
	 */
	private String webfolderQuota = null;
	/**
	 * <p>최대 포워딩 갯수</p>
	 */
	private String maxForwarding = null;
	/**
	 * <p>쿼터 초과 사용 비율. 1~100% 사이</p>
	 */
	private String quotaOverlookRatio = null;
	/**
	 * <p>쿼터 초과 경고 모드</p>
	 * <p>속성 : on/off</p>
	 */
	private String quotaWarningMode = null;
	/**
	 * <p>쿼터 위험 사용 비율. 1~100% 사이</p>
	 */
	private String quotaWarningRatio = null;
	/**
	 * <p>쿼터 오류 처리 방법</p>
	 */
	private String quotaViolationAction = null;
	/**
	 * <p>사용자가 사용하는 메일 서비스 종류. 숫자를 이용하여 설정. 설정시 각 서비스 숫자 값을 합산하여 정보를 설정</p>
	 * <p>속성 : ums(1) fax(2) pop(4) imap(8) webmail(16) security(32) voice(64) virus(128) smtpauth(256)</p>
	 * 
	 */
	private int mailServices = 0;
	/**
	 * <p>최대 메일 송신 크기</p>
	 */
	private String maxSendSize = null;
	/**
	 * <p>받은 메일함 유효기간 </p>
	 */
	private String inboxExpireDays = null;
	/**
	 * <p>보낸 메일함 유효기간 </p>
	 */
	private String sentExpireDays = null;
	/**
	 * <p>휴지통 메일함 유효기간</p>
	 */
	private String trashExpireDays = null;
	/**
	 * <p>스팸 메일함 유효기간</p>
	 */
	private String spamExpireDays = null;
	/**
	 * <p>사용자 메일함 유효기간 </p>
	 */
	private String userExpireDays = null;
	/**
	 * <p>그룹 에 대한 설명 정보</p>
	 */
	private String description = null;
	
	/**
	 * @return mailGroup 값 반환
	 */
	public String getMailGroup() {
		return mailGroup;
	}
	/**
	 * @param mailGroup 파라미터를 mailGroup값에 설정
	 */
	public void setMailGroup(String mailGroup) {
		this.mailGroup = mailGroup;
	}
	/**
	 * @return mailGroupName 값 반환
	 */
	public String getMailGroupName() {
		return mailGroupName;
	}
	/**
	 * @param mailGroupName 파라미터를 mailGroupName값에 설정
	 */
	public void setMailGroupName(String mailGroupName) {
		this.mailGroupName = mailGroupName;
	}
	/**
	 * @return quota 값 반환
	 */
	public String getQuota() {
		return quota;
	}
	/**
	 * @param quota 파라미터를 quota값에 설정
	 */
	public void setQuota(String quota) {
		this.quota = quota;
	}
	/**
	 * @return webfolderQuota 값 반환
	 */
	public String getWebfolderQuota() {
		return webfolderQuota;
	}
	/**
	 * @param webfolderQuota 파라미터를 webfolderQuota값에 설정
	 */
	public void setWebfolderQuota(String webfolderQuota) {
		this.webfolderQuota = webfolderQuota;
	}
	/**
	 * @return maxForwarding 값 반환
	 */
	public String getMaxForwarding() {
		return maxForwarding;
	}
	/**
	 * @param maxForwarding 파라미터를 maxForwarding값에 설정
	 */
	public void setMaxForwarding(String maxForwarding) {
		this.maxForwarding = maxForwarding;
	}
	/**
	 * @return quotaOverlookRatio 값 반환
	 */
	public String getQuotaOverlookRatio() {
		return quotaOverlookRatio;
	}
	/**
	 * @param quotaOverlookRatio 파라미터를 quotaOverlookRatio값에 설정
	 */
	public void setQuotaOverlookRatio(String quotaOverlookRatio) {
		this.quotaOverlookRatio = quotaOverlookRatio;
	}
	/**
	 * @return quotaWarningMode 값 반환
	 */
	public String getQuotaWarningMode() {
		return quotaWarningMode;
	}
	/**
	 * @param quotaWarningMode 파라미터를 quotaWarningMode값에 설정
	 */
	public void setQuotaWarningMode(String quotaWarningMode) {
		this.quotaWarningMode = quotaWarningMode;
	}
	/**
	 * @return quotaWarningRatio 값 반환
	 */
	public String getQuotaWarningRatio() {
		return quotaWarningRatio;
	}
	/**
	 * @param quotaWarningRatio 파라미터를 quotaWarningRatio값에 설정
	 */
	public void setQuotaWarningRatio(String quotaWarningRatio) {
		this.quotaWarningRatio = quotaWarningRatio;
	}
	/**
	 * @return quotaViolationAction 값 반환
	 */
	public String getQuotaViolationAction() {
		return quotaViolationAction;
	}
	/**
	 * @param quotaViolationAction 파라미터를 quotaViolationAction값에 설정
	 */
	public void setQuotaViolationAction(String quotaViolationAction) {
		this.quotaViolationAction = quotaViolationAction;
	}
	/**
	 * @return mailServices 값 반환
	 */
	public int getMailServices() {
		return mailServices;
	}
	/**
	 * @param mailServices 파라미터를 mailServices값에 설정
	 */
	public void setMailServices(int mailServices) {
		this.mailServices = mailServices;
	}
	/**
	 * @return maxSendSize 값 반환
	 */
	public String getMaxSendSize() {
		return maxSendSize;
	}
	/**
	 * @param maxSendSize 파라미터를 maxSendSize값에 설정
	 */
	public void setMaxSendSize(String maxSendSize) {
		this.maxSendSize = maxSendSize;
	}
	/**
	 * @return inboxExpireDays 값 반환
	 */
	public String getInboxExpireDays() {
		return inboxExpireDays;
	}
	/**
	 * @param inboxExpireDays 파라미터를 inboxExpireDays값에 설정
	 */
	public void setInboxExpireDays(String inboxExpireDays) {
		this.inboxExpireDays = inboxExpireDays;
	}
	/**
	 * @return sentExpireDays 값 반환
	 */
	public String getSentExpireDays() {
		return sentExpireDays;
	}
	/**
	 * @param sentExpireDays 파라미터를 sentExpireDays값에 설정
	 */
	public void setSentExpireDays(String sentExpireDays) {
		this.sentExpireDays = sentExpireDays;
	}
	/**
	 * @return trashExpireDays 값 반환
	 */
	public String getTrashExpireDays() {
		return trashExpireDays;
	}
	/**
	 * @param trashExpireDays 파라미터를 trashExpireDays값에 설정
	 */
	public void setTrashExpireDays(String trashExpireDays) {
		this.trashExpireDays = trashExpireDays;
	}
	/**
	 * @return spamExpireDays 값 반환
	 */
	public String getSpamExpireDays() {
		return spamExpireDays;
	}
	/**
	 * @param spamExpireDays 파라미터를 spamExpireDays값에 설정
	 */
	public void setSpamExpireDays(String spamExpireDays) {
		this.spamExpireDays = spamExpireDays;
	}
	/**
	 * @return userExpireDays 값 반환
	 */
	public String getUserExpireDays() {
		return userExpireDays;
	}
	/**
	 * @param userExpireDays 파라미터를 userExpireDays값에 설정
	 */
	public void setUserExpireDays(String userExpireDays) {
		this.userExpireDays = userExpireDays;
	}
	/**
	 * @return description 값 반환
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description 파라미터를 description값에 설정
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
