/**
 * MailSortMessageBean.java 2009. 1. 15.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.ibean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeUtility;

import org.alfresco.jlan.util.UTF8Normalizer;
import org.json.simple.JSONObject;

import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.mail.sort.SortMessage;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.MessageUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>MailSortMessageBean.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MailSortMessageBean {
	
	private SortMessage message = null;
	private long id = 0;	
	private String folderName = null;
	private String folderEncName = null;
	private String[] tagNames = null;
	private String preview = null;
	private String tagNameList = "";
	private String subject = null;
	private String from = null;	
	private String to = null;
	private String cc = null;
	private String size = null;	
	private String flag = null;
	private float spamRate = 0;
	private Locale locale = null;
	
	
	/**
	 * <p></p>
	 *
	 */
	public MailSortMessageBean(SortMessage message, Locale locale) throws MessagingException{
		this.message = message;
		this.id = message.getUid();
		this.folderName = message.getFolderName();
		this.folderEncName =  message.getFolderEncName();
		this.tagNames = message.getTagNames();
		this.tagNameList = "";
		this.subject = UTF8Normalizer.getInstance().normalize(message.getSubject());
		this.from = TMailAddress.getAddressString(message.getFrom());
		this.to = TMailAddress.getAddressString(message.getTo());
		this.size = FormatUtil.toUnitString(message.getSize(),1);
		this.flag = message.getFlagString();
		this.preview = message.getPreview();
		this.locale = locale;
		this.cc = TMailAddress.getAddressString(message.getCc());
	}

	/**
	 * @return message �� ��ȯ
	 */
	public SortMessage getMessage() {
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
	
	public String getFolderDepthName() {
		String dfname = null;
		int dotIdx = folderName.lastIndexOf("."); 
		if (dotIdx > 0) { 
			dfname = folderName.substring(dotIdx + 1, folderName.length()); 
		} else {
			dfname = folderName;
		}
		return dfname;
	}
	
	public String getFolderFullName() {
		String dfname = folderName.replaceAll("\\.", "/");		 
		return dfname;
	}
	
	/**
	 * @return folderName �� ��ȯ
	 */
	public String getFolderEncName() {
		return folderEncName;
	}

	/**
	 * @return tagNames �� ��ȯ
	 */
	public String[] getTagNames() {
		return tagNames;
	}
	
	/**
	 * @return tagNames �� ��ȯ
	 */
	public String getTagNameList() {
		if(tagNames != null){
			tagNameList = "";
			String tagName = null;
			for (int i = 0; i < tagNames.length; i++) {
				tagName = tagNames[i];
				tagName = (tagName != null)?tagName.replaceAll("[\t\n\r]", ""):"";
				tagName = (tagName != null)?tagName.replaceAll("null", ""):"";
				if(tagName != null && tagName.length() > 0){
					tagNameList += TMailUtility.IMAPFolderDecode(tagName);
					tagNameList += "|";					
				}
			}
		}
		
		return tagNameList;
	}
	
	public String getPreview(){
		
		if(preview != null){
			try {
				preview = MimeUtility.decodeText(preview);
				preview = preview.replaceAll("\\&nbsp;", " ");
				preview = preview.replaceAll(" ", "");
				//System.out.println("===["+preview+"]===");
			} catch (Exception e) {}
		}
		return preview;	
	}

	/**
	 * @return subject �� ��ȯ
	 */
	public String getSubject() {
		if(subject != null){
			subject = UTF8Normalizer.getInstance().normalize(subject);
			subject = StringUtils.getCRLFEscape(subject);
		}
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
	
	public String getFromName() {
		String name = "";
		try {
			name = message.getFrom().getPersonal();
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		return name;
	}
	
	public String getFromEmail() {
		String email = "";
		try {
			email = message.getFrom().getAddress();
		} catch (Exception e) {}
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
	
	public String getToName() {
		String name = "";
		try {
			name = message.getTo().getPersonal();
		} catch (Exception e) {}
		return name;
	}
	
	public String getToEmail() {
		String email = "";
		try {
			email = message.getTo().getAddress();
		} catch (Exception e) {}
		return email;
	}
	public String getCc() {
		return cc;
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
	
	public boolean isSeen(){
		boolean seen = false;
		if(flag.toLowerCase().indexOf("s") > -1){
			seen = true;
		}
		
		return seen;
	}
	
	public Date getInternalDate(){
		Date date = null;
		try {
        	date = message.getInternalDate();
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			System.out.println("-- Exception getReceivedDate3 : ["+e.getMessage()+"]");        	
		}
		
		return date;
	}
	
	public Date getEnvelopeDate(){
		Date date = null;
		try {
        	date = message.getEnvelopeDate();
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			System.out.println("-- Exception getReceivedDate3 : ["+e.getMessage()+"]");        	
		}
		
		return date;
	}
	
	/**
	 * <p>�޼��� ���� �ð��� ��ȯ. ����Ʈ�� ���.</p>
	 *
	 * @return
	 * @throws MessagingException
	 * @return String
	 */
	public String getDateForList() throws MessagingException{
		Date date1 = new Date();
        Date date2 = null;

		try {
        	date2 = message.getInternalDate();
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			System.out.println("-- Exception getReceivedDate3 : ["+e.getMessage()+"]");
        	return "";
		}


        if (date2 == null) {
            return null;
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

        int ms1 = Integer.parseInt(sdf2.format(date1));
        int ms2 = Integer.parseInt(sdf2.format(date2));
        
        String locStr = locale.getLanguage();
        String format = "";
        String dateStr = null;
        SimpleDateFormat sdf;
        //if("skin3".equals(skin)){
            format = "yy.MM.dd HH:mm";
           sdf = new SimpleDateFormat(format ,locale);
        /*}else{
            format = "(E) yyyy-MM-dd";
            if("ko".equalsIgnoreCase(locStr) || 
            		"ja".equals(locStr) || 
            		"jp".equals(locStr)){
            	format = "yyyy-MM-dd (E)";
            }
            sdf = new SimpleDateFormat((ms1 == ms2)? "a h:mm" : format ,locale);
	}*/
        
        dateStr = sdf.format(date2);
        format = null;
        locStr = null;
        sdf2 = null;
        sdf = null;
        return dateStr;
	}
	
	public String getDateForListCustom(String type, String format) throws MessagingException{
		Date date1 = new Date();
        Date date2 = null;

		try {
			if("sent".equals(type)){
				date2 = message.getEnvelopeDate();
			} else {
				date2 = message.getInternalDate();
			}
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			System.out.println("-- Exception getReceivedDate3 : ["+e.getMessage()+"]");
        	return "";
		}


        if (date2 == null) {
            return null;
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

        int ms1 = Integer.parseInt(sdf2.format(date1));
        int ms2 = Integer.parseInt(sdf2.format(date2));        
                
        String dateStr = null;
        SimpleDateFormat sdf =
            new SimpleDateFormat((ms1 == ms2)? "a h:mm" : format ,locale);
        
        dateStr = sdf.format(date2);
        format = null;        
        sdf2 = null;
        sdf = null;
        return dateStr;
	}
	
	/**
	 * <p>�޼��� ���� �ð��� ��ȯ. ����Ʈ�� ���.</p>
	 *
	 * @return
	 * @throws MessagingException
	 * @return String
	 */
	public String getDateForSentList() throws MessagingException{
		Date date1 = new Date();
        Date date2 = null;

		try {
			date2 = message.getEnvelopeDate();
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			System.out.println("-- Exception getReceivedDate3 : ["+e.getMessage()+"]");
        	return "";
		}


        if (date2 == null) {
            return null;
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

        int ms1 = Integer.parseInt(sdf2.format(date1));
        int ms2 = Integer.parseInt(sdf2.format(date2));
        
        String locStr = locale.getLanguage();
        String format = "";
        SimpleDateFormat sdf;
        String dateStr = null;
        
        //if("skin3".equals(skin)){
            format = "yy.MM.dd HH:mm";
            sdf = new SimpleDateFormat(format ,locale);
        /*}else{
            format = "(E) yyyy-MM-dd";
            
            if("ko".equalsIgnoreCase(locStr) || 
            		"ja".equals(locStr) || 
            		"jp".equals(locStr)){
            	format = "yyyy-MM-dd (E)";
            }
            
            sdf = new SimpleDateFormat((ms1 == ms2)? "a h:mm" : format ,locale);
        }*/
        
        dateStr = sdf.format(date2);
        format = null;
        locStr = null;
        sdf2 = null;
        sdf = null;

        return dateStr;
	}
	
	
	public int getPriority() throws MessagingException {
		try {
        	String[] values = message.getHeader("X-PRIORITY");

        	if (values != null) {
            	return Integer.parseInt(values[0]);
        	}
		} catch (NumberFormatException e) {
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			System.out.println("MessagingException getPriority : ["+e.getMessage()+"]");
		}

		return 3; // default priority
    }

	public float getSpamRate() {
		return spamRate;
	}

	public void setSpamRate(float spamRate) {
		this.spamRate = spamRate;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public boolean isCcMe(String email){
		if(this.cc != null){
			if(this.cc.indexOf(email)>-1){
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("all")
	public JSONObject toJson(String myEmail) throws MessagingException{
		JSONObject jobj = new JSONObject();		
		jobj.put("id",id);
		jobj.put("folderName",folderName);
		jobj.put("folderEncName",folderEncName);
		jobj.put("folderFullName",getFolderFullName());
		jobj.put("folderDepthName",getFolderDepthName());
		jobj.put("tagNameList",getTagNameList());
		jobj.put("subject",StringUtils.getCRLFEscapeOnly(subject));
		jobj.put("from",StringUtils.EscapeMailParam(from));
		jobj.put("fromEscape",StringUtils.EscapeMailParam(from));
		jobj.put("fromToSimple",StringUtils.EscapeMailParam(getFromSimple()));
		jobj.put("to",StringUtils.EscapeMailParam(to));
		jobj.put("toEscape",StringUtils.EscapeMailParam(to));
		jobj.put("isCcMe",isCcMe(myEmail));
		jobj.put("sendToSimple",StringUtils.EscapeMailParam(getSendToSimple()));
		jobj.put("size",size);
		jobj.put("byteSize",getByteSize());
		jobj.put("flag",getFlag());
		jobj.put("preview",getPreview());
		jobj.put("date", getDateForList());
		jobj.put("cdate", getDateForListCustom("normal","yyyy.MM.dd"));
		jobj.put("csdate", getDateForListCustom("sent","yyyy.MM.dd"));
		jobj.put("sentDate", getDateForSentList());
		jobj.put("seen", isSeen());
		jobj.put("priority", getPriority());
		jobj.put("spamRate", getSpamRate());		
		return jobj;
	}
	
	@SuppressWarnings("all")
	public JSONObject toJsonSimple() throws MessagingException{
		JSONObject jobj = new JSONObject();		
		jobj.put("id",id);
		jobj.put("folderFullName",getFolderFullName());
		jobj.put("subject",StringUtils.getCRLFEscape(subject));
		jobj.put("from",StringUtils.EscapeMailParam(from));
		jobj.put("flag",getFlag());		
		jobj.put("date", getDateForList());		
		return jobj;
	}
}
