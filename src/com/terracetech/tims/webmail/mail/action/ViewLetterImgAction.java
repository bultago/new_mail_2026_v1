/**
 * ViewLetterImgAction.java 2009. 4. 14.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.action;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mail.manager.LetterManager;
import com.terracetech.tims.webmail.mail.vo.LetterVO;

/**
 * <p><strong>ViewLetterImgAction.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class ViewLetterImgAction extends BaseAction {
	
	private LetterManager letterManager = null;
	
	public void setLetterManager(LetterManager letterManager) {
		this.letterManager = letterManager;
	}
	/**
	 * <p></p>
	 */
	private static final long serialVersionUID = 94014585209859764L;
	
	public String execute() throws Exception{
		
		int letterSeq = Integer.parseInt(request.getParameter("letterSeq"));
		String part = request.getParameter("part");
		
		String strLocalhost = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
		String letterPath = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
		String letterUrl = strLocalhost+"/design/common/image/attaches/";
		
		
		LetterVO vo = letterManager.readLetter(letterSeq, letterPath, letterUrl);
			
		
		String filePath = null;
		String fileType = null;
		String fileName = null;
		
		if(part.equals("header")){
			fileName = vo.getHeadImgName();
			filePath = vo.getLetterHeaderPath();
		} else if(part.equals("body")){
			fileName = vo.getBodyImgName();
			filePath = vo.getLetterBodyPath();			
		} else if(part.equals("tail")){
			fileName = vo.getTailImgName();
			filePath = vo.getLetterTailPath();
		}
		fileType = "image/"+fileName.substring(fileName.indexOf("."),fileName.length());

		BufferedOutputStream out =
            new BufferedOutputStream(response.getOutputStream());

		// DOWNLOAD FILE
		response.setHeader("Content-Type", fileType.toLowerCase());
	
        InputStream in = new FileInputStream(filePath);
        byte[] buffer = new byte[1024 * 1024];
        int n;

        while ((n = in.read(buffer, 0, buffer.length)) != -1) {
             out.write(buffer, 0, n);
        }

        in.close();
		out.close();	
		
		return null;
	}

}
