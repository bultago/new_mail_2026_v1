/**
 * MailWriteMessageBean.java 2009. 2. 19.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.ibean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>MailWriteMessageBean.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
@SuppressWarnings("unchecked")
public class MailWriteMessageBean {	
	
	private String to = null;
	private String cc = null;
	private String bcc = null;
	private String subject = null;
	private String htmlContent = null;
	private String textContent = null;
	
	private String forwardingMode = null;
	private String draftMsgId = null;
	private String trashMsgId = null;
	
	private String[][] attaches = null;
	private String attacheString = null;
	private String[] uids = null;
	private String writeType = null;
	private String sendFlag = null;
	private String folderName = null;
	
	private String receiveNoti = null;	
	private String saveSent = null;
	private String signAttach = null;
	private String vcardAttach = null;
	
	private String encoding = null;
	private String editorMode = null;
	private String senderName = null;
	private String senderEmail = null;
	
	private String autoSaveMode = null;
	private int autoSaveTerm = 0;
	
	private String securemail = null;
	private String securemailModule = null;
	private String secureWriteMode = null;
	private int maxReservedDay = 0;
	
	private String rcptMode = null;
	private String userDN = null;
	private String secureMakeMode = null;
	private int expressMaxSendCnt = 0;
	private String logStr = null;
	
	public JSONObject toJson(){
		JSONObject jObj = new JSONObject();
		jObj.put("to", to);
		jObj.put("cc", cc);
		jObj.put("bcc", bcc);
		jObj.put("subject", subject);
		jObj.put("htmlContent", htmlContent);
		jObj.put("textContent", textContent);
		jObj.put("forwardingMode", forwardingMode);
		jObj.put("draftMsgId", draftMsgId);
		jObj.put("trashMsgId", trashMsgId);	
		jObj.put("attaches", parseAttachJsonArray(attaches));
		jObj.put("uids", getUidsValue());
		jObj.put("writeType", writeType);
		jObj.put("sendFlag", sendFlag);
		jObj.put("folderName", folderName);
		jObj.put("receiveNoti", receiveNoti);
		jObj.put("saveSent", saveSent);
		jObj.put("signAttach", signAttach);
		jObj.put("vcardAttach", vcardAttach);
		jObj.put("encoding", encoding);
		jObj.put("editorMode", editorMode);
		jObj.put("senderName", senderName);
		jObj.put("senderEmail", senderEmail);
		jObj.put("autoSaveMode", autoSaveMode);
		jObj.put("autoSaveTerm", autoSaveTerm);
		jObj.put("securemail", securemail);
		jObj.put("maxReservedDay", maxReservedDay);
		jObj.put("rcptMode", rcptMode);
		jObj.put("securemailModule", securemailModule);
		jObj.put("secureWriteMode", secureWriteMode);
		jObj.put("userDN", userDN);
		jObj.put("secureMakeMode", secureMakeMode);
		jObj.put("expressMaxSendCnt",expressMaxSendCnt);
		
		return jObj;		
	}
	
	private JSONArray parseAttachJsonArray(String[][] attaches){
		JSONArray list = new JSONArray();
		JSONArray templist = null;
		if(attaches != null){
			for(int i = 0; i < attaches.length; i++) {
				if (attaches[i][0] != "") {
					templist = new JSONArray();
					templist.add(attaches[i][0]);
					templist.add(attaches[i][1]);
					templist.add(attaches[i][2]);
					list.add(templist);
				}				
			}
		}
		return list;
	}
	
	public String getWriteSettingInfo(){
		JSONObject jObj = new JSONObject();
		jObj.put("writeType", writeType);
		jObj.put("receiveNoti", receiveNoti);
		jObj.put("saveSent", saveSent);
		jObj.put("signAttach", signAttach);
		jObj.put("vcardAttach", vcardAttach);
		jObj.put("encoding", encoding);
		jObj.put("editorMode", editorMode);
		jObj.put("senderName", senderName);
		jObj.put("senderEmail", senderEmail);
		jObj.put("autoSaveMode", autoSaveMode);
		jObj.put("autoSaveTerm", autoSaveTerm);
		jObj.put("securemail", securemail);
		jObj.put("maxReservedDay", maxReservedDay);
		jObj.put("rcptMode", rcptMode);
		jObj.put("securemailModule", securemailModule);
		jObj.put("secureWriteMode", secureWriteMode);		
		return jObj.toString();
	}
	
	public String getCurrentDateInfo(){
		JSONObject jObj = new JSONObject();
		Calendar cal = Calendar.getInstance();	
		jObj.put("tyear", cal.get( Calendar.YEAR ) );
		jObj.put("tmonth",cal.get( Calendar.MONTH ) +1);
		jObj.put("tday", cal.get( Calendar.DATE ) );
		jObj.put("thour", cal.get( Calendar.HOUR_OF_DAY ) );
		jObj.put("tmin", cal.get( Calendar.MINUTE ) );
		jObj.put("todayDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		
		return jObj.toString();
	}
	
	/**
	 * @return to °ª ¹ÝÈ¯
	 */
	public String getTo() {
		return (to != null)?to:"";
	}
	/**
	 * @param to ÆÄ¶ó¹ÌÅÍ¸¦ to°ª¿¡ ¼³Á¤
	 */
	public void setTo(String to) {
		this.to = to;
	}
	/**
	 * @return cc °ª ¹ÝÈ¯
	 */
	public String getCc() {
		return (cc != null)?cc:"";
	}
	/**
	 * @param cc ÆÄ¶ó¹ÌÅÍ¸¦ cc°ª¿¡ ¼³Á¤
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}
	/**
	 * @return bcc °ª ¹ÝÈ¯
	 */
	public String getBcc() {
		return (bcc != null)?bcc:"";
	}
	/**
	 * @param bcc ÆÄ¶ó¹ÌÅÍ¸¦ bcc°ª¿¡ ¼³Á¤
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	/**
	 * @return subject °ª ¹ÝÈ¯
	 */
	public String getSubject() {
		return (subject != null)?subject:"";
	}
	/**
	 * @param subject ÆÄ¶ó¹ÌÅÍ¸¦ subject°ª¿¡ ¼³Á¤
	 */
	public void setSubject(String subject) {
		this.subject = TMailUtility.getCRLFEscape(subject);
	}
	/**
	 * @return htmlContent °ª ¹ÝÈ¯
	 */
	public String getHtmlContent() {
		return (htmlContent != null)?StringUtils.EscapeHTMLTag(htmlContent):"";
	}
	
	public String getHtmlNormalContent() {
		return (htmlContent != null)?htmlContent:"";
	}
	/**
	 * @param htmlContent ÆÄ¶ó¹ÌÅÍ¸¦ htmlContent°ª¿¡ ¼³Á¤
	 */
	public void setHtmlContent(String htmlContent) {
		this.htmlContent = StringUtils.getCRLFEscape(htmlContent);
	}
	/**
	 * @return textContent °ª ¹ÝÈ¯
	 */
	public String getTextContent() {
		return (textContent != null)?StringUtils.EscapeHTMLTag(textContent):"";
	}
	
	public String getTextNormalContent() {
		return (textContent != null)?textContent:"";
	}
	/**
	 * @param textContent ÆÄ¶ó¹ÌÅÍ¸¦ textContent°ª¿¡ ¼³Á¤
	 */
	public void setTextContent(String textContent) {
		this.textContent = textContent;
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
	/**
	 * @return draftMsgId °ª ¹ÝÈ¯
	 */
	public String getDraftMsgId() {
		return draftMsgId;
	}
	/**
	 * @param draftMsgId ÆÄ¶ó¹ÌÅÍ¸¦ draftMsgId ¼³Á¤
	 */
	public void setDraftMsgId(String draftMsgId) {
		this.draftMsgId = draftMsgId;
	}
	/**
	 * @return draftMsgId °ª ¹ÝÈ¯
	 */
	public String getTrashMsgId() {
		return trashMsgId;
	}
	/**
	 * @param draftMsgId ÆÄ¶ó¹ÌÅÍ¸¦ draftMsgId ¼³Á¤
	 */
	public void setTrashMsgId(String trashMsgId) {
		this.trashMsgId = trashMsgId;
	}
	/**
	 * @return attaches °ª ¹ÝÈ¯
	 */
	public String[][] getAttaches() {
		return attaches;
	}
	/**
	 * @param attaches ÆÄ¶ó¹ÌÅÍ¸¦ attaches°ª¿¡ ¼³Á¤
	 */
	public void setAttaches(String[][] attaches) {
		this.attaches = attaches;
	}
	/**
	 * @return attacheString °ª ¹ÝÈ¯
	 */
	public String getAttacheString() {
		return attacheString;
	}
	/**
	 * @param attacheString ÆÄ¶ó¹ÌÅÍ¸¦ attacheString°ª¿¡ ¼³Á¤
	 */
	public void setAttacheString(String attacheString) {
		this.attacheString = attacheString;
	}
	/**
	 * @return uids °ª ¹ÝÈ¯
	 */
	public String[] getUids() {
		return uids;
	}
	/**
	 * @param uids ÆÄ¶ó¹ÌÅÍ¸¦ uids°ª¿¡ ¼³Á¤
	 */
	public void setUids(String[] uids) {
		this.uids = uids;
	}
	
	public String getUidsValue(){
		String uidList = "";
		if(uids != null){			
			for (int i = 0; i < uids.length; i++) {
				uidList += uids[i];
				if(i < (uids.length-1) ){
					uidList += ",";
				}
			}
		}
		return uidList;
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
	 * @return sendFlag °ª ¹ÝÈ¯
	 */
	public String getSendFlag() {
		return sendFlag;
	}
	/**
	 * @param sendFlag ÆÄ¶ó¹ÌÅÍ¸¦ sendFlag°ª¿¡ ¼³Á¤
	 */
	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}
	/**
	 * @return receiveNoti °ª ¹ÝÈ¯
	 */
	public String getReceiveNoti() {
		return receiveNoti;
	}
	/**
	 * @param receiveNoti ÆÄ¶ó¹ÌÅÍ¸¦ receiveNoti°ª¿¡ ¼³Á¤
	 */
	public void setReceiveNoti(String receiveNoti) {
		this.receiveNoti = (receiveNoti != null && receiveNoti.equalsIgnoreCase("on"))?"on":"off";
	}	
	
	/**
	 * @return saveSent °ª ¹ÝÈ¯
	 */
	public String getSaveSent() {
		return saveSent;
	}
	/**
	 * @param saveSent ÆÄ¶ó¹ÌÅÍ¸¦ saveSent°ª¿¡ ¼³Á¤
	 */
	public void setSaveSent(String sentBox) {
		
		this.saveSent = (sentBox != null && sentBox.equalsIgnoreCase("on"))?"on":"off";
	}
	/**
	 * @return signAttach °ª ¹ÝÈ¯
	 */
	public String getSignAttach() {
		return signAttach;
	}
	/**
	 * @param signAttach ÆÄ¶ó¹ÌÅÍ¸¦ signAttach°ª¿¡ ¼³Á¤
	 */
	public void setSignAttach(String signAttach) {
		this.signAttach = (signAttach != null && signAttach.equalsIgnoreCase("T"))?"on":"off";
	}
	/**
	 * @return vcardAttach °ª ¹ÝÈ¯
	 */
	public String getVcardAttach() {
		return vcardAttach;
	}
	/**
	 * @param vcardAttach ÆÄ¶ó¹ÌÅÍ¸¦ vcardAttach°ª¿¡ ¼³Á¤
	 */
	public void setVcardAttach(String vcardAttach) {
		this.vcardAttach = (vcardAttach != null && vcardAttach.equalsIgnoreCase("on"))?"on":"off";
	}
	/**
	 * @return encoding °ª ¹ÝÈ¯
	 */
	public String getEncoding() {
		return encoding;
	}
	/**
	 * @param encoding ÆÄ¶ó¹ÌÅÍ¸¦ encoding°ª¿¡ ¼³Á¤
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
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
	/**
	 * @return senderName °ª ¹ÝÈ¯
	 */
	public String getSenderName() {
		return senderName;
	}
	/**
	 * @param senderName ÆÄ¶ó¹ÌÅÍ¸¦ senderName°ª¿¡ ¼³Á¤
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	/**
	 * @return senderEmail °ª ¹ÝÈ¯
	 */
	public String getSenderEmail() {
		return senderEmail;
	}
	/**
	 * @param senderEmail ÆÄ¶ó¹ÌÅÍ¸¦ senderEmail°ª¿¡ ¼³Á¤
	 */
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
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
	 * @return autoSaveTerm °ª ¹ÝÈ¯
	 */
	public int getAutoSaveTerm() {
		return autoSaveTerm;
	}


	/**
	 * @param autoSaveTerm ÆÄ¶ó¹ÌÅÍ¸¦ autoSaveTerm°ª¿¡ ¼³Á¤
	 */
	public void setAutoSaveTerm(int autoSaveTerm) {
		this.autoSaveTerm = autoSaveTerm;
	}

	public String getSecuremail() {
		return securemail;
	}

	public void setSecuremail(String securemail) {
		this.securemail = securemail;
	}

	public void setMaxReservedDay(int maxReservedDay) {
		this.maxReservedDay = maxReservedDay;
	}

	public String getRcptMode() {
		return rcptMode;
	}

	public void setRcptMode(String rcptMode) {
		this.rcptMode = rcptMode;
	}

	public String getSecuremailModule() {
		return securemailModule;
	}

	public void setSecuremailModule(String securemailModule) {
		this.securemailModule = securemailModule;
	}

	public String getSecureWriteMode() {
		return secureWriteMode;
	}

	public void setSecureWriteMode(String secureWriteMode) {
		this.secureWriteMode = secureWriteMode;
	}
	
	public boolean isXecureExpressWrite(){
		return "expressE".equalsIgnoreCase(secureWriteMode);
	}

	public String getUserDN() {
		return userDN;
	}

	public void setUserDN(String userDN) {
		this.userDN = userDN;
	}

	public String getSecureMakeMode() {
		return secureMakeMode;
	}

	public void setSecureMakeMode(String secureMakeMode) {
		this.secureMakeMode = secureMakeMode;
	}

	public int getExpressMaxSendCnt() {
		return expressMaxSendCnt;
	}

	public void setExpressMaxSendCnt(int expressMaxSendCnt) {
		this.expressMaxSendCnt = expressMaxSendCnt;
	}

	public String getLogStr() {
		return logStr;
	}

	public void setLogStr(String logStr) {
		this.logStr = logStr;
	}
}
