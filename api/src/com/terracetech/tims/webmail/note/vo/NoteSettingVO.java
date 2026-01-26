package com.terracetech.tims.webmail.note.vo;

/**
 * 쪽지 설정 VO
 */
public class NoteSettingVO {
    
    private int noteUserSeq;
    private String useNote;
    private String autoDelete;
    private int autoDeleteDays;
    private String receiveNotify;
    
    // Getters and Setters
    public int getNoteUserSeq() {
        return noteUserSeq;
    }
    
    public void setNoteUserSeq(int noteUserSeq) {
        this.noteUserSeq = noteUserSeq;
    }
    
    public String getUseNote() {
        return useNote;
    }
    
    public void setUseNote(String useNote) {
        this.useNote = useNote;
    }
    
    public String getAutoDelete() {
        return autoDelete;
    }
    
    public void setAutoDelete(String autoDelete) {
        this.autoDelete = autoDelete;
    }
    
    public int getAutoDeleteDays() {
        return autoDeleteDays;
    }
    
    public void setAutoDeleteDays(int autoDeleteDays) {
        this.autoDeleteDays = autoDeleteDays;
    }
    
    public String getReceiveNotify() {
        return receiveNotify;
    }
    
    public void setReceiveNotify(String receiveNotify) {
        this.receiveNotify = receiveNotify;
    }
}


