package com.terracetech.tims.jmobile.common.vo;

import java.util.Map;

@SuppressWarnings("all")
public class PreworkJobBean {
	String actionName = null;
	Map paramMap = null;
	
	public PreworkJobBean(String actionName, Map paramMap) {
		this.actionName = actionName;
		this.paramMap = paramMap;
	}
	
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public Map<String, String> getParamMap() {
		return paramMap;
	}
	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}
	
	
	
}
