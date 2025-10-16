/**
 * DraftWriteHandler.java 2009. 2. 20.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.write;

import javax.mail.Flags;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

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
 * <p><strong>DraftWriteHandler.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class DraftWriteHandler extends WriteHandler {

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
			long[] uids = writeInfo.getUidsValue();
			
			TMailFolder folder = store.getFolder(folderName);
		   	
			folder.open(true);
			TMailMessage message = null;
			
			try {			
				message = folder.getMessageByUID(uids[0], true);
			} catch (Exception e) { 
				LogManager.writeErr(this, e.getMessage(), e);	
			}
			message.setDirectRead(true);
			   	
			//get MessageId
		    String messageId = message.getMessageID();
		    if (messageId.startsWith("<") )
		    	messageId = messageId.substring(1, messageId.length() -1 );
		    		        
			// set Locale
			message.setLocale(msgResource.getLocale());				
			
			String subject = message.getSubject();		
			InternetAddress[] addrTo = message.getTo() ;
			InternetAddress[] addrCc = message.getCc();					
			String to = (addrTo != null)?TMailAddress.getAddressString(addrTo):null;
			String cc = (addrCc != null)?TMailAddress.getAddressString(addrCc):null;					
			String bcc = null;
			String[] headers = message.getHeader("X-Bcc");
			if (headers != null) {
				bcc = headers[0];
				if (bcc != null) {
					bcc = MimeUtility.decodeText(bcc);							
					bcc = TMailAddress.getAddressString(InternetAddress.parse(bcc));
				}
			}
			String[] uid = writeInfo.getUids();			
			String uidStr = null;
			String[] fuidHeaders = message.getHeader("X-FUID");
			if (fuidHeaders != null) {
				uidStr = fuidHeaders[0];
				if (uidStr != null) {
					uid = uidStr.split(",");
				}				
			}
			
			String[] sendFlagHeaders = message.getHeader("X-SENDFLAG");
			String sendFlagStr = null;
			if(sendFlagHeaders != null){
				sendFlagStr = sendFlagHeaders[0];
				if ("REPLY".equals(sendFlagStr)) {
					writeBean.setSendFlag("reply");
				} else if("FORWARDATTACHED".equals(sendFlagStr)){
					writeBean.setSendFlag("draftForwardAttached");
				} else if("FORWARD".equals(sendFlagStr)){
					writeBean.setSendFlag("forward");
				}
			}
			
			String forwardFolderName = writeInfo.getFolderName();			
			String folderNameStr = null;
			String[] ffnameHeaders = message.getHeader("X-FFNAME");
			if (ffnameHeaders != null) {
				folderNameStr = ffnameHeaders[0];
				if (folderNameStr != null) {
					forwardFolderName = MimeUtility.decodeText(folderNameStr);
				}
			}
			
			
			
			String[][] attaches = getMessageAttachFiles(message, tmpPath, user.get(User.EMAIL));
			
			writeBean.setTo(to);
			writeBean.setCc(cc);
			writeBean.setBcc(bcc);
			writeBean.setSubject(subject);		
			writeBean.setHtmlContent(getContentStringTextHtml(writeInfo.isMobileMode(),
					writeInfo.isSignInside(),message, wtype, 
					EnvConstants.getBasicSetting("tmpdir"),EnvConstants.getAttachSetting("upimage.url")));
			writeBean.setTextContent(getContentStringTextPlain(writeInfo.isMobileMode(),message, wtype));
			writeBean.setAttacheString(parseAttachListStr(attaches));
			writeBean.setAttaches(attaches);		
			writeBean.setDraftMsgId(messageId);
			writeBean.setUids(uid);
			writeBean.setFolderName(forwardFolderName);
			
			Flags flag = new Flags(javax.mail.Flags.Flag.SEEN);
			message.setFlags(flag, true);			
	    	folder.close(true);   	
	    	
	    	writeBean.setWriteType("normal");	    	
			
			return writeBean;		
	
		}
}
