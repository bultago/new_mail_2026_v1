package com.terracetech.tims.webmail.note.manager;

import java.io.UnsupportedEncodingException;

import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.util.StringUtils;

public class NoteHandler {

	public static final char SEEN_FLAG = 'S';
	public static final char UNSEEN_FLAG = 'U';
	public static final char FLAGED_FLAG = 'F';
	public static final String INBOX = "Inbox";
	public static final String SENT = "Sent";
	public static final String SAVE = "Save";
	
	private TMailStore store = null;
	private I18nResources msgResource = null;
	
	public void setStore(TMailStore store) {
		this.store = store;
	}
	
	public void setMsgResource(I18nResources msgResource) {
		this.msgResource = msgResource;
	}

	public void createDefaultFolders() {
		try {
			if (!store.getFolder(INBOX).exists()) store.getFolder(INBOX).create();
			if (!store.getFolder(SENT).exists()) store.getFolder(SENT).create();
			if (!store.getFolder(SAVE).exists()) store.getFolder(SAVE).create();
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage());
		}
	}
	
	public int readNoteUnseenCount() {
		int unSeenCount = 0;
		try {
			unSeenCount = store.getFolder(INBOX).getUnreadMessageCount();
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage());
		}
		
		return unSeenCount;
	}
	
	public TMailFolder getFolder(String folderName) throws MessagingException {
		return setAliasMessage(store.getFolder(folderName));
	}
	
	private TMailFolder setAliasMessage(TMailFolder folder) {
		String folderName = folder.getFullName();
		if (INBOX.equalsIgnoreCase(folderName)) {
			folder.setAlias(msgResource.getMessage("note.msg.001"));
		} else if (SENT.equalsIgnoreCase(folderName)) {
			folder.setAlias(msgResource.getMessage("note.msg.002"));
		} else if (SAVE.equalsIgnoreCase(folderName)) {
			folder.setAlias(msgResource.getMessage("note.msg.003"));
		}
		return folder;
	}
	
	public void changeFlag(TMailMessage message, char flagType, boolean use) throws MessagingException {
		Flags flag = null;
		if (flagType == SEEN_FLAG) {
			flag = new Flags(javax.mail.Flags.Flag.SEEN);
		} else if (flagType == FLAGED_FLAG) {
			flag = new Flags(javax.mail.Flags.Flag.FLAGGED);
		}
		
		if(flag == null){
			throw new MessagingException("FLAG IS NULL!!");
		}
		
		message.setFlags(flag, use);
	}
	
	public void setMDNFlag(InternetAddress mdnAddr, TMailFolder folder, String messageId, String charset) 
	throws MessagingException, UnsupportedEncodingException{		

		folder.xaddMDN(messageId);

		String personal = null;
		String address = null;	
		
		folder.xsetMDNOpen();		
	
		address = mdnAddr.getAddress();
		personal = mdnAddr.getPersonal();
		personal = (personal != null) ? MimeUtility.encodeText(personal, charset, "B") : "";
		personal = StringUtils.getCRLFEscapeOnly(personal);
		try {
			folder.xsetMDNAddCode(messageId, address, personal, 0, "300");
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		
		folder.xsetMDNClose();		
		personal = null;
		address = null;		
	}
	
	public long[] getNeighborUIDs(long uid, TMailFolder folder, MessageSortInfoBean infoBean)
	throws MessagingException {
		folder.setSort(infoBean.getMessageSortBy(), infoBean.getMessageSortDir());
		return folder.getNeighborUIDs(uid, infoBean.getSearchTerm());
	}
	
	public void moveMessageToFolder(long[] uids, String fromFolderName,
			String toFolderName) throws MessagingException {

		TMailFolder fromFolder = store.getFolder(fromFolderName);

		fromFolder.open(true);
		
		TMailMessage[] messages = fromFolder.getMessagesByUID(uids);
		if (messages != null && messages.length > 0) {
			boolean isFlag = (SENT.equalsIgnoreCase(fromFolderName)) ? true : false;
			for (TMailMessage message : messages) {
				if (isFlag) {
					changeFlag(message, FLAGED_FLAG, true);
				}
			}
		}
		fromFolder.xmove(uids, toFolderName);

		fromFolder.close(true);
	}
	
	public void deleteNoteMessage(long[] uids, TMailFolder folder) throws MessagingException {
		folder.open(true);
		TMailMessage[] messages = folder.getMessagesByUID(uids);
		folder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
		folder.close(true);
	}
}
