package com.terracetech.tims.service.mcnc.vo;

import java.io.Serializable;

public class PayloadWDO implements Serializable{
	
	private static final long serialVersionUID = -1979486782563452632L;
	
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
