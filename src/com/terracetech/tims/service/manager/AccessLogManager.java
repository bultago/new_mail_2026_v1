package com.terracetech.tims.service.manager;

import org.apache.log4j.Logger;

public class AccessLogManager {
	
	public static void writeLogMsg(String id, String agent, String action) {
		String message = id + "\t" +agent + "\t" + action;
		Logger.getLogger(AccessLogManager.class).debug(message);
	}
	
}