package com.terracetech.tims.webmail.note.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.exception.UserNotFoundException;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mail.manager.builder.TempMimeMessageBuilder;
import com.terracetech.tims.webmail.mail.manager.send.SendHandler;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.vo.MailUserInfoVO;
import com.terracetech.tims.webmail.note.manager.NoteHandler;
import com.terracetech.tims.webmail.note.manager.NoteManager;
import com.terracetech.tims.webmail.note.manager.NoteSendHandler;
import com.terracetech.tims.webmail.note.vo.NotePolicyVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class NoteSendAction extends BaseAction {

	private NoteManager noteManager = null;
	private MailUserManager mailUserManager = null;

	private String to = null;
	private String content = null;
	private String saveSentCheck = null;
	private Map<String, Integer> userSeqMap = new HashMap<String, Integer>();
	
	public void setNoteManager(NoteManager noteManager) {
		this.noteManager = noteManager;
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}

	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
	
		JSONObject result = new JSONObject();
		boolean isSuccess = true;
		
		I18nResources msgResource = getMessageResource("common");
		TMailStoreFactory factory = new TMailStoreFactory();
		
		String port = EnvConstants.getBasicSetting("web.port");		
		port = (port != null && port.length() > 0)?port:Integer.toString(request.getServerPort());
		String mdnHost = EnvConstants.getMailSetting("mdn.host");
		String localhost = request.getScheme() + "://" 
		+ request.getServerName() + ":" + port;
		
		String remoteIP = request.getRemoteAddr();
		
		mdnHost = (mdnHost != null)?mdnHost:localhost;
		mdnHost += EnvConstants.getMailSetting("mdn.action");
		
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String domain = user.get(User.MAIL_DOMAIN);
		List<String> failIdList = null;
		int totalCount = 0;
		int failCount = 0;
		
		try {
			Map<String, Object> addrResult = getCheckEmailAddress(to, domain, mailUserSeq);
			totalCount = (Integer)addrResult.get("totalCount");
			failCount = (Integer)addrResult.get("failCount");
			failIdList = (List<String>)addrResult.get("failIdList");
			
			if (totalCount == failCount) {
				throw new UserNotFoundException();
			}
			String addr = (String)addrResult.get("addr");
			
			SenderInfoBean info = new SenderInfoBean();
			info.setUser(user);
			info.setTo(addr);
			info.setSubject("");
			info.setContent(content);
			info.setOneSend(true);
			info.setSaveSent("on".equals(saveSentCheck));
			info.setMdnUrl(mdnHost);
			info.setHtmlMode(true);
			info.setCharset("utf-8");
			info.setRecvNoti(true);
			info.setRcptto(addr);
			
			info.setIasTo(TMailAddress.getParseEmailAddress(info.getTo(), info.getCharset()));
			info.setIasRcptto(TMailAddress.getParseEmailAddress(info.getRcptto(), info.getCharset()));
			info.setSenderName(user.get(User.USER_NAME));
			info.setSenderEmail(user.get(User.EMAIL));

		
			MimeMessage[][] messages = getMimeMessages(info, factory.getSession());
			
			
			TMailStore myStore = null;
			TMailStore targetStore = null;
			TMailFolder myFolder = null;
			TMailFolder targetFolder = null;
			TMailMessage tMailMessage = null;
			TMailMessage sentMailMessage = null;
			Map<String, String> connectMap = null;
			int targetSeq = 0;
			String userId = null;
			for (int i=0; i<messages[0].length;i++) {
				try {
					tMailMessage = new TMailMessage(messages[0][i]);
					sentMailMessage = new TMailMessage(messages[1][i]);
					userId = tMailMessage.getTo()[0].getAddress().split("@")[0];
					targetSeq = userSeqMap.get(userId);
					connectMap = noteManager.getNoteConnectInfo(targetSeq, mailDomainSeq);
					targetStore = factory.connect(remoteIP, connectMap);
					targetFolder = targetStore.getFolder(NoteHandler.INBOX);
					targetFolder.open(true);
					targetFolder.appendMessages(new TMailMessage[]{tMailMessage});
					targetFolder.close(true);
					
					if (info.isSaveSent()) {
						myStore = factory.connect(remoteIP, noteManager.getNoteConnectInfo(user));
						myFolder = myStore.getFolder(NoteHandler.SENT);
						myFolder.open(true);
						noteManager.changeFlag(sentMailMessage, NoteHandler.SEEN_FLAG, true);
						myFolder.appendMessages(new TMailMessage[]{sentMailMessage});
						myFolder.close(true);
						noteManager.setMDNFlag(tMailMessage.getTo()[0], myFolder, noteManager.getMessageId(messages[1][i]), info.getCharset());
					}
				} catch (Exception e) {
					LogManager.writeErr(this, e.getMessage(), e);
				} finally {
					if (myStore != null && myStore.isConnected()) {
						myStore.close();
					}
					if (targetStore != null && targetStore.isConnected()) {
						targetStore.close();
					}
				}
			}
		}
		catch (UserNotFoundException e) {
			isSuccess = false;
		}
		catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isSuccess = false;
		}
		
		StringBuffer sb = new StringBuffer();
		if (failCount > 0) {
			sb.append(msgResource.getMessage("note.msg.032", new Object[]{totalCount, failCount}));
			sb.append("\n\n");
			for (String failId : failIdList) {
				sb.append(msgResource.getMessage("note.msg.033", new Object[]{failId}));
				sb.append("\n");
			}
		} else {
			if (isSuccess) {
				sb.append(msgResource.getMessage("note.msg.034"));
			} 
			else {
				sb.append(msgResource.getMessage("note.msg.063"));
			}
		}
		
		result.put("isSuccess", isSuccess);
		result.put("msg", sb.toString());
		
		ResponseUtil.processResponse(response, result);
		
		return null;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public void setSaveSentCheck(String saveSentCheck) {
		this.saveSentCheck = saveSentCheck;
	}

	private Map<String, Object> getCheckEmailAddress(String addr, String domain, int mailUserSeq) {
		if(addr == null || addr.length() <= 0) {
			return null;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> failIdList = new ArrayList<String>();
		String addrstr = "";
		String[] addrs = addr.split("[;,]");
		String addrTrim = null;
		String userId = null;
		NotePolicyVO notePolicyVo = null;
		int totalCount = 0;
		int failCount = 0;
		for(int i = 0; i < addrs.length; i++) {
			addrTrim = TMailAddress.getTrimAddress(addrs[i]);
			
			if (StringUtils.isEmpty(addrTrim)) {
				continue;
			} else if(addrTrim.indexOf("@") < 0) {
				userId = addrTrim;
				addrTrim = addrTrim + "@" + domain;
			} else {
				String[] email_spilit = addrTrim.split("<");
				
				if(email_spilit.length != 1){
					String tmp = email_spilit[0].replaceAll("\"","").trim();
					String email = email_spilit[1].replace(">", "");
					if(tmp.length() > 0){
						addrTrim="\""+tmp+"\"<"+email_spilit[1];
					} else {
						addrTrim=email_spilit[1].replace(">", "");
					}
					userId = TMailAddress.getId(email);
				} else {
					userId = TMailAddress.getId(email_spilit[0]);
				}
			}
			
			int condTarget = mailUserManager.readUserSeq(userId, domain);
			MailUserInfoVO readUserInfo = mailUserManager.readUserInfo(condTarget);
			if(condTarget > 0 &&!"agree".equalsIgnoreCase(readUserInfo.getRegisterStatus())){
				continue;
				
			}
			if (condTarget > 0) {
				totalCount++;
				notePolicyVo = noteManager.readNotePolicy(condTarget);
				
				if (notePolicyVo != null) {
					String policyType = notePolicyVo.getPolicyType();
					if ("rejectAll".equals(policyType)) {
						failIdList.add(userId);
						failCount++;
						continue;
					}
					else if ("blackOnly".equals(policyType)) {
						if (noteManager.checkNotePolicyCondMe(mailUserSeq, condTarget)) {
							failIdList.add(userId);
							failCount++;
							continue;
						}
					} else if ("whiteOnly".equals(policyType)) {
						if (!noteManager.checkNotePolicyCondMe(mailUserSeq, condTarget)) {
							failIdList.add(userId);
							failCount++;
							continue;
						}
					}
				}
				
				userSeqMap.put(userId, condTarget);
				addrstr += addrTrim;
				
				if((i+1) != addrs.length) {			
					addrstr += ",";
				}
			}
		}
		
		resultMap.put("totalCount", totalCount);
		resultMap.put("failCount", failCount);
		resultMap.put("addr", addrstr);
		resultMap.put("failIdList", failIdList);
		
		return resultMap;
	}
	
	private MimeMessage[][] getMimeMessages(SenderInfoBean info, Session session) throws Exception {
		
		InternetAddress[] rcptto = info.getIasRcptto();
		MimeMessage[][] messages = new MimeMessage[2][rcptto.length];
		
		try {						
			MimeMessage tmpMime = null;
			TempMimeMessageBuilder builder = new TempMimeMessageBuilder();
			SendHandler sendHandler = new NoteSendHandler(session, info, null);
			MimeMessage sentMime = null;
			for (int i = 0; i < rcptto.length; i++) {					
				
				tmpMime = new MimeMessage(session);					
				builder.setFrom(tmpMime, info);				
				builder.setRecipient(tmpMime,rcptto[i]);				
				builder.setFlaged(tmpMime, info);					
				builder.setSendDate(tmpMime, info.getSendDate());					
				builder.setMessageHeader(tmpMime, info);
				builder.setSubject(tmpMime, info.getSubject(), info.getCharset());
				builder.setMessageContent(tmpMime, info);
				tmpMime.saveChanges();
				
				sentMime = new MimeMessage(tmpMime);
				
				String mid = tmpMime.getMessageID();
				if(mid.charAt(0) == '<' && mid.charAt(mid.length()-1) == '>') {
					mid = mid.substring(1, mid.length()-1);
				}
				info.setMessageId(noteManager.getMessageId(tmpMime));
				
				if(info.isRecvNoti()){
					info.setMdnStr(sendHandler.getMDNString(rcptto[i],"note"));
				} 
				
				builder.setMessageContent(tmpMime, info);
				tmpMime.saveChanges();
				tmpMime.setHeader("Message-ID", "<"+info.getMessageId()+">");
				sentMime.setHeader("Message-ID", "<"+info.getMessageId()+">");
				messages[0][i] = tmpMime;
				messages[1][i] = sentMime;
			}
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		return messages;
	}
}
