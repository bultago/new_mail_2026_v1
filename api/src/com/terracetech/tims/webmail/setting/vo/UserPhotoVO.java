package com.terracetech.tims.webmail.setting.vo;

/**
 * 사용자 사진 VO
 */
public class UserPhotoVO {
    
    private int userSeq;
    private byte[] photoData;
    private String photoType;
    private String uploadDate;
    
    // Getters and Setters
    public int getUserSeq() {
        return userSeq;
    }
    
    public void setUserSeq(int userSeq) {
        this.userSeq = userSeq;
    }
    
    public byte[] getPhotoData() {
        return photoData;
    }
    
    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }
    
    public String getPhotoType() {
        return photoType;
    }
    
    public void setPhotoType(String photoType) {
        this.photoType = photoType;
    }
    
    public String getUploadDate() {
        return uploadDate;
    }
    
    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
}
