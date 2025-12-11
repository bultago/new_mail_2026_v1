/**
 * AttachForwardMsgBodyCreator.java 2008. 12. 4.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager.body;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeUtility;

import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.util.FileUtil;

/**
 * <p><strong>AttachForwardMsgBodyCreator.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author jpjung
 * @since Tims7
 * @version 7.0 
 */
@SuppressWarnings("unchecked")
public class AttachForwardMsgBodyCreator extends AbstractMsgBodyCreator {

	
	public AttachForwardMsgBodyCreator(SenderInfoBean info) {
		super(info);
	}

	/**
	 * <p></p>
	 *
	 * @see com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator#execute(java.lang.String, javax.mail.Multipart)
	 * @param content
	 * @param mp
	 * @throws NoSuchProviderException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 * @throws IOException 
	 */
	public void execute(Multipart mp)
			throws NoSuchProviderException, MessagingException,
			UnsupportedEncodingException, IOException {
		
		String emlName = null;
		String tmpSubject = null;		
		TMailMessage[] messages = info.getFlagMessages();
		
		for (int i = 0; i < messages.length; i++) {
			tmpSubject = messages[i].getSubject();
            emlName = (tmpSubject != null) ? tmpSubject : "No Subject";	
    	    emlName = emlName.replaceAll("\\\\", "");
    	    emlName = emlName.replaceAll("[\t\n\r]", " ");
    	    emlName = emlName.replaceAll("[/:*?\"<>|]", "_");
    	    emlName = emlName+".eml";
    	        	    
    	    File tmpFile = FileUtil.makeTmpFile();
    	    
    	    FileOutputStream fos = new FileOutputStream(tmpFile,true);
    		Enumeration enumer = messages[i].getAllHeaderLines();

            while (enumer.hasMoreElements()) {
                String header = (String) enumer.nextElement();

                fos.write(header.getBytes());
                fos.write('\n');
            }

            fos.write('\n');

    		InputStream in = messages[i].getRawInputStream();
    		
            byte[] buffer = new byte[1024 * 1024];
            int n;

            while ((n = in.read(buffer, 0, buffer.length)) != -1) {
            	fos.write(buffer, 0, n);
            }
            
            fos.flush();
            fos.close();
            in.close();
            
            MimeBodyPart mbp = new MimeBodyPart();
        	
			FileDataSource fds = new FileDataSource(tmpFile.getPath());
			mbp.setDataHandler(new DataHandler(fds));
			
			String esc_name = MimeUtility.encodeText(emlName,
					info.getCharset(), "B");

			mbp.setHeader("Content-Type", "application/octet-stream");
			mbp.setFileName(esc_name);
			mbp.addHeader("Content-Transfer-Encoding", "7bit");
			mp.addBodyPart(mbp);            
            
			info.pushStack(mbp);
            info.addTempFile(tmpFile);			
		}
		
	}

	/**
	 * <p></p>
	 *
	 * @see com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator#isAcceptable()
	 * @return 
	 */
	public boolean isAcceptable() {
		return info.isForwardAttached();
	}

}
