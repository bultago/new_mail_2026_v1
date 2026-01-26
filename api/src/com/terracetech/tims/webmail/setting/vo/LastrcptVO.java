package com.terracetech.tims.webmail.setting.vo;

/**
 * 최근 수신자 VO
 */
public class LastrcptVO {
    
    private int rcptSeq;
    private int userSeq;
    private String email;
    private String name;
    private String regDate;
    
    // Getters and Setters
    public int getRcptSeq() {
        return rcptSeq;
    }
    
    public void setRcptSeq(int rcptSeq) {
        this.rcptSeq = rcptSeq;
    }
    
    public int getUserSeq() {
        return userSeq;
    }
    
    public void setUserSeq(int userSeq) {
        this.userSeq = userSeq;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRegDate() {
        return regDate;
    }
    
    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}


