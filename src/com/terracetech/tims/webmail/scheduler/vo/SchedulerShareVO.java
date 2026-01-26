package com.terracetech.tims.webmail.scheduler.vo;

public class SchedulerShareVO {

    private int shareSeq = 0;

    private int mailUserSeq = 0;

    private String shareName = null;

    private String shareColor = null;

    private String userName = null;

    public int getShareSeq() {
        return shareSeq;
    }

    public void setShareSeq(int shareSeq) {
        this.shareSeq = shareSeq;
    }

    public int getMailUserSeq() {
        return mailUserSeq;
    }

    public void setMailUserSeq(int mailUserSeq) {
        this.mailUserSeq = mailUserSeq;
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    public String getShareColor() {
        return shareColor;
    }

    public void setShareColor(String shareColor) {
        this.shareColor = shareColor;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
