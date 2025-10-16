package com.terracetech.tims.webmail.mail.manager.body;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.ByteArrayDataSource;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mail.ibean.MailSecureInfoBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class SecureFormMsgBodyCreator extends AbstractMsgBodyCreator {

	public SecureFormMsgBodyCreator(SenderInfoBean info) {
		super(info);
	}

	@Override
	public void execute(Multipart mp) throws NoSuchProviderException,
			MessagingException, UnsupportedEncodingException, IOException {
		MailSecureInfoBean secureInfo = info.getSecureInfo();
		String formHtmlFile = secureInfo.getSecureFormFile(); 
		String localhost = secureInfo.getSslHost();
		User user = info.getUser();
		String locale = user.get(User.LOCALE);
		String secureParam = "host:"+user.get(User.MAIL_HOST)+";"+
								"email:"+user.get(User.EMAIL)+";"+
								"userseq:"+user.get(User.MAIL_USER_SEQ)+";"+
								"domainseq:"+user.get(User.MAIL_DOMAIN_SEQ)+";"+
								"arg:"+user.get(User.IMAP_LOGIN_ARGS)+";"+
								"folder:"+FolderHandler.SENT +";"+
								"msgid:"+info.getMessageId();
		
		try {
			secureParam = SecureUtil.encrypt(SymmetricCrypt.AES, 
					EnvConstants.ENCRYPT_KEY,
					secureParam);			
		} catch (Exception e) {
			throw new MessagingException("HEADER ENCRYPT ERROR");			
		}
		
		String fileContents = new String(FileUtil.readFile(new File(formHtmlFile)),"UTF-8");
		fileContents = fileContents.replaceAll("\\{LOCAL_HOST\\}", localhost);
		fileContents = fileContents.replaceAll("\\{FONT_STYLE\\}",secureInfo.getFontStyle());
		fileContents = fileContents.replaceAll("\\{SECURE_ACTION\\}", 
				EnvConstants.getMailSetting("secure.action"));
		fileContents = fileContents.replaceAll("\\{SECURE_PARAM\\}", secureParam);
		fileContents = fileContents.replaceAll("\\{SECURE_HINT\\}", 
				StringUtils.javaToHtml(secureInfo.getSecureMailHint()));
		fileContents = secureInfo.getSecureMailMsg(fileContents);
		
		MimeBodyPart mbp = new MimeBodyPart();
		
		mbp.setDataHandler(new DataHandler(new ByteArrayDataSource(fileContents.getBytes())));
		
		String formFileName = "secureForm.html";
		String esc_name = MimeUtility.encodeText(formFileName,
				info.getCharset(), "B");

		mbp.setHeader("Content-Type", TMailUtility
				.getMIMEContentType(formFileName));
		mbp.setFileName(esc_name);

		mbp.addHeader("Content-Transfer-Encoding", "base64");
		mp.addBodyPart(mbp);
		
		info.pushStack(mbp);		
	}

	@Override
	public boolean isAcceptable() {		
		return info.isSecureMail();
	}

}
