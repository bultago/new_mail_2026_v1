/**
 * MailSendResultBean.java 2009. 2. 26.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.ibean;

import java.util.Map;

import javax.mail.internet.InternetAddress;

import com.terracetech.tims.mail.TMailAddress;

/**
 * <p><strong>MailSendResultBean.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MailSendResultBean {
	
	String to = null;
	String cc = null;
	String bcc = null;
	
	String returnUrl = null;
	String saveMid = null;
	
	String sendAddress = null;
	InternetAddress[] sendAddressList = null;
	String invalidAddress = null;
	InternetAddress[] invalidAddressList = null;
	Map<String, String> invalidAddrMap = null;
	
	String sendType = "normal";
	String sendResultType = "normal";
	String sendFolderName = null;
	
	long mailSize = 0;
	
	boolean errorOccur = false;
	boolean detectVirus = false;
	boolean noRcpt = false;
	
	String errorMessage = null;
	/**
	 * @return to 값 반환
	 */
	public String getTo() {
		return to;
	}
	
	/**
	 * @return to 값 반환
	 */
	public String getValidTo() {		
		return getValidAddress(to);		
	}
	/**
	 * @param to 파라미터를 to값에 설정
	 */
	public void setTo(String to) {
		this.to = to;
	}
	/**
	 * @return cc 값 반환
	 */
	public String getValidCc() {
		return getValidAddress(cc);				
	}
	
	public String getCc() {
		return cc;
	}
	/**
	 * @param cc 파라미터를 cc값에 설정
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}
	/**
	 * @return bcc 값 반환
	 */
	public String getBcc() {
		return bcc;
	}
	
	public String getValidBcc() {
		return getValidAddress(bcc);			
	}
	/**
	 * @param bcc 파라미터를 bcc값에 설정
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	/**
	 * @return returnUrl 값 반환
	 */
	public String getReturnUrl() {
		return returnUrl;
	}
	/**
	 * @param returnUrl 파라미터를 returnUrl값에 설정
	 */
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	/**
	 * @return sendAddress 값 반환
	 */
	public String getSendAddress() {
		return sendAddress;
	}
	/**
	 * @param sendAddress 파라미터를 sendAddress값에 설정
	 */
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}	
	/**
	 * @return invalidAddress 값 반환
	 */
	public String getInvalidAddress() {
		return invalidAddress;
	}
	/**
	 * @param invalidAddress 파라미터를 invalidAddress값에 설정
	 */
	public void setInvalidAddress(String invalidAddress) {
		this.invalidAddress = invalidAddress;
	}
	/**
	 * @return errorOccur 값 반환
	 */
	public boolean isErrorOccur() {
		return errorOccur;
	}
	/**
	 * @param errorOccur 파라미터를 errorOccur값에 설정
	 */
	public void setErrorOccur(boolean errorOccur) {
		this.errorOccur = errorOccur;
	}
	
	public boolean isDetectVirus() {
		return detectVirus;
	}

	public void setDetectVirus(boolean detectVirus) {
		this.detectVirus = detectVirus;
	}

	/**
	 * @return errorMessage 값 반환
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage 파라미터를 errorMessage값에 설정
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * @return saveMid 값 반환
	 */
	public String getSaveMid() {
		if(saveMid != null){
			saveMid = saveMid.replaceAll("<", "");
			saveMid = saveMid.replaceAll(">", "");
		}
		return saveMid;
	}
	/**
	 * @param saveMid 파라미터를 saveMid값에 설정
	 */
	public void setSaveMid(String saveMid) {
		this.saveMid = saveMid;
	}
	/**
	 * @return sendAddressList 값 반환
	 */
	public InternetAddress[] getSendAddressList() {
		return sendAddressList;
	}
	/**
	 * @param sendAddressList 파라미터를 sendAddressList값에 설정
	 */
	public void setSendAddressList(InternetAddress[] sendAddressList) {
		this.sendAddressList = sendAddressList;
	}
	/**
	 * @return invalidAddressList 값 반환
	 */
	public InternetAddress[] getInvalidAddressList() {
		return invalidAddressList;
	}
	/**
	 * @param invalidAddressList 파라미터를 invalidAddressList값에 설정
	 */
	public void setInvalidAddressList(InternetAddress[] invalidAddressList) {
		this.invalidAddressList = invalidAddressList;
	}
	/**
	 * @return sendType 값 반환
	 */
	public String getSendType() {
		return sendType;
	}
	/**
	 * @param sendType 파라미터를 sendType값에 설정
	 */
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	/**
	 * @return sendFolderName 값 반환
	 */
	public String getSendFolderName() {
		return sendFolderName;
	}
	/**
	 * @param sendFolderName 파라미터를 sendFolderName값에 설정
	 */
	public void setSendFolderName(String sendFolderName) {
		this.sendFolderName = sendFolderName;
	}

	public void setInvalidAddrMap(Map<String, String> invalidAddrMap) {
		this.invalidAddrMap = invalidAddrMap;
	}
	
	private String getValidAddress(String addrStr){
		return getValidAddress(addrStr, false);
	}
	private String getValidAddress(String addrStr, boolean isOnlyAddrss){		
		if(addrStr != null && invalidAddrMap != null){
			InternetAddress[] address = TMailAddress.getParseEmailAddress(addrStr);
			StringBuffer sb = new StringBuffer();			
			int cnt = 0;
			for (InternetAddress inetAddress : address) {
				if(!invalidAddrMap.containsKey(inetAddress.getAddress())){
					if(cnt > 0){
						sb.append(",");
					}
					if(isOnlyAddrss){
						sb.append(inetAddress.getAddress());
					} else {
						if(inetAddress.getPersonal() != null){
							sb.append(inetAddress.toString());
						} else {
							sb.append(inetAddress.getAddress());
						}
					}
					cnt++;		
				}
			}
			addrStr = sb.toString();
			address = null;
			sb = null;
		}
		return addrStr;
	}	
	
	
	public String getAllVaildAddress(){
		String addrs = getValidAddress(sendAddress,true);
		return (addrs != null)?addrs:"";
	}

	public String getSendResultType() {
		return sendResultType;
	}

	public void setSendResultType(String sendResultType) {
		this.sendResultType = sendResultType;
	}

	public long getMailSize() {
		return mailSize;
	}

	public void setMailSize(long mailSize) {
		this.mailSize = mailSize;
	}

	public boolean isNoRcpt() {
		return noRcpt;
	}

	public void setNoRcpt(boolean noRcpt) {
		this.noRcpt = noRcpt;
	}

}
