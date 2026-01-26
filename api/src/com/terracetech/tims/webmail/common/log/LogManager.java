package com.terracetech.tims.webmail.common.log;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terracetech.tims.webmail.common.EnvConstants;

/**
 * <p><strong>LogManager.java</strong> Class Description</p>
 * <p>
 * ������� �α� ������ ���� debug/info �α��� �Ѵ�.<br>
 * ���� ��(Junit test)���� �ڵ����� debug �ɼ��� �����Ǿ� ���۵ȴ�.
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class LogManager {

	public static boolean isDebugEnabled() {
		return "true".equals(EnvConstants.getBasicSetting("log.debug"));
	}

	public static void writeInfo (Logger log, Object msg) {
		log.info(msg);
	}
	public static void writeDebug (Logger log, Object msg) {
		if (isDebugEnabled())
			log.debug(msg);
	}
	
	public static void writeErr (Logger log, Object msg) {
		log.error(msg);
	}
	
	public static void writeInfo(Object that, Object msg) {
		LoggerFactory.getLogger(convertTarget(that)).info(msg);
	}
	
	public static void writeDebug (Object that, Object msg) {
		if (isDebugEnabled())
			LoggerFactory.getLogger(convertTarget(that)).debug(msg);
	}
	
	public static void writeErr (Object that, Object msg) {
		LoggerFactory.getLogger(convertTarget(that)).error(msg);
	}
	
	public static void writeErr (Object that, Object msg, Throwable t) {		
		LoggerFactory.getLogger(convertTarget(that)).error(msg, t);
	}
	
	private static Class<?> convertTarget (Object that) {
		if (that instanceof Class)
			return (Class<?>)that;
		else
			return that.getClass();
	}
	
	public static void writeLogMsg(Logger log, String id, String job, Object ip, String action, String err) {
		writeInfo(log, ip+" "+id+" "+action+" "+job+" "+"MESSAGE:"+err);
	}
	
	public static void writeLogMsg(Object that, String id, String job, String ip, String action, String err) {
		writeInfo(that, ip+" "+id+" "+action+" "+job+" "+"MESSAGE:"+err);
	}
	
	public static void writeLogMsg(Logger log, String id, String job, String ip, String action) {
		writeInfo(log, ip+" "+id+" "+action+" "+job);
	}
	
	public static void writeLogMsg(Object that, String id, String job, String ip, String action) {
		writeInfo(that, ip+" "+id+" "+action+" "+job);
	}
	
	public static void writeMailLogMsg(boolean isJobLog, Logger log, String category, String id, 
			String ip, String action, String sfolder, String tfolder,
			String to, String from,
			long size, String charset, String subject, String mid, String uid){
		
		category = (category != null)?category:"WEBMAIL";
		mid = (mid != null)?mid:"";
		mid = mid.replaceAll("<", "");
		mid = mid.replaceAll(">", "");
		
		String monitoringLog = (isJobLog)?"!":"";
		writeInfo(log, monitoringLog+" MC:"+category+" ACT:"+action+" SIP:"+ip + " MU:"+id +
				" BS:" + sfolder +" BT:" + tfolder +
				" ER:" + to + " EF:" + from +
				" SZ:"+ ((size > 0)?size:"") + " CS:"+ charset +				
				" MCS:"+EnvConstants.DEFAULT_CHARSET +" "+
				" MID:"+ mid+
				" UID:"+ uid+
				" SJ:"+subject);
	}
	
	public static void writeMailLogMsg(boolean isJobLog, Logger log, String id, 
			String ip, String action, String sfolder, String tfolder,			
			String uid) {
		writeMailLogMsg(isJobLog, log, "WEBMAIL", id, ip, action, sfolder, tfolder, "", "", 0, "", "", "", uid);
	}
	
	public static void writeMailLogMsg(boolean isJobLog, Logger log, String id, 
			String ip, String action, String toAddrs, String fromAddrs,
			String sfolder, String tfolder,
			String mid,
			String uid) {
		writeMailLogMsg(isJobLog, log, "WEBMAIL", id, ip, action, sfolder, tfolder, toAddrs, fromAddrs, 0, "", "", mid, uid);
	}
	
	public static void writeMailLogMsg(boolean isJobLog, Logger log, String id, 
			String ip, String action,
			String folder,
			String to, String from,			
			long size, String charset, String subject, String mid) {
		writeMailLogMsg(isJobLog, log, "WEBMAIL", id, ip, action, folder, "", to, from, size, charset, subject, mid, "");		
	}
	
	public static void writeMailLogMsg(boolean isJobLog, Logger log, String id, 
			String ip, String action){
		writeMailLogMsg(isJobLog, log, "WEBMAIL", id, ip, action, "", "", "", "", 0, "", "", "", "");
	}
	
	public static void writeWebfolderLogMsg(boolean isJobLog, Logger log, String id, 
			String ip, String action,
			String fromFolder,			
			String toFolder,
			String to, String from,			
			long size, String charset, String subject, String mid) {
		writeMailLogMsg(isJobLog, log, "WEBFOLDER", id, ip, action, fromFolder, toFolder, to, from, size, charset, subject, mid, "");		
	}
	
	public static void writeWebfolderLogMsg(boolean isJobLog, Logger log, String id, 
			String ip, String action, String sfolder, String tfolder,			
			String uid) {
		writeMailLogMsg(isJobLog, log, "WEBFOLDER", id, ip, action, sfolder, tfolder, "", "", 0, "", "", "", uid);
	}

	private static Map<String, Long> timeMap = new HashMap<String, Long>();
	public static void printElapsedTime(String key, String workName){
		Long start = timeMap.get(key);
		if(start==null){
			System.out.println("*****************************"+ workName + " started");
		}else{
			System.out.println("*****************************"+ workName + " elapsed : " + (System.currentTimeMillis() - start) + " ms");	
		}
		
		
		timeMap.put(key, System.currentTimeMillis());
	}
}