/**
 * WebfolderWriteHandler.java 2009. 2. 20.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.write;

import java.util.Map;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mail.ibean.MailWriteMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageWriteInfoBean;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.util.Validation;
import com.terracetech.tims.webmail.webfolder.manager.WebFolderUtils;

/**
 * <p><strong>WebfolderWriteHandler.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class WebfolderWriteHandler extends WriteHandler {

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
		String wfType = writeInfo.getWebfolderType();	

    	if(store.isConnected()){
    		store.close();
    	}
    	
    	boolean isRoot = false;
    	String folderUserSeq = writeInfo.getWebfolderShareSeq();
    	Map<String, String> confMap = 
    		WebFolderUtils.getWebfolderConf(wfType, folderUserSeq, user.get(User.MAIL_DOMAIN), user);		        	

    	store.connect(confMap.get(User.MAIL_HOST), confMap.get(User.EMAIL), confMap.get(User.IMAP_LOGIN_ARGS));		        	
        
    	if (StringUtils.isEmpty(folderName) || "/".equals(folderName)) {
			isRoot = true;
		}
    	
    	TMailFolder wfFolder = null;
    	if (isRoot) {
    		wfFolder = store.getDefaultWebFolder();
		}
		else {
			char separator = '.';
	        String xpath = folderName.substring(1);
	        xpath = xpath.replaceAll("/", "" + separator);
	        wfFolder = store.getWebFolder(xpath);
		}
       
        wfFolder.open(false);
        long[] wuids = writeInfo.getWuidsValue();
        TMailMessage[] messages = wfFolder.getMessagesByUID(wuids);
        
        String[][] attaches = getWebFolderAttachFiles(messages, 
        		tmpPath, user.get(User.EMAIL));
        
        if(messages != null){
        	String folderType = "";        
            if ("share".equals(wfType)) {
            	folderType = "S";
        	} else if ("public".equals(wfType)) {
        		folderType = "P";
        	}
            
            String logStr = "";            
            for (int i = 0; i < messages.length; i++) {
            	logStr += folderType+wfFolder.getFullName() +"|"+ Long.parseLong(messages[i].getWebFolderFileSize())+"|"+messages[i].getSubject()+"\n"; 
    		}
            writeBean.setLogStr(logStr);
        }

        wfFolder.close(true);
        
        writeBean.setAttaches(attaches);
		writeBean.setAttacheString(parseAttachListStr(attaches,wuids));
		writeBean.setWriteType("normal");
		
		return writeBean;
	}
	
	public String parseAttachListStr(String[][] attaches, long[] wuids){
		String attacheString = "";
		if(attaches != null) {
			int cnt = 0;
			for(int i = 0; i < attaches.length; i++) {
				if (!Validation.isNull(attaches[i][0])) {
					attacheString += "AttachList["+cnt+"] = new Array(\""
						+attaches[i][0]+"\", \""+attaches[i][1]+"\", "+attaches[i][2]+");\n";
						cnt++;
				}
			}
		}		
		return attacheString;
	}

}
