/**
 * AttachUploadedImageMsgBodyCreator.java 2008. 12. 5.
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
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Hashtable;

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

/**
 * <p>
 * <strong>AttachUploadedImageMsgBodyCreator.java</strong> Class Description
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
public class AttachUploadedImageMsgBodyCreator extends AbstractMsgBodyCreator {

	public AttachUploadedImageMsgBodyCreator(SenderInfoBean info) {
		super(info);
	}

	/**
	 * <p>
	 * 임시 경로에 만들어진 파일 경로를 문자열로 받아서 해당 문자를 파싱을 한다. <br>
	 * 파싱을 한 결과를 루프를 돌아서 해당 파일을 마임에 넣는다.
	 * </p>
	 * 
	 * @see com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator#execute(java.lang.String,
	 *      javax.mail.Multipart)
	 * @param content
	 * @param mp
	 * @throws NoSuchProviderException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@Override
	public void execute(Multipart mp)
			throws NoSuchProviderException, MessagingException,
			UnsupportedEncodingException, IOException {

		Hashtable<String, String> image2cid = info.getImage2cid();
		String tmpDir = EnvConstants.getBasicSetting("tmpdir");

		Enumeration<String> enumer = image2cid.keys();
		String fileNameChk = null;
		while (enumer.hasMoreElements()) {
			String key = (String) enumer.nextElement();

			String[] fileInfo = key.split("\\|");

			if (fileInfo.length < 2) {
				continue;
			}
			
			fileNameChk = fileInfo[0].toUpperCase();
			if(!fileNameChk.endsWith(".TSMATTIMG") &&
				!Validation.isImageFile(fileNameChk)){
				continue;
			}

			String fileName = URLDecoder.decode(fileInfo[1], "UTF-8");
			fileName = MimeUtility.encodeText(fileName, info.getCharset(), "B");

			File file = new File(tmpDir + "/" + fileInfo[0]);

			if (!file.exists()) {
				continue;
			}
			
			

			MimeBodyPart mbp = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(file);
			mbp.setDataHandler(new DataHandler(fds));
			mbp.setHeader("Content-Type", TMailUtility
					.getMIMEContentType(fileName)
					+ "; name=\"" + fileName + "\";");
			mbp.setHeader("Content-Disposition", "inline; filename=\""
					+ fileName + "\"");
			mbp.setHeader("Content-ID", "<" + image2cid.get(key) + ">");

			mp.addBodyPart(mbp);
						
			info.pushStack(mbp);
			if(!info.isAutoSave()){
				info.addTempFile(file);
			}
			
		}
		fileNameChk = null;
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @see com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator#isAcceptable()
	 * @return
	 */
	@Override
	public boolean isAcceptable() {
		return true;
	}

}
