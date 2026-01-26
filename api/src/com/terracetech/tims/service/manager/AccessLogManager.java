package com.terracetech.tims.service.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessLogManager {
	
	public static void writeLogMsg(String id, String agent, String action) {
		String message = id + "\t" +agent + "\t" + action;
		LoggerFactory.getLogger(AccessLogManager.class).debug(message);
	}
	
}