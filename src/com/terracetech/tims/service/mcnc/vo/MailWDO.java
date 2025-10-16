package com.terracetech.tims.service.mcnc.vo;

import java.io.Serializable;

/**
 * 메일 데이터를 표현한다.
 * @author kevin
 *
 */
public class MailWDO implements Serializable{

	private static final long serialVersionUID = -2701104786050445526L;

	/**
	 * 첨부파일 없음
	 */
	public static final int		FLAG_ATTACHMENTS_NO				= 0;
	
	/**
	 * 첨부파일 있음
	 */
	public static final int		FLAG_ATTACHMENTS_INCLUDED		= 1;
	
	/**
	 * 첨부파일은 있으나, 이 데이터 오브젝트에 포함되지 않음
	 */
	public static final int		FLAG_ATTACHMENTS_NOT_INCLUDED	= 2;

	private String				mailID;
	private String				subject;
	private BodyWDO				body;
	private ContactWDO			from;
	private ContactWDO[]		to;
	private ContactWDO[]		cc;
	private ContactWDO[]			bcc;
	private int mailSize;
	private int importance;	
	private String sentDate;
	private String sentTime;
	private String receivedDate;
	private String receivedTime;
	private int security;		
	private PayloadWDO[] payload;

	private int					flagAttachments	= FLAG_ATTACHMENTS_NO;
	private AttachmentInfoWDO[]	attachmentInfos	= new AttachmentInfoWDO[ 0 ];
	private AttachmentWDO[]		attachments		= new AttachmentWDO[ 0 ];
	
	/**
	 * 메일 ID를 얻는다.
	 * @return 메일 ID
	 */
	public String getMailID() {
		return mailID;
	}
	
	/**
	 * 메일 ID를 세팅한다.
	 * @param mailID 메일 ID
	 */
	public void setMailID(String mailID) {
		this.mailID = mailID;
	}
	
	/**
	 * 메일 제목을 얻는다.
	 * @return 메일 제목
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * 메일 제목을 세팅한다.
	 * @param subject 메일 제목
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * 메일 본문을 얻는다.
	 * @return 메일 본문
	 */
	public BodyWDO getBody() {
		return body;
	}
	
	/**
	 * 메일 본문을 세팅한다.
	 * @param body 메일 본문
	 */
	public void setBody(BodyWDO body) {
		this.body = body;
	}
	
	/**
	 * 메일 보낸 날짜를 얻는다.
	 * 날짜 형식은 예를들어, 2010년 2월 25일 14시 27분 35초 라면 20100225142735로 표현됨(14bytes)
	 * @return 메일 보낸 날짜
	 */
	public String getSentDate() {
		return sentDate;
	}
	
	/**
	 * 메일 보낸 날짜를 세팅한다.
	 * 날짜 형식은 예를들어, 2010년 2월 25일 14시 27분 35초 라면 20100225142735로 표현됨(14bytes)
	 * @param sentDate 메일 보낸 날짜
	 */
	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}
	
	
	/**
	 * 첨부 파일 정보 리스트를 얻는다.
	 * @return 첨부 파일 정보 리스트
	 */
	public AttachmentInfoWDO[] getAttachmentInfos() {
		return attachmentInfos;
	}

	/**
	 * 첨부 파일 정보 리스트를 세팅한다.
	 * @param attachmentInfos 첨부 파일 정보 리스트
	 */
	public void setAttachmentInfos(AttachmentInfoWDO[] attachmentInfos) {
		this.attachmentInfos = attachmentInfos;
	}

	/**
	 * 첨부 파일 데이터 리스트를 얻는다.
	 * @return 첨부 파일 데이터 리스트
	 */
	public AttachmentWDO[] getAttachments() {
		return attachments;
	}
	
	/**
	 * 첨부 파일 데이터 리스트를 세팅한다.
	 * @param attachments 첨부 파일 데이터 리스트
	 */
	public void setAttachments(AttachmentWDO[] attachments) {
		this.attachments = attachments;
	}
	
	/**
	 * 첨부 파일 유무를 얻는다.
	 * MailWDO.FLAG_ATTACHMENTS_NO
	 * MailWDO.FLAG_ATTACHMENTS_INCLUDED
	 * MailWDO.FLAG_ATTACHMENTS_NOT_INCLUDED
	 * @return 첨부 파일 유무
	 */
	public int getFlagAttachments() {
		return flagAttachments;
	}
	
	/**
	 * 첨부 파일 유무를 세팅한다.
	 * MailWDO.FLAG_ATTACHMENTS_NO
	 * MailWDO.FLAG_ATTACHMENTS_INCLUDED
	 * MailWDO.FLAG_ATTACHMENTS_NOT_INCLUDED 
	 * @param flagAttachments
	 */
	public void setFlagAttachments( int flagAttachments ) {
		this.flagAttachments = flagAttachments;
	}

	public ContactWDO getFrom() {
		return from;
	}

	public void setFrom(ContactWDO from) {
		this.from = from;
	}

	public ContactWDO[] getTo() {
		return to;
	}

	public void setTo(ContactWDO[] to) {
		this.to = to;
	}

	public ContactWDO[] getCc() {
		return cc;
	}

	public void setCc(ContactWDO[] cc) {
		this.cc = cc;
	}

	public ContactWDO[] getBcc() {
		return bcc;
	}

	public void setBcc(ContactWDO[] bcc) {
		this.bcc = bcc;
	}

	public int getImportance() {
		return importance;
	}

	public void setImportance(int importance) {
		this.importance = importance;
	}


	public String getSentTime() {
		return sentTime;
	}

	public void setSentTime(String sentTime) {
		this.sentTime = sentTime;
	}

	public String getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(String receivedTime) {
		this.receivedTime = receivedTime;
	}

	public int getSecurity() {
		return security;
	}

	public void setSecurity(int security) {
		this.security = security;
	}

	public PayloadWDO[] getPayload() {
		return payload;
	}

	public void setPayload(PayloadWDO[] payload) {
		this.payload = payload;
	}

	public int getMailSize() {
		return mailSize;
	}

	public void setMailSize(int mailSize) {
		this.mailSize = mailSize;
	}
}