package com.terracetech.tims.webmail.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PerformanceLogManager {
	
	public static void writeLog(String ip, String user, String agent, String action, String time){
		if(agent.indexOf("MSIE 8") > -1){
			agent = "MSIE8";
		} else if(agent.indexOf("MSIE 7") > -1){
			agent = "MSIE7";				
		} else if(agent.indexOf("MSIE 6") > -1){
			agent = "MSIE6";
		} else if(agent.indexOf("FIREFOX") > -1){
			agent = "FIREFOX";			
		} else if(agent.indexOf("OPERA") > -1){
			agent = "OPERA";			
		} else if(agent.indexOf("CHROME") > -1){
			agent = "CHROME";
		} else if(agent.indexOf("SAFARI") > -1){
			agent = "SAFARI";
		}
		StringBuffer logString = new StringBuffer();
		logString.append(ip);
		logString.append(",");
		logString.append(user);
		logString.append(",");
		logString.append(agent);
		logString.append(",");
		logString.append(action);
		logString.append(",");
		logString.append(time);
		LoggerFactory.getLogger(PerformanceLogManager.class).info(logString.toString());
	}
	
}
