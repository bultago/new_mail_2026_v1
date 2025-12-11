/**
 * XAllSortCommand.java 2009. 1. 9.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.mail.sort;

import java.util.Vector;

import org.eclipse.angus.mail.iap.Argument;
import org.eclipse.angus.mail.iap.ProtocolException;
import org.eclipse.angus.mail.iap.Response;
import org.eclipse.angus.mail.imap.IMAPFolder;
import org.eclipse.angus.mail.imap.protocol.IMAPProtocol;
import org.eclipse.angus.mail.imap.protocol.IMAPResponse;

/**
 * <p>
 * <strong>XAllSortCommand.java</strong> Class Description
 * </p>
 * <p>
 * 주요설명
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
public class XAllSortCommand implements IMAPFolder.ProtocolCommand {

	private SortRequest req = null;
	private int total = 0;
	
	public XAllSortCommand(SortRequest req) {
		this.req = req;			
	}

	public Object doCommand(IMAPProtocol p) throws ProtocolException {
		
		// Issue command
		Argument args = new Argument();
		String[] exceptFolders = req.getExceptFolders();
		String searchFolder = req.getSearchFolder();
		
		args.writeString(Integer.toString(req.getPage()));
		args.writeString(Integer.toString(req.getPageBase()));
		
		Argument eFolderAgs = new Argument();
		if(exceptFolders != null){
			for (int i = 0; i < exceptFolders.length; i++) {
				eFolderAgs.writeAtom("\""+exceptFolders[i]+"\"");
			}
		} 
		args.writeArgument(eFolderAgs);
		
		Argument sFolderAgs = new Argument();
		if(searchFolder != null){
			sFolderAgs.writeAtom("\""+searchFolder+"\"");
		}		
		args.writeArgument(sFolderAgs);
		
		Argument order = new Argument();
		order.writeAtom(req.getOrderValue());		
		args.writeArgument(order);
		
		args.writeString("UTF-8");
		args.append(req.getCondition());
		
		if(req.isRelationSearch()){
			args.writeString("X-THREAD");
			args.writeString(req.getRelationFolderName());
			args.writeString(req.getRelationUid());
		}		
		
		Response[] r = p.command("UID X-ALL-SORT", args);
		Response response = r[r.length - 1];
		
		// Grab response
		Vector v = new Vector();
		try {
			if (response.isOK()) { // command succesful
				for (int i = 0, len = r.length-1; i < len; i++) {
					if (!(r[i] instanceof IMAPResponse)) {
						continue;
					}
					try {
						XAllSortResponse sr = new XAllSortResponse((IMAPResponse) r[i]);
						v.addElement(new SortMessage(sr));					
					}catch (Exception e) {
						throw new ProtocolException(e.getMessage());
					}				
				}
			}		
			String totalStr = response.readAtom();
			if(totalStr != null){
				this.total = Integer.parseInt(totalStr);
			}
		} finally {
			// dispatch remaining untagged responses
			p.notifyResponseHandlers(r);
			p.handleResult(response);
			exceptFolders = null;
			searchFolder = null;
			args = null;
		}		

		return v;
	}
	
	public int getSortTotal(){
		return total;
	}

}
