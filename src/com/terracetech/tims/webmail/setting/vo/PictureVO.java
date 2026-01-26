package com.terracetech.tims.webmail.setting.vo;

/**
 * 그림 파일 VO
 */
public class PictureVO {
    
    private int pictureSeq;
    private int userSeq;
    private String fileName;
    private String filePath;
    private long fileSize;
    private String uploadDate;
    
    // Getters and Setters
    public int getPictureSeq() {
        return pictureSeq;
    }
    
    public void setPictureSeq(int pictureSeq) {
        this.pictureSeq = pictureSeq;
    }
    
    public int getUserSeq() {
        return userSeq;
    }
    
    public void setUserSeq(int userSeq) {
        this.userSeq = userSeq;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getUploadDate() {
        return uploadDate;
    }
    
    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
}


