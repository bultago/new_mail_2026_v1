package com.terracetech.tims.webmail.webfolder.vo;

/**
 * 웹폴더 파일 VO
 */
public class FileVO {
    
    private int fileSeq;
    private int folderSeq;
    private int userSeq;
    private String fileName;
    private String filePath;
    private long fileSize;
    private String fileType;
    private String uploadDate;
    private String updateDate;
    
    // Getters and Setters
    public int getFileSeq() {
        return fileSeq;
    }
    
    public void setFileSeq(int fileSeq) {
        this.fileSeq = fileSeq;
    }
    
    public int getFolderSeq() {
        return folderSeq;
    }
    
    public void setFolderSeq(int folderSeq) {
        this.folderSeq = folderSeq;
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
    
    public String getUpdateDate() {
        return updateDate;
    }
    
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}


