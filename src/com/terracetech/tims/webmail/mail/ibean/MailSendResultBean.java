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

import jakarta.mail.internet.InternetAddress;

import com.terracetech.tims.mail.TMailAddress;

/**
 * <p><strong>MailSendResultBean.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
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
	 * @return to �� ��ȯ
	 */
	public String getTo() {
		return to;
	}
	
	/**
	 * @return to �� ��ȯ
	 */
	public String getValidTo() {		
		return getValidAddress(to);		
	}
	/**
	 * @param to �Ķ���͸� to���� ����
	 */
	public void setTo(String to) {
		this.to = to;
	}
	/**
	 * @return cc �� ��ȯ
	 */
	public String getValidCc() {
		return getValidAddress(cc);				
	}
	
	public String getCc() {
		return cc;
	}
	/**
	 * @param cc �Ķ���͸� cc���� ����
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}
	/**
	 * @return bcc �� ��ȯ
	 */
	public String getBcc() {
		return bcc;
	}
	
	public String getValidBcc() {
		return getValidAddress(bcc);			
	}
	/**
	 * @param bcc �Ķ���͸� bcc���� ����
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	/**
	 * @return returnUrl �� ��ȯ
	 */
	public String getReturnUrl() {
		return returnUrl;
	}
	/**
	 * @param returnUrl �Ķ���͸� returnUrl���� ����
	 */
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	/**
	 * @return sendAddress �� ��ȯ
	 */
	public String getSendAddress() {
		return sendAddress;
	}
	/**
	 * @param sendAddress �Ķ���͸� sendAddress���� ����
	 */
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}	
	/**
	 * @return invalidAddress �� ��ȯ
	 */
	public String getInvalidAddress() {
		return invalidAddress;
	}
	/**
	 * @param invalidAddress �Ķ���͸� invalidAddress���� ����
	 */
	public void setInvalidAddress(String invalidAddress) {
		this.invalidAddress = invalidAddress;
	}
	/**
	 * @return errorOccur �� ��ȯ
	 */
	public boolean isErrorOccur() {
		return errorOccur;
	}
	/**
	 * @param errorOccur �Ķ���͸� errorOccur���� ����
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
	 * @return errorMessage �� ��ȯ
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage �Ķ���͸� errorMessage���� ����
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * @return saveMid �� ��ȯ
	 */
	public String getSaveMid() {
		if(saveMid != null){
			saveMid = saveMid.replaceAll("<", "");
			saveMid = saveMid.replaceAll(">", "");
		}
		return saveMid;
	}
	/**
	 * @param saveMid �Ķ���͸� saveMid���� ����
	 */
	public void setSaveMid(String saveMid) {
		this.saveMid = saveMid;
	}
	/**
	 * @return sendAddressList �� ��ȯ
	 */
	public InternetAddress[] getSendAddressList() {
		return sendAddressList;
	}
	/**
	 * @param sendAddressList �Ķ���͸� sendAddressList���� ����
	 */
	public void setSendAddressList(InternetAddress[] sendAddressList) {
		this.sendAddressList = sendAddressList;
	}
	/**
	 * @return invalidAddressList �� ��ȯ
	 */
	public InternetAddress[] getInvalidAddressList() {
		return invalidAddressList;
	}
	/**
	 * @param invalidAddressList �Ķ���͸� invalidAddressList���� ����
	 */
	public void setInvalidAddressList(InternetAddress[] invalidAddressList) {
		this.invalidAddressList = invalidAddressList;
	}
	/**
	 * @return sendType �� ��ȯ
	 */
	public String getSendType() {
		return sendType;
	}
	/**
	 * @param sendType �Ķ���͸� sendType���� ����
	 */
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	/**
	 * @return sendFolderName �� ��ȯ
	 */
	public String getSendFolderName() {
		return sendFolderName;
	}
	/**
	 * @param sendFolderName �Ķ���͸� sendFolderName���� ����
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
