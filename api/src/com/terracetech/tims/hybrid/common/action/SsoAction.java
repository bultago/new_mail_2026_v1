package com.terracetech.tims.hybrid.common.action;

import com.terracetech.tims.hybrid.common.manager.HybridAuthManager;
import com.terracetech.tims.webmail.util.StringUtils;

public class SsoAction extends BaseAction {

    private int schedulerId = 0;
    private String path = null;
    private String date = null;
    private String url = null;
    
    private HybridAuthManager hybridAuthManager = null;

    public void setHybridAuthManager(HybridAuthManager hybridAuthManager) {
        this.hybridAuthManager = hybridAuthManager;
    }

    public String execute() throws Exception {
        
        path = (StringUtils.isEmpty(path)) ? "calendar" : path;
        
        hybridAuthManager.makeSsoCookie(authKey, response);
        
        if("todayCalendar".equalsIgnoreCase(path)) {
            url = "/mobile/calendar/viewCalendar.do?schedulerId="+schedulerId+"&date="+date+"&target=home";
        }
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSchedulerId(int schedulerId) {
        this.schedulerId = schedulerId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }
}
