/**
 * DownloadAllAttachAction.java 2009. 2. 5.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import org.apache.commons.codec.binary.Base64;
import com.terracetech.tims.webmail.util.StringUtils;

import org.eclipse.angus.mail.imap.IMAPMessage;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.util.ZipUtil;

/**
 * <p><strong>DownloadAllAttachAction.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class DownloadAllAttachAction extends BaseAction {
	
	private static final long serialVersionUID = 7599504824370174717L;	
	
	public String execute() throws Exception{
		
		String sharedFlag = request.getParameter("sharedFlag");
		boolean isShared = (sharedFlag != null && sharedFlag.equals("shared"))?true:false;
		String sharedUserSeq = request.getParameter("sharedUserSeq");
		String sharedFolderName = request.getParameter("sharedFolderName");	
		String setupState = EnvConstants.getBasicSetting("setup.state");
		boolean isJapen = "jp".equalsIgnoreCase(setupState);
		
		String folderName = request.getParameter("folder");
		String uid = request.getParameter("uid");
		String part = request.getParameter("part");
		
		if(isShared){
			folderName = sharedFolderName;
		}
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder folder = null;
		File f = null;
		FileInputStream in = null;
		BufferedOutputStream out = null;
		try {
			//User user = EnvConstants.getTestUser();
			store = factory.connect(isShared, sharedUserSeq, request.getRemoteAddr(), user);
			
	        folder = store.getFolder(folderName);
	        folder.open(false);
	        TMailMessage message = folder.getMessageByUID(Long.parseLong(uid),false);
	        
	        
	        String agent = request.getHeader("user-agent");
			String subject = message.getSubject();
			subject = (subject != null) ? subject : "No Subject";
			subject = subject.replaceAll("\\\\", "");
			subject = subject.replaceAll("[\t\n\r]", "_");
			subject = subject.replaceAll("[/:*?\"<>| ]", "_");

			// TCUSTOM-3179 2017-05-23
			String uniqueDir = "";
			String zipFileName = "";
			String zipFilePath = "";			
			String zipFileName_stream = subject + ".zip";			
			zipFileName_stream = 
				StringUtils.getDownloadFileName(zipFileName_stream, agent);
			
			boolean isSuccess = false;
			if(!isJapen){
				uniqueDir = user.get(User.EMAIL) + "_" + System.currentTimeMillis();
				zipFileName = uniqueDir + ".zip";
				zipFilePath = EnvConstants.getBasicSetting("tmpdir") + "/" + uniqueDir;
				isSuccess = getMakeZipFile(zipFilePath,message,part);
				ZipUtil zipUtil = new ZipUtil();
				zipUtil.setDebug("true".equalsIgnoreCase(EnvConstants.getBasicSetting("log.debug")));
				String charset = StringUtils.isEmpty(EnvConstants.getMailSetting("default.zip.charset")) ? "EUC-KR" : EnvConstants.getMailSetting("default.zip.charset");
				zipUtil.zip(new File(zipFilePath), charset, false);
			}else{
				zipFileName = user.get(User.EMAIL) + "_" + System.currentTimeMillis() + ".zip";
				zipFilePath = EnvConstants.getBasicSetting("tmpdir") + "/" + zipFileName;			
				isSuccess = getJPMakeZipFile(zipFilePath,message,part);
			}
	        
			if(isSuccess){
				if(!isJapen){
					f = new File(zipFilePath+".zip");
				}else{
					f = new File(zipFilePath);
				}
				
				response.setHeader("Content-Type",
						"application/download; name=\"" + zipFileName_stream + "\"");
				response.setHeader("Content-Disposition",
					"attachment; filename=\"" + zipFileName_stream + "\"");
				response.setHeader("Content-Length",Long.toString(f.length()));
	
				out = new BufferedOutputStream(
					response.getOutputStream());
	
				in = new FileInputStream(f);
				byte[] buffer = new byte[1024 * 1024];
				int n;
	
				while((n = in.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, n);
				}
				out.flush();
			}
			
		} catch (Exception e) {
			if(e.getCause() instanceof SocketException){
			}else{
				LogManager.writeErr(this, e.getMessage(), e);	
			}
		}finally{
			try{ if(in != null) in.close(); }catch(Exception e){}
			try{ if(out != null) out.close(); }catch(Exception e){}
			try{ if(f != null && f.exists()) f.delete(); }catch(Exception e){}
			try{ if(folder !=null && folder.isOpen()) folder.close(false); }catch(Exception e){}
			try{ if(store !=null && store.isConnected()) store.close(); }catch(Exception e){}
		}		
		
		return null;
	}
	
	/*
	public boolean getMakeZipFile(String zipFilePath, TMailMessage message, String part){
		boolean isSuccess = true;
		try {			
			net.sf.jazzlib.ZipOutputStream zipOut = 
				new net.sf.jazzlib.ZipOutputStream(new FileOutputStream(zipFilePath));			
			
			String[] paths = part.split("_");
			Date date = new Date();
			
			for (int k = 0; k < paths.length; k++) {

				StringTokenizer st = new StringTokenizer(paths[k], ":");
				int[] attPath = new int[st.countTokens()];
				for (int i = 0; i < attPath.length; i++) {
					attPath[i] = Integer.parseInt(st.nextToken());
				}


				TMailPart myPart = new TMailPart(paths[k], message.getPart(attPath));
				boolean isAttachRFC822 = myPart.isMimeType("message/rfc822");
				String fileName = myPart.getFileName();
				
				net.sf.jazzlib.ZipEntry entry = new net.sf.jazzlib.ZipEntry(fileName);
				
				entry.setTime(date.getTime());					
				zipOut.putNextEntry(entry);
				
				InputStream in = (!isAttachRFC822)?myPart.getInputStream():null;
				
				if(isAttachRFC822){
					Object msg = myPart.getContent();
					Enumeration enumer = null;					
	            	if(msg instanceof IMAPMessage){            		
	            		enumer = ((IMAPMessage)msg).getAllHeaderLines();            		
	            	}	            	 
	            	
	            	if(enumer != null){
	            		boolean isBase64 = false;
						StringBuffer base64String = new StringBuffer();
						while (enumer.hasMoreElements()) {
							byte[] bytes = ((String) enumer.nextElement()).getBytes();
							if(!isBase64)	isBase64 = Base64.isArrayByteBase64(bytes);
							if(!isBase64){
								zipOut.write(bytes);
								zipOut.write("\n".getBytes());
							}else{
								base64String.append(new String(bytes));
								base64String.append("\n");
							}
						}
						if(isBase64){
							zipOut.write(Base64.decodeBase64(base64String.toString().getBytes()));
						}

						zipOut.write("\n".getBytes());
	            	}
	            	in = ((IMAPMessage)msg).getRawInputStream();	            	
				}					

				int i;
				byte[] buf = new byte[1024 * 1024];
				while ((i = in.read(buf)) != -1) {
					zipOut.write(buf, 0, i);
				}

				zipOut.closeEntry();
				in.close();
			}

			zipOut.close();
			
		} catch (Exception e) {
			e.printStackTrace();			
			LogManager.writeErr(this, e.getMessage(), e);
			isSuccess = false;
		}		
		return isSuccess;		
	}
	*/
	
	// TCUSTOM-3179 2017-05-23
	public boolean getMakeZipFile(String zipFilePath, TMailMessage message, String part){
		boolean isSuccess = true;
		
		StringTokenizer st = null;
		int[] attPath = null;
		TMailPart myPart = null;
		boolean isAttachRFC822 = false;
		String fileName = "";
		InputStream in = null;
		Object msg = null;
		Enumeration enumer = null;
		boolean isBase64 = false;
		StringBuffer base64String = null;
		byte[] bytes = null;
		
		FileOutputStream fos = null;
		boolean check = true;
		int count = 0;
		try {
			String[] paths = part.split("_");
			File zipFilePathDir = new File(zipFilePath);
			if(!zipFilePathDir.exists()) zipFilePathDir.mkdirs();
			File pfile = null;
			for (int k = 0; k < paths.length; k++) {

				st = new StringTokenizer(paths[k], ":");
				attPath = new int[st.countTokens()];
				for (int i = 0; i < attPath.length; i++) {
					attPath[i] = Integer.parseInt(st.nextToken());
				}


				myPart = new TMailPart(paths[k], message.getPart(attPath));
				isAttachRFC822 = myPart.isMimeType("message/rfc822");
				fileName = myPart.getFileName();
				if(fileName != null){
					fileName = fileName.replaceAll("\\\\", "");
					fileName = fileName.replaceAll("[\t\n\r]", " ");
					fileName = fileName.replaceAll("[/:*?\"<>|]", "_");    	    
				}else{
					fileName = "noname";
				}
				
	    	    while(check) {
	    	    	pfile = new File(zipFilePath, fileName);
	    		    if(pfile.exists()) {
	    		    	fileName = fileName+"("+count+")";
	    				count++;
	    				break;
	    			}
	    		    else {
	    		    	check = false;
	    		    }
	    	    }
				
				fos = new FileOutputStream(zipFilePath + EnvConstants.DIR_SEPARATOR + fileName,true);
				in = (!isAttachRFC822)?myPart.getInputStream():null;
				
				if(isAttachRFC822){
					msg = myPart.getContent();
	            	if(msg instanceof IMAPMessage){            		
	            		enumer = ((IMAPMessage)msg).getAllHeaderLines();            		
	            	}	            	 
	            	
	            	if(enumer != null){
	            		isBase64 = false;
						base64String = new StringBuffer();
						while (enumer.hasMoreElements()) {
							bytes = ((String) enumer.nextElement()).getBytes();
							if(!isBase64)	isBase64 = Base64.isArrayByteBase64(bytes);
							if(!isBase64){
								fos.write(bytes);
								fos.write("\n".getBytes());
							}else{
								base64String.append(new String(bytes));
								base64String.append("\n");
							}
						}
						if(isBase64){
							fos.write(Base64.decodeBase64(base64String.toString().getBytes()));
						}

						fos.write("\n".getBytes());
	            	}
	            	in = ((IMAPMessage)msg).getRawInputStream();	            	
				}					

				int i;
				byte[] buf = new byte[1024 * 1024];
				while ((i = in.read(buf)) != -1) {
					fos.write(buf, 0, i);
				}
				in.close();
				fos.close();
			}

		}catch(Exception e){
			e.printStackTrace();			
			LogManager.writeErr(this, e.getMessage(), e);
			isSuccess = false;
		}finally{
			try{ if(in != null) in.close(); }catch(Exception e){}
			try{ if(fos != null) fos.close(); }catch(Exception e){}
		}
		return isSuccess;		
	}
	
	public boolean getJPMakeZipFile(String zipFilePath, TMailMessage message, String part){
		boolean isSuccess = true;
		try {			
			org.apache.tools.zip.ZipOutputStream zipOut = 
				new org.apache.tools.zip.ZipOutputStream(new FileOutputStream(zipFilePath));
			zipOut.setEncoding("Shift-JIS");
			
			String[] paths = part.split("_");
			Date date = new Date();
			
			for (int k = 0; k < paths.length; k++) {

				StringTokenizer st = new StringTokenizer(paths[k], ":");
				int[] attPath = new int[st.countTokens()];
				for (int i = 0; i < attPath.length; i++) {
					attPath[i] = Integer.parseInt(st.nextToken());
				}


				TMailPart myPart = new TMailPart(paths[k], message.getPart(attPath));
				boolean isAttachRFC822 = myPart.isMimeType("message/rfc822");
				String fileName = myPart.getFileName();
				
				org.apache.tools.zip.ZipEntry entry = new org.apache.tools.zip.ZipEntry(fileName);
				
				entry.setTime(date.getTime());					
				zipOut.putNextEntry(entry);
				
				
				/* zip */
				InputStream in = (!isAttachRFC822)?myPart.getInputStream():null;
				
				if(isAttachRFC822){
					Object msg = myPart.getContent();
					Enumeration enumer = null;					
	            	if(msg instanceof IMAPMessage){            		
	            		enumer = ((IMAPMessage)msg).getAllHeaderLines();            		
	            	}	            	 
	            	boolean isBase64 = false;
					StringBuffer base64String = new StringBuffer();
					while (enumer.hasMoreElements()) {
						byte[] bytes = ((String) enumer.nextElement()).getBytes();
						if(!isBase64)	isBase64 = Base64.isArrayByteBase64(bytes);
						if(!isBase64){
							zipOut.write(bytes);
							zipOut.write("\n".getBytes());
						}else{
							base64String.append(new String(bytes));
							base64String.append("\n");
						}
					}
					if(isBase64){
						zipOut.write(Base64.decodeBase64(base64String.toString().getBytes()));
					}          	
				}					

				int i;
				byte[] buf = new byte[1024 * 1024];
				while ((i = in.read(buf)) != -1) {
					zipOut.write(buf, 0, i);
				}

				zipOut.closeEntry();
				in.close();
			}

			zipOut.close();
			
		} catch (Exception e) {
			e.printStackTrace();			
			LogManager.writeErr(this, e.getMessage(), e);
			isSuccess = false;
		}		
		return isSuccess;		
	}
	
	
}
