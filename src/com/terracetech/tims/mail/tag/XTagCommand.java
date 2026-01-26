/**
 * XTagCommand.java 2009. 1. 12.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.mail.tag;

import java.util.Vector;

import org.eclipse.angus.mail.iap.Argument;
import org.eclipse.angus.mail.iap.ProtocolException;
import org.eclipse.angus.mail.iap.Response;
import org.eclipse.angus.mail.imap.IMAPFolder.ProtocolCommand;
import org.eclipse.angus.mail.imap.protocol.IMAPProtocol;
import org.eclipse.angus.mail.imap.protocol.IMAPResponse;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.mail.sort.SortMessage;
import com.terracetech.tims.mail.sort.XAllSortResponse;

/**
 * <p><strong>XTagCommand.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
@SuppressWarnings("unchecked")
public class XTagCommand implements ProtocolCommand {	
	
	public static final int CMD_TAGLIST = 0;
	public static final int CMD_CREATE = 1;
	public static final int CMD_DELETE = 2;
	public static final int CMD_MODIFY = 3;
	public static final int CMD_STORE = 4;
	public static final int CMD_TAGSORTLIST = 5;
	
	private int cmd = -1;
	private TagRequest req = null;
	private int total = 0;
	
	/**
	 * <p></p>
	 *
	 */
	public XTagCommand(TagRequest req){
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
			case CMD_TAGLIST:
				obj = getTagList(protocol);
				break;				
			case CMD_CREATE :
				makeTag(protocol);
				break;
			case CMD_DELETE :
				deleteTag(protocol);
				break;				
			case CMD_MODIFY :
				modifyTag(protocol);
				break;				
			case CMD_STORE :
				storeTag(protocol);
				break;
				
			case CMD_TAGSORTLIST :
				obj = getSortMessageForTag(protocol);
				break;
	
			default:
				throw new ProtocolException("Unknown Command!! ");
				
		}
		
		return obj;
	}
	
	private Object getTagList(IMAPProtocol protocol) throws ProtocolException {		
		
		Argument args = new Argument();
		args.writeString(Integer.toString(req.getPage()));
		args.writeString(Integer.toString(req.getPageBase()));
		if(req.getSearchPattern() != null){
			args.writeString(req.getSearchPattern());
		}
		
		Response[] r = protocol.command("X-LIST-TAG ", args);
		Response response = r[r.length - 1];

		Vector v = new Vector();
		try {
			if (response.isOK()) {
				TMailTag tag = null;
				for (int i = 0, len = r.length-1; i < len; i++) {
					try {
						tag = new TMailTag();
						tag.setId(r[i].readNumber());
						tag.setName(TMailUtility.IMAPFolderDecode(r[i].readString()));
						tag.setColor(r[i].readString());
						v.addElement(tag);															
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
	
	private void makeTag(IMAPProtocol protocol) throws ProtocolException {
		TMailTag ntag = req.getNewTag();
		Argument args = new Argument();		
		args.writeAtom("\""+TMailUtility.IMAPFolderEncode(ntag.getName())+"\"");
		args.writeAtom("\""+ntag.getColor()+"\"");		
		Response[] r = protocol.command("X-CREATE-TAG ", args);
		Response response = r[r.length - 1];
		try {
			if (!response.isOK()) {
				throw new ProtocolException("Fail Make Tag!!");
			}
		}  finally {
			protocol.notifyResponseHandlers(r);
			protocol.handleResult(response);
			ntag = null;
			args = null;
		}
	}
	
	private void deleteTag(IMAPProtocol protocol) throws ProtocolException {		
		Argument args = new Argument();	
		args.writeAtom(req.getWorkIds());
		
		Response[] r = protocol.command("X-DELETE-TAG ", args);
		Response response = r[r.length - 1];		
		try {
			if (!response.isOK()) {
				throw new ProtocolException("Fail Delete Tag!!");
			}
		}  finally {
			protocol.notifyResponseHandlers(r);
			protocol.handleResult(response);
			args = null;
		}
		
	}
	
	private void modifyTag(IMAPProtocol protocol) throws ProtocolException {		
		TMailTag ntag = req.getNewTag();
		TMailTag ctag = req.getCurrentTag();
		
		Argument args = new Argument();
		args.writeAtom(Integer.toString(ctag.getId()));
		
		if(ntag.getName() != null){
			args.writeAtom("\""+TMailUtility.IMAPFolderEncode(ntag.getName())+"\"");
		} else {
			args.writeAtom("\"\"");
		}
		
		if(ntag.getColor() != null){
			args.writeAtom("\""+ntag.getColor()+"\"");
		} else {
			args.writeAtom("\"\"");
		}		
		Response[] r = protocol.command("X-MOD-TAG ", args);
		Response response = r[r.length - 1];
		
		try {
			if (!response.isOK()) {
				throw new ProtocolException("Fail Modify Tag!!");
			}
		}  finally {
			protocol.notifyResponseHandlers(r);
			protocol.handleResult(response);
			ctag = null;
			ntag = null;
			args = null;
		}
		
	}
	
	private void storeTag(IMAPProtocol protocol) throws ProtocolException {
		TMailTag ctag = req.getCurrentTag();
		Argument args = new Argument();
				
		if(req.isMessageStoreTag()){
			args.writeAtom("+");
		} else {
			args.writeAtom("-");
		}
		
		args.writeAtom(Integer.toString(ctag.getId()));
		args.writeAtom("\""+req.getStoreFolderName()+"\"");
		args.writeAtom(req.getStoreUid());
				
		Response[] r = protocol.command("UID X-STORE-TAG ", args);
		Response response = r[r.length - 1];
		
		try {
			if (!response.isOK()) {
				throw new ProtocolException("Fail Store Tag!!");
			}	
		}  finally {
			protocol.notifyResponseHandlers(r);
			protocol.handleResult(response);
			ctag = null;
			args = null;
		}
		
	}	
	private Object getSortMessageForTag(IMAPProtocol protocol) throws ProtocolException {
		TMailTag ctag = req.getCurrentTag();
		Vector v = new Vector();
		Argument args = new Argument();
		args.writeAtom(Integer.toString(req.getPage()));
		args.writeAtom(Integer.toString(req.getPageBase()));
		
		Argument order = new Argument();
		order.writeAtom(req.getOrder());		
		args.writeArgument(order);
		
		args.writeAtom(Integer.toString(ctag.getId()));
		
		Response[] r = protocol.command("X-SORT-TAG ", args);
		Response response = r[r.length - 1];
		
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
		}  finally {
			protocol.notifyResponseHandlers(r);
			protocol.handleResult(response);
			ctag = null;
			args = null;
		}		
		
		return v;
	}
	
	public int getSortTotal(){
		return total;
	}
}
