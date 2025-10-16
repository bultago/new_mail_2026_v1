/**
 * ReportSpamWhiteAction.java 2009. 4. 8.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.ladmin.Protocol;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.LadminManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mail.ibean.MailSendResultBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mail.manager.send.BatchSendHandler;
import com.terracetech.tims.webmail.mail.manager.send.SendHandler;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.PSpamRuleVO;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>ReportSpamWhiteAction.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
@SuppressWarnings("all")
public class ReportSpamWhiteAction extends BaseAction {

	private static final long serialVersionUID = -6434020194633109586L;
	private SettingManager settingManager = null;
	private MailManager mailManager = null;	
	private LadminManager ladminManager = null;
	private MailUserManager mailUserManager = null;
	private SystemConfigManager systemConfigManager = null;
	
	
	
	/**
	 * @param settingManager �Ķ���͸� settingManager���� ����
	 */
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
	
	public void setLadminManager(LadminManager ladminManager) {
		this.ladminManager = ladminManager;
	}

	/**
	 * @param mailManager �Ķ���͸� mailManager���� ����
	 */
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	public void setSystemConfigManager(SystemConfigManager systemConfigManager) {
		this.systemConfigManager = systemConfigManager;
	}
		
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}

	public String execute() throws Exception {		
		
		// Start time
		long classStartTime = System.currentTimeMillis();
		long classEndTime;
		
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		String ruleType = request.getParameter("rtype");
		String folderName = request.getParameter("folderName");
		String[] uids = request.getParameterValues("uids");
		String[] fromEmails = request.getParameterValues("femails");
		String addListFlag = request.getParameter("addlist");
		String moveFlag = request.getParameter("movebox");
		String adminRegist = request.getParameter("adminRegist");
		boolean isAddList = (addListFlag != null && addListFlag.equals("true"))?true:false;
		boolean isMove = (moveFlag != null && moveFlag.equals("true"))?true:false;
		boolean isBlack = (ruleType != null && ruleType.equals("black"))?true:false;
		boolean isRegistAdmin = (adminRegist != null && adminRegist.equals("regist"))?true:false;
		boolean isLocalBayesianUse = "on".equalsIgnoreCase(systemConfigManager.getBayesianOption());
		
		boolean checkNcsc = "true".equalsIgnoreCase(request.getParameter("reportNcsc"));
		boolean reportNcsc = useReportNcsc();
		
		JSONObject jObj = new JSONObject();
		TMailStore store = null;
		TMailStore admStore = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		String registUser = null;
		int registUserSeq = -1;		
		char registType = 0;
		try {
			store = factory.connect(request.getRemoteAddr(), user);
			if(isRegistAdmin){
				TMailFolder fromFolder, toFolder;
				fromFolder = store.getFolder(folderName);
				try {
					if(isLocalBayesianUse){
						
						/* areaTime1StartTime */
						long areaTime1StartTime = System.currentTimeMillis();
						
						registUser = (isBlack)?EnvConstants.getBasicSetting("spam.admin"):
											EnvConstants.getBasicSetting("white.admin");
						registUserSeq = mailUserManager.readUserSeq(registUser, domainSeq);
						
						if(registUserSeq > 0){
							admStore = factory.connect(true, Integer.toString(registUserSeq), remoteIp, user);					
							mailManager.setProcessResource(admStore, getMessageResource());			
							
							toFolder = admStore.getFolder(FolderHandler.INBOX);
							toFolder.open(true);
							mailManager.copySharedMessage(StringUtils.getStringsToLongs(uids), fromFolder, toFolder);
							toFolder.close(true);

							/* areaTime1EndTime */
							long areaTime1EndTime = System.currentTimeMillis();
							
							LogManager.writeInfo(this, "#####################################################################################################################################");
							LogManager.writeInfo(this, "### ReportSpamWhiteAction : AREA1 START TIME ::::::::: " + this.formatTime(areaTime1StartTime));
							LogManager.writeInfo(this, "######################### : AREA1 END TIME ::::::::::: "  + this.formatTime(areaTime1EndTime));
							LogManager.writeInfo(this, "######################### : AREA1 AREA TIME (0.0f) ::: "  + ( areaTime1StartTime - areaTime1EndTime )/1000.0f + "s");
							LogManager.writeInfo(this, "#####################################################################################################################################");
							
						}
					} else {
												
						/* areaTime2StartTime */
						long areaTime2StartTime = System.currentTimeMillis();
						

						registUser = (isBlack)?EnvConstants.getReportSetting("spam.report.address"):
							EnvConstants.getReportSetting("white.report.address");
						mailManager.setProcessResource(store, getMessageResource());
						
						fromFolder.open(false);
						TMailMessage[] tempMessage = mailManager.getMessage(fromFolder, StringUtils.getStringsToLongs(uids));
						
						/* areaTime2DBEndTime */
						long areaTime2DBEndTime = System.currentTimeMillis();
						
						Properties props = new Properties();
						props.setProperty("mail.debug", "true");
						props.setProperty("mail.transport.protocol", EnvConstants.getReportSetting("report.server.protocol"));					
						
						/* areaTime2DBEndTime */
						long areaTime2ReportEndTime = System.currentTimeMillis();
						
						Session session = Session.getDefaultInstance(props,null);										
						SendHandler.simpleSendMessage(session, tempMessage,
								EnvConstants.getReportSetting("report.server"),
								EnvConstants.getReportSetting("report.server.port"),							 
								TMailAddress.getInternetAddress(registUser));
						
						/* areaTime2EndTime */
						long areaTime2EndTime = System.currentTimeMillis();
						
						LogManager.writeInfo(this, "#####################################################################################################################################");
						LogManager.writeInfo(this, "### ReportSpamWhiteAction : AREA2 START TIME :::::::::::::::: " + this.formatTime(areaTime2StartTime));
						LogManager.writeInfo(this, "######################### : AREA2 DB AREA TIME (0.0f) ::::::: "  + ( areaTime2StartTime - areaTime2DBEndTime )/1000.0f + "s");
						LogManager.writeInfo(this, "######################### : AREA2 REPORT AREA TIME (0.0f) ::: "  + ( areaTime2StartTime - areaTime2ReportEndTime )/1000.0f + "s");
						LogManager.writeInfo(this, "######################### : AREA2 END TIME :::::::::::::::::: "  + this.formatTime(areaTime2EndTime));
						LogManager.writeInfo(this, "######################### : AREA2 AREA TIME (0.0f) :::::::::: "  + ( areaTime2StartTime - areaTime2EndTime )/1000.0f + "s");
						LogManager.writeInfo(this, "#####################################################################################################################################");
			
					}
										
					jObj.put("adminRegist", "success");
				} catch (Exception e) {
					e.printStackTrace();
					jObj.put("adminRegist", "error");
				} finally{
					if(fromFolder.isOpen()){
						fromFolder.close(false);
					}
				}
			}
			
			/* areaTime3StartTime */		
			long areaTime3StartTime = System.currentTimeMillis();
			
			if(isAddList){								
				PSpamRuleVO vo = settingManager.readSpamRule(userSeq);				
				if(vo == null){
					vo = new PSpamRuleVO();
				}				
				vo.setUserSeq(userSeq);
				
				if(isBlack){
					settingManager.saveBlackList(userSeq, fromEmails);
				} else {
					settingManager.saveWhiteList(userSeq, fromEmails);
				}								
			}
			
			try {
				if (checkNcsc && reportNcsc) {
					TMailFolder folder = store.getFolder(folderName);
					reportToNcsc(folder, uids);
					jObj.put("reportToNcsc", "true");
				}	
			} catch (Exception e) {
				jObj.put("reportToNcsc", "false");
				log.error("reportToNcsc error.", e);
			}

			if(isMove){				
				mailManager.setProcessResource(store, getMessageResource());				 
				if(isBlack){
					mailManager.moveMessage(StringUtils.getStringsToLongs(uids), folderName, FolderHandler.TRASH);					
				} else {
					mailManager.moveMessage(StringUtils.getStringsToLongs(uids), folderName, FolderHandler.INBOX);					
				}
			}
			
			jObj.put("result", "success");
			jObj.put("type", (isBlack)?"black":"white");
			
			
			/* areaTime3EndTime */
			long areaTime3EndTime = System.currentTimeMillis();
			
			LogManager.writeInfo(this, "#####################################################################################################################################");
			LogManager.writeInfo(this, "### ReportSpamWhiteAction : AREA3 START TIME ::::::::: " + this.formatTime(areaTime3StartTime));
			LogManager.writeInfo(this, "######################### : AREA3 END TIME ::::::::::: "  + this.formatTime(areaTime3EndTime));
			LogManager.writeInfo(this, "######################### : AREA3 AREA TIME (0.0f) ::: "  + ( areaTime3StartTime - areaTime3EndTime )/1000.0f + "s");
			LogManager.writeInfo(this, "#####################################################################################################################################");
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			jObj.put("result", "fail");
		}  finally {
			if(store !=null && store.isConnected())
				store.close();
			
			if(admStore !=null && admStore.isConnected())
				admStore.close();
			
		}
		
		response.addHeader("Cache-Control","no-store");
        response.addHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        out.println(jObj);
		
        
        // area2 end time
        classEndTime = System.currentTimeMillis();
        
        LogManager.writeInfo(this, "#####################################################################################################################################");
		LogManager.writeInfo(this, "### ReportSpamWhiteAction : CLASS START TIME ::::::::: " + this.formatTime(classStartTime));
		LogManager.writeInfo(this, "######################### : CLASS END TIME ::::::::::: "  + this.formatTime(classEndTime));
		LogManager.writeInfo(this, "######################### : CLASS AREA TIME (0.0f) ::: "  + ( classStartTime - classEndTime )/1000.0f + "s");
		LogManager.writeInfo(this, "#####################################################################################################################################");

		return null;
	}
	
	private void sendNcscMessage(MimeMessage message) throws AddressException, SendFailedException, MessagingException {
		Properties props = new Properties();
		props.setProperty("mail.debug", "true");
		props.setProperty("mail.transport.protocol", "smtp");
		Session session = Session.getDefaultInstance(props,null);	
		MimeMessage[] mimeMessages = new MimeMessage[1];
		mimeMessages[0] = message;
		SendHandler.simpleSendMessage(session, mimeMessages, EnvConstants.getMailSetting("smtp.serv"), EnvConstants.getMailSetting("smtp.port"), 
				new InternetAddress[][] {TMailAddress.getInternetAddress(getReportNcscAddress())});
	}

	private void reportToNcsc(TMailFolder folder, String[] uids) throws Exception {
		if (uids == null || uids.length == 0) {
			return;
		}
		I18nResources resource = getMessageResource();
		try {
			Properties props = new Properties();
            props.setProperty("mail.debug", "false");
            props.setProperty("mail.transport.protocol", "smtp");
            Session session = Session.getDefaultInstance(props, null);
			
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addHeader("Content-Transfer-Encoding", "base64");
            mimeMessage.setSubject(resource.getMessage("reportspam.ncsc.message.subject"), "utf-8");
            mimeMessage.setFrom(new InternetAddress(user.get(User.EMAIL), user.get(User.USER_NAME), "utf-8"));
            mimeMessage.setRecipients(Message.RecipientType.TO, TMailAddress.getInternetAddress(getReportNcscAddress()));
            mimeMessage.setSentDate(new Date());
            
            MimeMultipart multipart = new MimeMultipart();
            
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setContent(resource.getMessage("reportspam.ncsc.message.body"), "text/html; charset=utf-8");
            multipart.addBodyPart(mbp);
            
            long[] longUids = new long[uids.length];  
            for (int i = 0; i < uids.length; i++) {  
            	longUids[i] = Long.valueOf(uids[i]);  
            }
            
            folder.open(false);
            TMailMessage[] messages = folder.getMessagesByUID(longUids);
            
            addEmlAttachment(multipart, messages);            
            mimeMessage.setContent(multipart);
            mimeMessage.saveChanges();
            
            sendNcscMessage(mimeMessage);
		} catch (Exception e) {
			log .error("report to ncsc fail..", e);
			throw e;
		} finally {
			try {
				if (folder != null) {
					folder.close(false);
				}
			} catch (MessagingException e) {
				log.error("store close exception..", e);
			}
		}
		
	}

	private void addEmlAttachment(MimeMultipart multipart, TMailMessage[] messages) throws MessagingException, IOException {
		for (int i = 0; i < messages.length; i++) {
			String emlName = messages[i].getSubject();
            emlName = (emlName != null) ? emlName : "No Subject";	
    	    emlName = emlName.replaceAll("\\\\", "");
    	    emlName = emlName.replaceAll("[\t\n\r]", " ");
    	    emlName = emlName.replaceAll("[/:*?\"<>|]", "_");
    	    emlName = emlName+".eml";
    	        	    
    	    File tmpFile = FileUtil.makeTmpFile();
    	    
    	    FileOutputStream fos = new FileOutputStream(tmpFile,true);
    		Enumeration enumer = messages[i].getAllHeaderLines();

            while (enumer.hasMoreElements()) {
                String header = (String) enumer.nextElement();

                fos.write(header.getBytes());
                fos.write('\n');
            }

            fos.write('\n');

    		InputStream in = messages[i].getRawInputStream();
    		
            byte[] buffer = new byte[1024 * 1024];
            int n;

            while ((n = in.read(buffer, 0, buffer.length)) != -1) {
            	fos.write(buffer, 0, n);
            }
            
            fos.flush();
            fos.close();
            in.close();
            
            MimeBodyPart mbp = new MimeBodyPart();
        	
			FileDataSource fds = new FileDataSource(tmpFile.getPath());
			mbp.setDataHandler(new DataHandler(fds));
			
			String esc_name = MimeUtility.encodeText(emlName, "UTF-8", "B");

			mbp.setHeader("Content-Type", "message/rfc822");
			mbp.setFileName(esc_name);
			mbp.addHeader("Content-Transfer-Encoding", "base64");
			multipart.addBodyPart(mbp);		
		}	
	}

	public String ncscInfo() throws Exception {
		
		boolean reportNcsc = useReportNcsc();
		String reportNcscAddress = getReportNcscAddress();
		
		JSONObject result = new JSONObject();
		result.put("reportNcsc", reportNcsc);
		result.put("reportNcscAddress", reportNcscAddress);
		
		ResponseUtil.processResponse(response, result);
		
		return null;
	}
	
	private boolean useReportNcsc() {
		return "true".equalsIgnoreCase(EnvConstants.getReportSetting("report.spam.ncsc.use"));
	}
	
	private String getReportNcscAddress() {
		return EnvConstants.getReportSetting("report.spam.ncsc.address");
	}
	
	public String formatTime(long lTime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(lTime);
        return (c.get(Calendar.HOUR_OF_DAY) + "h " + c.get(Calendar.MINUTE) + "m " + c.get(Calendar.SECOND) + "." + c.get(Calendar.MILLISECOND) + "s");
    }
}
