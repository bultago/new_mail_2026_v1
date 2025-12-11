/**
 * MessageUtil.java 2008. 12. 1.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;

/**
 * <p><strong>MessageUtil.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MessageUtil {
	
	public static String getSimpleAddress(String str,int size){
		
		String simpleStr = null;
		str = (str != null)?str:"";
		try {
			InternetAddress[] ias = InternetAddress.parse(str, false);

			if (ias != null && ias.length > 0) {
				if (ias[0].getPersonal() != null) {
					simpleStr = ias[0].getPersonal();
				}
				else if (ias[0].getAddress() != null) {
					simpleStr = ias[0].getAddress();
				}
			} else {
				simpleStr = str;				
			}
		} catch(AddressException e) {
			System.out.println("-- ERR PRINT SIMPLE ADDRESS : ["+str+"]["+e.getMessage()+"]");
			simpleStr = str;
		}
		
		if (simpleStr.startsWith("=?") && simpleStr.endsWith("@")) {
			simpleStr = simpleStr.substring(0, simpleStr.length() - 1);
			try {
				simpleStr = MimeUtility.decodeText(simpleStr);
			} catch (UnsupportedEncodingException e) { }
		}

		if (simpleStr.startsWith("CN=")) {
			int e = simpleStr.indexOf("/");
			
			if(e != -1){
				simpleStr = simpleStr.substring(3, e);
			}
		}

        if (size > 0 && simpleStr.length() > size) {
            simpleStr = simpleStr.substring(0, size) + "...";
        }
        
        return simpleStr;
	}
	
	public static String getFolderAlias(I18nResources msgResource, String name) {
		String fname = null;
		if (name.equalsIgnoreCase(FolderHandler.INBOX)) {
			fname = msgResource.getMessage("folder.inbox");
		} else if (name.equals(FolderHandler.SENT)) {
			fname = msgResource.getMessage("folder.sent");
		} else if (name.equals(FolderHandler.DRAFTS)) {
			fname = msgResource.getMessage("folder.drafts");
		} else if (name.equals(FolderHandler.RESERVED)) {
			fname = msgResource.getMessage("folder.reserved");
		} else if (name.equals(FolderHandler.SPAM)) {
			fname = msgResource.getMessage("folder.spam");
		} else if (name.equals(FolderHandler.TRASH)) {
			fname = msgResource.getMessage("folder.trash");
		} else {
			fname = name;
		}

		return fname;
	}
	
	
	public static byte[] getMimeByte(InputStream is) throws Exception {
		BufferedInputStream bis = new BufferedInputStream(is);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		int bcount = 0;
		byte[] buf = new byte[2048];
		try {
			while (true) {
				int n = bis.read(buf);
				if (n == -1)
					break;
				// System.out.println(n);
				bcount += n;
				bout.write(buf, 0, n);
			}
			bout.flush();
		}catch (Exception e) {
			throw new IOException("MIME READ ERROR");
		}
		
		// return bout.toString();
		return bout.toByteArray();
	}
	
	
	
}
