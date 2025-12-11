package com.terracetech.tims.webmail.setting.vo;

/**
 * PKI 서명 VO
 */
public class PKISignVO {
    
    private int userSeq;
    private String usePKISign;
    private String pkiCertificate;
    private String pkiPrivateKey;
    private String pkiAlgorithm;
    private String createDate;
    private String updateDate;
    
    // Getters and Setters
    public int getUserSeq() {
        return userSeq;
    }
    
    public void setUserSeq(int userSeq) {
        this.userSeq = userSeq;
    }
    
    public String getUsePKISign() {
        return usePKISign;
    }
    
    public void setUsePKISign(String usePKISign) {
        this.usePKISign = usePKISign;
    }
    
    public String getPkiCertificate() {
        return pkiCertificate;
    }
    
    public void setPkiCertificate(String pkiCertificate) {
        this.pkiCertificate = pkiCertificate;
    }
    
    public String getPkiPrivateKey() {
        return pkiPrivateKey;
    }
    
    public void setPkiPrivateKey(String pkiPrivateKey) {
        this.pkiPrivateKey = pkiPrivateKey;
    }
    
    public String getPkiAlgorithm() {
        return pkiAlgorithm;
    }
    
    public void setPkiAlgorithm(String pkiAlgorithm) {
        this.pkiAlgorithm = pkiAlgorithm;
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


