package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;

public class ConditionCSVO implements Serializable{

	private static final long serialVersionUID = 20091102L;
	
	/**
	 * 목록 정렬 항목
	 */
	private int iSortType;
	
	/**
	 * 메일 선택 타입
	 */
	private int iSelectType;

	/**
	 * 페이지 번호
	 */
	private int iPageNo;
	
	/**
	 * 페이지당 항목 수
	 */
	private int iLinePerPage;
	
	/**
	 * 정렬 타입
	 */
	private boolean bSortOrder;
	
	/**
	 * 정렬 기본조건
	 */
	private boolean bDefaultRefCheck;
	
	/**
	 * Sub-Folders 검색 여부
	 */
	private boolean bSubFolderCheck;
	
	/**
	 * Private/Official 구분
	 */
	private int iOfficialType;
	
	/**
	 * And 검색 여부
	 */
	private boolean isAnd;
	
	/**
	 * 검색어 (Sender)
	 */
	private String senderKeyword;
	
	/**
	 * 검색어 (수신타입)
	 * 수신 : dave@samsung.com  
 	 * 참조 : mustaine@samsung.com 
	 */
	private String receiveKeyword;
	
	/**
	 * 검색어 (Recipient)
	 */
	private String recipientKeyword;
	
	/**
	 * 검색어 (Subject)
	 *  “2009-03-21 01:00:00”
	 */
	private String subjectKeyword;
	
	/**
	 * 검색어 (Mail Body)
	 *  “2009-03-23 01:00:00”
	 */
	private String bodyKeyword;
	
	/**
	 *  시작일  GMT+9 
	 */
	private String fromDate;
	
	/**
	 * 종료일
	 */
	private String toDate;
	
	private String timeZone;
	
	/**
	 * 메일함 폴더명
	 */
	private String folderName;
	
	/**
	 * 전체 수신인정보 요청 옵션
	 */
	private boolean bFullRecipientsInfo;
	
	/**
	 * 개봉메일 미처리 옵션
	 */
	private boolean bSkipSeenCheck;

	public int getiSortType() {
		return iSortType;
	}

	public void setiSortType(int iSortType) {
		this.iSortType = iSortType;
	}

	public int getiSelectType() {
		return iSelectType;
	}

	public void setiSelectType(int iSelectType) {
		this.iSelectType = iSelectType;
	}

	public int getiPageNo() {
		return iPageNo;
	}

	public void setiPageNo(int iPageNo) {
		this.iPageNo = iPageNo;
	}

	public int getiLinePerPage() {
		return iLinePerPage;
	}

	public void setiLinePerPage(int iLinePerPage) {
		this.iLinePerPage = iLinePerPage;
	}

	public boolean isbSortOrder() {
		return bSortOrder;
	}

	public void setbSortOrder(boolean bSortOrder) {
		this.bSortOrder = bSortOrder;
	}

	public boolean isbDefaultRefCheck() {
		return bDefaultRefCheck;
	}

	public void setbDefaultRefCheck(boolean bDefaultRefCheck) {
		this.bDefaultRefCheck = bDefaultRefCheck;
	}

	public boolean isbSubFolderCheck() {
		return bSubFolderCheck;
	}

	public void setbSubFolderCheck(boolean bSubFolderCheck) {
		this.bSubFolderCheck = bSubFolderCheck;
	}

	public int getiOfficialType() {
		return iOfficialType;
	}

	public void setiOfficialType(int iOfficialType) {
		this.iOfficialType = iOfficialType;
	}

	public boolean isAnd() {
		return isAnd;
	}

	public void setAnd(boolean isAnd) {
		this.isAnd = isAnd;
	}

	public String getSenderKeyword() {
		return senderKeyword;
	}

	public void setSenderKeyword(String senderKeyword) {
		this.senderKeyword = senderKeyword;
	}

	public String getReceiveKeyword() {
		return receiveKeyword;
	}

	public void setReceiveKeyword(String receiveKeyword) {
		this.receiveKeyword = receiveKeyword;
	}

	public String getRecipientKeyword() {
		return recipientKeyword;
	}

	public void setRecipientKeyword(String recipientKeyword) {
		this.recipientKeyword = recipientKeyword;
	}

	public String getSubjectKeyword() {
		return subjectKeyword;
	}

	public void setSubjectKeyword(String subjectKeyword) {
		this.subjectKeyword = subjectKeyword;
	}

	public String getBodyKeyword() {
		return bodyKeyword;
	}

	public void setBodyKeyword(String bodyKeyword) {
		this.bodyKeyword = bodyKeyword;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public boolean isbFullRecipientsInfo() {
		return bFullRecipientsInfo;
	}

	public void setbFullRecipientsInfo(boolean bFullRecipientsInfo) {
		this.bFullRecipientsInfo = bFullRecipientsInfo;
	}

	public boolean isbSkipSeenCheck() {
		return bSkipSeenCheck;
	}

	public void setbSkipSeenCheck(boolean bSkipSeenCheck) {
		this.bSkipSeenCheck = bSkipSeenCheck;
	}
	
}
