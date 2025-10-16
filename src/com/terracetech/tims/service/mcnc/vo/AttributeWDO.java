package com.terracetech.tims.service.mcnc.vo;

import java.io.Serializable;

public class AttributeWDO implements Serializable{
	
	private static final long serialVersionUID = -2616416528457534995L;
	
	private String name;
	private String value;
	private byte[] byteValue;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public byte[] getByteValue() {
		return byteValue;
	}
	public void setByteValue(byte[] byteValue) {
		this.byteValue = byteValue;
	}
	
}
