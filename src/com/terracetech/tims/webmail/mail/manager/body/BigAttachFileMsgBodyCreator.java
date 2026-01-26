package com.terracetech.tims.webmail.mail.manager.body;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeUtility;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.ByteArrayDataSource;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.util.StringUtils;

public class BigAttachFileMsgBodyCreator extends AbstractMsgBodyCreator {

	public BigAttachFileMsgBodyCreator(SenderInfoBean info) {
		super(info);
	}

	@Override
	public void execute(Multipart mp) throws NoSuchProviderException,
			MessagingException, UnsupportedEncodingException, IOException {
		
		String bigAttachContents = info.getBigAttachMailContents();
		
		if(info.isBigAttach()){
			String[] links = info.getBigAttachLink();
			String encodeLink = null;
			String cryptMethod = info.getCryptMethod();
			if(links != null){
				for (int i = 0; i < links.length; i++) {						
			        try {
			        	encodeLink = SecureUtil.encrypt(cryptMethod, EnvConstants.ENCRYPT_KEY,links[i]);
					} catch (Exception e) {
						throw new MessagingException("BIGATTACH LINK ENCRYPT ERROR");
					}       
					
					bigAttachContents = bigAttachContents.replaceAll("\\{tims_bigattach_link_"+i+"\\}", 
							info.getLocalUrl() +"/mail/downloadBigAttach.do?param="+URLEncoder.encode(encodeLink));						
				}
			}
		}
		
		info.setBigAttachMailContents(bigAttachContents);
		
		StringBuffer bigAttachContentsHtml = new StringBuffer();
		bigAttachContentsHtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		bigAttachContentsHtml.append("<html><head>");
		bigAttachContentsHtml.append("</head><body>");
		bigAttachContentsHtml.append(StringUtils.javaToHtml(bigAttachContents.toString(),false));
		bigAttachContentsHtml.append("</body></html>");
		
		MimeBodyPart mbp = new MimeBodyPart();
		mbp.setDataHandler(new DataHandler(new ByteArrayDataSource(bigAttachContentsHtml.toString().getBytes())));
		String fileName = "TMS_BIGATTACHMENT.html";
		String esc_name = MimeUtility.encodeText(fileName,
				info.getCharset(), "B");

		mbp.setHeader("Content-Type", TMailUtility
				.getMIMEContentType(fileName));
		mbp.setFileName(esc_name);
		mbp.addHeader("Content-Transfer-Encoding", "base64");
		mp.addBodyPart(mbp);
		info.pushStack(mbp);
	}

	@Override
	public boolean isAcceptable() {
		return info.isBigAttach();
	}

}
