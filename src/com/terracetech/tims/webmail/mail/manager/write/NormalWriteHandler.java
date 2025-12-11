/**
 * NormalWriteHandler.java 2009. 2. 20.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.write;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.mail.ibean.MailWriteMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageWriteInfoBean;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;
/**
 * <p><strong>NormalWriteHandler.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class NormalWriteHandler extends WriteHandler {

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
		
		writeBean.setTo(writeInfo.getReqTo());
		writeBean.setCc(writeInfo.getReqCc());
		writeBean.setBcc(writeInfo.getReqBcc());
		writeBean.setSubject(writeInfo.getReqSubject());
		writeBean.setHtmlContent(writeInfo.getReqContent());			
		
		// GET URL ATTACH FILE
		String[][] attaches = null;
		if (writeInfo.getAttatachFilenames() == null) {
			attaches = getUrlsAttachFiles(writeInfo.getAttatachUrls(), tmpPath, user.get(User.EMAIL));
		} else {
			attaches = getUrlsAttachFiles(writeInfo.getAttatachUrls(), writeInfo.getAttatachFilenames(), tmpPath, user.get(User.EMAIL));
		}
		String[] attachList = writeInfo.getAttachLists();		

		if(attaches != null) {			
			writeBean.setAttacheString(parseAttachListStr(attaches));
			writeBean.setAttaches(attaches);
		} else if(attachList != null) {
			writeBean.setAttacheString(parseAttachStrList(attachList));			
		}
		
		writeBean.setWriteType("normal");
		
		return writeBean;
	}

	public String parseAttachStrList(String[] attachList) {
		String attacheString = "";
		String[] attinfo = null;
		for(int i = 0; i < attachList.length; i++) {
			attachList[i] = StringUtils.uni2latin(attachList[i]);
			attinfo = attachList[i].split(":");

			if(attinfo != null && attinfo.length >= 3) {
				attacheString += "AttachList["+i+"] = new Array(\""
					+attinfo[0]+"\", \""		// FILE PATH
					+attinfo[1]+"\", "			// FILE NAME
					+attinfo[2]+");\n";			// FILE SIZE
				i++;
			}
		}
		return attacheString;
	}

}
