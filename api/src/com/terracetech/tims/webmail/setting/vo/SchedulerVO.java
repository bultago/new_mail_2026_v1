package com.terracetech.tims.webmail.setting.vo;

/**
 * 설정 > 일정 동기화 VO
 */
public class SchedulerVO {
    
    private int userSeq;
    private String useSchedulerSync;
    private String syncUrl;
    private String syncId;
    private String syncPassword;
    private String lastSyncTime;
    
    // Getters and Setters
    public int getUserSeq() {
        return userSeq;
    }
    
    public void setUserSeq(int userSeq) {
        this.userSeq = userSeq;
    }
    
    public String getUseSchedulerSync() {
        return useSchedulerSync;
    }
    
    public void setUseSchedulerSync(String useSchedulerSync) {
        this.useSchedulerSync = useSchedulerSync;
    }
    
    public String getSyncUrl() {
        return syncUrl;
    }
    
    public void setSyncUrl(String syncUrl) {
        this.syncUrl = syncUrl;
    }
    
    public String getSyncId() {
        return syncId;
    }
    
    public void setSyncId(String syncId) {
        this.syncId = syncId;
    }
    
    public String getSyncPassword() {
        return syncPassword;
    }
    
    public void setSyncPassword(String syncPassword) {
        this.syncPassword = syncPassword;
    }
    
    public String getLastSyncTime() {
        return lastSyncTime;
    }
    
    public void setLastSyncTime(String lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }
}


