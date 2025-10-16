/**
 * MailSearchFolderService.java 2009. 1. 16.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.service;

import org.json.simple.JSONArray;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseService;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.exception.ProtocolConnectException;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;

/**
 * <p><strong>MailSearchFolderService.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MailSearchFolderService extends BaseService {
	
	private MailManager mailManager = null;
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	public JSONArray getFolderList() throws Exception {		
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
			list = mailManager.getJsonSearchFolders(null);			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.folderInfo"),e);
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}	
		
		return list;
	}
	
	public void addSearchFolder(String folderName, String query) throws Exception {
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
			mailManager.addSearchFolder(folderName, query);		
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.folderadd"),e);
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}	
	}
	
	public void modifySearchFolder(String oldId, String folderName, String query) throws Exception {
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
			mailManager.modifySearchFolder(oldId, folderName, query);
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.foldermodfy"),e);
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}	
	}
	
	public void deleteSearchFolder(String[] folderIds) throws Exception {
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
				throw new Exception(resource.getMessage("error.imapconn"));
			}			
		}
		
		try {
			mailManager.setProcessResource(store, getMessageResource());			
			mailManager.deleteSearchFolder(folderIds);			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.folderdel"),e);
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}	
	}
}
