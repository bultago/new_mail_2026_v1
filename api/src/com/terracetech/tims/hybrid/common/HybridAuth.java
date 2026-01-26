package com.terracetech.tims.hybrid.common;

import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.manager.HybridAuthManager;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;

public class HybridAuth {

    private User user;
    private int authResult;
    private String authKey = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getAuthResult() {
        return authResult;
    }

    public void setAuthResult(int authResult) {
        this.authResult = authResult;
    }

    public boolean isAuthSuccess() {
        return authResult == HybridErrorCode.SUCCESS;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
    
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject result = new JSONObject();
        result.put("authKey", getAuthKey());
        result.put(HybridErrorCode.CODE_NAME, authResult);
        
        return result;
    }
    
    public void convertAuthCode(AuthUser authUser) {
        
        if (authUser.isAuthSuccess()) {
            setUser(authUser.getUser());
        }
        
        switch (authUser.getAuthResult()) {
        case UserAuthManager.SUCCESS:
            authResult = HybridErrorCode.SUCCESS;
            break;
        case UserAuthManager.NOT_FOUND:
            authResult = HybridErrorCode.NOT_FOUND;
            break;
        case UserAuthManager.PASSWORD_LOCK:
            authResult = HybridErrorCode.PASSWORD_LOCK;
            break;
        case UserAuthManager.PASSWORD_FAIL:
            authResult = HybridErrorCode.PASSWORD_FAIL;
            break;
        case UserAuthManager.NOT_AGREE:
            authResult = HybridErrorCode.NOT_AGREE;
            break;
        case UserAuthManager.SERVICE_STOP:
            authResult = HybridErrorCode.SERVICE_STOP;
            break;
        case UserAuthManager.WEBMAIL_SERVICE_EXPIRE:
            authResult = HybridErrorCode.WEBMAIL_SERVICE_EXPIRE;
            break;
        case UserAuthManager.WEBMAIL_SERVICE_FAIL:
            authResult = HybridErrorCode.WEBMAIL_SERVICE_FAIL;
            break;
        case UserAuthManager.PASSWORD_CHANGE:
            authResult = HybridErrorCode.PASSWORD_CHANGE;
            break;
       
        default:
            authResult = HybridErrorCode.ERROR;
            break;
        }
    }
    
}
