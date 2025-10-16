package com.terracetech.tims.hybrid.mail.action;

import java.io.File;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class MailAttachServiceAction extends BaseAction {
	
	public String execute() throws Exception {
		String result = "";
		String email = user.get(User.EMAIL);
		MultiPartRequestWrapper multiWrapper = null;
		if (request instanceof MultiPartRequestWrapper) {
			multiWrapper = (MultiPartRequestWrapper) request;
		}
		 
		if (multiWrapper.getFiles("theFile") == null) {
			
		} else {
			File attFile = multiWrapper.getFiles("theFile")[0];
            String attFileName = multiWrapper.getFileNames("theFile")[0];
            long attFileSize = attFile.length();
            
            try {
	            String tempFileName = "attach_" + Long.toString(System.nanoTime()) + "_" + email + ".u";
	            FileUtil.saveTmpFile(attFile, tempFileName);
	            result = tempFileName+"\t"+attFileName+"\t"+attFileSize;
            } catch (Exception e) {
            	LogManager.writeErr(this, e.getMessage(), e);
			}
		}

        ResponseUtil.makeAjaxMessage(response, result);
        
		return null;
	}
}
