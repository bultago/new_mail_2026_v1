/**
 * BatchSendHandler.java 2008. 12. 2.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.send;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.LadminManager;
import com.terracetech.tims.webmail.mail.ibean.MailSendResultBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.builder.TempMimeMessageBuilder;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;
/**
 * <p>
 * <strong>BatchSendHandler.java</strong> Class Description
 * </p>
 * <p>
 * �ֿ伳��
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
public class BatchSendHandler extends SendHandler{

	/**
	 * <p></p>
	 *
	 * @param session
	 * @param info
	 * @param sendResult
	 */
	public BatchSendHandler(Session session, SenderInfoBean info,
			MailSendResultBean sendResult, TMailStore store) {
		super(session, info, sendResult, store);
	}

	/**
	 * <p></p>
	 *
	 * @see com.terracetech.tims.webmail.mail.manager.send.SendHandler#sendMailMessage(javax.mail.internet.MimeMessage, java.lang.String)
	 * @param msg
	 * @param content
	 * @return 
	 */
	@Override
	public MailSendResultBean sendMailMessage(MimeMessage msg, TMailFolder folder) {
		
		try {			
			InternetAddress[] rcptto = info.getIasRcptto();
			String tempPath = EnvConstants.getBasicSetting("tmpdir");
			
			if(info.isRecvNoti()){
				info.setSaveSent(true);				
				info.setMdnStr(getMDNString(rcptto[0], "mail"));
                String mdnString = getMDNString(rcptto[0], "mail");
                mdnString = mdnString.replace("mdnData=", "batchmdnData=");
                info.setMdnStr(mdnString);
                
			}	
			String msgId = info.getMessageId();
			TempMimeMessageBuilder builder = new TempMimeMessageBuilder();
			MimeMessage tmpMime = new MimeMessage(session);
			builder.setFrom(tmpMime, info);
			if(info.isOneSend()){
				builder.setRecipient(tmpMime,rcptto[0]);
			} else {
				builder.setRecipient(tmpMime, info);
			}			
			builder.setFlaged(tmpMime, info);					
			builder.setSendDate(tmpMime, info.getSendDate());					
			builder.setMessageHeader(tmpMime, info);
			builder.setSubject(tmpMime, info.getSubject(), info.getCharset());
			builder.setMessageContent(tmpMime, info);
			tmpMime.saveChanges();			
			tmpMime.setHeader("Message-ID", "<"+msgId+">");			
			
			File mimefile = getMimeMessageFileName(new TMailMessage(tmpMime),tempPath);			
			File rcptsfile = getRcptsFileName(rcptto,tempPath);
			String tmpPath = EnvConstants.getBasicSetting("tmpdir");
				
			String mimeFilePath = tmpPath + "/" + mimefile.getName();
			String rcptsFilePath = tmpPath + "/" + rcptsfile.getName();			
			
			LadminManager ladminManager = info.getLadminManager();
			
			String args = " -s " + EnvConstants.getMailSetting("smtp.serv")
				+ " -p " + EnvConstants.getMailSetting("smtp.port")
				+ " -f " + info.getUser().get(User.EMAIL) 
				+ " -r " + rcptsFilePath
				+ " -m " + mimeFilePath
				+ " -d " + info.getUser().get(User.MAIL_DOMAIN) 
				//+ " -u " + store
				+ " -R " + (info.isRecvNoti()?"on":"off") //����Ȯ��
				+ " -P " + (info.isOneSend()?"on":"off")
				+ " -i " + msgId;		
			
			boolean isSuccess = ladminManager.batchSmtpExecute(rcptsfile, rcptsFilePath, mimefile, mimeFilePath, args);			
			if(!isSuccess){
				sendResult.setErrorOccur(false);
				errorMessage = "BATCH SMTP ERROR";
			}
			
			sendResult.setSendAddress(rcptto[0].getAddress());
			sendResult.setMailSize(mimefile.length());
			sendResult.setSendResultType("batch");
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			errorMessage = e.getMessage();
			sendResult.setErrorOccur(true);
		}		
		
		setSendResult();
		if(sendResult.isErrorOccur() && info.isRecvNoti()){			
			info.setSaveSent(false);			
		}
		
		sourceMime = msg;
		return sendResult;	
		
	}
	
	public File getRcptsFileName(InternetAddress[] ias, String path) {		
        SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
        String filePath = path + System.getProperty("file.separator") + 
        					sdf.format(new Date()) + "_" + info.getUser().get(User.MAIL_UID) +".rcpts";
        File tmpFile = new File(filePath);
		try {
			FileOutputStream fos = new FileOutputStream(tmpFile, true);
			PrintStream ps = new PrintStream(fos);
			String personal = null;
			for(int i = 0; i < ias.length; i++) {
				personal = ias[i].getPersonal();
				personal = (personal != null) ? 
				MimeUtility.encodeText("\""+personal+"\"", info.getCharset(), "B") : "";
				personal = StringUtils.getCRLFEscapeOnly(personal);				
				ps.println(personal+" <"+ias[i].getAddress()+">");
				personal = null;
			}

			fos.close();
		} catch (IOException ie) {
		}		
		
		return tmpFile;
	}
	
	/*
	public File getMimeMessageFileName(TMailMessage message, String path) {
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
        String filePath = path + System.getProperty("file.separator") + 
        					sdf.format(new Date()) + "_" + info.getUser().get(User.MAIL_UID) +".mime";
        File tmpFile = new File(filePath);
		try {
			OutputStream os = new FileOutputStream(tmpFile);

			message.writeTo(os);
		} catch (MessagingException e) {
		} catch (IOException ie) {
		}		
		
		return tmpFile;
	}
	*/
	
	// TCUSTOM-2260 20161030
	public File getMimeMessageFileName(TMailMessage message, String path) {
		  
		  SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
		        String filePath = path + System.getProperty("file.separator") + 
		             sdf.format(new Date()) + "_" + info.getUser().get(User.MAIL_UID) +".mime";
		        File tmpFile = new File(filePath);
		  
		  FileOutputStream fos = null;
		  Enumeration enumer = null;
		  InputStream in = null;
		  try {

		   fos = new FileOutputStream(tmpFile,true);
		   enumer = message.getAllHeaderLines();
		   String header = null;
		   while (enumer.hasMoreElements()) {
	             header = (String) enumer.nextElement();

	             fos.write(header.getBytes());
	             fos.write('\n');
	         }

	         fos.write('\n');

	         in = message.getInputStream();
	   
	         byte[] buffer = new byte[1024 * 1024];
	         int n;

	         while ((n = in.read(buffer, 0, buffer.length)) != -1) {
	          fos.write(buffer, 0, n);
	         }
	         fos.flush();
	         
		 } catch (MessagingException e) {
			  e.printStackTrace();
		 } catch (IOException ie) {
			  ie.printStackTrace();
		 }finally{
			if(fos != null){ try{ fos.close(); }catch(Exception e){
				LogManager.writeErr(this, e.getMessage());
			} }
			if(in != null){ try{ in.close(); }catch(Exception e){
				LogManager.writeErr(this, e.getMessage());
			} }
		 }
		 return tmpFile;
	}
	
}
