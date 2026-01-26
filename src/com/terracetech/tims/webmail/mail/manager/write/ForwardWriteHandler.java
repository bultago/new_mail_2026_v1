/**
 * ForwardWriteHandler.java 2009. 2. 20.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.write;


import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailWriteMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageWriteInfoBean;
import com.terracetech.tims.webmail.mailuser.User;

/**
 * <p><strong>ForwardWriteHandler.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class ForwardWriteHandler extends WriteHandler {

	/**
	 * <p></p>
	 *
	 * @see com.terracetech.tims.webmail.mail.manager.write.WriteHandler#getWriteMessageBean(com.terracetech.tims.webmail.mail.ibean.MessageWriteInfoBean, com.terracetech.tims.mail.TMailStore, com.terracetech.tims.webmail.mailuser.User, com.terracetech.tims.common.I18nResources)
	 * @param writeInfo
	 * @param store
	 * @param user
	 * @param msgResource
	 * @return
	 * @throws Exception 
	 */
	@Override
	public MailWriteMessageBean getWriteMessageBean(
			MessageWriteInfoBean writeInfo, TMailStore store, User user,
			I18nResources msgResource) throws Exception {
		
		MailWriteMessageBean writeBean = new MailWriteMessageBean();
		String folderName = writeInfo.getFolderName();
		String wtype = writeInfo.getWriteType();		
		boolean isForwardingParsed = writeInfo.isForwardParsed();		
		long[] uids = writeInfo.getUidsValue();
		String subject = null;
		String mode = null;
		String messageId = null;
		if(isForwardingParsed){			
	    	TMailFolder folder = store.getFolder(folderName);
	    	
	    	folder.open(false);
	    	TMailMessage message = null;
	    	
	    	try {			
				message = folder.getMessageByUID(uids[0], true);
				message.setDirectRead(true);
				message.setLocale(msgResource.getLocale());
				messageId = message.getMessageID();
			    if (messageId.startsWith("<") )
			    	messageId = messageId.substring(1, messageId.length() -1 );
			} catch (Exception e) { 
				LogManager.writeErr(this, e.getMessage(), e);				
			}
			subject = message.getSubject();
			String[][] attaches = getMessageAttachFiles(message, tmpPath, user.get(User.EMAIL));
			
			
			writeBean.setHtmlContent(getContentStringTextHtml(writeInfo.isMobileMode(),
					writeInfo.isSignInside(),message, wtype, 
					tmpPath,
					EnvConstants.getAttachSetting("upimage.url")));
			writeBean.setTextContent(getContentStringTextPlain(writeInfo.isMobileMode(),message, wtype));
			writeBean.setAttacheString(parseAttachListStr(attaches));
			writeBean.setAttaches(attaches);
			
			folder.close(false);
			
			subject = "Fwd: " + ((subject != null) ? 
					subject : msgResource.getMessage("header.nosubject"));
			
			mode = "forward";
			
		} else {
			 if(uids != null && uids.length == 1){
		    	TMailFolder folder = store.getFolder(folderName);
		    	
		    	folder.open(false);
		    	TMailMessage message = null;
		    	
		    	try {			
					message = folder.getMessageByUID(uids[0], true);
					messageId = message.getMessageID();
				    if (messageId.startsWith("<") )
				    	messageId = messageId.substring(1, messageId.length() -1 );
				} catch (Exception e) { 
					LogManager.writeErr(this, e.getMessage(), e);					
				}
				subject = message.getSubject();
				subject = "Fwd: " + ((subject != null) ? 
						subject : msgResource.getMessage("header.nosubject"));
				
				folder.close(false);
			} else {
				subject = "Fwd: ";
			}
			 
			 mode = "forwardAttached";
		}  
		
		if(writeInfo.isLocalMailWrite()){			
		    writeBean.setTrashMsgId(messageId);
		}		
		writeBean.setSubject(subject);
		writeBean.setUids(writeInfo.getUids());
		writeBean.setForwardingMode(writeInfo.getForwardingMode());
		writeBean.setWriteType("normal");
		writeBean.setSendFlag(mode);
		writeBean.setFolderName(folderName);

		return writeBean;
	}

}
