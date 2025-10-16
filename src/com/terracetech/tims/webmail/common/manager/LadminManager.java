/**
 * LadminManager.java 2009. 4. 27.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.common.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.ladmin.Protocol;
import com.terracetech.tims.webmail.exception.ProtocolException;
import com.terracetech.tims.webmail.mail.ibean.MailFolderBackupBean;
import com.terracetech.tims.webmail.util.MessageUtil;
import com.terracetech.tims.webmail.webfolder.manager.WebFolderUtils;

/**
 * <p><strong>LadminManager.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class LadminManager {
	
	private Protocol ladminProtocol = null;
	private I18nResources msgResource = null;
	private long fileSize = 0;
	
	public void setResource(Protocol ladminProtocol, I18nResources msgResource){
		this.ladminProtocol = ladminProtocol;
		this.msgResource = msgResource;
	}
	
	public void setResource(Protocol ladminProtocol){
		this.ladminProtocol = ladminProtocol;		
	}
	
	public boolean startFolderBackup(String email, String folderName)
	throws ProtocolException{
		boolean isStart = false;
		try {			
			isStart = ladminProtocol.sendCommand("USER_MAILBOX_BACKUP", new String[]{email, folderName});
		} catch (Exception e) {
			throw new ProtocolException("BACKUP_START_ERROR : "+ e.getMessage());
		}
		
		return isStart;
	}
	
	public MailFolderBackupBean getFolderBackupStatus(String email){	
		MailFolderBackupBean backupBean = new MailFolderBackupBean();
		
		try {
			Map map = ladminProtocol.get("USER_MAILBOX_BACKUP_STATUS", new String[]{email});			
			int percent = Integer.parseInt(((String[])map.get("status"))[0]);
			if(percent >= 0 && percent < 100){
				backupBean.setBackupLoad(true);
				backupBean.setBackupComplete(false);
				backupBean.setPercent(percent);
				
				String [] folderNameList = (String[])map.get("foldername");
				for(int i=0; i< folderNameList.length; i++){
					folderNameList[i] = WebFolderUtils.base64Decode(folderNameList[i]);
				}
				backupBean.setFolderName(MessageUtil.getFolderAlias(msgResource, 
						TMailUtility.IMAPFolderDecode(getBackupFolderName(folderNameList))));
			} else if(percent == 100){
				backupBean.setBackupLoad(false);
				backupBean.setBackupComplete(true);
				
				String [] folderNameList = (String[])map.get("foldername");
				for(int i=0; i< folderNameList.length; i++){
					folderNameList[i] = WebFolderUtils.base64Decode(folderNameList[i]);
				}				
				backupBean.setFolderName(MessageUtil.getFolderAlias(msgResource, 
						TMailUtility.IMAPFolderDecode(getBackupFolderName(folderNameList))));
			} else {
				backupBean.setBackupLoad(false);
				backupBean.setBackupComplete(false);
			}						
		} catch (Exception e) {			
			backupBean.setError(true);
		}
		
		return backupBean;
	}
	
	private String getBackupFolderName(String[] folderNameList){
		StringBuffer sb = new StringBuffer();		
		if(folderNameList != null){			
			int size = folderNameList.length;
			for (int i = 0; i < size; i++) {
				if(i > 0){
					sb.append(" ");
				}
				sb.append(folderNameList[i]);
			}
		}
		
		return sb.toString();
	}
	
	public InputStream getFolderBackupFile(String email)throws ProtocolException{
		InputStream is = null;
		try {
			this.fileSize = 
				ladminProtocol.fileFetch("USER_MAILBOX_BACKUP_DOWNLOAD", new String[]{email});
			if(fileSize > 0){
				is = ladminProtocol.getFileStream();
			}
		} catch (Exception e) {
			throw new ProtocolException("ERROR_BACKUP_FOLDER_DOWN :"+e.getMessage());						
		}		
		return is;
	}
	
	public long getFileSize(){
		return fileSize;
	}
	
	public boolean deleteFolderBackup(String email)throws ProtocolException{
		boolean isSuccess = false;
		try {			
			isSuccess = ladminProtocol.sendCommand("USER_MAILBOX_BACKUP_DELETE", new String[]{email});
		} catch (Exception e) {
			throw new ProtocolException("ERROR_BACKUP_FOLDER_DELETE :"+e.getMessage());			
		}
		
		return isSuccess;
	}
	
	public boolean batchSmtpExecute(File rcptFile, String rcptFilePath,
			File mimeFile, String mimeFilePath, String args)
	
	throws ProtocolException{
		
		boolean isSuccess = false;
		try {			
			
			FileInputStream rinput =
				new FileInputStream(rcptFile.getPath());
			ladminProtocol.fileUpload("FILE_UPLOAD", 
					new String[]{rcptFilePath,"{"+rcptFile.length()+"}"}, 
					rinput);
			
			FileInputStream minput =
				new FileInputStream(mimeFile.getPath());
			ladminProtocol.fileUpload("FILE_UPLOAD", 
					new String[]{mimeFilePath,"{"+mimeFile.length()+"}"}, 
					minput);			
			
			isSuccess = ladminProtocol.sendCommand("RSUPPORT_EXECUTE BATCHSMTPD", new String[]{args});
		} catch (Exception e) {
			throw new ProtocolException("ERROR_BATCH_SMTP_SEND :"+e.getMessage());			
		}	
		
		return isSuccess;
	}
	
	public void registSpamAndWhiteMessage(char type, String index, String folderName, String[] uids)
	throws ProtocolException{
		try {
			String[] params = null;
			List<String> paramList = new ArrayList<String>();
			if(type == 's'){
				paramList.add(new String("REGSPAM"));
			} else {
				paramList.add(new String("REGNORMAL"));
			}
			paramList.add(index);
			paramList.add(folderName);
			
			for (String uid : uids) {
				paramList.add(uid);
			}
			
			params = new String[paramList.size()];
			paramList.toArray(params);
			
			ladminProtocol.sendCommand("USER_MESSAGE",params);			
		} catch (Exception e) {
			throw new ProtocolException("ERROR_SPAM_MESSAGE_REGIST :"+e.getMessage());
		}
	}
	
	public float[] getSpamAndWhiteRate(String index, String folderName, String[] uids)
	throws ProtocolException{
		float[] spamRate = new float[uids.length];
		try {
			
			String[] params = null;
			List<String> paramList = new ArrayList<String>();
			paramList.add(new String("BAYESJUDGE"));			
			paramList.add(index);
			paramList.add(folderName);
			
			for (String uid : uids) {
				paramList.add(uid);
			}
			
			params = new String[paramList.size()];
			paramList.toArray(params);
			
			
			Map map = ladminProtocol.getMap("USER_MESSAGE", params);
			
			String[] resultValues = null;
			for (int i = 0; i < uids.length; i++) {		
				resultValues = (String[])map.get(uids[i]);
				if("SUCCESS".equalsIgnoreCase(resultValues[1])){
					spamRate[i] = Float.parseFloat(resultValues[2]);
				} else {
					spamRate[i] = 0;
				}				
			}			
		}catch (Exception e) {
			throw new ProtocolException("ERROR_SPAM_BAYESJUDGE_REGIST :"+e.getMessage());
		}
		
		return spamRate;
	}
	
	public void uploadFile(File file,String uploadPath)
	throws ProtocolException{
		try {
			FileInputStream input = new FileInputStream(file.getPath());
			ladminProtocol.fileUpload(
					"FILE_UPLOAD", 
					new String[]{uploadPath,"{"+file.length()+"}"},input);
		}catch (Exception e) {
			throw new ProtocolException("UPLOAD_FILE_EXCEPTION :"+e.getMessage());
		}
		
	}

}
