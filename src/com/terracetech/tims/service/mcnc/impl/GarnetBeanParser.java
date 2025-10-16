package com.terracetech.tims.service.mcnc.impl;

import com.terracetech.tims.service.mcnc.vo.AttachmentInfoWDO;
import com.terracetech.tims.service.mcnc.vo.AttachmentWDO;
import com.terracetech.tims.service.mcnc.vo.BodyWDO;
import com.terracetech.tims.service.mcnc.vo.BoxWDO;
import com.terracetech.tims.service.mcnc.vo.ContactWDO;
import com.terracetech.tims.service.mcnc.vo.MailBoxWDO;
import com.terracetech.tims.service.mcnc.vo.MailBriefListWDO;
import com.terracetech.tims.service.mcnc.vo.MailBriefWDO;
import com.terracetech.tims.service.mcnc.vo.MailWDO;
import com.terracetech.tims.service.mcnc.vo.PayloadWDO;
import com.terracetech.tims.service.tms.vo.AttachFileVO;
import com.terracetech.tims.service.tms.vo.EmailAddressVO;
import com.terracetech.tims.service.tms.vo.FolderContentVO;
import com.terracetech.tims.service.tms.vo.ListContentVO;
import com.terracetech.tims.service.tms.vo.ListInfoVO;
import com.terracetech.tims.service.tms.vo.ViewContentVO;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;

public class GarnetBeanParser {
	
	public static MailBoxWDO[] parseMailBoxWDOs(FolderContentVO[] folderElems,String userID){
		MailBoxWDO[] boxWDO = null;
		if(folderElems != null){
			int size = folderElems.length;

			if(size < 0){
				size = 0;
			}
			
			boxWDO = new MailBoxWDO[size];			
			for (int i = 0; i < size; i++) {
				boxWDO[i] = parseMailBoxWDO(folderElems[i],userID);
			}
		}	
		return boxWDO;
	}
	
	public static MailBoxWDO parseMailBoxWDO(FolderContentVO folderElem, String userID){		
		MailBoxWDO tempBoxWDO = new MailBoxWDO();
		String code = "";
		String folderName = folderElem.getEncFolderName();
		if (folderName.equalsIgnoreCase(FolderHandler.INBOX)) {
			code = "001";
		} else if (folderName.equalsIgnoreCase(FolderHandler.SENT)) {
			code = "002";
		} else if (folderName.equalsIgnoreCase(FolderHandler.DRAFTS)) {
			code = "004";
		} else if (folderName.equalsIgnoreCase(FolderHandler.RESERVED)) {
			code = "006";
		} else if (folderName.equalsIgnoreCase(FolderHandler.SPAM)) {
			code = "007";
		} else if (folderName.equalsIgnoreCase(FolderHandler.TRASH)) {
			code = "005";
		}		
		tempBoxWDO.setBox(new BoxWDO(code,folderElem.getFolderName()));
		tempBoxWDO.setBoxPath(folderElem.getEncFolderName());
		tempBoxWDO.setHasUnreadMail((folderElem.getUnseen() > 0));
		tempBoxWDO.setUnreadMailCount(folderElem.getUnseen());
		tempBoxWDO.setUserID(userID);
		return tempBoxWDO;		
	}
	
	public static MailBriefListWDO parseMailBriefListWDO(ListInfoVO listInfo){
		MailBriefListWDO mailBriefListWDO = new MailBriefListWDO();		
		mailBriefListWDO.setCurrentPageNo(Integer.toString(listInfo.getPageNo()));
		mailBriefListWDO.setMails(parseMailBriefWDOs(listInfo.getListContents()));
		
		return mailBriefListWDO;
	}
	
	public static MailBriefWDO[] parseMailBriefWDOs(ListContentVO[] listContentVOs){
		MailBriefWDO[] mailBriefWDOs = null;
		if(listContentVOs != null){
			int size = listContentVOs.length;
			
			if(size < 0){
				size = 0;
			}
			
			mailBriefWDOs = new MailBriefWDO[size];			
			for (int i = 0; i < size; i++) {
				mailBriefWDOs[i] = parseMailBriefWDO(listContentVOs[i]);
			}
		}	
		return mailBriefWDOs;
	}
	
	public static MailBriefWDO parseMailBriefWDO(ListContentVO listContentVO){
		MailBriefWDO mailBriefWDO = new MailBriefWDO();
		ContactWDO sender = new ContactWDO();		
		String flag = listContentVO.getFlag().toLowerCase();
		String date = listContentVO.getDate();		
		String displayName = listContentVO.getName();
		displayName = (displayName != null)?displayName:"";
		
		sender.setMailAddress(listContentVO.getEmail());		
		sender.setUserName(displayName);
		
		int priority = listContentVO.getPriority();
		int type = 0;
		int importance = (priority == 1)?3:0;
		if(flag.indexOf("a") > -1){
			type = 2;
		} else if(flag.indexOf("c") > -1){
			type = 3;
		} else if(flag.indexOf("s") > -1){
			type = 1;
		}
		mailBriefWDO.setMailSize(listContentVO.getByteSize());
		mailBriefWDO.setHasAttachments((flag.indexOf("t") > -1));		
		mailBriefWDO.setReceivedDate(date.substring(0,8));
		mailBriefWDO.setReceivedTime(date.substring(9).replaceAll(":", ""));
		mailBriefWDO.setUID(Long.toString(listContentVO.getId()));
		mailBriefWDO.setMailType(type);
		mailBriefWDO.setImportance(importance);
		mailBriefWDO.setDisplayName(displayName);		
		mailBriefWDO.setSender(sender);		
		mailBriefWDO.setTitle(listContentVO.getSubject());
		
		return mailBriefWDO;
	}

	public static MailWDO parseMailWDO(ViewContentVO contentVO) {
		MailWDO mailWDO = new MailWDO();
		BodyWDO bodyContents = new BodyWDO();
		AttachmentInfoWDO[] attachmentInfos = null;		 
		
		int attachCnt = contentVO.getAttachsCnt();
		int flagAttachments;
		int priority = contentVO.getPriority();
		int importance = (priority == 1)?3:0;
		
		String rdate = contentVO.getDate();
		String sdate = contentVO.getSentDate();
		mailWDO.setSubject(contentVO.getSubject());
		mailWDO.setTo(parseEmailToContactWDOs(contentVO.getTos()));
		mailWDO.setCc(parseEmailToContactWDOs(contentVO.getCcs()));
		mailWDO.setFrom(parseMailBriefWDO(contentVO.getFrom()));
		bodyContents.setBodyType(0);
		bodyContents.setBody(contentVO.getContents());
		bodyContents.setEncoding(contentVO.getBodyEncoding());
		mailWDO.setBody(bodyContents);
		
		if(attachCnt > 0){
			flagAttachments = MailWDO.FLAG_ATTACHMENTS_NOT_INCLUDED;
			attachmentInfos = parseAttachmentInfoWDOs(contentVO.getAttachs());			
		} else {
			flagAttachments = MailWDO.FLAG_ATTACHMENTS_NO;
			attachmentInfos = new AttachmentInfoWDO[]{new AttachmentInfoWDO()};
		}
		
		
		mailWDO.setMailID(Long.toString(contentVO.getUid()));
		mailWDO.setAttachmentInfos(attachmentInfos);		
		mailWDO.setMailSize(contentVO.getSize());
		mailWDO.setFlagAttachments(flagAttachments);
		mailWDO.setImportance(importance);
		mailWDO.setReceivedDate(rdate.substring(0,8));
		mailWDO.setReceivedTime(rdate.substring(9).replaceAll(":", ""));
		mailWDO.setSentDate(sdate.substring(0,8));
		mailWDO.setSentTime(sdate.substring(9).replaceAll(":", ""));
		
		PayloadWDO[] payload = new PayloadWDO[1];
		payload[0] = new PayloadWDO();
		payload[0].setName("MessageID");
		payload[0].setValue(contentVO.getMessageId());
		mailWDO.setPayload(payload);
		return mailWDO;
	}
	
	public static ContactWDO[] parseEmailToContactWDOs(EmailAddressVO[] emails){
		ContactWDO[] contactWDOs = null;
		if(emails != null){
			int size = emails.length;
			
			if(size < 0){
				size = 0;
			}
			
			contactWDOs = new ContactWDO[size];			
			for (int i = 0; i < size; i++) {
				contactWDOs[i] = parseMailBriefWDO(emails[i]);
			}
		}	
		return contactWDOs;
	}

	public static ContactWDO parseMailBriefWDO(EmailAddressVO emailAddressVO) {
		ContactWDO contactWDO = new ContactWDO();
		contactWDO.setMailAddress(emailAddressVO.getAddress());
		contactWDO.setUserName(emailAddressVO.getPersonal());
		return contactWDO;
	}
	
	public static AttachmentInfoWDO[] parseAttachmentInfoWDOs(AttachFileVO[] attaches){
		AttachmentInfoWDO[] attachmentInfoWDO = null;
		if(attaches != null){
			int size = attaches.length;

			if(size < 0){
				size = 0;
			}

			attachmentInfoWDO = new AttachmentInfoWDO[size];			
			for (int i = 0; i < size; i++) {
				attachmentInfoWDO[i] = parseAttachmentInfoWDO(attaches[i]);
			}
		}	
		return attachmentInfoWDO;
	}

	public static AttachmentInfoWDO parseAttachmentInfoWDO(
			AttachFileVO attachFileVO) {
		
		AttachmentInfoWDO attachmentInfoWDO = new AttachmentInfoWDO();
		String name = attachFileVO.getName();
		String format = "unknown";
		format = (name.lastIndexOf(".") > -1)?name.substring(name.lastIndexOf(".") + 1):format;
		attachmentInfoWDO.setName(name);
		attachmentInfoWDO.setID(attachFileVO.getDepth());
		attachmentInfoWDO.setSize(attachFileVO.getSize());
		attachmentInfoWDO.setFormat(format);
		
		return attachmentInfoWDO;
	}
	
	public static AttachmentWDO[] parseAttachmentWDOs(AttachFileVO[] attaches){
		AttachmentWDO[] attachmentInfoWDO = null;
		if(attaches != null){
			int size = attaches.length;

			if(size < 0){
				size = 0;
			}

			attachmentInfoWDO = new AttachmentWDO[size];			
			for (int i = 0; i < size; i++) {
				attachmentInfoWDO[i] = parseAttachmentWDO(attaches[i]);
			}
		}	
		return attachmentInfoWDO;
	}

	public static AttachmentWDO parseAttachmentWDO(
			AttachFileVO attachFileVO) {
		
		AttachmentWDO attachmentWDO = new AttachmentWDO();
		String name = attachFileVO.getName();
		String format = "unknown";
		format = (name.lastIndexOf(".") > -1)?name.substring(name.lastIndexOf(".") + 1):format;
		attachmentWDO.setName(name);
		attachmentWDO.setID(attachFileVO.getDepth());
		attachmentWDO.setSize(attachFileVO.getSize());
		attachmentWDO.setFormat(format);
		attachmentWDO.setData(attachFileVO.getFiledata());
		attachmentWDO.setEncrypted(false);
		
		return attachmentWDO;
	}
	
	
	
}
