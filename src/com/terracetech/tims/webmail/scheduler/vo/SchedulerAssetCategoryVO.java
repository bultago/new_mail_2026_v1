package com.terracetech.tims.webmail.scheduler.vo;

import java.util.List;

public class SchedulerAssetCategoryVO {

    private int categorySeq = 0;
    private String categoryName = null;
    private String categoryDescription = null;

    private List<SchedulerAssetCategoryImageVO> categoryImageList = null;
    private List<SchedulerAssetVO> assetList = null;

    public int getCategorySeq() {
        return categorySeq;
    }

    public void setCategorySeq(int categorySeq) {
        this.categorySeq = categorySeq;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public List<SchedulerAssetCategoryImageVO> getCategoryImageList() {
        return categoryImageList;
    }

    public void setCategoryImageList(List<SchedulerAssetCategoryImageVO> categoryImageList) {
        this.categoryImageList = categoryImageList;
    }

    public List<SchedulerAssetVO> getAssetList() {
        return assetList;
    }

    public void setAssetList(List<SchedulerAssetVO> assetList) {
        this.assetList = assetList;
    }

}
