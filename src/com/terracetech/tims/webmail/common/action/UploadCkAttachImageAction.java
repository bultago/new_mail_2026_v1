package com.terracetech.tims.webmail.common.action;

import java.io.File;
import java.io.PrintWriter;
import java.net.URLEncoder;

import jakarta.servlet.http.HttpSession;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.ImageFormatUtil;
import com.terracetech.tims.webmail.util.Validation;

public class UploadCkAttachImageAction extends BaseAction {

	
	public String execute() throws Exception {
		HttpSession session = request.getSession();
		if(session.getAttribute("authStatus")!=null){
			  return "paidError";
		}
		String id = user.get(User.MAIL_UID);
		String tempPath = EnvConstants.getBasicSetting("tmpdir");
		
		I18nResources resource = getMessageResource("setting");
		String retVal 	= "0";
		String fileUrl 	= EnvConstants.getAttachSetting("upimage.url");
		String fileName = "";
		String filePath = "";;
		String errorMessage = "";
		
		MultiPartRequestWrapper multiWrapper = null;		
		if(request instanceof MultiPartRequestWrapper) {
			multiWrapper = (MultiPartRequestWrapper) request;		
		}
		
		if(multiWrapper == null ||
				multiWrapper.getFiles("upload")[0] == null){
			throw new Exception("upload Attach Image request or File is null");			
		}
		try {			
			File imgFile = multiWrapper.getFiles("upload")[0];
			fileName	= multiWrapper.getFileNames("upload")[0];			
			
			String fileFormat = ImageFormatUtil.getFormatInFile(imgFile);
			
			if ( !Validation.isImageFile(fileName) || !Validation.isImageFormat(fileFormat)) {
				throw new Exception(resource.getMessage("conf.sign.8"));
			}
			
        	// Make Upload File Name        	
			String tempFileName = Long.toString(System.nanoTime()) + id + ".TSMATTIMG";

			filePath = tempFileName;

			try {				
				FileUtil.saveTmpFile(imgFile,filePath);

				fileUrl += "img=" + tempFileName + "|" + URLEncoder.encode(fileName, "UTF-8");

			} catch (Exception fex) {				
				LogManager.writeErr(this, fex.getMessage(), fex);
				retVal = "101";
				errorMessage = fex.getMessage();
			}
		} catch (Exception ex) {
			LogManager.writeErr(this, ex.getMessage(), ex);
			retVal = "401";
			errorMessage = ex.getMessage();
		}
		
		
		
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<script type=\"text/javascript\">");
		out.println("window.parent.CKEDITOR.tools.callFunction(2, '"+fileUrl+"', '"+errorMessage+"');</script>");
		out.flush();
		out.close();
		
		return null;
	}
}
