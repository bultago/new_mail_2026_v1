package com.terracetech.tims.webmail.setting.action;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.Validation;

public class UploadPictureAction extends BaseAction {
	
	private MultiPartRequestWrapper multiWrapper = null;

	public String execute() throws Exception {
		
		Map<String, String> reqParam = new HashMap<String, String>();
		
		if(request instanceof MultiPartRequestWrapper) {
			multiWrapper = (MultiPartRequestWrapper) request;		
		}
		
		String msg = "";
		
		try {	
			Iterator itr = 	multiWrapper.getParameterMap().keySet().iterator();
			String field,fieldValue;
			while(itr.hasNext()) {			
				field = (String)itr.next();
				fieldValue = multiWrapper.getParameter(field);
				reqParam.put(field, fieldValue);		
			}
			
			File attFile = multiWrapper.getFiles("theFile")[0];
			String attFileName = multiWrapper.getFileNames("theFile")[0];
			String attFilePath = "picture_"+Long.toString(System.nanoTime()) + "_" + user.get(User.MAIL_USER_SEQ)+".img";
			Long attFileSize = attFile.length();
			Long maxPictureSize = Long.parseLong(reqParam.get("maxPictureSize"));
			
			if (Validation.isImageFile(attFileName)) {
				
				if (attFileSize <= maxPictureSize) {
		
					attFilePath = FileUtil.saveTmpFile(attFile,attFilePath);
					
					int extIndex = attFileName.lastIndexOf(".");
					String ext = attFileName.substring(extIndex);
					
					String time = Long.toString(System.nanoTime());
					attFileName = user.get(User.MAIL_UID) + ext;
					String newAttFileName = time + "_" + user.get(User.MAIL_UID) + ext;
					
					String viewSrc = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir")+EnvConstants.DIR_SEPARATOR + newAttFileName;
					String strLocalhost = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
					String viewUrl = EnvConstants.getAttachSetting("attach.url")+ "/" + newAttFileName;
					
					viewUrl = strLocalhost + viewUrl;
					
					FileUtil.copy(attFile, viewSrc);
					
					request.setAttribute("pictureUrl", viewUrl);
					request.setAttribute("pictureSrc", attFilePath);
					request.setAttribute("pictureName", attFileName);
				} else {
					msg = "size";
				}
			} else {
				msg = "name";
			}
		}catch (Exception e) {
			msg = "error";
			LogManager.writeErr(this, e.getMessage(), e);
		}
		
		request.setAttribute("pictureMsg", msg);
		
		return "success";
	}
}
