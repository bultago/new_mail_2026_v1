/**
 * BbsMessageUtil.java 2008. 11. 25.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.bbs.manager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.sun.mail.imap.IMAPFolder;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.bbs.vo.AttachFileVO;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.bbs.vo.BoardQuotaVO;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.Cid2Image;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.QuotaUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.util.Validation;

/**
 * <p>
 * <strong>BbsMessageUtil.java</strong> Class Description
 * </p>
 * <p>
 * �ֿ伳��
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
public class BbsMessageUtil {

	/**
	 * 
	 * <p>�޽������� HTML ������ ������ �����´�.</p>
	 *
	 * @param myMessage			�޽���
	 * @param tmpDir			�̹��� ���� �ӽ� ���
	 * @param host				����� ȣ��Ʈ ����
	 * @param messageKey		�ӽ� ���� �̸� ����Ű
	 * @throws MessagingException 
	 * @throws IOException
	 * @return String
	 */
	public String getBbsEditHTML(TMailMessage myMessage, String tmpDir,
			String host, String messageKey) throws MessagingException,
			IOException {

		TMailPart[] textParts = myMessage
				.getTextContent(TMailMessage.TEXT_HTML);
		TMailPart[] imageParts = myMessage.getAttachImages();

		String attachesUrl = host + EnvConstants.getAttachSetting("upimage.url");

		Cid2Image c2i = new Cid2Image(imageParts, textParts, tmpDir,
				attachesUrl, messageKey);
		c2i.parseCidImage();
		TMailPart[] newTextParts = c2i.getTextParts();

		if (newTextParts != null && newTextParts.length > 0) {
			String tempText = newTextParts[0].getText();

			return tempText;
		}

		return "";
	}

	/**
	 * 
	 * <p>������ MineMessage ���·� �����´�</p>
	 *
	 * @param session		��������
	 * @param content		�������� ���� �� ����
	 * @param image2cid		���� �̹��� ��� ����Ʈ
	 * @param attachList	÷����������
	 * @param charset		����
	 * @param tmpDir		÷������ �ӽ� ���� ���
	 * @throws MessagingException 
	 * @throws IOException 
	 * @return MimeMessage
	 */
	public MimeMessage getBbsMimePart(Session session, String content,
			Hashtable image2cid, String attachList, String charset,
			String tmpDir) throws MessagingException, IOException {

		MimeMessage mimeMessage = getBbsMimeMessage(session, content,
				image2cid, attachList, charset, tmpDir);

		return mimeMessage;
	}

	/**
	 * 
	 * <p>���ε��� �ӽ� ������ �����Ѵ�.</p>
	 *
	 * @param attlist		÷�� ���� ���� ����Ʈ
	 */
	public static void deleteUploadFile(String attlist) {
		StringTokenizer st = new StringTokenizer(attlist, "\n");

		while (st.hasMoreElements()) {
			String strTmp = (String) st.nextElement();
			StringTokenizer st_sub = new StringTokenizer(strTmp, "\t");
			String strFile = (String) st_sub.nextElement();

			File file_src = new File(strFile);
			if (file_src.exists()) {
				file_src.delete();
			}
		}
	}

	/**
	 * 
	 * <p>String ������ ���� �޽����� �����.</p>
	 *
	 * @param session		��������
	 * @param content		�������� ���� �� ����
	 * @param image2cid		���� �̹��� ��� ���� ����Ʈ
	 * @param attachList	÷����������
	 * @param charset		����
	 * @param tmpDir		÷������ �ӽ� ���� ���
	 * @throws MessagingException
	 * @throws IOException
	 * @return String
	 */
	public MimeMessage getBbsMimeMessage(Session session, String content,
			Hashtable image2cid, String attachList, String charset,
			String tmpDir) throws MessagingException, IOException {

		MimeMessage mimeMessage = new MimeMessage(session);

		if (image2cid.size() > 0 || attachList.length() > 0) {
			MimeMultipart mimeMultipart = getImageMultipart(content, image2cid,
					attachList, charset, tmpDir);

			mimeMessage.setContent(mimeMultipart);
		} else {
			mimeMessage.setContent(content, "text/html; charset=" + charset);
			mimeMessage.addHeader("Content-Transfer-Encoding", "base64");
		}
		mimeMessage.saveChanges();

		return mimeMessage;
	}

	/**
	 * 
	 * <p>���� �̹����� ÷�� ������ ���ӿ� �߰��Ѵ�.</p>
	 * 
	 * @param content		�������� ���� �� ����
	 * @param image2cid		���� �̹��� ��� ���� ����Ʈ
	 * @param attachList	÷����������
	 * @param charset		����
	 * @param tmpDir		÷������ �ӽ� ���� ���
	 * @throws MessagingException
	 * @throws IOException
	 * @return MimeMultipart
	 */
	public MimeMultipart getImageMultipart(String content, Hashtable image2cid,
			String attachList, String charset, String tmpDir)
			throws MessagingException, IOException {

		MimeMultipart mimeMultipart = new MimeMultipart();

		// set content part
		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setContent(content, "text/html; charset=" + charset);
		bodyPart.addHeader("Content-Transfer-Encoding", "base64");
		mimeMultipart.addBodyPart(bodyPart);
		
		// set Image attach part
		Enumeration enumer = image2cid.keys();
		try{
		String fileNameChk = null;
		while (enumer.hasMoreElements()) {			
			String key = (String) enumer.nextElement();

			String[] fileInfo = key.split(","); //TCUSTOM-3734 201801

			if (fileInfo.length < 2) {
				continue;
			}		
			fileNameChk = fileInfo[0].toUpperCase();
                if (!fileNameChk.endsWith(".TSMATTIMG") &&
        				!Validation.isImageFile(fileNameChk)) {
				continue;
			}

			String fileName = StringUtils.UTF8URLDecode(fileInfo[1]);
			File file = new File(tmpDir + "/" + fileInfo[0]);

			if (!file.exists()) {
				continue;
			}

			MimeBodyPart mbp = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(file);
			mbp.setDataHandler(new DataHandler(fds));
			mbp.setFileName(MimeUtility.encodeText(fileName, charset, "B"));
			mbp.setHeader("Content-ID", "<" + image2cid.get(key) + ">");
			mbp.setHeader("Content-Type", TMailUtility
					.getMIMEContentType(fileName));
			mimeMultipart.addBodyPart(mbp);			
		}
		fileNameChk = null;
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		try{
		if (attachList.length() > 0) {
			StringTokenizer st = new StringTokenizer(attachList, "\n");
			while (st.hasMoreElements()) {
				String elem = (String) st.nextElement();
	
				StringTokenizer st2 = new StringTokenizer(elem, "\t");
				String path = (String) st2.nextElement();
				String filename = (String) st2.nextElement();
				
				if(path.indexOf("..\\") > -1||
						path.indexOf(".\\") > -1 ||
						path.indexOf("./") > -1 ||
						path.indexOf("../") > -1){			        	
						continue;
			    }

				File file = new File(tmpDir +EnvConstants.DIR_SEPARATOR +path);

				if (!file.exists()) {
					continue;
				}
				
				MimeBodyPart mbp = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(file);
				mbp.setDataHandler(new DataHandler(fds));
				mbp.setFileName(MimeUtility.encodeText(filename, charset, "B"));
				mbp.setHeader("Content-Type", TMailUtility
						.getMIMEContentType(filename));
				mimeMultipart.addBodyPart(mbp);
			}
		}
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		return mimeMultipart;
	}

	/**
	 * 
	 * <p>÷�����ϸ� �����Ѵ�.</p>
	 *
	 * @param TMailPart[] 			÷�� ���� ��Ʈ
	 * @throws MessagingException 
	 * @return TMailPart[] 			���е� ���� ��Ʈ
	 */
	public static TMailPart[] getMessageAttachFiles(TMailPart[] attaches)
			throws MessagingException {

		// ÷������ ���� ī��Ʈ
		int attachesCnt = 0;

		for (int i = 0; i < attaches.length; i++) {
			TMailPart part = attaches[i];
			if (part.isImageType() && part.getContentID() != null) {
				continue;
			}
			attachesCnt++;
		}

		TMailPart[] retParts = new TMailPart[attachesCnt];
		int j = 0;

		for (int i = 0; i < attaches.length; i++) {
			TMailPart part = attaches[i];
			if (part.isImageType() && part.getContentID() != null) {
				continue;
			}
			retParts[j] = part;
			j++;
		}
		return retParts;
	}

	/**
	 * 
	 * <p>BBS-ID �� UID������ �����´�.</p>
	 *
	 * @param folder 		���
	 * @param msgid 		�޽��� ID
	 * @return long 
	 */
	public long getBbsid2Uid(TMailFolder folder, String msgid) {
		long bbsuid = -1;

		try {
			bbsuid = folder.xsearchMID(msgid);
		} catch (MessagingException ex) {			
			LogManager.writeErr(this, ex.getMessage(), ex);
		}
		return bbsuid;
	}

	/**
	 * 
	 * <p>�ٹٲ� �ڵ带 �߰��Ѵ�.</p>
	 *
	 * @param comment 		����
	 * @return String 
	 */
	public static String convertHtmlBr(String comment) {
		if (comment == null)
			return "";
		StringReader sr = new StringReader(comment);
		BufferedReader dataIn = new BufferedReader(sr);
		StringBuffer buffer = new StringBuffer();

		try {
			String line = "";
			while ((line = dataIn.readLine()) != null) {
				if (line.endsWith("<br>"))
					buffer.append(line + "\r\n");
				else
					buffer.append(line + "<br>\r\n");
			}
		} catch (IOException e1) {
		}
		return buffer.toString();
	}
	
	public static String getBbsQuotaInfo(BoardQuotaVO boardQuotaVo) {
		long[] quotaValues = QuotaUtil.parseQuotaStr(boardQuotaVo.getMailQuota());
		long[] addQuotaValues = null;
		if (!"".equals(boardQuotaVo.getMailAddQuota())) {
			addQuotaValues = QuotaUtil.parseQuotaStr(boardQuotaVo.getMailAddQuota());
			for (int i = 0, cnt = quotaValues.length; i < cnt; i++) {
				quotaValues[i] = quotaValues[i] + addQuotaValues[i];
			}
		}
		
		String delim = " ";
		StringBuffer quotaValueStr = new StringBuffer();
        quotaValueStr
        			.append(boardQuotaVo.getMessageStore()).append(delim)
        			.append(quotaValues[0] / FileUtil.SIZE_MEGA).append(delim)
        			.append(quotaValues[0] % FileUtil.SIZE_MEGA).append(delim)
        			.append(quotaValues[1]).append(delim)
        			.append("ON".equalsIgnoreCase(boardQuotaVo.getQuotaWarningMode()) ? 1 : 0 ).append(delim)
        			.append(StringUtils.isEmpty(boardQuotaVo.getQuotaWarningRatio()) ? 0 : boardQuotaVo.getQuotaWarningRatio()).append(delim)
        			.append(StringUtils.isEmpty(boardQuotaVo.getQuotaOverlookRatio()) ? 0 : boardQuotaVo.getQuotaOverlookRatio()).append(delim)
        			.append(boardQuotaVo.getMailUserSeq()).append(delim)
        			.append(boardQuotaVo.getMailDomainSeq());
        
        return quotaValueStr.toString();
	}
	
}
