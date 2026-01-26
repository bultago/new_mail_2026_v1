/**
 * AttachSignMsgBodyCreator.java 2008. 12. 4.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.body;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.internet.MimeBodyPart;

import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.ByteArrayDataSource;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.setting.vo.SignDataVO;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p>
 * <strong>AttachSignMsgBodyCreator.java</strong> Class Description
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
public class AttachSignMsgBodyCreator extends AbstractMsgBodyCreator {

	public AttachSignMsgBodyCreator(SenderInfoBean info) {
		super(info);
	}

	/**
	 * <p>
	 * TimsV6������ Sing Image�� ���Ϸ� ���� ÷�θ� ������, TimsV7���ʹ� �ٷ� ����� �Ѵ�.
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
		
		String msgContents = info.getContent();
		SignDataVO sign = info.getSignData();	
		StringReplaceUtil replacer = new StringReplaceUtil();
		
		if(info.isAttachSign() && sign != null){
			String signContent = "";			
			if(info.isHtmlMode()){
				String signImageName = sign.getSignImageName();
				String signImageStr = "";
				byte[] signByte = sign.getSignImage();
				if(signImageName != null && 
						signByte != null && 
						!info.isEtcSecureMail()){					
					String cidString = (new SimpleDateFormat(
					"SSSssmmHH-12032001-MMddDDDyyyy").format(new Date()))+"8001";
					MimeBodyPart mbp = new MimeBodyPart();					
					mbp.setDataHandler(new DataHandler(new ByteArrayDataSource(sign.getSignImage())));
					mbp.setHeader("Content-Type", TMailUtility
							.getMIMEContentType(signImageName)
							+ "; name=\"" + signImageName + "\";");
					mbp.setHeader("Content-Disposition", "inline; filename=\""
							+ signImageName + "\"");
					mbp.setHeader("Content-ID", "<" + cidString + ">");					
					mp.addBodyPart(mbp);
					
					signImageStr = "<img src=\"cid:" + cidString + "\" border=0 width='110px' height='135px'>";
					info.pushStack(mbp);
				}
				if("text".equalsIgnoreCase(sign.getSignMode())){
					signContent = replacer.getTextToHTML(sign.getSignText());
				} else {
					signContent = sign.getSignText();
				}
				signContent = getSignString(signImageStr, signContent);
			} else {				
				if("text".equalsIgnoreCase(sign.getSignMode())){
					signContent = sign.getSignText();
				}
			}
			msgContents = msgContents.replaceAll("\\{tims_sign_pos\\}",Matcher.quoteReplacement(signContent));						
		} else {
			msgContents = msgContents.replaceAll("\\{tims_sign_pos\\}", "");			
		}
		
		info.setContent(msgContents);

		/*
		
		info.setSignImage(getSignString(signImage, "sign???"));*/
	}
	
	public void executePreview(String url, String imgPath, String attachPath)
	throws NoSuchProviderException, MessagingException,
	UnsupportedEncodingException, IOException {		

		String msgContents = info.getContent();
		SignDataVO sign = info.getSignData();	
		StringReplaceUtil replacer = new StringReplaceUtil();
		
		if(info.isAttachSign() && sign != null){
			String signContent = "";			
			if(info.isHtmlMode()){
				String signImageName = sign.getSignImageName();
				String signImageStr = "";
				byte[] signByte = sign.getSignImage();
				if(signImageName != null &&
						signByte != null && 
						!info.isEtcSecureMail()
					){
					String fileUrl = url + attachPath + signImageName;
					String filePath = imgPath + signImageName;				
					BufferedOutputStream out =
			            new BufferedOutputStream(new FileOutputStream(filePath));
				
			        InputStream in = new ByteArrayInputStream(signByte);
			        byte[] buffer = new byte[1024 * 1024];
			        int n;

			        while ((n = in.read(buffer, 0, buffer.length)) != -1) {
			             out.write(buffer, 0, n);
			        }
			        in.close();	
			        out.flush();
			        out.close();
					
					signImageStr = "<img src=\"" + fileUrl + "\" border=0 width='110px' height='135px'>";					
				}
				if("text".equalsIgnoreCase(sign.getSignMode())){
					signContent = replacer.getTextToHTML(sign.getSignText());
				} else {
					signContent = sign.getSignText();
				}
				signContent = getSignString(signImageStr, signContent);
				msgContents = msgContents.replaceAll("\\{tims_sign_pos\\}",Matcher.quoteReplacement(signContent));
			} else {
				msgContents = replacer.getTextToHTML(msgContents);
				if("text".equalsIgnoreCase(sign.getSignMode())){
					signContent = sign.getSignText();
					signContent = replacer.getTextToHTML(signContent);
				}
				msgContents = msgContents.replaceAll("\\{tims_sign_pos\\}",Matcher.quoteReplacement(signContent));				
			}			
		} else {
			msgContents = msgContents.replaceAll("\\{tims_sign_pos\\}", "");			
		}
		
		info.setContent(msgContents);		
	}
	
	private String getSignString(String signImage, String signStr) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("<br/><br/>");
	        sb.append("<table border=0 cellpadding=0 cellspacing=0 >");
	        sb.append("<tr>");
	        if (StringUtils.isNotEmpty(signImage)) {
	            sb.append("<td>");
	            sb.append(signImage);
	            sb.append("</td>");
	            sb.append("<td width=10></td>");
	        }
	        sb.append("<td style='vertical-align: top;font-size:9pt;'>");
	        sb.append(signStr);
	        sb.append("</td></tr></table>");

	        return sb.toString();
	}

	/*private byte[] getDummyImage() {
		byte[] img = { 'G', 'I', 'F', '8', '9', 'a', 0x01, 0x00, 0x01, 0x00,
				0xffffff80, 0xffffffff, 0x00, 0xffffffc0, 0xffffffc0,
				0xffffffc0, 0x00, 0x00, 0x00, 0x21, 0xfffffff9, 0x04, 0x01,
				0x00, 0x00, 0x00, 0x00, 0x2c, 0x00, 0x00, 0x00, 0x00, 0x01,
				0x00, 0x01, 0x00, 0x00, 0x02, 0x02, 0x44, 0x01, 0x00, 0x3b,
				0x00 };

		return img;
	}*/

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
