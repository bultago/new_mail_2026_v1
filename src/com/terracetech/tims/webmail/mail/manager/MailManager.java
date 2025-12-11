/**
 * MailManager.java 2008. 12. 16.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.mail.search.TMailSearchQuery;
import com.terracetech.tims.mail.sort.SortMessage;
import com.terracetech.tims.mail.tag.TMailTag;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.exception.MailNotFoundException;
import com.terracetech.tims.webmail.mail.dao.CacheEmailDao;
import com.terracetech.tims.webmail.mail.dao.FolderAgingDao;
import com.terracetech.tims.webmail.mail.ibean.MDNResponsesBean;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.mail.ibean.MailFolderBean;
import com.terracetech.tims.webmail.mail.ibean.MailMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MailQuotaBean;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MailWriteMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.ibean.MessageWriteInfoBean;
import com.terracetech.tims.webmail.mail.manager.write.BbsWriteHandler;
import com.terracetech.tims.webmail.mail.manager.write.DraftWriteHandler;
import com.terracetech.tims.webmail.mail.manager.write.ForwardWriteHandler;
import com.terracetech.tims.webmail.mail.manager.write.NormalWriteHandler;
import com.terracetech.tims.webmail.mail.manager.write.ReWriteHandler;
import com.terracetech.tims.webmail.mail.manager.write.ReplyWriteHandler;
import com.terracetech.tims.webmail.mail.manager.write.WebfolderWriteHandler;
import com.terracetech.tims.webmail.mail.manager.write.WriteHandler;
import com.terracetech.tims.webmail.mail.vo.FolderAgingVO;
import com.terracetech.tims.webmail.mail.vo.SharedFolderUserVO;
import com.terracetech.tims.webmail.mail.vo.SharedFolderVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p>
 * <strong>MailManager.java</strong> Class Description
 * </p>
 * <p>
 * �ֿ伳��
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
@Service
@Transactional
public class MailManager {
	private TMailStore store;
	private I18nResources msgResource = null;
	private FolderHandler folderHandler = null;
	private JsonHandler jsonHandler = null;
	private MessageHandler messageHandler = null;
	private XCommandHandler commandHandler = null;
	private CacheEmailDao emailDAO = null;
	private FolderAgingDao agingDAO = null;
	private SharedFolderHandler sharedFolderHandler = null;

	public void setProcessResource(TMailStore store, I18nResources msgResource)
			throws MessagingException {
		this.msgResource = msgResource;
		this.store = store;
		folderHandler.setResource(store, msgResource);
		folderHandler.createDefaultFolders();
		setCommandResource();
	}

	public void setFolderHandler(FolderHandler folderHandler) {
		this.folderHandler = folderHandler;
	}

	public void setJsonHandler(JsonHandler jsonHandler) {
		this.jsonHandler = jsonHandler;
	}

	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}
	
	public void setCommandHandler(XCommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}
	
	public void setSharedFolderHandler(SharedFolderHandler sharedFolderHandler) {
		this.sharedFolderHandler = sharedFolderHandler;
	}

	public void setEmailDAO(CacheEmailDao emailDAO) {
		this.emailDAO = emailDAO;
	}
	
	public void setAgingDAO(FolderAgingDao agingDAO) {
		this.agingDAO = agingDAO;
	}
	
	public void setCommandResource() throws MessagingException {
		commandHandler.setResource(store.getFolder(
				FolderHandler.INBOX).getCustomCommand(),msgResource.getLocale());
	}
	
	public int getSortTotal(TMailStore store, String folderName,
			MessageSortInfoBean sortInfo) throws Exception {

		messageHandler.setResource(store.getFolder(folderName), msgResource);
		return messageHandler.getSortTotal(sortInfo);
	}
	
	public TMailMessage[] getMessages(TMailStore store, String folderName,
			MessageSortInfoBean sortInfo) throws Exception {

		messageHandler.setResource(store.getFolder(folderName), msgResource);
		return messageHandler.getMessages(sortInfo);
	}

	public MailMessageBean[] getMessageBeans(TMailStore store,
			String folderName, MessageSortInfoBean sortInfo) throws Exception {

		messageHandler.setResource(store.getFolder(folderName), msgResource);
		return messageHandler.getMessageBeans(sortInfo);
	}
	
	public TMailMessage getMessage(String folderName, long uid) throws Exception {
		TMailFolder folder = store.getFolder(folderName);
		messageHandler.setResource(folder, msgResource);

		folder.open(true);
		TMailMessage message = messageHandler.getMessge(uid);
		message.setDirectRead(true);
		
		folder.close(true);
		return message;
	}
	
	public TMailMessage[] getMessage(TMailFolder folder, long[] uids) throws Exception {		
		messageHandler.setResource(folder, msgResource);
		TMailMessage[] messages = messageHandler.getMessges(uids);
		return messages;
	}

	public MailMessageBean getMessageBean(TMailFolder folder, long uid,
			MessageParserInfoBean bean) throws Exception {
		
		messageHandler.setResource(folder, msgResource);		
		TMailMessage message = messageHandler.getMessge(uid);
		if(message == null){
			throw new MailNotFoundException("READ MESSAGE IS NULL UID["+uid+"]");
		}
		message.setDirectRead(true);

		MessageParser mParser = new MessageParser();
		MailMessageBean messageBean = mParser.parseMessage(message, bean);		
		return messageBean;
	}
	
	public MailMessageBean getMessageBean(TMailFolder folder,
			Session session,
			MimeMessage msg,
			MessageParserInfoBean bean) throws Exception {
		
		messageHandler.setResource(folder, msgResource);		
		TMailMessage message = new TMailMessage(msg);
		MessageParser mParser = new MessageParser();
		MailMessageBean messageBean = mParser.parseMessage(message, bean);		
		return messageBean;
	}
	
	public MailMessageBean parseMessage(TMailMessage message,
			MessageParserInfoBean bean) 
	throws Exception {		
		message.setDirectRead(true);
		MessageParser mParser = new MessageParser();		
		return mParser.parseMessage(message, bean);
	}
	
	public long removeAttachFile(String folderName, long uid, 
			String part, String userId, String tmpPath) 
	throws Exception {		
		TMailFolder folder = store.getFolder(folderName);
		messageHandler.setResource(folder, msgResource);
		return messageHandler.removeAttachFile(uid, part, userId, tmpPath);		
	}
	
	public long[] getNeighborUID(TMailFolder folder, long uid, MessageSortInfoBean infoBean)
	throws Exception {
		long[] neighborUID = null;		
		messageHandler.setResource(folder, msgResource);
		neighborUID = messageHandler.getNeighborUIDs(uid, infoBean);		
		return neighborUID;
	}

	public MailQuotaBean getQuotaRootInfo(String folder) throws Exception {
		return folderHandler.getRootQuota(folder);
	}

	public JSONArray getJsonFolderList(int type, boolean isQuotaSet, int userSeq) throws Exception {
		return jsonHandler.getJsonFolderList(getFolderList(type,isQuotaSet, userSeq));
	}

	public MailFolderBean[] getFolderList(int type, boolean isQuotaSet, int userSeq) throws Exception {
		MailFolderBean[] folders = null;
		switch (type) {
		case EnvConstants.DEFAULT_FOLDER:
			folders = folderHandler.getDefaultFolders(isQuotaSet);
			break;
		case EnvConstants.USER_FOLDER:			
			Map<String, SharedFolderVO> map = null;
			if(userSeq > -1){
				map = sharedFolderHandler.getUserSharedFolderMap(userSeq);
			}
			folders = folderHandler.getUserFolders(isQuotaSet,map);
			break;
		}

		return folders;
	}
	
	public MailFolderBean[] getUserAgingFolderList(int userSeq, boolean isQuotaSet) 
	throws Exception {		
		MailFolderBean[] folders = getFolderList(EnvConstants.USER_FOLDER, isQuotaSet, userSeq);
		List agingList = agingDAO.getAgingInfo(userSeq);
		int agingListSize = agingList.size();
		if(agingListSize > 0){
			Map agingMap = new HashMap();
			FolderAgingVO fvo = null;
			for (int i = 0; i < agingListSize; i++) {
				fvo = (FolderAgingVO)agingList.get(i);
				agingMap.put(fvo.getFolderName(), fvo.getAging());
			}
			for (int i = 0; i < folders.length; i++) {
				if(agingMap.containsKey(folders[i].getEncName())){
					folders[i].setAgaing((Long)agingMap.get(folders[i].getEncName()));
				}
			}
			
			fvo = null;
			agingMap = null;
		}
		
		return folders;
	}
	
	@Transactional
	public void saveUserFolderAging(int userSeq, int preAgingDay, int newAgingDay, String folderName) 
	throws Exception{
		if(newAgingDay < 0){
			agingDAO.deleteAgingInfo(userSeq, folderName);
		} else if(preAgingDay < 0){
			agingDAO.deleteAgingInfo(userSeq, folderName);
			agingDAO.addAgingInfo(userSeq, newAgingDay, folderName);
		} else {
			agingDAO.setAgingInfo(userSeq, newAgingDay, folderName);
		}
	}
	
	public TMailFolder getFolder(String folderName) throws Exception{
		return folderHandler.getFolder(folderName);
	}
	
	public void switchMessagesFlags(long[] uids, String folderName, 
			String flagType, boolean used)
	throws Exception {
		
		TMailFolder folder = store.getFolder(folderName);
		messageHandler.setResource(folder, msgResource);		
		messageHandler.switchFlags(uids, flagType.charAt(0), used);
	}
	
	public void deleteMessage(long[] uids, String folderName)
	throws Exception {
		TMailFolder folder = store.getFolder(folderName);
		messageHandler.setResource(folder, msgResource);
		try{
			messageHandler.deleteMessages(uids);
		}catch (Exception e) {
			throw new Exception("Delete MESSAGE ERROR");
		}
	}
	
	public void cleanMessage(long[] uids, String folderName)
	throws Exception {
		TMailFolder folder = store.getFolder(folderName);
		messageHandler.setResource(folder, msgResource);
		try{
			messageHandler.cleanMessages(uids);
		}catch (Exception e) {
			throw new Exception("CELAN MESSAGE ERROR UIDS["+StringUtils.getLongsToString(uids)+"]");
		}
	}
	
	public void moveMessage(long[] uids, String fromFolderName, String toFolderName)
	throws Exception {		
		folderHandler.moveMessageToFolder(uids, fromFolderName, toFolderName);		
	}
	
	public void copyMessage(long[] uids, String fromFolderName, String toFolderName)
	throws Exception {		
		folderHandler.copyMessageToFolder(uids, fromFolderName, toFolderName);		
	}
	
	public void copySharedMessage(long[] uids, TMailFolder fromFolder, TMailFolder toFolder)
	throws Exception {		
		folderHandler.copyMessageFolderToFolder(uids, fromFolder, toFolder);		
	}
	
	public void emptyFolder(String folderName)
	throws Exception {		
		folderHandler.emptyFolder(folderName);
	}
	
	public boolean addFolder(String folderName)
	throws Exception {
		return folderHandler.makeFolder(folderName);
	}
	
	public void deleteFolder(int userSeq, String folderName)
	throws Exception {
		folderHandler.deleteFolder(folderName);
		SharedFolderVO vo = getCheckSharedFolder(userSeq, folderName);
		if(vo != null){
			deleteSharedFolder(vo.getFolderUid());
		}
		agingDAO.deleteAgingInfo(userSeq, folderName);		
	}
	
	public void modifyFolder(int userSeq, String previousName, String changeName)
	throws Exception {
		folderHandler.modifyFolder(previousName, changeName);
		SharedFolderVO vo = getCheckSharedFolder(userSeq, previousName);
		if(vo != null){
			vo.setFolderName(changeName);
			sharedFolderHandler.updateSharedFolderInfo(vo);
		}
		agingDAO.updateAgingInfo(userSeq, previousName,changeName);
	}
	
	public int getXCommandTotal() throws Exception {		
		return commandHandler.getTotalCnt();		
	}
	
	public SortMessage[] getXSortMessages(
			String folderName, String[] exceptFolders, 
			MessageSortInfoBean sortInfo) throws Exception {
		
		 return commandHandler.getXSortMessages(sortInfo, exceptFolders, folderName);
	}
    public int getXSortTotal(String folderName, String[] exceptFolders,
            MessageSortInfoBean sortInfo) throws Exception {
	
        return commandHandler.getXSortTotal(sortInfo, exceptFolders, folderName);
    }
    public MailSortMessageBean[] getXSortMessageBeans(String folderName, String[] exceptFolders,
			MessageSortInfoBean sortInfo) throws Exception {
		
		 return commandHandler.getXSortMessageBeans(sortInfo, exceptFolders, folderName);
	}
	
	public MailSortMessageBean[] getXSortRelationMessageBeans(
			String folderName, String[] exceptFolders, 
			MessageSortInfoBean sortInfo,
			String relationFolderName,
			String relationUid) throws Exception {
		
		 return commandHandler.getXSortRelationMessageBeans(sortInfo, exceptFolders, 
				 folderName, relationFolderName, relationUid);
	}
	public JSONArray getJsonTagList(String searchPattern) throws Exception {
		return jsonHandler.getJsonTagList(commandHandler.getTagList(searchPattern));
	}
	
	public TMailTag[] getTagList(String searchPattern) throws Exception {
		return commandHandler.getTagList(searchPattern);
	}
	
	public void addTag(String tagName, String tagColor) throws Exception {
		TMailTag tag = new TMailTag();
		tag.setName(tagName);
		tag.setColor(tagColor);
		
		commandHandler.addMailTag(tag);
	}
	
	public void deleteTag(String[] tagNames) throws Exception {
		commandHandler.deleteMailTag(tagNames);
	}
	
	public void modifyTag(String oldTagId, String newName, String newColor) 
	throws Exception {
		TMailTag modTag = new TMailTag();
		modTag.setName(newName);
		modTag.setColor(newColor);
		commandHandler.modMailTag(oldTagId, modTag);
	}
	
	public void storeTagging(boolean isAdd, String folderName, String tagId, long[] uids)
	throws Exception {
		commandHandler.storeMailTag(isAdd,folderName, tagId, uids);
	}
	
	public SortMessage[] getTagSortMessages(MessageSortInfoBean sortInfo, 
			int tagId) throws Exception {		
		 return commandHandler.getSortTagMessage(sortInfo, tagId);
	}
	
	public MailSortMessageBean[] getTagSortMessageBeans(MessageSortInfoBean sortInfo, 
			int tagId) throws Exception {		
		 return commandHandler.getTagSortMessageBeans(sortInfo, tagId);
	}
    public int getTagSortMessageTotal(MessageSortInfoBean sortInfo, int tagId) throws Exception {
    	return commandHandler.getTagSortMessageTotal(sortInfo, tagId);
    }
	
	
	public JSONArray getJsonSearchFolders(String searchPattern) throws Exception {
		return jsonHandler.getJsonSearchFolderList(commandHandler.getSearchQueryList(searchPattern));
	}
	
	public TMailSearchQuery[] getSearchFolders(String searchPattern) throws Exception {
		return commandHandler.getSearchQueryList(searchPattern);
	}
	
	
	public void addSearchFolder(String searchName, String query) throws Exception {
		
		TMailSearchQuery newQuery = new TMailSearchQuery();
		newQuery.setName(searchName);
		newQuery.setQuery(query);
		
		commandHandler.addSearchQuery(newQuery);
	}
	
	public void deleteSearchFolder(String[] searchNames) throws Exception {
		commandHandler.deleteSearchQuery(searchNames);
	}
	
	public void modifySearchFolder(String oldName, String newName, String newQuery) throws Exception {
		TMailSearchQuery modQuery = new TMailSearchQuery();
		modQuery.setName(newName);
		modQuery.setQuery(newQuery);
		
		commandHandler.modSearchQuery(oldName, modQuery);
	}
	
	
	public MailWriteMessageBean getWriteSettingBean(MessageWriteInfoBean writeInfo, User user)
	throws Exception {
		WriteHandler writeHandler = null;
		
		String wtype = writeInfo.getWriteType();
					
		if("webfolder".equalsIgnoreCase(wtype)){
			writeHandler = new WebfolderWriteHandler();
		} else if("reply".equalsIgnoreCase(wtype) || "replyall".equalsIgnoreCase(wtype)){
			writeHandler = new ReplyWriteHandler();
		} else if("forward".equalsIgnoreCase(wtype)){
			writeHandler = new ForwardWriteHandler();
		} else if("drafts".equalsIgnoreCase(wtype)){
			writeHandler = new DraftWriteHandler();
		} else if("bbs".equalsIgnoreCase(wtype)){
			writeHandler = new BbsWriteHandler();
		} else if("rewrite".equalsIgnoreCase(wtype)){
			writeHandler = new ReWriteHandler();
		} else {
			writeHandler = new NormalWriteHandler();
		}
		
		return writeHandler.getWriteMessageBean(writeInfo, store, user, msgResource);
	}
	
	public MDNResponsesBean getMDNResponsesContent (String folderName, 
			String uid, Map<String, Long> localDomainMap,
			String page, String pageBase, String pattern) 
	throws MessagingException{
		messageHandler.setResource(store.getFolder(folderName), msgResource);		
		return messageHandler.getMDNResponses(uid,localDomainMap, page, pageBase, pattern);
	}
	
	public MailAddressBean[] getUserMailAddressList(int userSeq, int domainSeq, 
			String locale, int maxCount, String keyWord, 
			boolean isAutoComplte, boolean isNotOrgSearch){
		int currentCount = 1;
		List<MailAddressBean>orgEmailList = null;
		List<MailAddressBean>sAddrEmailList = null;
		List<MailAddressBean>pAddrEmailList = null;
		List<MailAddressBean>recentList = null;
		List<MailAddressBean>domainList = null;
		List<MailAddressBean>deptList = null;
		
		String searchRcptOption = emailDAO.readSearchRcptOption(domainSeq);
		
		if(searchRcptOption.indexOf("O") > -1){
			orgEmailList = emailDAO.readOrgEmailList(domainSeq, userSeq, locale, keyWord,isAutoComplte);
			currentCount = (isAutoComplte)?maxCount - orgEmailList.size():1;
		}
		
		if(searchRcptOption.indexOf("P") > -1 && currentCount > 0){
			sAddrEmailList = emailDAO.readSharedEmailList(domainSeq, userSeq,keyWord,isAutoComplte);
			currentCount = (isAutoComplte)?maxCount - sAddrEmailList.size():1;
		}
		
		if(searchRcptOption.indexOf("I") > -1 && currentCount > 0){
			pAddrEmailList = emailDAO.readPrivateEmailList(domainSeq, userSeq, keyWord,isAutoComplte);
			currentCount = (isAutoComplte)?maxCount - pAddrEmailList.size():1;
		}
		
		if(searchRcptOption.indexOf("R") > -1 && currentCount > 0){
			recentList = emailDAO.readRecentEmailList(domainSeq, userSeq, keyWord,isAutoComplte);
			currentCount = (isAutoComplte)?currentCount:1;
		}
		
		if(searchRcptOption.indexOf("U") > -1 && currentCount > 0){
			domainList = emailDAO.readDomainEmailList(domainSeq, keyWord,isAutoComplte);
			currentCount = (isAutoComplte)?currentCount:1;
		}
		
		if((searchRcptOption.indexOf("O") > -1 || !isNotOrgSearch) && currentCount > 0){
			deptList = emailDAO.readDeptList(domainSeq, keyWord,isAutoComplte);
			currentCount = (isAutoComplte)?currentCount:1;
		}	
			
		
			
		List<MailAddressBean>allList = getMaillList(pAddrEmailList, sAddrEmailList, orgEmailList, recentList, domainList, deptList);
		
		MailAddressBean[] addrs = new MailAddressBean[allList.size()];		
		allList.toArray(addrs);
		return addrs;
	}
	
	private List<MailAddressBean> getMaillList(
				List<MailAddressBean>pAddrEmailList, 
				List<MailAddressBean>sAddrEmailList,
				List<MailAddressBean>orgEmailList,
				List<MailAddressBean>recentList,
				List<MailAddressBean>domainList,
				List<MailAddressBean>deptList
				){
		List<MailAddressBean>allList = new ArrayList<MailAddressBean>();
		
		Map<String, MailAddressBean> map= new HashMap<String, MailAddressBean>();
		
		if(orgEmailList != null){
			for (MailAddressBean email : orgEmailList) {
				MailAddressBean result = getEmail(map, email);
				if(result != null){
					allList.add(result);	
				}
			}	
		}
		
		if(pAddrEmailList != null){
			for (MailAddressBean email : pAddrEmailList) {
				MailAddressBean result = getEmail(map, email);
				if(result != null){
					allList.add(result);	
				}
			}	
		}
		
		if(sAddrEmailList != null){
			for (MailAddressBean email : sAddrEmailList) {
				MailAddressBean result = getEmail(map, email);
				if(result != null){
					allList.add(result);	
				}
			}	
		}
		
		if(recentList != null){
			for (MailAddressBean email : recentList) {
				MailAddressBean result = getEmail(map, email);
				if(result != null){
					allList.add(result);	
				}
			}	
		}
		
		if(domainList != null){
			for (MailAddressBean email : domainList) {
				MailAddressBean result = getEmail(map, email);
				if(result != null){
					allList.add(result);	
				}
			}	
		}
		
		if(deptList != null){
			for (MailAddressBean email : deptList) {
				MailAddressBean result = getEmail(map, email);
				if(result != null){
					allList.add(result);	
				}
			}	
		}
		
		return allList;
	}
	
	private MailAddressBean getEmail(Map<String, MailAddressBean> map, MailAddressBean email){
		if(map.get(email.getEmail()) != null)
			return null;
		else{
			map.put(email.getEmail(), email);
		}
		
		return email;
	}
	
	public String getMessageIntegrity(String folderName, String uid) throws MessagingException {	
		messageHandler.setResource(store.getFolder(folderName), msgResource);
		return messageHandler.getMessageIntegrity(uid);
	}
	
	private SharedFolderVO getCheckSharedFolder(int userSeq, String folderName){
		Map<String, SharedFolderVO> map = sharedFolderHandler.getUserSharedFolderMap(userSeq);
		SharedFolderVO vo = null;
		folderName = TMailUtility.IMAPFolderEncode(folderName);
		if(map != null && map.containsKey(folderName)){
			vo = map.get(folderName);
		}
		return vo;
	}
	
	public JSONArray getJsonSharringFolders(int userSeq){
		return jsonHandler.getJsonSharedFolderList(getSharingFolders(userSeq));
	}
	
	public List<SharedFolderVO> getSharingFolders(int userSeq){
		return sharedFolderHandler.getSharringFolders(userSeq);
	}
	
	public JSONArray getJsonSharedFolderReaders(int folderUid){
		return jsonHandler.getJsonSharedFolderReaders(getSharedFolderReaderList(folderUid));
	}
	
	public List<SharedFolderUserVO> getSharedFolderReaderList(int folderUid){
		return sharedFolderHandler.getSharedFolderReaderList(folderUid);
	}
	
	public void setSharedFolder(int folderUid, String folderName, int userSeq, int[] sharedUserSeq){
		SharedFolderVO sharedFolderVO = new SharedFolderVO();
		sharedFolderVO.setFolderUid(folderUid);
		sharedFolderVO.setFolderName(folderName);
		sharedFolderVO.setSharedUserSeq(userSeq);
		
		SharedFolderUserVO[] sharringUsers = null;
		if(sharedUserSeq != null){
			sharringUsers = new SharedFolderUserVO[sharedUserSeq.length];
			for (int i = 0 ; i < sharedUserSeq.length ; i++) {
				sharringUsers[i] = new SharedFolderUserVO();
				sharringUsers[i].setUserSeq(sharedUserSeq[i]);
				sharringUsers[i].setFolderUid(folderUid);
			}
		}
		
		sharedFolderHandler.saveSharedFolderInfo(sharedFolderVO, sharringUsers);
	}
	
	public void deleteSharedFolder(int folderUid){
		sharedFolderHandler.deleteSharedFolderInfo(folderUid);
	}
}
