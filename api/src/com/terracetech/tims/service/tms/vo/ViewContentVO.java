package com.terracetech.tims.service.tms.vo;

import java.io.Serializable;

public class ViewContentVO implements Serializable{
	
	private static final long serialVersionUID = 20100304L;
	
	private long uid = 0;
	private String folderName = null;
	private String folderEncName = null;
	private String subject = null;;
	private String date = null;;
	private String sentDate = null;
	private EmailAddressVO from = null;
	private EmailAddressVO[] ccs = null;
	private EmailAddressVO[] tos = null;
	private AttachFileVO[] attachs = null;
	private String contents = null;
	private String textContents = null;
	private long preUid = 0;
	private long nextUid = 0;
	private int size = 0;
	private int priority = 0;
	private String bodyEncoding = null;
	private String messageId = null;
	private String mdnCheck = null;
	
	private boolean errorOccur = false;
	private String errorMsg = null;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public String getFolderEncName() {
		return folderEncName;
	}
	public void setFolderEncName(String folderEncName) {
		this.folderEncName = folderEncName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSentDate() {
		return sentDate;
	}
	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}
	public EmailAddressVO getFrom() {
		return from;
	}
	public void setFrom(EmailAddressVO from) {
		this.from = from;
	}
	public EmailAddressVO[] getCcs() {
		return ccs;
	}
	public void setCcs(EmailAddressVO[] ccs) {
		this.ccs = ccs;
	}
	public EmailAddressVO[] getTos() {
		return tos;
	}
	public void setTos(EmailAddressVO[] tos) {
		this.tos = tos;
	}
	public AttachFileVO[] getAttachs() {
		return attachs;
	}
	public void setAttachs(AttachFileVO[] attachs) {
		this.attachs = attachs;
	}
	
	public int getAttachsCnt() {
		return (attachs != null)?attachs.length:0;
	}
	
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public String getTextContents() {
		return textContents;
	}
	public void setTextContents(String textContents) {
		this.textContents = textContents;
	}
	public long getPreUid() {
		return preUid;
	}
	public void setPreUid(long preUid) {
		this.preUid = preUid;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getBodyEncoding() {
		return bodyEncoding;
	}
	public void setBodyEncoding(String bodyEncoding) {
		this.bodyEncoding = bodyEncoding;
	}
	public long getNextUid() {
		return nextUid;
	}
	public void setNextUid(long nextUid) {
		this.nextUid = nextUid;
	}
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public boolean isErrorOccur() {
		return errorOccur;
	}
	public void setErrorOccur(boolean errorOccur) {
		this.errorOccur = errorOccur;
	}	
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
    	}
        public String getMdnCheck() {
            return mdnCheck;
        }
        public void setMdnCheck(String mdnCheck) {
            this.mdnCheck = mdnCheck;
        }
}
