package com.terracetech.tims.webmail.setting.vo;

/**
 * 외부 메일 설정 VO
 */
public class ExtMailVO {
    
    private int extMailSeq;
    private String extMailAddress;
    private String extMailServer;
    private int extMailPort;
    private String extMailProtocol;
    private boolean useSSL;
    private String username;
    private String password;
    
    // Getters and Setters
    public int getExtMailSeq() {
        return extMailSeq;
    }
    
    public void setExtMailSeq(int extMailSeq) {
        this.extMailSeq = extMailSeq;
    }
    
    public String getExtMailAddress() {
        return extMailAddress;
    }
    
    public void setExtMailAddress(String extMailAddress) {
        this.extMailAddress = extMailAddress;
    }
    
    public String getExtMailServer() {
        return extMailServer;
    }
    
    public void setExtMailServer(String extMailServer) {
        this.extMailServer = extMailServer;
    }
    
    public int getExtMailPort() {
        return extMailPort;
    }
    
    public void setExtMailPort(int extMailPort) {
        this.extMailPort = extMailPort;
    }
    
    public String getExtMailProtocol() {
        return extMailProtocol;
    }
    
    public void setExtMailProtocol(String extMailProtocol) {
        this.extMailProtocol = extMailProtocol;
    }
    
    public boolean isUseSSL() {
        return useSSL;
    }
    
    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}

