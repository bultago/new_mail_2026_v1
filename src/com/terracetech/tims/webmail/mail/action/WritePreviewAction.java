/**
 * WritePreviewAction.java 2009. 4. 24.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.action;

import org.apache.commons.lang.StringEscapeUtils;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.body.AttachSignMsgBodyCreator;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SignManager;
import com.terracetech.tims.webmail.setting.vo.SignDataVO;
import com.terracetech.tims.webmail.util.StringReplaceUtil;

/**
 * <p><strong>WritePreviewAction.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class WritePreviewAction extends BaseAction {
	
	private SignManager signManager;
	
	/**
	 * <p></p>
	 */
	private static final long serialVersionUID = 1053879760180269249L;
	
	public String execute() throws Exception{
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String localhost = request.getScheme() + "://" 
		+ request.getServerName() + ":" + request.getServerPort();		
		String imgPath = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");		
		String attachPath = EnvConstants.getAttachSetting("attach.dir");
		
		String strTo = request.getParameter("to");
		String strCc = request.getParameter("cc");
		String strBcc = request.getParameter("bcc");
		String content = request.getParameter("content");
		String subject = request.getParameter("subject");
		String signSeq = request.getParameter("signseq");
		String senderEmail = request.getParameter("senderEmail");
		String senderName = request.getParameter("senderName");
		
		String strFrom = "";
		
		if(senderName != null){
			strFrom += "\'"+StringEscapeUtils.escapeHtml(senderName) + "\"";			
		} else {
			strFrom += "\'"+user.get(User.USER_NAME) + "\"";
		} 
		
		if(senderEmail != null){
			strFrom += "&lt;"+StringEscapeUtils.escapeHtml(senderEmail) + "&gt;";			
		} else {
			strFrom += "&lt;"+user.get(User.EMAIL) + "&gt;";
		} 
		
		boolean isAttachSign = "on".equals(request.getParameter("attachsign"));
		
		SenderInfoBean infoBean = new SenderInfoBean();
		infoBean.setAttachSign(isAttachSign);
		infoBean.setHtmlMode("html".equals(request.getParameter("wmode")));		
		
		if (!infoBean.isHtmlMode() && !isAttachSign) {
			StringReplaceUtil sr = new StringReplaceUtil();
			content = sr.getTextToHTML(content);
		}
		infoBean.setContent(content);
		
		SignDataVO sign = null;
		if(isAttachSign && signSeq != null){			
			sign = signManager.getSignData(userSeq, Integer.parseInt(signSeq));			
			infoBean.setSignData(sign);
		}
		
		AttachSignMsgBodyCreator signCreator = new AttachSignMsgBodyCreator(infoBean);
		signCreator.executePreview(localhost, imgPath, attachPath);
		
		content = infoBean.getContent();
		
		request.setAttribute("from",strFrom);
		request.setAttribute("to",strTo);
		request.setAttribute("cc",strCc);
		request.setAttribute("bcc",strBcc);
		request.setAttribute("subject",subject);
		request.setAttribute("content",content);		
		
		return "success";
	}
	
	
	public void setSignManager(SignManager signManager) {
		this.signManager = signManager;
	}

}
