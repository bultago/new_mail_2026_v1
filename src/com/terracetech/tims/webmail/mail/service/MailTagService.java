/**
 * MailTagService.java 2009. 1. 16.
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
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;

/**
 * <p><strong>MailTagService.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MailTagService extends BaseService {
	
	private MailManager mailManager = null;
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	
	public JSONArray getTagList() throws Exception {
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
			list = mailManager.getJsonTagList(null);			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.imapconn"));
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}	
		
		return list;
	}
	
	public void addTag(String tagName, String tagColor) throws Exception {
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
			mailManager.addTag(tagName, tagColor);			
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.imapconn"));
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}	
	}
	
	public void modifyTag(String oldId, String tagName, String tagColor) throws Exception {
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
			mailManager.modifyTag(oldId, tagName, tagColor);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.imapconn"));
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}	
	}
	
	public void deleteTag(String[] tagIds) throws Exception {
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
			mailManager.deleteTag(tagIds);
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.imapconn"));
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}	
	}
	
	public void taggingMessage(String addFlag, long[] uids, String[] folders , String tagID) 
	throws Exception {
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		boolean isAdd = addFlag.equals("true");
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
			if(uids != null && folders != null){
				if(folders.length > 1){
					long[] multiUid = null;
					for (int i = 0; i < folders.length; i++) {
						multiUid = new long[]{uids[i]};
						mailManager.storeTagging(isAdd, folders[i], tagID, multiUid);
					}
				} else {
					mailManager.storeTagging(isAdd, folders[0], tagID, uids);
				}
			}						
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.imapconn"));
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}	
		
	}

}
