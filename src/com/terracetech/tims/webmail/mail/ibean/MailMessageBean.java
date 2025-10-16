/**
 * MailMessageBean.java 2008. 11. 27.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.ibean;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.mail.TMailMDNResponse;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailTnefAttach;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.MessageUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>MailMessageBean.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li>���� �޼��� ������ ���� �ϰ� �ڵ鸵 �ϱ����� ���� ���� Ŭ����</li>
 * <li>JSONObject�� ��ȯ �ϴ� ���</li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MailMessageBean {
	
	private TMailMessage message = null;
	private long id = 0;	
	private String folderName = null;	
	private String subject = null;
	private String from = null;	
	private String to = null;
	private String size = null;	
	private String flag = null;
	
	private boolean hiddenImg = false;
	private boolean hiddenTag = false;
	private boolean mdnCheck = false;
	private TMailPart[] bodyContent = null;
	private TMailPart[] attachContent = null;
	private TMailPart[] vcardContent = null;
	private String[] imageContent = null;
	
    
    private InternetAddress[] addrTo = null;
    private InternetAddress[] addrCc = null;		
    private InternetAddress[] addrBcc = null;		
	/**
	 * <p>�ʱ� ����. �޼����� �޾� ���ο� ����</p>
	 *
	 * @param message				���� �޼���
	 * @throws MessagingException
	 */
	public MailMessageBean(TMailMessage message) throws MessagingException{
		this.message = message;		
	}
	
	/**
	 * @return message �� ��ȯ
	 */
	public TMailMessage getMessage(){
		return message;
	}
		
	/**
	 * @return id �� ��ȯ
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @return folderName �� ��ȯ
	 */
	public String getFolderName() {
		return folderName;
	}
	
	/**
	 * @return subject �� ��ȯ
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * @return from �� ��ȯ
	 */
	public String getFrom() {
		return from;
	}
	
	public String getFromSimple() {
		return MessageUtil.getSimpleAddress(from, 0);
	}
	
	public String getFromEmail() {
		String email = "";
		try {
			email = message.getFromAddress();
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		return email;
	}
	
	/**
	 * @return to �� ��ȯ
	 */
	public String getTo() {
		return to;
	}
	
	public String getSendToSimple() {		
		return MessageUtil.getSimpleAddress(to, 0);
	}
	
	/**
	 * @return size �� ��ȯ
	 */
	public String getSize() {
		return size;
	}
	
	public int getByteSize() throws MessagingException{
		return message.getSize();
	}
	
	/**
	 * @return flag �� ��ȯ
	 */
	public String getFlag() {
		return flag;
	}	
	
	/**
	 * @return bodyContent �� ��ȯ
	 */
	public TMailPart[] getBodyContent() {
		return bodyContent;
	}

	/**
	 * @param bodyContent �Ķ���͸� bodyContent���� ����
	 */
	public void setBodyContent(TMailPart[] bodyContent) {
		this.bodyContent = bodyContent;
	}

	/**
	 *  <p>�޼����� ÷�� �κ��� Part�� ��ȯ</p>
	 * 
	 * @return attachContent �� ��ȯ
	 */
	public TMailPart[] getAttachContent() {
		return attachContent;
	}

	/**
	 * @param attachContent �Ķ���͸� attachContent���� ����
	 */
	public void setAttachContent(TMailPart[] attachContent) {
		this.attachContent = attachContent;
	}

	/**
	 *  <p>�޼����� vcard �κ��� Part�� ��ȯ</p>
	 * 
	 * @return vcardContent �� ��ȯ
	 */
	public TMailPart[] getVcardContent() {
		return vcardContent;
	}

	/**
	 * @param vcardContent �Ķ���͸� vcardContent���� ����
	 */
	public void setVcardContent(TMailPart[] vcardContent) {
		this.vcardContent = vcardContent;
	}


	public String[] getImageContent() {
		return imageContent;
	}

	public void setImageContent(String[] imageContent) {
		this.imageContent = imageContent;
	}

	/**
	 * <p>�޼��� ���� �ð��� ��ȯ. �б�� ���.</p>
	 *
	 * 
	 * @throws MessagingException
	 * @return String
	 */
	public String getSendDateForRead() throws MessagingException{
		return message.getSentDateForRead();
	}
	
	/**
	 * <p>�޼��� ���� �ð��� ��ȯ. ����Ʈ ��½� ���.</p>
	 *
	 * @return
	 * @throws MessagingException
	 * @return String
	 */
	public String getSendDateForList() throws MessagingException{
		return message.getSentDateForList();
	}
	
	/**
	 * <p>�޼��� ���� �ð��� ��ȯ. �б�� ���.</p>
	 *
	 * @return
	 * @throws MessagingException
	 * @return String
	 */
	public String getReceivedDateForRead() throws MessagingException{
		return message.getReceivedDateForRead();
	}
	
	/**
	 * <p>�޼��� ���� �ð��� ��ȯ. ����Ʈ�� ���.</p>
	 *
	 * @return
	 * @throws MessagingException
	 * @return String
	 */
	public String getReceivedDateForList() throws MessagingException{
		return message.getReceivedDateForList();
	}
	
	/**
	 * @return hiddenImg �� ��ȯ
	 */
	public boolean isHiddenImg() {
		return hiddenImg;
	}

	/**
	 * @param hiddenImg �Ķ���͸� hiddenImg���� ����
	 */
	public void setHiddenImg(boolean hiddenImg) {
		this.hiddenImg = hiddenImg;
	}

	/**
	 * @return hiddenTag �� ��ȯ
	 */
	public boolean isHiddenTag() {
		return hiddenTag;
	}

	/**
	 * @param hiddenTag �Ķ���͸� hiddenTag���� ����
	 */
	public void setHiddenTag(boolean hiddenTag) {
		this.hiddenTag = hiddenTag;
	}
	
	public int getPriority() throws MessagingException{
		return message.getPriority();
	}
	
	/**
	 * @return mdnCheck �� ��ȯ
	 */
	public boolean isMdnCheck() {
		return mdnCheck;
	}

	/**
	 * @param mdnCheck �Ķ���͸� mdnCheck���� ����
	 */
	public void setMdnCheck(boolean mdnCheck) {
		this.mdnCheck = mdnCheck;
	}
	
	public int getMDNReadCount(){
		return message.getMDNReadCount();
	}
	
	
	public int getMDNCount(){
		return message.getMDNResponseCount();
	}
	
	public TMailMDNResponse[] getMDNResponses(){
		return message.getMDNResponses();
	}
	
	public String getMessageID() throws MessagingException{
		return message.getMessageID();		
	}

	/**
	 * <p>�޼����� ���� �κ��� Part�� ��ȯ</p>
	 *
	 * @param bodyContents		message�� ���� body part �迭
	 * @return void
	 */
	public void setBodyContents(TMailPart[] bodyContents){
		this.bodyContent = bodyContents;		
	}
	
	public boolean isSeen(){
		boolean seen = false;
		if(flag.toLowerCase().indexOf("s") > -1){
			seen = true;
		}
		
		return seen;
	}
	public InternetAddress[] getAddrTo() {
		return addrTo;
	}

	public void setAddrTo(InternetAddress[] addrTo) {
		this.addrTo = addrTo;
	}

	public InternetAddress[] getAddrCc() {
		return addrCc;
	}

	public void setAddrCc(InternetAddress[] addrCc) {
		this.addrCc = addrCc;
	}

	public InternetAddress[] getAddrBcc() {
		return addrBcc;
	}

	public void setAddrBcc(InternetAddress[] addrBcc) {
		this.addrBcc = addrBcc;
	}
	/**
	 * <p>List ��½� message������ ����.</p>
	 *
	 * @throws MessagingException
	 * @return void
	 */
	public void setListParam() throws MessagingException{
		this.id = message.getUid();
		this.folderName = message.getFolderEncName();
		this.subject = message.getSubject();
		this.from = message.getFromString();		
		this.to = message.getToString();		
		this.size = FormatUtil.toUnitString(message.getSize(), 2);			
		this.flag = message.getFlagsString(); 
	}
	
	/**
	 * <p>Read ��½� message������ ����.</p>
	 *
	 * @throws MessagingException
	 * @return void
	 */
	public void setReadParam() throws MessagingException{
		this.id = message.getUid();
		this.folderName = message.getFolderEncName();
		this.subject = message.getSubject();
		this.from = message.getFromString();
		this.size = FormatUtil.toUnitString(message.getSize(), 2);			
		this.flag = message.getFlagsString(); 
	}

	/**
	 * <p>List�� Json Object ��ȯ</p>
	 * 
	 * @throws MessagingException
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJsonForList() throws MessagingException{
		JSONObject jObj = new JSONObject();
		jObj.put("id",id);
		setJsonEscapeValue(jObj,"subject", subject);
		
		setJsonEscapeValue(jObj,"from", from);
		jObj.put("fromEncode", StringUtils.UTF8URLEncode(from));
		setJsonEscapeValue(jObj,"fromText", MessageUtil.getSimpleAddress(from, 0));		
		
		setJsonEscapeValue(jObj,"to", to);
		jObj.put("toEncode", StringUtils.UTF8URLEncode(to));
		setJsonEscapeValue(jObj,"toText", MessageUtil.getSimpleAddress(to, 0));
		
		jObj.put("size", size);
		jObj.put("sendDate", message.getSentDateForList());
		jObj.put("receivedDate", message.getReceivedDateForList());
		jObj.put("flag", flag);		
		return jObj;		
	}
	
	/**
	 * <p>Read�� Json Object ��ȯ</p>
	 *
	 * 
	 * @throws Exception
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJsonForRead() throws Exception{
		JSONObject jObj = new JSONObject();
		jObj.put("uid",message.getUid());
		jObj.put("subject", StringUtils.getCRLFEscapeOnly(message.getSubject()));
		jObj.put("folderFullName",message.getFolderFullName());
		jObj.put("folderEncName",message.getFolderEncName());
		jObj.put("size",message.getSize());
		
		setJsonEscapeValue(jObj,"fromHidden",message.getFromAddress());
		setJsonEscapeValue(jObj,"toHidden",message.getToString1());
		setJsonEscapeValue(jObj,"from",message.getFromString());		
		jObj.put("date", message.getSentDateForRead());
		
		JSONArray addrToList = new JSONArray();		
        setJsonListOfAddr(addrToList, addrTo);
		jObj.put("toList", addrToList);
		
		JSONArray addrCcList = new JSONArray();
        setJsonListOfAddr(addrCcList, addrCc);
		jObj.put("ccList", addrCcList);
		
		JSONArray addrBccList = new JSONArray();
        setJsonListOfAddr(addrBccList, addrBcc);
		jObj.put("bccList", addrBccList);
		
		jObj.put("differentCharset", message.isDifferentCharset());
		
		JSONArray vcardList = new JSONArray();		
		if(vcardContent != null){
			for (int i = 0; i < vcardContent.length; i++) {
				JSONObject vcardObj = new JSONObject();
				vcardObj.put("path", vcardContent[i].getPath());
				vcardObj.put("size", FormatUtil.toUnitString(vcardContent[i].getSize75()));
				vcardList.add(vcardObj);
			}			
		}
		jObj.put("vcardList",vcardList);
		
		JSONArray attachList = new JSONArray();
		JSONObject atachObj = null;
		if(attachContent != null){
			for (int i = 0; i < attachContent.length; i++) {
				atachObj = new JSONObject();
				atachObj.put("path", attachContent[i].getPath());
				atachObj.put("fileName", attachContent[i].getFileName());
				atachObj.put("fileType", 
						FormatUtil.getFileFormat(attachContent[i].getFileName()).toLowerCase());
				atachObj.put("size", attachContent[i].getSize75());
				atachObj.put("fsize", FormatUtil.toUnitString(attachContent[i].getSize75()));
				atachObj.put("isTnef",attachContent[i].isTnefType());
				if(attachContent[i].isTnefType()){
					TMailTnefAttach[] tnefAttach = attachContent[i].getTnefFiles();
					JSONArray tnefAttachList = new JSONArray();
					JSONObject tnefObj = null;
					if(tnefAttach != null){						
						for (int j = 0; j < tnefAttach.length; j++) {
							tnefObj = new JSONObject();
							tnefObj.put("attachKey", tnefAttach[j].getAttachKey());
							tnefObj.put("fileName", tnefAttach[j].getFileName());
							tnefAttachList.add(tnefObj);
						}
						atachObj.put("tnefList",tnefAttachList);
						tnefAttachList = null;
						tnefObj = null;
					}					
				}
				attachList.add(atachObj);
			}
		}
		jObj.put("attachList",attachList);
		
		JSONArray imgList = new JSONArray();		
		if(imageContent != null){
			for (int i = 0; i < imageContent.length; i++) {
				if(imageContent[i] != null){
					imgList.add(imageContent[i]);
				}
			}
		}
		jObj.put("imgList",imgList);		
		return jObj;		
	}
	
	/**
	 * <p>Body Content part�� JSon���� ��ȯ.</p>
	 *
	 * @throws Exception
	 * @return JSONArray
	 */
	@SuppressWarnings("unchecked")
	private JSONArray getJsonParseContent() throws Exception{
		JSONArray bodyList = new JSONArray();
		for (int i = 0; i < bodyContent.length; i++) {
			JSONObject content = new JSONObject();
			if(bodyContent[i].isMessageRFC822()){
				content.put("rfc822","true");
				setJsonEscapeValue(content,"subject", bodyContent[i].getSubject());
				
				JSONArray addrList = new JSONArray();		
				setJsonListOfAddr(addrList, bodyContent[i].getFrom());		
				content.put("fromList", addrList);
				
				addrList.clear();		
				setJsonListOfAddr(addrList, bodyContent[i].getTo());		
				content.put("toList", addrList);
				
				addrList.clear();		
				setJsonListOfAddr(addrList, bodyContent[i].getCc());		
				content.put("ccList", addrList);
				bodyList.add(content);				
			} else {
				content.put("rfc822","false");
				setJsonEscapeValue(content,"content",bodyContent[i].getText());
				bodyList.add(content);
			}
		}
		
		return bodyList;
	}	
	
	
	/**
	 * <p>JSON ��� HTM �±� Ư������ ġȯ.</p>
	 *
	 * @param jObj
	 * @param key
	 * @param value
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	private void setJsonEscapeValue(JSONObject jObj, String key, String value){
		jObj.put(key, StringUtils.EscapeHTMLTag(value));
	}
	
	/**
	 * <p>�ּ� Part�� JSON List �� ��ȯ</p>
	 *
	 * @param array
	 * @param addrs
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	private void setJsonListOfAddr(JSONArray array, InternetAddress[] addrs){
		if(addrs != null){
			
			for (int i = 0; i < addrs.length; i++) {
				JSONObject addrObj = new JSONObject();
				if(addrs[i].getPersonal() != null){
					setJsonEscapeValue(addrObj,"personal",addrs[i].getPersonal());
					setJsonEscapeValue(addrObj,"address",addrs[i].getAddress());
				} else {
					addrObj.put("personal", "");
					setJsonEscapeValue(addrObj,"address",addrs[i].getAddress());
				}
				array.add(addrObj);				
				
			}
		}		
	}
}
