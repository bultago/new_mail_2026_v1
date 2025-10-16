package com.terracetech.tims.service.mcnc.vo;

import java.io.Serializable;

public class BodyWDO implements Serializable{

	private static final long serialVersionUID = -4098680514679069550L;

	/**
	 * HTML 타입 
	 */
	public static final int		BODY_TYPE_HTML		= 0;
	
	/**
	 * TEXT 타입
	 */
	public static final int		BODY_TYPE_TEXT		= 1;
	
	/**
	 * HTTP URL 타입
	 */
	public static final int		BODY_TYPE_HTTP_URL	= 2;
	
	/*
	 * HWP type
	 */
	public static final int		BODY_TYPE_HWP	= 3;

	
	private int					bodyType	= 0;
	private String				body;	
	private String				encoding;
	private byte[]				objectBody;
	
	/**
	 * 본문을 얻는다.
	 * @return 본문
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * 본문을 세팅한다.
	 * @param body 본문
	 */
	public void setBody(String body) {
		this.body = body;
	}
	
	/**
	 * 본문 형식을 얻는다.
	 * 본문 타입은 HTML, Text, HTTP URL Link
	 * @return 본문 형식
	 */
	public int getBodyType() {
		return bodyType;
	}
	
	/**
	 * 본문 형식을 세팅한다.
	 * 본문 타입은 HTML, Text, HTTP URL Link
	 * @param bodyType 본문 형식
	 */
	public void setBodyType( int bodyType ) {
		this.bodyType = bodyType;
	}
	
	/**
	 * 본문 인코딩을 얻는다.
	 * 예를들어, "UTF-8"일 수 있다. 
	 * @return 본문 인코딩
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * 본문 인코딩을 세팅한다.
	 * @param encoding 본문 인코딩
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public byte[] getObjectBody() {
		return objectBody;
	}

	public void setObjectBody(byte[] objectBody) {
		this.objectBody = objectBody;
	}
}