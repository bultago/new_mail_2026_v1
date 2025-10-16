package com.terracetech.tims.service.mcnc.vo;

import java.io.Serializable;

public class OptionWDO implements Serializable{
		
	private static final long serialVersionUID = -2249329346768759449L;
	
	private String name;
	private String value ;
	
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
	
	
}
