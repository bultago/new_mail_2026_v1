/**
 * 
 */
package com.terracetech.tims.webmail.common;

import java.util.Map;

import com.terracetech.tims.logging.LogManagerBean;
import com.terracetech.tims.webmail.util.FileUtil;

/**
 * @author ������
 *
 */
public class EnvConstants {
	private static Map<String, String> basicMap = null;
	private static Map<String, String> reportMap = null;
	private static Map<String, String> mailMap = null;
	private static Map<String, String> ladminMap = null;
	private static Map<String, String> attachMap = null;
	private static Map<String, String> utilMap = null;
	private static Map<String, String> virusMap = null;
	
	/**
	 * @param basicMap the basicMap to set
	 */
	public static void setBasicMap(Map<String, String> basicMap) {
		if(basicMap.containsKey("alim.crypt.method")){
			if(basicMap.get("alim.crypt.method").length() == 0){
				basicMap.put("alim.crypt.method","normal");
			}
		} else {
			basicMap.put("alim.crypt.method","normal");
		}
		EnvConstants.basicMap = basicMap;
		LogManagerBean.setDebugFlag(Boolean.parseBoolean(basicMap.get("log.debug")));
	}
	
	public static String getBasicSetting(String name){
		if(basicMap.containsKey(name)){
			return basicMap.get(name);
		} else {
			return null;
		}		
	}
	
	public static void setReportMap(Map<String, String> reportMap) {
		EnvConstants.reportMap = reportMap;		
	}
	
	public static String getReportSetting(String name){
		if(reportMap.containsKey(name)){
			return reportMap.get(name);
		} else {
			return null;
		}		
	}
	
	public static void setVirusMap(Map<String, String> virusMap) {
		EnvConstants.virusMap = virusMap;		
	}
	
	public static String getVirusSetting(String name){
		if(virusMap.containsKey(name)){
			return virusMap.get(name);
		} else {
			return null;
		}		
	}
	
	/**
	 * @param ladminMap the ladminMap to set
	 */
	public static void setMailMap(Map<String, String> mailMap) {
		EnvConstants.mailMap = mailMap;
	}
	
	public static String getMailSetting(String name){		
		if(mailMap.containsKey(name)){
			return mailMap.get(name);
		} else {
			return null;
		}		
	}
	
	/**
	 * @param ladminMap the ladminMap to set
	 */
	public static void setLadminMap(Map<String, String> ladminMap) {
		EnvConstants.ladminMap = ladminMap;
	}
	
	public static String getLadminSetting(String name){		
		if(ladminMap.containsKey(name)){
			return ladminMap.get(name);
		} else {
			return null;
		}
	}
	
	/**
	 * @param attachMap the attachMap to set
	 */
	public static void setAttachMap(Map<String, String> attachMap) {
		EnvConstants.attachMap = attachMap;
	}
	
	public static String getAttachSetting(String name){		
		if(attachMap.containsKey(name)){
			return attachMap.get(name);
		} else {
			return null;
		}
	}
	
	/**
	 * @param utilMap the utilMap to set
	 */
	public static void setUtilMap(Map<String, String> utilMap) {
		EnvConstants.utilMap = utilMap;
	}
	
	public static String getUtilSetting(String name){		
		if(utilMap.containsKey(name)){
			return utilMap.get(name);
		} else {
			return null;
		}
	}
	
	public static final int ALL_FOLDER = 0;
	public static final int DEFAULT_FOLDER = 1;
	public static final int USER_FOLDER = 2;
	public static final int SPECIFIC_FOLDER = 3;
	
	public static final int WEBFOLDER_QUOTA_SIZE = 10 * FileUtil.SIZE_MEGA;
	public static final int WEBFOLDER_MAX_FILE_SIZE = 10 * FileUtil.SIZE_MEGA;
	
	public static final String DIR_SEPARATOR = System.getProperty("file.separator");
	public static final String ENCRYPT_KEY = "tErRaCeNtWeBmAiL";
	
	public static final String COOKIE_ALGORITHM_METHOD = "COOKIE_ALGORITHM_METHOD";
	
	public static final String DEFAULT_CHARSET = java.nio.charset.Charset.defaultCharset().name();
	public static final String S_MAIL_DEFAULT = "tms";
	public static final String S_MAIL_INITECH = "initech";
	public static final String S_MAIL_SOFTFORUM = "softforum";
	
	
	public static byte[] transimage = new byte[] {
	       71, 73, 70, 56, 57, 97, 1, 0, 1, 0, -128, 0, 0, -1, -1, -1, 0, 0, 0, 33,
	       -7, 4, 1, 0, 0, 0, 0, 44, 0, 0, 0, 0, 1, 0, 1, 0, 0, 2, 2, 68, 1, 0, 59};
	
	/**
	 * dev64.terracetech.co.kr
	 * mailadm@dev64.terracetech.co.kr
	 * /data/test/tims6/mindex/hmkim.terracetech.co.kr/88/mailadm 953 0 0 1 90 10
	 * 
	 * mail.terracetech.com
	 * mailadm@terracetech.com
	 * /mindex/573/mailadm 3162 0 100000 1 90 10
	 * @return
	 */
	public static com.terracetech.tims.webmail.mailuser.User getTestUser(){
		com.terracetech.tims.webmail.mailuser.User user = new com.terracetech.tims.webmail.mailuser.UserInfo();

		/*user.put(com.terracetech.tims.webmail.mailuser.User.MAIL_HOST, "dev64.terracetech.co.kr");
		user.put(com.terracetech.tims.webmail.mailuser.User.EMAIL, "mailadm@dev64.terracetech.co.kr");
		user.put(com.terracetech.tims.webmail.mailuser.User.IMAP_LOGIN_ARGS,				
				"/data/test/tims6/mindex/hmkim.terracetech.co.kr/88/mailadm 953 0 0 1 90 10");*/
		
		user.put(com.terracetech.tims.webmail.mailuser.User.MAIL_USER_SEQ, "255");
		user.put(com.terracetech.tims.webmail.mailuser.User.MAIL_DOMAIN_SEQ, "8");
		user.put(com.terracetech.tims.webmail.mailuser.User.MAIL_DOMAIN, "dev64.terracetech.com");
		user.put(com.terracetech.tims.webmail.mailuser.User.MAIL_UID, "sshyun");		
		user.put(com.terracetech.tims.webmail.mailuser.User.MAIL_HOST, "dev64.terracetech.co.kr");
		user.put(com.terracetech.tims.webmail.mailuser.User.EMAIL, "sshyun@dev64.terracetech.com");
		user.put(com.terracetech.tims.webmail.mailuser.User.IMAP_LOGIN_ARGS,
				//"/mindex/793/sshyun 5120 0 100000 1 90 10");
			"/tims/mindex/12/sshyun 5120 0 100000 1 90 10");
		
		
		return user;
		
	}
	
	public static com.terracetech.tims.webmail.mailuser.User getWebfolderAdminTestUser(){
		com.terracetech.tims.webmail.mailuser.User user = new com.terracetech.tims.webmail.mailuser.UserInfo();
		
		user.put(com.terracetech.tims.webmail.mailuser.User.MAIL_USER_SEQ, "25");
		user.put(com.terracetech.tims.webmail.mailuser.User.MAIL_UID, "tims_webfolder_storage_user");
		user.put(com.terracetech.tims.webmail.mailuser.User.MAIL_DOMAIN, "dev64.terracetech.co.kr");
		user.put(com.terracetech.tims.webmail.mailuser.User.EMAIL, "tims_webfolder_storage_user@dev64.terracetech.co.kr");
		user.put(com.terracetech.tims.webmail.mailuser.User.MAIL_HOST, "dev64.terracetech.co.kr");
		user.put(com.terracetech.tims.webmail.mailuser.User.IMAP_LOGIN_ARGS,
			"/tims/mindex/888/tims_webfolder_storage_user 5120 0 100000 1 90 10");
		user.put(com.terracetech.tims.webmail.mailuser.User.WEBFOLDER_LOGIN_ARGS,
			"/tims/mindex/888/tims_webfolder_storage_user/etc/WEBFOLDER 5120 0 100000 1 90 10");
		
		return user;
		
	}
	
}
