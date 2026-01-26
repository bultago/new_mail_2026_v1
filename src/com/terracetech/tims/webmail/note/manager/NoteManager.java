package com.terracetech.tims.webmail.note.manager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.mail.Flags;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.dao.SystemConfigDao;
import com.terracetech.tims.webmail.mail.dao.CacheEmailDao;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.mail.ibean.MailMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MessageParser;
import com.terracetech.tims.webmail.mail.manager.XCommandHandler;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.UserInfo;
import com.terracetech.tims.webmail.mailuser.dao.MailUserDao;
import com.terracetech.tims.webmail.note.dao.NotePolicyDao;
import com.terracetech.tims.webmail.note.vo.NotePolicyCondVO;
import com.terracetech.tims.webmail.note.vo.NotePolicyVO;
import com.terracetech.tims.webmail.util.QuotaUtil;

@Service
@Transactional
public class NoteManager {

	private TMailStore store = null;
	private I18nResources msgResource = null;
	private NotePolicyDao notePolicyDao = null;
	private NoteHandler noteHandler = null;
	private XCommandHandler commandHandler = null;
	private CacheEmailDao emailDAO = null;
	private MailUserDao mailUserDao = null;
	private SystemConfigDao systemConfigDao = null;
	
	public void setStore(TMailStore store) {
		this.store = store;
	}

	public void setNotePolicyDao(NotePolicyDao notePolicyDao) {
		this.notePolicyDao = notePolicyDao;
	}
	
	public void setNoteHandler(NoteHandler noteHandler) {
		this.noteHandler = noteHandler;
	}

	public void setCommandHandler(XCommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}
	
	public void setEmailDAO(CacheEmailDao emailDAO) {
		this.emailDAO = emailDAO;
	}
	
	public void setMailUserDao(MailUserDao mailUserDao) {
		this.mailUserDao = mailUserDao;
	}

	public void setSystemConfigDao(SystemConfigDao systemConfigDao) {
		this.systemConfigDao = systemConfigDao;
	}

	public void noteInit(TMailStore store, I18nResources msgResource) throws MessagingException {
		this.store = store;
		this.msgResource = msgResource;
		noteHandler.setStore(store);
		noteHandler.setMsgResource(msgResource);
		noteHandler.createDefaultFolders();
		setCommandResource();
	}
	
	public void setCommandResource() throws MessagingException {
		commandHandler.setResource(store.getFolder(NoteHandler.INBOX).getCustomCommand(), msgResource.getLocale());
	}
	
	public int readNoteUnseenCount() {
		return noteHandler.readNoteUnseenCount();
	}

	public Map<String, String> getNoteConnectInfo(User user) {
		Map<String, String> confMap = new HashMap<String, String>();
	
		String arg = " 20 0 0 1 0 0 " + user.get(User.MAIL_USER_SEQ) + " " + user.get(User.MAIL_DOMAIN_SEQ);

		confMap.put(User.MAIL_UID, user.get(User.MAIL_UID));
		confMap.put(User.MAIL_DOMAIN, user.get(User.MAIL_DOMAIN));
		confMap.put(User.EMAIL, user.get(User.EMAIL));
		confMap.put(User.MAIL_HOST, user.get(User.MAIL_HOST));
		confMap.put(User.IMAP_LOGIN_ARGS, user.get(User.MESSAGE_STORE) + EnvConstants.getBasicSetting("note.home")+ arg);	
		
		return confMap;
	}
	
	public Map<String, String> getNoteConnectInfo(int mailUserSeq, int mailDomainSeq) {
		
		Map<String, Object> userMap = mailUserDao.readMailUserConnectionInfo(mailUserSeq, mailDomainSeq);
		if (userMap == null || userMap.isEmpty()) {
			return null;
		}	
		UserInfo user = new UserInfo();
		user.setUserInfoMap(userMap);		
		
		Map<String, String> confMap = new HashMap<String, String>();
	
		String arg = " 20 0 0 1 0 0 " + mailUserSeq + " " + mailDomainSeq;

		confMap.put(User.MAIL_UID, user.get(User.MAIL_UID));
		confMap.put(User.MAIL_DOMAIN, user.get(User.MAIL_DOMAIN));
		confMap.put(User.EMAIL, user.get(User.MAIL_UID)+ "@" + user.get(User.MAIL_DOMAIN));
		confMap.put(User.MAIL_HOST, systemConfigDao.getHostName(user.get(User.MAIL_HOST)));
		confMap.put(User.IMAP_LOGIN_ARGS, user.get(User.MESSAGE_STORE) + EnvConstants.getBasicSetting("note.home")+ arg);	
		
		return confMap;
	}
	
	public MailSortMessageBean[] getXSortMessageBeans(String folderName, MessageSortInfoBean sortInfo) throws Exception {
		 return commandHandler.getXSortMessageBeans(sortInfo, null, folderName);
	}

	public NotePolicyVO readNotePolicy(int mailUserSeq) {
		return notePolicyDao.readNotePolicy(mailUserSeq);
	}
	
	public void saveNotePolicy(NotePolicyVO notePolicyVo) {
		notePolicyDao.saveNotePolicy(notePolicyVo);
	}
	
	public void modifyNotePolicy(NotePolicyVO notePolicyVo) {
		notePolicyDao.modifyNotePolicy(notePolicyVo);
	}
	
	public void saveNotePolicyCond(NotePolicyCondVO notePolicyCondVo) {
		notePolicyDao.saveNotePolicyCond(notePolicyCondVo);
	}
	
	public void deleteNotePolicyCond(int mailUserSeq) {
		notePolicyDao.deleteNotePolicyCond(mailUserSeq);
	}
	
	public List<NotePolicyCondVO> readNotePolicyCondList(int mailUserSeq) {
		return notePolicyDao.readNotePolicyCondList(mailUserSeq);
	}
	
	public int getXCommandTotal() throws Exception {		
		return commandHandler.getTotalCnt();		
	}
	
	public TMailFolder getFolder(String folderName) throws MessagingException {
		return noteHandler.getFolder(folderName);
	}
	
	public List<MailAddressBean> readDomainUserList(int mailDomainSeq, String keyWord, boolean isAutoComplte) {
		return emailDAO.readDomainEmailList(mailDomainSeq, keyWord, isAutoComplte);
	}
	
	public boolean checkNotePolicyCondMe(int mailUserSeq, int condTarget) {
		return (notePolicyDao.checkNotePolicyCondMe(mailUserSeq, condTarget) > 0) ? true : false;
	}
	
	public void changeFlag(TMailMessage message, char flagType, boolean use) throws MessagingException {
		noteHandler.changeFlag(message, flagType, use);
	}
	
	public void setMDNFlag(InternetAddress mdnAddr, TMailFolder folder, String messageId, String charset) throws UnsupportedEncodingException, MessagingException {
		noteHandler.setMDNFlag(mdnAddr, folder, messageId, charset);
	}
	
	public String getMessageId(MimeMessage message) throws MessagingException {
		String mid = message.getMessageID();
		if(mid.charAt(0) == '<' && mid.charAt(mid.length()-1) == '>') {
			mid = mid.substring(1, mid.length()-1);
		}
		return mid;
	}
	
	public List<Map<String, Object>> getMdnMessageList(long[] uids) throws MessagingException {
		TMailFolder folder = noteHandler.getFolder(NoteHandler.SENT);
		folder.open(false);
		TMailMessage[] messages = folder.getMessagesByUID(uids);
		List<Map<String, Object>> mdnList = new ArrayList<Map<String, Object>>();
		Map<String, Object> mdnMap = null;
		String mid = null;
		for (int i=0; i< messages.length; i++) {
			mdnMap = new HashMap<String, Object>();
			
			mid = messages[i].getMessageID();
			mid = mid.replaceAll("<", "");
			mid = mid.replaceAll(">", "");
			
			mdnMap.put("messageId", mid);
			mdnMap.put("uid", uids[i]);
			
			folder.setMDNResponseInfo(messages[i]);
			if (messages[i].getMDNResponses() != null && messages[i].getMDNResponses().length > 0) {
				mdnMap.put("code", messages[i].getMDNResponses()[0].getCode());
				mdnMap.put("date", messages[i].getMDNResponses()[0].getSentDate4());
			}
			
			mdnList.add(mdnMap);
		}
		folder.close(false);
		return mdnList;
	}
	
	public long[] getNeighborUIDs(long uid, TMailFolder folder, MessageSortInfoBean infoBean)
	throws MessagingException {
		return noteHandler.getNeighborUIDs(uid, folder, infoBean);
	}
	
	public MailMessageBean readNoteMessage(TMailFolder folder, long uid, MessageParserInfoBean bean) throws Exception {
		TMailMessage message = folder.getMessageByUID(uid, true);
		message.setDirectRead(true);
		
		MessageParser mParser = new MessageParser();
		MailMessageBean messageBean = mParser.parseMessage(message, bean);		
		return messageBean;
	}
	
	public void moveMessageToFolder(long[] uids, String fromFolderName, String toFolderName) throws MessagingException {
		noteHandler.moveMessageToFolder(uids, fromFolderName, toFolderName);
	}
	
	public void deleteNoteMessage(long[] uids, TMailFolder folder) throws MessagingException {
		noteHandler.deleteNoteMessage(uids, folder);
	}
	
	public int readNoteSaveCount(int mailDomainSeq, int mailGroupSeq, int mailUserSeq) {
		return mailUserDao.readNoteSaveCount(mailDomainSeq, mailGroupSeq, mailUserSeq);
	}
	
	public int readNoteSaveDate(int mailDomainSeq, int mailGroupSeq, int mailUserSeq) {
		return mailUserDao.readNoteSaveDate(mailDomainSeq, mailGroupSeq, mailUserSeq);
	}
}
