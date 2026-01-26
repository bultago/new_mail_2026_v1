package com.terracetech.tims.service.tms.vo;

public class WriteCondVO {
	private String remoteIp = null;
	private String userEmail = null;
	private String locale = null;
	private boolean mobileMode = false;
	
	private String writeType = null;
	private String editorMode = null; 
	
	private String[] uids = null;
		
	private String folderName = null;		
	
	private String reqTo = null; 
	private String reqCc = null;
	private String reqBcc = null;
	private String reqSubject = null;
	private String reqContent = null;
	private String returl = null;	    	
	private String forwardingMode = null;
	
	private boolean isSignInside = false;

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public boolean isMobileMode() {
		return mobileMode;
	}

	public void setMobileMode(boolean mobileMode) {
		this.mobileMode = mobileMode;
	}

	public String getWriteType() {
		return writeType;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setWriteType(String writeType) {
		this.writeType = writeType;
	}

	public String getEditorMode() {
		return editorMode;
	}

	public void setEditorMode(String editorMode) {
		this.editorMode = editorMode;
	}

	public String[] getUids() {
		return uids;
	}

	public void setUids(String[] uids) {
		this.uids = uids;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getReqTo() {
		return reqTo;
	}

	public void setReqTo(String reqTo) {
		this.reqTo = reqTo;
	}

	public String getReqCc() {
		return reqCc;
	}

	public void setReqCc(String reqCc) {
		this.reqCc = reqCc;
	}

	public String getReqBcc() {
		return reqBcc;
	}

	public void setReqBcc(String reqBcc) {
		this.reqBcc = reqBcc;
	}

	public String getReqSubject() {
		return reqSubject;
	}

	public void setReqSubject(String reqSubject) {
		this.reqSubject = reqSubject;
	}

	public String getReqContent() {
		return reqContent;
	}

	public void setReqContent(String reqContent) {
		this.reqContent = reqContent;
	}

	public String getReturl() {
		return returl;
	}

	public void setReturl(String returl) {
		this.returl = returl;
	}

	public String getForwardingMode() {
		return forwardingMode;
	}

	public void setForwardingMode(String forwardingMode) {
		this.forwardingMode = forwardingMode;
	}

	public boolean isSignInside() {
		return isSignInside;
	}

	public void setSignInside(boolean isSignInside) {
		this.isSignInside = isSignInside;
	}
	
	
	
	
}
