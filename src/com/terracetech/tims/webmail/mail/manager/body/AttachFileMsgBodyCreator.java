/**
 * SimpleMultipartMsgBodyCreator.java 2008. 12. 4.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.body;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;

import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.util.Validation;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p>
 * <strong>SimpleMultipartMsgBodyCreator.java</strong> Class Description
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
public class AttachFileMsgBodyCreator extends AbstractMsgBodyCreator {

	public AttachFileMsgBodyCreator(SenderInfoBean info) {
		super(info);
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @see com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator#isAcceptable()
	 * @return
	 */
	public boolean isAcceptable() {
		return true;
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @see com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator#execute(java.lang.String,
	 *      javax.mail.Multipart)
	 * @param content
	 * @param mp
	 * @return
	 * @throws NoSuchProviderException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void execute(Multipart mp)
			throws NoSuchProviderException, MessagingException,
			UnsupportedEncodingException, IOException {
		
		String tmpDir = EnvConstants.getBasicSetting("tmpdir");
		String attlist = info.getAttlist();
		if(attlist != null){
			StringTokenizer st = new StringTokenizer(info.getAttlist(), "\n");
			while (st.hasMoreElements()) {
				String strTmp = (String) st.nextElement();
				StringTokenizer st_sub = new StringTokenizer(strTmp, "\t");
				String strFile = (String) st_sub.nextElement();
				String strName = (String) st_sub.nextElement();
				
				if(strFile.indexOf("..\\") > -1||
						strFile.indexOf(".\\") > -1 ||
						strFile.indexOf("./") > -1 ||
						strFile.indexOf("../") > -1){			        	
						continue;
			    }
	
				File file_src = new File(tmpDir + EnvConstants.DIR_SEPARATOR + strFile);

				//2012.05.17 - 첨부파일이 다른 장비로 올라갔을 경우 check - SS
				if (!file_src.exists()) {
					if (FileUtil.checkUploadFiles(strFile)) {
						file_src = new File(tmpDir + EnvConstants.DIR_SEPARATOR + strFile);
					}
				}
				//2012.05.17 - 첨부파일이 다른 장비로 올라갔을 경우 check - EE
				info.addTempFile(file_src);
	
				MimeBodyPart mbp = new MimeBodyPart();
	
				FileDataSource fds = new FileDataSource(file_src);
				mbp.setDataHandler(new DataHandler(fds));
	
				strName = strName.replaceAll("\r\n", " ");
				String esc_name = MimeUtility.encodeText(strName,
						info.getCharset(), "B");
	
				mbp.setHeader("Content-Type", TMailUtility
						.getMIMEContentType(strName));
				mbp.setFileName(esc_name);
	
				mbp.addHeader("Content-Transfer-Encoding", (Validation.isEmlFile(strName))?"7bit":"base64");
	
				mp.addBodyPart(mbp);
				
				info.pushStack(mbp);
			}
		}

	}

}
