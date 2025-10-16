/**
 * DownloadAddrAction.java 2008. 12. 8.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.addrbook.action;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.SocketException;
import java.util.List;
import java.util.Locale;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.manager.vendors.EmailVendorFactory;
import com.terracetech.tims.webmail.addrbook.manager.vendors.IEmailVendor;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p>
 * <strong>DownloadAddrAction.java</strong> Class Description
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
public class ExportAddrAction extends BaseAction {

	private static final long serialVersionUID = 20081208L;

	private AddressBookManager manager;

	private String leadingPattern;

	private int theGroupSeq;

	private int theBookSeq;
	
	private int theVendor;
	
	private String theEncoding;
	
	private InputStream inputStream;
	private int contentLength;
	private String contentDisposition;
	
	public void setLeadingPattern(String leadingPattern) {
		this.leadingPattern = leadingPattern;
	}

	public void setTheGroupSeq(int theGroupSeq) {
		this.theGroupSeq = theGroupSeq;
	}

	public void setTheBookSeq(int theBookSeq) {
		this.theBookSeq = theBookSeq;
	}

	public void setTheEncoding(String theEncoding) {
		this.theEncoding = theEncoding;
	}
	
	public void setTheVendor(int theVendor) {
		this.theVendor = theVendor;
	}

	public void setManager(AddressBookManager manager) {
		this.manager = manager;
	}

	public String execute() throws Exception {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		I18nResources resource = null;
		if(StringUtils.isEmpty(theEncoding)){
			Locale locale = new Locale(user.get(User.LOCALE));
			resource = new I18nResources(locale,"addr");
			if("jp".equals(user.get(User.LOCALE))){
				theEncoding = "shift-jis";
			}else if("ko".equals(user.get(User.LOCALE))){
				theEncoding = "euc-kr";
			}else{
			}
		}else{
			Locale locale = null;
			if("shift-jis".equals(theEncoding)){
				locale = new Locale("jp");
			}else if("euc-kr".equals(theEncoding)){
				locale = new Locale("ko");
			}else{
				locale = new Locale("en");
			}
			
			resource = new I18nResources(locale,"addr");
		}
		
		List<AddressBookMemberVO> members = null;
		if(theBookSeq==0){
			members = manager.readPrivateMemberListByIndex(userSeq, theGroupSeq, leadingPattern, 1, 900000, "", "");
		}else{
			members = manager.readSharedMemberListByIndex(theBookSeq, theGroupSeq, leadingPattern, 1, 900000, "", "");
		}
		
		IEmailVendor vendor = EmailVendorFactory.getEmailVendor(resource, theEncoding, theVendor);
		StringBuffer result = vendor.getAddrCSVDownload(members);
		
		Reader in = new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes("utf-8")), "utf-8");
		Writer out = new OutputStreamWriter(new BufferedOutputStream(response.getOutputStream()), theEncoding);
		
		String fileName = "Addr_"+theEncoding+"_"+FormatUtil.getBasicDateStr()+".csv";
		response.setCharacterEncoding(theEncoding);
		response.setHeader("Content-Type","application/download; name=\"" + fileName + "\"");
		response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
		
		if ("utf-8".equalsIgnoreCase(theEncoding)) {
			out.write(65279);
		}
		
		try {
			int c;
			 while ((c = in.read()) != -1){
		            out.write(c);
		     }
		} catch (Exception e) {
			if(e.getCause() instanceof SocketException){
			}else{
				LogManager.writeErr(this, e.getMessage(), e);	
			}
		}finally{
			in.close();
			out.close();
		}

		return null;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public String getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}
	
}
