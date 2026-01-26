package com.terracetech.tims.webmail.mobile.vo;

/**
 * Mobile 동기화 로그 VO
 */
public class MobileSyncLogVO {
    
    private int logSeq;
    private int mailUserSeq;
    private String syncKey;
    private String syncType;
    private String syncTime;
    private String eventType;
    private String eventData;
    
    // Getters and Setters
    public int getLogSeq() {
        return logSeq;
    }
    
    public void setLogSeq(int logSeq) {
        this.logSeq = logSeq;
    }
    
    public int getMailUserSeq() {
        return mailUserSeq;
    }
    
    public void setMailUserSeq(int mailUserSeq) {
        this.mailUserSeq = mailUserSeq;
    }
    
    public String getSyncKey() {
        return syncKey;
    }
    
    public void setSyncKey(String syncKey) {
        this.syncKey = syncKey;
    }
    
    public String getSyncType() {
        return syncType;
    }
    
    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }
    
    public String getSyncTime() {
        return syncTime;
    }
    
    public void setSyncTime(String syncTime) {
        this.syncTime = syncTime;
    }
    
    public String getEventType() {
        return eventType;
    }
    
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    
    public String getEventData() {
        return eventData;
    }
    
    public void setEventData(String eventData) {
        this.eventData = eventData;
    }
}
