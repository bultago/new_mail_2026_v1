package com.terracetech.tims.webmail.organization.vo;

/**
 * 조직도 VO
 */
public class OrganizationVO {
    
    private int deptSeq;
    private String deptName;
    private String deptCode;
    private int parentDeptSeq;
    private int deptLevel;
    private int deptOrder;
    
    // Getters and Setters
    public int getDeptSeq() {
        return deptSeq;
    }
    
    public void setDeptSeq(int deptSeq) {
        this.deptSeq = deptSeq;
    }
    
    public String getDeptName() {
        return deptName;
    }
    
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    
    public String getDeptCode() {
        return deptCode;
    }
    
    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }
    
    public int getParentDeptSeq() {
        return parentDeptSeq;
    }
    
    public void setParentDeptSeq(int parentDeptSeq) {
        this.parentDeptSeq = parentDeptSeq;
    }
    
    public int getDeptLevel() {
        return deptLevel;
    }
    
    public void setDeptLevel(int deptLevel) {
        this.deptLevel = deptLevel;
    }
    
    public int getDeptOrder() {
        return deptOrder;
    }
    
    public void setDeptOrder(int deptOrder) {
        this.deptOrder = deptOrder;
    }
}


