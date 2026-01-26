/**
 * MailUserDomainVO.java 2008  2008-09-26
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.vo;

/**
 * <p><strong>MailUserDomainVO.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li>»ç¿ëÀÚÀÇ µµ¸ÞÀÎ Á¤º¸ ¸¦ °¡Á®¿À´ÂVO. µµ¸ÞÀÎ¿¡ °üÇÑ ±âº» Á¤º¸¸¦ Ç¥Çö</li>
 * <li>µµ¸ÞÀÎ¸í, ¼­ºñ½º ½ÃÀÛ ½Ã°£, ÆÐ½º¿öµå ¾ÏÈ£È­ ¹æ½Ä, ÇöÀç »ç¿ëÀÚ¼ö ¸ÞÀÏ¼­ºñ½º µîÀÇ ¿É¼Ç Ç×¸ñ Æ÷ÇÔ</li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MailUserDomainVO {
	/**
	 * <p>¸ÞÀÏ µµ¸ÞÀÎ ¸í</p>
	 */
	private String mailDomain = null;
	/**
	 * <p>¸ÞÀÏ µµ¸ÞÀÎ º°Äª</p>
	 */
	private String mailDomainName = null;
	/**
	 * <p>¼­ºñ½º ½ÃÀÛ ³¯Â¥</p>
	 */
	private String serviceStartTime = null;
	/**
	 * <p>¼­ºñ½º Á¾·á ³¯Â¥</p>
	 */
	private String serviceEndTime = null;
	/**
	 * <p>µµ¸ÞÀÎ »ç¿ë ¿©ºÎ</p>
	 * <p>¼Ó¼º : enabled / disabled</p>
	 */
	private String domainStatus = null;
	/**
	 * <p>ÃÖ´ë »ç¿ëÀÚ¼ö</p>
	 */
	private String maxUserCount = null;
	/**
	 * <p>ÇöÀç »ç¿ëÀÚ¼ö</p>
	 */
	private String currentUserCount = null;
	/**
	 * <p>ºñ¹Ð¹øÈ£ ¾ÏÈ£È­ ¹æ½Ä</p>
	 */	
	private String passwordMethod = null;
	/**
	 * <p>»ç¿ëÀÚ°¡ »ç¿ëÇÏ´Â ¸ÞÀÏ ¼­ºñ½º Á¾·ù. ¼ýÀÚ¸¦ ÀÌ¿ëÇÏ¿© ¼³Á¤. ¼³Á¤½Ã °¢ ¼­ºñ½º ¼ýÀÚ °ªÀ» ÇÕ»êÇÏ¿© Á¤º¸¸¦ ¼³Á¤</p>
	 * <p>¼Ó¼º : ums(1) fax(2) pop(4) imap(8) webmail(16) security(32) voice(64) virus(128) smtpauth(256)</p>
	 */
	private String mailServices = null;
	/**
	 * <p>½ºÆÔ ¸ÞÀÏ Á¤Ã¥ ¼³Á¤</p>
	 */
	private String defaultPspam = null;
	/**
	 * <p>µµ¸ÞÀÎ ÇÊÅÍ »ç¿ë.</p>
	 */
	private String filterApply = null;
	/**
	 * @return mailDomain °ª ¹ÝÈ¯
	 */
	public String getMailDomain() {
		return mailDomain;
	}
	/**
	 * @param mailDomain ÆÄ¶ó¹ÌÅÍ¸¦ mailDomain°ª¿¡ ¼³Á¤
	 */
	public void setMailDomain(String mailDomain) {
		this.mailDomain = mailDomain;
	}
	/**
	 * @return mailDomainName °ª ¹ÝÈ¯
	 */
	public String getMailDomainName() {
		return mailDomainName;
	}
	/**
	 * @param mailDomainName ÆÄ¶ó¹ÌÅÍ¸¦ mailDomainName°ª¿¡ ¼³Á¤
	 */
	public void setMailDomainName(String mailDomainName) {
		this.mailDomainName = mailDomainName;
	}
	/**
	 * @return serviceStartTime °ª ¹ÝÈ¯
	 */
	public String getServiceStartTime() {
		return serviceStartTime;
	}
	/**
	 * @param serviceStartTime ÆÄ¶ó¹ÌÅÍ¸¦ serviceStartTime°ª¿¡ ¼³Á¤
	 */
	public void setServiceStartTime(String serviceStartTime) {
		this.serviceStartTime = serviceStartTime;
	}
	/**
	 * @return serviceEndTime °ª ¹ÝÈ¯
	 */
	public String getServiceEndTime() {
		return serviceEndTime;
	}
	/**
	 * @param serviceEndTime ÆÄ¶ó¹ÌÅÍ¸¦ serviceEndTime°ª¿¡ ¼³Á¤
	 */
	public void setServiceEndTime(String serviceEndTime) {
		this.serviceEndTime = serviceEndTime;
	}
	/**
	 * @return domainStatus °ª ¹ÝÈ¯
	 */
	public String getDomainStatus() {
		return domainStatus;
	}
	/**
	 * @param domainStatus ÆÄ¶ó¹ÌÅÍ¸¦ domainStatus°ª¿¡ ¼³Á¤
	 */
	public void setDomainStatus(String domainStatus) {
		this.domainStatus = domainStatus;
	}
	/**
	 * @return maxUserCount °ª ¹ÝÈ¯
	 */
	public String getMaxUserCount() {
		return maxUserCount;
	}
	/**
	 * @param maxUserCount ÆÄ¶ó¹ÌÅÍ¸¦ maxUserCount°ª¿¡ ¼³Á¤
	 */
	public void setMaxUserCount(String maxUserCount) {
		this.maxUserCount = maxUserCount;
	}
	/**
	 * @return currentUserCount °ª ¹ÝÈ¯
	 */
	public String getCurrentUserCount() {
		return currentUserCount;
	}
	/**
	 * @param currentUserCount ÆÄ¶ó¹ÌÅÍ¸¦ currentUserCount°ª¿¡ ¼³Á¤
	 */
	public void setCurrentUserCount(String currentUserCount) {
		this.currentUserCount = currentUserCount;
	}
	/**
	 * @return passwordMethod °ª ¹ÝÈ¯
	 */
	public String getPasswordMethod() {
		return passwordMethod;
	}
	/**
	 * @param passwordMethod ÆÄ¶ó¹ÌÅÍ¸¦ passwordMethod°ª¿¡ ¼³Á¤
	 */
	public void setPasswordMethod(String passwordMethod) {
		this.passwordMethod = passwordMethod;
	}
	/**
	 * @return mailServices °ª ¹ÝÈ¯
	 */
	public String getMailServices() {
		return mailServices;
	}
	/**
	 * @param mailServices ÆÄ¶ó¹ÌÅÍ¸¦ mailServices°ª¿¡ ¼³Á¤
	 */
	public void setMailServices(String mailServices) {
		this.mailServices = mailServices;
	}
	/**
	 * @return defaultPspam °ª ¹ÝÈ¯
	 */
	public String getDefaultPspam() {
		return defaultPspam;
	}
	/**
	 * @param defaultPspam ÆÄ¶ó¹ÌÅÍ¸¦ defaultPspam°ª¿¡ ¼³Á¤
	 */
	public void setDefaultPspam(String defaultPspam) {
		this.defaultPspam = defaultPspam;
	}
	/**
	 * @return filterApply °ª ¹ÝÈ¯
	 */
	public String getFilterApply() {
		return filterApply;
	}
	/**
	 * @param filterApply ÆÄ¶ó¹ÌÅÍ¸¦ filterApply°ª¿¡ ¼³Á¤
	 */
	public void setFilterApply(String filterApply) {
		this.filterApply = filterApply;
	}

	
	
}
