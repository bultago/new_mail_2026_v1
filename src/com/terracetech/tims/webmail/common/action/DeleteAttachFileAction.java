/**
 * DeleteAttachFileAction.java 2009. 4. 15.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.common.action;

import java.io.File;
import java.util.StringTokenizer;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;

/**
 * <p><strong>DeleteAttachFileAction.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class DeleteAttachFileAction extends BaseAction {

	/**
	 * <p></p>
	 */
	private static final long serialVersionUID = -2772179968367802051L;
	
	public String execute() throws Exception{
		
		String email = user.get(User.EMAIL);
		String attfile = request.getParameter("attfile");
		String rcptsfile = request.getParameter("massFilePath");
		String userid = request.getParameter("userId");		
		String attfiles = request.getParameter("attfiles");
		
		String strFile = "";
		String strName = "";
		String strSize = "";	
		String type = "";
	    if (attfile != null && attfile.length() > 0) {
	    	attfile = new String(attfile.getBytes("8859_1"), "UTF-8");

	        StringTokenizer st= new StringTokenizer(attfile, "\t");
	        strFile = (String)st.nextElement();	        
	        strName = (String)st.nextElement();
	        strSize = (String)st.nextElement();
			
	        
	        String userfile = email + ".u";
	        String lastname = strFile.substring(
	            strFile.length() - userfile.length(), strFile.length());

			if(userfile.equals(lastname)) {
	        	File file_src = new File(strFile);

	        	if(file_src.exists()) {
	            	file_src.delete();
	        	}
			}
			
			type = "attach";			
	    }
		else if(rcptsfile != null) {
			strFile = new String(rcptsfile.getBytes("8859_1"), "UTF-8");
			File file_src = new File(strFile);
			if(file_src.exists()) {
				file_src.delete();
			}
			
			type = "massrcpt";
	        
		}else if(attfiles != null){
			attfiles = new String(attfiles.getBytes("8859_1"), "UTF-8");
	        StringTokenizer st= new StringTokenizer(attfiles, " \r\n");
			while(st.hasMoreElements()){
		        String strFileName = (String)st.nextElement();
		
		        String userfile = email + ".u";
		        String lastname = strFile.substring(
		            strFile.length() - userfile.length(), strFile.length());
		
		        if(userfile.equals(lastname)) {
		            File file_src = new File(strFileName);
		
		            if(file_src.exists()) {
		                file_src.delete();
		            }
		        }
			}
		}
	    
	    request.setAttribute("filePath", strFile);
	    request.setAttribute("fileName", strName);
	    request.setAttribute("fileSize", strSize);
	    request.setAttribute("type", type);
	    
		
		return "simpleResult";
	}
}
