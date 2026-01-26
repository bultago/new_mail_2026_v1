package com.terracetech.tims.webmail.mail.manager.write;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeUtility;

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

public class ReWriteHandler extends WriteHandler {

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
		   	
		// set Locale
		message.setLocale(msgResource.getLocale());				
		
		String subject = message.getSubject();		
		InternetAddress[] addrTo = message.getTo() ;
		InternetAddress[] addrCc = message.getCc();					
		String to = (addrTo != null)?TMailAddress.getAddressString(addrTo):null;
		String cc = (addrCc != null)?TMailAddress.getAddressString(addrCc):null;					
		String bcc = null;
		String[] headers = message.getHeader("Bcc");
		if (headers != null) {
			bcc = headers[0];
			if (bcc != null) {
				bcc = MimeUtility.decodeText(bcc);							
				bcc = TMailAddress.getAddressString(InternetAddress.parse(bcc));
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
		
		folder.close(true);   	
    	
    	writeBean.setWriteType("rewrite");
		
		return writeBean;		
	}

}
