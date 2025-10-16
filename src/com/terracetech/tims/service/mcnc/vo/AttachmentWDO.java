package com.terracetech.tims.service.mcnc.vo;

import java.io.Serializable;

/**
 * 첨부 파일 데이터를 표현하는 클래스
 * @author kevin
 *
 */
public class AttachmentWDO extends AttachmentInfoWDO implements Serializable{
	
	private static final long serialVersionUID = 456597221450835808L;
	private boolean encrypted;	
	private byte[] data;
	
	/**
	 * 첨부 파일 데이터를 얻는다.
	 * @return 첨부 파일 데이터
	 */
	public byte[] getData() {
		return data;
	}
	
	/**
	 * 첨부 파일 데이터를 세팅한다.
	 * @param data 첨부 파일 데이터
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	public boolean getEncrypted() {
		return encrypted;
	}

	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}
}