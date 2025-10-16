package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;


public class DetailESBVO implements Serializable{

	private static final long serialVersionUID = 20091102L;
	
	/**
	 * 본문
	 *  UTF-8 스트링
	 */
	private String body;
	
	/**
	 * 본문 type
	 */
	private String contentType;
	
	/**
	 * 답장요구
	 */
	private String requestReply;
	
	/**
	 * 답장 메일 주소
	 */
	private String replyTo;
	
	/**
	 * 이전 메일 번호
	 */
	private int iPrevMsgNo;
	
	/**
	 * 이후 메일 번호
	 */
	private String iNextMsgNo;
	
	/**
	 * 이전 메일함 폴더
	 */
	private String prevFolderName;
	
	/**
	 * 이후 메일함 폴더
	 */
	private String nextFolderName;
	
	/**
	 * 이전 메일 제목
	 */
	private String prevSubject;
	
	/**
	 * 이후 메일 제목
	 */
	private String nextSubject;
	
	/**
	 * 이전 메일 발신인
	 */
	private String prevPersonInfo;
	
	/**
	 * 이후 메일 발신인
	 */
	private String nextPersonInfo;
	
	/**
	 * 현재 page 번호
	 */
	private int iPageNo;
	
	/**
	 * 수신인 수
	 */
	private int iCountTO;
	
	/**
	 * 참조인 수
	 */
	private int iCountCC;
	
	/**
	 * 비밀참조인 수
	 */
	private int iCountBCC;
	
	/**
	 * 수신인 정보 VO
	 */
	private RecipientEtyCSVO recipients;
	
	/**
	 * 첨부 파일 정보 VO
	 */
	private ExtractedAttachESBVO[] attachs;

	/**
	 * 문자 셋
	 */
	private String charset;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getRequestReply() {
		return requestReply;
	}

	public void setRequestReply(String requestReply) {
		this.requestReply = requestReply;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public int getiPrevMsgNo() {
		return iPrevMsgNo;
	}

	public void setiPrevMsgNo(int iPrevMsgNo) {
		this.iPrevMsgNo = iPrevMsgNo;
	}

	public String getiNextMsgNo() {
		return iNextMsgNo;
	}

	public void setiNextMsgNo(String iNextMsgNo) {
		this.iNextMsgNo = iNextMsgNo;
	}

	public String getPrevFolderName() {
		return prevFolderName;
	}

	public void setPrevFolderName(String prevFolderName) {
		this.prevFolderName = prevFolderName;
	}

	public String getNextFolderName() {
		return nextFolderName;
	}

	public void setNextFolderName(String nextFolderName) {
		this.nextFolderName = nextFolderName;
	}

	public String getPrevSubject() {
		return prevSubject;
	}

	public void setPrevSubject(String prevSubject) {
		this.prevSubject = prevSubject;
	}

	public String getNextSubject() {
		return nextSubject;
	}

	public void setNextSubject(String nextSubject) {
		this.nextSubject = nextSubject;
	}

	public String getPrevPersonInfo() {
		return prevPersonInfo;
	}

	public void setPrevPersonInfo(String prevPersonInfo) {
		this.prevPersonInfo = prevPersonInfo;
	}

	public String getNextPersonInfo() {
		return nextPersonInfo;
	}

	public void setNextPersonInfo(String nextPersonInfo) {
		this.nextPersonInfo = nextPersonInfo;
	}

	public int getiPageNo() {
		return iPageNo;
	}

	public void setiPageNo(int iPageNo) {
		this.iPageNo = iPageNo;
	}

	public int getiCountTO() {
		return iCountTO;
	}

	public void setiCountTO(int iCountTO) {
		this.iCountTO = iCountTO;
	}

	public int getiCountCC() {
		return iCountCC;
	}

	public void setiCountCC(int iCountCC) {
		this.iCountCC = iCountCC;
	}

	public int getiCountBCC() {
		return iCountBCC;
	}

	public void setiCountBCC(int iCountBCC) {
		this.iCountBCC = iCountBCC;
	}

	public RecipientEtyCSVO getRecipients() {
		return recipients;
	}

	public void setRecipients(RecipientEtyCSVO recipients) {
		this.recipients = recipients;
	}

	public ExtractedAttachESBVO[] getAttachs() {
		return attachs;
	}

	public void setAttachs(ExtractedAttachESBVO[] attachs) {
		this.attachs = attachs;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
}
