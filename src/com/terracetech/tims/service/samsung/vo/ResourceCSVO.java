package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;

public class ResourceCSVO implements Serializable{

	private static final long serialVersionUID = 20091102L;
	
	/**
	 * 발신자 이메일주소
	 */
	private String email;
	
	/**
	 * 로케일정보
	 *  ko_KR (또는 zh_CN , 또는 ja_JP, 또는 en_US)
	 */
	private String locale;
	
	/**
	 * 메일인코딩정보
	 *  euc-kr (또는 utf-8)
	 */
	private String encoding;
	
	/**
	 * 패스워드
	 */
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
	
}
