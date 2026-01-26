package com.terracetech.tims.service.tms.vo;

import java.io.Serializable;

public class SendResultVO implements Serializable{
	
	private static final long serialVersionUID = 20100304L;
	
	private boolean errorOccur = false;
	private String sendType = null;
	private String messageId = null;
	private String errorMsg = null;
	private String sendAddrs = null;
	private String invalidAddrs = null;	
	private String sendFolderName = null;
	
	public boolean isErrorOccur() {
		return errorOccur;
	}
	public void setErrorOccur(boolean errorOccur) {
		this.errorOccur = errorOccur;
	}
	
	public boolean isCheckErrorOccur() {
		return (invalidAddrs != null)?true:false;
	}	
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getSendAddrs() {		
		return sendAddrs;
	}
	public String[] getSendArrayAddrs(){
		String[] emails = null;
		if(sendAddrs != null){
			emails = sendAddrs.split(",");		
		}
		
		return emails;
	}
	
	public void setSendAddrs(String sendAddrs) {
		this.sendAddrs = sendAddrs;
	}
	public String getInvalidAddrs() {
		return invalidAddrs;
	}
	public String[] getInvalidArrayAddrs(){
		String[] emails = null;
		if(invalidAddrs != null){
			emails = invalidAddrs.split(",");		
		}
		
		return emails;
	}
	public void setInvalidAddrs(String invalidAddrs) {
		this.invalidAddrs = invalidAddrs;
	}
	public String getSendFolderName() {
		return sendFolderName;
	}
	public void setSendFolderName(String sendFolderName) {
		this.sendFolderName = sendFolderName;
	}	
	
}
