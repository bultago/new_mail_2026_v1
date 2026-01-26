package com.terracetech.tims.webmail.note.vo;

/**
 * Mobile용 쪽지 VO
 */
public class NoteVO {
    
    private int noteSeq;
    private String noteTitle;
    private String noteContent;
    private String senderName;
    private String senderUid;
    private String sendDate;
    private boolean isRead;
    
    // Getters and Setters
    public int getNoteSeq() {
        return noteSeq;
    }
    
    public void setNoteSeq(int noteSeq) {
        this.noteSeq = noteSeq;
    }
    
    public String getNoteTitle() {
        return noteTitle;
    }
    
    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }
    
    public String getNoteContent() {
        return noteContent;
    }
    
    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }
    
    public String getSenderName() {
        return senderName;
    }
    
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    
    public String getSenderUid() {
        return senderUid;
    }
    
    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }
    
    public String getSendDate() {
        return sendDate;
    }
    
    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
}

