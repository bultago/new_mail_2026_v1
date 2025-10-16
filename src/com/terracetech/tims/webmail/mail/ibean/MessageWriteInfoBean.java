/**
 * MailWriteInfoBean.java 2009. 2. 18.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.ibean;

import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>MailWriteInfoBean.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MessageWriteInfoBean {
	private boolean mobileMode = false;
	private String writeType = null;
	private String editorMode = null; 
	
	private String[] uids = null;
	private String[] wuids = null;
	private String buids = null;
	private String bmids = null;
	private String bbsId = null;
	
	private boolean isLocalMailWrite = false;
	private String folderName = null;
	private String webfolderType = null;
	private String webfolderShareSeq = null;
	private String place = null;	
	
	private String reqTo = null; 
	private String reqCc = null;
	private String reqBcc = null;
	private String reqSubject = null;
	private String reqContent = null;
	private String returl = null;
	private String[] attatachUrls = null;
	private String[] attatachFilenames = null;
	private String[] attachLists = null;
	private String autoSaveMode = null;    	
	private String forwardingMode = null;
	
	private boolean isSignInside = false;
	
	private String imgFilePath = null;
	
	public boolean isLocalMailWrite() {
		return isLocalMailWrite;
	}
	public void setLocalMailWrite(boolean isLocalMailWrite) {
		this.isLocalMailWrite = isLocalMailWrite;
	}
	
	public boolean isMobileMode() {
		return mobileMode;
	}
	public void setMobileMode(boolean mobileMode) {
		this.mobileMode = mobileMode;
	}
	/**
	 * @return writeType 값 반환
	 */
	public String getWriteType() {
		return writeType;
	}	
	/**
	 * @param writeType 파라미터를 writeType값에 설정
	 */
	public void setWriteType(String writeType) {
		this.writeType = writeType;
	}	
	/**
	 * @return editorMode 값 반환
	 */
	public String getEditorMode() {
		return editorMode;
	}
	/**
	 * @param editorMode 파라미터를 editorMode값에 설정
	 */
	public void setEditorMode(String editorMode) {
		this.editorMode = editorMode;
	}
	
	public boolean isHtmlEditorMode(){
		return (editorMode != null)?editorMode.equals("html"):false;
	}
	
	public boolean isTextEditorMode(){
		return (editorMode != null)?editorMode.equals("text"):false;
	}
	/**
	 * @return uid 값 반환
	 */
	public String[] getUids() {
		return uids;
	}
	
	public long[] getUidsValue() {		
		return StringUtils.getStringsToLongs(uids);
	}
	/**
	 * @param uid 파라미터를 uid값에 설정
	 */
	public void setUids(String[] uids) {
		this.uids = uids;
	}
	/**
	 * @return uids 값 반환
	 */
	public String[] getWuids() {
		return wuids;
	}
	/**
	 * @return uids 값 반환
	 */
	public long[] getWuidsValue() {		
		return StringUtils.getStringsToLongs(wuids);
	}
	/**
	 * @param uids 파라미터를 uids값에 설정
	 */
	public void setWuids(String wuid) {
		if(wuid != null){
			this.wuids = wuid.split("\\|");
		}		
	}
	/**
	 * @return buids 값 반환
	 */
	public String getBuids() {
		return buids;
	}
	/**
	 * @param buids 파라미터를 buids값에 설정
	 */
	public void setBuids(String buids) {
		this.buids = buids;
	}
	public String getBmids() {
		return bmids;
	}
	public void setBmids(String bmids) {
		this.bmids = bmids;
	}
	/**
	 * @return bbsId 값 반환
	 */
	public String getBbsId() {
		return bbsId;
	}
	/**
	 * @param bbsId 파라미터를 bbsId값에 설정
	 */
	public void setBbsId(String bbsId) {
		this.bbsId = bbsId;
	}
	/**
	 * @return folderName 값 반환
	 */
	public String getFolderName() {
		return folderName;
	}
	/**
	 * @param folderName 파라미터를 folderName값에 설정
	 */
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	/**
	 * @return webfolderType 값 반환
	 */
	public String getWebfolderType() {
		return webfolderType;
	}
	/**
	 * @param webfolderType 파라미터를 webfolderType값에 설정
	 */
	public void setWebfolderType(String webfolderType) {
		this.webfolderType = webfolderType;
	}
	/**
	 * @return webfolderShareSeq 값 반환
	 */
	public String getWebfolderShareSeq() {
		return webfolderShareSeq;
	}
	/**
	 * @param webfolderShareSeq 파라미터를 webfolderShareSeq값에 설정
	 */
	public void setWebfolderShareSeq(String webfolderShareSeq) {
		this.webfolderShareSeq = webfolderShareSeq;
	}
	/**
	 * @return place 값 반환
	 */
	public String getPlace() {
		return place;
	}
	/**
	 * @param place 파라미터를 place값에 설정
	 */
	public void setPlace(String place) {
		this.place = place;
	}
	/**
	 * @return reqTo 값 반환
	 */
	public String getReqTo() {		
		return (reqTo != null)?reqTo:"";
	}
	/**
	 * @param reqTo 파라미터를 reqTo값에 설정
	 */
	public void setReqTo(String reqTo) {
		this.reqTo = reqTo;
	}
	/**
	 * @return reqCc 값 반환
	 */
	public String getReqCc() {
		return (reqCc != null)?reqCc:"";
	}
	/**
	 * @param reqCc 파라미터를 reqCc값에 설정
	 */
	public void setReqCc(String reqCc) {
		this.reqCc = reqCc;
	}
	/**
	 * @return reqBcc 값 반환
	 */
	public String getReqBcc() {
		return (reqBcc != null)?reqBcc:"";
	}
	/**
	 * @param reqBcc 파라미터를 reqBcc값에 설정
	 */
	public void setReqBcc(String reqBcc) {
		this.reqBcc = reqBcc;
	}
	/**
	 * @return reqSubject 값 반환
	 */
	public String getReqSubject() {
		return (reqSubject != null)?reqSubject:"";
	}
	/**
	 * @param reqSubject 파라미터를 reqSubject값에 설정
	 */
	public void setReqSubject(String reqSubject) {
		this.reqSubject = reqSubject;
	}
	/**
	 * @return reqContent 값 반환
	 */
	public String getReqContent() {
		return (reqContent != null)?reqContent:"";
	}
	/**
	 * @param reqContent 파라미터를 reqContent값에 설정
	 */
	public void setReqContent(String reqContent) {		
		this.reqContent = reqContent;
	}
	/**
	 * @return returl 값 반환
	 */
	public String getReturl() {
		return returl;
	}
	/**
	 * @param returl 파라미터를 returl값에 설정
	 */
	public void setReturl(String returl) {
		this.returl = returl;
	}
	/**
	 * @return attatachUrls 값 반환
	 */
	public String[] getAttatachUrls() {
		return attatachUrls;
	}
	/**
	 * @param attatachUrls 파라미터를 attatachUrls값에 설정
	 */
	public void setAttatachUrls(String[] attatachUrls) {
		this.attatachUrls = attatachUrls;
	}
	public String[] getAttatachFilenames() {
		return attatachFilenames;
	}
	public void setAttatachFilenames(String[] attatachFilenames) {
		this.attatachFilenames = attatachFilenames;
	}	
	/**
	 * @return attachLists 값 반환
	 */
	public String[] getAttachLists() {
		return attachLists;
	}
	/**
	 * @param attachLists 파라미터를 attachLists값에 설정
	 */
	public void setAttachLists(String[] attachLists) {
		this.attachLists = attachLists;
	}
	/**
	 * @return autoSaveMode 값 반환
	 */
	public String getAutoSaveMode() {
		return autoSaveMode;
	}
	/**
	 * @param autoSaveMode 파라미터를 autoSaveMode값에 설정
	 */
	public void setAutoSaveMode(String autoSaveMode) {
		this.autoSaveMode = autoSaveMode;
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
	
	public boolean isForwardParsed(){
		return (forwardingMode != null)?forwardingMode.equals("parsed"):false;	
	}
	/**
	 * @return isSignInside 값 반환
	 */
	public boolean isSignInside() {
		return isSignInside;
	}
	/**
	 * @param isSignInside 파라미터를 isSignInside값에 설정
	 */
	public void setSignInside(boolean isSignInside) {
		this.isSignInside = isSignInside;
	}
	public String getImgFilePath() {
		return imgFilePath;
	}
	public void setImgFilePath(String imgFilePath) {
		this.imgFilePath = imgFilePath;
	}


	
}
