/**
 * MailFolderService.java 2008. 12. 15.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.BaseService;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.exception.ProtocolConnectException;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;

/**
 * <p>
 * <strong>MailFolderService.java</strong> Class Description
 * </p>
 * <p>
 * �ֿ伳��
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author sshyun
 * @since Tims7
 * @version 7.0
 */
@SuppressWarnings("unchecked")
public class MailFolderService extends BaseService {

	private MailManager mailManager = null;	
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	public JSONObject getMailFolderAllInfo() throws Exception {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		JSONObject jObj = new JSONObject();		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();		
		I18nResources resource = getMessageResource();
		
		try {			
			store = getStore(factory,request.getRemoteAddr());
		} catch (Exception e) {
			Thread.sleep(2000);
			try {
				store = getStore(factory,request.getRemoteAddr());
			} catch (Exception e2) {
				LogManager.writeErr(this, e2.getMessage(), e2);
				throw new Exception(resource.getMessage("error.imapconn"), e2);
			}			
		}
		
		try{
			mailManager.setProcessResource(store, getMessageResource());
			
			jObj.put("quotaInfo",mailManager.getQuotaRootInfo("Inbox").toJson());
			jObj.put("defaultFolders", mailManager.getJsonFolderList(
					EnvConstants.DEFAULT_FOLDER,false,userSeq));
			jObj.put("userFolders",mailManager.getJsonFolderList(
					EnvConstants.USER_FOLDER,false,userSeq));			
			jObj.put("userSearchFolders",mailManager.getJsonSearchFolders(null));
			jObj.put("userTags",mailManager.getJsonTagList(null));
			jObj.put("userSharedFolderList",mailManager.getJsonSharringFolders(userSeq));			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.folderInfo"),e);
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}	
		
		
		return jObj;
	}

	public JSONObject getMailFolderInfo() throws Exception {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		JSONObject jObj = new JSONObject();		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();		
		I18nResources resource = getMessageResource();
		
		try {			
			store = getStore(factory,request.getRemoteAddr());
		} catch (Exception e) {
			Thread.sleep(2000);
			try {
				store = getStore(factory,request.getRemoteAddr());
			} catch (Exception e2) {
				LogManager.writeErr(this, e2.getMessage(), e2);
				throw new Exception(resource.getMessage("error.imapconn"), e2);
			}			
		}
		
		try{
			mailManager.setProcessResource(store, getMessageResource());
			
			jObj.put("quotaInfo",mailManager.getQuotaRootInfo("Inbox").toJson());
			jObj.put("defaultFolders", mailManager.getJsonFolderList(
					EnvConstants.DEFAULT_FOLDER,false,userSeq));
			jObj.put("userFolders",mailManager.getJsonFolderList(
					EnvConstants.USER_FOLDER,false,userSeq));			
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.folderInfo"),e);
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}		
		
		
		return jObj;
	}
	
	public String emptyFolder(String folderName) throws Exception {
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource();
		
		try {			
			store = getStore(factory,request.getRemoteAddr());
		} catch (Exception e) {
			Thread.sleep(2000);
			try {
				store = getStore(factory,request.getRemoteAddr());
			} catch (Exception e2) {
				LogManager.writeErr(this, e2.getMessage(), e2);
				throw new Exception(resource.getMessage("error.imapconn"));
			}			
		}
		
		try {					
			mailManager.setProcessResource(store, getMessageResource());
			mailManager.emptyFolder(folderName);
			writeMailLog(false,"empty_folder", folderName, "", "");
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.folderempty"),e);
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}
		
		return "success";
	}
	
	public String addFolder(String folderName) throws Exception {
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource();
		boolean isExist = false;
		String result = "success";
		
		try {			
			store = getStore(factory,request.getRemoteAddr());
		} catch (Exception e) {
			Thread.sleep(2000);
			try {
				store = getStore(factory,request.getRemoteAddr());
			} catch (Exception e2) {
				LogManager.writeErr(this, e2.getMessage(), e2);
				throw new Exception(resource.getMessage("error.imapconn"));
			}			
		}
		
		try {
			mailManager.setProcessResource(store, getMessageResource());
			// 2012.05.14 - ���θ����� ��� �����Ը� �յ� �������
			isExist = mailManager.addFolder(folderName.trim());	
			// 2012.05.14 - ���θ����� ��� �����Ը� �յ� �������
				
			if(isExist){				
				result = "exist";				
			} else {
				writeMailLog(false,"create_folder", folderName, "", "");
			}
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.folderadd"),e);						
		} finally {
			if(store !=null && store.isConnected())
				store.close();
		}
		
		return result;
	}
	
	public void deleteFolder(String folderName) throws Exception {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource();
		
		try {			
			store = getStore(factory,request.getRemoteAddr());
		} catch (Exception e) {
			Thread.sleep(2000);
			try {
				store = getStore(factory,request.getRemoteAddr());
			} catch (Exception e2) {
				LogManager.writeErr(this, e2.getMessage(), e2);
				throw new Exception(resource.getMessage("error.imapconn"));
			}			
		}
		
		try {
			mailManager.setProcessResource(store, getMessageResource());
			mailManager.deleteFolder(userSeq,TMailUtility.IMAPFolderEncode(folderName));
			writeMailLog(false,"delete_folder", folderName, "", "");
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.folderdel"),e);
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}
	}
	
	public void modifyFolder(String previousName, String parentName, String newName) throws Exception {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource();
		
		try {			
			store = getStore(factory,request.getRemoteAddr());
		} catch (Exception e) {
			Thread.sleep(2000);
			try {
				store = getStore(factory,request.getRemoteAddr());
			} catch (Exception e2) {
				LogManager.writeErr(this, e2.getMessage(), e2);
				throw new Exception(resource.getMessage("error.imapconn"));
			}			
		}
		
		try {
			String changeName= (parentName != null && parentName.length() > 0)?parentName+".":"";
			// 2012.05.14 - ���θ����� ��� �����Ը� �յ� �������
			changeName += TMailUtility.IMAPFolderEncode(newName.trim());
			// 2012.05.14 - ���θ����� ��� �����Ը� �յ� �������
			mailManager.setProcessResource(store, getMessageResource());
			mailManager.modifyFolder(userSeq, previousName, changeName);
			
			writeMailLog(false,"rename_folder", TMailUtility.IMAPFolderDecode(previousName), 
					TMailUtility.IMAPFolderDecode(changeName), "");
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.foldermodfy"),e);	
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}		
	}
	
	public JSONArray getSharringFolderList() throws Exception {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		JSONArray list = null;
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource();
		
		try {			
			store = getStore(factory,request.getRemoteAddr());
		} catch (Exception e) {
			Thread.sleep(2000);
			try {
				store = getStore(factory,request.getRemoteAddr());
			} catch (Exception e2) {
				LogManager.writeErr(this, e2.getMessage(), e2);
				throw new Exception(resource.getMessage("error.imapconn"));
			}			
		}
		
		try {			
			mailManager.setProcessResource(store, getMessageResource());			
			list = mailManager.getJsonSharringFolders(userSeq);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.folderInfo"),e);
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}	
		
		return list;
	}
	
	public JSONArray getSharringReaderList(int folderUid) throws Exception {		
		JSONArray list = null;		
		try {			
			list = mailManager.getJsonSharedFolderReaders(folderUid);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception("Faile to get shared readers.",e);
		}
		
		return list;
		
	}
	
	public JSONObject setSharringReaderList(boolean isSave, int folderUid, String folderName, int[] sharedUserSeq) throws Exception {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		JSONObject jObj = new JSONObject();
		try {
			if(isSave){
				mailManager.setSharedFolder(folderUid, 
						TMailUtility.IMAPFolderEncode(folderName), 
						userSeq, sharedUserSeq);
			} else if(folderUid > 0){
				mailManager.deleteSharedFolder(folderUid);
			}
			jObj.put("result", "success");
		} catch (Exception e) {			
			LogManager.writeErr(this, e.getMessage(), e);
			jObj.put("result", "fail");
		}
		
		return jObj;
	}
}