package com.terracetech.tims.service.tms.vo;

import java.io.Serializable;

public class SendCondVO implements Serializable{
	
	private static final long serialVersionUID = 20100304L;
	
	private String sendType = null;
	private String sendFlag = null;
	private String locale = null;
	private String[] toAddr = null;
	private String[] ccAddr = null;
	private String[] bccAddr = null;
	
	private String subject = null;	
	private String encode = null;
	private boolean receivnoti = false;
	private boolean onesend = false;
	private boolean savesent = false;
	private boolean vcard = false;
	private boolean priority = false;
	
	private boolean signUse = false;
	private int signSeq = -1;
	
	private String content = null;
	
	private String senderEmail = null;
	private String senderName = null;
	private String remoteIp = null;
	private String mdnHost = null;
	private String localhost = null;
	private String editMode = null;	
	private String charset = null;
	private String uid = null;
	private String draftMid = null;
	private String folder = null; 
	
	private String attachListStr = null;
	private AttachFileVO[] attachList = null;
	private boolean bigAttachUse = false;
	
	private String reservYear = null;	
	private String reservMonth = null;	
	private String reservDay = null;	
	private String reservHour = null;	
	private String reservMin = null;
	
	private String notiMode = null;

	public String[] getToAddr() {
		return toAddr;
	}

	public void setToAddr(String[] toAddr) {
		this.toAddr = toAddr;
	}

	public String[] getCcAddr() {
		return ccAddr;
	}

	public void setCcAddr(String[] ccAddr) {
		this.ccAddr = ccAddr;
	}

	public String[] getBccAddr() {
		return bccAddr;
	}

	public void setBccAddr(String[] bccAddr) {
		this.bccAddr = bccAddr;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public boolean isVcard() {
		return vcard;
	}

	public void setVcard(boolean vcard) {
		this.vcard = vcard;
	}

	public boolean isPriority() {
		return priority;
	}

	public void setPriority(boolean priority) {
		this.priority = priority;
	}

	public boolean isSavesent() {
		return savesent;
	}

	public void setSavesent(boolean savesent) {
		this.savesent = savesent;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}

	public String getMdnHost() {
		return mdnHost;
	}

	public void setMdnHost(String mdnHost) {
		this.mdnHost = mdnHost;
	}

	public String getLocalhost() {
		return localhost;
	}

	public void setLocalhost(String localhost) {
		this.localhost = localhost;
	}

	public boolean isSignUse() {
		return signUse;
	}

	public void setSignUse(boolean signUse) {
		this.signUse = signUse;
	}

	public int getSignSeq() {
		return signSeq;
	}

	public String getReservYear() {
		return reservYear;
	}

	public void setReservYear(String reservYear) {
		this.reservYear = reservYear;
	}

	public String getReservMonth() {
		return reservMonth;
	}

	public void setReservMonth(String reservMonth) {
		this.reservMonth = reservMonth;
	}

	public String getReservDay() {
		return reservDay;
	}

	public void setReservDay(String reservDay) {
		this.reservDay = reservDay;
	}

	public String getReservHour() {
		return reservHour;
	}

	public void setReservHour(String reservHour) {
		this.reservHour = reservHour;
	}

	public String getReservMin() {
		return reservMin;
	}

	public void setReservMin(String reservMin) {
		this.reservMin = reservMin;
	}

	public void setSignSeq(int signSeq) {
		this.signSeq = signSeq;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getRemoteIp() {
		return remoteIp;
	}
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public boolean isReceivnoti() {
		return receivnoti;
	}

	public void setReceivnoti(boolean receivnoti) {
		this.receivnoti = receivnoti;
	}

	public boolean isOnesend() {
		return onesend;
	}

	public void setOnesend(boolean onesend) {
		this.onesend = onesend;
	}

	public String getEditMode() {
		return editMode;
	}

	public void setEditMode(String editMode) {
		this.editMode = editMode;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDraftMid() {
		return draftMid;
	}

	public void setDraftMid(String draftMid) {
		this.draftMid = draftMid;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getAttachListStr() {
		return attachListStr;
	}

	public void setAttachListStr(String attachListStr) {
		this.attachListStr = attachListStr;
	}

	public AttachFileVO[] getAttachList() {
		return attachList;
	}

	public void setAttachList(AttachFileVO[] attachList) {
		this.attachList = attachList;
	}

	public boolean isBigAttachUse() {
		return bigAttachUse;
	}

	public void setBigAttachUse(boolean bigAttachUse) {
		this.bigAttachUse = bigAttachUse;
	}

        public String getNotiMode() {
            return notiMode;
        }
    
        public void setNotiMode(String notiMode) {
            this.notiMode = notiMode;
        }
	
}
