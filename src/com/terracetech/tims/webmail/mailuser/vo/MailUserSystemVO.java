/**
 * MailUserSystemVO.java 2008  2008-09-26
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */

package com.terracetech.tims.webmail.mailuser.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <p>
 * <strong>MailUserSystemVO</strong> Å¬·¡½º ¸í¼¼
 * </p>
 * <ul>
 * <li>»ç¿ëÀÚ Á¤º¸¸¦ °¡Á®¿À´Â VO. »ç¿ëÀÚÀÇ ±âº»ÀûÀÎ ½Ã½ºÅÛ Á¤º¸¸¦ °¡Á®¿Â´Ù.</li>
 * <li>Key °¡µÇ´Â Á¤º¸´Â Sequence Á¤º¸·Î¼­ Domain, Group ÀÇ Sequence Á¤º¸µµ Æ÷ÇÔ.</li>
 * <li>»ç¿ëÀÚ¿¡°Ô °³°³º°·Î ¼³Á¤µÈ Quota, Forwarding,AutoReply,delivery µî¿¡°üÇÑ Á¤º¸ Æ÷ÇÔ.</li>
 * </ul>
 * 
 * @author sshyun
 * @since TIMS7
 * @version 7.0
 * 
 */
public class MailUserSystemVO {	
	
	/**
	 * <p>
	 * »ç¿ëÀÚ Sequence ¹øÈ£ Á¤º¸
	 * </p>
	 */
	private int seqNum = 0;
	/**
	 * <p>
	 * »ç¿ëÀÚ ID Á¤º¸
	 * </p>
	 */
	private String uid = null;
	/**
	 * <p>
	 * »ç¿ëÀÚ Domain Á¤º¸
	 * </p>
	 */
	private String domain = null;
	/**
	 * <p>
	 * »ç¿ëÀÚ Domain Sequence ¹øÈ£
	 * </p>
	 */
	private int domainSeqNum = 0;
	/**
	 * <p>
	 * »ç¿ëÀÚ Group Á¤º¸
	 * </p>
	 */
	private String group = null;
	/**
	 * <p>
	 * »ç¿ëÀÚ Group Sequence ¹øÈ£Á¤º¸
	 */
	private int groupSeqNum = 0;
	/**
	 * <p>
	 * »ç¿ëÀÚ Password Á¤º¸
	 * </p>
	 */
	private String password = null;
	/**
	 * <p>
	 * »ç¿ëÀÚ Mail HOST Á¤º¸. µî·ÏµÇ¾îÀÖ´Â ¸ÞÀÏ ¼­¹öÀÇ È£½ºÆ® Á¤º¸.
	 * </p>
	 */
	private String host = null;
	/**
	 * <p>
	 * »ç¿ëÀÚ Mail Store Á¤º¸. ¸ÞÀÏ ¼­¹ö¿¡ ÀúÀå µÇ´Â ¸ÞÀÏÀÇ ½ºÅä¸®Áö À§Ä¡ Á¤º¸
	 */
	private String store = null;	
	/**
	 * <p>»ç¿ëÀÚ °èÁ¤ÀÇ Expire ½Ã°£ (yyyyMMdd)</p>
	 */
	private String expireTime = null;
	/**
	 * <p>»ç¿ëÀÚ °èÁ¤ÀÇ »ç¿ë »óÅÂ.</p>
	 * <p>¼Ó¼º : enabled, disabled</p>
	 */
	private String status = null;
	/**
	 * <p>»ç¿ëÀÚ °èÁ¤ Á¾·ù.</p>
	 * <p>¼Ó¼º : mailuser</p>
	 */	
	private String userType = null;		
	/**
	 * <p>»ç¿ëÀÚ°¡ »ç¿ëÇÏ´Â ¸ÞÀÏ ¼­ºñ½º Á¾·ù. ¼ýÀÚ¸¦ ÀÌ¿ëÇÏ¿© ¼³Á¤. ¼³Á¤½Ã °¢ ¼­ºñ½º ¼ýÀÚ °ªÀ» ÇÕ»êÇÏ¿© Á¤º¸¸¦ ¼³Á¤</p>
	 * <p>¼Ó¼º : ums(1) fax(2) pop(4) imap(8) webmail(16) security(32) voice(64) virus(128) smtpauth(256)</p>
	 * 
	 */
	private int mailServices = 0;
	/**
	 * <p>»ç¿ëÀÚ Ãß°¡ ÄõÅÍÁ¤º¸. 'ÄõÅÍ¿ë·®S, Ä«¿îÅÍ¼öC' ÇüÅÂ·Î ÀúÀå.</p>
	 */
	private String addQuota = null;
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
	 * <p>ÈÞÁöÅë ¸ÞÀÏÇÔ À¯È¿±â°£ </p>
	 */
	private String trashExpireDays = null;
	/**
	 * <p>½ºÆÔ ¸ÞÀÏÇÔ À¯È¿±â°£ </p>
	 */
	private String spamExpireDays = null;
	/**
	 * <p>»ç¿ëÀÚ ¸ÞÀÏÇÔ À¯È¿±â°£ </p>
	 */
	private String userExpireDays = null;

	/**
	 * <p>Àü´Þ½Ã ¾Ë¸² Á¾·ù</p>
	 */
	private String deliveryNotiType = null;
	/**
	 * <p>Àü´Þ½Ã ¾Ë¸² ¸ðµå</p>
	 */
	private String deliveryNotiMode = null;

	/**
	 * <p>Àü´Þ ¼³Á¤</p>
	 */
	private String forwardingMode = null;
	/**
	 * <p>¼û±è Àü´Þ ¼³Á¤</p>
	 */
	private String hiddenForwardingMode = null;

	/**
	 * <p>ÀÚµ¿ÀÀ´ä ¼³Á¤</p>
	 */
	private String autoReplyMode = null;
	/**
	 * <p>ÀÚµ¿ ÀÀ´ä </p>
	 */
	private String autoReplyInclude = null;
	/**
	 * <p>ÀÚµ¿ÀÀ´ä ½ÃÀÛÇÒ ³¯Â¥</p>
	 */
	private String autoReplyStartTime = null;
	/**
	 * <p>ÀÚµ¿ÀÀ´ä Á¾·á ³¯Â¥</p>
	 */
	private String autoReplyEndTime = null;
	/**
	 * <p>ÀÚµ¿ÀÀ´ä ¹®±¸</p>
	 */
	private String autoReplyText = null;
	/**
	 * <p>ÀÚµ¿ÀÀ´ä Á¦¸ñ</p>
	 */
	private String autoReplySubject = null;


    
	/**
	 * @return seqNum °ª ¹ÝÈ¯
	 */
	public int getSeqNum() {
		return seqNum;
	}



	/**
	 * @param seqNum ÆÄ¶ó¹ÌÅÍ¸¦ seqNum°ª¿¡ ¼³Á¤
	 */
	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}



	/**
	 * @return uid °ª ¹ÝÈ¯
	 */
	public String getUid() {
		return uid;
	}



	/**
	 * @param uid ÆÄ¶ó¹ÌÅÍ¸¦ uid°ª¿¡ ¼³Á¤
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}



	/**
	 * @return domain °ª ¹ÝÈ¯
	 */
	public String getDomain() {
		return domain;
	}



	/**
	 * @param domain ÆÄ¶ó¹ÌÅÍ¸¦ domain°ª¿¡ ¼³Á¤
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}



	/**
	 * @return domainSeqNum °ª ¹ÝÈ¯
	 */
	public int getDomainSeqNum() {
		return domainSeqNum;
	}



	/**
	 * @param domainSeqNum ÆÄ¶ó¹ÌÅÍ¸¦ domainSeqNum°ª¿¡ ¼³Á¤
	 */
	public void setDomainSeqNum(int domainSeqNum) {
		this.domainSeqNum = domainSeqNum;
	}



	/**
	 * @return group °ª ¹ÝÈ¯
	 */
	public String getGroup() {
		return group;
	}



	/**
	 * @param group ÆÄ¶ó¹ÌÅÍ¸¦ group°ª¿¡ ¼³Á¤
	 */
	public void setGroup(String group) {
		this.group = group;
	}



	/**
	 * @return groupSeqNum °ª ¹ÝÈ¯
	 */
	public int getGroupSeqNum() {
		return groupSeqNum;
	}



	/**
	 * @param groupSeqNum ÆÄ¶ó¹ÌÅÍ¸¦ groupSeqNum°ª¿¡ ¼³Á¤
	 */
	public void setGroupSeqNum(int groupSeqNum) {
		this.groupSeqNum = groupSeqNum;
	}



	/**
	 * @return password °ª ¹ÝÈ¯
	 */
	public String getPassword() {
		return password;
	}



	/**
	 * @param password ÆÄ¶ó¹ÌÅÍ¸¦ password°ª¿¡ ¼³Á¤
	 */
	public void setPassword(String password) {
		this.password = password;
	}



	/**
	 * @return host °ª ¹ÝÈ¯
	 */
	public String getHost() {
		return host;
	}



	/**
	 * @param host ÆÄ¶ó¹ÌÅÍ¸¦ host°ª¿¡ ¼³Á¤
	 */
	public void setHost(String host) {
		this.host = host;
	}



	/**
	 * @return store °ª ¹ÝÈ¯
	 */
	public String getStore() {
		return store;
	}



	/**
	 * @param store ÆÄ¶ó¹ÌÅÍ¸¦ store°ª¿¡ ¼³Á¤
	 */
	public void setStore(String store) {
		this.store = store;
	}



	/**
	 * @return expireTime °ª ¹ÝÈ¯
	 */
	public String getExpireTime() {
		return expireTime;
	}



	/**
	 * @param expireTime ÆÄ¶ó¹ÌÅÍ¸¦ expireTime°ª¿¡ ¼³Á¤
	 */
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}



	/**
	 * @return status °ª ¹ÝÈ¯
	 */
	public String getStatus() {
		return status;
	}



	/**
	 * @param status ÆÄ¶ó¹ÌÅÍ¸¦ status°ª¿¡ ¼³Á¤
	 */
	public void setStatus(String status) {
		this.status = status;
	}



	/**
	 * @return userType °ª ¹ÝÈ¯
	 */
	public String getUserType() {
		return userType;
	}



	/**
	 * @param userType ÆÄ¶ó¹ÌÅÍ¸¦ userType°ª¿¡ ¼³Á¤
	 */
	public void setUserType(String userType) {
		this.userType = userType;
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
	 * @return addQuota °ª ¹ÝÈ¯
	 */
	public String getAddQuota() {
		return addQuota;
	}



	/**
	 * @param addQuota ÆÄ¶ó¹ÌÅÍ¸¦ addQuota°ª¿¡ ¼³Á¤
	 */
	public void setAddQuota(String addQuota) {
		this.addQuota = addQuota;
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
	 * @return deliveryNotiType °ª ¹ÝÈ¯
	 */
	public String getDeliveryNotiType() {
		return deliveryNotiType;
	}



	/**
	 * @param deliveryNotiType ÆÄ¶ó¹ÌÅÍ¸¦ deliveryNotiType°ª¿¡ ¼³Á¤
	 */
	public void setDeliveryNotiType(String deliveryNotiType) {
		this.deliveryNotiType = deliveryNotiType;
	}



	/**
	 * @return deliveryNotiMode °ª ¹ÝÈ¯
	 */
	public String getDeliveryNotiMode() {
		return deliveryNotiMode;
	}



	/**
	 * @param deliveryNotiMode ÆÄ¶ó¹ÌÅÍ¸¦ deliveryNotiMode°ª¿¡ ¼³Á¤
	 */
	public void setDeliveryNotiMode(String deliveryNotiMode) {
		this.deliveryNotiMode = deliveryNotiMode;
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
	 * @return hiddenForwardingMode °ª ¹ÝÈ¯
	 */
	public String getHiddenForwardingMode() {
		return hiddenForwardingMode;
	}



	/**
	 * @param hiddenForwardingMode ÆÄ¶ó¹ÌÅÍ¸¦ hiddenForwardingMode°ª¿¡ ¼³Á¤
	 */
	public void setHiddenForwardingMode(String hiddenForwardingMode) {
		this.hiddenForwardingMode = hiddenForwardingMode;
	}



	/**
	 * @return autoReplyMode °ª ¹ÝÈ¯
	 */
	public String getAutoReplyMode() {
		return autoReplyMode;
	}



	/**
	 * @param autoReplyMode ÆÄ¶ó¹ÌÅÍ¸¦ autoReplyMode°ª¿¡ ¼³Á¤
	 */
	public void setAutoReplyMode(String autoReplyMode) {
		this.autoReplyMode = autoReplyMode;
	}



	/**
	 * @return autoReplyInclude °ª ¹ÝÈ¯
	 */
	public String getAutoReplyInclude() {
		return autoReplyInclude;
	}



	/**
	 * @param autoReplyInclude ÆÄ¶ó¹ÌÅÍ¸¦ autoReplyInclude°ª¿¡ ¼³Á¤
	 */
	public void setAutoReplyInclude(String autoReplyInclude) {
		this.autoReplyInclude = autoReplyInclude;
	}



	/**
	 * @return autoReplyStartTime °ª ¹ÝÈ¯
	 */
	public String getAutoReplyStartTime() {
		return autoReplyStartTime;
	}



	/**
	 * @param autoReplyStartTime ÆÄ¶ó¹ÌÅÍ¸¦ autoReplyStartTime°ª¿¡ ¼³Á¤
	 */
	public void setAutoReplyStartTime(String autoReplyStartTime) {
		this.autoReplyStartTime = autoReplyStartTime;
	}



	/**
	 * @return autoReplyEndTime °ª ¹ÝÈ¯
	 */
	public String getAutoReplyEndTime() {
		return autoReplyEndTime;
	}



	/**
	 * @param autoReplyEndTime ÆÄ¶ó¹ÌÅÍ¸¦ autoReplyEndTime°ª¿¡ ¼³Á¤
	 */
	public void setAutoReplyEndTime(String autoReplyEndTime) {
		this.autoReplyEndTime = autoReplyEndTime;
	}



	/**
	 * @return autoReplyText °ª ¹ÝÈ¯
	 */
	public String getAutoReplyText() {		
		return autoReplyText;
	}



	/**
	 * @param autoReplyText ÆÄ¶ó¹ÌÅÍ¸¦ autoReplyText°ª¿¡ ¼³Á¤
	 */
	public void setAutoReplyText(String autoReplyText) {
		this.autoReplyText = autoReplyText;
	}


	
	/**
	 * <p>»ç¿ëÀÚ ±âº»Á¤º¸¸¦ Map ÇüÅÂ·Î »ý¼ºÇÏ¿© ¹ÝÈ¯.</p>
	 * <p>
	 * Map Æ÷ÇÔ Á¤º¸ : id, idseq, domain, domainseq, group, groupseq, email, password, host, store,expiretime, accountstatus, usertype, maxsendsize
	 * 
	 * @return Map<String,String>
	 */
	public Map<String, String> getMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", uid);
		map.put("idseq", Integer.toString(seqNum));
		map.put("domain", domain);
		map.put("domainseq", Integer.toString(domainSeqNum));
		map.put("group", group);
		map.put("groupseq", Integer.toString(groupSeqNum));
		map.put("email", uid + "@" + domain);
		map.put("password", password);
		map.put("host", host);
		map.put("store", store);
		map.put("expiretime", expireTime);
		map.put("accountstatus", status);
		map.put("usertype", userType);
		map.put("maxsendsize", maxSendSize);
		return map;
	}

	public String getAutoReplySubject() {
		return autoReplySubject;
	}

	public void setAutoReplySubject(String autoReplySubject) {
		this.autoReplySubject = autoReplySubject;
	}
}
