/**
 * ViewImageAction.java 2009. 4. 14.
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

import org.apache.log4j.Logger;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.util.Validation;

/**
 * <p><strong>ViewImageAction.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class ViewImageAction extends BaseAction {
	
	private static final long serialVersionUID = -4634296753604224595L;
	
	public Logger log = Logger.getLogger(this.getClass());

	public String execute() throws Exception{
		String imageFile = request.getParameter("img");

		if (imageFile == null) {
        	return null;
		}

        String[] fileInfo = imageFile.split(","); //TCUSTOM-3734 201801

        if (fileInfo.length < 2) {
            return null;
        }        
        
        String fileName = fileInfo[0];
        String chkName = fileName.toUpperCase();
        boolean isImageFileChk = false;
        boolean isSmartImg = false;
        
        if(chkName == null || chkName.length() == 0){
        	return null;
        }
        
        if(chkName.endsWith(".TSMATTIMG")){
        	isSmartImg = true;
        }
        
        if(!Validation.isImageFile(chkName)){
        	if(!isSmartImg){
        		return null;
        	}
        }
        
        if(chkName.indexOf("..\\") > -1||
        	chkName.indexOf(".\\") > -1 ||
        	chkName.indexOf("./") > -1 ||
        	chkName.indexOf("../") > -1){
        	
        	return null;
        }
        
        //if(isImageFileChk){
			String filePath = EnvConstants.getBasicSetting("tmpdir") + "/" + fileInfo[0];
	
			// DOWNLOAD FILE
			response.setHeader("Content-Type", "image/gif");
		
			InputStream in = null;
			BufferedOutputStream out =null;
			try {
				out =new BufferedOutputStream(response.getOutputStream());
				in = new FileInputStream(filePath);
		        byte[] buffer = new byte[1024 * 1024];
		        int n;
		
		        while ((n = in.read(buffer, 0, buffer.length)) != -1) {
		             out.write(buffer, 0, n);
		        }	
			} catch (Exception e) {
				log.error(e.getMessage());
			}finally{
				try{ if(in != null) in.close(); } catch(Exception e){}
				try{ if(out != null) out.close(); } catch(Exception e){}
			}
        //}	
		return null;
	}
}
