package com.terracetech.tims.webmail.setting.vo;

/**
 * 파일 VO
 */
public class FileVO {
    
    private int fileSeq;
    private String fileName;
    private String filePath;
    private long fileSize;
    private String fileType;
    private String uploadDate;
    
    // Getters and Setters
    public int getFileSeq() {
        return fileSeq;
    }
    
    public void setFileSeq(int fileSeq) {
        this.fileSeq = fileSeq;
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
    
    public String getFileType() {
        return fileType;
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public String getUploadDate() {
        return uploadDate;
    }
    
    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
}


