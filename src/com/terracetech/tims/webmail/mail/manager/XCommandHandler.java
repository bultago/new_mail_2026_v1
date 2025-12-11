/**
 * XCommandHandler.java 2009. 1. 15.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager;

import java.util.Locale;

import jakarta.mail.MessagingException;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.mail.TMailXCommand;
import com.terracetech.tims.mail.search.SearchRequest;
import com.terracetech.tims.mail.search.TMailSearchQuery;
import com.terracetech.tims.mail.search.XSearchCommand;
import com.terracetech.tims.mail.sort.SortMessage;
import com.terracetech.tims.mail.sort.SortRequest;
import com.terracetech.tims.mail.tag.TMailTag;
import com.terracetech.tims.mail.tag.TagRequest;
import com.terracetech.tims.mail.tag.XTagCommand;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;

/**
 * <p><strong>XCommandHandler.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class XCommandHandler {
	private TMailXCommand command = null;
	private int total = 0;
	private Locale locale = null;
	
	
	public void setResource(TMailXCommand command, Locale locale) {
		this.command = command;
		this.locale = locale;
	}
	
	public int getTotalCnt(){
		return total;
	}
	
	public int getXSortTotal(
			MessageSortInfoBean sortBean,
			String[] exceptFolders, 
			String folderName) throws MessagingException{		
		int total = 0;
		
		sortBean.setPage("1");
		sortBean.setPageBase("1");
		SortMessage[] msgs = getXSortMessages(sortBean, exceptFolders, folderName);
		if(msgs != null){
			total = command.getSortTotal();
		}
		
		return total;		
	}
	
	public MailSortMessageBean[] getXSortMessageBeans(
			MessageSortInfoBean sortBean,
			String[] exceptFolders, 
			String folderName) 
			throws MessagingException{
		
		MailSortMessageBean[] beans = null;
		SortMessage[] msgs = getXSortMessages(sortBean, exceptFolders, folderName);
		if(msgs != null){
			beans = new MailSortMessageBean[msgs.length];
			for (int i = 0; i < msgs.length; i++) {
				beans[i] = new MailSortMessageBean(msgs[i],locale);				
			}			
		}
		
		total = command.getSortTotal();
		
		return beans;
	}
	
	public MailSortMessageBean[] getXSortRelationMessageBeans(
			MessageSortInfoBean sortBean,
			String[] exceptFolders, 
			String folderName,
			String relationFolderName,
			String relationUid) 
			throws MessagingException{
		
		MailSortMessageBean[] beans = null;
		SortMessage[] msgs = getXSortRelationMessages(sortBean, exceptFolders, folderName,relationFolderName,relationUid);
		if(msgs != null){
			beans = new MailSortMessageBean[msgs.length];
			for (int i = 0; i < msgs.length; i++) {
				beans[i] = new MailSortMessageBean(msgs[i],locale);				
			}			
		}
		
		total = command.getSortTotal();
		
		return beans;
	}
	
	
	public SortMessage[] getXSortMessages(
			MessageSortInfoBean sortBean,
			String[] exceptFolders, 
			String folderName) 
			throws MessagingException{
		
		SortRequest sortReq = new SortRequest();
		sortReq.setPage(Integer.parseInt(sortBean.getPage()));
		sortReq.setPageBase(sortBean.getPageBaseSize());
		sortReq.setOrderKey(sortBean.getMessageSortBy());
		sortReq.setOrderDirection(sortBean.getMessageSortDir());
		sortReq.setExceptFolders(exceptFolders);
		sortReq.setSearchFolder(folderName);
		sortReq.setCondition(TMailFolder.getSortArgs(sortBean.getSearchTerm()));				
		
		return command.getXSortMessage(sortReq);
	}
	
	public SortMessage[] getXSortRelationMessages(
			MessageSortInfoBean sortBean,
			String[] exceptFolders, 
			String folderName,
			String relationFolderName,
			String relationUid) 
			throws MessagingException{
		
		SortRequest sortReq = new SortRequest();
		sortReq.setPage(Integer.parseInt(sortBean.getPage()));
		sortReq.setPageBase(sortBean.getPageBaseSize());
		sortReq.setOrderKey(sortBean.getMessageSortBy());
		sortReq.setOrderDirection(sortBean.getMessageSortDir());
		sortReq.setExceptFolders(exceptFolders);
		sortReq.setSearchFolder(folderName);
		sortReq.setCondition(TMailFolder.getSortArgs(sortBean.getSearchTerm()));
		sortReq.setRelationSearch(true);
		sortReq.setRelationFolderName(relationFolderName);
		sortReq.setRelationUid(relationUid);
		
		return command.getXSortMessage(sortReq);
	}
	
	public TMailTag[] getTagList(String searchPattern) throws MessagingException{
		return getTagList(searchPattern, 0, 0);
	}
	
	public TMailTag[] getTagList(String searchPattern, int page, int pageBase) 
	throws MessagingException{
		TagRequest tagReq = new TagRequest(XTagCommand.CMD_TAGLIST);
		tagReq.setSearchPattern(searchPattern);
		tagReq.setPage(page);
		tagReq.setPageBase(pageBase);
		
		return command.getTagList(tagReq);
	}
	
	public void addMailTag(TMailTag newTag) throws MessagingException {		
		TagRequest tagReq = new TagRequest(XTagCommand.CMD_CREATE);
		tagReq.setNewTag(newTag);			
		command.controlTag(tagReq);
	}
	
	public void deleteMailTag(String[] tids) throws MessagingException {		
		TagRequest tagReq = new TagRequest(XTagCommand.CMD_DELETE);
		tagReq.setWorkIds(tids);			
		command.controlTag(tagReq);
	}
	
	public void modMailTag(String oldTagId, TMailTag modTag) 
	throws MessagingException {
		TMailTag currentTag = new TMailTag();
		currentTag.setId(Integer.parseInt(oldTagId));
		
		TagRequest tagReq = new TagRequest(XTagCommand.CMD_MODIFY);
		tagReq.setCurrentTag(currentTag);
		tagReq.setNewTag(modTag);
		
		command.controlTag(tagReq);
	}
	
	public void storeMailTag(boolean isAdd, String folderName, String tagId, long[] uids) 
	throws MessagingException {
		TMailTag currentTag = new TMailTag();
		currentTag.setId(Integer.parseInt(tagId));		
		
		TagRequest tagReq = new TagRequest(XTagCommand.CMD_STORE);
		tagReq.setCurrentTag(currentTag);
		tagReq.setStoreFolderName(TMailUtility.IMAPFolderEncode(folderName));
		tagReq.setStoreUid(uids);
		if(isAdd){
			tagReq.setMessageStoreTag(true);
		} else {
			tagReq.setMessageStoreTag(false);
		}		
		
		command.controlTag(tagReq);
	}
	
	public int getTagSortMessageTotal(
			MessageSortInfoBean sortBean, 
			int tagId) throws MessagingException{
		int total = 0;
		
		sortBean.setPage("1");
		sortBean.setPageBase("1");
		SortMessage[] msgs = getSortTagMessage(sortBean, tagId);
		if(msgs != null){
			total = command.getSortTotal();
		}
		
		return total;		
	}
	
	public MailSortMessageBean[] getTagSortMessageBeans(
			MessageSortInfoBean sortBean,
			int tagId) 
			throws MessagingException{
		
		MailSortMessageBean[] beans = null;
		SortMessage[] msgs = getSortTagMessage(sortBean, tagId);
		if(msgs != null){
			beans = new MailSortMessageBean[msgs.length];
			for (int i = 0; i < msgs.length; i++) {
				beans[i] = new MailSortMessageBean(msgs[i],locale);				
			}		
		}
		
		total = command.getSortTotal();
		
		return beans;
	}
	
	public SortMessage[] getSortTagMessage(MessageSortInfoBean sortBean, 
			int tagId) throws MessagingException {		
		
		TMailTag currentTag = new TMailTag();
		currentTag.setId(tagId);		
		
		TagRequest tagReq = new TagRequest(XTagCommand.CMD_TAGSORTLIST);
		tagReq.setPage(Integer.parseInt(sortBean.getPage()));
		tagReq.setPageBase(sortBean.getPageBaseSize());
		tagReq.setOrder(sortBean.getMessageSortBy(), sortBean.getMessageSortDir());
		tagReq.setCurrentTag(currentTag);		
		
		
		return command.getSortTagMessage(tagReq);		
	}
	
	public TMailSearchQuery[] getSearchQueryList(String searchPattern) 
	throws MessagingException{
		return getSearchQueryList(searchPattern, 0, 0);
	}
	
	public TMailSearchQuery[] getSearchQueryList(String searchPattern, 
			int page, int pageBase) throws MessagingException{
		
		SearchRequest searchReq = new SearchRequest(XSearchCommand.CMD_QUERYLIST);
		searchReq.setPattern(searchPattern);
		searchReq.setPage(page);
		searchReq.setPageBase(pageBase);
		
		return command.getSearchQueryList(searchReq);
	}
	
	public void addSearchQuery(TMailSearchQuery newQuery) throws MessagingException {
		SearchRequest searchReq = 
			new SearchRequest(XSearchCommand.CMD_CREATE);
		searchReq.setNewQuery(newQuery);
		
		command.controlSearchQuery(searchReq);
	}
	
	public void deleteSearchQuery(String[] sids) throws MessagingException {
		SearchRequest searchReq = 
			new SearchRequest(XSearchCommand.CMD_DELETE);
		searchReq.setWorkIds(sids);
		
		command.controlSearchQuery(searchReq);
	}
	public void modSearchQuery(String oldId, TMailSearchQuery modQuery) 
	throws MessagingException {
		SearchRequest searchReq = new SearchRequest(XSearchCommand.CMD_MODIFY);
		searchReq.setWorkIds(new String[]{oldId});
		searchReq.setNewQuery(modQuery);
		
		command.controlSearchQuery(searchReq);
	}
	
}
