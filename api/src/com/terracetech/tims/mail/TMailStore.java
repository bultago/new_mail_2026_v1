package com.terracetech.tims.mail;

import jakarta.mail.*;

import org.eclipse.angus.mail.imap.*;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class TMailStore {

	public static final String WEBFOLDER_ROOT	= "WEBFOLDERROOT";
	public static String[] DEFAULT_FOLDERS		= null;
    Store store = null;
    
    public TMailStore(Store store) {
        this.store = store;        
		setDefaultFolders();
    }

	public void setDefaultFolders() {
		if (DEFAULT_FOLDERS == null) {
			String folders = "Inbox,Sent,Drafts,Reserved,Trash,Spam";

			InputStream is = 
				TMailStore.class.getResourceAsStream("/mailapi.properties");

			if (is != null) {
				Properties props = new Properties();
				try {
					props.load(is);
					folders = props.getProperty("default_folders", folders);
				} catch (IOException ex) { }
			}

			DEFAULT_FOLDERS = folders.split("[,|:; ]");
			System.out.println("-- default_folders : ["+folders+"]");
		}
	}

    public void close() throws MessagingException {
        store.close();
    }

    public void connect(String host, String user, String password)
        throws MessagingException {
    	
        store.connect(host, user, password);
    }

    public void connect(String host, int port, String user, String password)
        throws MessagingException {

        store.connect(host, port, user, password);
    }

    public boolean isConnected() throws MessagingException {
        return store.isConnected();
    }

    public TMailFolder getDefaultFolder() throws MessagingException {
        return new TMailFolder(store.getDefaultFolder());
    }

    public TMailFolder getDefaultWebFolder() throws MessagingException {
        Folder folder = store.getFolder(WEBFOLDER_ROOT);

        if (!folder.exists()) {
            folder.create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES);
        }

        return new TMailFolder(folder);
    }

    public TMailFolder getFolder(String name)
        throws MessagingException {

		return new TMailFolder(
				store.getFolder(TMailUtility.IMAPFolderDecode(name)));
    }

	public TMailFolder getWebFolder(String name) throws MessagingException {
		if ( TMailUtility.isNull(name) || name.equals("/") ) {
			return getDefaultWebFolder();
		} else if ( name.startsWith(WEBFOLDER_ROOT) ) {
			return getFolder(name);
		} else {
			return getDefaultWebFolder().getFolder(
					TMailUtility.IMAPFolderDecode(name));
		}
	}

    public Quota[] getQuota(String root) throws MessagingException {
        return ((IMAPStore) store).getQuota(root);
    }

	public void createDefaultFolders() throws MessagingException {
		for (int i = 0; i < DEFAULT_FOLDERS.length; i++) {
			if (TMailUtility.isNull(DEFAULT_FOLDERS[i])) {
				continue;
			}
			getFolder(DEFAULT_FOLDERS[i]).create();
		}
	}

	public TMailFolder[] getFolders() throws MessagingException {
		TMailFolder[] myFolders = getDefaultFolder().list("*");

		TMailUtility.folderSort(myFolders);

		return myFolders;
	}

	public TMailFolder[] getFoldersStatus() throws MessagingException {
		TMailFolder[] myFolders = getFolders();

		for (int i = 0; i < myFolders.length; i++) {
			myFolders[i].status(new String[]{"RECENT", "UNSEEN", "MESSAGES", "X-DISKUSAGE"});
		}

		return myFolders;
	}

	public TMailFolder[] getWebfolderFolders() throws MessagingException {
		TMailFolder[] myFolders = getDefaultFolder().list("*");

		TMailUtility.webfolderFolderSort(myFolders);

		return myFolders;
	}

	public TMailFolder[] getWebfolderFoldersStatus() throws MessagingException {
		TMailFolder[] myFolders = getWebfolderFolders();

		for (int i = 0; i < myFolders.length; i++) {
			myFolders[i].status(new String[]{"RECENT", "UNSEEN", "MESSAGES", "X-DISKUSAGE"});
		}

		return myFolders;
	}
}

