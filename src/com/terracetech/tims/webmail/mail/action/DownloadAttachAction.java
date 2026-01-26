/**
 * DownoadAttachAction.java 2009. 2. 4.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;

import org.apache.commons.codec.binary.Base64;

import org.eclipse.angus.mail.imap.IMAPBodyPart;
import org.eclipse.angus.mail.imap.IMAPInputStream;
import org.eclipse.angus.mail.imap.IMAPMessage;
import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.TMailTnefAttach;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.MessageUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>DownoadAttachAction.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class DownloadAttachAction extends BaseAction {
	
	private MailUserManager userManager = null;
	
	public void setUserManager(MailUserManager userManager) {
		this.userManager = userManager;
	}
	
	public DownloadAttachAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	/**
	 * <p></p>
	 */
	private static final long serialVersionUID = 4066261537274597402L;
	
	public String execute() throws Exception{
		
		String sharedFlag = request.getParameter("sharedFlag");
		boolean isShared = (sharedFlag != null && sharedFlag.equals("shared"))?true:false;
		
		String downType = request.getParameter("type");
		String tnefKey = request.getParameter("tnefKey");
		
		String securemode = request.getParameter("secure");
		boolean isSecureMode = (securemode != null && "on".equals(securemode))?true:false;
		String cryptMethod = request.getParameter("cryptMethod");
		String domainParam = request.getParameter("domainParam");
		String userParam = request.getParameter("userParam");
		
		
			
		String sharedUserSeq = request.getParameter("sharedUserSeq");
		String sharedFolderName = request.getParameter("sharedFolderName");		
		
		String folderName = request.getParameter("folder");
		String uid = request.getParameter("uid");
		String part = request.getParameter("part");
		String nestedPart = request.getParameter("nestedPart");
		
		String downAgent = request.getParameter("downAgent");
		
		if(isShared){
			folderName = sharedFolderName;
		}
		
		if ("mobile".equals(downAgent)) {
			folderName = StringUtils.doubleUrlDecode(folderName);
		}
		
		if(uid == null){
			log.warn("USER["+user.get(User.MAIL_UID)+"] download attach Message UID is null!");
			return null;
		}
		
		if(folderName == null){
			log.warn("USER["+user.get(User.MAIL_UID)+"] download attach FolderName is null!");
			return null;
		}
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder folder = null;
		TMailMessage message = null;
		Stack<String> nestedPartStatck = new Stack<String>();
		try {
			
			//User user = EnvConstants.getTestUser();			
	        
	        if(isSecureMode){
	        		        	
	        	try {	        	
		        	String decodeCryptMethod = SecureUtil.decrypt(SymmetricCrypt.AES,EnvConstants.ENCRYPT_KEY,
							TMailUtility.IMAPFolderDecode(cryptMethod));
		        	
		        	if(domainParam == null)throw new Exception("secure mail download attach domainParam is null!");
					if(userParam == null)throw new Exception("secure mail download attach userParam is null!");
		        	
		        	int domainSeq = Integer.parseInt(domainParam);
			        int userSeq = Integer.parseInt(userParam);
			        
			        User readUser = userManager.readUserMailConnectionInfo(userSeq, domainSeq);		
					readUser.put(User.IMAP_LOGIN_ARGS, 
							readUser.get(User.MESSAGE_STORE) + " 0 0 0 0 0 0 "+userSeq +" " +domainSeq);
					this.user = readUser;
					store = factory.connect(request.getRemoteAddr(), readUser);
					
					folder = store.getFolder(folderName);
			        folder.open(false);
			        message = folder.getMessageByUID(Long.parseLong(uid),false);
			        		
		        	byte[] mimeByte = null;	
		        	TMailPart[] attachPart = message.getAttachFiles();
		        	TMailPart secureMailPart = null;
				
					String val = null;
					for (int i = 0; i < attachPart.length; i++) {
						val = attachPart[i].getFileName();
						if(val.equals("TSM_SECURE_MIME.TSM")){
							secureMailPart = attachPart[i]; 
							break;
						}
					}
					
					mimeByte = MessageUtil.getMimeByte(((IMAPBodyPart)secureMailPart.getPart()).getInputStream());					
					byte[] decryptContents = SecureUtil.decryptToByte(decodeCryptMethod, EnvConstants.ENCRYPT_KEY, new String(mimeByte));
					MimeMessage parseMessage = 
						new MimeMessage(factory.getSession(),new ByteArrayInputStream(decryptContents));
					message = new TMailMessage(parseMessage);
					
					parseMessage.saveChanges();
					
				} catch (Exception e1) {					
					LogManager.writeErr(this, e1.getMessage(), e1);
					throw e1;
				}
			} else {
				if(user ==null)
					return null;
				
				try{
					if(uid == null)throw new Exception("USER["+user.get(User.MAIL_UID)+"] download attach Message UID is null!");
					if(folderName == null)throw new Exception("USER["+user.get(User.MAIL_UID)+"] download attach FolderName is null!");		
					
					store = factory.connect(isShared, sharedUserSeq, request.getRemoteAddr(), user);		        
					
			        folder = store.getFolder(folderName);
			        folder.open(false);
			        message = folder.getMessageByUID(Long.parseLong(uid),false);
				} catch (Exception e1) {					
					LogManager.writeErr(this, e1.getMessage(), e1);
					throw e1;
				}
			}
	        if(StringUtils.isNotEmpty(part)){
		        if(nestedPart != null && nestedPart.length() > 0){
					String[] parts = nestedPart.split("\\|");
					for (int i = (parts.length-1); i >= 0; i--) {
						nestedPartStatck.push(parts[i]);
					}				
				} else {
					nestedPartStatck.push(part);				
				}
	        }
	        InputStream in = null;
	        String fileName = null;			
	        TMailPart attachPart = null;
	        boolean isAttachRFC822 = false;
	        
	        if(StringUtils.isNotEmpty(part)){
				attachPart = getNestedPart(message,factory.getSession(),nestedPartStatck);
				isAttachRFC822 = attachPart.isMimeType("message/rfc822");
				if("tnef".equals(downType)){
					Map<String, TMailTnefAttach> map = attachPart.getTnefAttachMap();
					TMailTnefAttach tnefAttach = map.get(tnefKey);
					fileName = tnefAttach.getFileName();
					in = tnefAttach.getAttachment().getRawData();
				} 
				
				else {
					fileName = attachPart.getFileName();				
					in = (!isAttachRFC822)?attachPart.getInputStream():null;
				}			
	        }else{
				in = new BufferedInputStream(message.getInputStream());
				fileName = MimeUtility.decodeText(message.getFileName());
	        }
			String agent 	= request.getHeader("user-agent");			
			fileName = StringUtils.getDownloadFileName(fileName, agent);			
			
		    String contentType = getContentTypeHeder(downAgent, fileName, context.getMimeType(fileName));

		     response.setHeader("Content-Type", 
		        contentType);
			response.setHeader("Content-Disposition",
		        	"attachment; filename=\"" + fileName + "\"");			
			
			BufferedOutputStream out = new BufferedOutputStream(
					response.getOutputStream());
			
			try {
				
				if(isAttachRFC822){
					Object msg = attachPart.getContent();
					Enumeration enumer = null;					
	            	if(msg instanceof IMAPMessage){            		
	            		enumer = ((IMAPMessage)msg).getAllHeaderLines();            		
	            	} else if(msg instanceof MimeMessage){
	            		enumer = ((MimeMessage)msg).getAllHeaderLines();
	            	}
	            	
					if (enumer != null) {
						boolean isBase64 = false;
						StringBuffer base64String = new StringBuffer();
						while (enumer.hasMoreElements()) {
							byte[] bytes = ((String) enumer.nextElement()).getBytes();
							if(!isBase64)	isBase64 = Base64.isArrayByteBase64(bytes);
							if(!isBase64){
								out.write(bytes);
								out.write("\n".getBytes());
							}else{
								base64String.append(new String(bytes));
								base64String.append("\n");
							}
						}
						if(isBase64){
							out.write(Base64.decodeBase64(base64String.toString().getBytes()));
						}

						out.write("\n".getBytes());

					}
        	if(msg instanceof IMAPMessage){
	            		in = ((IMAPMessage)msg).getRawInputStream();
	            	} else if(msg instanceof MimeMessage){
	            		in = ((MimeMessage)msg).getRawInputStream();
	            	}
	            		            	
				}
				
				byte[] buffer = new byte[1024 * 1024];
	            int n;
	            
				while ((n = in.read(buffer, 0, buffer.length)) != -1) {
	            	out.write(buffer, 0, n);
	            }
				
				
			} catch (Exception ex) {
				if(ex.getCause() instanceof SocketException){
				}else{
					LogManager.writeErr(this, ex.getMessage(), ex);	
				}
			} finally {
				out.close();
				in.close();
			}   			
			
		} catch (Exception e) {
			if(e.getCause() instanceof SocketException){
			}else{
				LogManager.writeErr(this, e.getMessage(), e);	
			}
		}finally{
			try {
				if(store !=null && store.isConnected())
					store.close();
				if(folder !=null && folder.isOpen())
					folder.close(false);				
			} catch (Exception e2) {
				LogManager.writeErr(this, e2.getMessage(), e2);
			}
		}		
		
		return null;
	}

	private String getContentTypeHeder(String downAgent, String fileName,
			String mimeType) {
		String contentType = "";
		if ("mobile".equals(downAgent))
			contentType = fileName.endsWith(".hwp") ? "application/haansofthwp"
					: "application/octet-stream";
		else {
			mimeType = (mimeType != null) ? mimeType : getContentType(fileName);
			contentType = mimeType + "; name=\"" + fileName + "\"";
		}

		return contentType;
	}
	
	private TMailPart getNestedPart(TMailMessage sourceMessage,
			Session session,
			Stack<String> nestedPartStatck) throws Exception{
		TMailPart returnPart = null;
		if(!nestedPartStatck.empty()){			
			String part = nestedPartStatck.pop();		
			StringTokenizer st = new StringTokenizer(part, ":");
			int[] part2 = null;
	        if(st.countTokens() > 0){
				part2 = new int[st.countTokens()];
		        for (int i = 0; i < part2.length; i++) {
		            part2[i] = Integer.parseInt(st.nextToken());
		        }
	        } else {
	        	part2 = new int[1];
	        	part2[0] = Integer.parseInt(part);
	        }	        
	        
        	returnPart = new TMailPart(part,sourceMessage.getPart(part2));
        	if(!nestedPartStatck.empty()){
        		Object msg = returnPart.getContent();
        		TMailMessage message = null;
        		if(msg instanceof IMAPInputStream){
        			message = new TMailMessage(new MimeMessage(session,(IMAPInputStream)msg));
        		} else {
        			message = new TMailMessage((MimeMessage)msg);
        		}
        		returnPart = getNestedPart(message,session,nestedPartStatck);
        	}	        
		}
        
        return returnPart;		
	}
	
	private String getContentType(String fileName){
    	String contentType = "application/download";
    	if(fileName != null){
    		int pos = fileName.lastIndexOf(".");
    		if(pos > -1){
    			String ext = fileName.substring(pos+1);
    			if("docx".equalsIgnoreCase(ext)){
    				contentType = "application/msword";
    			} else if("xlsx".equalsIgnoreCase(ext)){
    				contentType = "application/vnd.ms-excel";
    			} else if("pptx".equalsIgnoreCase(ext)){
    				contentType = "application/vnd.ms-powerpoint";
    			}    			
    		}
    		
    	}    	
    	return contentType;
    }
}
