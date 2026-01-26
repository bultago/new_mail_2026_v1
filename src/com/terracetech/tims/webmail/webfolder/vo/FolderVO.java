package com.terracetech.tims.webmail.webfolder.vo;

/**
 * 웹폴더 VO
 */
public class FolderVO {
    
    private int folderSeq;
    private int parentFolderSeq;
    private int userSeq;
    private String folderName;
    private String folderPath;
    private int folderLevel;
    private long folderSize;
    private String createDate;
    private String updateDate;
    
    // Getters and Setters
    public int getFolderSeq() {
        return folderSeq;
    }
    
    public void setFolderSeq(int folderSeq) {
        this.folderSeq = folderSeq;
    }
    
    public int getParentFolderSeq() {
        return parentFolderSeq;
    }
    
    public void setParentFolderSeq(int parentFolderSeq) {
        this.parentFolderSeq = parentFolderSeq;
    }
    
    public int getUserSeq() {
        return userSeq;
    }
    
    public void setUserSeq(int userSeq) {
        this.userSeq = userSeq;
    }
    
    public String getFolderName() {
        return folderName;
    }
    
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
    
    public String getFolderPath() {
        return folderPath;
    }
    
    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }
    
    public int getFolderLevel() {
        return folderLevel;
    }
    
    public void setFolderLevel(int folderLevel) {
        this.folderLevel = folderLevel;
    }
    
    public long getFolderSize() {
        return folderSize;
    }
    
    public void setFolderSize(long folderSize) {
        this.folderSize = folderSize;
    }
    
    public String getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    
    public String getUpdateDate() {
        return updateDate;
    }
    
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}


