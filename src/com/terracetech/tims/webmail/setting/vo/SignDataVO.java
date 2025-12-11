/**
 * SignDataVO.java 2008. 11. 26.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.setting.vo;


/**
 * <p><strong>SignDataVO.java</strong> Class Description</p>
 * <p>»ç¿ëÀÚ°¡ ÁöÁ¤ÇÑ SignÀÇ ÀÌ¹ÌÁö¿Í ÅØ½ºÆ®¸¦ °¡Áö°í ÀÖ´Ù.</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author jpjung
 * @since Tims7
 * @version 7.0 
 */
public class SignDataVO {

	private int signSeq;
	
	private int userSeq;
	
	private String signName;
	
	private String signText;
	
	private String signMode;
	
	private byte[] signImage;
	
	private String defaultSign;
	
	private String signImageUrl;
	
	private String signImageName;

	public int getSignSeq() {
		return signSeq;
	}

	public void setSignSeq(int signSeq) {
		this.signSeq = signSeq;
	}

	public int getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getSignText() {
		return signText;
	}

	public void setSignText(String signText) {
		this.signText = signText;
	}

	public String getSignMode() {
		return signMode;
	}

	public void setSignMode(String signMode) {
		this.signMode = signMode;
	}

	public byte[] getSignImage() {
		return signImage;
	}

	public void setSignImage(byte[] signImage) {
		this.signImage = signImage;
	}

	public String getDefaultSign() {
		return defaultSign;
	}

	public void setDefaultSign(String defaultSign) {
		this.defaultSign = defaultSign;
	}

	public String getSignImageUrl() {
		return signImageUrl;
	}

	public void setSignImageUrl(String signImageUrl) {
		this.signImageUrl = signImageUrl;
	}

	public String getSignImageName() {
		return signImageName;
	}

	public void setSignImageName(String signImageName) {
		this.signImageName = signImageName;
	}
}
