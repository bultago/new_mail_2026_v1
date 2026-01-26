package com.terracetech.tims.webmail.mail.manager;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Store;

import org.eclipse.angus.mail.pop3.POP3Folder;
import org.eclipse.angus.mail.pop3.POP3Message;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.setting.dao.SettingPop3Dao;
import com.terracetech.tims.webmail.setting.vo.Pop3VO;
import com.terracetech.tims.webmail.util.StringUtils;

public class Pop3Manager {
 
	private SettingPop3Dao popDao = null;
	private final static String POP3_PROTOCOL = "pop3";
	 //pops protocol ����
    private final static String POP3S_PROTOCOL = "pop3s";
    //ssl ��뿩��?
    private final static String POP3_SSL_ON="1";
	private Pop3VO pop3 = null;
	
	public void setPopDao(SettingPop3Dao popDao) {
		this.popDao = popDao;
	}

	public void setPop3(Pop3VO pop3) {
		this.pop3 = pop3;
	}
	
	public Map<String, String> getPop3Config() {
		
		Map<String, String> configMap = new HashMap<String, String>();
		 // ssl ����ϸ�? protocol�� "pops"�� �ƴϸ� pop����
        configMap.put("protocol", POP3_SSL_ON.equals(pop3.getUsedSsl()) ? POP3S_PROTOCOL:POP3_PROTOCOL);
		configMap.put("host", pop3.getPop3Host());
		configMap.put("id", pop3.getPop3Id());
		configMap.put("port", Integer.toString(pop3.getPop3Port()));
		configMap.put("pass", pop3.getPop3Passwd());
		
		return configMap;
	}
	
	public Folder getPOP3Folder(Store store) throws MessagingException {
		Folder folder = store.getFolder(FolderHandler.INBOX);
		return folder;		
	}
	
	public TMailFolder getMyFolder(TMailStore store) throws MessagingException {
		String mailBox = pop3.getPop3Boxname();
		int index = mailBox.indexOf(' ');
		if (index != -1) {
			mailBox = mailBox.substring(index+1);
		}
		TMailFolder folder = store.getFolder(mailBox);
		
		if(!folder.exists()) {
			folder.create();
		}
		
		folder.open(true);
		return folder;		
	}
	
	public boolean haveMsgName(Folder folder, Message message) throws MessagingException {
		boolean haveMsg = false;
		String msgName = pop3.getPop3Msgname();
		String pop3Msgname = getPop3MsgName(folder, message);
		
		if (StringUtils.isEmpty(msgName)) {
			return false;
		}
		
		if (msgName.equalsIgnoreCase(pop3Msgname)) haveMsg = true;
		
		return haveMsg;		
	}
	
	public void getPop3Message(TMailFolder myFolder, Folder folder, Message message, int userSeq) throws MessagingException, IOException {
		message.getContent();
		if("true".equalsIgnoreCase(EnvConstants.getMailSetting("mail.debug")))
			System.out.println(this.getClass().getName() +" - ["+myFolder.getFullName()+"] message.getSubject() =>"+message.getSubject()+"  :  append START");
		
		myFolder.getIMAPFolder().appendMessages(new Message[]{message});
		
		if("true".equalsIgnoreCase(EnvConstants.getMailSetting("mail.debug")))
			System.out.println(this.getClass().getName() +" - ["+myFolder.getFullName()+"] message.getSubject() =>"+message.getSubject()+"  :  append END");
		
		modifyMsgName(userSeq, getPop3MsgName(folder, message));
	}
	
	public boolean isQuotaFull(Throwable t) {
		String str = t.getMessage();

		if (str != null &&
			(str.toLowerCase()).indexOf("exceeded your quota") > 0) {
			return true;
		}

		return false;
	}
	
	public void modifyMsgName(int userSeq, String pop3Msgname) {
		popDao.modifyPop3Msgname(userSeq, pop3.getPop3Host(), pop3.getPop3Id(), pop3Msgname);
	}
	
	public String getPop3MsgName(Folder folder, Message message) throws MessagingException {
		return ((POP3Folder)folder).getUID(message);
	}
}
