/**
 * MailDomainGroup.java 2008. 12. 9.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.vo;

/**
 * <p><strong>MailDomainGroup.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class MailDomainGroupVO {
	private int 
		mailDomainSeq, mailGroupSeq, mailServices;
	
	private String 
		mailGroup, mailGroupName, mailQuota, webFolderQuota, 
		maxForwarding, quotaOverlookRatio, quotaWarningMode, 
		quotaWarningRatio, quotaViolationAction, mailMaxSendSize, inboxExpireDays, 
		sentExpireDays, trashExpireDays, userExpireDays, spamExpireDays, 
		bigAttachQuota, deliveryMode, description;
	
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
	public String getMailGroup() {
		return mailGroup;
	}
	public void setMailGroup(String mailGroup) {
		this.mailGroup = mailGroup;
	}
	public String getMailGroupName() {
		return mailGroupName;
	}
	public void setMailGroupName(String mailGroupName) {
		this.mailGroupName = mailGroupName;
	}
	public String getMailQuota() {
		return mailQuota;
	}
	public void setMailQuota(String mailQuota) {
		this.mailQuota = mailQuota;
	}
	public String getWebFolderQuota() {
		return webFolderQuota;
	}
	public void setWebFolderQuota(String webFolderQuota) {
		this.webFolderQuota = webFolderQuota;
	}
	public String getMaxForwarding() {
		return maxForwarding;
	}
	public void setMaxForwarding(String maxForwarding) {
		this.maxForwarding = maxForwarding;
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
	public String getUserExpireDays() {
		return userExpireDays;
	}
	public void setUserExpireDays(String userExpireDays) {
		this.userExpireDays = userExpireDays;
	}
	public String getSpamExpireDays() {
		return spamExpireDays;
	}
	public void setSpamExpireDays(String spamExpireDays) {
		this.spamExpireDays = spamExpireDays;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBigAttachQuota() {
		return bigAttachQuota;
	}
	public void setBigAttachQuota(String bigAttachQuota) {
		this.bigAttachQuota = bigAttachQuota;
	}
	public String getDeliveryMode() {
		return deliveryMode;
	}
	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}
}
