package com.terracetech.tims.webmail.scheduler.vo;

public class SchedulerAssetVO {

    private int categorySeq = 0;
    private int assetSeq = 0;
    private String assetName = null;
    private String assetType = null;
    private String assetStatus = null;

    public int getCategorySeq() {
        return categorySeq;
    }

    public void setCategorySeq(int categorySeq) {
        this.categorySeq = categorySeq;
    }

    public int getAssetSeq() {
        return assetSeq;
    }

    public void setAssetSeq(int assetSeq) {
        this.assetSeq = assetSeq;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(String assetStatus) {
        this.assetStatus = assetStatus;
    }
}
