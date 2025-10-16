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
 * <p>주요설명</p>
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
	 * @return backupLoad 값 반환
	 */
	public boolean isBackupLoad() {
		return backupLoad;
	}
	/**
	 * @param backupLoad 파라미터를 backupLoad값에 설정
	 */
	public void setBackupLoad(boolean backupLoad) {
		this.backupLoad = backupLoad;
	}
	/**
	 * @return backupComplete 값 반환
	 */
	public boolean isBackupComplete() {
		return backupComplete;
	}
	/**
	 * @param backupComplete 파라미터를 backupComplete값에 설정
	 */
	public void setBackupComplete(boolean backupComplete) {
		this.backupComplete = backupComplete;
	}
	/**
	 * @return folderName 값 반환
	 */
	public String getFolderName() {
		return folderName;
	}
	/**
	 * @param folderName 파라미터를 folderName값에 설정
	 */
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	/**
	 * @return percent 값 반환
	 */
	public int getPercent() {
		return percent;
	}
	/**
	 * @param percent 파라미터를 percent값에 설정
	 */
	public void setPercent(int percent) {
		this.percent = percent;
	}	
	/**
	 * @return backupFile 값 반환
	 */
	public String getBackupFile() {
		return backupFile;
	}
	/**
	 * @param backupFile 파라미터를 backupFile값에 설정
	 */
	public void setBackupFile(String backupFile) {
		this.backupFile = backupFile;
	}
	/**
	 * @return error 값 반환
	 */
	public boolean isError() {
		return error;
	}
	/**
	 * @param error 파라미터를 error값에 설정
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
