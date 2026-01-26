package com.terracetech.tims.webmail.addrbook.vo;

/**
 * 주소록 VO (간략 버전)
 */
public class AddressVO {
    
    private int addrSeq;
    private String name;
    private String email;
    private String phone;
    private String company;
    private String department;
    
    // Getters and Setters
    public int getAddrSeq() {
        return addrSeq;
    }
    
    public void setAddrSeq(int addrSeq) {
        this.addrSeq = addrSeq;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getCompany() {
        return company;
    }
    
    public void setCompany(String company) {
        this.company = company;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
}


