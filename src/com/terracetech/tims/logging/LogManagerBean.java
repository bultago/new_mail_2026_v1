package com.terracetech.tims.logging;

import java.text.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManagerBean {
	
	private static boolean debugFlag = false;
	private Logger logger = null;
	private String ip = null;
	private String email = null;
	private String actionName = null;

	private String formatString = null;
	
	public static void setDebugFlag(boolean flag){
		debugFlag = flag;
	}
	
	@SuppressWarnings("all")
	public void setClassLoger(Class cls){
		this.logger = LoggerFactory.getLogger(cls);		
	}
	
	public void setFormatString(String formatString){
		this.formatString = formatString;
	}	
	
	public void setIP(String ip) {
		this.ip = ip;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getIP() {
		return ip;
	}

	public String getEmail() {
		return email;
	}
	
	public String getActionName() {
		return actionName;
	}

	public void setIPnEmail(String ip, String email) {
		this.ip = ip;
		this.email = email;
	}

	public void debug(String str) {
		if (logger.isDebugEnabled() && debugFlag) {
			logger.debug(getLogMsg(str));
		}
	}

	public void info(String str) {
		if (logger.isInfoEnabled()) {
			logger.info(getLogMsg(str));
		}
	}
	
	public void warn(String str) {
		logger.warn(getLogMsg(str));
	}

	public void error(String str) {
		logger.error(getLogMsg(str));
	}	

	public void error(Throwable t) {
		logger.error(getLogMsg(t.toString()));
	}

	public void fatal(String str) {
		logger.fatal(getLogMsg(str));
	}

	public void debug(String ip, String email, String action) {
		setIPnEmail(ip, email);
		debug(action);
	}

	public void info(String ip, String email, String info) {
		setIPnEmail(ip, email);
		info(info);
	}
	
	public void warn(String ip, String email, String info) {
		setIPnEmail(ip, email);
		warn(info);
	}

	public void error(String ip, String email, String info) {
		setIPnEmail(ip, email);
		error(info);
	}

	public void error(String ip, String email, Throwable t) {
		setIPnEmail(ip, email);
		error(t);
	}

	public void fatal(String ip, String email, String action) {
		setIPnEmail(ip, email);
		fatal(action);
	}
	
	public String getLogMsg(String info) {

		MessageFormat mf = new MessageFormat(formatString);
		Object[] arguments = {"","","","",""};
		
		if (ip != null) {
			arguments[0] = ip;
		}
		if (email != null) {
			arguments[1] = email;
		}
		if (actionName != null) {
			arguments[2] = actionName;
		}
		if (info != null) {
			arguments[3] = info;
		}		
				
		return mf.format(arguments);
	}	
}

