package com.terracetech.tims.webmail.scheduler.vo;

public class SchedulerShareTargetVO {

    private int shareSeq = 0;

    private String targetType = null;

    private String targetValue = null;

    public int getShareSeq() {
        return shareSeq;
    }

    public void setShareSeq(int shareSeq) {
        this.shareSeq = shareSeq;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

}
