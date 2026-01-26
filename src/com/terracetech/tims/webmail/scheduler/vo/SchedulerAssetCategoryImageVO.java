package com.terracetech.tims.webmail.scheduler.vo;

public class SchedulerAssetCategoryImageVO {

    private int categorySeq = 0;
    private String imageCid = null;
    private byte[] imageData = null;

    public int getCategorySeq() {
        return categorySeq;
    }

    public void setCategorySeq(int categorySeq) {
        this.categorySeq = categorySeq;
    }

    public String getImageCid() {
        return imageCid;
    }

    public void setImageCid(String imageCid) {
        this.imageCid = imageCid;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
