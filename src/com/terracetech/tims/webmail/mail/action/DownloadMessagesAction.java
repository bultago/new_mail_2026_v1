/**
 * DownloadMessagesAction.java 2009. 2. 17.
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
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import com.terracetech.tims.webmail.util.StringUtils;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.DateUtil;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.util.ZipUtil;
/**
 * <p><strong>DownloadMessagesAction.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */

@SuppressWarnings("unchecked")
public class DownloadMessagesAction extends BaseAction {
	
	private MailUserManager mailUserManager = null;
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	private static final long serialVersionUID = -7899408186307574274L;
	
	public String execute() throws Exception{
		//User user = EnvConstants.getTestUser();
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		String sharedFlag = request.getParameter("sharedFlag");
		boolean isShared = (sharedFlag != null && sharedFlag.equals("shared"))?true:false;
		String sharedUserSeq = request.getParameter("sharedUserSeq");
		String sharedFolderName = request.getParameter("sharedFolderName");			
		
		String folderName = request.getParameter("folder");
		//String uids[] = request.getParameterValues("uids");		
		String uidsParam = request.getParameter("uids") == null ? "" : request.getParameter("uids");
		String[] uids = uidsParam.split("[;,]");
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder folder = null;

		String dateStr = DateUtil.getBasicDateStr();
        String tmpFilePath = EnvConstants.getBasicSetting("tmpdir") + "/" + 
        							user.get(User.EMAIL)+"_backup_"+dateStr + "/";
        
        String agent = request.getParameter("agent");
		if(agent == null) {
			agent = request.getHeader("user-agent");
		}
		
        if(isShared){
			folderName = sharedFolderName;
		}
        if (StringUtils.isEmpty(folderName))
        	folderName = FolderHandler.INBOX;
        
		File tmpPath = new File(tmpFilePath);
		try {
			
			long uids2[] = new long[uids.length];
	        for(int i = 0; i < uids2.length; i++)
	        {
	            uids2[i] = Long.parseLong(uids[i]);
	        }
	        
	        store = factory.connect(isShared, sharedUserSeq, request.getRemoteAddr(), user);
	        
			folder = store.getFolder(folderName);
	        folder.open(false);
	        
	        TMailMessage[] messages = folder.getMessagesByUID(uids2);
	        
			if(!tmpPath.exists()){
				tmpPath.mkdir();
			}
			
			String fileNames[] = new String[uids.length];
	        String emlName = null;
	        String tmpSubject = null;
	        File pfile = null;
	        
	        int count = 0;
	        for(int i = 0; i < uids.length; i++) {
	        	tmpSubject = messages[i].getSubject();
	            emlName = (tmpSubject != null) ? tmpSubject : "No Subject";	            
	        
	    		//String agent = request.getHeader("user-agent");	    	    
	    	    emlName = emlName.replaceAll("\\\\", "");
	    	    emlName = emlName.replaceAll("[\t\n\r]", " ");
	    	    emlName = emlName.replaceAll("[/:*?\"<>|]", "_");
	    			
	    		  
	    	    boolean check = true;
	    	    
	    	    while(check) {
	    	    	pfile = new File(tmpFilePath,emlName+".eml");
	    		    if(pfile.exists()) {
	    				emlName = emlName+"["+count+"]";
	    				count++;
	    				break;
	    			}
	    		    else {
	    		    	check = false;
	    		    }
	    	    }
	    	    
	    	    emlName = emlName + ".eml";
	    		fileNames[i] = emlName;
	    		
	    		FileOutputStream fos = new FileOutputStream(tmpFilePath+emlName,true);
	    		
	    		Enumeration enumer = messages[i].getAllHeaderLines();

	            while (enumer.hasMoreElements()) {
	                String header = (String) enumer.nextElement();

	                fos.write(header.getBytes());
	                fos.write('\n');
	            }

	            fos.write('\n');

	    		InputStream in = messages[i].getRawInputStream();
	    		
	            byte[] buffer = new byte[1024 * 1024];
	            int n;

	            while ((n = in.read(buffer, 0, buffer.length)) != -1) {
	            	fos.write(buffer, 0, n);
	            }
	            
	            fos.flush();
	            fos.close();
	            in.close();
	        	
	        }
	        
	        
	        folder.close(true);
	        store.close();
	        
	        String target = null;
	        String downFileName = null;
	        File targetFile = null;
	        if(fileNames.length == 1){
	        	downFileName = StringUtils.getDownloadFileName(fileNames[0], agent);;
	        	targetFile = new File(tmpFilePath+fileNames[0]);	        	
	        } else {
		        String[] source = new String[fileNames.length];
		        downFileName = user.get(User.EMAIL) +"_backup_"+ dateStr +".zip";
		        target = tmpFilePath + downFileName;
		        
		        String uniqueDir = user.get(User.EMAIL) +"_backup_"+ dateStr;
				String zipFileDir = EnvConstants.getBasicSetting("tmpdir")+EnvConstants.DIR_SEPARATOR+uniqueDir;  
				String zipFileName = uniqueDir + ".zip";
				String zipFilePath = EnvConstants.getBasicSetting("tmpdir") + EnvConstants.DIR_SEPARATOR + zipFileName;
		        
				ZipUtil zipUtil = new ZipUtil();
				zipUtil.setDebug("true".equalsIgnoreCase(EnvConstants.getBasicSetting("log.debug")));
				String charset = StringUtils.isEmpty(EnvConstants.getMailSetting("default.zip.charset")) ? "EUC-KR" : EnvConstants.getMailSetting("default.zip.charset");
				zipUtil.zip(new File(zipFileDir), charset, false);
				
				targetFile = new File(zipFilePath);
				/*
		        for(int i = 0; i < uids.length; i++) {
		        	source[i] = tmpFilePath + fileNames[i];
		        }
		        
		        byte[] buf = new byte[1024];
	
		        try {
		            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(target));
	
		            for (int i = 0; i < source.length; i++) {
		                FileInputStream in = new FileInputStream(source[i]);
		                ZipEntry entry = new ZipEntry(user.get(User.EMAIL)+"_backup_"+dateStr+"/"+fileNames[i]);
		                entry.setTime(new Date().getTime());
		                zipOut.putNextEntry(entry);
		               
		                int len;
		                while ((len = in.read(buf)) > 0) {
		                    zipOut.write(buf, 0, len);
		                }
		                zipOut.closeEntry();
		                in.close();
		            }
		            zipOut.close();
		        } catch (IOException e) {
		        	LogManager.writeErr(this, e.getMessage(), e);
		        }
		        
		        targetFile = new File(tmpFilePath + downFileName);
		        */
				
				
	        }
	        
	        
	        response.setHeader("Content-Type",
	        		"application/download; name=\"" + downFileName + "\"");
    		response.setHeader("Content-Disposition",
    				"attachment; filename=\"" + downFileName + "\"");
    		response.setHeader("Content-Length", Long.toString(targetFile.length()));

        	BufferedOutputStream out = new BufferedOutputStream(
            response.getOutputStream());
        	
        	FileInputStream in = new FileInputStream(targetFile);
        	byte[] buffer = new byte[1024 * 1024];
            int n;

            while ((n = in.read(buffer, 0, buffer.length)) != -1) {
                 out.write(buffer, 0, n);
            }

            in.close();
            out.close();
            
		} catch (Exception e) {
			if(e.getCause() instanceof SocketException){
			}else{
				LogManager.writeErr(this, e.getMessage(), e);	
			}
		}finally{
			try {
				if(store !=null && store.isConnected())
					store.close();
				if(folder !=null && folder.isOpen())
					folder.close(false);	
			} catch (Exception e2) {
			}
			
			try {
				FileUtil.deletePathFiles(tmpPath);
	            tmpPath.delete();	
			} catch (Exception e2) {
			}
				     
		}		
		
		return null;
	}

}
