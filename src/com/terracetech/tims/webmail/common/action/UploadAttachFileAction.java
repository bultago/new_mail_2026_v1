/**
 * UploadFileAction.java 2009. 4. 3.
 *
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 *
 */
package com.terracetech.tims.webmail.common.action;

import java.io.File;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.internet.MimeMessage;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.mail.MailAuthenticationException;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mail.manager.BigattachManager;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mail.vo.MailBigAttachVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.ResponseUtil;

/**
 * <p><strong>UploadFileAction.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0
 */

@SuppressWarnings("unchecked")
public class UploadAttachFileAction extends BaseAction {

	public UploadAttachFileAction() {
		setAuthCheck(false);
	}

	private BigattachManager bigAttachManager = null;
	private SystemConfigManager systemConfigManager = null;

	/**
	 * @param bigAttachManager �Ķ���͸� bigAttachManager���� ����
	 */
	public void setBigAttachManager(BigattachManager bigAttachManager) {
		this.bigAttachManager = bigAttachManager;
	}

	public void setSystemConfigManager(SystemConfigManager systemConfigManager) {
		this.systemConfigManager = systemConfigManager;
	}

	/**
	 * <p></p>
	 */
	private static final long serialVersionUID = -8077972251304834801L;


	public String updateBigAttachInfo() throws Exception{
		checkUser();
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder folder = null;

		String[] seqFileIdx = request.getParameterValues("hidx");
		String[] hugeFilePath = request.getParameterValues("hfilePath");
		String[] hugeFileSize = request.getParameterValues("hfileSize");
		String[] hugeFileName = request.getParameterValues("hfileName");
		String tmpDir = EnvConstants.getBasicSetting("tmpdir");
		try {

			Map attachConfigMap = systemConfigManager.getAttachConfig();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("email", user.get(User.EMAIL));
			paramMap.put("expiredays",(String)attachConfigMap.get("bigattach_expireday"));
			paramMap.put("regdate", request.getParameter("regdate"));

			Map<String, String> connectionInfoMap =  bigAttachManager.getBigAttachConnectInfo(user);
			store = factory.connect(request.getRemoteAddr(),connectionInfoMap);
			folder = store.getFolder(FolderHandler.BIGATTACHHOME);
			if(!folder.exists()){
				folder.create();
			}
			JSONObject info = new JSONObject();
			JSONArray infoList = new JSONArray();
			File tempFile = null;
			for (int i = 0; i < seqFileIdx.length; i++) {

				MimeMessage message = new MimeMessage(factory.getSession());
				tempFile = new File(tmpDir + EnvConstants.DIR_SEPARATOR + "attach_"+Long.toString(System.nanoTime()) + "_"+paramMap.get("email") + ".u");
				tempFile.createNewFile();

				message.setDataHandler(new DataHandler(new FileDataSource(tempFile)));

				// Set DB
				MailBigAttachVO vo = new MailBigAttachVO();
				vo.setAttachFlag("");
				vo.setDownloadCount(0);
				vo.setFileName(hugeFileName[i]);
				vo.setFolderPath(user.get(User.MESSAGE_STORE) + FolderHandler.BIGATTACH);
				vo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
				vo.setFileSize(hugeFileSize[i]);

				bigAttachManager.uploadBigattachFile(vo, paramMap, folder, message, new File[]{new File(tmpDir + EnvConstants.DIR_SEPARATOR + hugeFilePath[i])});

				tempFile.delete();
				JSONObject jObj = new JSONObject();
				jObj.put("seq", seqFileIdx[i]);
				jObj.put("uid", vo.getMessageUid());
				infoList.add(jObj);
			}
			info.put("hugeInfoList", infoList);
			ResponseUtil.processResponse(response, info);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(store != null){
				store.close();
			}
		}

		return null;
	}


	@Override
	public String execute() throws Exception{

		String agent = request.getHeader("user-agent");
		agent = agent.toLowerCase();
		if(agent.indexOf("flash") > -1){
			this.user = UserAuthManager.getUser(request,request.getParameter(EnvConstants.getBasicSetting("cookie.name")));
		}
		checkUser();

		Map<String, String> reqParam = new HashMap<String, String>();
		MultiPartRequestWrapper multiWrapper = null;
		if(request instanceof MultiPartRequestWrapper) {
			multiWrapper = (MultiPartRequestWrapper) request;
		}
		I18nResources resource = getMessageResource("common");
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder folder = null;
		String forward = null;

		try {
			File attFile = null;
			long attFileSize = 0;
			String attFileName = null;

			// Process the uploaded items
			Iterator itr = 	multiWrapper.getParameterMap().keySet().iterator();
			String field,fieldValue;
			while(itr.hasNext()) {
				field = (String)itr.next();
				fieldValue = multiWrapper.getParameter(field);
				reqParam.put(field, fieldValue);
			}

			Map attachConfigMap = systemConfigManager.getAttachConfig();
			String reqValue = null;
			String uploadType = reqParam.get("uploadType");
			String maxFileSizeValue = reqParam.get("maxAttachFileSize");
			int maxFileSize = (maxFileSizeValue != null)?Integer.parseInt(maxFileSizeValue):-1;

			reqValue = reqParam.get("attachtype");
			String attachtype = (reqValue != null) ? reqValue : "normal";

			boolean isBigAttach = (attachtype != null) ? attachtype.equals("huge") : false;
			boolean isMassRcptFile = (attachtype != null) ? attachtype.equals("massrcpt") : false;


			//HTML5 Uploader S
			File [] arrAttFile = null;
			String [] arrAttFileName = null;
			String [] arrTempFileName = null;
			Long [] arrAttFileSize = null;
			String [] arrAttachMode = null;
			String [] arrAttMessageId = null;
			//HTML5 Uploader E

			if(isMassRcptFile){
				attFile = multiWrapper.getFiles("massRcptFile")[0];
				attFileName = multiWrapper.getFileNames("massRcptFile")[0];
			} else {
				if(multiWrapper.getFiles("theFile") == null){
					throw new SocketException("REQUEST NOT EXIST FILE.");
				}

				//HTML5 Uploader S
				if(uploadType.equals("ajax")){
					if(multiWrapper.getFiles("theFile").length > 0){
						arrAttFile = multiWrapper.getFiles("theFile");
						arrAttFileName = multiWrapper.getFileNames("theFile");
					}
				} else {
					if(multiWrapper.getFiles("theFile").length == 1){
						attFile = multiWrapper.getFiles("theFile")[0];
						attFileName = multiWrapper.getFileNames("theFile")[0];
						attFileSize = attFile.length();
					}
				}
				//HTML5 Uploader E
			}

			//attFileName = StringUtils.uni2latin(attFileName);


			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("email", user.get(User.EMAIL));
			paramMap.put("expiredays",(String)attachConfigMap.get("bigattach_expireday"));
			paramMap.put("regdate", reqParam.get("regdate"));

			String tempFileName = null;
			String attFilePath = null;
			String messageUid = "0";
			boolean isOutOfMemoryError = false;

			File tempFile = null;
			if (isBigAttach) {
				String tmpDir = EnvConstants.getBasicSetting("tmpdir");
				try{
					Map<String, String> connectionInfoMap =  bigAttachManager.getBigAttachConnectInfo(user);
					store = factory.connect(request.getRemoteAddr(),connectionInfoMap);
					MimeMessage message = new MimeMessage(factory.getSession());

					folder = store.getFolder(FolderHandler.BIGATTACHHOME);
					if(!folder.exists()){
						folder.create();
					}

					tempFileName = "attach_"+Long.toString(System.nanoTime()) + "_"+paramMap.get("email") + (isMassRcptFile ? ".r" : ".u");
					tempFile = new File(tmpDir + EnvConstants.DIR_SEPARATOR + tempFileName);
					tempFile.createNewFile();
					message.setDataHandler(new DataHandler(new FileDataSource(tempFile)));

					// Set DB
					MailBigAttachVO vo = new MailBigAttachVO();
					vo.setAttachFlag("");
					vo.setDownloadCount(0);
					vo.setFileName(attFileName);
					vo.setFolderPath(user.get(User.MESSAGE_STORE) + FolderHandler.BIGATTACH);
					vo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
					vo.setFileSize(Long.toString(attFileSize));

					bigAttachManager.uploadBigattachFile(vo, paramMap, folder, message,new File[]{attFile});
					messageUid = vo.getMessageUid();
					tempFile.delete();
				} catch (VirtualMachineError ve) {
					ve.printStackTrace();
					isOutOfMemoryError = true;
				}

				attFilePath = "";
			} else {
				//HTML5 Uploader S
				if(arrAttFile != null){
					arrTempFileName = new String[arrAttFile.length];
					arrAttFileSize = new Long[arrAttFile.length];
					arrAttachMode = new String[arrAttFile.length];
					arrAttMessageId = new String[arrAttFile.length];


					for(int i=0; i < arrAttFile.length; i++){
						arrTempFileName[i] = "attach_"+Long.toString(System.nanoTime()) + "_"+paramMap.get("email") + (isMassRcptFile ? ".r" : ".u");
						arrAttFileSize[i] = arrAttFile[i].length();

						FileUtil.saveTmpFile(arrAttFile[i], arrTempFileName[i]);
						if(maxFileSize > -1){
							if((maxFileSize*1024*1024) >= arrAttFileSize[i]){
								arrAttachMode[i] = "normal";
							}  else {
								arrAttachMode[i] = "huge";
							}
						} else {
							arrAttachMode[i] = "normal";
						}
					}
				//HTML5 Uploader E
				} else {
					tempFileName = "attach_"+Long.toString(System.nanoTime()) + "_"+paramMap.get("email") + (isMassRcptFile ? ".r" : ".u");
					attFilePath = FileUtil.saveTmpFile(attFile,tempFileName);
					if(maxFileSize > -1){
						if((maxFileSize*1024*1024) >= attFileSize){
							attachtype = "normal";
						} else {
							attachtype = "huge";
						}
					} else {
						attachtype = "normal";
					}
				}
			}
			if(uploadType.equals("power")){
				attFileName = new String(attFileName.getBytes(),"8859_1");
				response.setHeader("Content-Type", "text/html; charset=iso-8859-1");

				PrintWriter out = response.getWriter();
				if(!isOutOfMemoryError){
					out.print("EMAIL=" + user.get(User.EMAIL) + "\\r\\n");
					out.print("FILEPATH=" + tempFileName + "\\r\\n");
					out.print("UPKEY=" + tempFileName + "\\r\\n");
					out.print("SIZE=" + attFileSize + "\\r\\n");
					out.print("FILENAME=" + attFileName + "\\r\\n");
					out.print("UID=" + messageUid + "\\r\\n");
					out.print("TYPE=" + attachtype + "\\r\\n");
					out.print("RESULT=OK");
				} else {
					out.print("File Upload Fail");
				}


			} else if(uploadType.equals("basic")){
				if(attFileSize > 0){
					request.setAttribute("fileSize", attFileSize);
					request.setAttribute("fileName", attFileName);
					request.setAttribute("filePath", tempFileName);
					request.setAttribute("attatchType", (isMassRcptFile)?"mass":"attach");
					request.setAttribute("attachMode", attachtype);

					forward = "simpleResult";
				}
			} else if(uploadType.equals("flash")){
				JSONObject jObj = new JSONObject();
				if(!isOutOfMemoryError){
					jObj.put("uploadResult","success");
					jObj.put("fileSize",attFileSize);
					jObj.put("fileName",attFileName);
					jObj.put("filePath",tempFileName);
					jObj.put("attatchType","attach");
					jObj.put("uid",messageUid);
					jObj.put("attachMode", (isBigAttach)?"huge":"normal");
				} else {
					jObj.put("uploadResult","fail");
				}

				ResponseUtil.processResponse(response, jObj);

			} else if(uploadType.equals("ajax")){
				//HTML5 Uploader S
				forward = null;
				JSONObject jOutObj = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				if(!isOutOfMemoryError){
					jOutObj.put("uploadResult","success");
					if(arrAttFile != null){
						jOutObj.put("length", arrAttFile.length);

						for(int i=0; i < arrAttFile.length; i++){
							JSONObject jObj = new JSONObject();
							jObj.put("fileSize", arrAttFileSize[i]);
							jObj.put("fileName", arrAttFileName[i]);
							jObj.put("filePath", arrTempFileName[i]);
							jObj.put("attatchType","attach");
							jObj.put("uid", "");
							jObj.put("attachMode", arrAttachMode[i]);
							jsonArray.add(jObj);
						}
					}
					jOutObj.put("files", jsonArray);
				} else {
					jOutObj.put("uploadResult","fail");
				}
				ResponseUtil.processResponse(response, jOutObj);
				//HTML5 Uploader E
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getCause() instanceof SocketException){
			}else{
				LogManager.writeErr(this, e.getMessage(), e);
			}
			JSONObject jObj = new JSONObject();
			jObj.put("uploadResult","fail");
			ResponseUtil.processResponse(response, jObj);
		} finally {
			if(store != null){
				store.close();
			}
		}

		return forward;
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
