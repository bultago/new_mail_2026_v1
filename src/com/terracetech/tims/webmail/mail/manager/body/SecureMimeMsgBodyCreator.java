package com.terracetech.tims.webmail.mail.manager.body;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.ByteArrayDataSource;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mail.ibean.MailSecureInfoBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.util.FileUtil;

public class SecureMimeMsgBodyCreator extends AbstractMsgBodyCreator {
	
	private MimeMessage contentMimeMessage = null;
	
	public SecureMimeMsgBodyCreator(SenderInfoBean info) {
		super(info);
	}
	
	public SecureMimeMsgBodyCreator(SenderInfoBean info, MimeMessage contentMimeMessage) {
		super(info);
		this.contentMimeMessage = contentMimeMessage;
	}

	@Override
	public void execute(Multipart mp) throws NoSuchProviderException,
			MessagingException, UnsupportedEncodingException, IOException {
		
		MailSecureInfoBean secureInfo = info.getSecureInfo();
				
        String fileName = "TSM_SECURE_MIME.TSM";
        String cryptMethod = secureInfo.getCryptMethod();        
       
	        	    
	    File tmpFile = FileUtil.makeTmpFile();
	    
	    FileOutputStream fos = new FileOutputStream(tmpFile,true);
		Enumeration enumer = contentMimeMessage.getAllHeaderLines();

        while (enumer.hasMoreElements()) {
            String header = (String) enumer.nextElement();

            fos.write(header.getBytes());
            fos.write('\n');
        }

        fos.write('\n');

		InputStream in = contentMimeMessage.getInputStream();
		
        byte[] buffer = new byte[1024 * 1024];
        int n;

        while ((n = in.read(buffer, 0, buffer.length)) != -1) {
        	fos.write(buffer, 0, n);
        }
        
        fos.flush();
        fos.close();
        in.close();
        
        byte[] mimeByte;
		try {
			mimeByte = FileUtil.readFile(tmpFile);
		} catch (Exception e1) {
			throw new MessagingException("MIME GET BYTE ERROR");
		}        
        //TODO  ENCRPTY
		String encryptContents = null;
        try {
        	encryptContents = SecureUtil.encrypt(cryptMethod, EnvConstants.ENCRYPT_KEY, new String(mimeByte));
		} catch (Exception e) {
			throw new MessagingException("MIME ENCRYPT ERROR");
		}       
		
	        	    
        MimeBodyPart mbp = new MimeBodyPart();
		mbp.setDataHandler(new DataHandler(new ByteArrayDataSource(encryptContents.getBytes())));
		
		mbp.setHeader("Content-Type", TMailUtility
				.getMIMEContentType(fileName));
		mbp.setFileName(fileName);
		mbp.addHeader("Content-Transfer-Encoding","binary");
		mbp.addHeader("Mime-Version", "1.0");
		mbp.setHeader("X-SECURE-MIMEPART", "TRUE");
		mp.addBodyPart(mbp);
		
		info.pushStack(mbp);
		info.addTempFile(tmpFile);

	}


	@Override
	public boolean isAcceptable() {		
		return info.isSecureMail();
	}

}
