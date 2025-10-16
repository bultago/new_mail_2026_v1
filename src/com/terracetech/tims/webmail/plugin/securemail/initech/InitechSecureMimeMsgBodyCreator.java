package com.terracetech.tims.webmail.plugin.securemail.initech;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;

import com.initech.inimas.INISym;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.ByteArrayDataSource;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.ExtPartConstants;
import com.terracetech.tims.webmail.mail.ibean.MailSecureInfoBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.body.AbstractMsgBodyCreator;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class InitechSecureMimeMsgBodyCreator extends AbstractMsgBodyCreator {
	
	public InitechSecureMimeMsgBodyCreator(SenderInfoBean info) {
		super(info);
	}

	@Override
	public void execute(Multipart mp) throws NoSuchProviderException,
			MessagingException, UnsupportedEncodingException, IOException {
		MailSecureInfoBean secureInfoBean = info.getSecureInfo();
		INISym iniSym = INISym.getInstance();
		try {
			iniSym.init(ExtPartConstants.getExtPartConfig("initech.properties.path"));
			byte[] symKey = iniSym.createSymKey(ExtPartConstants.getExtPartConfig("initech.clientkey"), 
					secureInfoBean.getSecureMailPassword());
			String tmpDir = EnvConstants.getBasicSetting("tmpdir");
			String fileFlag = (INISym.props).getProperty("FileFlag");
			
			List<InitechByteContents> encryptContentList = new ArrayList<InitechByteContents>();			
			encryptContentList.add(new InitechByteContents(iniSym.getBeginTag()));
			encryptContentList.add(new InitechByteContents(iniSym.encryptBody(symKey, info.getBodyContent().getBytes())));
			
			
			List<InitechByteContents> fileContentList = null;
			encryptContentList.add(new InitechByteContents(iniSym.getFileTag()));			
			if(fileFlag.equalsIgnoreCase("Y")){
				String attlist = info.getAttlist();				
				if(attlist != null && attlist.length() > 0){					
					StringTokenizer st = new StringTokenizer(info.getAttlist(), "\n");
						
					while (st.hasMoreElements()) {
						fileContentList = new ArrayList<InitechByteContents>();
						String strTmp = (String) st.nextElement();
						StringTokenizer st_sub = new StringTokenizer(strTmp, "\t");
						String strFile = (String) st_sub.nextElement();
						String strName = (String) st_sub.nextElement();
			
						File file_src = new File(tmpDir + EnvConstants.DIR_SEPARATOR +strFile);

						//2012.05.17 - 첨부파일이 다른 장비로 올라갔을 경우 check - SS
						if (!file_src.exists()) {
							if (FileUtil.checkUploadFiles(strFile)) {
								file_src = new File(tmpDir + EnvConstants.DIR_SEPARATOR + strFile);
							}
						}
						//2012.05.17 - 첨부파일이 다른 장비로 올라갔을 경우 check - EE
						encryptContentList.add(new InitechByteContents(iniSym.encryptAttachedFile(symKey, 
								strName, FileUtil.getByteFile(tmpDir + EnvConstants.DIR_SEPARATOR +strFile))));	
						 
					}					
					fileContentList = null;
				}
				
				String emlName = null;
				String tmpSubject = null;		
				TMailMessage[] messages = info.getFlagMessages();
				
				if(messages != null){					
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
			            
			            						
			            encryptContentList.add(new InitechByteContents(iniSym.encryptAttachedFile(symKey, 
								emlName, FileUtil.getByteFile(tmpFile.getPath()))));
			            			
					}
				}				
			}
			
			encryptContentList.add(new InitechByteContents(iniSym.getFileEndTag()));			
			
			encryptContentList.add(new InitechByteContents(iniSym.getEndTag()));
			encryptContentList.add(new InitechByteContents(iniSym.getContentTag()));			 
			
			byte[] encryptedMsg = makeByteContents(encryptContentList);			
			MimeBodyPart mbp = new MimeBodyPart();
			
			mbp.setDataHandler(new DataHandler(new ByteArrayDataSource(encryptedMsg)));
			String fileName = "INISAFEMail.htm";
			String esc_name = MimeUtility.encodeText(fileName,
					info.getCharset(), "B");

			mbp.setHeader("Content-Type", TMailUtility
					.getMIMEContentType(fileName));
			mbp.setFileName(esc_name);

			mbp.addHeader("Content-Transfer-Encoding", "base64");
			mp.addBodyPart(mbp);
			
			info.pushStack(mbp);			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new MessagingException("initech secure mail Exception",e);
		}
		
	}
	
	private byte[] makeByteContents(List<InitechByteContents> contentList){
		byte[] content = null;
		InitechByteContents[] contents = new InitechByteContents[contentList.size()];
		contentList.toArray(contents);
		
		int length = 0;
		for (InitechByteContents bodyContent : contents) {
			length += bodyContent.getContentLength();
		}		
		content = new byte[length];
		length = 0; 
		for (InitechByteContents bodyContent : contents) {
			System.arraycopy(bodyContent.getContentByte(), 0, content, length, 
					bodyContent.getContentLength());
			length+=bodyContent.getContentLength();
		}
		return content;
	}

	@Override
	public boolean isAcceptable() {		
		return true;
	}
	
	class InitechByteContents {
		private byte[] contentByte = null;
		
		public InitechByteContents(byte[] contentByte) {
			this.contentByte = contentByte;
		}
		
		public byte[] getContentByte(){
			return contentByte;
		}
		
		public void setContentByte(byte[] contentByte){
			this.contentByte = contentByte;
		}
		
		public int getContentLength(){
			return contentByte.length;
		}
		
	}

}




