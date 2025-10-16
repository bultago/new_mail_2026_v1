/**
 * MailUserVO.java 2008. 12. 4.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.vo;

import java.io.Serializable;

/**
 * <p><strong>MailUserVO.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class MailUserVO implements Serializable{
	private int mailUserSeq, mailDomainSeq, mailGroupSeq, mailServices;
	private String mailUid, mailPassword, mailHost, messageStore, accountExpireDate, accountStatus, userType, mailAddQuota, quotaOverlookRatio, quotaWarningMode, quotaWarningRatio, quotaViolationAction, mailMaxSendSize, inboxExpireDays, sentExpireDays, trashExpireDays, spamExpireDays, userExpireDays, deliveryNotiType, deliveryNotiMode, forwardingMode, hiddenForwardingMode, autoReplyMode, autoReplyInclude, autoReplyStartTime, autoReplyEndTime, autoReplyText, autoReplySubject;
	
	public int getMailUserSeq() {
		return mailUserSeq;
	}
	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}
	public int getMailDomainSeq() {
		return mailDomainSeq;
	}
	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}
	public int getMailGroupSeq() {
		return mailGroupSeq;
	}
	public void setMailGroupSeq(int mailGroupSeq) {
		this.mailGroupSeq = mailGroupSeq;
	}
	public int getMailServices() {
		return mailServices;
	}
	public void setMailServices(int mailServices) {
		this.mailServices = mailServices;
	}
	public String getMailUid() {
		return mailUid;
	}
	public void setMailUid(String mailUid) {
		this.mailUid = mailUid;
	}
	public String getMailPassword() {
		return mailPassword;
	}
	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}
	public String getMailHost() {
		return mailHost;
	}
	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}
	public String getMessageStore() {
		return messageStore;
	}
	public void setMessageStore(String messageStore) {
		this.messageStore = messageStore;
	}
	public String getAccountExpireDate() {
		return accountExpireDate;
	}
	public void setAccountExpireDate(String accountExpireDate) {
		this.accountExpireDate = accountExpireDate;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getMailAddQuota() {
		return mailAddQuota;
	}
	public void setMailAddQuota(String mailAddQuota) {
		this.mailAddQuota = mailAddQuota;
	}
	public String getQuotaOverlookRatio() {
		return quotaOverlookRatio;
	}
	public void setQuotaOverlookRatio(String quotaOverlookRatio) {
		this.quotaOverlookRatio = quotaOverlookRatio;
	}
	public String getQuotaWarningMode() {
		return quotaWarningMode;
	}
	public void setQuotaWarningMode(String quotaWarningMode) {
		this.quotaWarningMode = quotaWarningMode;
	}
	public String getQuotaWarningRatio() {
		return quotaWarningRatio;
	}
	public void setQuotaWarningRatio(String quotaWarningRatio) {
		this.quotaWarningRatio = quotaWarningRatio;
	}
	public String getQuotaViolationAction() {
		return quotaViolationAction;
	}
	public void setQuotaViolationAction(String quotaViolationAction) {
		this.quotaViolationAction = quotaViolationAction;
	}
	public String getMailMaxSendSize() {
		return mailMaxSendSize;
	}
	public void setMailMaxSendSize(String mailMaxSendSize) {
		this.mailMaxSendSize = mailMaxSendSize;
	}
	public String getInboxExpireDays() {
		return inboxExpireDays;
	}
	public void setInboxExpireDays(String inboxExpireDays) {
		this.inboxExpireDays = inboxExpireDays;
	}
	public String getSentExpireDays() {
		return sentExpireDays;
	}
	public void setSentExpireDays(String sentExpireDays) {
		this.sentExpireDays = sentExpireDays;
	}
	public String getTrashExpireDays() {
		return trashExpireDays;
	}
	public void setTrashExpireDays(String trashExpireDays) {
		this.trashExpireDays = trashExpireDays;
	}
	public String getSpamExpireDays() {
		return spamExpireDays;
	}
	public void setSpamExpireDays(String spamExpireDays) {
		this.spamExpireDays = spamExpireDays;
	}
	public String getUserExpireDays() {
		return userExpireDays;
	}
	public void setUserExpireDays(String userExpireDays) {
		this.userExpireDays = userExpireDays;
	}
	public String getDeliveryNotiType() {
		return deliveryNotiType;
	}
	public void setDeliveryNotiType(String deliveryNotiType) {
		this.deliveryNotiType = deliveryNotiType;
	}
	public String getDeliveryNotiMode() {
		return deliveryNotiMode;
	}
	public void setDeliveryNotiMode(String deliveryNotiMode) {
		this.deliveryNotiMode = deliveryNotiMode;
	}
	public String getForwardingMode() {
		return forwardingMode;
	}
	public void setForwardingMode(String forwardingMode) {
		this.forwardingMode = forwardingMode;
	}
	public String getHiddenForwardingMode() {
		return hiddenForwardingMode;
	}
	public void setHiddenForwardingMode(String hiddenForwardingMode) {
		this.hiddenForwardingMode = hiddenForwardingMode;
	}
	public String getAutoReplyMode() {
		return autoReplyMode;
	}
	public void setAutoReplyMode(String autoReplyMode) {
		this.autoReplyMode = autoReplyMode;
	}
	public String getAutoReplyInclude() {
		return autoReplyInclude;
	}
	public void setAutoReplyInclude(String autoReplyInclude) {
		this.autoReplyInclude = autoReplyInclude;
	}
	public String getAutoReplyStartTime() {
		return autoReplyStartTime;
	}
	public void setAutoReplyStartTime(String autoReplyStartTime) {
		this.autoReplyStartTime = autoReplyStartTime;
	}
	public String getAutoReplyEndTime() {
		return autoReplyEndTime;
	}
	public void setAutoReplyEndTime(String autoReplyEndTime) {
		this.autoReplyEndTime = autoReplyEndTime;
	}
	public String getAutoReplyText() {
		return autoReplyText;
	}
	public void setAutoReplyText(String autoReplyText) {
		this.autoReplyText = autoReplyText;
	}
	public String getAutoReplySubject() {
		return autoReplySubject;
	}
	public void setAutoReplySubject(String autoReplySubject) {
		this.autoReplySubject = autoReplySubject;
	}
}
