/**
 * FolderHandler.java 2008. 11. 21.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager;

import java.util.Arrays;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.quartz.utils.Key;

import com.sun.mail.imap.Quota;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailFolderBean;
import com.terracetech.tims.webmail.mail.ibean.MailQuotaBean;
import com.terracetech.tims.webmail.mail.vo.SharedFolderVO;

/**
 * <p>
 * <strong>FolderHandler.java</strong> Class Description
 * </p>
 * <p>
 * �ֿ伳��
 * </p>
 * <ul>
 * <li>Mail ����� �� ó�� Ŭ����.</li>
 * <li>�߰� ���� ���� �� List  �������� ���� handling  �޼��� ����.</li>
 * </ul>
 * 
 * @author sshyun
 * @since Tims7
 * @version 7.0
 */
@SuppressWarnings("all")
public class FolderHandler {

	public static final String INBOX = "Inbox";
	public static final String SENT = "Sent";
	public static final String DRAFTS = "Drafts";
	public static final String TRASH = "Trash";
	public static final String SPAM = "Spam";
	public static final String RESERVED = "Reserved";	
	public static final String QUOTAVIOLATE = "Quotaviolate";
	public static final String BIGATTACH = "/etc/BIGATTACH";
	public static final String BIGATTACHHOME = "BIGATTACHROOT";
	public static final String[] STATUS_ARGS = { "RECENT", "UNSEEN",
		"MESSAGES", "X-DISKUSAGE" };

	private static final int MAX_DELETE_COUNT = 1000;

	private TMailFolder defaultFolder = null;
	private Map statusMap = null;

	private TMailStore store = null;
	private I18nResources msgResource = null;

	/**
	 * @return store �� ��ȯ
	 */
	public TMailStore getStore() {
		return store;
	}

	/**
	 * @param store �Ķ���͸� store���� ����
	 */
	public void setStore(TMailStore store) {
		this.store = store;
	}

	/**
	 * @param msgResource �Ķ���͸� msgResource���� ����
	 */
	public void setMsgResource(I18nResources msgResource) {
		this.msgResource = msgResource;
	}

	/**
	 * @return msgResource �� ��ȯ
	 */
	public I18nResources getMsgResource() {
		return msgResource;
	}

	/**
	 * <p>FolderHandler �� Store ��� �� �޼��� Resource ����.</p>
	 *
	 * @param store				����� Ŭ���� ��ü
	 * @param msgResource	�޼��� ���ҽ�
	 * @throws MessagingException
	 * @return void
	 */
	public void setResource(TMailStore store, I18nResources msgResource)
			throws MessagingException {
		this.store = store;
		defaultFolder = store.getDefaultFolder();
		this.msgResource = msgResource;
	}

	/**
	 * <p>�⺻ ����� ����Ʈ ��ȯ.</p>
	 * 
	 * @throws MessagingException
	 * @return MailFolderBean[]
	 */
	public MailFolderBean[] getDefaultFolders(boolean isQuotaSet) throws MessagingException {
		MailFolderBean[] folderBeans = null;
		MailQuotaBean quotaBean = null;
		initFolderStatus();	
		
		quotaBean = getRootQuota(FolderHandler.INBOX);		
		
		long quotaOverMsgCnt = getStatus(statusMap, FolderHandler.QUOTAVIOLATE, "MESSAGES");
		boolean isOverQuota = quotaBean.isOverQuota();
		isOverQuota = (isOverQuota || quotaOverMsgCnt > 0);
		
		MailFolderSorter sorter = new MailFolderSorter();
		TMailFolder[] folders = sorter.getSortedFoldersByDefault(isOverQuota,defaultFolder
				.list("*"));
		int folderLength = folders.length;

		folderBeans = new MailFolderBean[folderLength];

		// TODO ���� �̽��� �߻��ϸ� ���� �ϴ� ������ �ѹ����� �ٿ��� ��
		String fname = null;		
		for (int i = 0; i < folderLength; i++) {
			fname = folders[i].getFullName();
			folderBeans[i] = new MailFolderBean(settingAlias(folders[i],fname));			
			folderBeans[i].setId("dfolderNode" + i);
			folderBeans[i].setNewCnt(getStatus(statusMap, fname, "RECENT"));
			folderBeans[i].setUnseenCnt(getStatus(statusMap, fname, "UNSEEN"));
			folderBeans[i].setTotalCnt(getStatus(statusMap, fname, "MESSAGES"));
			if(isQuotaSet){
				folderBeans[i].setQuota(
						new MailQuotaBean((getStatus(statusMap, fname, "X-DISKUSAGE")),
								quotaBean.getLimit()));
			}
		}		

		// TIMSSEVEN-9809 : OOM
		statusMap = null; 
		
		return folderBeans;
	}

	/**
	 * <p>����� ������ ����Ʈ ��ȯ</p>
	 *
	 * @return
	 * @throws MessagingException
	 * @return MailFolderBean[]
	 */
	public MailFolderBean[] getUserFolders(boolean isQuotaSet, 
			Map<String, SharedFolderVO> sharedFolderMap) 
	throws MessagingException {
		MailQuotaBean quotaBean = null;		
		initFolderStatus();
		if(isQuotaSet){
			quotaBean = getRootQuota(FolderHandler.INBOX);
		}

		TMailFolder[] folders = defaultFolder.list("*");
		int folderLength = folders.length;

		Arrays.sort(folders);
		MailFolderBean bean;
		Vector<MailFolderBean> folderList = new Vector<MailFolderBean>();
		String fname = null;
		String encFname = null;
		for (int i = 0; i < folderLength; i++) {
			if (!isDefaultFolder(folders[i])) {
				fname = folders[i].getFullName();
				encFname = folders[i].getEncName();
				bean = new MailFolderBean(settingAlias(folders[i],fname));				
				bean.setId("dfolderNode" + i);

				bean.setNewCnt(getStatus(statusMap, fname,"RECENT"));
				bean.setUnseenCnt(getStatus(statusMap,fname, "UNSEEN"));
				bean.setTotalCnt(getStatus(statusMap, fname,"MESSAGES"));
				
				if(sharedFolderMap != null && 
					sharedFolderMap.containsKey(encFname)){
					bean.setShare(true);
					bean.setSharedUid(sharedFolderMap.get(encFname).getFolderUid());
				}
				
				if(isQuotaSet){
					bean.setQuota(
							new MailQuotaBean((getStatus(statusMap, fname, "X-DISKUSAGE")),
									quotaBean.getLimit()));
				}
				folderList.add(bean);
			}
		}
		
		// TIMSSEVEN-9809 : OOM
		statusMap = null; 

		return folderList.toArray(new MailFolderBean[folderList.size()]);
	}

	/**
	 * <p>��� ������ ����Ʈ ��ȯ</p>
	 *
	 * 
	 * @throws MessagingException
	 * @return MailFolderBean[]
	 */
	public MailFolderBean[] getAllFolders(boolean isQuotaSet) throws MessagingException {
		MailFolderBean[] defaultFolders = getDefaultFolders(isQuotaSet);
		MailFolderBean[] userFolders = getUserFolders(isQuotaSet,null);

		MailFolderBean[] dest = new MailFolderBean[defaultFolders.length
				+ userFolders.length];

		System.arraycopy(defaultFolders, 0, dest, 0, defaultFolders.length);
		System.arraycopy(userFolders, 0, dest, defaultFolders.length,
				userFolders.length);

		return dest;
	}
	
	public TMailFolder getFolder(String folderName) throws MessagingException {
		return settingAlias(store.getFolder(folderName), null);
	}

	/**
	 * <p>�⺻ ������ ���� ���� �˻�</p> 
	 *
	 * @param folder
	 * @return boolean			true �⺻ ������, false ����� ������.
	 */
	private boolean isDefaultFolder(TMailFolder folder) {
		if (MailFolderSorter.getDefaultBoxPos(folder.getFullName()) >= 0)
			return true;

		return false;
	}

	/**
	 * <p>������ Status ���� ��ȯ.</p>
	 * <p></p>
	 *
	 * @param statusMap
	 * @param folderName
	 * @param valueKey
	 * @return long
	 */
	private long getStatus(Map statusMap, String folderName, String valueKey) {
		long status = 0;
		if(statusMap.containsKey(folderName)){
			status = Long.parseLong((String) (((Map)statusMap.get(folderName))
					.get(valueKey))); 
		} else {
			try{
				Thread.sleep(1000);
			}catch (Exception e){}
			status = getFolderStatus(statusMap,folderName, valueKey);			
		}
		return status;
	}
	
	private long getFolderStatus(Map statusMap, String folderName, String valueKey){
		long status = 0;
		try {
			TMailFolder folder = store.getFolder(folderName);
			folder.status(STATUS_ARGS);
			Map folderStatusMap = folder.getStatusHT();
			statusMap.put(folderName, folderStatusMap);
			status = Long.parseLong((String)folderStatusMap.get(valueKey));			
		} catch (Exception e) {
		}
		
		return status;
	}

	/**
	 * <p>��� �������� Status ������ ���� statusMap �� ����.</p>
	 * <p>�⺻ �׸���  "RECENT", "UNSEEN","MESSAGES", "X-DISKUSAGE" �׸�</p>
	 *
	 * @throws MessagingException
	 * @return void
	 */
	private void initFolderStatus() throws MessagingException {		
		statusMap = getStoreFoldersStatus(STATUS_ARGS);		
	}

	/**
	 * <p>������ Status ���� ���� </p>
	 *  <p>�⺻ �׸���  "RECENT", "UNSEEN","MESSAGES", "X-DISKUSAGE" �׸�</p>
	 *
	 * @throws MessagingException
	 * @return void
	 */
	private void resetFolderStatus() throws MessagingException {
		statusMap = getStoreFoldersStatus(STATUS_ARGS);
	}

	/**
	 * <p>�Էµ� status ������ ������ x-status�� �̿��Ͽ� ��� �������� status ������ ������.</p>
	 *
	 * @param args				������ Status ���� 
	 * @return
	 * @throws MessagingException
	 * @return Map				status ���� Map
	 */
	private Map getStoreFoldersStatus(String[] args) throws MessagingException {
		return defaultFolder.xstatus(args);
	}

	/**
	 * <p>������ ��Ī �̸� ����. </p>
	 * <p>����������, ����������, ���������, �ӽ�������, ����������, �������� ��Ī ����.</p>
	 *
	 * @param folder					������
	 * @param name					�������� Fullname
	 * @return
	 * @return TMailFolder			��Ī ������ ������.
	 */
	public TMailFolder settingAlias(TMailFolder folder, String name) {

		if (name == null) {
			name = folder.getFullName();
		}

		if (name.equalsIgnoreCase(INBOX)) {
			folder.setAlias(msgResource.getMessage("folder.inbox"));
		} else if (name.equals(SENT)) {
			folder.setAlias(msgResource.getMessage("folder.sent"));
		} else if (name.equals(DRAFTS)) {
			folder.setAlias(msgResource.getMessage("folder.drafts"));
		} else if (name.equals(RESERVED)) {
			folder.setAlias(msgResource.getMessage("folder.reserved"));
		} else if (name.equals(SPAM)) {
			folder.setAlias(msgResource.getMessage("folder.spam"));
		} else if (name.equals(TRASH)) {
			folder.setAlias(msgResource.getMessage("folder.trash"));
		} else if (name.equals(QUOTAVIOLATE)) {
			folder.setAlias(msgResource.getMessage("folder.quotaviolate"));
		} else {
			folder.setAlias(name);
		}

		return folder;
	}

	/**
	 * <p>�⺻ ������ ��.</p>
	 * <p>����������, ����������, ���������, �ӽ�������, ����������, ������ ��</p>
	 *
	 * 
	 * @return void
	 */
	public void createDefaultFolders() {
		
		try {
			TMailFolder[] afolders = defaultFolder.list("*");
			Stack<String> dfolderStack = new Stack<String>();
			dfolderStack.push(INBOX);
			dfolderStack.push(SENT);
			dfolderStack.push(DRAFTS);
			dfolderStack.push(TRASH);
			dfolderStack.push(SPAM);
			dfolderStack.push(RESERVED);
			dfolderStack.push(QUOTAVIOLATE);			
			for (int i = 0; i < afolders.length; i++) {
				TMailFolder tMailFolder = afolders[i];
				dfolderStack.remove(afolders[i].getEncName());
			}			
			while(!dfolderStack.isEmpty()){
				store.getFolder(dfolderStack.pop()).create();
			}
			afolders = null;
			dfolderStack = null;
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage());			
		}		
	}

	/**
	 * <p>������ ��.</p>
	 *
	 * @param name			�� ������ �̸�.
	 * @return
	 * @throws MessagingException
	 * @return boolean		�̹� �����ϴ����� �ߺ����� 
	 */
	public boolean makeFolder(String name) throws MessagingException {
		boolean isExist = false;
		TMailFolder folder = store.getFolder(name);
		if (!folder.exists()) {
			folder.create();
		} else {
			isExist = true;
		}
		return isExist;
	}

	/**
	 * <p>������ ����</p>
	 *
	 * @param name			���� ������ �̸�
	 * @throws MessagingException
	 * @return void
	 */
	public void deleteFolder(String name) throws MessagingException {
		TMailFolder folder = store.getFolder(name);
		if (folder.exists()) {
			folder.delete();
		}
	}

	/**
	 * <p>������ �̸� ����</p>
	 *
	 * @param previousName				���� ������ �̸�
	 * @param changeName				���� �� ������ �̸�
	 * @return
	 * @throws MessagingException
	 * @return boolean						������ �̸����� �������� ���� �ϴ����� ���� ����.
	 */
	public boolean modifyFolder(String previousName, String changeName)
			throws MessagingException {

		boolean isExist = false;
		TMailFolder changeFolder = store.getFolder(changeName);

		if (!changeFolder.exists()) {
			TMailFolder previousFolder = store.getFolder(previousName);
			previousFolder.renameTo(changeFolder);
		} else {
			isExist = true;
		}

		return isExist;
	}

	/**
	 * <p>������ ����.  ��� ���� ����.</p>
	 *
	 * @param name			��� ������ �̸�.
	 * @throws MessagingException
	 * @return void
	 */
	public void cleanFolder(String name) throws MessagingException {
		TMailFolder folder = store.getFolder(name);

		folder.open(true);
		TMailMessage[] messages = folder.getMessages();

		if (messages.length > 0) {
			folder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
			folder.expunge();
		}
		folder.close(true);
	}

	/**
	 * <p>������ ����, �������� �ƴҰ�� ���������� �̵�, �������ϰ�� ���� ����.</p>
	 *
	 * @param name						������ �̸�
	 * @throws MessagingException
	 * @return void
	 */
	public void emptyFolder(String name) throws MessagingException {
		TMailFolder folder = store.getFolder(name);
		
		folder.open(true);
        int messageCount = folder.getMessageCount();
        
        if (messageCount > 0) {
            int term = messageCount / MAX_DELETE_COUNT;
            int mod = messageCount % MAX_DELETE_COUNT;
            
            if (mod > 0) {
            	term += 1;
            }
         
            Folder imapFolder = folder.getIMAPFolder();
            if (name.equals(TRASH)|| name.equals(SPAM)) {
                Message[] messages = null;
                for (int i=1; i<=term; i++) {
                	int start  = (i - 1) * MAX_DELETE_COUNT + 1;
                	int end = i * MAX_DELETE_COUNT;
                	if (i == term && mod > 0) {
                		end = start + mod - 1;
                	}
                	messages = folder.getLimitMessages(start, end);
                	imapFolder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
                	messages = null;
                } 
            } else {
            	Message firstMessage = imapFolder.getMessage(1);
            	Message lastMessage = imapFolder.getMessage(messageCount);
                long start = folder.getUID(firstMessage);
                long end = folder.getUID(lastMessage);
            	String uids = start + ":" + end;
            	folder.xmove2(uids, TRASH);
            }
        }
        folder.close(true);
	}

	/**
	 * <p>���� �޼��� �̵�. �������� ������ ������ �ٸ� ���������� �̵�.</p>
	 *
	 * @param uids						�̵��� �޼��� ID
	 * @param fromFolderName		�޼��� ������
	 * @param toFolderName			�ű� ������.
	 * @throws MessagingException
	 * @return void
	 */
	public void moveMessageToFolder(long[] uids, String fromFolderName,
			String toFolderName) throws MessagingException {

		TMailFolder fromFolder = store.getFolder(fromFolderName);

		fromFolder.open(true);
		fromFolder.xmove(uids, toFolderName);

		fromFolder.close(true);
	}

	/**
	 * <p>���� �޼��� ����. �������� ������ ������ �ٸ� ���������� ����.</p>
	 *
	 * @param uids					������ �޼��� ID
	 * @param fromFolderName	�޼��� ������
	 * @param toFolderName		������ ������.
	 * @throws MessagingException
	 * @return void
	 */
	public void copyMessageToFolder(long[] uids, String fromFolderName,
			String toFolderName) throws MessagingException {

		TMailFolder fromFolder = store.getFolder(fromFolderName);
		TMailFolder toFolder = store.getFolder(toFolderName);

		fromFolder.open(true);
		TMailMessage[] messages = fromFolder.getMessagesByUID(uids);
		fromFolder.copyMessages(messages, toFolder);
		fromFolder.close(true);
	}
	
	public void copyMessageFolderToFolder(
			long[] uids, 
			TMailFolder fromFolder,
			TMailFolder toFolder) throws MessagingException {
		

		fromFolder.open(false);
		TMailMessage[] messages = fromFolder.getMessagesByUID(uids);
		fromFolder.copyMessages(messages, toFolder);
		fromFolder.close(false);
	}
	
	public MailQuotaBean getRootQuota(String root) throws MessagingException{
		MailQuotaBean quotaBean = new MailQuotaBean();		
		Quota[] quotas = store.getQuota(root);
		long usage = quotas[0].resources[0].usage * 1024;
		long limit = quotas[0].resources[0].limit * 1024;

        int percent = (int) ((double) (usage * 10) / limit * 100);
		percent = (int) java.lang.Math.round((double) percent / 10);
		
		quotaBean.setUsage(usage);
		quotaBean.setLimit(limit);
		quotaBean.setPercent(percent);
		
		return quotaBean;
	}

	/**
	 * <p>����� Folder ��� resource ����.</p>
	 *
	 * @throws MessagingException
	 * @return void
	 */
	public void cleanResource() throws MessagingException {
		defaultFolder = null;
		statusMap = null;
		store.close();
		store = null;
		msgResource = null;
	}
}
