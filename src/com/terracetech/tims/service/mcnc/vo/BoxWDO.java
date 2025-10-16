package com.terracetech.tims.service.mcnc.vo;

import java.io.Serializable;

/**
 * 박스 타입 정의 클래스
 *  
 * @author kevin
 *
 */
public class BoxWDO implements Serializable{
	
	private static final long serialVersionUID = 8787756018103529730L;
	private String code;
	private String name;

	/**
	 * 박스등의 인스턴스를 만들 때 사용한다.
	 * @param code 박스 코드
	 * @param name 박스 이름
	 */
	
	public BoxWDO(){		
	}
	
	public BoxWDO( String code, String name ) {
		this.code			= code;
		this.name			= name;
	}
	
	/**
	 * 박스 이름을 얻는다.
	 * @return 박스 이름
	 */
	public String getName() {
		return name;
	}

	/**
	 * 박스 이름을 세팅한다.
	 * @param name 박스 이름
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 박스 코드를 얻는다.
	 * @return 박스 코드
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * 박스 코드를 세팅한다.
	 * @param code 박스 코드
	 */
	public void setCode(String code) {
		this.code = code;
	}
}