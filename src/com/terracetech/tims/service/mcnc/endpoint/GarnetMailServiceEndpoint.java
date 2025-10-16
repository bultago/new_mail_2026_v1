package com.terracetech.tims.service.mcnc.endpoint;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.terracetech.tims.service.mcnc.IMailService;
import com.terracetech.tims.service.mcnc.exception.GarnetException;
import com.terracetech.tims.service.mcnc.impl.GarnetMailManager;
import com.terracetech.tims.service.mcnc.vo.AttachmentInfoWDO;
import com.terracetech.tims.service.mcnc.vo.AttachmentWDO;
import com.terracetech.tims.service.mcnc.vo.AttributeWDO;
import com.terracetech.tims.service.mcnc.vo.MailBoxWDO;
import com.terracetech.tims.service.mcnc.vo.MailBriefListWDO;
import com.terracetech.tims.service.mcnc.vo.MailWDO;
import com.terracetech.tims.service.mcnc.vo.OptionWDO;
import com.terracetech.tims.service.mcnc.vo.UserAuthWDO;

public class GarnetMailServiceEndpoint extends ServletEndpointSupport implements IMailService {
	
	private GarnetMailManager manager = null;
	private String remoteIp = "";
	private HttpServletRequest req = null;
	protected void onInit() { 
		this.manager = (GarnetMailManager)getWebApplicationContext().getBean("garnetMailManager");
		
		MessageContext msgCtx = (MessageContext) getServletEndpointContext().getMessageContext ();
		req = (HttpServletRequest) msgCtx.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
		remoteIp = req.getRemoteAddr();		
	}
	
	public AttachmentWDO getAttachment(UserAuthWDO userAuth,
			MailBoxWDO mailBox, String subFolder, String mailID,
			String attachmentID) throws GarnetException {
		userAuth.setRemoteIp(remoteIp);
		return manager.getAttachment(userAuth, mailBox, subFolder, mailID, attachmentID, req);
	}

	public AttachmentInfoWDO[] getAttachmentList(UserAuthWDO userAuth,
			MailBoxWDO mailBox, String subFolder, String mailID)
			throws GarnetException {
		userAuth.setRemoteIp(remoteIp);
		return manager.getAttachmentList(userAuth, mailBox, subFolder, mailID, req);
	}

	public MailWDO getMail(UserAuthWDO userAuth, MailBoxWDO mailBox,
			String subFolder, String mailID) throws GarnetException {
		userAuth.setRemoteIp(remoteIp);
		return manager.getMail(userAuth, mailBox, subFolder, mailID, req);
	}

	public MailBoxWDO[] getMailBoxes(UserAuthWDO userAuth, String companyID,
			AttributeWDO[] attribute) throws GarnetException {
		userAuth.setRemoteIp(remoteIp);
		return manager.getMailBoxes(userAuth, companyID, attribute);
	}

	public MailBriefListWDO getMailList(UserAuthWDO userAuth,
			MailBoxWDO mailBox, String subFolder, AttributeWDO[] attributes)
			throws GarnetException {
		userAuth.setRemoteIp(remoteIp);
		return manager.getMailList(userAuth, mailBox, subFolder, attributes);
	}

	public String[] getSubFolders(UserAuthWDO userAuth, MailBoxWDO mailBox)
			throws GarnetException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getUnreadMailCount(UserAuthWDO userAuth, MailBoxWDO mailBox,
			String subFolder, AttributeWDO[] attribute) throws GarnetException {
		userAuth.setRemoteIp(remoteIp);
		return manager.getUnreadMailCount(userAuth, mailBox, subFolder, attribute);
	}
	
	public boolean copyMail(UserAuthWDO userAuth, MailBoxWDO srcMailBox,
			String srcSubFolder, String srcMailID, MailBoxWDO dstMailBox,
			String dstsubFolder) throws GarnetException {
		userAuth.setRemoteIp(remoteIp);
		return manager.copyMail(userAuth, srcMailBox, srcSubFolder, srcMailID, dstMailBox, dstsubFolder);
	}
	
	public boolean moveMail(UserAuthWDO userAuth, MailBoxWDO srcMailBox,
			String srcSubFolder, String srcMailID, MailBoxWDO dstMailBox,
			String dstSubFolder) throws GarnetException {
		userAuth.setRemoteIp(remoteIp);
		return manager.moveMail(userAuth, srcMailBox, srcSubFolder, srcMailID, dstMailBox, dstSubFolder);
	}

	public boolean removeMail(UserAuthWDO userAuth, MailBoxWDO mailBox,
			String subFolder, String mailID) throws GarnetException {
		userAuth.setRemoteIp(remoteIp);
		return manager.removeMail(userAuth, mailBox, subFolder, mailID);
	}
	
	public boolean sendMail(UserAuthWDO userAuth, MailBoxWDO mailBox,
			String subFolder, MailWDO mail, String mailType, 
			AttributeWDO[] attribute,
			OptionWDO[] option)
			throws GarnetException {
		userAuth.setRemoteIp(remoteIp);
		return manager.sendMail(userAuth, mailBox, subFolder, mail, mailType, attribute, option, req);
	}

	public boolean saveTempMail(UserAuthWDO userAuth, MailBoxWDO mailBox,
			MailWDO content, AttributeWDO[] attribute) throws GarnetException {
		userAuth.setRemoteIp(remoteIp);
		return manager.saveTempMail(userAuth, mailBox, content, attribute, req);
	}
	
}
