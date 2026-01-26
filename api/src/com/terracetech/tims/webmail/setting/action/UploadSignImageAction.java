package com.terracetech.tims.webmail.setting.action;

import java.io.File;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SignManager;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.Validation;

public class UploadSignImageAction extends BaseAction {
	
	private MultiPartRequestWrapper multiWrapper = null;
	private SignManager signManager = null;

	public void setSignManager(SignManager signManager) {
		this.signManager = signManager;
	}

	public String execute() throws Exception {
		
		User user = UserAuthManager.getUser(request);
		
		if(request instanceof MultiPartRequestWrapper) {
			multiWrapper = (MultiPartRequestWrapper) request;		
		}
		
		File attFile = multiWrapper.getFiles("theFile")[0];
		String attFileName = multiWrapper.getFileNames("theFile")[0];
		String attFilePath = "sign_"+Long.toString(System.nanoTime()) + "_" + user.get(User.MAIL_USER_SEQ)+".img";
		String tmpAttFileName = attFilePath;
		
		if (Validation.isImageFile(attFileName)) {
		
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
			
			request.setAttribute("signImageUrl", viewUrl);
			request.setAttribute("signImageSrc", tmpAttFileName);
			request.setAttribute("signImageName", attFileName);
		}
		
		return "success";
	}
}
