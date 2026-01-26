package com.terracetech.tims.webmail.webfolder.manager;

import java.util.Arrays;

import jakarta.mail.Flags;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.mail.ibean.MailFolderBean;

public class WebfolderImapHandler {
	
	private TMailStore store = null;
	
	public WebfolderImapHandler(TMailStore store) {
		this.store = store;
	}
	
	public MailFolderBean[] getDefaultWebfolder() throws MessagingException {
		
		TMailFolder folder = store.getDefaultWebFolder();
		TMailFolder[] folders = folder.list();
		Arrays.sort(folders);
		MailFolderBean[] folderBeans = new MailFolderBean[folders.length];
		for (int i = 0; i < folders.length; i++) {
			folderBeans[i] = new MailFolderBean(folders[i]);
		}
		return folderBeans;
	}
	
	public MailFolderBean[] getWebfolder(String xpath) throws MessagingException {
		TMailFolder folder = store.getWebFolder(xpath);
		TMailFolder[] folders = folder.list();
		Arrays.sort(folders);
		MailFolderBean[] folderBeans = new MailFolderBean[folders.length];
		for (int i = 0; i < folders.length; i++) {
			folderBeans[i] = new MailFolderBean(folders[i]);
		}
		return folderBeans;
	}
	
	public void copyFolders(String[] fids, TMailFolder fromFolder, TMailFolder toFolder) throws MessagingException, Exception {
		if (fids != null) {        	
            for (int i = 0; i < fids.length; i++) {
                
            	String xfid = new String(fids[i].getBytes("8859_1"), "UTF-8");
            	
            	if (xfid.length() > 0){
            		
            		TMailFolder src = fromFolder.getFolder(xfid);
            		TMailFolder dst = toFolder.getFolder(xfid);
	                TMailFolder[] folders = src.list();
	                
	                for (int j = 0; j < folders.length; i++) {
	                	
	                	if(dst.getFolderDepth() >= 5){
	        	    		throw new MessagingException("over deth count!!!");
	        	    	}
	                	
	                	TMailFolder target = toFolder.getFolder(folders[i].getName());
	                	target.create();
	                }  
	                copyChildFolders(src, dst);
            	}	   
            }
		}
	}
	
	public void moveFolders(String[] fids, TMailFolder fromFolder, TMailFolder toFolder) throws MessagingException, Exception {
		if (fids != null) {        	
            for (int i = 0; i < fids.length; i++) {
                
            	String xfid = new String(fids[i].getBytes("8859_1"), "UTF-8");
            	
            	if (xfid.length() > 0){
            		
            		TMailFolder src = fromFolder.getFolder(xfid);
            		TMailFolder dst = toFolder.getFolder(xfid);
	                TMailFolder[] folders = src.list();
	                
	                for (int j = 0; j < folders.length; i++) {
	                	
	                	if(dst.getFolderDepth() >= 5){
	        	    		throw new MessagingException("over deth count!!!");
	        	    	}
	                	
	                	TMailFolder target = toFolder.getFolder(folders[i].getName());
	                	target.create();
	                }  
	                moveChildFolders(src, dst);
            	}	   
            }
		}
	}
	
	public void copyChildFolders(TMailFolder fromFolder, TMailFolder toFolder) throws MessagingException {
        // copy folders
		TMailFolder[] folders = fromFolder.list();

        for (int i = 0; i < folders.length; i++) {
        	TMailFolder dst = toFolder.getFolder(folders[i].getName());
        	dst.create();
        	
            copyChildFolders(folders[i], dst);
        }
	}
	
	public void copyFilesAll(TMailFolder fromFolder, TMailFolder toFolder) throws MessagingException {
		// copy files
        fromFolder.open(true);
        TMailMessage[] messages = fromFolder.getMessages();
                
        if (messages.length > 0) {        	
        	fromFolder.copyMessages(messages, toFolder);
        }
        fromFolder.close(true);
	}
	
	public void moveChildFolders(TMailFolder fromFolder, TMailFolder toFolder) throws MessagingException {
		// move folders
		TMailFolder[] folders = fromFolder.list();

        for (int i = 0; i < folders.length; i++) {
        	TMailFolder src = fromFolder.getFolder(folders[i].getName());
        	TMailFolder dst = toFolder.getFolder(folders[i].getName());
        	
        	dst.create();
            moveChildFolders(folders[i], dst);
            src.delete();
        }
	}
	
	public void moveFiles(TMailFolder fromFolder, TMailFolder toFolder, String[] uids) throws MessagingException {
		// move files
        if (uids != null) {
            long luids[] = new long[uids.length];

            for (int i = 0; i < luids.length; i++) {
                luids[i] = Long.parseLong(uids[i]);
            }

            fromFolder.open(true);
            fromFolder.xmove(luids, toFolder.getFullName());
            fromFolder.close(true);
        }
	}
	
	public void moveFilesAll(TMailFolder fromFolder, TMailFolder toFolder) throws MessagingException {
		// move files
        fromFolder.open(true);
        TMailMessage[] messages = fromFolder.getMessages();

        if (messages.length > 0) {
            long[] luids = new long[messages.length];

            for (int i = 0; i < luids.length; i++) {
                luids[i] = fromFolder.getUID(messages[i]);
            }

            fromFolder.xmove(luids, toFolder.getFullName());
        }

        fromFolder.close(true);
	}
	
	public void deleteFolders(TMailFolder fromFolder, String[] fids) throws MessagingException, Exception {
		if (fids != null) {
            for (int i = 0; i < fids.length; i++) {
                String xfid = new String(fids[i].getBytes("8859_1"), "UTF-8");
                fromFolder.getFolder(xfid).delete();
            }
        }
	}
	
	public void deleteFiles(TMailFolder fromFolder, String[] uids) throws MessagingException {
		if (uids != null) {
            long luids[] = new long[uids.length];

            for (int i = 0; i < luids.length; i++) {
                luids[i] = Long.parseLong(uids[i]);
            }

            fromFolder.open(true);
            TMailMessage[] messages = fromFolder.getMessagesByUID(luids);
            fromFolder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
            fromFolder.close(true);
        }
	}
	
	public void uploadFile(TMailFolder folder, MimeMessage message) throws MessagingException {
		folder.appendMessages(new TMailMessage[]{new TMailMessage(message)});
	}
}
