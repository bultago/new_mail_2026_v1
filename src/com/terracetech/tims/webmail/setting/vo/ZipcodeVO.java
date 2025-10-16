package com.terracetech.tims.webmail.setting.vo;

import java.io.Serializable;

public class ZipcodeVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String zipcode = null;
	
	private String sido = null;
	
	private String gugun = null;
	
	private String dong = null;
	
	private String bunji = null;

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getSido() {
		return sido;
	}

	public void setSido(String sido) {
		this.sido = sido;
	}

	public String getGugun() {
		return gugun;
	}

	public void setGugun(String gugun) {
		this.gugun = gugun;
	}

	public String getDong() {
		return dong;
	}

	public void setDong(String dong) {
		this.dong = dong;
	}

	public String getBunji() {
		return bunji;
	}

	public void setBunji(String bunji) {
		this.bunji = bunji;
	}
	
}
