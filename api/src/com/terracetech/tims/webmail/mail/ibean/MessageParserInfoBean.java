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
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li>¸Þ¼¼Áö¸¦ Parse ÇÏ±âÀ§ÇÑ Á¤º¸ ÀúÀå.</li>
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
	 * @return userId °ª ¹ÝÈ¯
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId ÆÄ¶ó¹ÌÅÍ¸¦ userId°ª¿¡ ¼³Á¤
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return locale °ª ¹ÝÈ¯
	 */
	public Locale getLocale() {
		return locale;
	}
	/**
	 * @param locale ÆÄ¶ó¹ÌÅÍ¸¦ locale°ª¿¡ ¼³Á¤
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	/**
	 * @return defaultCharset °ª ¹ÝÈ¯
	 */
	public String getDefaultCharset() {
		return defaultCharset;
	}
	/**
	 * @param defaultCharset ÆÄ¶ó¹ÌÅÍ¸¦ defaultCharset°ª¿¡ ¼³Á¤
	 */
	public void setDefaultCharset(String defaultCharset) {
		this.defaultCharset = defaultCharset;
	}
	/**
	 * @return tmpPath °ª ¹ÝÈ¯
	 */
	public String getTmpPath() {
		return tmpPath;
	}
	/**
	 * @param tmpPath ÆÄ¶ó¹ÌÅÍ¸¦ tmpPath°ª¿¡ ¼³Á¤
	 */
	public void setTmpPath(String tmpPath) {
		this.tmpPath = tmpPath;
	}
	/**
	 * @return attachesDir °ª ¹ÝÈ¯
	 */
	public String getAttachesDir() {
		return attachesDir;
	}
	/**
	 * @param attachesDir ÆÄ¶ó¹ÌÅÍ¸¦ attachesDir°ª¿¡ ¼³Á¤
	 */
	public void setAttachesDir(String attachesDir) {
		this.attachesDir = attachesDir;
	}
	/**
	 * @return attachesUrl °ª ¹ÝÈ¯
	 */
	public String getAttachesUrl() {
		return attachesUrl;
	}
	/**
	 * @param attachesUrl ÆÄ¶ó¹ÌÅÍ¸¦ attachesUrl°ª¿¡ ¼³Á¤
	 */
	public void setAttachesUrl(String attachesUrl) {
		this.attachesUrl = attachesUrl;
	}
	/**
	 * @return defaultImg °ª ¹ÝÈ¯
	 */
	public String getDefaultImg() {
		return defaultImg;
	}
	/**
	 * @param defaultImg ÆÄ¶ó¹ÌÅÍ¸¦ defaultImg°ª¿¡ ¼³Á¤
	 */
	public void setDefaultImg(String defaultImg) {
		this.defaultImg = defaultImg;
	}
	/**
	 * @return strLocalhost °ª ¹ÝÈ¯
	 */
	public String getStrLocalhost() {
		return strLocalhost;
	}
	/**
	 * @param strLocalhost ÆÄ¶ó¹ÌÅÍ¸¦ strLocalhost°ª¿¡ ¼³Á¤
	 */
	public void setStrLocalhost(String strLocalhost) {
		this.strLocalhost = strLocalhost;
	}
	/**
	 * @return hiddenImg °ª ¹ÝÈ¯
	 */
	public boolean isHiddenImg() {
		return hiddenImg;
	}
	/**
	 * @param hiddenImg ÆÄ¶ó¹ÌÅÍ¸¦ hiddenImg°ª¿¡ ¼³Á¤
	 */
	public void setHiddenImg(boolean hiddenImg) {
		this.hiddenImg = hiddenImg;
	}
	/**
	 * @return hiddenTag °ª ¹ÝÈ¯
	 */
	public boolean isHiddenTag() {
		return hiddenTag;
	}
	/**
	 * @param hiddenTag ÆÄ¶ó¹ÌÅÍ¸¦ hiddenTag°ª¿¡ ¼³Á¤
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
