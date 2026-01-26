/**
 * MailMessageService.java 2009. 1. 16.
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
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseService;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>MailMessageService.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
@SuppressWarnings("unchecked")
public class MailMessageService extends BaseService {
	
	private MailManager mailManager = null;
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	public JSONObject switchMessagesFlags(
			String sharedFlag,
			String sharedUserSeq,
			String sharedFolderName,
			long[] uids, String[] folderNames, 
			String flagType, boolean used) 
	throws Exception {
		
		boolean isShared = (sharedFlag != null && sharedFlag.equals("shared"))?true:false;
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		JSONObject jObj = new JSONObject();;
		String[] msgList = new String[uids.length];
		I18nResources resource = getMessageResource();
		
		User connUser = user; 
		if(isShared){
			MailUserManager mailUserManager = 
				(MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");
			connUser = mailUserManager.readUserMailConnectionInfo(Integer.parseInt(sharedUserSeq), 
					Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)));		
		}
		
		try {			
			store = factory.connect(isShared, sharedUserSeq, remoteIp, connUser);;
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.imapconn"));
		}
		
		try {			
			mailManager.setProcessResource(store, getMessageResource());
			if(uids != null && folderNames != null){
				if(folderNames.length > 1){
					long[] multiUid = null;
					for (int i = 0; i < folderNames.length; i++) {
						multiUid = new long[]{uids[i]};
						mailManager.switchMessagesFlags(multiUid, folderNames[i], flagType, used);
						msgList[i] = folderNames[i]+"_"+uids[i];
					}
				} else {
					mailManager.switchMessagesFlags(uids, folderNames[0], flagType, used);
					for (int i = 0; i < uids.length; i++) {
						msgList[i] = folderNames[0]+"_"+uids[i];
					}
				}
			}
			
			jObj.put("used", used);
			jObj.put("flagType",flagType);
			jObj.put("list", msgList);
			
			return jObj;
			
		} catch (Exception e) {	
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(getMailErrorMessage(resource, e.getMessage()),e);			
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}		
	}
	
	public void deleteMessage(long[] uids, String[] folderNames) throws Exception {
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource();
		
		try {			
			store = factory.connect(request.getRemoteAddr(), user);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.imapconn"));
		}
		
		try {			
			mailManager.setProcessResource(store, getMessageResource());
			if(uids != null && folderNames != null){
				if(folderNames.length > 1){
					long[] multiUid = null;
					for (int i = 0; i < folderNames.length; i++) {
						multiUid = new long[]{uids[i]};
						mailManager.deleteMessage(multiUid, folderNames[i]);						
						writeMailLog(false,"action_message_delete", folderNames[i], "", Long.toString(uids[i]));
					}
				} else {
					mailManager.deleteMessage(uids, folderNames[0]);
					writeMailLog(false,"action_message_delete", folderNames[0], "", StringUtils.getLongsToString(uids));
				}
			}						
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(getMailErrorMessage(resource, e.getMessage()),e);
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}		
	}
	
	public void cleanMessage(long[] uids, String[] folderNames) throws Exception {
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource();
		
		try {			
			store = factory.connect(request.getRemoteAddr(), user);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.imapconn"));
		}
		
		try {
			//User user = EnvConstants.getTestUser();
			store = factory.connect(request.getRemoteAddr(), user);
			
			mailManager.setProcessResource(store, getMessageResource());
			if(uids != null && folderNames != null){
				if(folderNames.length > 1){
					long[] multiUid = null;
					for (int i = 0; i < folderNames.length; i++) {
						multiUid = new long[]{uids[i]};
						mailManager.cleanMessage(multiUid, folderNames[i]);						
						writeMailLog(false,"action_message_clean", folderNames[i], "",Long.toString(uids[i]));
					}
				} else {
					mailManager.cleanMessage(uids, folderNames[0]);
					writeMailLog(false,"action_message_clean", folderNames[0], "",StringUtils.getLongsToString(uids));
				}
			}						
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(getMailErrorMessage(resource,e.getMessage()),e);
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}		
	}
	
	public void moveMessage(long[] uids, String[] fromFolder , String toFolder) throws Exception {
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource();
		
		try {			
			store = factory.connect(request.getRemoteAddr(), user);
		} catch (Exception e) {
			throw new Exception(resource.getMessage("error.imapconn"));
		}
		
		try {			
			
			mailManager.setProcessResource(store, getMessageResource());
			if(uids != null && fromFolder != null){
				if(fromFolder.length > 1){
					long[] multiUid = null;
					for (int i = 0; i < fromFolder.length; i++) {
						if(!fromFolder[i].equals(toFolder)){
							multiUid = new long[]{uids[i]};
							mailManager.moveMessage(multiUid, fromFolder[i], toFolder);
							writeMailLog(false,"action_message_move", fromFolder[i], toFolder,Long.toString(uids[i]));
						}
					}
				} else {					
					if(!fromFolder[0].equals(toFolder)){
						mailManager.moveMessage(uids, fromFolder[0], toFolder);
						writeMailLog(false,"action_message_move", fromFolder[0], toFolder,StringUtils.getLongsToString(uids));
					}
				}
			}						
			
		} catch (Exception e) {
			throw new Exception(getMailErrorMessage(resource, e.getMessage()),e);
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}
	}
	
	public void copyMessage(String sharedFlag,
							String sharedUserSeq,
							String sharedFolderName,							
							long[] uids, 
							String[] fromFolder , 
							String toFolder) 
	throws Exception {
		
		boolean isShared = (sharedFlag != null && sharedFlag.equals("shared"))?true:false;				
		
		TMailStore store = null;
		TMailStore sharedStore = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource();
		
		try {			
			store = factory.connect(request.getRemoteAddr(), user);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.imapconn"));
		}		
		
		if(!isShared){
			try {			
				mailManager.setProcessResource(store, getMessageResource());
				if(uids != null && fromFolder != null){
					if(fromFolder.length > 1){
						long[] multiUid = null;
						for (int i = 0; i < fromFolder.length; i++) {
							if(!fromFolder[i].equals(toFolder)){
								multiUid = new long[]{uids[i]};
								mailManager.copyMessage(multiUid, fromFolder[i], toFolder);
								writeMailLog(false,"action_message_copy", fromFolder[i], toFolder,Long.toString(uids[i]));
							}
						}
					} else {
						if(!fromFolder[0].equals(toFolder)){
							mailManager.copyMessage(uids, fromFolder[0], toFolder);
							writeMailLog(false,"action_message_copy", fromFolder[0], toFolder,StringUtils.getLongsToString(uids));
						}
					}
				}						
				
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
				throw new Exception(getMailErrorMessage(resource, e.getMessage()));
			} finally{
				if(store !=null && store.isConnected())
					store.close();
			}
		} else {
			MailUserManager mailUserManager = 
				(MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");
			User sharedUser = mailUserManager.readUserMailConnectionInfo(Integer.parseInt(sharedUserSeq), 
					Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)));		
			try {			
				sharedStore = factory.connect(isShared, sharedUserSeq, request.getRemoteAddr(), user);;
			} catch (Exception e) {
				throw new Exception(resource.getMessage("error.imapconn"));
			}
			
			try {				
				if(uids != null && sharedFolderName != null){					
					TMailFolder fromMailFolder = sharedStore.getFolder(sharedFolderName);
					TMailFolder toMailFolder = store.getFolder(toFolder);					
					mailManager.copySharedMessage(uids,fromMailFolder,toMailFolder);					
					
				}
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
				throw new Exception(getMailErrorMessage(resource, e.getMessage()),e);
			} finally{
				if(store !=null && store.isConnected())
					store.close();
				if(sharedStore !=null && sharedStore.isConnected())
					sharedStore.close();
			}
			
			
		}
	}
	
	
	public long removeAttachFile(long uid, String folderName, String part) throws Exception {
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource();
		
		try {			
			store = factory.connect(request.getRemoteAddr(), user);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.imapconn"));
		}
		
		try {			
			mailManager.setProcessResource(store, getMessageResource());
			writeMailLog(false,"action_message_removeattachfile",folderName,"",Long.toString(uid));		
			return mailManager.removeAttachFile(folderName, uid, part, 
					user.get(User.MAIL_UID), EnvConstants.getBasicSetting("tmpdir"));
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(getMailErrorMessage(resource, e.getMessage()),e);
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}	
	}
	
	public JSONObject getMailAdressList(boolean isNotOrgSearch) throws Exception {
		String locale = user.get(User.LOCALE);
		
		JSONObject jObj = new JSONObject();
		try {			
			MailAddressBean[] addrs = 
				mailManager.getUserMailAddressList(Integer.parseInt(user.get(User.MAIL_USER_SEQ)),
						Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)), locale, 15, null, false,isNotOrgSearch);
			
			JSONArray list = new JSONArray();
			int size = addrs.length;
			for (int i = 0; i < size; i++) {				
				list.add(addrs[i].getAddress());
			}
			jObj.put("addrs", list);			
			
		} catch (Exception e) {			
			LogManager.writeErr(this, e.getMessage(), e);
		}
		return jObj;
	}
	
	public JSONObject getMessageIntegrity(String folderName, String uid) throws Exception {
		JSONObject jObj = new JSONObject();
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource();
		
		try {			
			store = factory.connect(request.getRemoteAddr(), user);
		} catch (Exception e) {
			throw new Exception(resource.getMessage("error.imapconn"));
		}
		
		try {			
			mailManager.setProcessResource(store, getMessageResource());									
			String integrity = mailManager.getMessageIntegrity(folderName, uid);
			jObj.put("result", "success");
			jObj.put("integrity", integrity.toLowerCase());
			writeMailLog(false,"action_message_integrity",folderName,"",uid);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			jObj.put("result", "error");
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}
		
		return jObj;
	}
	
	private String getMailErrorMessage(I18nResources mresource,String msg) {
		
		String errMsg = mresource.getMessage("error.commonerror");		
		if(msg != null){			
			if(msg.indexOf("quota") > -1){
				errMsg = mresource.getMessage("error.quota");
			} 
		}		
		return errMsg;
	}
}
