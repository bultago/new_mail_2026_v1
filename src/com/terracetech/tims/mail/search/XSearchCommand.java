/**
 * XSearchCommand.java 2009. 1. 13.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.mail.search;

import java.util.Vector;

import org.eclipse.angus.mail.iap.Argument;
import org.eclipse.angus.mail.iap.ProtocolException;
import org.eclipse.angus.mail.iap.Response;
import org.eclipse.angus.mail.imap.IMAPFolder.ProtocolCommand;
import org.eclipse.angus.mail.imap.protocol.IMAPProtocol;
import com.terracetech.tims.mail.TMailUtility;

/**
 * <p><strong>XSearchCommand.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
@SuppressWarnings("unchecked")
public class XSearchCommand implements ProtocolCommand {
	
	public static final int CMD_QUERYLIST = 0;
	public static final int CMD_CREATE = 1;
	public static final int CMD_DELETE = 2;
	public static final int CMD_MODIFY = 3;
	
	private SearchRequest req = null;
	private int cmd = -1;
	
	/**
	 * <p></p>
	 *
	 */
	public XSearchCommand(SearchRequest req) {
		this.req = req;
		this.cmd = req.getCmd();
	}
	/**
	 * <p></p>
	 *
	 * @see com.sun.mail.imap.IMAPFolder.ProtocolCommand#doCommand(com.sun.mail.imap.protocol.IMAPProtocol)
	 * @param protocol
	 * @return
	 * @throws ProtocolException 
	 */
	public Object doCommand(IMAPProtocol protocol) throws ProtocolException {
		Object obj = null;
		
		switch (cmd) {
			case CMD_QUERYLIST:
				obj = getSearchQueryList(protocol);
				break;				
			case CMD_CREATE :
				makeSearchQuery(protocol,false);
				break;
			case CMD_DELETE :
				deleteSearchQuery(protocol,false);
				break;				
			case CMD_MODIFY :
				modifySearchQuery(protocol);
				break;
			default:
				throw new ProtocolException("Unknown Command!! ");
				
		}
		
		return obj;
	}
	
	private Object getSearchQueryList(IMAPProtocol protocol) throws ProtocolException {		
		
		Argument args = new Argument();
		args.writeAtom(Integer.toString(req.getPage()));
		args.writeAtom(Integer.toString(req.getPageBase()));
		if(req.getPattern() != null){
			args.writeString(req.getPattern());
		}
		Response[] r = protocol.command("X-LIST-SEARCH ", args);
		Response response = r[r.length - 1];

		Vector v = new Vector();
		try {
			if (response.isOK()) {
				TMailSearchQuery query = null;
				for (int i = 0, len = r.length-1; i < len; i++) {
					try {
						query = new TMailSearchQuery();
						query.setId(r[i].readNumber());
						query.setName(TMailUtility.IMAPFolderDecode(r[i].readString()));						
						r[i].skipSpaces();
						r[i].skip(1);
						r[i].skipToken();									
						query.setQuery(TMailUtility.IMAPFolderDecode(r[i].readString()));
						v.addElement(query);															
					}catch (Exception e) {
						throw new ProtocolException(e.getMessage());
					}	
				}
			}
		}  finally {
			protocol.notifyResponseHandlers(r);
			protocol.handleResult(response);
			args = null;
		}
		
		return v;
	}
	
	private void makeSearchQuery(IMAPProtocol protocol, boolean stable) throws ProtocolException {
		TMailSearchQuery nquery = req.getNewQuery();
		Argument args = new Argument();		
		args.writeAtom("\""+TMailUtility.IMAPFolderEncode(nquery.getName())+"\"");
		Argument query = new Argument();
		query.writeAtom("query");
		query.writeAtom("\""+TMailUtility.IMAPFolderEncode(nquery.getQuery())+"\"");		
		args.writeArgument(query);
		
		Response[] r = protocol.command("X-CREATE-SEARCH ", args);
		Response response = r[r.length - 1];
		try {
			if (!response.isOK()) {
				throw new ProtocolException("Fail Make Search!!");
			}
		}  finally {
			if(!stable){
				protocol.notifyResponseHandlers(r);
				protocol.handleResult(response);
			}
			nquery = null;
			args = null;
		}
	}
	
	private void deleteSearchQuery(IMAPProtocol protocol, boolean stable) throws ProtocolException {		
		Argument args = new Argument();		
		args.writeAtom(req.getWorkIds());
		
		Response[] r = protocol.command("X-DELETE-SEARCH ", args);
		Response response = r[r.length - 1];
		try {
			if (!response.isOK()) {
				throw new ProtocolException("Fail Delete Search!!");
			}
		}  finally {
			if(!stable){
				protocol.notifyResponseHandlers(r);
				protocol.handleResult(response);
			}	
			args = null;
		}
	}
	
	private void modifySearchQuery(IMAPProtocol protocol) throws ProtocolException {
		deleteSearchQuery(protocol,true);
		makeSearchQuery(protocol,false);		
	}
	
	
}
