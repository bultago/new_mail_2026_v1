/**
 * SendAction.java 2008. 12. 4.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
import com.terracetech.tims.webmail.common.manager.VirusManager;
import com.terracetech.tims.webmail.common.vo.VirusCheckVO;
import com.terracetech.tims.webmail.exception.VirusCheckException;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.mail.ibean.MailSecureInfoBean;
import com.terracetech.tims.webmail.mail.ibean.MailSendResultBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.Image2Cid;
import com.terracetech.tims.webmail.mail.manager.LetterManager;
import com.terracetech.tims.webmail.mail.manager.SendMessageDirector;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mail.manager.builder.DraftMimeMessageBuilder;
import com.terracetech.tims.webmail.mail.manager.builder.GeneralMimeMessageBuilder;
import com.terracetech.tims.webmail.mail.manager.builder.MimeMessageBuilder;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.manager.SearchEmailManager;
import com.terracetech.tims.webmail.setting.manager.LastrcptManager;
import com.terracetech.tims.webmail.setting.manager.SignManager;
import com.terracetech.tims.webmail.setting.manager.VCardManager;
import com.terracetech.tims.webmail.util.HangulEmail;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>SendAction.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author jpjung
 * @since Tims7
 * @version 7.0 
 */

@SuppressWarnings("unchecked")
public class SendMessageAction extends BaseAction {	
	
	private static final long serialVersionUID = 20081204L;
	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private LastrcptManager rcptManager = null;	
	private VCardManager vcardManager = null;
	private LetterManager letterManager = null;
	private SignManager signManager = null;
	private SearchEmailManager searchEmailManager = null;
	private LadminManager ladminManager = null;
	private SystemConfigManager systemConfigManager = null;
	private VirusManager virusManager = null;
	
	private MailSendResultBean sendResult = null;
	private MailUserManager mailUserManager = null;
		
	boolean flag = false;
	
	/**
	 * �̸��� �ּ��� ��ȿ���� �����ϴ� �޼ҵ� �̴�.
	 * 
	 * @since 2012. 5. 24.
	 * @author ��μ�
	 * @param emailAddress
	 * @return
	 */
	private static boolean isValidEmail(String emailAddress) {
	    Pattern pattern=Pattern.compile("^([\\w]((\\.(?!\\.))|[-!#\\$%'\\*\\+/=\\?\\^`\\{\\}\\|~\\w])*)(?<=[\\w])@(([\\w][-\\w]*[\\w]\\.)+[a-zA-Z]{2,6})$", Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(emailAddress);   
            return matcher.matches();   
	}   
	
	public String makeRcpt(String strRcptto) {
	    String massRcpt = "";
	    String strTo = "";
	    
	    flag = true;
            massRcpt = strRcptto;
            strRcptto = "";
            
            String[] emails = massRcpt.split(",");                          
            massRcpt = "";
            for (int i = 0; i < emails.length; i++) {
                try {
                    InternetAddress internetAddress = new InternetAddress(emails[i]);
                    if (isValidEmail(internetAddress.getAddress())) {
                        massRcpt += internetAddress.getAddress();
                        massRcpt += ",";
                    }
                } catch (AddressException e) {
                    e.printStackTrace();
                }
            }
            
            massRcpt = massRcpt.substring(0, massRcpt.length() - 1);
            strTo = (strTo != null && strTo.length() > 0 && isValidEmail(strTo))? strTo + ","+ massRcpt:massRcpt;
            strRcptto = (strRcptto.length() > 0)? strRcptto + ","+ massRcpt:massRcpt;
	    
            return strRcptto;
        }
	 
	public String execute() throws Exception {
		I18nResources msgResource = getMessageResource();
		I18nResources commonResource = getMessageResource("common");
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		String secureMailModule = EnvConstants.getUtilSetting("secure.module");
		
		int maxRcpt = 100;
		boolean allowMassMail = true;		
		
		Map batchSmtpConfig = systemConfigManager.getBatchSmtpConfig();
		if(batchSmtpConfig != null){ 
			if(batchSmtpConfig.containsKey("send_allowmassmail"))
				allowMassMail = "on".equals((String)batchSmtpConfig.get("send_allowmassmail"));
			if(batchSmtpConfig.containsKey("send_maxrcpts"))
				maxRcpt = Integer.parseInt((String)batchSmtpConfig.get("send_maxrcpts"));
		}
		
		String port = EnvConstants.getBasicSetting("web.port");
		port = systemConfigManager.readMdnPort();
		port = (port != null && port.length() > 0)?port:Integer.toString(request.getServerPort());
		String mdnHost = EnvConstants.getMailSetting("mdn.host");
		String localhost = request.getScheme() + "://" 
				+ request.getServerName() + ":" + port;
		
		String sslHost = "https://" 
			+ request.getServerName() + ":443";
		
		
		String remoteIP = request.getRemoteAddr();
		
		mdnHost = (mdnHost != null)?mdnHost:localhost;
		mdnHost += EnvConstants.getMailSetting("mdn.action");
		
		String domain = user.get(User.MAIL_DOMAIN);
		String strTo = null;
		String strCc = null;
		String strBcc = null;
		String strRcptto = null;
		
		try{
			strTo = getCheckEmailAddress(userSeq, domainSeq, request.getParameter("to"), domain);
			strCc = getCheckEmailAddress(userSeq, domainSeq, request.getParameter("cc"), domain);
			strBcc = getCheckEmailAddress(userSeq, domainSeq, request.getParameter("bcc"), domain);
		} catch(Exception e){
			LogManager.writeErr(this, "RCPT ADDRESS PARSE ERROR", e);
			throw new Exception("RCPT ADDRESS PARSE ERROR",e);
		}
		
		String massRcptMode = request.getParameter("massMode");
		String signSeq = request.getParameter("signseq");
		String smode = request.getParameter("sendViewMode");
		int recipientCount = 0;		
		String massRcpt = "";
		
		int maxRecipient = NumberUtils.toInt(systemConfigManager.getMaxRecipient(), -1);
		
		// 2012.08.13 mskim
		boolean isMassSenderUser = "on".equals(mailUserManager.readMassSenderUser(domainSeq));     // 대량메일발송권한 
		boolean readFileUploadUser = "on".equals(mailUserManager.readFileUploadUser(domainSeq));   // 대량메일수신파일 업로드
		
		boolean readMassSend = "on".equals(mailUserManager.getMassSend(userSeq));                  // 특정 사용자 대량 메일 발송 권한 
		boolean readMassUpload = "on".equals(mailUserManager.getMassUpload(userSeq));              // 특정 사용자 대량 메일 수신 파일 업로드 권한   
				
		try {
			strRcptto = ((strTo != null) ? strTo : "")
					+ ((strCc != null) ? ","+strCc : "")
					+ ((strBcc != null) ? ","+strBcc : "");
			strRcptto = strRcptto.trim();
			recipientCount = org.apache.commons.lang.StringUtils.splitPreserveAllTokens(strRcptto, ",").length;
		
			if (maxRecipient < recipientCount) {                       // 최대 수신자 수보다 많을때
			    if (!isMassSenderUser && !readFileUploadUser) {        // 모든 사용자에게 권한 부여 
			        strRcptto = makeRcpt(strRcptto);
			    } else if (!isMassSenderUser && readFileUploadUser) {  // 모든 사용자에게 대량메일 발송권한 부여 & 수신자 파일업로드 개별 부여
			        //if (readMassUpload) {
			            strRcptto = makeRcpt(strRcptto);
			        //}
			    } else if (isMassSenderUser) {                         // 특정 사용자에게 대량메일 발송 부여
			        if (readMassSend) {
			            strRcptto = makeRcpt(strRcptto);
			        }
			    }
			} else {
			    if ("on".equals(massRcptMode)) {
			            massRcpt = getMassFileRcpt(request.getParameter("massRcpt"), strRcptto);
	                            
	                            String[] emails = massRcpt.split(",");                          
	                            massRcpt = "";
	                            for (int i = 0; i < emails.length; i++) {
	                                try {
                                            InternetAddress internetAddress = new InternetAddress(emails[i]);
                                            if (isValidEmail(internetAddress.getAddress())) {
                                                massRcpt += internetAddress.getAddress();
                                                massRcpt += ",";
                                            }
                                        } catch (AddressException e) {
                                            e.printStackTrace();
                                        }
	                            }
	                            
	                            massRcpt = massRcpt.substring(0, massRcpt.length() - 1);
	                            strTo = (strTo != null && strTo.length() > 0 && isValidEmail(strTo))? strTo + ","+ massRcpt:massRcpt;
	                            strRcptto = (strRcptto.length() > 0)? strRcptto + ","+ massRcpt:massRcpt;
			    }
			}
			
			/*
			if ("on".equals(massRcptMode)) {
     			    massRcpt = getMassFileRcpt(request.getParameter("massRcpt"), strRcptto);
     			    
	         	    String[] emails = massRcpt.split(",");	         	    
	         	    massRcpt = "";
	         	    for (int i = 0; i < emails.length; i++) {
	         	        if (isValidEmail(emails[i])) {
	         	            massRcpt += emails[i];
	         	            massRcpt += ",";
	         	        }
	         	    }
	         	    
	         	    massRcpt = massRcpt.substring(0, massRcpt.length() - 1);
	         	    strTo = (strTo != null && strTo.length() > 0 && isValidEmail(strTo))? strTo + ","+ massRcpt:massRcpt;
			    strRcptto = (strRcptto.length() > 0)? strRcptto + ","+ massRcpt:massRcpt;
			}
			*/
		} catch(Exception e) {
			LogManager.writeErr(this, "MASS RCPT MAKE FILE ERROR", e);
			throw new Exception("MASS RCPT MAKE FILE ERROR",e);
		}
		
		
		SenderInfoBean info = new SenderInfoBean();
		String sendType = request.getParameter("sendType");
		boolean isAutoSave = false;
		boolean isNoRcpt = false;
		if(sendType.equals("autosave")){
			isAutoSave = true;
			sendType = "draft";			
		}	
		
		if(!"draft".equals(sendType) && strRcptto.length() == 0){
			sendType = "draft";
			isNoRcpt = true;
		}
		 
		boolean invalidMaxRecipient = false;
	    String maxRecipientMessage = "";
		
		try{
			info.setAutoSave(isAutoSave);
			info.setSendType(sendType);
			info.setAllowMaxRcpts(allowMassMail);
			info.setMaxRcpt(maxRcpt);
			info.setMdnUrl(mdnHost);
			info.setLocalUrl(localhost);
			
			info.setTo(strTo);
			info.setCc(strCc);
			info.setBcc(strBcc);
			info.setRcptto(strRcptto);	
			
			info.setSubject(request.getParameter("subject"));
			info.setContent(request.getParameter("content"));
			
			info.setSenderEmail(request.getParameter("senderEmail"));
			info.setSenderName(request.getParameter("senderName"));
			
			info.setSendFlag(request.getParameter("sendFlag"));
			
			info.setPriority(request.getParameter("priority"));
			info.setRecvNoti("on".equals(request.getParameter("receivenoti")));
			info.setOneSend("on".equals(request.getParameter("onesend")));
			info.setSaveSent("on".equals(request.getParameter("savesent")));
			info.setAttachVcard("on".equals(request.getParameter("attachvcard")));
			info.setAttachSign("on".equals(request.getParameter("attachsign")));
			info.setHtmlMode("html".equals(request.getParameter("wmode")));		
	
			info.setFolder(request.getParameter("folder"));
			info.setUids(request.getParameter("uids"));
			
			info.setSenderName(request.getParameter("senderName"));
			info.setSenderEmail(request.getParameter("senderEmail"));
	
			info.setCharset(request.getParameter("encharset"));
			info.setAttlist(request.getParameter("attachList"));
			
			info.setBigAttach("on".equalsIgnoreCase(request.getParameter("bigAttachMode")));
			info.setBigAttachLink(request.getParameterValues("bigAttachLinks"));
			
			info.setReservYear(request.getParameter("reservYear"));
			info.setReservMonth(request.getParameter("reservMonth"));
			info.setReservDay(request.getParameter("reservDay"));
			info.setReservHour(request.getParameter("reservHour"));
			info.setReservMin(request.getParameter("reservMin"));
			
			info.setDraftMid(request.getParameter("draftMid"));
			info.setTrashMid(request.getParameter("trashMid"));
			info.setLetterPaperMode(request.getParameter("letterMode"));
			
			info.setSecureMail("on".equals(request.getParameter("secure")));
			info.setCryptMethod(systemConfigManager.getCryptMethod());
					
			info.setBennerInfo(systemConfigManager.getBannerInfo(domainSeq));		
			
			info.setRemoteIp(remoteIP);
			info.setUser(user);
			info.setSecureMailModule(secureMailModule);
			
			info.setBigAttachMailContents(request.getParameter("bigattachContents"));
			
			if(!info.isSendDraft() && !info.isOneSend()){
				info.setOneSend(checkFoceOnesend(domainSeq, strRcptto));
			}
			
			if(info.isSecureMail()){		
				MailSecureInfoBean secureInfoBean = new MailSecureInfoBean();			
				secureInfoBean.setSslHost(sslHost);
				secureInfoBean.setSecureMailPassword(request.getParameter("securePass"));
				secureInfoBean.setSecureMailHint(request.getParameter("secureHint"));		
				secureInfoBean.setWebRootPath(context.getRealPath("/"));			
				secureInfoBean.setCryptMethod(info.getCryptMethod());
				secureInfoBean.setModule(secureMailModule);
				secureInfoBean.setHtmlMode(info.isHtmlMode());
				secureInfoBean.setLocale(user.get(User.LOCALE));
				info.setSecureInfo(secureInfoBean);
				Map<String, String> msgMap = new HashMap<String, String>();
				msgMap.put("SECURE_MSG_TITLE", StringUtils.javaToHtml(msgResource.getMessage("mail.secure.title")));
				msgMap.put("CONTENT_MSG_1", msgResource.getMessage("mail.secure.content.001"));
				msgMap.put("CONTENT_MSG_2", msgResource.getMessage("mail.secure.content.002"));
				msgMap.put("CONTENT_MSG_3", msgResource.getMessage("mail.secure.content.003"));
				msgMap.put("CONTENT_PKI_MSG_1", msgResource.getMessage("mail.secure.content.004"));
				msgMap.put("FORM_MSG_1", StringUtils.javaToHtml(msgResource.getMessage("mail.secure.form.001")));
				msgMap.put("FORM_MSG_2", StringUtils.javaToHtml(msgResource.getMessage("mail.secure.form.002")));
				msgMap.put("FORM_MSG_3", StringUtils.javaToHtml(msgResource.getMessage("mail.secure.form.003")));
				msgMap.put("FORM_MSG_4", StringUtils.javaToHtml(msgResource.getMessage("mail.secure.form.004")));
				secureInfoBean.setMsgMap(msgMap);			
			}
			
			if(info.isAttachVcard()){
				String vcard = vcardManager.getVcardString(userSeq);
				if(StringUtils.isNotEmpty(vcard))	{
					info.setVcard(vcard);	
				}
			}
			
			if(info.isLetterPaperMode()){
				String letterPath = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
				String url = "";
				int letterSeq = Integer.parseInt(request.getParameter("letterSeq"));
				info.setLetterPaper(letterManager.readLetter(letterSeq, letterPath, url));			
			}
			
			if(info.isAttachSign() && signSeq != null){			
				info.setSignData(signManager.getSignData(userSeq, Integer.parseInt(signSeq)));
			}				
			
			Image2Cid cid = new Image2Cid();		
			info.setContent(cid.parseUploadImage(info.getContent()));		
			info.setImage2cid(cid.getImage2Cid());
		
		}catch(Exception e){
			LogManager.writeErr(this, "SEND INFO SETTING ERROR", e);
			throw new Exception("SEND INFO SETTING ERROR",e);
		}
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		Protocol ladminProtocol  = null;
		boolean detectVirus = false;
		String virusMsg = "";
		
		try {
			
			store = factory.connect(remoteIP, user);
			ladminProtocol = new Protocol(user.get(User.MAIL_HOST));
			ladminManager.setResource(ladminProtocol,msgResource);			
			info.setLadminManager(ladminManager);			
			
			if (StringUtils.isNotEmpty(info.getAttlist())) {
				String virusHost = EnvConstants.getVirusSetting("virus.server");
				String virusPortStr = EnvConstants.getVirusSetting("virus.server.port");
				int virusPort = StringUtils.isNotEmpty(virusPortStr) ? Integer.parseInt(virusPortStr) : 17777;
				VirusCheckVO checkVO = virusManager.checkVirus(virusHost, virusPort, info.getAttlist(), commonResource);
				if (!checkVO.isSuccess()) {
					if(!info.isSendDraft()){
						String content = info.getContent();
						int start = content.indexOf("<div class='TerraceMsg'>");
						if (start != -1) {
							int end = content.lastIndexOf("</div>");
							content = content.substring(start+24, end);
							info.setContent(content);
						}
					}
					info.setSendType("draft");
					info.setAttlist("");
					info.setSaveSent(false);
					info.setRecvNoti(false);
					
					detectVirus = true;
					virusMsg = checkVO.getCheckResultMsg();
				}
			}
		
            //int maxRecipient = NumberUtils.toInt(systemConfigManager.getMaxRecipient(), -1);

            if (maxRecipient < recipientCount && !flag) {
                maxRecipientMessage = msgResource.getMessage("mail.secure.maxrcpt", new Object[] { maxRecipient });

                info.setSendType("draft");
                info.setSaveSent(false);
                info.setRecvNoti(false);

                invalidMaxRecipient = true;
            }

			MimeMessageBuilder builder = null;
			if(info.isSendDraft()){
				builder = new DraftMimeMessageBuilder();
			} else {
				builder = new GeneralMimeMessageBuilder();
			}			
			
			SendMessageDirector director = new SendMessageDirector(builder, info);			
			sendResult = director.sendProcess(store, factory);
			
			String delMid = info.getDraftMid();
			if(delMid != null && delMid.length() > 0){
				TMailFolder draftFolder = store.getFolder(FolderHandler.DRAFTS);
				deleteTempMessage(draftFolder, delMid,user.get(User.MAIL_USER_SEQ));
			}
			delMid = info.getTrashMid();
			if(delMid != null && delMid.length() > 0){
				TMailFolder draftFolder = store.getFolder(FolderHandler.TRASH);
				deleteTempMessage(draftFolder, delMid,user.get(User.MAIL_USER_SEQ));
			}
			
			if(!isAutoSave){
				rcptManager.saveLastRcpt(userSeq, sendResult.getSendAddressList());			
			}			
			
		} catch (Exception e) {
			if(sendResult == null){
				sendResult = new MailSendResultBean();				
			}
			sendResult.setErrorOccur(true);
			sendResult.setSendType("error");			
			LogManager.writeErr(this, e.getMessage(), e);
		}finally{			
			try {
				if(store != null && store.isConnected()){
					store.close();
				}				
			} catch (Exception ignore) {
				LogManager.writeErr(this, ignore.getMessage(), ignore);
			}
			
			if(ladminProtocol != null){
				ladminProtocol.logout();
			}
		}
		
		if(sendResult.isErrorOccur() && 
				sendResult.getInvalidAddress() == null){
				sendResult.setInvalidAddress(strRcptto);			
		}
		
		if (detectVirus) {
			sendResult.setErrorOccur(true);
			sendResult.setDetectVirus(true);
			sendResult.setErrorMessage(virusMsg);
		}
		
		if (invalidMaxRecipient) {
            sendResult.setErrorOccur(true);
            sendResult.setErrorMessage(maxRecipientMessage);
            sendResult.setInvalidAddress(strRcptto);
	    }
	        
	
		info.dispose();
		String forward = null;
		
		if(isAutoSave){
			JSONObject jObj = new JSONObject();
			
			jObj.put("msgId", sendResult.getSaveMid());
			jObj.put("updateTime", new SimpleDateFormat("HH:mm:ss").format(new Date()));
			
			response.addHeader("Cache-Control","no-store");
	        response.addHeader("Pragma", "no-cache");
	        PrintWriter out = response.getWriter();
	        out.println(jObj);
			
		} else {
			writeSendMailLog(info, invalidMaxRecipient);			
			if(sendResult.getSendType().equals("reserved")){
				sendResult.setSendFolderName(msgResource.getMessage("folder.reserved"));
			} else if(sendResult.getSendType().equals("draft")){				
				sendResult.setSendFolderName(msgResource.getMessage("folder.drafts"));			        				
			} else if(sendResult.getSendType().equals("savesent")){
				sendResult.setSendFolderName(msgResource.getMessage("folder.sent"));
			}
			if(isNoRcpt){
				sendResult.setErrorOccur(true);
				sendResult.setNoRcpt(true);				
			}
			
			if("popup".equals(smode)){
				forward = "popupSend";
			} else {
				forward = "success";
			}			
		}		
		return forward;
	}

	private boolean checkFoceOnesend(int domainSeq, String strRcptto) {
            Map forceOnesendConfig = systemConfigManager.getForceEachSendConfig(domainSeq);
            boolean onesendCheck = false;
            boolean isCheckRcptMax = (Boolean) forceOnesendConfig.get("forceEachUse");
            // Oracle Bug - hkkim - 2011.03.11
            int checkRcptSize = 0;
            String forceEachRcptCnt = (String) forceOnesendConfig.get("forceEachRcptCnt");
            if (forceEachRcptCnt != null &&  forceEachRcptCnt.length() > 0) {
                checkRcptSize = Integer.parseInt((String) forceOnesendConfig.get("forceEachRcptCnt"));
            }
            boolean isDomainCheck = (Boolean) forceOnesendConfig.get("forceEachOutDomain");
            String userDomain = "@" + user.get(User.MAIL_DOMAIN);

            if (strRcptto != null) {
                String[] rcpt = strRcptto.split("[;,\r\n]");
                if (isCheckRcptMax && (rcpt.length >= checkRcptSize)) {
                    onesendCheck = true;
                }
                if (!onesendCheck && isDomainCheck) {
                    for (int i = 0; i < rcpt.length; i++) {
                        if (rcpt[i].indexOf(userDomain) < 0) {
                            onesendCheck = true;
                            break;
                        }
                    }
                }
            }
            return onesendCheck;
        }

	public void writeSendMailLog(SenderInfoBean info, boolean invalidMaxRecipient){
        	String action = "";
                String folder = "";
                boolean isJobLog = false;
                
		if(sendResult != null && !sendResult.isErrorOccur()){
			if(info.isSaveSent()){
				folder = "Sent";
			}
			if("normal".equals(sendResult.getSendResultType())){
				action = "action_message_send";
				isJobLog = true;
			} else if("reserved".equals(sendResult.getSendResultType())){
				action = "action_message_reserved";
				folder = "Reserved";
				isJobLog = false;
			} else if("draft".equals(sendResult.getSendResultType())){
				action = "action_message_draft";			        
				folder = "Drafts";
				isJobLog = true;
			} else if("batch".equals(sendResult.getSendResultType())){
				action = "action_message_send";
				isJobLog = true;
				writeMailLog(false,"action_message_batch", folder, sendResult.getAllVaildAddress(), 
						user.get(User.EMAIL), sendResult.getMailSize(), 
						info.getSubject(), sendResult.getSaveMid());
			}			
			writeMailLog(isJobLog,action, folder, sendResult.getAllVaildAddress(), 
					user.get(User.EMAIL), sendResult.getMailSize(), 
					info.getSubject(), sendResult.getSaveMid());			
		} else if (invalidMaxRecipient) {			    
		    action = "action_reject_maxrcpt";                          
                    folder = "Drafts";
                    isJobLog = true;
                    writeMailLog(isJobLog,action, folder, sendResult.getAllVaildAddress(), 
                            user.get(User.EMAIL), sendResult.getMailSize(), 
                            info.getSubject(), sendResult.getSaveMid());
		}
	}
	
	
	
	public String executePart() throws Exception {		
		return execute();
	}
	
	public void setRcptManager(LastrcptManager rcptManager) {
		this.rcptManager = rcptManager;
	}
	
	public void setVcardManager(VCardManager vcardManager) {
		this.vcardManager = vcardManager;
	}
	
	public void setLetterManager(LetterManager letterManager) {
		this.letterManager = letterManager;
	}
	
	public void setSignManager(SignManager signManager) {
		this.signManager = signManager;
	}
	
	public void setSearchEmailManager(SearchEmailManager searchEmailManager) {
		this.searchEmailManager = searchEmailManager;
	}
	
	public void setLadminManager(LadminManager ladminManager) {
		this.ladminManager = ladminManager;
	}
	
	public void setSystemConfigManager(SystemConfigManager systemConfigManager) {
		this.systemConfigManager = systemConfigManager;
	}
	
	public void setVirusManager(VirusManager virusManager) {
		this.virusManager = virusManager;
	}

	public MailSendResultBean getSendResult(){
		return sendResult;		
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
            this.mailUserManager = mailUserManager;
        }
	
	private String getCheckEmailAddress(int userSeq, int domainSeq, String addr, String domain) {
		if(addr == null || addr.length() <= 0) {
			return null;
		}

		String[] addrs = addr.split("[;,\r\n]");
		String addrstr = "";
		String addrTrim = null;
		String searchToken = null;
		List<MailAddressBean> addrList = null;
		String tempAddrStr = null;
		
		for(int i = 0; i < addrs.length; i++) {
			addrTrim = TMailAddress.getTrimAddress(addrs[i]);
			
			
			if (addrTrim.indexOf("@") < 0) {
				addrTrim = TMailAddress.getEmailAddress(addrTrim);

			}

			if(addrTrim.length() == 0) {
				continue;
			}
			else if(TMailAddress.isPersonalString(addrTrim)) {
				addrstr += addrTrim + " ";
				continue;
			}
			else if(addrTrim.charAt(0) == '&' && addrTrim.indexOf("@") < 0) {				
				searchToken = addrTrim.substring(1);
				addrList = searchEmailManager.readSharedAddrEmailList(userSeq, searchToken);
				tempAddrStr = getMailAddressStr(addrList);
				if(tempAddrStr.length() > 0){
					addrstr += tempAddrStr;
				} else {
					continue;
				}				 
			}
			else if(addrTrim.charAt(0) == '$' && addrTrim.indexOf("@") < 0) {
				searchToken = addrTrim.substring(1);
				addrList = searchEmailManager.readPrivateAddrEmailList(userSeq, searchToken);
				tempAddrStr = getMailAddressStr(addrList);
				if(tempAddrStr.length() > 0){
					addrstr += tempAddrStr;
				} else {
					continue;
				}
			}           
            else if(addrTrim.indexOf("#") == 0 && addrTrim.indexOf("@") < 0) {
            	searchToken = addrTrim.substring(1);
            	
            	String[] tokens = searchToken.split("\\.");

        		if(tokens.length > 3) {
        			String orgCode = tokens[0];            		
            		String codeType = tokens[1];
                    String code = tokens[2];
                    boolean isSearchHierarchy= Boolean.parseBoolean(tokens[3]);
                	
    				addrList = searchEmailManager.readAddressList(domainSeq, orgCode, 
    						codeType, code, isSearchHierarchy);
    				tempAddrStr = getMailAddressStr(addrList);
    				if(tempAddrStr.length() > 0){
    					addrstr += tempAddrStr;
    				} else {
    					continue;
    				}
        		}else if(tokens.length==2) {
        			String orgName = tokens[0];
        			String orgCode = tokens[1];
        			
        			addrList = searchEmailManager.readDeptAddressList(domainSeq, orgName, orgCode);
        			
        			tempAddrStr = getMailAddressStr(addrList);
    				if(tempAddrStr.length() > 0){
    					addrstr += tempAddrStr;
    				} else {
    					continue;
    				}
        		}else if(tokens.length==1) {
        			String orgName = tokens[0];
        			
        			addrList = searchEmailManager.readDeptAddressList2(domainSeq, orgName);
        			
        			tempAddrStr = getMailAddressStr(addrList);
    				if(tempAddrStr.length() > 0){
    					addrstr += tempAddrStr;
    				} else {
    					continue;
    				}
        		}else {
					continue;
				}

        		
				//TODO ���� �ּ� ������ ����ڸ� ��������	            	
                /*String systemAddr = getSystemMember(domain, addr_trim);
                if(systemAddr.length() <= 0) continue;
                addrstr += systemAddr;*/
            }
			else if (HangulEmail.isHangulEmail(addrTrim)) {
                String s = HangulEmail.getHangulEmail(addrTrim);

                if(s.length() <= 0) continue;

                addrstr += s;
            }
			else if(addrTrim.indexOf("@") < 0) {
				addrstr += addrTrim + "@" + domain;
			}
			else {
				addrstr += addrTrim;
			}

			if((i+1) != addrs.length) {
				addrstr += ",";
			}
		}
			
		String[] addrstrs = addrstr.split(",");
		String emailAddr = "";
		String email_trim = null;
		String[] email_spilit = null;
		String tmp = null;
		for(int i = 0; i < addrstrs.length; i++) {
			
			email_trim = TMailAddress.getTrimAddress(addrstrs[i]);
			
			email_spilit = email_trim.split("<");
			if(email_spilit.length != 1){
				if(existEmailAddress(emailAddr, email_spilit[1])){
					continue;
				}
				tmp = email_spilit[0].replaceAll("\"","").trim();				
				if(tmp.length() > 0){
					email_trim="\""+tmp+"\"<"+email_spilit[1];
				} else {
					email_trim=email_spilit[1].replace(">", "");
				}
			} else {
				if(existEmailAddress(emailAddr, email_trim)){
					continue;
				}
			}
			emailAddr += email_trim;			
			if((i+1) != addrstrs.length) {			
				emailAddr += ",";
			}
						
		}
		email_trim = null;
		email_spilit = null;
		tmp = null;
		return emailAddr;
	}

	private boolean existEmailAddress(String emails, String email ){
		boolean exist = false;
		String tmpEmail = null;
		String [] email_spilit = emails.split(",");
		if(email_spilit.length > 0) {
			for (int i = 0; i < email_spilit.length; i++) {
				tmpEmail = email_spilit[i];
				tmpEmail = TMailAddress.getAddress(TMailAddress.getTrimAddress(tmpEmail));
				email = TMailAddress.getAddress(TMailAddress.getTrimAddress(email));
				if (tmpEmail.equals(email)){
				    exist = true;
				}
			}
		} else {
			emails = TMailAddress.getAddress(TMailAddress.getTrimAddress(emails));
			email = TMailAddress.getAddress(TMailAddress.getTrimAddress(email));
			if (emails.equals(email)){
			    exist = true;
			}
		}
		return exist;
	}
	
	private String getMailAddressStr(List<MailAddressBean> list){
		StringBuffer sb = new StringBuffer();
		MailAddressBean mailAddressBean = null;
		if(list.size() > 0){
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				mailAddressBean = (MailAddressBean) iterator.next();
				sb.append(mailAddressBean.getAddress());
				sb.append(",");
			}
		}
		mailAddressBean = null;
		return sb.toString();
	}
	
	private void deleteTempMessage(TMailFolder draftsFolder, String msgId,
			String userSeq) throws MessagingException {
		draftsFolder.open(true);
		long uid = draftsFolder.xsearchMID(msgId);
		if(uid > -1){
			TMailMessage msg = draftsFolder.getMessageByUID(uid,false);
			
			draftsFolder.setFlags(new TMailMessage[]{msg}, new Flags(
						Flags.Flag.DELETED), true);		
			draftsFolder.close(true);
		}
	}
	
	private String getMassFileRcpt(String filePath, String strRcptto) {
		StringBuffer sb = new StringBuffer();
		String tmpDir = EnvConstants.getBasicSetting("tmpdir");
		File rcptFile = new File(tmpDir + EnvConstants.DIR_SEPARATOR + filePath);
		
		try {
			if(rcptFile.exists()){
				BufferedReader reader = new BufferedReader(new FileReader(rcptFile));
				String line = null;
				String email = null;
				while ((line = reader.readLine()) != null) {
					email = TMailAddress.getEmailAddress(line);				
					sb.append(",");					
					if(strRcptto.indexOf(email) < 0){
						sb.append(line);
					}					
				}
			}
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}	
		
		return sb.toString(); 
	}
}
