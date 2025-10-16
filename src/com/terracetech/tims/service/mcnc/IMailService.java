package com.terracetech.tims.service.mcnc;

import java.rmi.Remote;

import com.terracetech.tims.service.mcnc.exception.GarnetException;
import com.terracetech.tims.service.mcnc.vo.AttachmentInfoWDO;
import com.terracetech.tims.service.mcnc.vo.AttachmentWDO;
import com.terracetech.tims.service.mcnc.vo.AttributeWDO;
import com.terracetech.tims.service.mcnc.vo.MailBoxWDO;
import com.terracetech.tims.service.mcnc.vo.MailBriefListWDO;
import com.terracetech.tims.service.mcnc.vo.MailWDO;
import com.terracetech.tims.service.mcnc.vo.OptionWDO;
import com.terracetech.tims.service.mcnc.vo.UserAuthWDO;

public interface IMailService extends Remote {
	public MailBoxWDO[] getMailBoxes(
			UserAuthWDO userAuth,
			String companyID,
			AttributeWDO[] attribute) throws GarnetException;
	
	public String[] getSubFolders(
			UserAuthWDO userAuth,
			MailBoxWDO mailBox ) throws GarnetException;
	
	public MailBriefListWDO getMailList(
		UserAuthWDO userAuth,
		MailBoxWDO mailBox,
		String subFolder,
		AttributeWDO[] attributes
		) throws GarnetException;

	public MailWDO getMail( 
			UserAuthWDO userAuth, 
			MailBoxWDO mailBox, 
			String subFolder,
			String mailID ) throws GarnetException;
	
	public boolean sendMail( 
			UserAuthWDO userAuth, 
			MailBoxWDO mailBox, 
			String subFolder,
			MailWDO mail,
			String mailType,
			AttributeWDO[] attributes,
			OptionWDO[] option) throws GarnetException;

	public boolean removeMail( 
			UserAuthWDO userAuth, 
			MailBoxWDO mailBox, 
			String subFolder,
			String mailID ) throws GarnetException;
	
	public boolean moveMail( 
			UserAuthWDO userAuth,
			MailBoxWDO srcMailBox, 
			String srcSubFolder,
			String srcMailID,
			MailBoxWDO dstMailBox, 
			String dstSubFolder ) throws GarnetException;
	
	public int getUnreadMailCount(
			UserAuthWDO userAuth,
			MailBoxWDO mailBox,
			String subFolder,
			AttributeWDO[] attribute) throws GarnetException;
	
	public AttachmentInfoWDO[] getAttachmentList(
			UserAuthWDO userAuth,
			MailBoxWDO mailBox,
			String subFolder,
			String mailID ) throws GarnetException;
	
	public AttachmentWDO getAttachment( 
			UserAuthWDO userAuth,
			MailBoxWDO mailBox,
			String subFolder,
			String mailID,
			String attachmentID ) throws GarnetException;
	
	public boolean copyMail( 
			UserAuthWDO userAuth, 
			MailBoxWDO srcMailBox, 
			String srcSubFolder,
			String srcMailID,
			MailBoxWDO dstMailBox, 
			String dstsubFolder ) throws GarnetException;

	public boolean saveTempMail(
			UserAuthWDO userAuth,
			MailBoxWDO mailBox,
			MailWDO content,
			AttributeWDO[] attribute)  throws GarnetException;
}
