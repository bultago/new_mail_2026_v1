package com.terracetech.tims.webmail.setting.action;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.util.StringUtils;

public class CacheDeleteAction extends BaseAction{

	private static final long serialVersionUID = 20090112L;
	
	private SystemConfigManager systemManager = null;
	
	private String param = null;
	
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}
	
	public String execute() throws Exception {
		if(StringUtils.isEmpty(param))
			param = "all";
		
		systemManager.deleteCache(param);
		
		return null;
	}
}
