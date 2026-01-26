package com.terracetech.tims.webmail.scheduler.vo;

public class SchedulerShareExtVO {

    private int schedulerId = 0;

    private int mailUserSeq = 0;

    private String userName = null;

    private String email = null;

    public int getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(int schedulerId) {
        this.schedulerId = schedulerId;
    }

    public int getMailUserSeq() {
        return mailUserSeq;
    }

    public void setMailUserSeq(int mailUserSeq) {
        this.mailUserSeq = mailUserSeq;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
