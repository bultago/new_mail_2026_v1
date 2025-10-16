/**
 * MailUserSystemVO.java 2008  2008-09-26
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */

package com.terracetech.tims.webmail.mailuser.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <p>
 * <strong>MailUserSystemVO</strong> 클래스 명세
 * </p>
 * <ul>
 * <li>사용자 정보를 가져오는 VO. 사용자의 기본적인 시스템 정보를 가져온다.</li>
 * <li>Key 가되는 정보는 Sequence 정보로서 Domain, Group 의 Sequence 정보도 포함.</li>
 * <li>사용자에게 개개별로 설정된 Quota, Forwarding,AutoReply,delivery 등에관한 정보 포함.</li>
 * </ul>
 * 
 * @author sshyun
 * @since TIMS7
 * @version 7.0
 * 
 */
public class MailUserSystemVO {	
	
	/**
	 * <p>
	 * 사용자 Sequence 번호 정보
	 * </p>
	 */
	private int seqNum = 0;
	/**
	 * <p>
	 * 사용자 ID 정보
	 * </p>
	 */
	private String uid = null;
	/**
	 * <p>
	 * 사용자 Domain 정보
	 * </p>
	 */
	private String domain = null;
	/**
	 * <p>
	 * 사용자 Domain Sequence 번호
	 * </p>
	 */
	private int domainSeqNum = 0;
	/**
	 * <p>
	 * 사용자 Group 정보
	 * </p>
	 */
	private String group = null;
	/**
	 * <p>
	 * 사용자 Group Sequence 번호정보
	 */
	private int groupSeqNum = 0;
	/**
	 * <p>
	 * 사용자 Password 정보
	 * </p>
	 */
	private String password = null;
	/**
	 * <p>
	 * 사용자 Mail HOST 정보. 등록되어있는 메일 서버의 호스트 정보.
	 * </p>
	 */
	private String host = null;
	/**
	 * <p>
	 * 사용자 Mail Store 정보. 메일 서버에 저장 되는 메일의 스토리지 위치 정보
	 */
	private String store = null;	
	/**
	 * <p>사용자 계정의 Expire 시간 (yyyyMMdd)</p>
	 */
	private String expireTime = null;
	/**
	 * <p>사용자 계정의 사용 상태.</p>
	 * <p>속성 : enabled, disabled</p>
	 */
	private String status = null;
	/**
	 * <p>사용자 계정 종류.</p>
	 * <p>속성 : mailuser</p>
	 */	
	private String userType = null;		
	/**
	 * <p>사용자가 사용하는 메일 서비스 종류. 숫자를 이용하여 설정. 설정시 각 서비스 숫자 값을 합산하여 정보를 설정</p>
	 * <p>속성 : ums(1) fax(2) pop(4) imap(8) webmail(16) security(32) voice(64) virus(128) smtpauth(256)</p>
	 * 
	 */
	private int mailServices = 0;
	/**
	 * <p>사용자 추가 쿼터정보. '쿼터용량S, 카운터수C' 형태로 저장.</p>
	 */
	private String addQuota = null;
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
	 * <p>휴지통 메일함 유효기간 </p>
	 */
	private String trashExpireDays = null;
	/**
	 * <p>스팸 메일함 유효기간 </p>
	 */
	private String spamExpireDays = null;
	/**
	 * <p>사용자 메일함 유효기간 </p>
	 */
	private String userExpireDays = null;

	/**
	 * <p>전달시 알림 종류</p>
	 */
	private String deliveryNotiType = null;
	/**
	 * <p>전달시 알림 모드</p>
	 */
	private String deliveryNotiMode = null;

	/**
	 * <p>전달 설정</p>
	 */
	private String forwardingMode = null;
	/**
	 * <p>숨김 전달 설정</p>
	 */
	private String hiddenForwardingMode = null;

	/**
	 * <p>자동응답 설정</p>
	 */
	private String autoReplyMode = null;
	/**
	 * <p>자동 응답 </p>
	 */
	private String autoReplyInclude = null;
	/**
	 * <p>자동응답 시작할 날짜</p>
	 */
	private String autoReplyStartTime = null;
	/**
	 * <p>자동응답 종료 날짜</p>
	 */
	private String autoReplyEndTime = null;
	/**
	 * <p>자동응답 문구</p>
	 */
	private String autoReplyText = null;
	/**
	 * <p>자동응답 제목</p>
	 */
	private String autoReplySubject = null;


    
	/**
	 * @return seqNum 값 반환
	 */
	public int getSeqNum() {
		return seqNum;
	}



	/**
	 * @param seqNum 파라미터를 seqNum값에 설정
	 */
	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}



	/**
	 * @return uid 값 반환
	 */
	public String getUid() {
		return uid;
	}



	/**
	 * @param uid 파라미터를 uid값에 설정
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}



	/**
	 * @return domain 값 반환
	 */
	public String getDomain() {
		return domain;
	}



	/**
	 * @param domain 파라미터를 domain값에 설정
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}



	/**
	 * @return domainSeqNum 값 반환
	 */
	public int getDomainSeqNum() {
		return domainSeqNum;
	}



	/**
	 * @param domainSeqNum 파라미터를 domainSeqNum값에 설정
	 */
	public void setDomainSeqNum(int domainSeqNum) {
		this.domainSeqNum = domainSeqNum;
	}



	/**
	 * @return group 값 반환
	 */
	public String getGroup() {
		return group;
	}



	/**
	 * @param group 파라미터를 group값에 설정
	 */
	public void setGroup(String group) {
		this.group = group;
	}



	/**
	 * @return groupSeqNum 값 반환
	 */
	public int getGroupSeqNum() {
		return groupSeqNum;
	}



	/**
	 * @param groupSeqNum 파라미터를 groupSeqNum값에 설정
	 */
	public void setGroupSeqNum(int groupSeqNum) {
		this.groupSeqNum = groupSeqNum;
	}



	/**
	 * @return password 값 반환
	 */
	public String getPassword() {
		return password;
	}



	/**
	 * @param password 파라미터를 password값에 설정
	 */
	public void setPassword(String password) {
		this.password = password;
	}



	/**
	 * @return host 값 반환
	 */
	public String getHost() {
		return host;
	}



	/**
	 * @param host 파라미터를 host값에 설정
	 */
	public void setHost(String host) {
		this.host = host;
	}



	/**
	 * @return store 값 반환
	 */
	public String getStore() {
		return store;
	}



	/**
	 * @param store 파라미터를 store값에 설정
	 */
	public void setStore(String store) {
		this.store = store;
	}



	/**
	 * @return expireTime 값 반환
	 */
	public String getExpireTime() {
		return expireTime;
	}



	/**
	 * @param expireTime 파라미터를 expireTime값에 설정
	 */
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}



	/**
	 * @return status 값 반환
	 */
	public String getStatus() {
		return status;
	}



	/**
	 * @param status 파라미터를 status값에 설정
	 */
	public void setStatus(String status) {
		this.status = status;
	}



	/**
	 * @return userType 값 반환
	 */
	public String getUserType() {
		return userType;
	}



	/**
	 * @param userType 파라미터를 userType값에 설정
	 */
	public void setUserType(String userType) {
		this.userType = userType;
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
	 * @return addQuota 값 반환
	 */
	public String getAddQuota() {
		return addQuota;
	}



	/**
	 * @param addQuota 파라미터를 addQuota값에 설정
	 */
	public void setAddQuota(String addQuota) {
		this.addQuota = addQuota;
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
	 * @return deliveryNotiType 값 반환
	 */
	public String getDeliveryNotiType() {
		return deliveryNotiType;
	}



	/**
	 * @param deliveryNotiType 파라미터를 deliveryNotiType값에 설정
	 */
	public void setDeliveryNotiType(String deliveryNotiType) {
		this.deliveryNotiType = deliveryNotiType;
	}



	/**
	 * @return deliveryNotiMode 값 반환
	 */
	public String getDeliveryNotiMode() {
		return deliveryNotiMode;
	}



	/**
	 * @param deliveryNotiMode 파라미터를 deliveryNotiMode값에 설정
	 */
	public void setDeliveryNotiMode(String deliveryNotiMode) {
		this.deliveryNotiMode = deliveryNotiMode;
	}



	/**
	 * @return forwardingMode 값 반환
	 */
	public String getForwardingMode() {
		return forwardingMode;
	}



	/**
	 * @param forwardingMode 파라미터를 forwardingMode값에 설정
	 */
	public void setForwardingMode(String forwardingMode) {
		this.forwardingMode = forwardingMode;
	}



	/**
	 * @return hiddenForwardingMode 값 반환
	 */
	public String getHiddenForwardingMode() {
		return hiddenForwardingMode;
	}



	/**
	 * @param hiddenForwardingMode 파라미터를 hiddenForwardingMode값에 설정
	 */
	public void setHiddenForwardingMode(String hiddenForwardingMode) {
		this.hiddenForwardingMode = hiddenForwardingMode;
	}



	/**
	 * @return autoReplyMode 값 반환
	 */
	public String getAutoReplyMode() {
		return autoReplyMode;
	}



	/**
	 * @param autoReplyMode 파라미터를 autoReplyMode값에 설정
	 */
	public void setAutoReplyMode(String autoReplyMode) {
		this.autoReplyMode = autoReplyMode;
	}



	/**
	 * @return autoReplyInclude 값 반환
	 */
	public String getAutoReplyInclude() {
		return autoReplyInclude;
	}



	/**
	 * @param autoReplyInclude 파라미터를 autoReplyInclude값에 설정
	 */
	public void setAutoReplyInclude(String autoReplyInclude) {
		this.autoReplyInclude = autoReplyInclude;
	}



	/**
	 * @return autoReplyStartTime 값 반환
	 */
	public String getAutoReplyStartTime() {
		return autoReplyStartTime;
	}



	/**
	 * @param autoReplyStartTime 파라미터를 autoReplyStartTime값에 설정
	 */
	public void setAutoReplyStartTime(String autoReplyStartTime) {
		this.autoReplyStartTime = autoReplyStartTime;
	}



	/**
	 * @return autoReplyEndTime 값 반환
	 */
	public String getAutoReplyEndTime() {
		return autoReplyEndTime;
	}



	/**
	 * @param autoReplyEndTime 파라미터를 autoReplyEndTime값에 설정
	 */
	public void setAutoReplyEndTime(String autoReplyEndTime) {
		this.autoReplyEndTime = autoReplyEndTime;
	}



	/**
	 * @return autoReplyText 값 반환
	 */
	public String getAutoReplyText() {		
		return autoReplyText;
	}



	/**
	 * @param autoReplyText 파라미터를 autoReplyText값에 설정
	 */
	public void setAutoReplyText(String autoReplyText) {
		this.autoReplyText = autoReplyText;
	}


	
	/**
	 * <p>사용자 기본정보를 Map 형태로 생성하여 반환.</p>
	 * <p>
	 * Map 포함 정보 : id, idseq, domain, domainseq, group, groupseq, email, password, host, store,expiretime, accountstatus, usertype, maxsendsize
	 * 
	 * @return Map<String,String>
	 */
	public Map<String, String> getMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", uid);
		map.put("idseq", Integer.toString(seqNum));
		map.put("domain", domain);
		map.put("domainseq", Integer.toString(domainSeqNum));
		map.put("group", group);
		map.put("groupseq", Integer.toString(groupSeqNum));
		map.put("email", uid + "@" + domain);
		map.put("password", password);
		map.put("host", host);
		map.put("store", store);
		map.put("expiretime", expireTime);
		map.put("accountstatus", status);
		map.put("usertype", userType);
		map.put("maxsendsize", maxSendSize);
		return map;
	}

	public String getAutoReplySubject() {
		return autoReplySubject;
	}

	public void setAutoReplySubject(String autoReplySubject) {
		this.autoReplySubject = autoReplySubject;
	}
}
