/**
 * SenderInfo.java 2008. 12. 1.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.ibean;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import javax.mail.internet.InternetAddress;

import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.manager.LadminManager;
import com.terracetech.tims.webmail.common.vo.BannerVO;
import com.terracetech.tims.webmail.mail.vo.LetterVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.vo.SignDataVO;

/**
 * <p>
 * <strong>SenderInfo.java</strong> Class Description
 * </p>
 * <p>
 * 주요설명
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
@SuppressWarnings("unchecked")
public class SenderInfoBean {
	
	private boolean isSendNormal = false;	
	private boolean isSendDraft = false;
	private boolean isReserved = false;
	
	private boolean isForward= false;
	private boolean isForwardAttached = false;
	private boolean isReply = false;
	private boolean isWebfolder = false;
	
	
	private boolean isOneSend = false;
	private boolean isSaveSent = false;
	private boolean isAttachVcard = false;
	private boolean isAttachSign = false;
	private boolean isHtmlMode = false;	
	
	private boolean isAutoSave = false;
	
	private boolean isSecureMail = false;
	private MailSecureInfoBean secureInfo = null;	 
	
	private String folder = null;
	
	private String uids = null;	

	private String charset = null;

	private String priority = null;
	
	private String to = null;

	private String cc = null;

	private String bcc = null;
	
	private String subject = null;
	
	private String content = null;
	
	private String bodyContent = null;

	private String rcptto = null;

	private String from = null;

	private String replyto = null;

	private String senderName = null;
	private String senderEmail = null;
	
	private boolean isRecvNoti;

	private InternetAddress[] iasTo = null;
	private InternetAddress[] iasCc = null;
	private InternetAddress[] iasBcc = null;
	private InternetAddress[] iasRcptto = null;
	
	private String mdnStr = "";
	
	private String attdir;
	
	private String attlist;
	
	private User user;
	
	private Date sentDate;
	
	private TMailMessage[] flagMessages;
	
	private String vcard;
	
	private Hashtable<String, String> image2cid= null;
	
	private String reservYear = null;
	
	private String reservMonth = null;
	
	private String reservDay = null;
	
	private String reservHour = null;
	
	private String reservMin = null;
	
	private String reservDateStr = null;
	
	private int maxRcpt = 0;
	
	private boolean isAllowMaxRcpts = false;
	
	private String messageId = null;
	
	private String replyMid = null;
	
	private String draftMid = null;
	
	private String trashMid = null;
	
	private String localUrl = null;
	
	private String mdnUrl = null;
	
	private boolean letterPaperMode = false;
	
	private LetterVO letterPaper = null;
	
	private SignDataVO signData = null;
	
	private boolean isBigAttach = false;
	private String[] bigAttachLink = null;	
	
	private String cryptMethod = "AES";	
	
	private LadminManager ladminManager = null;
	
	private Stack stack = new Stack();
	
	private BannerVO bennerInfo = null;
	
	private String remoteIp = null;
	
	private String secureMailModule = null;
	
	private String bigAttachMailContents = null;

	public void setSendType(String sendType){
		if(sendType != null){
			isSendNormal = sendType.equals("normal");
			isSendDraft = sendType.equals("draft");
			isReserved = sendType.equals("reserved");
		}
	}
	
	public boolean isSendNormal(){
		return isSendNormal;		
	}
	
	public boolean isSendDraft(){
		return isSendDraft;
	}
	
	public boolean isReserved(){
		return isReserved;
	}
	
	public void setSendFlag(String flagType){
		if(flagType != null){
			isForward = flagType.equalsIgnoreCase("forward");
			isForwardAttached = flagType.equalsIgnoreCase("forwardAttached");
			isReply = flagType.equalsIgnoreCase("reply") || flagType.equalsIgnoreCase("replyall");			
			isWebfolder = flagType.equalsIgnoreCase("wfolder");
		}
	}
	
	
	public boolean isForward() {
		return isForward;
	}
	
	public boolean isForwardAttached() {
		return isForwardAttached;
	}
	
	public boolean isReply() {
		return isReply;
	}
	
	public boolean isWebfolder() {
		return isWebfolder;
	}
	
	public boolean isMultiPart(){
		boolean isMultiPart = false;
		if(isAttachVcard 
				|| isForwardAttached
				|| attlist.length() > 0 							
				|| image2cid.size() > 0){			
			isMultiPart = true;
		}		
		return isMultiPart;		
	}
	
	public void setAllowMaxRcpts(boolean isAllowMaxRcpts){
		this.isAllowMaxRcpts = isAllowMaxRcpts;
	}
	
	public boolean isAllowMaxRcpts() {
		return isAllowMaxRcpts;
	}
	
	public void setMaxRcpt(int maxRcpt){
		this.maxRcpt = maxRcpt;
	}
	
	public int getMaxRcpt() {
		return maxRcpt;
	}
	
	public boolean isBatchSend(){
		boolean isBatchSend = false;		
		if(iasRcptto != null && maxRcpt < iasRcptto.length && isAllowMaxRcpts){
			isBatchSend =  true;
		}		
		return isBatchSend;
	}

	/**
	 * @return forwardFolder 값 반환
	 */
	public String getFolder() {
		return folder;
	}

	/**
	 * @param forwardFolder 파라미터를 forwardFolder값에 설정
	 */
	public void setFolder(String folder) {
		this.folder = folder;
	}

	/**
	 * @return forwardUids 값 반환
	 */
	public String getUids() {
		return uids;
	}

	/**
	 * @param forwardUids 파라미터를 forwardUids값에 설정
	 */
	public void setUids(String uids) {
		this.uids = uids;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}	

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBodyContent() {
		return bodyContent;
	}

	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}

	public String getRcptto() {
		return rcptto;
	}

	public void setRcptto(String rcptto) {
		this.rcptto = rcptto;
	}

	/**
	 * @return reservYear 값 반환
	 */
	public String getReservYear() {
		return reservYear;
	}

	/**
	 * @param reservYear 파라미터를 reservYear값에 설정
	 */
	public void setReservYear(String reservYear) {
		this.reservYear = reservYear;
	}

	/**
	 * @return reservMonth 값 반환
	 */
	public String getReservMonth() {
		return reservMonth;
	}

	/**
	 * @param reservMonth 파라미터를 reservMonth값에 설정
	 */
	public void setReservMonth(String reservMonth) {
		this.reservMonth = reservMonth;
	}

	/**
	 * @return reservDay 값 반환
	 */
	public String getReservDay() {
		return reservDay;
	}

	/**
	 * @param reservDay 파라미터를 reservDay값에 설정
	 */
	public void setReservDay(String reservDay) {
		this.reservDay = reservDay;
	}

	/**
	 * @return reservHour 값 반환
	 */
	public String getReservHour() {
		return reservHour;
	}

	/**
	 * @param reservHour 파라미터를 reservHour값에 설정
	 */
	public void setReservHour(String reservHour) {
		this.reservHour = reservHour;
	}

	/**
	 * @return reservMin 값 반환
	 */
	public String getReservMin() {
		return reservMin;
	}

	/**
	 * @param reservMin 파라미터를 reservMin값에 설정
	 */
	public void setReservMin(String reservMin) {
		this.reservMin = reservMin;
	}
	
	
	public Date getSendDate() throws Exception{
		if(sentDate != null){
			return sentDate;			
		}
		
		if(isReserved){
			String[] monthData={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
			int in_month=Integer.parseInt(reservMonth);

			reservDateStr = 
				reservDay+"-"+monthData[in_month-1]+"-"+
				reservYear+" "+reservHour+":"+reservMin+":00 +0900";			
			
			SimpleDateFormat format =
				new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss Z", Locale.US);
			sentDate = format.parse(reservDateStr);

		} else {
			sentDate = new Date();			
		}
		
		return sentDate;
	}
	
	public String getReservDateStr(){
		return reservDateStr;
	}

	/**
	 * @return tempFiles 값 반환
	 */
	public List<File> getTempFiles() {
		return tempFiles;
	}

	/**
	 * @param tempFiles 파라미터를 tempFiles값에 설정
	 */
	public void setTempFiles(List<File> tempFiles) {
		this.tempFiles = tempFiles;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSenderName() {
		senderName = (senderName != null)?senderName:user.get(User.USER_NAME);
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	public String getSenderEmail() {
		senderEmail = (senderEmail != null)?senderEmail:user.get(User.EMAIL);
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getReplyto() {
		return replyto;
	}

	public void setReplyto(String replyto) {
		this.replyto = replyto;
	}

	public InternetAddress[] getIasTo() {
		return iasTo;
	}

	public void setIasTo(InternetAddress[] iasTo) {
		this.iasTo = iasTo;
	}

	public InternetAddress[] getIasCc() {
		return iasCc;
	}

	public void setIasCc(InternetAddress[] iasCc) {
		this.iasCc = iasCc;
	}

	public InternetAddress[] getIasBcc() {
		return iasBcc;
	}

	public void setIasBcc(InternetAddress[] iasBcc) {
		this.iasBcc = iasBcc;
	}

	public InternetAddress[] getIasRcptto() {
		return iasRcptto;
	}

	public void setIasRcptto(InternetAddress[] iasRcptto) {
		this.iasRcptto = iasRcptto;
	}

	public String getMdnStr() {		
		return (mdnStr != null)?mdnStr:"";
	}

	public void setMdnStr(String mdnStr) {
		this.mdnStr = mdnStr;
	}

	public String getUserEmail() {
		return user.get(User.EMAIL);
	}

	public String getAttdir() {
		return attdir;
	}

	public void setAttdir(String attdir) {
		this.attdir = attdir;
	}

	public boolean isRecvNoti() {
		return isRecvNoti;
	}

	public void setRecvNoti(boolean isRecvNoti) {
		this.isRecvNoti = (isSendDraft)?false:isRecvNoti;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isOneSend() {
		return isOneSend;
	}

	public void setOneSend(boolean isOneSend) {
		this.isOneSend = (isSendDraft)?false:isOneSend;
	}
	
	public boolean isSaveSent() {		
		return isSaveSent;
	}

	public void setSaveSent(boolean isSaveSent) {	
		this.isSaveSent = (isSendDraft)?false:isSaveSent;
	}
	
	public boolean isAttachVcard() {
		return isAttachVcard;
	}

	public void setAttachVcard(boolean isAttachVcard) {
		this.isAttachVcard = (isSendDraft)?false:isAttachVcard;
	}
	
	public boolean isAttachSign() {
		return isAttachSign;
	}

	public void setAttachSign(boolean isAttachSign) {
		this.isAttachSign = (isSendDraft)?false:isAttachSign;
	}
	
	public boolean isHtmlMode() {
		return isHtmlMode;
	}

	public void setHtmlMode(boolean isHtmlMode) {
		this.isHtmlMode = isHtmlMode;
	}	

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public String getAttlist() {
		return attlist;
	}

	public void setAttlist(String attlist) {
		this.attlist = attlist;
	}

	public TMailMessage[] getFlagMessages() {
		return flagMessages;
	}

	public void setFlagMessages(TMailMessage[] flagMessages) {
		this.flagMessages = flagMessages;
	}

	public String getVcard() {
		return vcard;
	}

	public void setVcard(String vcard) {
		this.vcard = vcard;
	}

	public Hashtable<String, String> getImage2cid() {
		return image2cid;
	}

	public void setImage2cid(Hashtable<String, String> image2cid) {
		this.image2cid = image2cid;
	}
	
	/**
	 * @return messageId 값 반환
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId 파라미터를 messageId값에 설정
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return replyMid 값 반환
	 */
	public String getReplyMid() {
		return replyMid;
	}

	/**
	 * @param replyMid 파라미터를 replyMid값에 설정
	 */
	public void setReplyMid(String replyMid) {
		this.replyMid = replyMid;
	}

	/**
	 * @return draftMid 값 반환
	 */
	public String getDraftMid() {
		if(draftMid != null){
			draftMid = draftMid.replaceAll("<", "");
			draftMid = draftMid.replaceAll(">", "");
		}
		return draftMid;
	}

	/**
	 * @param draftMid 파라미터를 draftMid값에 설정
	 */
	public void setDraftMid(String draftMid) {
		this.draftMid = draftMid;
	}

	public String getTrashMid() {
		return trashMid;
	}

	public void setTrashMid(String trashMid) {
		this.trashMid = trashMid;
	}

	/**
	 * @return localUrl 값 반환
	 */
	public String getLocalUrl() {
		return localUrl;
	}

	/**
	 * @param localUrl 파라미터를 localUrl값에 설정
	 */
	public void setLocalUrl(String localUrl) {
		this.localUrl = localUrl;
	}

	/**
	 * @return mdnUrl 값 반환
	 */
	public String getMdnUrl() {
		return mdnUrl;
	}

	/**
	 * @param mdnUrl 파라미터를 mdnUrl값에 설정
	 */
	public void setMdnUrl(String mdnUrl) {
		this.mdnUrl = mdnUrl;
	}

	private List<File> tempFiles = new ArrayList<File>();

	public void addTempFile(File f) {
		tempFiles.add(f);
	}
	
	
	public void pushStack(Object item){
		stack.push(item);		
	}
	/**
	 * @return partStack 값 반환
	 */
	public Stack getStack() {
		return stack;
	}
	
	public void clearStack() {
		stack.clear();
	}

	/**
	 * @return letterPaper 값 반환
	 */
	public LetterVO getLetterPaper() {
		return letterPaper;
	}

	/**
	 * @param letterPaper 파라미터를 letterPaper값에 설정
	 */
	public void setLetterPaper(LetterVO letterPaper) {
		this.letterPaper = letterPaper;
	}

	/**
	 * @return letterPaperMode 값 반환
	 */
	public boolean isLetterPaperMode() {
		return letterPaperMode;
	}

	/**
	 * @param letterPaperMode 파라미터를 letterPaperMode값에 설정
	 */
	public void setLetterPaperMode(String letterPaperMode) {		
		this.letterPaperMode = (letterPaperMode != null && letterPaperMode.equals("on"))?true:false;
	}

	/**
	 * @return isAutoSave 값 반환
	 */
	public boolean isAutoSave() {
		return isAutoSave;
	}

	/**
	 * @param isAutoSave 파라미터를 isAutoSave값에 설정
	 */
	public void setAutoSave(boolean isAutoSave) {
		this.isAutoSave = isAutoSave;
	}

	/**
	 * @return signSeq 값 반환
	 */
	public SignDataVO getSignData() {
		return signData;
	}

	/**
	 * @param signSeq 파라미터를 signSeq값에 설정
	 */
	public void setSignData(SignDataVO signData) {
		this.signData = signData;
	}

	public LadminManager getLadminManager() {
		return ladminManager;
	}

	public void setLadminManager(LadminManager ladminManager) {
		this.ladminManager = ladminManager;
	}

	public boolean isSecureMail() {
		return isSecureMail;
	}

	public void setSecureMail(boolean isSecureMail) {
		this.isSecureMail = isSecureMail;
	}

	public MailSecureInfoBean getSecureInfo() {
		return secureInfo;
	}

	public void setSecureInfo(MailSecureInfoBean secureInfo) {
		this.secureInfo = secureInfo;
	}

	public boolean isBigAttach() {
		return isBigAttach;
	}

	public void setBigAttach(boolean isBigAttach) {
		this.isBigAttach = isBigAttach;
	}

	public String[] getBigAttachLink() {
		return bigAttachLink;
	}

	public void setBigAttachLink(String[] bigAttachLink) {
		this.bigAttachLink = bigAttachLink;
	}

	public String getCryptMethod() {
		return cryptMethod;
	}

	public void setCryptMethod(String cryptMethod) {
		this.cryptMethod = cryptMethod;
	}

	public BannerVO getBennerInfo() {
		return bennerInfo;
	}

	public void setBennerInfo(BannerVO bennerInfo) {
		this.bennerInfo = bennerInfo;
	}

	public void dispose() {
		for (File f : tempFiles) {
			if (f.exists()) {
				try {
					f.delete();
				} catch (Exception ignore) {
				}
			}
		}

	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public String getSecureMailModule() {
		return secureMailModule;
	}

	public void setSecureMailModule(String secureMailModule) {
		this.secureMailModule = secureMailModule;
	}
	
	public boolean isEtcSecureMail(){
		return (isSecureMail && !EnvConstants.S_MAIL_DEFAULT.equals(secureMailModule)); 
	}

	public String getBigAttachMailContents() {
		return bigAttachMailContents;
	}

	public void setBigAttachMailContents(String bigAttachMailContents) {
		this.bigAttachMailContents = bigAttachMailContents;
	}
	
}
