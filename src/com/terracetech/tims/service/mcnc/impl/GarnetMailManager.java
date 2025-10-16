package com.terracetech.tims.service.mcnc.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.terracetech.tims.service.manager.IMailServiceManager;
import com.terracetech.tims.service.mcnc.IMailService;
import com.terracetech.tims.service.mcnc.exception.GarnetException;
import com.terracetech.tims.service.mcnc.vo.AttachmentInfoWDO;
import com.terracetech.tims.service.mcnc.vo.AttachmentWDO;
import com.terracetech.tims.service.mcnc.vo.AttributeWDO;
import com.terracetech.tims.service.mcnc.vo.BodyWDO;
import com.terracetech.tims.service.mcnc.vo.ContactWDO;
import com.terracetech.tims.service.mcnc.vo.MailBoxWDO;
import com.terracetech.tims.service.mcnc.vo.MailBriefListWDO;
import com.terracetech.tims.service.mcnc.vo.MailWDO;
import com.terracetech.tims.service.mcnc.vo.OptionWDO;
import com.terracetech.tims.service.mcnc.vo.UserAuthWDO;
import com.terracetech.tims.service.tms.vo.AttachFileVO;
import com.terracetech.tims.service.tms.vo.FolderCondVO;
import com.terracetech.tims.service.tms.vo.FolderContentVO;
import com.terracetech.tims.service.tms.vo.FolderInfoVO;
import com.terracetech.tims.service.tms.vo.ListCondVO;
import com.terracetech.tims.service.tms.vo.ListInfoVO;
import com.terracetech.tims.service.tms.vo.MailWorkCondVO;
import com.terracetech.tims.service.tms.vo.MailWorkResultVO;
import com.terracetech.tims.service.tms.vo.ReadCondVO;
import com.terracetech.tims.service.tms.vo.SendCondVO;
import com.terracetech.tims.service.tms.vo.SendResultVO;
import com.terracetech.tims.service.tms.vo.ViewContentVO;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.setting.manager.SettingManager;

public class GarnetMailManager {
	private IMailServiceManager mailServiceManager = null;	
	
	private SettingManager settingManager = null;	
	
	public void setMailServiceManager(IMailServiceManager mailServiceManager) {
		this.mailServiceManager = mailServiceManager;
	}
	
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
	
	public MailBoxWDO[] getMailBoxes(UserAuthWDO userAuth, String companyID,
			AttributeWDO[] attribute) throws GarnetException {
		MailBoxWDO[] boxList = null;
		try{			
			FolderCondVO folderCond = new FolderCondVO();
			folderCond.setLocale("ko");
			folderCond.setUserEmail(userAuth.getEmail());
			folderCond.setRemoteIp(userAuth.getRemoteIp());
			FolderInfoVO folderInfo = mailServiceManager.doSimpleMailFolder(folderCond);
			MailBoxWDO[] defaultFolderEls = GarnetBeanParser.parseMailBoxWDOs(folderInfo.getDefaultFolders(),userAuth.getID());
			MailBoxWDO[] userFolderEls = GarnetBeanParser.parseMailBoxWDOs(folderInfo.getUserFolders(),userAuth.getID());
			List<MailBoxWDO> boxArray = new ArrayList<MailBoxWDO>(Arrays.asList(defaultFolderEls));			
			if(userFolderEls != null){
				boxArray.addAll(Arrays.asList(userFolderEls));
			}
			boxList = new MailBoxWDO[boxArray.size()];			
			boxArray.toArray(boxList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GarnetException("001", e.getMessage());
		}
		
		return boxList;
	}
	
	public MailBriefListWDO getMailList(UserAuthWDO userAuth,
			MailBoxWDO mailBox, String subFolder, AttributeWDO[] attributes)
			throws GarnetException {
		
		MailBriefListWDO mailBriefListWDO = null;
		try{
			ListCondVO listVO = new ListCondVO();
			listVO.setLocale("ko");
			listVO.setEmail(userAuth.getEmail());
			listVO.setRemoteIp(userAuth.getRemoteIp());
			listVO.setFolderName(mailBox.getBoxPath());
			listVO.setPageBase(15);
			listVO.setPage(1);
			if(attributes != null){
				String startDate = null;
				String endDate = null;
				for (int i = 0; i < attributes.length; i++) {
					if("maxNum".equals(attributes[i].getName())){
						listVO.setPageBase(Integer.parseInt(attributes[i].getValue()));
					} else if("nextPageNo".equals(attributes[i].getName())){
						listVO.setPage(Integer.parseInt(attributes[i].getValue()));
					} else if("startDate".equals(attributes[i].getName())){
						startDate = attributes[i].getValue();
					} else if("endDate".equals(attributes[i].getName())){
						endDate = attributes[i].getValue();
					}				
				}
				if(startDate != null && endDate != null){
					listVO.setAdvancedSearch(true);
					listVO.setFromDate(startDate);
					listVO.setToDate(endDate);
				}
			}
			
			mailBriefListWDO = GarnetBeanParser.parseMailBriefListWDO(mailServiceManager.doSimpleMailList(listVO));
		}catch (Exception e) {
			e.printStackTrace();
			throw new GarnetException("002", e.getMessage());
		}
		
		return mailBriefListWDO;
	}

	public MailWDO getMail(UserAuthWDO userAuth, MailBoxWDO mailBox,
			String subFolder, String mailID,
			HttpServletRequest request) throws GarnetException {
		MailWDO mailWDO = null;
		try{
			String attachesDir = request.getSession().getServletContext().getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
			String hostStr = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
			
			ReadCondVO readVO = new ReadCondVO();
			readVO.setEmail(userAuth.getEmail());
			readVO.setRemoteIp(userAuth.getRemoteIp());
			readVO.setFolder(mailBox.getBoxPath());
			readVO.setLocale("ko");
			readVO.setUid(Long.parseLong(mailID));
			readVO.setAttachDir(attachesDir);
			readVO.setLocalURL(hostStr);
			
			mailWDO = GarnetBeanParser.parseMailWDO(mailServiceManager.doSimpleMailRead(readVO));
		} catch (Exception e) {
			e.printStackTrace();
			throw new GarnetException("003", e.getMessage());
		}
		return mailWDO;
	}
	
	public AttachmentWDO getAttachment(UserAuthWDO userAuth,
			MailBoxWDO mailBox, String subFolder, String mailID,
			String attachmentID,
			HttpServletRequest request) throws GarnetException {
		AttachmentWDO attachmentWDO = null;
		try{
			String attachesDir = request.getSession().getServletContext().getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
			String hostStr = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
			
			ReadCondVO readVO = new ReadCondVO();
			readVO.setEmail(userAuth.getEmail());
			readVO.setRemoteIp(userAuth.getRemoteIp());
			readVO.setFolder(mailBox.getBoxPath());
			readVO.setLocale("ko");
			readVO.setUid(Long.parseLong(mailID));
			readVO.setAttachPath(attachmentID);
			readVO.setAttachDir(attachesDir);
			readVO.setLocalURL(hostStr);
			
			ViewContentVO mailContents = mailServiceManager.doSimpleMailRead(readVO);
			if(mailContents.getAttachsCnt() > 0){
				attachmentWDO = GarnetBeanParser.parseAttachmentWDO(mailServiceManager.doSimpleMailDownLoadAttach(readVO));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new GarnetException("003-1", e.getMessage());
		}
		return attachmentWDO;
	}

	public AttachmentInfoWDO[] getAttachmentList(UserAuthWDO userAuth,
			MailBoxWDO mailBox, String subFolder, String mailID,
			HttpServletRequest request)
			throws GarnetException {
		AttachmentInfoWDO[] attachmentInfoWDOs = null;
		try{
			String attachesDir = request.getSession().getServletContext().getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
			String hostStr = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
			
			ReadCondVO readVO = new ReadCondVO();
			readVO.setEmail(userAuth.getEmail());
			readVO.setRemoteIp(userAuth.getRemoteIp());
			readVO.setFolder(mailBox.getBoxPath());
			readVO.setLocale("ko");
			readVO.setUid(Long.parseLong(mailID));
			readVO.setAttachDir(attachesDir);
			readVO.setLocalURL(hostStr);
			
			ViewContentVO mailContents = mailServiceManager.doSimpleMailRead(readVO);
			if(mailContents.getAttachsCnt() > 0){
				attachmentInfoWDOs = GarnetBeanParser.parseAttachmentInfoWDOs(mailContents.getAttachs());
			} else {
				attachmentInfoWDOs = new AttachmentInfoWDO[]{new AttachmentInfoWDO()};
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new GarnetException("003-2", e.getMessage());
		}
		return attachmentInfoWDOs;
	}

	

	public String[] getSubFolders(UserAuthWDO userAuth, MailBoxWDO mailBox)
			throws GarnetException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean copyMail(UserAuthWDO userAuth, MailBoxWDO srcMailBox,
			String srcSubFolder, String srcMailID, MailBoxWDO dstMailBox,
			String dstsubFolder) throws GarnetException {
		
		boolean isSuccess = false;
		try{
			MailWorkCondVO workVO = new MailWorkCondVO();
			workVO.setUserEmail(userAuth.getEmail());
			workVO.setRemoteIp(userAuth.getRemoteIp());
			workVO.setLocale("ko");
			workVO.setWorkMode("copy");
			workVO.setFolderName(new String[]{srcMailBox.getBoxPath()});
			workVO.setTargetFolderName(dstMailBox.getBoxPath());
			workVO.setUid(new String[]{srcMailID});
			
			MailWorkResultVO resultVO = mailServiceManager.doSimpleMailWork(workVO);
			isSuccess = (resultVO.isErrorOccur())?false:true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GarnetException("004", e.getMessage());
		}
		return isSuccess;
	}

	public boolean moveMail(UserAuthWDO userAuth, MailBoxWDO srcMailBox,
			String srcSubFolder, String srcMailID, MailBoxWDO dstMailBox,
			String dstSubFolder) throws GarnetException {

		boolean isSuccess = false;
		try{
			MailWorkCondVO workVO = new MailWorkCondVO();
			workVO.setUserEmail(userAuth.getEmail());
			workVO.setRemoteIp(userAuth.getRemoteIp());
			workVO.setLocale("ko");
			workVO.setWorkMode("move");
			workVO.setFolderName(new String[]{srcMailBox.getBoxPath()});
			workVO.setTargetFolderName(dstMailBox.getBoxPath());
			workVO.setUid(new String[]{srcMailID});
			
			MailWorkResultVO resultVO = mailServiceManager.doSimpleMailWork(workVO);
			isSuccess = (resultVO.isErrorOccur())?false:true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GarnetException("005", e.getMessage());
		}
		return isSuccess;
	}

	public boolean removeMail(UserAuthWDO userAuth, MailBoxWDO mailBox,
			String subFolder, String mailID) throws GarnetException {
		boolean isSuccess = false;
		try{
			MailWorkCondVO workVO = new MailWorkCondVO();
			workVO.setUserEmail(userAuth.getEmail());
			workVO.setRemoteIp(userAuth.getRemoteIp());
			workVO.setLocale("ko");
			workVO.setWorkMode("delete");
			workVO.setFolderName(new String[]{mailBox.getBoxPath()});
			workVO.setUid(new String[]{mailID});
			
			MailWorkResultVO resultVO = mailServiceManager.doSimpleMailWork(workVO);
			isSuccess = (resultVO.isErrorOccur())?false:true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GarnetException("006", e.getMessage());
		}
		return isSuccess;
	}

	public int getUnreadMailCount(UserAuthWDO userAuth, MailBoxWDO mailBox,
			String subFolder, AttributeWDO[] attribute) throws GarnetException {
		int count = 0;
		boolean isTotal = false;
		try{			
			FolderCondVO folderCond = new FolderCondVO();
			folderCond.setLocale("ko");
			folderCond.setUserEmail(userAuth.getEmail());
			folderCond.setRemoteIp(userAuth.getRemoteIp());
			folderCond.setFolderName(mailBox.getBoxPath());
			
			FolderContentVO folderContentVO = mailServiceManager.doSimpleMailFolderContent(folderCond);
			
			isTotal = "total".equalsIgnoreCase(subFolder);
						
			if(isTotal){
				count = folderContentVO.getTotal();
			} else {
				count = folderContentVO.getUnseen();
			}			
		} catch (Exception e) {
			e.printStackTrace();
			throw new GarnetException("007", e.getMessage());
		}
		return count;
	}
	
	

	public boolean saveTempMail(UserAuthWDO userAuth, MailBoxWDO mailBox,
			MailWDO content, AttributeWDO[] attribute,
			HttpServletRequest request) throws GarnetException {
		
		boolean isSuccess = false;
		try{			
			String port = EnvConstants.getBasicSetting("web.port");		
			port = (port != null && port.length() > 0)?port:Integer.toString(request.getServerPort());
			String mdnHost = EnvConstants.getMailSetting("mdn.host");
			String localhost = request.getScheme() + "://" 
					+ request.getServerName() + ":" + port;
			
			mdnHost = (mdnHost != null)?mdnHost:localhost;
			mdnHost += EnvConstants.getMailSetting("mdn.action");
			
			SendCondVO sendVO = new SendCondVO();
			sendVO.setSenderEmail(userAuth.getEmail());
			sendVO.setRemoteIp(userAuth.getRemoteIp());
			sendVO.setLocale("ko");
			sendVO.setLocalhost(localhost);
			sendVO.setMdnHost(mdnHost);
			sendVO = makeMailContents(sendVO, attribute, content);			
			sendVO.setSendType("draft");
			
			SendResultVO resultVO = mailServiceManager.doSimpleMailSend(sendVO);
			
			isSuccess = (resultVO.isErrorOccur())?false:true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GarnetException("008", e.getMessage());
		}
		return isSuccess;
	}

	public boolean sendMail(UserAuthWDO userAuth, MailBoxWDO mailBox,
			String subFolder, MailWDO mail, String mailType,
			AttributeWDO[] attributes,
			OptionWDO[] option,
			HttpServletRequest request)
			throws GarnetException {
		boolean isSuccess = false;
		try{			
			String port = EnvConstants.getBasicSetting("web.port");		
			port = (port != null && port.length() > 0)?port:Integer.toString(request.getServerPort());
			String mdnHost = EnvConstants.getMailSetting("mdn.host");
			String localhost = request.getScheme() + "://" 
					+ request.getServerName() + ":" + port;
			
			mdnHost = (mdnHost != null)?mdnHost:localhost;
			mdnHost += EnvConstants.getMailSetting("mdn.action");
			
			SendCondVO sendVO = new SendCondVO();
			sendVO.setSenderEmail(userAuth.getEmail());
			sendVO.setRemoteIp(userAuth.getRemoteIp());
			sendVO.setLocale("ko");
			sendVO.setLocalhost(localhost);
			sendVO.setMdnHost(mdnHost);			
			sendVO.setFolder(mailBox.getBoxPath());
			sendVO = makeMailOption(sendVO, mailType, option);
			sendVO = makeMailContents(sendVO, attributes, mail);
			SendResultVO resultVO = mailServiceManager.doSimpleMailSend(sendVO);
			
			isSuccess = (resultVO.isErrorOccur())?false:true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GarnetException("008", e.getMessage());
		}
		return isSuccess;
	}
	
	private SendCondVO makeMailOption(SendCondVO sendVO, 
			String mailType,
			OptionWDO[] option){
		String sendType = "normal";
		if(mailType.equals("1")){
			sendType = "forward";
		} else if(mailType.equals("2")){
			sendType = "reply";
		}
		
		sendVO.setEncode("UTF-8");		
		sendVO.setReceivnoti(false);
		sendVO.setVcard(false);
		sendVO.setSavesent(false);
		sendVO.setOnesend(false);		
		if(option != null){
			boolean isCheck = false;
			for (int i = 0; i < option.length; i++) {
				if("ReservedDate".equalsIgnoreCase(option[i].getName())){
					sendType = "reserved";
					String dateStr = option[i].getValue();
					sendVO.setReservYear(dateStr.substring(0,4));
					sendVO.setReservMonth(dateStr.substring(4,2));
					sendVO.setReservDay(dateStr.substring(6,2));
					sendVO.setReservHour(dateStr.substring(8,2));
					sendVO.setReservMin(dateStr.substring(10,2));					
				}
				
				if("Importance".equalsIgnoreCase(option[i].getName())){
					int importance = Integer.parseInt(option[i].getValue());
					sendVO.setPriority((importance > 0));
				}
				
				if("Onesend".equalsIgnoreCase(option[i].getName())){
					sendVO.setOnesend("on".equalsIgnoreCase(option[i].getValue()));					
				}
				
				if("Savesent".equalsIgnoreCase(option[i].getName())){
					sendVO.setSavesent("on".equalsIgnoreCase(option[i].getValue()));
				}
				
				if("Receivnoti".equalsIgnoreCase(option[i].getName())){
					isCheck = "on".equalsIgnoreCase(option[i].getValue());
					sendVO.setReceivnoti(isCheck);
					if(isCheck){
						sendVO.setSavesent(true);
					}
				}
				
				if("Vcard".equalsIgnoreCase(option[i].getName())){
					sendVO.setVcard("on".equalsIgnoreCase(option[i].getValue()));
				}
				
			}
		}
		sendVO.setSendType(sendType);	
		
		
		return sendVO;
	}
	
	private SendCondVO makeMailContents(SendCondVO sendVO, 
			AttributeWDO[] attributes, MailWDO mail){
		
		BodyWDO body = mail.getBody();
		AttachmentInfoWDO[] attachmentInfos = mail.getAttachmentInfos();		
		
		sendVO.setSubject(mail.getSubject());
		sendVO.setEditMode("html");
		if(body.getBodyType() == 1){
			sendVO.setEditMode("text");
		}		
		sendVO.setContent(mail.getBody().getBody());
		
		sendVO.setToAddr(makeAddrStr(mail.getTo()));
		sendVO.setCcAddr(makeAddrStr(mail.getCc()));		
		sendVO.setBccAddr(makeAddrStr(mail.getBcc()));
		
		sendVO.setUid(mail.getMailID());
		if(attributes != null){
			for (int i = 0; i < attributes.length; i++) {
				if("MessageID".equalsIgnoreCase(attributes[i].getName())){
					sendVO.setDraftMid(attributes[i].getValue());
				}
			}
		}
		
		if(attachmentInfos != null){
			AttachFileVO[] attachVOs = new AttachFileVO[attachmentInfos.length];			
			for (int i = 0; i < attachVOs.length; i++) {
				attachVOs[i] = new AttachFileVO();
				attachVOs[i].setName(attachmentInfos[i].getName());
				attachVOs[i].setSize(attachmentInfos[i].getSize());				
				attachVOs[i].setDepth(attachmentInfos[i].getID());
			}
			
			sendVO.setAttachList(attachVOs);
		}
		
		return sendVO;
	}
	
	private String[] makeAddrStr(ContactWDO[] contacts){
		String[] addresses = null;
		if(contacts != null && contacts.length > 0){
			int size = contacts.length;
			
			if(size < 0){
				size = 0;
			}

			addresses = new String[size];			
			for (int i = 0; i < size; i++) {
				addresses[i] = getAddressStr(contacts[i]);					
			}
		}
		
		return addresses;
	}
	
	private String getAddressStr(ContactWDO contact){
		String address = null;
		String name = null;
		String email = null;
		if(contact != null){
			name = contact.getUserName();
			email = contact.getMailAddress();
			if(name != null && name.trim().length() > 0){
				address = "\"" + name +"\" <"+email+">";					
			} else {
				address = email;
			}
		}
		
		return address;
	}

}
