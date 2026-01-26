/**
 * MailUserGroupVO.java 2008  2008-09-26
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.vo;

/**
 * <p><strong>MailUserGroupVO.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li>»ç¿ëÀÚ°¡ ¼ÓÇÑ ±×·ìÀÇ Á¤º¸¸¦ °¡Á®¿À±â À§ÇÑ VO. ±×·ì´ÜÀ§ÀÇ ¼³Á¤ºÎºÐÀ» °¡Á®¿Â´Ù.</li>
 * <li>±×·ì¸í, ÄõÅÍ °ü·Ã ¼Ó¼º, ¸ÞÀÏÇÔ À¯È¿±â°£ ¼³Á¤, ±âÅ¸ »ç¿ëÀÚ default ¼³Á¤ Á¤º¸¸¦ Æ÷ÇÔ.</li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MailUserGroupVO {
	
	/**
	 * <p>±×·ì¸í Á¤º¸</p>
	 */
	private String mailGroup = null;
	/**
	 * <p>±×·ì º°Äª</p>
	 */
	private String mailGroupName = null;
	/**
	 * <p>±×·ì ±âº» ÄõÅÍ ¿ë·®. 'ÄõÅÍ¿ë·®S, Ä«¿îÅÍ¼öC' ÇüÅÂ·Î ÀúÀå.</p>
	 */
	private String quota = null;
	/**
	 * <p>±×·ì À¥Æú´õ ÄõÅÍ Á¤º¸</p>
	 */
	private String webfolderQuota = null;
	/**
	 * <p>ÃÖ´ë Æ÷¿öµù °¹¼ö</p>
	 */
	private String maxForwarding = null;
	/**
	 * <p>ÄõÅÍ ÃÊ°ú »ç¿ë ºñÀ². 1~100% »çÀÌ</p>
	 */
	private String quotaOverlookRatio = null;
	/**
	 * <p>ÄõÅÍ ÃÊ°ú °æ°í ¸ðµå</p>
	 * <p>¼Ó¼º : on/off</p>
	 */
	private String quotaWarningMode = null;
	/**
	 * <p>ÄõÅÍ À§Çè »ç¿ë ºñÀ². 1~100% »çÀÌ</p>
	 */
	private String quotaWarningRatio = null;
	/**
	 * <p>ÄõÅÍ ¿À·ù Ã³¸® ¹æ¹ý</p>
	 */
	private String quotaViolationAction = null;
	/**
	 * <p>»ç¿ëÀÚ°¡ »ç¿ëÇÏ´Â ¸ÞÀÏ ¼­ºñ½º Á¾·ù. ¼ýÀÚ¸¦ ÀÌ¿ëÇÏ¿© ¼³Á¤. ¼³Á¤½Ã °¢ ¼­ºñ½º ¼ýÀÚ °ªÀ» ÇÕ»êÇÏ¿© Á¤º¸¸¦ ¼³Á¤</p>
	 * <p>¼Ó¼º : ums(1) fax(2) pop(4) imap(8) webmail(16) security(32) voice(64) virus(128) smtpauth(256)</p>
	 * 
	 */
	private int mailServices = 0;
	/**
	 * <p>ÃÖ´ë ¸ÞÀÏ ¼Û½Å Å©±â</p>
	 */
	private String maxSendSize = null;
	/**
	 * <p>¹ÞÀº ¸ÞÀÏÇÔ À¯È¿±â°£ </p>
	 */
	private String inboxExpireDays = null;
	/**
	 * <p>º¸³½ ¸ÞÀÏÇÔ À¯È¿±â°£ </p>
	 */
	private String sentExpireDays = null;
	/**
	 * <p>ÈÞÁöÅë ¸ÞÀÏÇÔ À¯È¿±â°£</p>
	 */
	private String trashExpireDays = null;
	/**
	 * <p>½ºÆÔ ¸ÞÀÏÇÔ À¯È¿±â°£</p>
	 */
	private String spamExpireDays = null;
	/**
	 * <p>»ç¿ëÀÚ ¸ÞÀÏÇÔ À¯È¿±â°£ </p>
	 */
	private String userExpireDays = null;
	/**
	 * <p>±×·ì ¿¡ ´ëÇÑ ¼³¸í Á¤º¸</p>
	 */
	private String description = null;
	
	/**
	 * @return mailGroup °ª ¹ÝÈ¯
	 */
	public String getMailGroup() {
		return mailGroup;
	}
	/**
	 * @param mailGroup ÆÄ¶ó¹ÌÅÍ¸¦ mailGroup°ª¿¡ ¼³Á¤
	 */
	public void setMailGroup(String mailGroup) {
		this.mailGroup = mailGroup;
	}
	/**
	 * @return mailGroupName °ª ¹ÝÈ¯
	 */
	public String getMailGroupName() {
		return mailGroupName;
	}
	/**
	 * @param mailGroupName ÆÄ¶ó¹ÌÅÍ¸¦ mailGroupName°ª¿¡ ¼³Á¤
	 */
	public void setMailGroupName(String mailGroupName) {
		this.mailGroupName = mailGroupName;
	}
	/**
	 * @return quota °ª ¹ÝÈ¯
	 */
	public String getQuota() {
		return quota;
	}
	/**
	 * @param quota ÆÄ¶ó¹ÌÅÍ¸¦ quota°ª¿¡ ¼³Á¤
	 */
	public void setQuota(String quota) {
		this.quota = quota;
	}
	/**
	 * @return webfolderQuota °ª ¹ÝÈ¯
	 */
	public String getWebfolderQuota() {
		return webfolderQuota;
	}
	/**
	 * @param webfolderQuota ÆÄ¶ó¹ÌÅÍ¸¦ webfolderQuota°ª¿¡ ¼³Á¤
	 */
	public void setWebfolderQuota(String webfolderQuota) {
		this.webfolderQuota = webfolderQuota;
	}
	/**
	 * @return maxForwarding °ª ¹ÝÈ¯
	 */
	public String getMaxForwarding() {
		return maxForwarding;
	}
	/**
	 * @param maxForwarding ÆÄ¶ó¹ÌÅÍ¸¦ maxForwarding°ª¿¡ ¼³Á¤
	 */
	public void setMaxForwarding(String maxForwarding) {
		this.maxForwarding = maxForwarding;
	}
	/**
	 * @return quotaOverlookRatio °ª ¹ÝÈ¯
	 */
	public String getQuotaOverlookRatio() {
		return quotaOverlookRatio;
	}
	/**
	 * @param quotaOverlookRatio ÆÄ¶ó¹ÌÅÍ¸¦ quotaOverlookRatio°ª¿¡ ¼³Á¤
	 */
	public void setQuotaOverlookRatio(String quotaOverlookRatio) {
		this.quotaOverlookRatio = quotaOverlookRatio;
	}
	/**
	 * @return quotaWarningMode °ª ¹ÝÈ¯
	 */
	public String getQuotaWarningMode() {
		return quotaWarningMode;
	}
	/**
	 * @param quotaWarningMode ÆÄ¶ó¹ÌÅÍ¸¦ quotaWarningMode°ª¿¡ ¼³Á¤
	 */
	public void setQuotaWarningMode(String quotaWarningMode) {
		this.quotaWarningMode = quotaWarningMode;
	}
	/**
	 * @return quotaWarningRatio °ª ¹ÝÈ¯
	 */
	public String getQuotaWarningRatio() {
		return quotaWarningRatio;
	}
	/**
	 * @param quotaWarningRatio ÆÄ¶ó¹ÌÅÍ¸¦ quotaWarningRatio°ª¿¡ ¼³Á¤
	 */
	public void setQuotaWarningRatio(String quotaWarningRatio) {
		this.quotaWarningRatio = quotaWarningRatio;
	}
	/**
	 * @return quotaViolationAction °ª ¹ÝÈ¯
	 */
	public String getQuotaViolationAction() {
		return quotaViolationAction;
	}
	/**
	 * @param quotaViolationAction ÆÄ¶ó¹ÌÅÍ¸¦ quotaViolationAction°ª¿¡ ¼³Á¤
	 */
	public void setQuotaViolationAction(String quotaViolationAction) {
		this.quotaViolationAction = quotaViolationAction;
	}
	/**
	 * @return mailServices °ª ¹ÝÈ¯
	 */
	public int getMailServices() {
		return mailServices;
	}
	/**
	 * @param mailServices ÆÄ¶ó¹ÌÅÍ¸¦ mailServices°ª¿¡ ¼³Á¤
	 */
	public void setMailServices(int mailServices) {
		this.mailServices = mailServices;
	}
	/**
	 * @return maxSendSize °ª ¹ÝÈ¯
	 */
	public String getMaxSendSize() {
		return maxSendSize;
	}
	/**
	 * @param maxSendSize ÆÄ¶ó¹ÌÅÍ¸¦ maxSendSize°ª¿¡ ¼³Á¤
	 */
	public void setMaxSendSize(String maxSendSize) {
		this.maxSendSize = maxSendSize;
	}
	/**
	 * @return inboxExpireDays °ª ¹ÝÈ¯
	 */
	public String getInboxExpireDays() {
		return inboxExpireDays;
	}
	/**
	 * @param inboxExpireDays ÆÄ¶ó¹ÌÅÍ¸¦ inboxExpireDays°ª¿¡ ¼³Á¤
	 */
	public void setInboxExpireDays(String inboxExpireDays) {
		this.inboxExpireDays = inboxExpireDays;
	}
	/**
	 * @return sentExpireDays °ª ¹ÝÈ¯
	 */
	public String getSentExpireDays() {
		return sentExpireDays;
	}
	/**
	 * @param sentExpireDays ÆÄ¶ó¹ÌÅÍ¸¦ sentExpireDays°ª¿¡ ¼³Á¤
	 */
	public void setSentExpireDays(String sentExpireDays) {
		this.sentExpireDays = sentExpireDays;
	}
	/**
	 * @return trashExpireDays °ª ¹ÝÈ¯
	 */
	public String getTrashExpireDays() {
		return trashExpireDays;
	}
	/**
	 * @param trashExpireDays ÆÄ¶ó¹ÌÅÍ¸¦ trashExpireDays°ª¿¡ ¼³Á¤
	 */
	public void setTrashExpireDays(String trashExpireDays) {
		this.trashExpireDays = trashExpireDays;
	}
	/**
	 * @return spamExpireDays °ª ¹ÝÈ¯
	 */
	public String getSpamExpireDays() {
		return spamExpireDays;
	}
	/**
	 * @param spamExpireDays ÆÄ¶ó¹ÌÅÍ¸¦ spamExpireDays°ª¿¡ ¼³Á¤
	 */
	public void setSpamExpireDays(String spamExpireDays) {
		this.spamExpireDays = spamExpireDays;
	}
	/**
	 * @return userExpireDays °ª ¹ÝÈ¯
	 */
	public String getUserExpireDays() {
		return userExpireDays;
	}
	/**
	 * @param userExpireDays ÆÄ¶ó¹ÌÅÍ¸¦ userExpireDays°ª¿¡ ¼³Á¤
	 */
	public void setUserExpireDays(String userExpireDays) {
		this.userExpireDays = userExpireDays;
	}
	/**
	 * @return description °ª ¹ÝÈ¯
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description ÆÄ¶ó¹ÌÅÍ¸¦ description°ª¿¡ ¼³Á¤
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
