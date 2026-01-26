/**
 * BigattachManager.java 2009. 3. 31.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.webmail.mail.dao.BigAttachDao;
import com.terracetech.tims.webmail.mail.vo.MailBigAttachVO;
import com.terracetech.tims.webmail.mailuser.User;

import com.terracetech.tims.webmail.webfolder.manager.WebFolderUtils;

/**
 * <p><strong>BigattachManager.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class BigattachManager {
	
	private BigAttachDao bigAttachDao = null;
	
	public void setBigAttachDao(BigAttachDao bigAttachDao){
		this.bigAttachDao = bigAttachDao;
	}
	
	public Map<String, String> getBigAttachConnectInfo(User user) {
		Map<String, String> confMap = new HashMap<String, String>();		
		long quota = 10737418240L;
	
		confMap.put(User.MAIL_UID, user.get(User.MAIL_UID));
		confMap.put(User.MAIL_DOMAIN, user.get(User.MAIL_DOMAIN));
		confMap.put(User.EMAIL, user.get(User.EMAIL));
		confMap.put(User.MAIL_HOST, user.get(User.MAIL_HOST));
		confMap.put(User.IMAP_LOGIN_ARGS, user.get(User.MESSAGE_STORE) + 
				FolderHandler.BIGATTACH + 
				"  "+ (quota/(1024*1024)) +" "+(quota%(1024*1024)) +" 100000 0 90 50 " + 
				user.get(User.MAIL_USER_SEQ) + " " + 
				user.get(User.MAIL_DOMAIN_SEQ));
		
		return confMap;
	}
	
	public void uploadBigattachFile(MailBigAttachVO info,
												Map<String, String> paramMap,
												TMailFolder folder,
												MimeMessage message,
												File[] files)
	throws Exception{
		
		folder.open(true);				
	    
	    WebFolderUtils folderUtils = new WebFolderUtils();
	    String fileName = info.getFileName();
	    
	    folderUtils.setFilename(info.getFileName());
	    folderUtils.setFilesize(info.getFileSize());
	    folderUtils.setFileext(fileName.substring(fileName.lastIndexOf(".") + 1));
	    message = folderUtils.makeHeaderAll(message, paramMap.get("email"));
	    String messageId = message.getMessageID();
	    messageId = messageId.replaceAll("<","");
	    messageId = messageId.replaceAll(">","");
	    
	    folder.appendDirectMessagesBinary(new TMailMessage[]{new TMailMessage(message)}, files);
	    
		folder.close(true);		
						
		folder.open(false);			
		int messageUid = folder.xsearchMID(messageId);				
		folder.close(false);	
		
		
		Date tm2 = new Date(Long.parseLong(paramMap.get("regdate")));
		Date tm3 = null;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(tm2);
		calendar.add(Calendar.DAY_OF_MONTH,(Integer.parseInt(paramMap.get("expiredays"))-1));
		tm3 = calendar.getTime();		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		info.setRegistTime(sdf.format(tm2));
		info.setExpireTime(sdf.format(tm3));
		info.setMessageUid(Integer.toString(messageUid));		
		
		bigAttachDao.saveMailBigAttach(info);		
	}
	
	public MailBigAttachVO[] getBigAttachList(int userSeq) throws Exception{
		List<MailBigAttachVO> attList = bigAttachDao.getListMailBigAttach(userSeq);
		MailBigAttachVO[] attFileList = new MailBigAttachVO[attList.size()];
		attList.toArray(attFileList);
		return attFileList;
	}
	
	@Transactional
	public void deleteBigattachInfo(int userSeq, String messageUid) throws Exception{
		bigAttachDao.updateDeleteMailBigAttach(userSeq, messageUid);
	}
	
	@Transactional
	public void updateBigattachInfo(MailBigAttachVO vo) throws Exception{
		bigAttachDao.updateMailBigAttach(vo);
	}
	
	public MailBigAttachVO getBigAttachInfo(int userSeq, String messageUid) throws Exception {
		return bigAttachDao.getMailBigAttach(userSeq, messageUid);		
	}
	
	public long getBigAttachQuotaUsage(int userSeq) throws Exception {
		long useage = 0L;
		List<MailBigAttachVO> voList = bigAttachDao.getListMailBigAttach(userSeq);
		Iterator<MailBigAttachVO> it = voList.iterator();
		MailBigAttachVO tempVo = null;
		while (it.hasNext()) {
			tempVo = it.next();			
			useage += Long.parseLong(tempVo.getFileSize());			
		}		
		return useage;
	}
}
