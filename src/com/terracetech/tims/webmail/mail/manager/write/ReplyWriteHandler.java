/**
 * ReplyWriteHandler.java 2009. 2. 20.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.write;

import java.util.ArrayList;
import java.util.List;

import jakarta.mail.internet.InternetAddress;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailWriteMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageWriteInfoBean;
import com.terracetech.tims.webmail.mailuser.User;


/**
 * <p><strong>ReplyWriteHandler.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class ReplyWriteHandler extends WriteHandler {

	/**
	 * <p></p>
	 *
	 * @see com.terracetech.tims.webmail.mail.manager.write.WriteHandler#getWriteMessageBean(com.terracetech.tims.webmail.mail.ibean.MessageWriteInfoBean, com.terracetech.tims.mail.TMailStore, com.terracetech.tims.webmail.mailuser.User)
	 * @param writeInfo
	 * @param store
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	@Override
	public MailWriteMessageBean getWriteMessageBean(
			MessageWriteInfoBean writeInfo, TMailStore store, User user,
			I18nResources msgResource)
			throws Exception {
		
		MailWriteMessageBean writeBean = new MailWriteMessageBean();
		String folderName = writeInfo.getFolderName();
		String wtype = writeInfo.getWriteType();		
		boolean isReplyAll	 = wtype.equals("replyall");
		long[] uids = writeInfo.getUidsValue();		
		
    	TMailFolder folder = store.getFolder(folderName);
       	
    	folder.open(false);
    	TMailMessage message = null; 	
    	
    	try {			
			message = folder.getMessageByUID(uids[0], true);
			message.setLocale(msgResource.getLocale());
			message.setDirectRead(true);
		} catch (Exception e) { 
			LogManager.writeErr(this, e.getMessage(), e);
		}				
		
		String to = null;
		String cc = null;
		String bcc = null;		
		InternetAddress[] addrReplyto,addrFrom,addrTo,addrCc;					
		
		addrReplyto = message.getReplyTo();
		to = (addrReplyto != null)?TMailAddress.getAddressString(addrReplyto)
				:TMailAddress.getAddressString(message.getFrom());								
		
		writeBean.setWriteType("normal");
		writeBean.setSendFlag("reply");
		if(isReplyAll) {
			addrFrom = message.getFrom();
			addrTo = message.getTo();
			addrCc = message.getCc();
			
			addrTo = checkMyAddress(addrTo, user.get(User.EMAIL));
			addrCc = checkMyAddress(addrCc, user.get(User.EMAIL));
			
			to = (to != null)?to:"";
			cc = (cc != null)?cc+",":"";			
			
			if (addrReplyto == null && addrFrom != null) {
				String tempStr = TMailAddress.getAddressString(addrFrom).trim();
				if(tempStr != null && tempStr.length() != 0){
					to = to + ((to.length() > 0)?",":"") + tempStr;
				}										
			}			
			
			if (addrTo != null) {					
				String tempStr = TMailAddress.getAddressString(addrTo).trim();
				if(tempStr != null && tempStr.length() != 0){
					to = to +((to.length() > 0)?",":"") + tempStr;
				}				
			}		
			
			if (addrCc != null) {
				String tempStr = TMailAddress.getAddressString(addrCc).trim();
				if(tempStr != null && tempStr.length() != 0){
					cc = cc + ((cc.length() > 0)?",":"") + tempStr;
				}
			}
			writeBean.setSendFlag("replyAll");
		}

		String subject = message.getSubject();	
		subject = "Re: " + ((subject != null) ? 
				subject : msgResource.getMessage("header.nosubject"));
		
		if(writeInfo.isLocalMailWrite()){
			String messageId = message.getMessageID();
		    if (messageId.startsWith("<") )
		    	messageId = messageId.substring(1, messageId.length() -1 );
		    writeBean.setTrashMsgId(messageId);
		}		
    	
    	writeBean.setTo(to);
		writeBean.setCc(cc);
		writeBean.setBcc(bcc);
		writeBean.setSubject(subject);		
		writeBean.setHtmlContent(getContentStringTextHtml(writeInfo.isMobileMode(),
				writeInfo.isSignInside(),message, wtype,	tmpPath,
				EnvConstants.getAttachSetting("upimage.url")));
		writeBean.setTextContent(getContentStringTextPlain(writeInfo.isMobileMode(),message, wtype));
		writeBean.setUids(writeInfo.getUids());
		writeBean.setFolderName(folderName);
		
		folder.close(false);
		
		return writeBean;
	}
	
	
	private InternetAddress[] checkMyAddress(InternetAddress[] checkAddrs, String email){
		List<InternetAddress> tempAddrList = null;
		InternetAddress[] returnAddrs = null;
		if(checkAddrs != null){
			tempAddrList = new ArrayList<InternetAddress>();
			for (InternetAddress iAddress : checkAddrs) {
				if(!email.equals(iAddress.getAddress())){
					tempAddrList.add(iAddress);					
				}
			}
			
			returnAddrs = new InternetAddress[tempAddrList.size()];
			tempAddrList.toArray(returnAddrs);
		}
		
		tempAddrList = null;
		return returnAddrs;
				
	}

}
