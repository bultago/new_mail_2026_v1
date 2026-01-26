package com.terracetech.tims.webmail.common.action;

import java.io.File;
import java.net.URLEncoder;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.ImageFormatUtil;
import com.terracetech.tims.webmail.util.Validation;

public class UploadSmartAttachImageAction extends BaseAction {

	
	public String execute() throws Exception {
		String id = user.get(User.MAIL_UID);
        String callback = request.getParameter("callback");
        String callbackFunc = request.getParameter("callback_func");
        I18nResources resource = getMessageResource("setting");
        String retVal = "0";
        String fileUrl = EnvConstants.getAttachSetting("upimage.url");
        String fileName = "";
        String filePath = "";
        String errorMessage = "";
        
        MultiPartRequestWrapper multiWrapper = null;
        if (request instanceof MultiPartRequestWrapper) {
            multiWrapper = (MultiPartRequestWrapper) request;
        }

        if (multiWrapper == null || multiWrapper.getFiles("NewFile")[0] == null) {
            throw new Exception("upload Attach Image request or File is null");
        }
        try {
            File imgFile = multiWrapper.getFiles("NewFile")[0];
            fileName = multiWrapper.getFileNames("NewFile")[0];

            String fileFormat = ImageFormatUtil.getFormatInFile(imgFile);

            if (!Validation.isImageFile(fileName) || !Validation.isImageFormat(fileFormat)) {
                throw new Exception(resource.getMessage("conf.sign.8"));
            }

            // Make Upload File Name
            String tempFileName = Long.toString(System.nanoTime()) + id + ".TSMATTIMG";

            filePath = tempFileName;

            try {
                FileUtil.saveTmpFile(imgFile, filePath);

                fileUrl += "img=" + tempFileName + "," + URLEncoder.encode(fileName, "UTF-8"); //TCUSTOM-3734 201801

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
        
        String url = callback+"?callback_func="+callbackFunc;
        url += "&bNewLine=true";
        //url += "&sFileName="+fileName;
        url += "&sFileName="+URLEncoder.encode(fileName, "UTF-8");
        url += "&sFileURL="+fileUrl;
        
        response.sendRedirect(url);
        return null;
	}
}
