package com.terracetech.tims.mail;

import java.io.*;
import java.util.*;
import java.text.*;
import java.net.URLEncoder;
import jakarta.mail.*;
import jakarta.mail.search.*;
import jakarta.mail.internet.*;
import org.eclipse.angus.mail.iap.*;
import org.eclipse.angus.mail.imap.*;
import org.eclipse.angus.mail.imap.protocol.*;

@SuppressWarnings("all")
public class TMailFolder implements Comparable {

    public static final int SORT_ARRIVAL    = 0;
    public static final int SORT_CC         = 1;
    public static final int SORT_DATE       = 2;
    public static final int SORT_FROM       = 3;
    public static final int SORT_SIZE       = 4;
    public static final int SORT_SUBJECT    = 5;
    public static final int SORT_ASCENDING  = 6;
    public static final int SORT_DESCENDING = 7;
    public static final int SORT_TO			= 8;

	public static boolean isCHKMDN		= false;
	public static boolean isMDN2		= false;

    public static final String WEBFOLDER_ROOT	= "WEBFOLDERROOT";

    private int index 		= -1;
	private Folder folder 	= null;
    private String name 	= null;
    private String alias 	= null;
    private String encName 	= null;
    private int sortKey 	= SORT_ARRIVAL;
    private int sortDirection = SORT_DESCENDING;

    private int cachedSortedMessageCount = -1;
    private int cachedUnreadMessageCount = -1;
    private Map cachedStatus = null;
    private int mdnTotalCnt = 0;

    public TMailFolder(Folder folder) {
        this.folder = folder;
    }

	public Folder getIMAPFolder(){
		return folder;
	}

    public void appendMessages(TMailMessage[] msgs) throws MessagingException {
        Message[] messages = new Message[msgs.length];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = msgs[i].message;
        }
        folder.appendMessages(messages);
    }

    /**
     * WebFolder �� Binary Message Append Method �߰�
     * Add by hkkim for WEBFOLDER - 2008.01.29
     */
    public void appendMessagesBinary(TMailMessage[] msgs) throws MessagingException {
        Message[] messages = new Message[msgs.length];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = msgs[i].message;
        }
        folder.appendMessagesBinary(messages);
    }
    
    /**
     * ��뷮 file�� Binary Message Append Method �߰�
     * Add by sshyun - 2010.05.10
     */
    public void appendDirectMessagesBinary(TMailMessage[] msgs,File[] files) throws MessagingException {
        Message[] messages = new Message[msgs.length];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = msgs[i].message;
        }
        ((IMAPFolder)folder).appendDirectMessagesBinary(messages, files);
    }

    public void close(boolean expunge) throws MessagingException {
		if (isOpen()) {
			folder.close(expunge);
		}
	}

	public boolean isOpen() throws MessagingException {
        return folder.isOpen();
    }

	public boolean isSubscribed() throws MessagingException {
        return folder.isSubscribed();
    }

    public int getType() throws MessagingException {
        return folder.getType();
    }

    public TMailStore getStore() throws MessagingException {
		return new TMailStore(folder.getStore());
    }

	public TMailFolder getParent() throws MessagingException {
		return new TMailFolder(folder.getParent());
    }

    public int compareTo(Object o) {
    	 TMailFolder peer = (TMailFolder) o;
         String fullName = folder.getFullName().replaceAll("\\.", "\\!");
         String peerFullName = peer.getFullName().replaceAll("\\.", "\\!");
 		
 		return fullName.compareTo(peerFullName);
    }

    public void copyMessages(TMailMessage[] msgs, TMailFolder folder)
        throws MessagingException {

        Message[] messages = new Message[msgs.length];

        for (int i = 0; i < messages.length; i++) {
            messages[i] = msgs[i].message;
        }

        this.folder.copyMessages(messages, folder.folder);
    }

    public boolean exists() throws MessagingException {
        return folder.exists();
    }

    public boolean create() throws MessagingException {
		if (!exists()) {
        	return folder.create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES);
		}

		return true;
    }

    public boolean delete() throws MessagingException {
		if (exists()) {
			return folder.delete(true);
		}

		return true;
    }

    public void expunge() throws MessagingException {
		if (exists()) {
			folder.expunge();
		}
    }

    public Map getStatusHT() {
        return cachedStatus;
    }

    public String getAlias() {
        return alias;
    }

	/* 
	 * �θ�������� �����Ѵ�. (a.b.c => a.b�� ����) 
	 */ 
	public String getAlias4parentFolder() { 
		String str = ""; 
		int dotIdx = alias.lastIndexOf("."); 
		if (dotIdx > 0) { 
			str = alias.substring(0, dotIdx); 
		} 
		return str; 
	} 

	/* 
	 * ����������� �����Ѵ�. (a.b.c => c�� ����) 
	 */ 
	public String getAlias4subFolder() {
		String str = alias; 
		int dotIdx = str.lastIndexOf("."); 
		if (dotIdx > 0) { 
			str = str.substring(dotIdx + 1, str.length()); 
		} 
		return str; 
	}

	public String getAliasSize(){
		String aliasSize = alias;
		byte[] b = alias.getBytes();

		int cnt = 0;

		if (b.length >= 13) {
			char[] array = aliasSize.toCharArray();

			for (int n = 0; n < array.length; n++) {

				if(array[n] >='\uAC00' && array[n]<='\uD7A3') {
					cnt+=2;
				}
				else {
					cnt++;
				}

				if (cnt >= 13) {
					aliasSize = new String(b, 0, cnt) + "..";
					break;
				}
			}
		}

		return aliasSize;
	}

    public TMailFolder getFolder(String folderName) throws MessagingException {
        return new TMailFolder(folder.getFolder(folderName));
    }

    public String getName() {
		if (name != null) {
			return name;
		}

        return folder.getName();
    }

    public String getFullName() {
        return folder.getFullName();
    }


	/*
	* added by doyoung
	* 2007.05.03
	*/
	public int getFolderDepth() {
		String[] names = getFullName().split("\\.");
		return names.length;
    }

	public int getSubfoldersCount() {
		try {
			return folder.list().length;
		} catch (MessagingException ex) {
			return 0;
		}
    }

	/*
	* added by doyoung
	* 2006.11.08
	*/
    public String getEncUTF8() {
		String fullName = getFullName();

		try {
			return URLEncoder.encode(fullName, "UTF-8");
		} catch (Exception e) {
			System.out.println("-- Error getUTF8EncName : ["+e.getMessage()+"]");
		}

		return fullName;
    }

	/*
	* added by doyoung
	* 2006.12.18
	*/
    public String getEncName() {
		if (encName == null) {
			encName = TMailUtility.IMAPFolderEncode(getFullName());
		}
		return encName;
    }

    public String getFullNameForWebFolder() {
        String fn = getFullName();
        fn = fn.substring(WEBFOLDER_ROOT.length());

        return fn.replaceAll("\\.", "/");
    }

    public TMailMessage getMessageByUID2(long uid, boolean prefetch)
        throws MessagingException {

        Message msg = ((IMAPFolder) folder).getMessageByUID(uid);
 
        if (msg == null) {
            return null;
        }

        if (prefetch) {
            FetchProfile fp = new FetchProfile();

            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.FLAGS);
            fp.add(FetchProfile.Item.CONTENT_INFO);
            fp.add(UIDFolder.FetchProfileItem.UID);
            fp.add("X-PRIORITY");

            folder.fetch(new Message[]{msg}, fp);
        }

        TMailMessage mymsg = new TMailMessage(msg);
        mymsg.uid = uid; // ((IMAPFolder) folder).getUID(msg);

        // Sent folder MDN processing
		// modified by doyoung
		// 2007.09.28
        if (folder.getFullName().equals("Sent")) {
			setMDNResponseInfo(mymsg);
        }

        return mymsg;
    }

    public TMailMessage getMessageByUID(long uid, boolean prefetch)
        throws MessagingException {

        Message msg = ((IMAPFolder) folder).getMessageByUID(uid);
 
        if (msg == null) {
            return null;
        }

        if (prefetch) {
            FetchProfile fp = new FetchProfile();

            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.FLAGS);
            fp.add(FetchProfile.Item.CONTENT_INFO);
            fp.add(UIDFolder.FetchProfileItem.UID);
            fp.add("X-PRIORITY");

            folder.fetch(new Message[]{msg}, fp);
        }

        TMailMessage mymsg = new TMailMessage(msg);
        mymsg.uid = uid; // ((IMAPFolder) folder).getUID(msg);

		// Sent folder MDN processing
		// modified by doyoung
		// 2007.09.28
        if (folder.getFullName().equals("Sent")) {
			setMDNResponseInfo(mymsg);
        }

        return mymsg;
    }

    public int getMessageCount() throws MessagingException {
        if (cachedStatus != null) {
            return Integer.parseInt((String) cachedStatus.get("MESSAGES"));
        }
        else {
            return folder.getMessageCount();
        }
    }

    public TMailMessage[] getMessages() throws MessagingException {
        Message[] msgs = folder.getMessages();
        TMailMessage[] mymsgs = new TMailMessage[msgs.length];

        for (int i = 0; i < mymsgs.length; i++) {
            mymsgs[i] = new TMailMessage(msgs[i]);
        }

        return mymsgs;
    }
    
    public Message[] getLimitMessages(int start, int end) throws MessagingException {
        return folder.getMessages(start, end);
    }

    public TMailMessage[] getMessagesByUID(long[] uids) throws MessagingException {
        Message[] messages = ((IMAPFolder) folder).getMessagesByUID(uids);
        TMailMessage[] mymessages = new TMailMessage[messages.length];

        for (int i = 0; i < mymessages.length; i++) {
            mymessages[i] = new TMailMessage(messages[i]);
        }

        return mymessages;
    }

    public long getUID(TMailMessage msg) throws MessagingException {
        return ((IMAPFolder) folder).getUID(msg.message);
    }
    
    public long getUID(Message msg) throws MessagingException {
        return ((IMAPFolder) folder).getUID(msg);
    }

    public long[] getNeighborUIDs(long uid, SearchTerm term)
        throws MessagingException {

        Argument args = getSortArgs(term);        

        SortCommand command = new SortCommand(sortKey, sortDirection,args,true);
        Vector vec = (Vector) ((IMAPFolder) folder).doCommand(command);

        int idx = vec.indexOf(String.valueOf(uid));
        long[] uids = new long[2];

        uids[0] = (idx > 0)?
            Long.parseLong((String) vec.elementAt(idx - 1)) : -1;
        uids[1] = (idx < vec.size() - 1)?
            Long.parseLong((String) vec.elementAt(idx + 1)) : -1;

        return uids;
    }

    public int getNewMessageCount() throws MessagingException {
        if (cachedStatus != null) {
            return Integer.parseInt((String) cachedStatus.get("RECENT"));
        }
        else {
            return folder.getNewMessageCount();
        }
    }

    public int getSortedMessageCount() {
        return cachedSortedMessageCount;
    }

    public long getSize() {
        if (cachedStatus != null) {
            return Long.parseLong((String) cachedStatus.get("X-DISKUSAGE"));
        }
        else {
            return -1;
        }
    }

    public int getUnreadMessageCount() throws MessagingException {
		try{
        if (cachedUnreadMessageCount != -1) {
            return cachedUnreadMessageCount;
        }
        else if (cachedStatus != null) {
            return Integer.parseInt((String) cachedStatus.get("UNSEEN"));
        }
        else {
            return folder.getUnreadMessageCount();
        }
		}catch(MessagingException e){
			throw new MessagingException("Exception");	
		}
    }

    public void setUnreadMessageCount(int count) {
        cachedUnreadMessageCount = count;
    }

    public TMailFolder[] list(String pattern) throws MessagingException {
		Folder[] folders = null;
		try{
			folders = ((IMAPFolder)folder).list(pattern);
		}catch(MessagingException e){
			throw new MessagingException("Exception");
		}
		if(folders == null)
			throw new MessagingException("Exception");

        Vector v = new Vector();
        for (int i = 0; i < folders.length; i++) {
            if((folders[i].getType() & Folder.HOLDS_MESSAGES) == 1){
                v.add(new TMailFolder(folders[i]));
            }
        }

        TMailFolder[] myfolders = new TMailFolder[v.size()];
        v.copyInto(myfolders);

        return myfolders;
    }
    public TMailFolder[] list() throws MessagingException {
        Folder[] folders = folder.list();
        TMailFolder[] myfolders = new TMailFolder[folders.length];

        for (int i = 0; i < folders.length; i++) {
            myfolders[i] = new TMailFolder(folders[i]);
        }

        return myfolders;
    }

    public void open(boolean modify) throws MessagingException {
		if(folder == null) {
			throw new MessagingException("Exception folder is Null");
		}

		if (!isOpen()) {
			if (modify) {
				folder.open(Folder.READ_WRITE);
			}
			else {
				folder.open(Folder.READ_ONLY);
			}
		}
    }
    
    public static Argument getSortArgs(SearchTerm term){
    	Argument args = new Argument();
    	parseSearchTerm(term,args);    	
    	return args;
    }

    private static void parseSearchTerm(SearchTerm term, Argument args) {

        if (term == null) {
            args.writeAtom("ALL");
        }
        else if (term instanceof FlagTerm) {
			// args.writeAtom("UNSEEN");
			//FlagTerm flagTerm = (FlagTerm)term;
			//Flags flags = flagTerm.getFlags();
			String flagStr = Arrays.toString(
					(((FlagTerm)term).getFlags()).getUserFlags());

			if (flagStr.indexOf("UNSEEN") >= 0) {
				args.writeAtom("UNSEEN");
			} else if (flagStr.indexOf("SEEN") >= 0) {
				args.writeAtom("SEEN");
			} else if (flagStr.indexOf("FLAGGED") >= 0) {
				args.writeAtom("FLAGGED");
			} else if (flagStr.indexOf("ATTACH") >= 0) {
				args.writeAtom("X-ATT-COUNT LARGER 1");
			} else if (flagStr.indexOf("ANSWERED") >= 0) {
				args.writeAtom("ANSWERED");
			}
			
			flagStr = null;
        }
        else if (term instanceof SubjectTerm) {
            args.writeAtom("SUBJECT");
            try {
                args.writeString(((StringTerm) term).getPattern(), "UTF-8");
            }
            catch (UnsupportedEncodingException e) { }
        }
        else if (term instanceof FromStringTerm) {
            args.writeAtom("FROM");
            try {
                args.writeString(((StringTerm) term).getPattern(), "UTF-8");
            }
            catch (UnsupportedEncodingException e) { }
        }
        else if (term instanceof RecipientStringTerm) {
			if (((RecipientStringTerm)term).
				getRecipientType().equals(Message.RecipientType.CC)) {
            	args.writeAtom("CC");
			}
			else {
            	args.writeAtom("TO");
			}

            try {
                args.writeString(((StringTerm) term).getPattern(), "UTF-8");
            }
            catch (UnsupportedEncodingException e) { }
        }
        else if (term instanceof BodyTerm) {
            args.writeAtom("BODY");
            try {
                args.writeString(((StringTerm) term).getPattern(), "UTF-8");
            }
            catch (UnsupportedEncodingException e) { }
        }
        else if (term instanceof ReceivedDateTerm) {

			SimpleDateFormat sdf =
				new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss Z", Locale.US);
			DateTerm recTerm = (DateTerm) term;
			int comp = recTerm.getComparison();

			if (comp == ReceivedDateTerm.EQ) {
				args.writeAtom(" ON");
			} else if (comp == ReceivedDateTerm.GE) {
				args.writeAtom(" SINCE");
			} else if (comp == ReceivedDateTerm.GT) {
				args.writeAtom(" SINCE");
			} else if (comp == ReceivedDateTerm.LE) {
				args.writeAtom(" BEFORE");
			} else if (comp == ReceivedDateTerm.LT) {
				args.writeAtom(" BEFORE");
			} else if (comp == ReceivedDateTerm.NE) {
				args.writeAtom(" NOT");
			}

			try {				
				args.writeString(sdf.format(recTerm.getDate()), "UTF-8");
            }
            catch (UnsupportedEncodingException e) { }
            finally {
            	sdf = null;
            	recTerm = null;
            }
        }

        else if (term instanceof SentDateTerm) {

            SimpleDateFormat sdf =
                new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss Z", Locale.US);
            DateTerm sentTerm = (DateTerm) term;
            int comp = sentTerm.getComparison();

            if (comp == SentDateTerm.EQ) {
            	args.writeAtom(" ON");
            } else if(comp == SentDateTerm.GE) {
            	args.writeAtom(" SINCE");
            } else if(comp == SentDateTerm.GT) {
            	args.writeAtom(" SINCE");
            } else if(comp == SentDateTerm.LE) {
            	args.writeAtom(" BEFORE");
            } else if(comp == SentDateTerm.LT) {
            	args.writeAtom(" BEFORE");
            } else if(comp == SentDateTerm.NE) {
            	args.writeAtom(" NOT");
            }

            try {                
                args.writeString(sdf.format(sentTerm.getDate()), "UTF-8");
            }
            catch (UnsupportedEncodingException e) { }
            finally {
            	sdf = null;
            	sentTerm = null;
            }
        }
        else if (term instanceof OrTerm) {
            args.writeAtom("(OR");

            SearchTerm[] terms = ((OrTerm) term).getTerms();
            for (int i = 0; i < terms.length; i++) {
                parseSearchTerm(terms[i], args);
            }
            args.writeAtom(")");
        }
		else if (term instanceof AndTerm) {
			//  args.writeAtom("AND");

			SearchTerm[] terms = ((AndTerm) term).getTerms();
			for (int i = 0; i < terms.length; i++) {
				parseSearchTerm(terms[i], args);
			}
		}
        else if (term instanceof NotTerm) {
            args.writeAtom("NOT");

			parseSearchTerm(((NotTerm) term).getTerm(), args);
        } 
        else if(term instanceof AttachTerm){
        	AttachTerm attachTerm = (AttachTerm)term;
        	args.writeAtom(attachTerm.getAtomString());
        	        	
        	try {				
        		args.writeString(attachTerm.getPattern(),"UTF-8");
            }
            catch (UnsupportedEncodingException e) { }
            finally {
            	attachTerm = null;            	
            }
        }
        else if(term instanceof MyselfSearchTerm){
        	MyselfSearchTerm attachTerm = (MyselfSearchTerm)term;
        	args.writeAtom(attachTerm.getAtomString());
        	        	
        	try {				
        		args.writeString(attachTerm.getPattern(),"UTF-8");
            }
            catch (UnsupportedEncodingException e) { }
            finally {
            	attachTerm = null;            	
            }
        }
    }

	public long[] getSortedUids( int off, int len, SearchTerm term )
        throws MessagingException {

        Argument args = getSortArgs(term);        

        SortCommand command = new SortCommand(sortKey, sortDirection, args, true);
        Vector vec = (Vector) ((IMAPFolder) folder).doCommand(command);

		if (off < 0) off = 0;
		if (len < 0) len = 15;

        int total = cachedSortedMessageCount = vec.size();
        len = Math.min(Math.min(len, total), total - off);

        if (len <= 0) {
            return new long[0];
        }

        long[] msguids = new long[len];
        
        for (int i = 0; i < msguids.length; i++) {
            String msguid = (String) vec.elementAt(off + i);
            msguids[i] = Long.parseLong(msguid);
        }

		return msguids;
    }

    public TMailMessage[] sort(int off, int len, SearchTerm term)
        throws MessagingException {

		return sort(off, len, sortKey, sortDirection, term);
	}

	public TMailMessage[] sort(
		int off, int len, String sortby, String sortdir, SearchTerm term)
        throws MessagingException {

		setSort(sortby, sortdir);

		return sort(off, len, sortKey, sortDirection, term);
	}

	public TMailMessage[] sort(
		int off, int len, int sortKey, int sortDirection, SearchTerm term)
        throws MessagingException {

		Argument args = getSortArgs(term);        

        SortCommand command = new SortCommand(sortKey,sortDirection,args,false);
        Vector vec = (Vector) ((IMAPFolder) folder).doCommand(command);

		if (off < 0) {
			off = 0;
		} else if (off > vec.size()) {
			off = (vec.size()/len) * len;
		}

        int total = cachedSortedMessageCount = vec.size();
        len = Math.min(Math.min(len, total), total - off);

		if (len < 0) {
			len = total;
		}

        if (len <= 0) {
            return new TMailMessage[0];
        }

        int[] msgnums = new int[len];
        
        for (int i = 0; i < msgnums.length; i++) {
            String msgnum = (String) vec.elementAt(off + i);
            msgnums[i] = Integer.parseInt(msgnum);
        }

        Message[] msgs = folder.getMessages(msgnums);
        FetchProfile fp = new FetchProfile();

        fp.add(FetchProfile.Item.ENVELOPE);
        fp.add(FetchProfile.Item.FLAGS);
        fp.add(FetchProfile.Item.CONTENT_INFO);
        fp.add(UIDFolder.FetchProfileItem.UID);
        fp.add("X-PRIORITY");

        folder.fetch(msgs, fp);

        TMailMessage[] mymsgs = new TMailMessage[msgs.length];

        for (int i = 0; i < mymsgs.length; i++) {
            mymsgs[i] = new TMailMessage(msgs[i]);
            mymsgs[i].uid = ((IMAPFolder) folder).getUID(msgs[i]);

            // Sent folder MDN processing
			// modified by doyoung
			// 2007.09.28
            if (folder.getFullName().equals("Sent")) {
				setMDNResponseInfo(mymsgs[i]);
            }
        }

        return mymsgs;
    }
	
	public int getSortMessageTotal(SearchTerm term)
	throws MessagingException {
		int total = 0;
		Argument args = getSortArgs(term);

        SortCommand command = new SortCommand(sortKey,sortDirection,args,false);
        Vector vec = (Vector) ((IMAPFolder) folder).doCommand(command);
        total = cachedSortedMessageCount = vec.size();        
        vec = null;
		
		return total;
	}	
	

	public void setMDNResponseInfo(TMailMessage myMessage) throws MessagingException {
		String mid = myMessage.getTrimMessageID();
		int mdnlen = 0;

		if (mid == null) {
			return;
		}

		TMailMDNResponse[] mdnresponses = xgetMDN(mid,"0","0","all");
		int readcnt = 0;

		if (mdnresponses != null) {
			for (int i = 0; i < mdnresponses.length; i++) {
				String code 	= mdnresponses[i].getCode();

				if (code != null && code.equalsIgnoreCase("1000")) {
					readcnt ++;
				}
			}

			myMessage.setMDNResponses(mdnresponses);
			myMessage.setMDNReadCount(readcnt);
		}
	}
	
	public TMailMDNResponse[] getMDNResponseInfo(String mid,String page,String pagebase,String pattern)
	throws MessagingException {		
		return xgetMDN(mid,page,pagebase,pattern);
	}
	
	public int getMdnTotalCnt(){
		return mdnTotalCnt;
	}
	
	

	/*public void setMDNResponseInfo(TMailMessage myMessage) throws MessagingException {
		String mid = myMessage.getTrimMessageID();
		int mdnlen = 0;

		if (mid == null) {
			return;
		}

		TMailMDNResponse[] mdnresponses = null;
	
		// if (!isCHKMDN || isMDN2) {
		if (isMDN2) {
			isCHKMDN = true;

			try {
				setMDNResponseInfo2(myMessage);
				isMDN2 = true;
				return ;
			} catch (MessagingException ex) {
				System.out.println("-- ERR setMDNResponseInfo2 : ["+ex.getMessage()+"]");
			}
		}

		// OLD GETMDN
		mdnresponses = xgetMDN(mid);

		if (mdnresponses == null) {
			return;
		}

		InternetAddress[] recipients = myMessage.getAllRecipients();

		int[] mdnchk = new int[mdnresponses.length];
		for (int l = 0; l < mdnresponses.length; l++) {
			mdnchk[l] = 0;
		}

		int bcc_cnt = 0;

		for (int j = 0, k; j < mdnresponses.length; j++) {
			boolean isBcc = true;
			for (k = 0; k < recipients.length; k++) {
				if (mdnresponses[j].getAddress().equalsIgnoreCase(
					recipients[k].getAddress())) {
					mdnresponses[j].setPersonal(recipients[k].getPersonal());
					isBcc = false;
					break;
				}
			}

			if (isBcc) {
				bcc_cnt++;
			}
		}

		TMailMDNResponse[] mdns = 
			new TMailMDNResponse[recipients.length + bcc_cnt];

		int mdn_cnt = 0;
		int mdn_readcnt = 0;

		for (int j = 0, k; j < recipients.length; j++) {
			for (k = 0; k < mdnresponses.length; k++) {
				if (recipients[j].getAddress().equalsIgnoreCase(
					mdnresponses[k].getAddress())) {
					mdns[mdn_cnt] = mdnresponses[k];

					if (mdnresponses[k].getSentDate() > 1) {
						mdn_readcnt++;
					}
					break;
				}
			}

			if (k == mdnresponses.length) {
				mdns[mdn_cnt] = new TMailMDNResponse(
					recipients[j].getAddress(), null);
			}

			mdn_cnt++;
		}

		if(bcc_cnt > 0) {
			for (int j = 0, k; j < mdnresponses.length; j++) {
				for (k = 0; k < recipients.length; k++) {
					if (mdnresponses[j].getAddress().equalsIgnoreCase(
						recipients[k].getAddress())) {
						break;
						}
					if ((k+1) == recipients.length) {
						mdns[mdn_cnt] = mdnresponses[j];
						if (mdnresponses[j].getSentDate() > 1) {
							mdn_readcnt++;
						}
							mdn_cnt++;
					}
				}
			}
		}

		myMessage.setMDNResponses(mdns);
		myMessage.setMDNReadCount(mdn_readcnt);
	}*/

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

    public void setName(String name) {
        this.name = name;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setEncName(String encName) {
        this.encName = encName;
    }

    public void setFlags(TMailMessage[] msgs, Flags flag, boolean value)
        throws MessagingException {

        Message[] messages = new Message[msgs.length];

        for (int i = 0; i < messages.length; i++) {
            messages[i] = msgs[i].message;
        }
  
        folder.setFlags(messages, flag, value);
    }

    public boolean renameTo(TMailFolder f) throws MessagingException {
        return folder.renameTo(f.folder);
    }

    public void setSort(int sortKey, int sortDirection) {
        this.sortKey = sortKey;
        this.sortDirection = sortDirection;
    }

    public void setSortKey(int sortKey) {
        this.sortKey = sortKey;
    }

    public void setSortDirection(int sortDirection) {
        this.sortDirection = sortDirection;
    }

    public void setSort(String sortby, String sortdir) {
		setSortKey(sortby);
		setSortDirection(sortdir);
    }

	public void setSortKey(String sortby) {
		int key = SORT_ARRIVAL;

		if (sortby != null) {
			if (sortby.equals("from")) {
				key = SORT_FROM;
			}
			else if (sortby.equals("size")) {
				key = SORT_SIZE;
			}
			else if (sortby.indexOf("subj") >= 0) {
				key = SORT_SUBJECT;
			}
			else if (sortby.equals("to")) {
				key = SORT_TO;
			}
			else if (sortby.equals("kind")) {
				key = SORT_CC;
			}
		}

        this.sortKey = key;
	}

	public void setSortDirection(String sortdir) {
		int dir = SORT_DESCENDING;

		if (sortdir != null) {
			if (sortdir.equals("asce")) {
				dir = SORT_ASCENDING;
			}
		}

        this.sortDirection = dir;
	}

    public void status(String[] list) throws MessagingException {
        Argument args = new Argument();
        for (int i = 0; i < list.length; i++) {
            args.writeAtom(list[i]);
        }

		try {
			StatusCommand command = 
				new StatusCommand(folder.getFullName(), args);
        	cachedStatus = 
				(Map) ((IMAPFolder) folder).doCommand(command);
        	cachedStatus.put("NAME", folder.getFullName());
		}
        catch (MessagingException e) {
			cachedStatus = new HashMap();
        	cachedStatus.put("NAME", folder.getFullName());
        	cachedStatus.put("MESSAGES", "-1");
        	cachedStatus.put("UNSEEN", "-1");
        	cachedStatus.put("RECENT", "-1");
        	cachedStatus.put("X-DISKUSAGE", "-1");
        }
    }
    
    public Map xstatus(String[] list) throws MessagingException {
        Argument args = new Argument();
        for (int i = 0; i < list.length; i++) {
            args.writeAtom(list[i]);
        }

		
		XStatusCommand command = 
			new XStatusCommand(args);
    	Map statusMap = 
			(Map) ((IMAPFolder) folder).doCommand(command);
    	
    	return statusMap;		
    }

    public void xaddMDN(String mid) throws MessagingException {
        XMDNCommand command = new XMDNCommand("X-ADDMDN", mid);
        ((IMAPFolder) folder).doCommand(command);
    }

	public void xdelMDN(String mid) throws MessagingException {
		XMDNCommand command = new XMDNCommand("X-DELMDN", mid);
		((IMAPFolder) folder).doCommand(command);
	}
	
	/* X-SETMDN 
    public void xsetMDN(String mid, String to, long d) throws MessagingException {
        XMDNCommand command = 
			new XMDNCommand("X-SETMDN", mid + ' ' + to + ' ' + d);
        ((IMAPFolder) folder).doCommand(command);
    }*/

	/* X-SETMDN2 */
	
	public void xsetMDNOpen() throws MessagingException {
    	XMDNSetAdvanceCommand command = 
			new XMDNSetAdvanceCommand( "X-OPENMDN",null);
        ((IMAPFolder) folder).doCommand(command);
    }
	
	public void xsetMDNClose() throws MessagingException {
    	XMDNSetAdvanceCommand command = 
			new XMDNSetAdvanceCommand( "X-CLOSEMDN",null);
        ((IMAPFolder) folder).doCommand(command);
    }
	
	public void xsetMDNAddCode(String mid, String to, String personal, long time, String code) throws MessagingException {
		XMDNSetAdvanceCommand command = 
			new XMDNSetAdvanceCommand( "X-SETMDN-DB", 
				mid + " " + to + " \"" + personal + "\" " + time + " " + code);
        ((IMAPFolder) folder).doCommand(command);
    }
	
    public void xsetMDN(String mid, String to, String personal, long time, String code) throws MessagingException {
        XMDNCommand command = 
			new XMDNCommand( "X-SETMDN", 
				mid + " " + to + " \"" + personal + "\" " + time + " " + code);
        ((IMAPFolder) folder).doCommand(command);
    }

    /*public void xsetMDN2(String mid, String to, String personal, String time, String code) throws MessagingException {
        XMDNCommand command = 
			new XMDNCommand( "X-SETMDN", 
				mid + " " + to + " \"" + personal + "\" " + time + " " + code);
        ((IMAPFolder) folder).doCommand(command);
    }*/

	/* X-RECALL */
    public void xrecall(String mid) throws MessagingException {
        XRECALLCommand command = new XRECALLCommand("X-RECALL", mid);
        ((IMAPFolder) folder).doCommand(command);
    }

	/* X-RECALL-ALL */
    public void xrecallAll(String mid) throws MessagingException {
        XRECALLCommand command = new XRECALLCommand("X-RECALL-ALL", mid);
        ((IMAPFolder) folder).doCommand(command);
    }

    public TMailMDNResponse[] xgetMDN(String mid, String page, String pageBase, String pattern) 
    throws MessagingException {
        try {
            XMDNCommand command = new XMDNCommand("X-GETMDN", mid, page, pageBase, pattern);
            Vector vec = (Vector) ((IMAPFolder) folder).doCommand(command);
            
            mdnTotalCnt = command.getTotal();

            TMailMDNResponse[] mdns = new TMailMDNResponse[vec.size()];
            vec.copyInto(mdns);

            return mdns;
        }
        catch (MessagingException e) {
            if (e.getNextException() instanceof CommandFailedException){
                return null;
            }
            else {
                throw e;
            }
        }
    }
    
    public TMailMDNHistory[] xgetMDNHistory(String mid, String to) 
    throws MessagingException {
        try {
        	XMDNHistoryCommand command = new XMDNHistoryCommand("X-GETMDN-HISTORY", mid, to);
            Vector vec = (Vector) ((IMAPFolder) folder).doCommand(command);
            
            TMailMDNHistory[] mdnHistories = new TMailMDNHistory[vec.size()];
            vec.copyInto(mdnHistories);
            int total = mdnHistories.length;
            for (int i = 0; i < mdnHistories.length; i++) {
            	mdnHistories[i].setReadSeq(total);
            	total--;
			}

            return mdnHistories;
        }
        catch (MessagingException e) {
            if (e.getNextException() instanceof CommandFailedException){
                return null;
            }
            else {
                throw e;
            }
        }
    }

    /*public TMailMDNResponse[] xgetMDN2(String mid) throws MessagingException {
        try {
            XMDNCommand2 command = new XMDNCommand2("X-GETMDN2", mid);
            Vector vec = (Vector) ((IMAPFolder) folder).doCommand(command);

            TMailMDNResponse[] mdns = new TMailMDNResponse[vec.size()];
            vec.copyInto(mdns);

            return mdns;
        }
        catch (MessagingException e) {
            if (e.getNextException() instanceof CommandFailedException){
                return null;
            }
            else {
                throw e;
            }
        }
    }*/

    public void xreceiveMDN(String mid, String to) throws MessagingException {
        XMDNCommand command = new XMDNCommand("X-RECEIVEMDN", mid + ' ' + to);
        ((IMAPFolder) folder).doCommand(command);
    }

    public void xmove(long[] uids, String folder)
        throws MessagingException {

        XMoveCommand command = new XMoveCommand(uids, folder);
        ((IMAPFolder) this.folder).doCommand(command);
    }

    public void xmove2(String uids, String folder)
        throws MessagingException {

        XMoveCommand2 command = new XMoveCommand2(uids, folder);
        ((IMAPFolder) this.folder).doCommand(command);
    }

    public void xreserve(Date date, TMailMessage message)
        throws MessagingException {

        XReserveCommand command = new XReserveCommand(date, message);
        ((IMAPFolder) folder).doCommand(command);
    }

    public void xappend(String fldr, TMailMessage message)
        throws MessagingException {

        XAppendCommand command = new XAppendCommand(fldr, message);
        ((IMAPFolder) folder).doCommand(command);
    }

	public Map xcommand(String cmd, String[] args)
        throws MessagingException
    {
        XCommandCustom command = new XCommandCustom(cmd, args);
        Map map = (Map)((IMAPFolder)folder).doCommand(command);

		return map;
    }
	
	public int xsearchMID(String mid)
    throws MessagingException{
		int uidVal = -1;
		
	    XSearchMIDCommand command = new XSearchMIDCommand(mid);
	    Integer uid = (Integer)((IMAPFolder)folder).doCommand(command);
	    
	    if(uid != null){
	    	uidVal = uid.intValue(); 
	    }	
		return uidVal;
	}
	
	public String xintegrityMID(String mid)
    throws MessagingException{
		String resultIntegirty = null;
		
	    XIntegrityCommand command = new XIntegrityCommand(mid);
	    resultIntegirty = (String)((IMAPFolder)folder).doCommand(command);	    
	    	
		return resultIntegirty;
	}

	public Folder getMyFolder(){
		return folder;
	}
	
	public TMailXCommand getCustomCommand(){
		return new TMailXCommand((IMAPFolder)folder);
	}

    class SortCommand implements IMAPFolder.ProtocolCommand {
        
        private String key = null;
        private Argument condition = null;
        private boolean uid = false;

        SortCommand(int key, int direction, Argument condition, boolean uid) {
            switch (key) {
                case SORT_ARRIVAL:
                    this.key = "ARRIVAL";
                    break;
                case SORT_CC:
                    this.key = "CC";
                    break;
                case SORT_DATE:
                    this.key = "DATE";
                    break;
                case SORT_FROM:
                    this.key = "FROM";
                    break;
                case SORT_SIZE:
                    this.key = "SIZE";
                    break;
                case SORT_SUBJECT:
                    this.key = "SUBJECT";
                    break;
				case SORT_TO:
					this.key = "TO";
					break;
            }
            
            if (direction == SORT_DESCENDING) {
                this.key = "REVERSE " + this.key;
            }

            this.condition = condition;
            this.uid = uid;
        }

        public Object doCommand(IMAPProtocol p) throws ProtocolException {
            Argument list = new Argument();
            list.writeAtom(key);

            // Issue command
            Argument args = new Argument();
            args.writeArgument(list);
            args.writeString("UTF-8");
            args.append(condition);
            Response[] r = p.command((uid)? "UID SORT" : "SORT", args);
            Response response = r[r.length-1];

            // Grab response
            Vector v = new Vector();
            if (response.isOK()) { // command succesful
                for (int i = 0, len = r.length; i < len; i++) {
                    if (!(r[i] instanceof IMAPResponse)) {
                        continue;
                    }

                    IMAPResponse ir = (IMAPResponse)r[i];
                    if (ir.keyEquals("SORT")) {
                        String num;
                        while ((num = ir.readAtomString()) != null) {
                            v.addElement(num);
                        }
                        r[i] = null;
                    }
                }
            }

            // dispatch remaining untagged responses
            p.notifyResponseHandlers(r);
            p.handleResult(response);

            return v;
        }
    }

    class StatusCommand implements IMAPFolder.ProtocolCommand {

        private String folder = null;
        private Argument list = null;

        StatusCommand(String folder, Argument list) {
            this.folder = folder;
            this.list = list;
        }

        public Object doCommand(IMAPProtocol p) throws ProtocolException {
            // Issue command
            Argument args = new Argument();
            args.writeString(TMailUtility.IMAPFolderEncode(folder));
            args.writeArgument(list);
            Response[] r = p.command("STATUS", args);
            Response response = r[r.length-1];

            // Grab response
            Map h = new HashMap();
            if (response.isOK()) { // command succesful
                for (int i = 0, len = r.length; i < len; i++) {
                    if (!(r[i] instanceof IMAPResponse)) {
                        continue;
                    }

                    IMAPResponse ir = (IMAPResponse)r[i];
                    if (ir.keyEquals("STATUS")) {
                        ir.readAtomString();
                        String[] list = ir.readSimpleList();
                        for (int j = 0; j < list.length; j += 2) {
                            h.put(list[j], list[j + 1]);
                        }
                        r[i] = null;
                    }
                }
            }

            // dispatch remaining untagged responses
            p.notifyResponseHandlers(r);
            p.handleResult(response);

            return h;
        }
    }
    
    @SuppressWarnings("all")
	class XStatusCommand implements IMAPFolder.ProtocolCommand {

        
        private Argument list = null;

        XStatusCommand(Argument list) {            
            this.list = list;
        }

        public Object doCommand(IMAPProtocol p) throws ProtocolException {
            // Issue command
            Argument args = new Argument();           
            args.writeArgument(list);
            Response[] r = p.command("X-STATUS", args);
            Response response = r[r.length-1];

            // Grab response
            Map statusInfo = new HashMap();
            Map boxInfo = null;
            String folderName = null;
            if (response.isOK()) { // command succesful
            	for (int i = 0, len = r.length; i < len; i++) {                	
                    if (!(r[i] instanceof IMAPResponse)) {
                        continue;
                    }

                    IMAPResponse ir = (IMAPResponse)r[i];
                    if (ir.keyEquals("STATUS")) {
                    	boxInfo = new HashMap();
                    	folderName = ir.readAtomString();                    	
                    	
                        String[] list = ir.readSimpleList();
                        for (int j = 0; j < list.length; j += 2) {                        	
                        	boxInfo.put(list[j], list[j + 1]);
                        }                        
                        statusInfo.put(TMailUtility.IMAPFolderDecode(folderName), boxInfo);
                        r[i] = null;
                        folderName = null;
                        boxInfo = null;
                    }
                }
            }

            // dispatch remaining untagged responses
            p.notifyResponseHandlers(r);
            p.handleResult(response);

            return statusInfo;
        }
    }

    /*class XMDNCommand implements IMAPFolder.ProtocolCommand {

        private String cmd = null;
        private String mid = null;

        XMDNCommand(String cmd, String mid) {
            this.cmd = cmd;
            this.mid = mid;
        }

        public Object doCommand(IMAPProtocol p) throws ProtocolException {
            SimpleDateFormat sdf =
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

            // Issue command
            Argument args = new Argument();
            args.writeAtom(mid);
            Response[] r = p.command(cmd, args);
            Response response = r[r.length-1];

            // Grab response
            Vector v = new Vector();
            if (response.isOK()) { // command succesful
                for (int i = 0, len = r.length; i < len; i++) {
                    if (!(r[i] instanceof IMAPResponse)) {
                        continue;
                    }

                    IMAPResponse ir = (IMAPResponse)r[i];
                    if (ir.getKey() != null) {
                        String address = ir.readAtomString();
                        String date = ir.readAtomString();

						if (address == null || address.length() <= 0) {
							break;
						}

                        try {
                            v.addElement(new TMailMDNResponse(address,
                                sdf.parse(date)));
                        }
                        catch (java.text.ParseException e) {
							System.out.println("XMDN : ["+e.getMessage()+"]");
                        }

                        r[i] = null;
                    }
                }
            }

            // dispatch remaining untagged responses
            p.notifyResponseHandlers(r);
            p.handleResult(response);

            return v;
        }
    }*/

    class XMDNCommand implements IMAPFolder.ProtocolCommand {

        private String cmd = null;
        private String mid = null;
        private String page = null;
        private String pageBase = null;
        private String pattern = null;
        private int total = 0;

        XMDNCommand(String cmd, String mid, String page, String pageBase, String pattern) {
            this.cmd = cmd;
            this.mid = mid;
            this.page = page;
            this.pageBase = pageBase;
            this.pattern = pattern;
        }
        
        XMDNCommand(String cmd, String mid) {
            this.cmd = cmd;
            this.mid = mid;            
        }
        
        public int getTotal(){
        	return total;
        }

        public Object doCommand(IMAPProtocol p) throws ProtocolException {
            SimpleDateFormat sdf =
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

            // Issue command
            Argument args = new Argument();
            args.writeAtom(mid);
            
            if(page != null){
            	args.writeString(page);
            }            
            if(pageBase != null){
            	args.writeString(pageBase);
            }
    		if(pattern != null){
    			args.writeString(pattern);
    		}    		
    		
            Response[] r = p.command(cmd, args);
            Response response = r[r.length-1];            
            Response totalResponse = null;

            // Grab response
            Vector v = new Vector();
            if (response.isOK()) { // command succesful
            	if(cmd.equals("X-GETMDN")){
            		totalResponse = r[r.length-2];
            	}
            	
                for (int i = 0, len = r.length; i < len; i++) {
                    if (!(r[i] instanceof IMAPResponse)) {
                        continue;
                    }

                    IMAPResponse ir = (IMAPResponse)r[i];
					if (ir.getKey() != null) {
						String address = ir.readAtomString();
						String personal = ir.readAtomString();
						String unixTime = ir.readAtomString();						
						String code = ir.readAtomString();
						String readCnt = ir.readAtomString();

						/*
						System.out.println("::: address: " + address);
						System.out.println("::: personal: " + personal);
						System.out.println("::: unixTime: " + unixTime);
						System.out.println("::: readCnt: " + readCnt);
						System.out.println("::: code: " + code);
						*/

						if (address == null || address.length() <= 0) {
							break;
						}
						
						try {
							TMailMDNResponse myMDN = 
								new TMailMDNResponse(personal,address,sdf.parse(unixTime));						
							myMDN.setReadCnt(readCnt);
							myMDN.setCode(code);

							v.addElement(myMDN);
                        }
                        catch (java.text.ParseException e) {
							System.out.println("XMDN : ["+e.getMessage()+"]");
                        }
                        r[i] = null;
                        
                    }
                }
                if(cmd.equals("X-GETMDN")){
	                String totalStr = totalResponse.readAtom();
	    			if(totalStr != null){
	    				this.total = Integer.parseInt(totalStr);
	    			}
                }
                
                
            }
            
            
            // dispatch remaining untagged responses
            p.notifyResponseHandlers(r);
            p.handleResult(response);

            return v;
        }
    }
    
    class XMDNSetAdvanceCommand implements IMAPFolder.ProtocolCommand {

        private String cmd = null;
        private String argValue = null;         
        
        XMDNSetAdvanceCommand(String cmd, String argValue) {
            this.cmd = cmd;
            this.argValue = argValue;            
        }        
        

        public Object doCommand(IMAPProtocol p) throws ProtocolException {
            SimpleDateFormat sdf =
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

            // Issue command
            Argument args = new Argument();
            if(argValue != null){
            	args.writeAtom(argValue);
            }           
              		
    		
            Response[] r = p.command(cmd, args);
            Response response = r[r.length-1];
            
            // dispatch remaining untagged responses
            p.notifyResponseHandlers(r);
            p.handleResult(response);

            return null;
        }
    }   

	/* X-RECALL */
    class XRECALLCommand implements IMAPFolder.ProtocolCommand {

        private String cmd = null;
        private String mid = null;

        XRECALLCommand(String cmd, String mid) {
            this.cmd = cmd;
            this.mid = mid;
        }

        public Object doCommand(IMAPProtocol p) throws ProtocolException {
            // Issue command
            Argument args = new Argument();
            args.writeAtom(mid);
            Response[] r = p.command(cmd, args);
            Response response = r[r.length-1];

            // dispatch remaining untagged responses
            p.notifyResponseHandlers(r);
            p.handleResult(response);

            return null;
        }
    }
    
    class XMDNHistoryCommand implements IMAPFolder.ProtocolCommand {
    	private SimpleDateFormat sdf =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);    	
        private String cmd = null;
        private String mid = null;
        private String receviedUser = null;

        XMDNHistoryCommand(String cmd, String mid, String receviedUser) {
            this.cmd = cmd;
            this.mid = mid;
            this.receviedUser = receviedUser;
        }

        public Object doCommand(IMAPProtocol p) throws ProtocolException {
            // Issue command
            Argument args = new Argument();
            args.writeAtom(mid);
            args.writeAtom(receviedUser);
            
            Response[] r = p.command(cmd, args);
            Response response = r[r.length-1];            
            Response totalResponse = null;

            // Grab response
            Vector v = new Vector();
            if (response.isOK()) { // command succesful
            	String unixTime = null;
            	TMailMDNHistory history = null;            	
                for (int i = 0, len = r.length; i < len; i++) {
                    if (!(r[i] instanceof IMAPResponse)) {
                        continue;
                    }

                    IMAPResponse ir = (IMAPResponse)r[i];
                    
					if (ir.getKey() != null) {
						unixTime = ir.readAtomString();	
						
						if (unixTime == null || unixTime.length() <= 0) {
							break;
						}
						
						try {
							history = new TMailMDNHistory(0, sdf.parse(unixTime));
							v.addElement(history);
                        }
                        catch (java.text.ParseException e) {
							System.out.println("XMDN : ["+e.getMessage()+"]");
                        }
                        r[i] = null;
                        
                    }
                }
            }

            // dispatch remaining untagged responses
            p.notifyResponseHandlers(r);
            p.handleResult(response);

            return v;
        }
    }

    class XMoveCommand implements IMAPFolder.ProtocolCommand {

        private long[] uids = null;
        private String folder = null;

        XMoveCommand(long[] uids, String folder) {
            this.uids = uids;
            this.folder = folder;
        }

        public Object doCommand(IMAPProtocol p) throws ProtocolException {
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < uids.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }

                sb.append(String.valueOf(uids[i]));
            }

            // Issue command
            Argument args = new Argument();
            args.writeString(sb.toString());
            args.writeString(TMailUtility.IMAPFolderEncode(folder));
            Response[] r = p.command("UID X-MOVE", args);
            Response response = r[r.length-1];

            // dispatch remaining untagged responses
            p.notifyResponseHandlers(r);
            p.handleResult(response);

            return null;
        }
    }

    class XMoveCommand2 implements IMAPFolder.ProtocolCommand {

        private String uids = null;
        private String folder = null;

        XMoveCommand2(String uids, String folder) {
            this.uids = uids;
            this.folder = folder;
        }

        public Object doCommand(IMAPProtocol p) throws ProtocolException {
            // Issue command
            Argument args = new Argument();
            args.writeString(uids);
            args.writeString(TMailUtility.IMAPFolderEncode(folder));
            Response[] r = p.command("UID X-MOVE", args);
            Response response = r[r.length-1];

            // dispatch remaining untagged responses
            p.notifyResponseHandlers(r);
            p.handleResult(response);

            return null;
        }
    }

    class XReserveCommand implements IMAPFolder.ProtocolCommand {

        private Date date = null;
        private TMailMessage message = null;

        XReserveCommand(Date date, TMailMessage message) {
            this.date = date;
            this.message = message;
        }

        public Object doCommand(IMAPProtocol p) throws ProtocolException {
            SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss Z",
                Locale.US);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                message.writeTo(baos);
            }
            catch (IOException e) {
                throw new ProtocolException(e.getMessage());
            }
            catch (MessagingException e) {
                throw new ProtocolException(e.getMessage());
            }

            // Issue command
            Argument args = new Argument();
            args.writeAtom("(\\Seen)");
            args.writeString(sdf.format(date));
            args.writeBytes(baos);
            Response[] r = p.command("X-RESERVE", args);
            Response response = r[r.length-1];

            // dispatch remaining untagged responses
            p.notifyResponseHandlers(r);
            p.handleResult(response);

            return null;
        }
    }

    class XAppendCommand implements IMAPFolder.ProtocolCommand {

        private String fldr = null;
        private Date date = null;
        private TMailMessage message = null;

        XAppendCommand(String fldr, TMailMessage message) {
            this.fldr = fldr;
            this.message = message;
        }

        public Object doCommand(IMAPProtocol p) throws ProtocolException {
            SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss +0900",
                Locale.US);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                message.writeTo(baos);
				date = message.getSentDate();
            }
            catch (IOException e) {
                throw new ProtocolException(e.getMessage());
            }
            catch (MessagingException e) {
                throw new ProtocolException(e.getMessage());
            }

            // Issue command
            Argument args = new Argument();
            args.writeString(TMailUtility.IMAPFolderEncode(fldr));
			if(date != null) {
            	args.writeString(sdf.format(date));
			}
            args.writeBytes(baos);
            Response[] r = p.command("X-APPEND", args);
            Response response = r[r.length-1];

            // dispatch remaining untagged responses
            p.notifyResponseHandlers(r);
            p.handleResult(response);

            return null;
        }
    }
    
    class XSearchMIDCommand implements IMAPFolder.ProtocolCommand {

        private String mid = null;        

        XSearchMIDCommand(String mid) {
            this.mid = mid;            
        }

        public Object doCommand(IMAPProtocol p) throws ProtocolException {
            SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss +0900",
                Locale.US);
            
            // Issue command
            Argument args = new Argument();
            args.writeString(mid);
			
            Response[] r = p.command("UID X-SEARCH-MSGID", args);
            Response response = r[r.length-1];            

            // Grab response
            Integer uid = null;
            if (response.isOK()) { // command succesful
            	String uidStr = null;
                for (int i = 0, len = r.length; i < len; i++) {
                    if (!(r[i] instanceof IMAPResponse)) {
                        continue;
                    }

                    IMAPResponse ir = (IMAPResponse)r[i];
					if (ir.getKey() != null) {
						uidStr = ir.readAtomString();
						if(uidStr != null){
							uid = new Integer(uidStr);
						}
                        r[i] = null;
                    }
                }
            }
            // dispatch remaining untagged responses
            p.notifyResponseHandlers(r);
            p.handleResult(response);

            return uid;
        }
    }
    
    class XIntegrityCommand implements IMAPFolder.ProtocolCommand {

        private String mid = null;        

        XIntegrityCommand(String mid) {
            this.mid = mid;            
        }

        public Object doCommand(IMAPProtocol p) throws ProtocolException {
            SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss +0900",
                Locale.US);
            
            // Issue command
            Argument args = new Argument();
            args.writeString(mid);
            args.writeString("X-INTEGRITY");            
			
            Response[] r = p.command("UID FETCH", args);
            Response response = r[r.length-1];            

            // Grab response
            String result = null;
            if (response.isOK()) { // command succesful
                for (int i = 0, len = r.length; i < len; i++) {
                    if (!(r[i] instanceof IMAPResponse)) {
                        continue;
                    }

                    IMAPResponse ir = (IMAPResponse)r[i];
					if (ir.getKey() != null) {						
						result = ir.readAtomString();
                        r[i] = null;
                    }
                }
            }
            // dispatch remaining untagged responses
            p.notifyResponseHandlers(r);
            p.handleResult(response);

            return result;
        }
    }

    class XCommandCustom implements IMAPFolder.ProtocolCommand {

        private String cmd = null;
		private String[] argsStr = null;

        XCommandCustom(String cmd,String[] args) {
            this.cmd = cmd;
            this.argsStr = argsStr;
        }

        public Object doCommand(IMAPProtocol p) throws ProtocolException {
            // Issue command
			int atomNullCnt = 0;
            Argument args = new Argument();
			if (argsStr != null){
				for (int i = 0 ; i < argsStr.length ; i++){
					args.writeString(argsStr[i]);
				}
			}

            Response[] r = p.command(cmd, args);
            Response response = r[r.length-1];

			Map result = new HashMap();
			List atoms = null;
			String atom = null;
			
			if (response.isOK()) {
				for (int i = 0; i < r.length - 1; i++) {					
					atoms = new ArrayList();
					if ((r[i] instanceof IMAPResponse)){
						IMAPResponse ir = (IMAPResponse)r[i];
						String mapKey = ir.getKey();
						if (ir.keyEquals("STATUS")) {
	                        String temp = ir.readAtomString();
	                        String[] list = ir.readSimpleList();
	                        result.put(temp,list);	                        
	                    } else {						
							while( ( atom = ir.readAtom()) != null) {
								//System.out.println("::::::::::KEY["+ir.getKey()+"]");
								//System.out.println("::::::::::ATOM["+atom+"]");
								if (atom != null && atom.length() > 0){
									atoms.add(atom);
								} else {
									atomNullCnt++;
								}
	
								if (atomNullCnt > 100){
									throw new ProtocolException("Atom_read_error");
								}
							}
							if ( atoms.size() > 0){
								result.put(mapKey, 
										(String [])atoms.toArray(new String[atoms.size()]));
								
							}
	                    }
						
						r[i] = null;
					}
				}
			}
			
			atom = null;
			atoms = null;

            // dispatch remaining untagged responses
            p.notifyResponseHandlers(r);
            p.handleResult(response);

            return result;
        }
    }
    
    
    
	
	/**
	 * �������� �˻��������� Ư�� ���� �ڽ��� �ִ� ������ �˻��Ѵ�.<br>
	 * ���� ����� ���µǾ� ���� �ʴٸ� ����� �����ϰ� �۾��Ѵ�.<br>
	 * ��� ���� �޵�� nullüũ(Ȥ�� Ÿ��üũ)�� �ؾ��Ѵ�.<br>
	 * 2008.06.13 add by ysko 
	 * ��� ��) 
	 *<code>
	 *	MessageIDTerm midterm = new MessageIDTerm(messageId);
	 *	MyMessage[] searchResultMsgs = draftsFolder.search(midterm);
	 *	//delete doubled messages 
	 *	if (searchResultMsgs instanceof MyMessage[]) {
	 *		draftsFolder.setFlags(searchResultMsgs, new Flags(Flags.Flag.DELETED), true);
	 *	}
	 *</code>
	 * 
	 * @param term - SearchTerm�� ��� ���� �˻� ����
	 * @return - <li>�˻��� �޼������� ����: ������ �޼����� ��ٸ� null�� �����Ѵ�.
	 * @throws MessagingException 
	 * 
	 */
	public TMailMessage[] search(SearchTerm term) throws MessagingException {
		if (!folder.isOpen())
			folder.open(Folder.READ_WRITE);
		Message[] resultMessages = folder.search(term);
		
		int messageCnt = (resultMessages != null) ? resultMessages.length : 0 ;
		TMailMessage[] messages = null;
		if (messageCnt > 0) {
			messages = new TMailMessage[messageCnt]; 
			for (int i = 0 ; i < messageCnt; i++) {
				messages[i] = new TMailMessage(resultMessages[i]);
			}
		}
		return messages;
	}
}
