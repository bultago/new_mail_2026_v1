package com.terracetech.tims.webmail.common.vo;

public class WebAccessInfoVO {
    
    private String webAccessType = "all";
    private String allowBigattach = null;
    private String webAllowIpStr = null;
    private String[] accessIpList = null;
    
    public String getWebAccessType() {
        return webAccessType;
    }
    public void setWebAccessType(String webAccessType) {
        this.webAccessType = webAccessType;
    }
    public String getAllowBigattach() {
        return allowBigattach;
    }
    public void setAllowBigattach(String allowBigattach) {
        this.allowBigattach = allowBigattach;
    }
    public String getWebAllowIpStr() {
        return webAllowIpStr;
    }
    public void setWebAllowIpStr(String webAllowIpStr) {
        this.webAllowIpStr = webAllowIpStr;
    }
    public String[] getAccessIpList() {
        return accessIpList;
    }
    public void setAccessIpList(String[] accessIpList) {
        this.accessIpList = accessIpList;
    }
}
