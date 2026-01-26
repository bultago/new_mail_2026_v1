package com.terracetech.tims.jmobile.mail.action;

import java.util.Map;

import com.terracetech.tims.jmobile.common.action.BaseAction;
import com.terracetech.tims.service.manager.MailServiceManager;
import com.terracetech.tims.service.tms.vo.WriteCondVO;
import com.terracetech.tims.service.tms.vo.WriteResultVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;

public class MailWriteAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 61878819768562973L;
	
	private MailServiceManager mailServiceManager = null;
	
	public void setMailServiceManager(MailServiceManager mailServiceManager) {
		this.mailServiceManager = mailServiceManager;
	}
	
	public String execute() throws Exception {
		
		String forward = "success";
		try {
			
			String to = request.getParameter("to");
			String cc = request.getParameter("cc");
			String bcc = request.getParameter("bcc");
			String subject = request.getParameter("subject");
			String content = request.getParameter("content");
			String folderName = request.getParameter("folderName");
			String uid = request.getParameter("uid");
			String writeType = request.getParameter("writeType");
			
			Object writeParam = request.getSession().getAttribute("writeParamMap");
			Map writeParamMap = null;
			
			if(writeParam != null){
				writeParamMap = (Map)writeParam;
				if(writeParamMap.containsKey("uid")){
					uid = (String)writeParamMap.get("uid");
				}
				if(writeParamMap.containsKey("folderName")){
					folderName = (String)writeParamMap.get("folderName");
				}				
				if(writeParamMap.containsKey("writeType")){
					writeType = (String)writeParamMap.get("writeType");
				}
				request.getSession().removeAttribute("writeParamMap");
			}
			
			WriteCondVO writeVO = new WriteCondVO();
			writeVO.setWriteType(writeType);
			writeVO.setRemoteIp(request.getRemoteAddr());
			writeVO.setUserEmail(user.get(User.EMAIL));
			writeVO.setLocale(user.get(User.LOCALE));
			writeVO.setMobileMode(false);
			writeVO.setEditorMode("text");
			//writeVO.setEditorMode(userSettingVo.getWriteMode());
			writeVO.setUids(new String[]{uid});
			writeVO.setFolderName(folderName);	
			writeVO.setReqTo(to);
			writeVO.setReqCc(cc);
			writeVO.setReqBcc(bcc);
			writeVO.setReqSubject(subject);
			writeVO.setReqContent(content);			
			writeVO.setForwardingMode("parsed");
			WriteResultVO writeResultVO = mailServiceManager.doSimpleMailWrite(writeVO, user);
			
			String contents = writeResultVO.getTextNormalContent();
			if(contents != null && contents.length() > 0){
				byte[] contentsBytes = contents.getBytes(); 
				writeResultVO.setTextNormalContent(splitContents(0,MAIL_CONTENTS_SIZE,contentsBytes)+"...");
			}
			
			request.setAttribute("writeContent", writeResultVO);
			
			saveCurrentAction("maillist");
		}catch (Exception e) {			
			LogManager.writeErr(this, e.getMessage(), e);
			forward = "errorAlert";
		}
		
		return forward;
	}
	
	public String splitContents(int pos, int size, byte[] contentsBytes){
		int startBytesPos = pos * size;
		if((contentsBytes.length - startBytesPos) < size){	
			size = (contentsBytes.length - startBytesPos);
		}
		
		if(size < 0){
			size = 0;
		}
		
		byte[] readContents = new byte[size];
		System.arraycopy(contentsBytes, startBytesPos, readContents, 0, size);
		int multi = 0;
		for (int i = 0; i < size; i++) {
			if( readContents[i] < 0)multi++;
		}
		if((multi % 2) != 0){
			readContents = new byte[size-(multi % 2)];
			System.arraycopy(contentsBytes, 0, readContents, 0, readContents.length);
		}
		return new String(readContents);	
	}

}
