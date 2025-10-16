package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;

public class HeaderESBVO implements Serializable{

	private static final long serialVersionUID = 20091102L;
	
	/**
	 * 메일 메시지 번호
	 */
	private int iMsgNo;
	
	/**
	 * 제목
	 */
	private String subject;
	
	/**
	 * 발신인 이름
	 */
	private String personName;
	
	/**
	 * 발신자 메일 주소
	 */
	private String personEmail;
	
	/**
	 * 직급
	 */
	private String position;
	
	/**
	 * 부서
	 */
	private String department;
	
	/**
	 * 회사
	 */
	private String company;
	
	/**
	 * 발신인 조직정보
	 */
	private String personTitle;
	
	/**
	 * 발신날짜
	 *  “2007030212”  YYYYmmdd
	 */
	private String sentDate;
	
	/**
	 * 메일 종류
	 */
	private String mailType;
	
	/**
	 * 긴급도 우선순위
	 */
	private String priority;
	
	/**
	 * 배달 속성
	 */
	private String delivery;
	
	/**
	 * 개봉 확인 여부
	 */
	private boolean bMailOpenCheck;
	
	/**
	 * Attachment 유무
	 */
	private boolean bAttachCheck;
	
	/**
	 * locale 다름 유무
	 */
	private boolean bSameLocale;
	
	/**
	 * MTR-Key
	 */
	private String mtrKey;
	
	/**
	 * 원본 메일 ID
	 */
	private String messageId;
	
	/**
	 * 메일함 폴더명
	 */
	private String folderName;
	
	/**
	 * 수신상태 정보
	 */
	private String statusCount;
	
	/**
	 * 첫번째 수신인 정보
	 */
	private String firstRecp;
	
	/**
	 * 수신인 갯수
	 */
	private int countRecp;
	
	/**
	 * 문자셋
	 */
	private String charset;
	
	/**
	 * 중요표시 check 여부
	 */
	private boolean bFlaggedCheck;
	
	/**
	 * 메일 크기
	 */
	private long size;
	
	/**
	 * 재전송,답장 여부
	 */
	private String userFlagType;
	
	/**
	 * 첨부 file 분리여부
	 */
	private boolean bExternal;
	
	/**
	 * 스팸 표시
	 */
	private String tSpammark;

	/**
	 * 첨부 전체 Size
	 */
	private String exTotSize;
	
	/**
	 * Message UID
	 */
	private String msgUID;

	public int getiMsgNo() {
		return iMsgNo;
	}

	public void setiMsgNo(int iMsgNo) {
		this.iMsgNo = iMsgNo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonEmail() {
		return personEmail;
	}

	public void setPersonEmail(String personEmail) {
		this.personEmail = personEmail;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPersonTitle() {
		return personTitle;
	}

	public void setPersonTitle(String personTitle) {
		this.personTitle = personTitle;
	}

	public String getSentDate() {
		return sentDate;
	}

	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}

	public String getMailType() {
		return mailType;
	}

	public void setMailType(String mailType) {
		this.mailType = mailType;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public boolean isbMailOpenCheck() {
		return bMailOpenCheck;
	}

	public void setbMailOpenCheck(boolean bMailOpenCheck) {
		this.bMailOpenCheck = bMailOpenCheck;
	}

	public boolean isbAttachCheck() {
		return bAttachCheck;
	}

	public void setbAttachCheck(boolean bAttachCheck) {
		this.bAttachCheck = bAttachCheck;
	}

	public boolean isbSameLocale() {
		return bSameLocale;
	}

	public void setbSameLocale(boolean bSameLocale) {
		this.bSameLocale = bSameLocale;
	}

	public String getMtrKey() {
		return mtrKey;
	}

	public void setMtrKey(String mtrKey) {
		this.mtrKey = mtrKey;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getStatusCount() {
		return statusCount;
	}

	public void setStatusCount(String statusCount) {
		this.statusCount = statusCount;
	}

	public String getFirstRecp() {
		return firstRecp;
	}

	public void setFirstRecp(String firstRecp) {
		this.firstRecp = firstRecp;
	}

	public int getCountRecp() {
		return countRecp;
	}

	public void setCountRecp(int countRecp) {
		this.countRecp = countRecp;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public boolean isbFlaggedCheck() {
		return bFlaggedCheck;
	}

	public void setbFlaggedCheck(boolean bFlaggedCheck) {
		this.bFlaggedCheck = bFlaggedCheck;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getUserFlagType() {
		return userFlagType;
	}

	public void setUserFlagType(String userFlagType) {
		this.userFlagType = userFlagType;
	}

	public boolean isbExternal() {
		return bExternal;
	}

	public void setbExternal(boolean bExternal) {
		this.bExternal = bExternal;
	}

	public String gettSpammark() {
		return tSpammark;
	}

	public void settSpammark(String tSpammark) {
		this.tSpammark = tSpammark;
	}

	public String getExTotSize() {
		return exTotSize;
	}

	public void setExTotSize(String exTotSize) {
		this.exTotSize = exTotSize;
	}

	public String getMsgUID() {
		return msgUID;
	}

	public void setMsgUID(String msgUID) {
		this.msgUID = msgUID;
	}
	
}
