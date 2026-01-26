package com.terracetech.tims.webmail.setting.vo;

import java.io.Serializable;

public class UserEtcInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int userSeq = -1; 
	private int pageLineCnt = -1;
	private int notiInterval = -1;
	private int autoSaveTerm = -1;
	private String senderName, saveSendBox, receiveNoti, vcardAttach, userLocale, writeMode, charSet, hiddenImg, signAttach, afterLogin, hiddenTag, userSkin, forwardingMode, senderEmail, autoSaveId, autoSaveMode, encSenderName, searchAllFolder, composeMode, writeNoti, activeXUse, renderMode;
	
	public int getUserSeq() {
		return userSeq;
	}
	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	public int getPageLineCnt() {
		if(pageLineCnt <= 0){
			pageLineCnt = 15;
		}
		return pageLineCnt;
	}
	public void setPageLineCnt(Integer pageLine) {
		if(pageLine!=null){
			this.pageLineCnt = pageLine;	
		}else{
			pageLineCnt = 15;
		}
	}
	public int getNotiInterval() {
		return notiInterval;
	}
	public void setNotiInterval(Integer notiInterval) {
		if(notiInterval!=null){
			this.notiInterval = notiInterval;	
		}else{
			this.notiInterval = 10;
		}
	}
	public int getAutoSaveTerm() {
		return autoSaveTerm;
	}
	public void setAutoSaveTerm(Integer autoSaveTerm) {
		if(autoSaveTerm!=null){
			this.autoSaveTerm = autoSaveTerm;	
		}else{
			this.autoSaveTerm = 10;
		}
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSaveSendBox() {
		return saveSendBox;
	}
	public void setSaveSendBox(String saveSendBox) {
		this.saveSendBox = saveSendBox;
	}
	public String getReceiveNoti() {
		return receiveNoti;
	}
	public void setReceiveNoti(String receiveNoti) {
		this.receiveNoti = receiveNoti;
	}
	public String getVcardAttach() {
		return vcardAttach;
	}
	public void setVcardAttach(String vcardAttach) {
		this.vcardAttach = vcardAttach;
	}
	public String getUserLocale() {
		return userLocale;
	}
	public void setUserLocale(String userLocale) {
		this.userLocale = userLocale;
	}
	public String getWriteMode() {
		writeMode = (writeMode != null && writeMode.length() > 0)?writeMode:"html";
		return writeMode;
	}
	public void setWriteMode(String writeMode) {
		this.writeMode = writeMode;
	}
	public String getCharSet() {
		return charSet;
	}
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}
	public String getHiddenImg() {
		hiddenImg = (hiddenImg != null && hiddenImg.length() > 0)?hiddenImg:"off";
		return hiddenImg;
	}
	public void setHiddenImg(String hiddenImg) {
		this.hiddenImg = hiddenImg;		
	}
	public String getSignAttach() {
		return signAttach;
	}
	public void setSignAttach(String signAttach) {
		this.signAttach = signAttach;
	}
	public String getAfterLogin() {
		return afterLogin;
	}
	public void setAfterLogin(String afterLogin) {
		this.afterLogin = afterLogin;
	}
	public String getHiddenTag() {
		hiddenTag = (hiddenTag != null && hiddenTag.length() > 0)?hiddenTag:"off";
		return hiddenTag;
	}
	public void setHiddenTag(String hiddenTag) {
		this.hiddenTag = hiddenTag;
	}
	public String getUserSkin() {
		return userSkin;
	}
	public void setUserSkin(String userSkin) {
		this.userSkin = userSkin;
	}
	public String getForwardingMode() {
		return forwardingMode;
	}
	public void setForwardingMode(String forwardingMode) {
		this.forwardingMode = forwardingMode;
	}
	public String getSenderEmail() {
		return senderEmail;
	}
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	public String getAutoSaveId() {
		return autoSaveId;
	}
	public void setAutoSaveId(String autoSaveId) {
		this.autoSaveId = autoSaveId;
	}
	public String getAutoSaveMode() {
		return autoSaveMode;
	}
	public void setAutoSaveMode(String autoSaveMode) {
		this.autoSaveMode = autoSaveMode;
	}
	public String getEncSenderName() {
		return encSenderName;
	}
	public void setEncSenderName(String encSenderName) {
		this.encSenderName = encSenderName;
	}
	public String getSearchAllFolder() {
		return searchAllFolder;
	}
	public void setSearchAllFolder(String searchAllFolder) {
		this.searchAllFolder = searchAllFolder;
	}
	public String getComposeMode() {		
		return composeMode;
	}
	public void setComposeMode(String composeMode) {
		this.composeMode = composeMode;
	}
	public String getWriteNoti() {
		if(writeNoti == null || 
		   writeNoti.length() ==0){			
			writeNoti = "disable";
		}
		return writeNoti;
	}
	public void setWriteNoti(String writeNoti) {
		this.writeNoti = writeNoti;
	}
	
	public String getActiveXUse() {
		if(activeXUse == null || 
				activeXUse.length() ==0){			
			activeXUse = "disable";
		}
		return activeXUse;
	}
	public void setActiveXUse(String activeXUse) {
		this.activeXUse = activeXUse;
	}
	
	public String getRenderMode() {
		return renderMode;
	}
	public void setRenderMode(String renderMode) {
		this.renderMode = renderMode;
	}
	
}
