/**
 * BbsWriteHandler.java 2009. 4. 9.
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
 * <p><strong>BbsWriteHandler.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class BbsWriteHandler extends WriteHandler {

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
		TMailFolder folder = store.getFolder(writeInfo.getBbsId());
    	
    	folder.open(false);
    	TMailMessage message = null;    	
    	try {
    		long uid = -1;    		
    		if(writeInfo.getBuids() != null){
    			uid = Long.parseLong(writeInfo.getBuids());
    		} else {
    			String mid = writeInfo.getBmids();
    			uid = folder.xsearchMID(mid);
    		}    					
			message = folder.getMessageByUID(uid, true);
		} catch (Exception e) { 
			LogManager.writeErr(this, e.getMessage(), e);
		}
		
		String subject = message.getSubject();
		String writer = message.getFromPersonal();
		String date = message.getSentDateForRead();
		String htmlContents = getContentStringTextHtml(writeInfo.isMobileMode(),false,message, "drafts", 
				writeInfo.getImgFilePath(),EnvConstants.getAttachSetting("attach.url"));
		String textContents = getContentStringTextPlain(writeInfo.isMobileMode(),message, "drafts");
		
		
		String[][] attaches = getMessageAttachFiles(message, tmpPath, user.get(User.EMAIL));		
		writeBean.setHtmlContent(getBbsContents(true, subject, writer, date, htmlContents, msgResource));
		writeBean.setTextContent(getBbsContents(false, subject, writer, date, textContents, msgResource));
		writeBean.setAttacheString(parseAttachListStr(attaches));		
		
		folder.close(false);		
		
		writeBean.setWriteType("normal");
		return writeBean;
	}
	
	public String getBbsContents(boolean isHtml, String subject, String writer,
			String date, String contents, I18nResources msgResource) {
		
		StringBuffer sb = new StringBuffer();
		
		if(isHtml){			
			sb.append("<table width=100% cellpadding=0 cellspacing=1 border=0 bgcolor='#68A9EB'>");
			sb.append("<col width='100px'></col><col width='50%'></col><col width='100px'></col><col width='50%'></col>");
			sb.append("<tr bgcolor='#ffffff'>");
			sb.append("<td align='center' bgcolor='#3972BC' style='padding:5px;font-size:12px;color:#ffffff;white-space:nowrap' nowrap>");
			sb.append(msgResource.getMessage("bbs.content.list.creator"));
			sb.append("</td><td style='padding:5px;font-size:12px;'>");
			sb.append(writer);
			sb.append("</td><td align='center' bgcolor='#3972BC' style='padding:5px;font-size:12px;color:#ffffff;white-space:nowrap' nowrap>");
			sb.append(msgResource.getMessage("bbs.content.list.createDate"));
			sb.append("</td><td style='padding:5px;font-size:12px;white-space:nowrap' nowrap>");
			sb.append(date);
			sb.append("</td></tr><tr bgcolor='#ffffff'>");
			sb.append("<td align='center' bgcolor='#3972BC' style='padding:5px;font-size:12px;color:#ffffff;white-space:nowrap' nowrap>");
			sb.append(msgResource.getMessage("bbs.content.list.subject"));
			sb.append("</td><td colspan=3 style='padding:5px;font-size:12px;'>");			
			sb.append(subject);
			sb.append("</td></tr></table>");			
			sb.append("<table width=100% cellpadding=0 cellspacing=1 border=0 bgcolor='#68A9EB' style='margin-top:1px;'>");
			sb.append("<tr bgcolor='#ffffff'>");
			sb.append("<td bgcolor='#ffffff' style='padding:5px;font-size:12px;'>");
			sb.append(contents);
			sb.append("</td></tr></table>");			
		} else {
			sb.append("- ");
			sb.append(msgResource.getMessage("bbs.content.list.creator"));
			sb.append(":");
			sb.append(writer);
			sb.append("\n");
			sb.append("- ");
			sb.append(msgResource.getMessage("bbs.content.list.createDate"));
			sb.append(":");
			sb.append(date);
			sb.append("\n");
			sb.append("- ");
			sb.append(msgResource.getMessage("bbs.content.list.subject"));
			sb.append(":");
			sb.append(subject);
			sb.append("\n");
			sb.append("----------------------------");	
			sb.append("\n");
			sb.append(contents);
			sb.append("\n");			
		}
		
		
		return sb.toString();
	}

}
