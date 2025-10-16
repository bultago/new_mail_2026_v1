package com.terracetech.tims.webmail.mail.manager.body;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.MimeBodyPart;

import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.ByteArrayDataSource;
import com.terracetech.tims.webmail.common.vo.BannerVO;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;

public class AttachBannerMsgBodyCreator extends AbstractMsgBodyCreator {
	 
	private BannerVO bennerInfo = null;
	public AttachBannerMsgBodyCreator(SenderInfoBean info) {
		super(info);
		bennerInfo = info.getBennerInfo();
	}
	@Override
	public void execute(Multipart mp) throws NoSuchProviderException,
			MessagingException, UnsupportedEncodingException, IOException {
		
		String msgContents = info.getContent();
		
		if(info.isHtmlMode()){
			byte[] headerImgBytes = bennerInfo.getBannerHeader();
			byte[] tailImgBytes = bennerInfo.getBannerTail();
			String headerImgStr = null;
			String tailImgStr = null;
			
			if(headerImgBytes != null){
				headerImgStr = makeBannerPart(mp,10,"bennerTop.gif",headerImgBytes);
				msgContents = headerImgStr + "<br/><br/>" + msgContents;
			}			
			if(tailImgBytes != null){
				tailImgStr = makeBannerPart(mp,11,"bennerBottom.gif",tailImgBytes);				
				msgContents = msgContents + tailImgStr;
			}
		}
		
		info.setContent(msgContents);		
	}
	
	private String makeBannerPart(Multipart mp, 
							int seqNum, 
							String fileName, 
							byte[] imageByte) throws MessagingException{
		
		String cidString = (new SimpleDateFormat(
		"SSSssmmHH-12032001-MMddDDDyyyy").format(new Date()))+"900"+seqNum;
		MimeBodyPart mbp = new MimeBodyPart();					
		mbp.setDataHandler(new DataHandler(new ByteArrayDataSource(imageByte)));
		mbp.setHeader("Content-Type", TMailUtility
				.getMIMEContentType(fileName)
				+ "; name=\"" + fileName + "\";");
		mbp.setHeader("Content-Disposition", "inline; filename=\""
				+ fileName + "\"");
		mbp.setHeader("Content-ID", "<" + cidString + ">");					
		mp.addBodyPart(mbp);
		
		String bennerImageStr = "<img src=\"cid:" + cidString + "\" width='600px' border=0>";
		info.pushStack(mbp);
		
		return bennerImageStr;
	}

	@Override
	public boolean isAcceptable() {
		return (bennerInfo != null)?"on".equals(bennerInfo.getBannerApply()):false;
	}

}
