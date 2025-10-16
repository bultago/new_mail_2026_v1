package com.terracetech.tims.service.mcnc.vo;

import java.io.Serializable;

/**
 * 사용자 인증정보를 나타낸다.
 * @author kevin
 *
 */
public class UserAuthWDO implements Serializable{
	
	private static final long serialVersionUID = 2177629768300424390L;
	
	private String id;
	private String email;
	private String password;
	private String sessionKey;
	private String remoteIp;
	private PayloadWDO[] payload;	

	/**
	 * 사용자 아이디를 얻는다.
	 * @return 사용자 아이디
	 */
	public String getID() {
		return id;
	}

	/**
	 * 사용자 아이디를 세팅한다.
	 * @param id 사용자 아이디
	 */
	public void setID(String id) {
		this.id = id;
	}

	/**
	 * 사용자 패스워드를 얻는다.
	 * @return 사용자 패스워드
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * 사용자 패스워드를 세팅한다.
	 * @param password 사용자 패스워드
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public PayloadWDO[] getPayload() {
		return payload;
	}

	public void setPayload(PayloadWDO[] payload) {
		this.payload = payload;
	}
}