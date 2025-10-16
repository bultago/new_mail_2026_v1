/**
 * LetterPaperMsgBodyCreator.java 2009. 4. 14.
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
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.terracetech.tims.webmail.mail.vo.LetterVO;

/**
 * <p><strong>LetterPaperMsgBodyCreator.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class LetterPaperMsgBodyCreator extends AbstractMsgBodyCreator {

	/**
	 * <p></p>
	 *
	 * @param info
	 */
	public LetterPaperMsgBodyCreator(SenderInfoBean info) {
		super(info);
	}

	/**
	 * <p></p>
	 *
	 * @see com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator#execute(javax.mail.Multipart)
	 * @param mp
	 * @throws NoSuchProviderException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 * @throws IOException 
	 */
	@Override
	public void execute(Multipart mp) throws NoSuchProviderException,
			MessagingException, UnsupportedEncodingException, IOException {
				
		String content = info.getContent();
		LetterVO vo = info.getLetterPaper();
		if(vo != null){
			String mode = vo.getAttachMode();
			String headerStr = null;
			String bodyStr = null;
			String tailStr = null;
			
			if(mode.equals("T")){
				if(!info.isEtcSecureMail()){
					Date tm = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"SSSssmmHH-12032001-MMddDDDyyyy");
					String defaultcidstr = sdf.format(tm);
					
					headerStr =  defaultcidstr + 90001;
					makeLetterPart(mp, vo.getHeadImgName(), vo.getLetterHeaderPath(), headerStr);				
					bodyStr =  defaultcidstr + 90002;
					makeLetterPart(mp, vo.getBodyImgName(), vo.getLetterBodyPath(), bodyStr);
					tailStr =  defaultcidstr + 90003;	
					makeLetterPart(mp, vo.getTailImgName(), vo.getLetterTailPath(),tailStr);
					
					headerStr = "cid:"+headerStr;
					bodyStr = "cid:"+bodyStr;
					tailStr = "cid:"+tailStr;
				} else {
					headerStr = "";
					bodyStr = "";
					tailStr = "";
				}
				
			} else if(mode.equals("F")){
				String localUrl = info.getLocalUrl();
				String viewUrl = EnvConstants.getAttachSetting("letter.url");
				headerStr = localUrl+viewUrl+"part=header&letterSeq="+vo.getLetterSeq();
				bodyStr = localUrl+viewUrl+"part=body&letterSeq="+vo.getLetterSeq();
				tailStr = localUrl+viewUrl+"part=tail&letterSeq="+vo.getLetterSeq();				
			}
			
			content = content.replaceAll("\\{tims_letter_paper_header\\}", headerStr);
			content = content.replaceAll("\\{tims_letter_paper_body\\}", bodyStr);
			content = content.replaceAll("\\{tims_letter_paper_tail\\}", tailStr);
			
			info.setContent(content);
		}
	}
	
	
	private void makeLetterPart(Multipart mp, String fileName, String filePath, String cidStr) 
	throws MessagingException, UnsupportedEncodingException{
		
		fileName = URLDecoder.decode(fileName, "UTF-8");
		fileName = MimeUtility.encodeText(fileName, info.getCharset(), "B");

		File file = new File(filePath);

		if (file.exists()) {
			MimeBodyPart mbp = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(file);
			mbp.setDataHandler(new DataHandler(fds));
			mbp.setHeader("Content-Type", TMailUtility
					.getMIMEContentType(fileName)
					+ "; name=\"" + fileName + "\";");
			mbp.setHeader("Content-Disposition", "inline; filename=\""
					+ fileName + "\"");
			mbp.setHeader("Content-ID", "<" + cidStr + ">");
	
			mp.addBodyPart(mbp);
			
			info.pushStack(mbp);
		}
		
	}

	/**
	 * <p></p>
	 *
	 * @see com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator#isAcceptable()
	 * @return 
	 */
	@Override
	public boolean isAcceptable() {
		// TODO Auto-generated method stub
		return true;
	}

}
