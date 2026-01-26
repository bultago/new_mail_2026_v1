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
 * <p>ÁÖ¿ä¼³¸í</p>
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
	 * @return writeType °ª ¹ÝÈ¯
	 */
	public String getWriteType() {
		return writeType;
	}	
	/**
	 * @param writeType ÆÄ¶ó¹ÌÅÍ¸¦ writeType°ª¿¡ ¼³Á¤
	 */
	public void setWriteType(String writeType) {
		this.writeType = writeType;
	}	
	/**
	 * @return editorMode °ª ¹ÝÈ¯
	 */
	public String getEditorMode() {
		return editorMode;
	}
	/**
	 * @param editorMode ÆÄ¶ó¹ÌÅÍ¸¦ editorMode°ª¿¡ ¼³Á¤
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
	 * @return uid °ª ¹ÝÈ¯
	 */
	public String[] getUids() {
		return uids;
	}
	
	public long[] getUidsValue() {		
		return StringUtils.getStringsToLongs(uids);
	}
	/**
	 * @param uid ÆÄ¶ó¹ÌÅÍ¸¦ uid°ª¿¡ ¼³Á¤
	 */
	public void setUids(String[] uids) {
		this.uids = uids;
	}
	/**
	 * @return uids °ª ¹ÝÈ¯
	 */
	public String[] getWuids() {
		return wuids;
	}
	/**
	 * @return uids °ª ¹ÝÈ¯
	 */
	public long[] getWuidsValue() {		
		return StringUtils.getStringsToLongs(wuids);
	}
	/**
	 * @param uids ÆÄ¶ó¹ÌÅÍ¸¦ uids°ª¿¡ ¼³Á¤
	 */
	public void setWuids(String wuid) {
		if(wuid != null){
			this.wuids = wuid.split("\\|");
		}		
	}
	/**
	 * @return buids °ª ¹ÝÈ¯
	 */
	public String getBuids() {
		return buids;
	}
	/**
	 * @param buids ÆÄ¶ó¹ÌÅÍ¸¦ buids°ª¿¡ ¼³Á¤
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
	 * @return bbsId °ª ¹ÝÈ¯
	 */
	public String getBbsId() {
		return bbsId;
	}
	/**
	 * @param bbsId ÆÄ¶ó¹ÌÅÍ¸¦ bbsId°ª¿¡ ¼³Á¤
	 */
	public void setBbsId(String bbsId) {
		this.bbsId = bbsId;
	}
	/**
	 * @return folderName °ª ¹ÝÈ¯
	 */
	public String getFolderName() {
		return folderName;
	}
	/**
	 * @param folderName ÆÄ¶ó¹ÌÅÍ¸¦ folderName°ª¿¡ ¼³Á¤
	 */
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	/**
	 * @return webfolderType °ª ¹ÝÈ¯
	 */
	public String getWebfolderType() {
		return webfolderType;
	}
	/**
	 * @param webfolderType ÆÄ¶ó¹ÌÅÍ¸¦ webfolderType°ª¿¡ ¼³Á¤
	 */
	public void setWebfolderType(String webfolderType) {
		this.webfolderType = webfolderType;
	}
	/**
	 * @return webfolderShareSeq °ª ¹ÝÈ¯
	 */
	public String getWebfolderShareSeq() {
		return webfolderShareSeq;
	}
	/**
	 * @param webfolderShareSeq ÆÄ¶ó¹ÌÅÍ¸¦ webfolderShareSeq°ª¿¡ ¼³Á¤
	 */
	public void setWebfolderShareSeq(String webfolderShareSeq) {
		this.webfolderShareSeq = webfolderShareSeq;
	}
	/**
	 * @return place °ª ¹ÝÈ¯
	 */
	public String getPlace() {
		return place;
	}
	/**
	 * @param place ÆÄ¶ó¹ÌÅÍ¸¦ place°ª¿¡ ¼³Á¤
	 */
	public void setPlace(String place) {
		this.place = place;
	}
	/**
	 * @return reqTo °ª ¹ÝÈ¯
	 */
	public String getReqTo() {		
		return (reqTo != null)?reqTo:"";
	}
	/**
	 * @param reqTo ÆÄ¶ó¹ÌÅÍ¸¦ reqTo°ª¿¡ ¼³Á¤
	 */
	public void setReqTo(String reqTo) {
		this.reqTo = reqTo;
	}
	/**
	 * @return reqCc °ª ¹ÝÈ¯
	 */
	public String getReqCc() {
		return (reqCc != null)?reqCc:"";
	}
	/**
	 * @param reqCc ÆÄ¶ó¹ÌÅÍ¸¦ reqCc°ª¿¡ ¼³Á¤
	 */
	public void setReqCc(String reqCc) {
		this.reqCc = reqCc;
	}
	/**
	 * @return reqBcc °ª ¹ÝÈ¯
	 */
	public String getReqBcc() {
		return (reqBcc != null)?reqBcc:"";
	}
	/**
	 * @param reqBcc ÆÄ¶ó¹ÌÅÍ¸¦ reqBcc°ª¿¡ ¼³Á¤
	 */
	public void setReqBcc(String reqBcc) {
		this.reqBcc = reqBcc;
	}
	/**
	 * @return reqSubject °ª ¹ÝÈ¯
	 */
	public String getReqSubject() {
		return (reqSubject != null)?reqSubject:"";
	}
	/**
	 * @param reqSubject ÆÄ¶ó¹ÌÅÍ¸¦ reqSubject°ª¿¡ ¼³Á¤
	 */
	public void setReqSubject(String reqSubject) {
		this.reqSubject = reqSubject;
	}
	/**
	 * @return reqContent °ª ¹ÝÈ¯
	 */
	public String getReqContent() {
		return (reqContent != null)?reqContent:"";
	}
	/**
	 * @param reqContent ÆÄ¶ó¹ÌÅÍ¸¦ reqContent°ª¿¡ ¼³Á¤
	 */
	public void setReqContent(String reqContent) {		
		this.reqContent = reqContent;
	}
	/**
	 * @return returl °ª ¹ÝÈ¯
	 */
	public String getReturl() {
		return returl;
	}
	/**
	 * @param returl ÆÄ¶ó¹ÌÅÍ¸¦ returl°ª¿¡ ¼³Á¤
	 */
	public void setReturl(String returl) {
		this.returl = returl;
	}
	/**
	 * @return attatachUrls °ª ¹ÝÈ¯
	 */
	public String[] getAttatachUrls() {
		return attatachUrls;
	}
	/**
	 * @param attatachUrls ÆÄ¶ó¹ÌÅÍ¸¦ attatachUrls°ª¿¡ ¼³Á¤
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
	 * @return attachLists °ª ¹ÝÈ¯
	 */
	public String[] getAttachLists() {
		return attachLists;
	}
	/**
	 * @param attachLists ÆÄ¶ó¹ÌÅÍ¸¦ attachLists°ª¿¡ ¼³Á¤
	 */
	public void setAttachLists(String[] attachLists) {
		this.attachLists = attachLists;
	}
	/**
	 * @return autoSaveMode °ª ¹ÝÈ¯
	 */
	public String getAutoSaveMode() {
		return autoSaveMode;
	}
	/**
	 * @param autoSaveMode ÆÄ¶ó¹ÌÅÍ¸¦ autoSaveMode°ª¿¡ ¼³Á¤
	 */
	public void setAutoSaveMode(String autoSaveMode) {
		this.autoSaveMode = autoSaveMode;
	}
	/**
	 * @return forwardingMode °ª ¹ÝÈ¯
	 */
	public String getForwardingMode() {
		return forwardingMode;
	}
	/**
	 * @param forwardingMode ÆÄ¶ó¹ÌÅÍ¸¦ forwardingMode°ª¿¡ ¼³Á¤
	 */
	public void setForwardingMode(String forwardingMode) {
		this.forwardingMode = forwardingMode;
	}
	
	public boolean isForwardParsed(){
		return (forwardingMode != null)?forwardingMode.equals("parsed"):false;	
	}
	/**
	 * @return isSignInside °ª ¹ÝÈ¯
	 */
	public boolean isSignInside() {
		return isSignInside;
	}
	/**
	 * @param isSignInside ÆÄ¶ó¹ÌÅÍ¸¦ isSignInside°ª¿¡ ¼³Á¤
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
