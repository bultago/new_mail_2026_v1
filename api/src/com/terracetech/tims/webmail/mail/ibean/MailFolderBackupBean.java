/**
 * MailFolderBackupBean.java 2009. 4. 27.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.ibean;

import org.json.simple.JSONObject;

/**
 * <p><strong>MailFolderBackupBean.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MailFolderBackupBean {
	
	boolean backupLoad = false;
	boolean backupComplete = false;
	boolean error = false;
	
	String folderName = null;
	String backupFile = null;
	int percent = -1;	
	

	/**
	 * @return backupLoad °ª ¹ÝÈ¯
	 */
	public boolean isBackupLoad() {
		return backupLoad;
	}
	/**
	 * @param backupLoad ÆÄ¶ó¹ÌÅÍ¸¦ backupLoad°ª¿¡ ¼³Á¤
	 */
	public void setBackupLoad(boolean backupLoad) {
		this.backupLoad = backupLoad;
	}
	/**
	 * @return backupComplete °ª ¹ÝÈ¯
	 */
	public boolean isBackupComplete() {
		return backupComplete;
	}
	/**
	 * @param backupComplete ÆÄ¶ó¹ÌÅÍ¸¦ backupComplete°ª¿¡ ¼³Á¤
	 */
	public void setBackupComplete(boolean backupComplete) {
		this.backupComplete = backupComplete;
	}
	/**
	 * @return folderName °ª ¹ÝÈ¯
	 */
	public String getFolderName() {
		return folderName;
	}
	/**
	 * @param folderName ÆÄ¶ó¹ÌÅÍ¸¦ folderName°ª¿¡ ¼³Á¤
	 */
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	/**
	 * @return percent °ª ¹ÝÈ¯
	 */
	public int getPercent() {
		return percent;
	}
	/**
	 * @param percent ÆÄ¶ó¹ÌÅÍ¸¦ percent°ª¿¡ ¼³Á¤
	 */
	public void setPercent(int percent) {
		this.percent = percent;
	}	
	/**
	 * @return backupFile °ª ¹ÝÈ¯
	 */
	public String getBackupFile() {
		return backupFile;
	}
	/**
	 * @param backupFile ÆÄ¶ó¹ÌÅÍ¸¦ backupFile°ª¿¡ ¼³Á¤
	 */
	public void setBackupFile(String backupFile) {
		this.backupFile = backupFile;
	}
	/**
	 * @return error °ª ¹ÝÈ¯
	 */
	public boolean isError() {
		return error;
	}
	/**
	 * @param error ÆÄ¶ó¹ÌÅÍ¸¦ error°ª¿¡ ¼³Á¤
	 */
	public void setError(boolean error) {
		this.error = error;
	}
	
	
	public JSONObject toJson(){
		JSONObject jObj = new JSONObject();
		jObj.put("error", error);
		jObj.put("backupLoad", backupLoad);
		jObj.put("backupComplete", backupComplete);
		jObj.put("folderName", folderName);
		jObj.put("backupFile", backupFile);
		jObj.put("percent", percent);		
		return jObj;		
	}
	
	public String getJson(){		
		return toJson().toString();
	}

}
