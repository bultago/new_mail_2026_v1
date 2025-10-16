/**
 * SMTPSendManager.java 2008. 12. 1.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.send;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.sun.mail.iap.ProtocolException;
import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailSendResultBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.util.Validation;

/**
 * <p>
 * <strong>SMTPSendManager.java</strong> Class Description
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
public abstract class SendHandler {
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	protected int totalAddressCnt = 0;
	protected boolean isFirstSeq = false;
	protected Session session;
	protected SenderInfoBean info;
	protected MailSendResultBean sendResult;
	protected MimeMessage sourceMime = null; 
	
	protected String errorMessage = null; 
	protected InternetAddress[] validAddress = null;
	protected InternetAddress[] inValidAddress = null;
	protected List<InternetAddress> validAddrList = new ArrayList<InternetAddress>();
	protected List<InternetAddress> inValidAddrList = new ArrayList<InternetAddress>();
	
	protected TMailStore store = null;
	public SendHandler(Session session, SenderInfoBean info, MailSendResultBean sendResult,TMailStore store) {
		this.session = session;
		this.info = info;
		this.sendResult = sendResult;
		this.store = store;
	}
	
	public abstract MailSendResultBean sendMailMessage(MimeMessage msg, TMailFolder folder);	
	
	public void setMDNFlag(InternetAddress[] mdnAddrs, TMailFolder folder) 
	throws MessagingException, UnsupportedEncodingException{		

		String mid = info.getMessageId();
		folder.xaddMDN(mid);

		String personal = null;
		String address = null;	
		
		folder.xsetMDNOpen();		
		for (int i = 0; i < mdnAddrs.length; i++) {			
			// folder2.xsetMDN(mid, ias_rcptto[x].getAddress(), 0);
			address = mdnAddrs[i].getAddress();
			personal = mdnAddrs[i].getPersonal();
			personal = (personal != null) ? 
			MimeUtility.encodeText(personal, info.getCharset(), "B") : "";
			personal = StringUtils.getCRLFEscapeOnly(personal);
			try {
				folder.xsetMDNAddCode(mid, address, personal, 0, "300");
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
			}
			
		}		
		folder.xsetMDNClose();		
		personal = null;
		address = null;		
	}
	
	public void delMDNFlag(TMailFolder folder) throws MessagingException{
		folder.xdelMDN(info.getMessageId());				
	}
	
	
	public String getMDNStringBack(InternetAddress mdnAddr, String type) throws Exception{
		
		Security.addProvider(new BouncyCastleProvider());
		StringBuffer sb = new StringBuffer();
		StringBuffer encodeBuffer = new StringBuffer();
		sb.append("\n<div id='TMSMDN' style=\"background:url('");
		
		sb.append(info.getMdnUrl());
		
		encodeBuffer.append("from=");
		encodeBuffer.append(info.getUserEmail());
		encodeBuffer.append("&mid=");
		encodeBuffer.append(URLEncoder.encode(info.getMessageId()));
		encodeBuffer.append("&to=");
		encodeBuffer.append(mdnAddr.getAddress());
		encodeBuffer.append("&type=");
		encodeBuffer.append(type);
		
		sb.append(URLEncoder.encode(SecureUtil.encrypt(SymmetricCrypt.AES, EnvConstants.ENCRYPT_KEY, encodeBuffer.toString())));		
		
		sb.append("')\"></div>");
		 
		return  sb.toString();
	}
	
	// TCUSTOM-2179 mdn html 테그 방식 수정 - nate에서 수신확인 되지 않는 현상
	public String getMDNString(InternetAddress mdnAddr, String type) throws Exception{
		
		Security.addProvider(new BouncyCastleProvider());
		StringBuffer sb = new StringBuffer();
		StringBuffer encodeBuffer = new StringBuffer();
//		sb.append("\n<div id='TMSMDN' style=\"background:url('");
		sb.append("\n");
		String mdnPrefix = EnvConstants.getMailSetting("mdn.tag.prefix") == null 
				? "<div id='TMSMDN' style=\"background:url('" : EnvConstants.getMailSetting("mdn.tag.prefix");
		sb.append(mdnPrefix);		
		
		sb.append(info.getMdnUrl());
		
		encodeBuffer.append("from=");
		encodeBuffer.append(info.getUserEmail());
		encodeBuffer.append("&mid=");
		encodeBuffer.append(URLEncoder.encode(info.getMessageId()));
		encodeBuffer.append("&to=");
		encodeBuffer.append(mdnAddr.getAddress());
		encodeBuffer.append("&type=");
		encodeBuffer.append(type);
		
		sb.append(URLEncoder.encode(SecureUtil.encrypt(SymmetricCrypt.AES, EnvConstants.ENCRYPT_KEY, encodeBuffer.toString())));		
		
		//sb.append("')\"></div>");
		String mdnPostfix = EnvConstants.getMailSetting("mdn.tag.postfix") == null 
				? "')\" />" : EnvConstants.getMailSetting("mdn.tag.postfix");
		sb.append(mdnPostfix);
		//sb.append("')\" />");		 
		//System.out.println("### getMDNString :"+sb.toString());
		 
		return  sb.toString();
	}
	
	public void setSendResult(){
		if(!sendResult.isErrorOccur()){ 
			if (inValidAddrList.size() > 0 && 
					totalAddressCnt != inValidAddrList.size()){
				Map<String, String> map = new HashMap<String, String>();
				for (InternetAddress address : inValidAddrList) {
					map.put(address.getAddress(), address.getAddress());
				}
				sendResult.setInvalidAddrMap(map);
				sendResult.setErrorOccur(false);
			} else if(inValidAddrList.size() > 0 && 
					totalAddressCnt == inValidAddrList.size()){
				sendResult.setErrorOccur(true);
			}
		}
		
		
		if(validAddrList.size() > 0){
			validAddress = new InternetAddress[validAddrList.size()];
			validAddrList.toArray(validAddress);
		}
		
		if(validAddress != null){
			sendResult.setSendAddressList(validAddress);
			sendResult.setSendAddress(TMailAddress.getAddressString(validAddress));
		}
		
		if(inValidAddrList.size() > 0){
			inValidAddress = new InternetAddress[inValidAddrList.size()];
			inValidAddrList.toArray(inValidAddress);
		}
		
		if(inValidAddress != null){
			sendResult.setInvalidAddressList(inValidAddress);
			sendResult.setInvalidAddress(TMailAddress.getAddressString(inValidAddress));		
		}				
		
		
		if (!Validation.isNull(errorMessage)) {			
			errorMessage = errorMessage.trim();
			int e = errorMessage.indexOf(";");
			errorMessage = errorMessage.substring(0, 
				(e > 0) ? e : errorMessage.length());
			sendResult.setErrorMessage(errorMessage);
		}
		
	}
	
	
	protected void send(MimeMessage[] messages, InternetAddress[] addresses) {
		if(!isFirstSeq){
			this.totalAddressCnt = addresses.length;
			isFirstSeq = true;
		}
        for (int i = 0; i < messages.length; i++) {
            try {
            	//System.out.println("====================[("+info.getUserEmail()+")MTA SEND SEQ("+i+") START:"+new Date()+"]=========================");
                sendMessage(messages[i], addresses[i]);
                validAddrList.add(addresses[i]);
               // System.out.println("====================[("+info.getUserEmail()+")MTA SEND SEQ("+i+") END:"+new Date()+"]=========================");
            } catch(SendFailedException e) {
            	inValidAddrList.add(addresses[i]);            					
            } catch(MessagingException e) { 
            	errorMessage = e.getMessage();
            	sendResult.setErrorOccur(true);
			}
        }
    }
	
	protected void sendOne(MimeMessage messages, InternetAddress[] addresses, int addressCnt) {
		if(!isFirstSeq){
			this.totalAddressCnt = addressCnt;
			isFirstSeq = true;
		}
            try {
            	
                sendMessageOneSend(messages, addresses);
                validAddrList.add(addresses[0]);            
            } catch(SendFailedException e) {
            	e.printStackTrace();
            	inValidAddrList.add(addresses[0]);            					
            } catch(MessagingException e) { 
            	errorMessage = e.getMessage();
            	sendResult.setErrorOccur(true);
			}
     
    }

	protected void send(MimeMessage message, InternetAddress[] addresses) {
		if(!isFirstSeq){
			this.totalAddressCnt = addresses.length;
			isFirstSeq = true;
		}
        try {
			sendMessage(message, addresses);
			
			for (InternetAddress inetAddress : addresses) {
				validAddrList.add(inetAddress);
			}		
        } catch(SendFailedException e) {
			Address[] invalid		= e.getInvalidAddresses();
			Address[] validUnsent 	= e.getValidUnsentAddresses();
			errorMessage = e.getMessage();

			for (int i = 0; i < invalid.length; i++) {
				inValidAddrList.add( (InternetAddress)invalid[i] );
			}

			if (!Validation.isNull(validUnsent)) {
				InternetAddress[] ias = new InternetAddress[validUnsent.length];

				for (int i = 0; i < validUnsent.length; i++) {
					ias[i] = (InternetAddress)validUnsent[i];
				}

				send(message, ias);
			}			
        } catch(MessagingException e) {
        	errorMessage = e.getMessage();
        	sendResult.setErrorOccur(true);
		}
    }
	
	protected void sendMessageOneSend(MimeMessage message,
			InternetAddress[] addresses) throws MessagingException, SendFailedException {

		Transport transport = session.getTransport();

		transport.connect();		
		transport.sendMessage(message, addresses);
		transport.close();
	}

	protected void sendMessage(MimeMessage message, InternetAddress address)
			throws MessagingException, SendFailedException {

		sendMessage(message, new InternetAddress[] { address });
	}

	protected void sendMessage(MimeMessage message, InternetAddress[] addresses)
			throws MessagingException, SendFailedException {

		sendMessage(new MimeMessage[] { message }, addresses);
	}

	protected void sendMessage(MimeMessage[] messages, InternetAddress[] addresses)
			throws MessagingException, SendFailedException {

		sendMessage(messages, new InternetAddress[][] { addresses });
	}

	protected void sendMessage(MimeMessage[] messages,
			InternetAddress[][] addresses) throws MessagingException, SendFailedException {

		Transport transport = session.getTransport();

		transport.connect();

		for (int i = 0; i < messages.length; i++) {
			transport.sendMessage(messages[i], addresses[i]);
		}

		transport.close();
	}
	
	public MimeMessage getSourceMimeMessage() {
		return this.sourceMime;
	}
	
	
	public static void simpleSendMessage(Session session, MimeMessage[] messages,
			InternetAddress[][] addresses)throws MessagingException, SendFailedException {
		
		Transport transport = session.getTransport();
		transport.connect();
		for (int i = 0; i < messages.length; i++) {
			transport.sendMessage(messages[i], addresses[i]);
		}

		transport.close();		
	}
	
	public static void simpleSendMessage(Session session, MimeMessage[] messages, String server,
			String port,
			InternetAddress[][] addresses)throws MessagingException, SendFailedException {
		
		Transport transport = session.getTransport();
		transport.connect(server, Integer.parseInt(port), null,null);
		for (int i = 0; i < messages.length; i++) {
			transport.sendMessage(messages[i], addresses[i]);
		}

		transport.close();		
	}
	
	public static void simpleSendMessage(Session session, TMailMessage[] messages,
			String server,
			String port,
			InternetAddress[] addresses)throws MessagingException, SendFailedException {
		if(messages != null && addresses != null){			
			Transport transport = session.getTransport();			
			transport.connect(server, Integer.parseInt(port), null,null);			
			for (int i = 0; i < messages.length; i++) {
				transport.sendMessage(messages[i].message, addresses);		
			}			
		}				
	}
	
	public long getMessageSize(MimeMessage message) {
	    if (message == null) {
	        return 0;
	    }
	    long messageSize = 0;
	    ByteArrayOutputStream bos = null;
	    try {
	        bos = new ByteArrayOutputStream();
	        message.writeTo(bos);
	        messageSize = bos.size();
	    }catch (Exception e) {}
	    finally {
	        try {
	            if (bos != null) bos.close();
	            bos = null;
	        } catch (IOException e) {}
	    } 
	    return messageSize;
	}
}
