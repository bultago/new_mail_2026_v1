package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;

public class RecipientEtyCSVO implements Serializable{

	private static final long serialVersionUID = 20091102L;
	
	/**
	 *  0부터…순차적으로
	 */
	private int iSeqID;
	
	/**
	 * 수신형태  t  (또는 c , 또는 b)
	 */
	private String recType;
	
	/**
	 * 수신인 이메일 주소
	 * 
	 */
	private String recAddr;

	public int getiSeqID() {
		return iSeqID;
	}

	public void setiSeqID(int iSeqID) {
		this.iSeqID = iSeqID;
	}

	public String getRecType() {
		return recType;
	}

	public void setRecType(String recType) {
		this.recType = recType;
	}

	public String getRecAddr() {
		return recAddr;
	}

	public void setRecAddr(String recAddr) {
		this.recAddr = recAddr;
	}
	
}
