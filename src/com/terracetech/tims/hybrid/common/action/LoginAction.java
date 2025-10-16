package com.terracetech.tims.hybrid.common.action;

import javax.servlet.http.HttpServletResponse;

import com.terracetech.tims.hybrid.common.HybridAuth;
import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.manager.HybridAuthManager;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class LoginAction extends BaseAction {
    
    HybridAuthManager hybridAuthManager = null;
    
    private String email = null;
    private String pass = null;
    private String uuid = null;
    private String AppVer = null;
    private String DeviceType = null;
    private String DeviceVer = null;
    
    public LoginAction(boolean authcheck) {
        setAuthCheck(authcheck);
    }
    
    public void setHybridAuthManager(HybridAuthManager hybridAuthManager) {
        this.hybridAuthManager = hybridAuthManager;
    }

    public String execute() throws Exception {
    	HybridAuth hybridAuth = null;
    	if (!hybridAuthManager.useMobileLicense()) {
    		hybridAuth = new HybridAuth();
    		hybridAuth.setAuthResult(HybridErrorCode.MOBILE_LICENSE_ERROR);
    	}
    	else if (!hybridAuthManager.allowDeviceAppVersion(AppVer, DeviceType, DeviceVer)) {
    		hybridAuth = new HybridAuth();
    		hybridAuth.setAuthResult(HybridErrorCode.MOBILE_VERSION_ERROR);
    		hybridAuth.setAuthKey("iphone".equalsIgnoreCase(DeviceType) ? EnvConstants.getUtilSetting("iphone.update.url") : EnvConstants.getUtilSetting("android.update.url"));
    	} else {
	        hybridAuth = hybridAuthManager.validateUser(request.getSession(), email, uuid, pass);
    	}
    	
        if (hybridAuth.isAuthSuccess()) {
        	hybridAuth.getUser().put(User.MOBILE_UID, uuid);
        	if (HybridAuthManager.checkMobieAccess(hybridAuth.getUser())) {
        		String authKey = hybridAuthManager.doLoginProcess(request, uuid, hybridAuth.getUser());
                hybridAuth.setAuthKey(authKey);
            } else {
            	hybridAuth.setAuthResult(HybridErrorCode.SERVICE_FORBIDDEN);
            }
        }
    	

        String returnName = request.getParameter("tmslogin");
        ResponseUtil.makeAjaxMessage(response, returnName+"("+hybridAuth.toJson()+")");
        //ResponseUtil.makeJsonpResponse(request, response, hybridAuth.toJson());
        
        return null;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

	public void setAppVer(String appVer) {
		AppVer = appVer;
	}

	public void setDeviceType(String deviceType) {
		DeviceType = deviceType;
	}

	public void setDeviceVer(String deviceVer) {
		DeviceVer = deviceVer;
	}
}
