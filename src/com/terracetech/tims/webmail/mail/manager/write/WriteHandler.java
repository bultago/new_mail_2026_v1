/**
 * WriteHandler.java 2009. 2. 20.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.write;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.eclipse.angus.mail.imap.IMAPMessage;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mail.ibean.MailWriteMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageWriteInfoBean;
import com.terracetech.tims.webmail.mail.manager.Cid2Image;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.util.Validation;

/**
 * <p><strong>WriteHandler.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public abstract class WriteHandler {
	
	protected String tmpPath = EnvConstants.getBasicSetting("tmpdir");
	protected String attachUrl = EnvConstants.getAttachSetting("attach.url");
	protected MessageWriteInfoBean writeInfo = null;		
	
	public abstract MailWriteMessageBean getWriteMessageBean(
			MessageWriteInfoBean writeInfo,
			TMailStore store, User user, 
			I18nResources msgResource) throws Exception;
	
	
	public String parseAttachListStr(String[][] attaches){
		String attacheString = "";
		if(attaches != null) {
			int cnt = 0;
			for(int i = 0; i < attaches.length; i++) {
				if (!Validation.isNull(attaches[i][0])) {
					attacheString += "AttachList["+cnt+"] = new Array(\""
						+attaches[i][0]+"\", \""+attaches[i][1]+"\", "+attaches[i][2]+");\n";
						cnt++;
				}
			}
		}		
		return attacheString;
	}
	
	public String getContentStringTextHtml(TMailMessage message, String type, String filePath, String imgUrl) 
	throws MessagingException {
		return getContentStringTextHtml(false, false, message, type, filePath, imgUrl);
		
	}
	
	public String getContentStringTextHtml(
			boolean isMobileMode,
			boolean isSignInside, 
			TMailMessage message, 
			String type, String filePath, String imgUrl) 
	throws MessagingException {
		if(type == null) {
			return "";
		}
	
		boolean isDrafts = type.equals("drafts");
		boolean isRewrite = type.equals("rewrite");
	
		InternetAddress[] ifrom = message.getFrom();
		InternetAddress[] ito = message.getTo();
		InternetAddress[] icc = message.getCc();
	
		String from = TMailAddress.getAddressString(ifrom);
		String to = TMailAddress.getAddressString(ito);
		String date = message.getSentDateForRead();
		String subject = message.getSubject();
	
		try {
			TMailPart[] textParts 	= message.getTextContent(TMailMessage.TEXT_HTML);
			TMailPart[] imageParts = message.getAttachImagesNCid();			
			TMailPart[] texts = null;
		
			Cid2Image c2i = new Cid2Image(imageParts,textParts,filePath,imgUrl,null);
			c2i.parseCidImage();
			texts = c2i.getTextParts();		
	
			StringBuffer newtext = new StringBuffer();
	
			if(!isDrafts && !isRewrite) {
				if(isSignInside){					
					newtext.append("<br><br><br><br><div style='margin-top:15px;'><signpos></signpos></div><BLOCKQUOTE style='PADDING-RIGHT: 0px; "
							 +"PADDING-LEFT: 5px; MARGIN-LEFT: 5px; "
							 +"BORDER-LEFT: #000000 2px solid; MARGIN-RIGHT: 0px'>");
				} else{
					newtext.append("<br><BLOCKQUOTE style='PADDING-RIGHT: 0px; "
							 +"PADDING-LEFT: 5px; MARGIN-LEFT: 5px; "
							 +"BORDER-LEFT: #000000 2px solid; MARGIN-RIGHT: 0px'>");
				}
				
	
				newtext.append("--- Original Message ---<br>");
				newtext.append("<b>From    : </b> "+TMailUtility.getEscHtmlTag(from)+"<br>");
				newtext.append("<b>To      : </b> "+TMailUtility.getEscHtmlTag(to)+"<br>");	
				if (icc != null) {
					newtext.append("<b>Cc      : </b> "
						+ TMailUtility.getEscHtmlTag(
							TMailAddress.getAddressString(icc))+"<br>");
				}
				newtext.append("<b>Date    : </b> "+date+"<br>");
				newtext.append("<b>Subject : </b> "+subject+"<br><br>");
			}
	
			for(int i = 0; i < texts.length; i++) {
				if (texts[i].isMessageRFC822()) {					
					isDrafts = false;
					break;
				}
			}
			
			
			if(!isMobileMode || isDrafts){
				for(int i = 0; i < texts.length; i++) {
					if (texts[i].isMessageRFC822()) {
				 		newtext.append(getContentHeader(texts[i], (!isMobileMode)?true:false));
					} else {
						newtext.append(getContentString(texts[i], isDrafts, (!isMobileMode)?true:false));
					}	
					if (isDrafts) break;
				}
			}
			
			return newtext.toString();
	
		} catch (IOException ie) { }
	
	
		return "";
	}
	
	public String getContentStringTextPlain(boolean isMobileMode,TMailMessage message, String type)
	throws MessagingException {
		if(type == null) {
			return "";
		}
	
		boolean isDrafts = type.equals("drafts");
		boolean isRewrite = type.equals("rewrite");
	
		InternetAddress[] ifrom = message.getFrom();
		InternetAddress[] ito = message.getTo();
		InternetAddress[] icc = message.getCc();
	
		String from 	= TMailAddress.getAddressString(ifrom);
		String to 		= TMailAddress.getAddressString(ito);
		String date 	= message.getSentDateForRead();
		String subject 	= message.getSubject();
	
		try {			
			TMailPart[] texts =message.getTextContent(TMailMessage.TEXT_PLAIN);
			StringBuffer newtext = new StringBuffer();
			if(!isDrafts || isRewrite) {
				newtext.append("\n\n--- Original Message ---\n");
				newtext.append("From    : "+ from +"\n");
				newtext.append("To      : "+ to +"\n");
	
				if (icc != null) {
					newtext.append("Cc      : "
						+ TMailAddress.getAddressString(icc) +"\n");
				}
	
				newtext.append("Date    : "+ date +"\n");
				newtext.append("Subject : "+ subject +"\n\n");
			}
	
			// CHECK FORWARD DRAFTS MAIL
			// 2005.10.20 doyoung
			for(int i = 0; i < texts.length; i++) {
				if (texts[i].isMessageRFC822()) {					
					isDrafts = false;
					break;
				}
			}
			
			if(!isMobileMode || isDrafts){
				for(int i = 0; i < texts.length; i++) {
					if (texts[i].isMessageRFC822()) {
				 		newtext.append(getContentHeader(texts[i], false));
					} else {
						newtext.append(getContentString(texts[i], isDrafts, false));
					}	
					if (isDrafts) break;
				}
			}
	
			return newtext.toString();
		} catch (IOException ie) { }
	
		return "";
	}
	
	private String getContentHeader(TMailPart part, boolean isHtmlMode) 
	throws MessagingException, IOException {

		StringBuffer newtext = new StringBuffer();
	
		InternetAddress[] ifrom = part.getFrom();
		InternetAddress[] ito = part.getTo();
	
		String from = TMailAddress.getAddressString(ifrom);
		String to = TMailAddress.getAddressString(ito);
		String date = part.getSentDate2();
		String subject = part.getSubject();
	
		if (isHtmlMode) {
			newtext.append("<br><BLOCKQUOTE style='PADDING-RIGHT: 0px; "
				 +"PADDING-LEFT: 5px; MARGIN-LEFT: 5px; "
				 +"BORDER-LEFT: #000000 2px solid; MARGIN-RIGHT: 0px'>");
	
			newtext.append("--- Original Message ---<br>");
			newtext.append("<b>From    : </b> "+TMailUtility.getEscHtmlTag(from)+"<br>");
			newtext.append("<b>To      : </b> "+TMailUtility.getEscHtmlTag(to)+"<br>");
			newtext.append("<b>Date    : </b> "+date+"<br>");
			newtext.append("<b>Subject : </b> "+subject+"<br><br>");
		} else {
			newtext.append("\n\n--- Original Message ---\n");
			newtext.append("From    : "+ from +"\n");
			newtext.append("To      : "+ to +"\n");
			newtext.append("Date    : "+ date +"\n");
			newtext.append("Subject : "+ subject +"\n\n");
		}
	
		return newtext.toString();
	}
	
	public String getContentString(
			TMailPart part, boolean isDrafts, boolean isHtmlMode) 
	throws MessagingException, IOException {

		StringBuffer newtext = new StringBuffer();
		String content = part.getText();
	
		if (isHtmlMode) {
			content = StringReplaceUtil.removeMdnUrl(content);
			
			StringReplaceUtil sr = new StringReplaceUtil();

			if (content.length() > 0 
				&& part.isMimeType("text/html")) {

				if(!isDrafts) {					
					content = sr.replace(content, false, false);
				}
			}
			else {
				content = TMailUtility.getEscHtmlTag(content);
			}

			newtext.append(content);

		} else {
			if (content.length() > 0 
				&& part.isMimeType("text/html")) {
				newtext.append(StringUtils.html2Text(content));
			}
			else {
				newtext.append(content);
			}
		}
		
		
		return newtext.toString();
	}
	
	public String[][] getMessageAttachFiles(TMailMessage message, String path, String email)
	throws IOException, MessagingException {		

		TMailPart[] attaches =message.getAttachFiles();
		String[][] attArray = new String[attaches.length][3];
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
		TMailPart tmpPart = null;

		for(int i = 0; i < attaches.length; i++) {
			tmpPart = attaches[i];

			if ((tmpPart.isImageType() && tmpPart.getContentID() != null) ||
				attaches[i].getSize75() == 0) {
				/* TCUSTOM-2159 20161020 */ 
				if (!"attachment".equalsIgnoreCase(tmpPart.getDisposition()))
					continue;
			}

			String fileName = sdf.format(new Date()) + "_" + email +".upld";
			String filePath = path + "/" + fileName;
			File file = new File(filePath);

			// Get Attach FileName
			String name = attaches[i].getFileName();			
			
			// Make Attach file
			int size = 0;
			InputStream in = attaches[i].getInputStream();
			OutputStream os = new FileOutputStream(file);
			
			if(Validation.isEmlFile(name)){
				Object msg = attaches[i].getContent();            	
            	if(msg instanceof IMAPMessage){
            		Enumeration enumer = ((IMAPMessage)msg).getAllHeaderLines();
            		String header = null;
            		 while (enumer.hasMoreElements()) {
     	                header = (String) enumer.nextElement();
     	                os.write(header.getBytes());
     	                os.write('\n');
     	            }
     	            os.write('\n');
     	           header = null;
            	}	           
			}

			byte[] buffer = new byte[1024 * 1024];
			int n;

			while ((n = in.read(buffer, 0, buffer.length)) != -1) {
				os.write(buffer, 0, n);
				size += n;
			}

			os.close();
			in.close();

			attArray[i][0] = fileName;
			attArray[i][1] = (name != null) ? name : "unknown";
			attArray[i][2] = Integer.toString(size);
		}

		return attArray;
		
	}
	
	public String[][] getWebFolderAttachFiles(TMailMessage[] messages,
																String path, String email)
		throws IOException, MessagingException {

        String[][] attArray = new String[messages.length][3];
        SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMdd-HHmmssSSS");

        for (int i = 0; i < messages.length; i++) {
            // Make Upload FileName
        	String fileName = sdf.format(new Date()) + i + "_" + email + ".upld";
            String filePath = path + "/" + fileName;
            
            File file = new File(filePath);

            // Get Attach FileName
            String name = messages[i].getSubject();

            // Make Attach file
            int size = 0;

            InputStream in = messages[i].getInputStream();
            OutputStream os = new FileOutputStream(file);

            byte[] buffer = new byte[1024 * 1024];
            int n;

            while ((n = in.read(buffer, 0, buffer.length)) != -1) {
                os.write(buffer, 0, n);
                size += n;
            }

            in.close();
            os.close();

            attArray[i][0] = fileName;
            attArray[i][1] = name;
            attArray[i][2] = Integer.toString(size);
        }

        return attArray;
    }
	
	public String[][] getUrlsAttachFiles(String[] atturls, String path, String email) {
		
		if(atturls == null || atturls.length == 0) {
			return null;
		}
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
		String[][] attArray = new String[atturls.length][3];

		for(int i = 0; i < atturls.length; i++) {
			String url = null;

			if(atturls[i] == null) {
				attArray[i][0] = null;
				continue;
			}

			try {
				url = new String(atturls[i].getBytes("8859_1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				attArray[i][0] = null;
				continue;
			}

			url = url.trim();
			int start = url.lastIndexOf("/")+1;
			int len = url.length();

			if(url.indexOf("http") != 0) {
				attArray[i][0] = null;
				continue;
			}

			// Get Attach FileName
			String name = url.substring(start, len);

			// Make Upload FileName
			String filePath = sdf.format(new Date()) + "_" + email +".upld";
			File file = new File(path + "/" + filePath);

			// Make Attach file
			try {
				url = url.replaceAll(" ", "%20");
				URL u = new URL(url);

				InputStream in = u.openStream();
				OutputStream os = new FileOutputStream(file);

				byte[] buffer = new byte[1024*1024];

				int n = 0;
				int size = 0;

				while((n = in.read(buffer)) != -1) {
					os.write(buffer, 0, n);
					size += n;
				}

				in.close();
				os.close();

				attArray[i][0] = filePath;
				attArray[i][1] = name;
				attArray[i][2] = Integer.toString(size);
			} catch (MalformedURLException e) {
				attArray[i][0] = null;
			} catch (IOException e) {
				attArray[i][0] = null;
			}
		}

		/*System.out.println("==========================================================SS");
		for (int i = 0; i < attArray.length; i++) {
			System.out.println("#### filePath ["+attArray[i][0]+"]");
			System.out.println("#### name     ["+attArray[i][1]+"]");
			System.out.println("#### size     ["+attArray[i][2]+"]");
		}
		System.out.println("==========================================================EE");*/
		
		return attArray;
	}
	
	/**
	 * 2012.06.19 - writeAPI Added
	 * @param atturls
	 * @param attnames
	 * @param path
	 * @param email
	 * @return
	 * @throws Exception 
	 */
	public String[][] getUrlsAttachFiles(String[] atturls, String[] attnames, String path, String email) throws Exception {

		if(atturls == null || atturls.length == 0 || attnames == null || attnames.length == 0) {
			return null;
		}
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
		String[][] attArray = new String[atturls.length][3];

		for(int i = 0; i < atturls.length; i++) {
			String url = null;

			if(atturls[i] == null) {
				attArray[i][0] = null;
				continue;
			}

			try {
				url = new String(atturls[i].getBytes("8859_1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				attArray[i][0] = null;
				continue;
			}

			url = url.trim();
			int start = url.lastIndexOf("/")+1;
			int len = url.length();

			if(url.indexOf("http") != 0) {
				attArray[i][0] = null;
				continue;
			}

			// Get Attach FileName
			if(attnames[i] == null) {
				attArray[i][0] = null;
				continue;
			}

			String name = attnames[i];
			try {
				name = name.replaceAll(" ","\\+");
				name = base64Decode(name);
			} catch (Exception e) {
				attArray[i][0] = null;
				continue;
			}

			// Make Upload FileName
			String filePath = sdf.format(new Date()) + "_" + email +".upld";
			File file = new File(path + "/" + filePath);			

			// Make Attach file
			try {
				url = url.replaceAll(" ", "%20");
				URL u = new URL(url);

				InputStream in = u.openStream();
				OutputStream os = new FileOutputStream(file);

				byte[] buffer = new byte[1024*1024];

				int n = 0;
				int size = 0;

				while((n = in.read(buffer)) != -1) {
					os.write(buffer, 0, n);
					size += n;
				}

				in.close();
				os.close();

				attArray[i][0] = filePath;
				attArray[i][1] = name;
				attArray[i][2] = Integer.toString(size);
			} catch (MalformedURLException e) {
				attArray[i][0] = null;
			} catch (IOException e) {
				attArray[i][0] = null;
			}
		}

		/*System.out.println("==========================================================SS");
		for (int i = 0; i < attArray.length; i++) {
			System.out.println("#### filePath ["+attArray[i][0]+"]");
			System.out.println("#### name     ["+attArray[i][1]+"]");
			System.out.println("#### size     ["+attArray[i][2]+"]");
		}
		System.out.println("==========================================================EE");*/
		return attArray;
	}

	/**
	 * base64 ���ڵ�
	 * 
	 * @since 2012. 6. 19.
	 * @param str
	 * @return
	 * @throws java.io.IOException 
	 */
	public String base64Encode(String str) throws java.io.IOException {
		if (str == null || str.equals("")) {
			return "";
		} else {
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			String result = encoder.encode(str.getBytes("UTF-8"));
			return result;
		}
	}
	
	/**
	 * base64 ���ڵ� 
	 * 
	 * @since 2012. 6. 19.
	 * @param str
	 * @return
	 * @throws java.io.IOException
	 */
    public String base64Decode(String str) throws java.io.IOException{
        if( str == null || str.equals("") ){
			return "";
        }else{
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			byte[] b1 = decoder.decodeBuffer(str);
			String result = new String(b1, "UTF-8");
			return result;
		}
	}	
}
