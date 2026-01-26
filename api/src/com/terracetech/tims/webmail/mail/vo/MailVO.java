package com.terracetech.tims.webmail.mail.vo;

/**
 * Mobile용 메일 VO
 */
public class MailVO {
    
    private String uid;
    private String folder;
    private String subject;
    private String from;
    private String to;
    private String date;
    private int size;
    private boolean hasAttachment;
    private boolean isRead;
    private boolean isFlagged;
    
    // Getters and Setters
    public String getUid() {
        return uid;
    }
    
    public void setUid(String uid) {
        this.uid = uid;
    }
    
    public String getFolder() {
        return folder;
    }
    
    public void setFolder(String folder) {
        this.folder = folder;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getFrom() {
        return from;
    }
    
    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getTo() {
        return to;
    }
    
    public void setTo(String to) {
        this.to = to;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public boolean isHasAttachment() {
        return hasAttachment;
    }
    
    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
    
    public boolean isFlagged() {
        return isFlagged;
    }
    
    public void setFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }
}

