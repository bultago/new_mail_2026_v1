/**
 * WriteAction.java 2008. 12. 4.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.action;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONObject;

import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.ExtPartConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mail.ibean.MailWriteMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageWriteInfoBean;
import com.terracetech.tims.webmail.mail.manager.BigattachManager;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.setting.manager.LastrcptManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.manager.SignManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p>
 * <strong>WriteAction.java</strong> Class Description
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

@SuppressWarnings("all")
public class WriteMessageAction extends BaseAction {
	private static final long serialVersionUID = 20081204L;
	private MailUserManager mailUserManager = null;
	private MailManager mailManager = null;
	private SettingManager userSettingManager = null;
	private BigattachManager bigAttachManager = null;
	private BbsManager bbsManager = null;
	private SignManager signManager = null;
	private SystemConfigManager systemConfigManager = null;
	
	private MailWriteMessageBean writeBean = null;
	private LastrcptManager rcptManager = null;
	
	public MailWriteMessageBean getWriteBean() {
		return writeBean;
	}
	
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	public void setRcptManager(LastrcptManager rcptManager) {
		this.rcptManager = rcptManager;
	}
	
	public void setUserSettingManager(SettingManager userSettingManager) {
		this.userSettingManager = userSettingManager;
	}
	
	public void setBigAttachManager(BigattachManager bigAttachManager) {
		this.bigAttachManager = bigAttachManager;
	}
	
	public void setBbsManager(BbsManager bbsManager) {
		this.bbsManager = bbsManager;
	}
	
	public void setSignManager(SignManager signManager) {
		this.signManager = signManager;
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public void setSystemConfigManager(SystemConfigManager systemConfigManager) {
		this.systemConfigManager = systemConfigManager;
	}
	

	public String execute() throws Exception {
		String processMethod = request.getParameter("method");
		boolean isAjax = (processMethod != null && processMethod.equals("ajax"))?true:false;
		
		boolean isError = false;
		boolean isMassConfirm = false;
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int groupSeq = Integer.parseInt(user.get(User.MAIL_GROUP_SEQ));
				
		String secureWriteMode = request.getParameter("secureWriteMode");
		secureWriteMode = (secureWriteMode != null)?secureWriteMode:"normal";
		
		String wmode = request.getParameter("wmode");
		wmode = (wmode != null)?wmode:"normal";
		
		String imgPath = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
		String secureMailModule = EnvConstants.getUtilSetting("secure.module");
	
		// Check Browser
		String agent = request.getParameter("agent");
		if(agent == null) {
			agent = request.getHeader("user-agent");
		}

		String browserinfo = "MSIE";
		boolean isMsieBrowser = true;
		String writeType = request.getParameter("wtype");
        writeType = StringEscapeUtils.escapeHtml(writeType);
		
		//User user = EnvConstants.getTestUser();
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;		
		
		if("bbs".equals(writeType)){
			Map<String, String> confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
			store = factory.connect(request.getRemoteAddr(), confMap);
			mailManager.setProcessResource(store, getMessageResource("bbs"));
		} else {			
			store = factory.connect(request.getRemoteAddr(),user);
			mailManager.setProcessResource(store, getMessageResource());
		}
		
		
		if (agent.equals("mobile")						// MOBILE 
			|| ((agent.indexOf("MSIE") < 0				// NETSCAPE & MOZILLA
				|| agent.indexOf("Mac") > 0				// MAC OS
				|| agent.indexOf("Opera") > 0))) {		// OPERA 
			// modified by doyoung 2005.11.07
			isMsieBrowser = false;
			browserinfo = "NOT_MSIE";
		}
		
		String toReq = request.getParameter("to");
		String ccReq = request.getParameter("cc");
		String bccReq = request.getParameter("bcc");
		String subjectReq = request.getParameter("subj");
		String contentReq = request.getParameter("content");
		
		/*if (request.getMethod().equalsIgnoreCase("get")) {
			toReq = StringUtils.getDecodingUTF(toReq);
			ccReq = StringUtils.getDecodingUTF(ccReq);
			bccReq = StringUtils.getDecodingUTF(bccReq);
			subjectReq = StringUtils.getDecodingUTF(subjectReq);
			contentReq = StringUtils.getDecodingUTF(contentReq);
		}*/

		
		JSONObject jObj = null;
		try {		
			Map<String, String> configMap = mailUserManager.readUserSetting(domainSeq, groupSeq, userSeq);
			
			MessageWriteInfoBean writeInfo = new MessageWriteInfoBean();			
			writeInfo.setWriteType(writeType);
			writeInfo.setLocalMailWrite(wmode.equals("localpopup"));
			
			if(writeInfo.isLocalMailWrite()){
				writeInfo.setUids(new String[]{request.getParameter("puids")});
			} else {
				writeInfo.setUids(request.getParameterValues("uids"));
			}			
			writeInfo.setWuids(request.getParameter("wuid"));
			writeInfo.setFolderName(request.getParameter("folder"));
			writeInfo.setWebfolderType(request.getParameter("wfolderType"));
			writeInfo.setWebfolderShareSeq(request.getParameter("wfolderShareSeq"));
			writeInfo.setPlace(request.getParameter("place"));
			writeInfo.setReqTo(toReq);
			writeInfo.setReqCc(ccReq);
			writeInfo.setReqBcc(bccReq);
			writeInfo.setReqSubject(subjectReq);
			writeInfo.setReqContent(contentReq);
			writeInfo.setReturl(request.getParameter("returl"));
			
			writeInfo.setAttachLists(request.getParameterValues("attlists"));
			writeInfo.setAttatachUrls(request.getParameterValues("atturls"));
			writeInfo.setAttatachFilenames(request.getParameterValues("down_filename"));
			writeInfo.setAutoSaveMode(request.getParameter("autoSaveMode"));
			writeInfo.setForwardingMode(request.getParameter("fwmode"));
			
			writeInfo.setBuids(request.getParameter("buid"));
			writeInfo.setBmids(request.getParameter("bmid"));
			writeInfo.setBbsId(request.getParameter("bbsid"));
			writeInfo.setSignInside("inside".equals(getSignLocation(configMap)));
			writeInfo.setImgFilePath(imgPath);
						
			UserEtcInfoVO userSettingVo = userSettingManager.readUserEtcInfo(userSeq);
			
			String senderName = userSettingVo.getSenderName();
			String senderEmail = userSettingVo.getSenderEmail();
			writeBean = mailManager.getWriteSettingBean(writeInfo, user);
			writeBean.setEditorMode(userSettingVo.getWriteMode());
			writeBean.setSaveSent(userSettingVo.getSaveSendBox());
			writeBean.setReceiveNoti(userSettingVo.getReceiveNoti());
			writeBean.setEncoding(userSettingVo.getCharSet());			
			writeBean.setVcardAttach(userSettingVo.getVcardAttach());
			writeBean.setSenderName((senderName == null || 
									senderName.trim().length() == 0)?
									user.get(User.USER_NAME):senderName);
			writeBean.setSenderEmail((senderEmail == null || 
									senderEmail.trim().length() == 0)?
									user.get(User.EMAIL):userSettingVo.getSenderEmail());
			
			String autoSaveMode = userSettingVo.getAutoSaveMode();
			writeBean.setAutoSaveMode((autoSaveMode != null)?autoSaveMode:"off");
			writeBean.setAutoSaveTerm(userSettingVo.getAutoSaveTerm());			
			
			//TCUSTOM-3262 201706
			//html event replace
			if("bbs".equals(writeType)){
				StringReplaceUtil sr = new StringReplaceUtil();
				writeBean.setHtmlContent(StringEscapeUtils.unescapeHtml(sr.replace(writeBean.getHtmlContent(), false, true)));
			}

			//SignSetting		
			writeBean.setSignAttach(getSignApply(configMap));			
			request.setAttribute("signList", "");
			request.setAttribute("signLocation", getSignLocation(configMap));
			
			writeBean.setSecuremail(getSecureMailConfig(configMap));
			writeBean.setSecuremailModule(secureMailModule);
			
			writeBean.setMaxReservedDay(getMaxReservedDay(configMap));
			writeBean.setRcptMode(getRcptModeConfig(configMap));
			writeBean.setSecureWriteMode(secureWriteMode);
			if(writeBean.isXecureExpressWrite()){
				writeBean.setUserDN(mailUserManager.getUserDN(userSeq));
				writeBean.setSecureMakeMode(ExtPartConstants.getExtPartConfig("softforum.express.type"));
				writeBean.setExpressMaxSendCnt(Integer.parseInt(ExtPartConstants.getExtPartConfig("softforum.express.maxsendcount")));				
			}			

			// 2012.08.13 mskim					
			boolean isMassSenderUser = "on".equals(mailUserManager.readMassSenderUser(domainSeq));     // 대량메일발송권한 
	                boolean readFileUploadUser = "on".equals(mailUserManager.readFileUploadUser(domainSeq));   // 대량메일수신파일 업로드	                
	                boolean readMassSend = "on".equals(mailUserManager.getMassSend(userSeq));                  // 특정 사용자 대량 메일 발송 권한 
	                boolean readMassUpload = "on".equals(mailUserManager.getMassUpload(userSeq));              // 특정 사용자 대량 메일 수신 파일 업로드 권한   
			
	                if (!isMassSenderUser && !readFileUploadUser) {        // 모든 사용자에게 권한 부여 
	                    isMassConfirm = true;
                        } else if (!isMassSenderUser && readFileUploadUser) {  // 모든 사용자에게 대량메일 발송권한 부여 & 수신자 파일업로드 개별 부여
                            if (readMassUpload) {
                                isMassConfirm = true;
                            }
                        } else if (isMassSenderUser) {                         // 특정 사용자에게 대량메일 발송 부여
                            if (readMassSend) {
                                isMassConfirm = true;
                            }
                        }
	                
//			if (isMassSenderUser) {   
//			    isMassConfirm = "on".equalsIgnoreCase(getMassSend(configMap));
//			}
			
			long bigattachQuotaUseage = Long.parseLong(configMap.get("file_size"));
			Map<String, String> attachConfigMap = getAttachConfig(configMap);
			
			if("webfolder".equalsIgnoreCase(writeInfo.getWriteType())){
				String logStr = writeBean.getLogStr();
				if(logStr != null){
					String[] logLines = logStr.split("\n");
					String[] logContent = null;
					for (int j = 0; j < logLines.length; j++) {
						logContent = logLines[j].split("\\|");
						writeWebfolderLog(true,"wfolder_mail_send", logContent[0],"", "", "", Long.parseLong(logContent[1]),logContent[2],"");
					}
					logContent = null;
					logLines = null;
					writeBean.setLogStr(null);
				}
			}
			
			if(isAjax){
				jObj = writeBean.toJson();
				jObj.put("wmode", (writeType != null)?writeType:"html");
				jObj.put("bigAttachDownCnt",(String)attachConfigMap.get("bigattach_download"));
				jObj.put("bigAttachExpireday", (String)attachConfigMap.get("bigattach_expireday"));
				jObj.put("maxAttachSize",(String)attachConfigMap.get("attach_maxsize"));
				jObj.put("maxBigAttachSize", (String)attachConfigMap.get("bigattach_maxsize"));
				jObj.put("maxBigAttachQuota", (String)attachConfigMap.get("bigattach_maxsize"));
				jObj.put("useBigAttachDownCnt", (String)attachConfigMap.get("bigattach_download_limited"));				
				jObj.put("useBigAttachQuota", bigattachQuotaUseage);
				jObj.put("massRcptConfirm", Boolean.toString(isMassConfirm));
				jObj.put("systemTime", Long.toString(new Date().getTime()));
				jObj.put("sendCheckApply", "on".equalsIgnoreCase(configMap.get("send_check")));
				jObj.put("docTemplateApply", "on".equalsIgnoreCase(configMap.get("doc_template_use")));
			} else {
				request.setAttribute("wmode", (writeType != null)?writeType:"html");
				request.setAttribute("bigAttachDownCnt", (String)attachConfigMap.get("bigattach_download"));
				request.setAttribute("bigAttachExpireday", (String)attachConfigMap.get("bigattach_expireday"));			
				request.setAttribute("maxAttachSize", (String)attachConfigMap.get("attach_maxsize"));
				request.setAttribute("maxBigAttachSize", (String)attachConfigMap.get("bigattach_maxsize")); 
				request.setAttribute("maxBigAttachQuota",(String)attachConfigMap.get("bigattach_maxsize"));
				request.setAttribute("useBigAttachDownCnt", (String)attachConfigMap.get("bigattach_download_limited"));
				request.setAttribute("useBigAttachQuota", bigattachQuotaUseage);
				request.setAttribute("massRcptConfirm", Boolean.toString(isMassConfirm));
				request.setAttribute("systemTime", Long.toString(new Date().getTime()));
				request.setAttribute("sendCheckApply", "on".equalsIgnoreCase(configMap.get("send_check")));
				request.setAttribute("docTemplateApply", "on".equalsIgnoreCase(configMap.get("doc_template_use")));
			}			
			
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isError = true;
		} finally { 
			if(store !=null && store.isConnected())
				store.close();
			
			toReq = null;
			ccReq = null;
			bccReq = null;
			subjectReq = null;
			contentReq = null;
		}
		
		String forward = null;
		if(isAjax){
			if(isError){
				jObj.put("jobResult", "error");
			} else {
				jObj.put("jobResult", "success");
			}
			ResponseUtil.processResponse(response, jObj);
		} else {
			forward = "success";
			if(wmode.indexOf("popup") > -1){
				forward = "popupWrite";
			}
			
			if(isError){
				forward = "subError";
			}
		}				
		return forward;
	}

	private int getMaxReservedDay(Map<String, String> configMap){
		String maxReservedDay = configMap.get("reserve_limit");
		if(StringUtils.isEmpty(maxReservedDay))
			return 0;
		
		maxReservedDay = maxReservedDay.replaceAll("day", "");
		try {
			maxReservedDay = maxReservedDay.replaceAll("day", "");
			maxReservedDay = maxReservedDay.replaceAll(" ", "");
			return Integer.parseInt(maxReservedDay.trim());
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		
		return 0;
	}
	
	private String getRcptModeConfig(Map<String, String> configMap){
		
		String rcptMode = "normal";
		if(configMap != null && configMap.containsKey("rcpt_mode")){
			rcptMode = (String)configMap.get("rcpt_mode");
		}
		return rcptMode;
	}
	
	private Map<String, String> getAttachConfig(Map<String, String> configMap){
		
		if(!configMap.containsKey("bigattach_expireday")){
			configMap.put("bigattach_expireday", "7");
		} else if(!configMap.containsKey("bigattach_download")){
			configMap.put("bigattach_download", "10");
		} else if(!configMap.containsKey("bigattach_maxsize")){
			configMap.put("bigattach_maxsize", "500");			
		} else if(!configMap.containsKey("attach_maxsize")){
			configMap.put("attach_maxsize", "10");
		} else if(!configMap.containsKey("bigattach_download_limited")){
			configMap.put("bigattach_download_limited", "off");
		}
		
		return configMap;
	}
		
	private String getSecureMailConfig(Map<String, String> configMap){
		String securemail = "disabled";
		if(configMap != null && configMap.containsKey("securemail")){
			securemail = (String)configMap.get("securemail");
		}
		return securemail;		
	}
	
	private String getSignApply(Map<String, String> configMap){
		
		String signApply = "F";
		if(configMap != null && configMap.containsKey("sign_apply")){
			signApply = (String)configMap.get("sign_apply");
		}
		return signApply;
	}
	
	private String getSignLocation(Map<String, String> configMap){
		
		String signLocation = "outside";
		if(configMap != null && configMap.containsKey("sign_location")){
			signLocation = (String)configMap.get("sign_location");
		}
		return signLocation;
	}
}
