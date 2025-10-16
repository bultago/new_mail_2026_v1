/**
 * ViewSourceAction.java 2009. 3. 4.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.action;

import java.io.InputStream;
import java.util.Enumeration;

import javax.mail.internet.MimeUtility;

import com.initech.util.BufferedOutputStream;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;

/**
 * <p><strong>ViewSourceAction.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class ViewSourceAction extends BaseAction {

	/**
	 * <p></p>
	 */
	private static final long serialVersionUID = -4693021282081906431L;
	
	
	public String execute() throws Exception{
		boolean isError = false;
		String sharedFlag = request.getParameter("sharedFlag");
		boolean isShared = (sharedFlag != null && sharedFlag.equals("shared"))?true:false;
		String sharedUserSeq = request.getParameter("sharedUserSeq");
		String sharedFolderName = request.getParameter("sharedFolderName");		
		
		String folderName = request.getParameter("folder");
		String uid = request.getParameter("uid");
		TMailStore store = null;
		TMailFolder folder = null;		
		TMailStoreFactory factory = new TMailStoreFactory();
		
		if(isShared){
			folderName = sharedFolderName;
		}
		
		try {
			store = factory.connect(isShared, sharedUserSeq, request.getRemoteAddr(), user);	
			folder = store.getFolder(folderName);
			folder.open(false);
			TMailMessage message = folder.getMessageByUID(Long.parseLong(uid), true);
			TMailPart[] bodyPart = message.getTextContent(TMailMessage.TEXT_HTML);
			String charset = null;
			if(bodyPart != null){
				charset = (bodyPart[0]).getCharset();
			} else {
				charset = MimeUtility.getDefaultJavaCharset();
			}
			
			response.setContentType("text/plain");
			response.setCharacterEncoding(charset);
			BufferedOutputStream out = new BufferedOutputStream(
	            response.getOutputStream());
	        Enumeration enumer = message.getAllHeaderLines();

	        while (enumer.hasMoreElements()) {
	            String header = (String) enumer.nextElement();

	            // out.write(header.getBytes("8859_1"));
	            out.write(header.getBytes());
	            out.write('\n');
	        }

	        out.write('\n');

	        InputStream in = message.getRawInputStream();
	        byte[] buffer = new byte[1024 * 1024];
	        int n;

	        while ((n = in.read(buffer, 0, buffer.length)) != -1) {
	             out.write(buffer, 0, n);
	        }

	        in.close();
	        out.close();

	        folder.close(true);
	        store.close();
			
		} catch (Exception e) {
			isError = true;
		} finally {
			if(folder != null){
				folder.close(false);
			}			
			if(store != null){
				store.close();
			}			
		}
		
		return null;
	}

}
