package com.terracetech.tims.webmail.mobile.vo;

public class MobileSyncVO {
    
    private int mailUserSeq;

    private String deviceId;
    
    private String deviceType;
    
    private String pingCheck;

    private String syncKey;

    private String pingUpdateTime;
    
    private String emailUpdateTime;
    
    private String contactsInsertSyncTime;
    
    private String contactsUpdateSyncTime;
    
    private String contactsDeleteSyncTime;
    
    private String calendarInsertSyncTime;
    
    private String calendarUpdateSyncTime;
    
    private String calendarDeleteSyncTime;
    
	public int getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}


	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getSyncKey() {
		return syncKey;
	}

	public void setSyncKey(String syncKey) {
		this.syncKey = syncKey;
	}

	public String getPingUpdateTime() {
		return pingUpdateTime;
	}

	public void setPingUpdateTime(String pingUpdateTime) {
		this.pingUpdateTime = pingUpdateTime;
	}

	public String getEmailUpdateTime() {
		return emailUpdateTime;
	}

	public void setEmailUpdateTime(String emailUpdateTime) {
		this.emailUpdateTime = emailUpdateTime;
	}

	public String getContactsInsertSyncTime() {
		return contactsInsertSyncTime;
	}

	public void setContactsInsertSyncTime(String contactsInsertSyncTime) {
		this.contactsInsertSyncTime = contactsInsertSyncTime;
	}

	public String getContactsUpdateSyncTime() {
		return contactsUpdateSyncTime;
	}

	public void setContactsUpdateSyncTime(String contactsUpdateSyncTime) {
		this.contactsUpdateSyncTime = contactsUpdateSyncTime;
	}

	public String getContactsDeleteSyncTime() {
		return contactsDeleteSyncTime;
	}

	public void setContactsDeleteSyncTime(String contactsDeleteSyncTime) {
		this.contactsDeleteSyncTime = contactsDeleteSyncTime;
	}

	public String getCalendarInsertSyncTime() {
		return calendarInsertSyncTime;
	}

	public void setCalendarInsertSyncTime(String calendarInsertSyncTime) {
		this.calendarInsertSyncTime = calendarInsertSyncTime;
	}

	public String getCalendarUpdateSyncTime() {
		return calendarUpdateSyncTime;
	}

	public void setCalendarUpdateSyncTime(String calendarUpdateSyncTime) {
		this.calendarUpdateSyncTime = calendarUpdateSyncTime;
	}

	public String getCalendarDeleteSyncTime() {
		return calendarDeleteSyncTime;
	}

	public void setCalendarDeleteSyncTime(String calendarDeleteSyncTime) {
		this.calendarDeleteSyncTime = calendarDeleteSyncTime;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getPingCheck() {
		return pingCheck;
	}

	public void setPingCheck(String pingCheck) {
		this.pingCheck = pingCheck;
	}
}