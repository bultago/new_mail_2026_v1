package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;

public class ESBFaultVO implements Serializable{

	private static final long serialVersionUID = 20091102L;
	
	/**
	 * fault 발생 행위자 
	 *  AP
	 */
	private String faultActor1;
	
	/**
	 * fault 코드
	 *  AP0001
	 */
	private String faultCode1;
	
	/**
	 * fault 문자열
	 *  TimeZone not found
	 */
	private String faultString1;
	
	/**
	 * fault 메시지 
	 */
	private String faultMessage;

	public String getFaultActor1() {
		return faultActor1;
	}

	public void setFaultActor1(String faultActor1) {
		this.faultActor1 = faultActor1;
	}

	public String getFaultCode1() {
		return faultCode1;
	}

	public void setFaultCode1(String faultCode1) {
		this.faultCode1 = faultCode1;
	}

	public String getFaultString1() {
		return faultString1;
	}

	public void setFaultString1(String faultString1) {
		this.faultString1 = faultString1;
	}

	public String getFaultMessage() {
		return faultMessage;
	}

	public void setFaultMessage(String faultMessage) {
		this.faultMessage = faultMessage;
	}
	
}
