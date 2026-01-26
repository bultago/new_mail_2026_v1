package com.terracetech.tims.webmail.common.vo;

import java.util.List;

public class CommonLogoVO {
	private String ImgMode = null;
	
	private String logoImgName = null;
	
	private byte[] logoImg = null;	
	
	private String logoImgUrl = null;
	
	public String getLogoImgUrl() {
		return logoImgUrl;
	}

	public void setLogoImgUrl(String logoImgUrl) {
		this.logoImgUrl = logoImgUrl;
	}

	public String getImgMode() {
		return ImgMode;
	}

	public void setImgMode(String imgMode) {
		ImgMode = imgMode;
	}

	public String getLogoImgName() {
		return logoImgName;
	}

	public void setLogoImgName(String logoImgName) {
		this.logoImgName = logoImgName;
	}

	public byte[] getLogoImg() {
		return logoImg;
	}

	public void setLogoImg(byte[] logoImg) {
		this.logoImg = logoImg;
	}	
}
