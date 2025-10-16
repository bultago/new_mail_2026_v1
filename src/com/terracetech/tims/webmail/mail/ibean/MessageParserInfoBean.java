/**
 * MessageParserInfoBean.java 2008. 12. 4.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.ibean;

import java.util.Locale;

/**
 * <p><strong>MessageParserInfoBean.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li>메세지를 Parse 하기위한 정보 저장.</li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MessageParserInfoBean {
	
	private String userId = null;	
	private Locale locale = null; 
	private String defaultCharset = null; 
	private String tmpPath = null;
	private String attachesDir = null;
	private String attachesUrl = null;
	private String defaultImg = null;
	private String strLocalhost = null;	 
	private boolean hiddenImg = false; 
	private boolean hiddenTag = false;
	private boolean imgResize = false;
	private int imgResizeWidth = 0;
	
	/**
	 * @return userId 값 반환
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId 파라미터를 userId값에 설정
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return locale 값 반환
	 */
	public Locale getLocale() {
		return locale;
	}
	/**
	 * @param locale 파라미터를 locale값에 설정
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	/**
	 * @return defaultCharset 값 반환
	 */
	public String getDefaultCharset() {
		return defaultCharset;
	}
	/**
	 * @param defaultCharset 파라미터를 defaultCharset값에 설정
	 */
	public void setDefaultCharset(String defaultCharset) {
		this.defaultCharset = defaultCharset;
	}
	/**
	 * @return tmpPath 값 반환
	 */
	public String getTmpPath() {
		return tmpPath;
	}
	/**
	 * @param tmpPath 파라미터를 tmpPath값에 설정
	 */
	public void setTmpPath(String tmpPath) {
		this.tmpPath = tmpPath;
	}
	/**
	 * @return attachesDir 값 반환
	 */
	public String getAttachesDir() {
		return attachesDir;
	}
	/**
	 * @param attachesDir 파라미터를 attachesDir값에 설정
	 */
	public void setAttachesDir(String attachesDir) {
		this.attachesDir = attachesDir;
	}
	/**
	 * @return attachesUrl 값 반환
	 */
	public String getAttachesUrl() {
		return attachesUrl;
	}
	/**
	 * @param attachesUrl 파라미터를 attachesUrl값에 설정
	 */
	public void setAttachesUrl(String attachesUrl) {
		this.attachesUrl = attachesUrl;
	}
	/**
	 * @return defaultImg 값 반환
	 */
	public String getDefaultImg() {
		return defaultImg;
	}
	/**
	 * @param defaultImg 파라미터를 defaultImg값에 설정
	 */
	public void setDefaultImg(String defaultImg) {
		this.defaultImg = defaultImg;
	}
	/**
	 * @return strLocalhost 값 반환
	 */
	public String getStrLocalhost() {
		return strLocalhost;
	}
	/**
	 * @param strLocalhost 파라미터를 strLocalhost값에 설정
	 */
	public void setStrLocalhost(String strLocalhost) {
		this.strLocalhost = strLocalhost;
	}
	/**
	 * @return hiddenImg 값 반환
	 */
	public boolean isHiddenImg() {
		return hiddenImg;
	}
	/**
	 * @param hiddenImg 파라미터를 hiddenImg값에 설정
	 */
	public void setHiddenImg(boolean hiddenImg) {
		this.hiddenImg = hiddenImg;
	}
	/**
	 * @return hiddenTag 값 반환
	 */
	public boolean isHiddenTag() {
		return hiddenTag;
	}
	/**
	 * @param hiddenTag 파라미터를 hiddenTag값에 설정
	 */
	public void setHiddenTag(boolean hiddenTag) {
		this.hiddenTag = hiddenTag;
	}
	public boolean isImgResize() {
		return imgResize;
	}
	public void setImgResize(boolean imgResize) {
		this.imgResize = imgResize;
	}
	public int getImgResizeWidth() {
		return imgResizeWidth;
	}
	public void setImgResizeWidth(int imgResizeWidth) {
		this.imgResizeWidth = imgResizeWidth;
	}
	
	
	
	
	
}
