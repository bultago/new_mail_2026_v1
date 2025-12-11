/**
 * UploadMessageAction.java 2009. 2. 17.
 *
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 *
 */
package com.terracetech.tims.webmail.mail.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

import jakarta.mail.internet.MimeMessage;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.json.simple.JSONObject;
import org.springframework.mail.MailAuthenticationException;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.util.Validation;

/**
 * <p><strong>UploadMessageAction.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0
 */
@SuppressWarnings("all")
public class UploadMessageAction extends BaseAction {

	public UploadMessageAction() {
		setAuthCheck(false);
	}

	private static final long serialVersionUID = -9002037226251315702L;

	@Override
	public String execute() throws Exception{
		//User user = EnvConstants.getTestUser();

		String agent = request.getHeader("user-agent");
		boolean isOcx = (agent.indexOf("TerraceUpload") > -1);
		boolean isFlash = false;
		if("flash".equals(request.getParameter("uploadType"))){
			this.user = UserAuthManager.getUser(request,request.getParameter(EnvConstants.getBasicSetting("cookie.name")));
			isFlash = true;
		}


		checkUser();

		MultiPartRequestWrapper multiWrapper = null;

		if(request instanceof MultiPartRequestWrapper) {
			multiWrapper = (MultiPartRequestWrapper) request;
		}

		I18nResources msgResource = getMessageResource();
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder folder = null;

		String resultMessage = "";
		String resultStatus = "OK";
		PrintWriter out = null;

		//HTML5 Uploader S
		File [] arrAttFile = null;
		String [] arrAttFileName = null;
		String [] arrTempFileName = null;
		Long [] arrAttFileSize = null;
		String [] arrAttachMode = null;
		String [] arrAttMessageId = null;

		try {
			String folderName = multiWrapper.getParameter("folder");
			folderName = StringUtils.UTF8URLDecode(folderName);
			store = factory.connect(request.getRemoteAddr(), user);
			folder = store.getFolder(folderName);
			String filePath = "test";

			BufferedInputStream bis	= null;
			MimeMessage msg = null;
			TMailMessage[] messages = null;
			StringBuilder returnMessages = new StringBuilder();
			returnMessages.append("[" +  msgResource.getMessage("menu.uploadmsg") + "]");

			if(multiWrapper.getFiles("theFile").length > 0){
				int i = 0;
				for(File emlFile : multiWrapper.getFiles("theFile")){
					if (Validation.isEmlFile(multiWrapper.getFileNames("theFile")[i])) {
						try {
							bis	= new BufferedInputStream(new FileInputStream(emlFile));
							msg	= new MimeMessage(factory.getSession(), bis);
							messages = new TMailMessage[1];
							messages[0] = new TMailMessage(msg);
							folder.appendMessages(messages);
							returnMessages.append("\\n" + multiWrapper.getFileNames("theFile")[i] + " : " + msgResource.getMessage("alert.messageupload"));
						} catch (Exception e) {
							LogManager.writeErr(this, e.getMessage(), e);
							String errorMsg = e.getMessage();
							if(errorMsg.indexOf("quota") > -1){
								returnMessages.append("\\n" + multiWrapper.getFileNames("theFile")[i] + " : " + msgResource.getMessage("error.upload.quota"));
								resultStatus = "QUOTAOVER";
							}else {
								returnMessages.append("\\n" + multiWrapper.getFileNames("theFile")[i] + " : " + msgResource.getMessage("error.upload"));
								resultStatus = "ERROR";
							}
						}
						i++;
					}
				}

				//return a forward to display.jsp
				resultMessage = returnMessages.toString();
				if (isOcx) {
					response.setHeader("Content-Type", "text/html; charset=UTF-8");
					out = response.getWriter();
					out.print("UPKEY=SUCCESS\\r\\n");
					out.print("RESULTSTATUS=" + resultStatus + "\\r\\n");
					out.print("TYPE=message\\r\\n");
					out.print("RESULT=OK");
				} else if(isFlash){
					JSONObject jObj = new JSONObject();
					jObj.put("result", resultStatus);
					ResponseUtil.processResponse(response, jObj);
				} else{
					response.setHeader("Content-Type", "text/html; charset=UTF-8");
					out = response.getWriter();
					out.print("<script>alert('"+resultMessage+"'); try{parent.closeUploadModal();}catch(e){}</script>");
				}
			}

		} catch (Exception e){
			e.printStackTrace();
			LogManager.writeErr(this, e.getMessage(), e);
		} finally {
			if(store !=null && store.isConnected())
				store.close();
			if(out != null)
				out.close();
		}
		return null;
	}


	public void checkUser() throws Exception{
		if(this.user != null){
			int sesstionCheckTime = Integer.parseInt(user.get(User.SESSION_CHECK_TIME));
			if(sesstionCheckTime > 0)UserAuthManager.setSessionTime(response);
		} else {
			log.debug("BaseAction.NOT EXIST USER");
			throw new MailAuthenticationException("NOT EXIST USER");
		}
	}
}
