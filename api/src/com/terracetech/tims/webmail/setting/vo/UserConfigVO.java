/**
 * UserConfigVO.java 2008  2008-09-26
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.setting.vo;

/**
 * <p><strong>UserConfigVO.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li>»ç¿ëÀÚ È¯°æ ¼³Á¤ Á¤º¸¸¦ °¡Á®¿À±â À§ÇÑ VO. ¸ÞÀÏ ¼­ºñ½º¸¦ »ç¿ëÇÏ±â À§ÇÑ ¼³Á¤ Á¤º¸¸¦ Ç¥Çö.</li>
 * <li>ÆäÀÌÁö °¹¼ö, ¾ð¾î ¼Û½ÅÀÌ¸§µîÀÇ ¼­ºñ½º »ç¿ë½ÃÀÇ ¼³Á¤ Á¤º¸Æ÷ÇÔ.</li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class UserConfigVO {
	
	/**
	 * <p>ÆäÀÌÂ¡ ´ÜÀ§ °¹¼ö</p>
	 */
	private int pageLineCnt = 0;
	/**
	 * <p>¸ÞÀÏ ¼Û½Å½Ã º¸³»´Â ÀÌ¸§</p>
	 */
	private String senderName = null;
	/**
	 * <p>¼ö½ÅÈ®ÀÎ ÇÔ ÀúÀå</p>
	 */
	private String saveSendBox = null;
	/**
	 * <p>¼ö½ÅÈ®ÀÎ ±â´É ¼³Á¤</p>
	 * <p>¼Ó¼º : on/off</p>
	 */
	private String receiveNoti = null;
	/**
	 * <p>Vcard »ç¿ë¿©ºÎ</p>
	 */
	private String vcardAttach = null;
	/**
	 * <p>»ç¿ëÀÚ ÆäÀÌÁö ¾ð¾î ¼³Á¤</p>
	 */
	private String userLocale = null;
	/**
	 * <p>¾²±â ¸ðµå ¼³Á¤</p>
	 * <p>¼Ó¼º : text/html</p>
	 */
	private String writeMode = null;
	/**
	 * <p>¸ÞÀÏ ÀÛ¼º½Ã ¾ð¾î¼Â</p>
	 */
	private String charSet = null;
	/**
	 * <p>¸ÞÀÏ ÀÐ±â½Ã ÀÌ¹ÌÁö º¸±â ¼³Á¤</p>
	 * <p>¼Ó¼º : on/off</p>
	 */
	private String hiddenImg = null;
	/**
	 * <p>¼­¸í Ã·ºÎ ¿©ºÎ</p>
	 * <p>¼Ó¼º : on/off</p>
	 */
	private String signAttach = null;
	/**
	 * <p>·Î±×ÀÎÈÄ ÀÌµ¿ ÆäÀÌÁö ¼³Á¤</p>
	 */
	private String afterLogin = null;
	/**
	 * <p>¸ÞÀÏ ÀÐ±â½Ã ÅÂ±× º¸±â ¼³Á¤</p>
	 * <p>¼Ó¼º : on/off</p>
	 */
	private String hiddenTag = null;
	/**
	 * <p>»õ¸ÞÀÏ ¾Ë¸² ÁÖ±â ¼³Á¤</p>
	 */
	private int notiInterval = 0;
	/**
	 * <p>»ç¿ëÀÚ ½ºÅ² ¼³Á¤. Left ¸Þ´º¸¸ Àû¿ë</p>
	 */
	private String userSkin = null;
	/**
	 * <p>Àü´Þ ¸ðµå ¼³Á¤.</p>
	 */
	private String forwardingMode = null;
	/**
	 * <p>¼ÛÁøÀÚ Email  ÁÖ¼Ò ¼³Á¤.</p>
	 */
	private String senderEmail = null;
	/**
	 * @return pageLineCnt °ª ¹ÝÈ¯
	 */
	public int getPageLineCnt() {
		return pageLineCnt;
	}
	/**
	 * @param pageLineCnt ÆÄ¶ó¹ÌÅÍ¸¦ pageLineCnt°ª¿¡ ¼³Á¤
	 */
	public void setPageLineCnt(int pageLineCnt) {
		this.pageLineCnt = pageLineCnt;
	}
	/**
	 * @return senderName °ª ¹ÝÈ¯
	 */
	public String getSenderName() {
		return senderName;
	}
	/**
	 * @param senderName ÆÄ¶ó¹ÌÅÍ¸¦ senderName°ª¿¡ ¼³Á¤
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	/**
	 * @return saveSendBox °ª ¹ÝÈ¯
	 */
	public String getSaveSendBox() {
		return saveSendBox;
	}
	/**
	 * @param saveSendBox ÆÄ¶ó¹ÌÅÍ¸¦ saveSendBox°ª¿¡ ¼³Á¤
	 */
	public void setSaveSendBox(String saveSendBox) {
		this.saveSendBox = saveSendBox;
	}
	/**
	 * @return receiveNoti °ª ¹ÝÈ¯
	 */
	public String getReceiveNoti() {
		return receiveNoti;
	}
	/**
	 * @param receiveNoti ÆÄ¶ó¹ÌÅÍ¸¦ receiveNoti°ª¿¡ ¼³Á¤
	 */
	public void setReceiveNoti(String receiveNoti) {
		this.receiveNoti = receiveNoti;
	}
	/**
	 * @return vcardAttach °ª ¹ÝÈ¯
	 */
	public String getVcardAttach() {
		return vcardAttach;
	}
	/**
	 * @param vcardAttach ÆÄ¶ó¹ÌÅÍ¸¦ vcardAttach°ª¿¡ ¼³Á¤
	 */
	public void setVcardAttach(String vcardAttach) {
		this.vcardAttach = vcardAttach;
	}
	/**
	 * @return userLocale °ª ¹ÝÈ¯
	 */
	public String getUserLocale() {
		return userLocale;
	}
	/**
	 * @param userLocale ÆÄ¶ó¹ÌÅÍ¸¦ userLocale°ª¿¡ ¼³Á¤
	 */
	public void setUserLocale(String userLocale) {
		this.userLocale = userLocale;
	}
	/**
	 * @return writeMode °ª ¹ÝÈ¯
	 */
	public String getWriteMode() {
		return writeMode;
	}
	/**
	 * @param writeMode ÆÄ¶ó¹ÌÅÍ¸¦ writeMode°ª¿¡ ¼³Á¤
	 */
	public void setWriteMode(String writeMode) {
		this.writeMode = writeMode;
	}
	/**
	 * @return charSet °ª ¹ÝÈ¯
	 */
	public String getCharSet() {
		return charSet;
	}
	/**
	 * @param charSet ÆÄ¶ó¹ÌÅÍ¸¦ charSet°ª¿¡ ¼³Á¤
	 */
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}
	/**
	 * @return hiddenImg °ª ¹ÝÈ¯
	 */
	public String getHiddenImg() {
		return hiddenImg;
	}
	/**
	 * @param hiddenImg ÆÄ¶ó¹ÌÅÍ¸¦ hiddenImg°ª¿¡ ¼³Á¤
	 */
	public void setHiddenImg(String hiddenImg) {
		this.hiddenImg = hiddenImg;
	}
	/**
	 * @return signAttach °ª ¹ÝÈ¯
	 */
	public String getSignAttach() {
		return signAttach;
	}
	/**
	 * @param signAttach ÆÄ¶ó¹ÌÅÍ¸¦ signAttach°ª¿¡ ¼³Á¤
	 */
	public void setSignAttach(String signAttach) {
		this.signAttach = signAttach;
	}
	/**
	 * @return afterLogin °ª ¹ÝÈ¯
	 */
	public String getAfterLogin() {
		return afterLogin;
	}
	/**
	 * @param afterLogin ÆÄ¶ó¹ÌÅÍ¸¦ afterLogin°ª¿¡ ¼³Á¤
	 */
	public void setAfterLogin(String afterLogin) {
		this.afterLogin = afterLogin;
	}
	/**
	 * @return hiddenTag °ª ¹ÝÈ¯
	 */
	public String getHiddenTag() {
		return hiddenTag;
	}
	/**
	 * @param hiddenTag ÆÄ¶ó¹ÌÅÍ¸¦ hiddenTag°ª¿¡ ¼³Á¤
	 */
	public void setHiddenTag(String hiddenTag) {
		this.hiddenTag = hiddenTag;
	}
	/**
	 * @return notiInterval °ª ¹ÝÈ¯
	 */
	public int getNotiInterval() {
		return notiInterval;
	}
	/**
	 * @param notiInterval ÆÄ¶ó¹ÌÅÍ¸¦ notiInterval°ª¿¡ ¼³Á¤
	 */
	public void setNotiInterval(int notiInterval) {
		this.notiInterval = notiInterval;
	}
	/**
	 * @return userSkin °ª ¹ÝÈ¯
	 */
	public String getUserSkin() {
		return userSkin;
	}
	/**
	 * @param userSkin ÆÄ¶ó¹ÌÅÍ¸¦ userSkin°ª¿¡ ¼³Á¤
	 */
	public void setUserSkin(String userSkin) {
		this.userSkin = userSkin;
	}
	/**
	 * @return forwardingMode °ª ¹ÝÈ¯
	 */
	public String getForwardingMode() {
		return forwardingMode;
	}
	/**
	 * @param forwardingMode ÆÄ¶ó¹ÌÅÍ¸¦ forwardingMode°ª¿¡ ¼³Á¤
	 */
	public void setForwardingMode(String forwardingMode) {
		this.forwardingMode = forwardingMode;
	}
	/**
	 * @return senderEmail °ª ¹ÝÈ¯
	 */
	public String getSenderEmail() {
		return senderEmail;
	}
	/**
	 * @param senderEmail ÆÄ¶ó¹ÌÅÍ¸¦ senderEmail°ª¿¡ ¼³Á¤
	 */
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	
	
	
}
