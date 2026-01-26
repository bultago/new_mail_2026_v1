/**
 * ViewFileAction.java 2009. 2. 9.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.common.action;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.SocketException;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>ViewFileAction.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class ViewFileAction extends BaseAction {
	
	
	private static final long serialVersionUID = 7897420077167253993L;

	public String execute() throws Exception{
		
		try {
			String attfile = request.getParameter("attfile");
			if (attfile == null) {
	        	return null;
			}

			attfile = new String(attfile.getBytes("8859_1"), "UTF-8");

			String[] fileInfo = attfile.split("\t");

			if (fileInfo.length < 2) {
	        	return null;
			}

			String filePath = fileInfo[0];
			String fileName = fileInfo[1];
			
			if(filePath.indexOf("..\\") > -1||
				filePath.indexOf(".\\") > -1 ||
				filePath.indexOf("./") > -1 ||
				filePath.indexOf("../") > -1){
	        	
	        	return null;
	        }
			String tempPath = EnvConstants.getBasicSetting("tmpdir");			
			String agent 	= request.getHeader("user-agent");			
			fileName = StringUtils.getDownloadFileName(fileName, agent);		

			response.setHeader("Content-Type",
		        	"application/download; name=\"" + fileName + "\"");
			response.setHeader("Content-Disposition",
		        	"attachment; filename=\"" + fileName + "\"");			
			
			BufferedOutputStream out = new BufferedOutputStream(
					response.getOutputStream());
			
			InputStream in = new FileInputStream(tempPath + EnvConstants.DIR_SEPARATOR + filePath);
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
		}
				
		
		return null;
	}
}
