/**
 * TMailXCommand.java 2009. 1. 15.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.mail;

import java.util.Vector;
import jakarta.mail.MessagingException;

import org.eclipse.angus.mail.imap.IMAPFolder;
import com.terracetech.tims.mail.search.SearchRequest;
import com.terracetech.tims.mail.search.TMailSearchQuery;
import com.terracetech.tims.mail.search.XSearchCommand;
import com.terracetech.tims.mail.sort.SortMessage;
import com.terracetech.tims.mail.sort.SortRequest;
import com.terracetech.tims.mail.sort.XAllSortCommand;
import com.terracetech.tims.mail.tag.TMailTag;
import com.terracetech.tims.mail.tag.TagRequest;
import com.terracetech.tims.mail.tag.XTagCommand;

/**
 * <p><strong>TMailXCommand.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class TMailXCommand {
	
	private IMAPFolder folder = null;
	private int total = 0;
	
	public TMailXCommand(IMAPFolder folder) {
		this.folder = folder;
	}	
	
	public SortMessage[] getXSortMessage(SortRequest sortReq) throws MessagingException{
		SortMessage[] messages = null;
		XAllSortCommand cmd = new XAllSortCommand(sortReq);		
		Vector v = (Vector) folder.doCommand(cmd);
		
		if(v.size() > 0){
			messages = new SortMessage[v.size()];
			v.copyInto(messages);
		}
		
		total = cmd.getSortTotal();		
		return messages;
	}
	
	public TMailTag[] getTagList(TagRequest tagReq) 
	throws MessagingException{
		TMailTag[] tagList = null;	
			
		XTagCommand cmd = new XTagCommand(tagReq);
		Vector v = (Vector) folder.doCommand(cmd);
		
		if(v.size() > 0){
			tagList = new TMailTag[v.size()];
			v.copyInto(tagList);
		}
		
		return tagList;
	}
	
	public void controlTag(TagRequest tagReq) throws MessagingException {			
		XTagCommand cmd = new XTagCommand(tagReq);
		folder.doCommand(cmd);
	}
	
	public SortMessage[] getSortTagMessage(TagRequest tagReq) 
	throws MessagingException {
		
		SortMessage[] messages = null;		
		
		XTagCommand cmd = new XTagCommand(tagReq);
		Vector v = (Vector) folder.doCommand(cmd);
		
		if(v.size() > 0){
			messages = new SortMessage[v.size()];
			v.copyInto(messages);
		}
		total = cmd.getSortTotal();			
		return messages;
		
	}
	
	public TMailSearchQuery[] getSearchQueryList(SearchRequest searchReq) 
	throws MessagingException{		
	
		TMailSearchQuery[] queryList = null;	
		
		XSearchCommand cmd = new XSearchCommand(searchReq);
		Vector v = (Vector)folder.doCommand(cmd);
		
		if(v.size() > 0){
			queryList = new TMailSearchQuery[v.size()];
			v.copyInto(queryList);
		}
		return queryList;
	}
	
	public void controlSearchQuery(SearchRequest searchReq) throws MessagingException {		
		XSearchCommand cmd = new XSearchCommand(searchReq);
		folder.doCommand(cmd);
	}
	
	public int getSortTotal(){
		return total;
	}	
	
}
