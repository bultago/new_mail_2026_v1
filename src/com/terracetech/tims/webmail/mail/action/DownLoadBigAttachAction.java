/**
 * DownLoadBigAttachAction.java 2009. 4. 3.
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
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mail.manager.BigattachManager;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mail.vo.MailBigAttachVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>DownLoadBigAttachAction.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class DownLoadBigAttachAction extends BaseAction {
	
	private BigattachManager bigAttachManager = null;
	private MailUserManager userManager = null;
	private SystemConfigManager systemConfigManager = null;
	
	public DownLoadBigAttachAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}

	/**
	 * @param bigAttachManager �Ķ���͸� bigAttachManager���� ����
	 */
	public void setBigAttachManager(BigattachManager bigAttachManager) {
		this.bigAttachManager = bigAttachManager;
	}	
	/**
	 * @param userManager �Ķ���͸� userManager���� ����
	 */
	public void setUserManager(MailUserManager userManager) {
		this.userManager = userManager;
	}
	
	public void setSystemConfigManager(SystemConfigManager systemConfigManager) {
		this.systemConfigManager = systemConfigManager;
	}

	/**
	 * <p></p>
	 */
	private static final long serialVersionUID = 1199378390937684971L;
	
	public String execute() throws Exception{
		String userDownLoad = request.getParameter("udown");
		boolean isUserDownLoad = "T".equals(userDownLoad);
		String param = request.getParameter("param");		
		String email = null; 	
		String uid = null;
		String[] userPram = null;
		I18nResources resource = getMessageResource();
		
		int limitCount = 0;
		boolean isLimitCountUse = false;
		
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder folder = null;
		TMailMessage message = null;
		
		String forward = null;
		String msg = null;
		boolean isSuccess = true;
		try {
			Map attachConfigMap = systemConfigManager.getAttachConfig();
			String cryptMethod = systemConfigManager.getCryptMethod();
			String encParam = null;
			if(!isUserDownLoad){			
				encParam = null;
				try {
					encParam = SecureUtil.decrypt(cryptMethod, EnvConstants.ENCRYPT_KEY,param);
				} catch (Exception e) {
					msg = resource.getMessage("bigattach.14");
					isSuccess = false;
				}
			}
			
			if(isSuccess){
				if(!isUserDownLoad){
					Map<String, String> paramMap = new HashMap<String, String>();				
					StringTokenizer stA = new StringTokenizer(encParam,"&");			       
			        while(stA.hasMoreElements()){	       	
			        	StringTokenizer stB = new StringTokenizer((String)stA.nextElement(),"=");	    
			        	if(stB.countTokens()==2){
			        		paramMap.put((String)stB.nextElement(),(String)stB.nextElement());
			        	}
			        }
			        
			        email = paramMap.get("email");
			        uid = paramMap.get("uid");
					
			        isLimitCountUse = "on".equalsIgnoreCase((String)attachConfigMap.get("bigattach_download_limited"));
					limitCount = Integer.parseInt((String)attachConfigMap.get("bigattach_download"));					
				} else {
					email = request.getParameter("email");
			        uid = request.getParameter("uid");
				}
				
				userPram = email.split("@");
				User downUser = userManager.readUserAuthInfo(userPram[0], userPram[1]);
				MailBigAttachVO vo = bigAttachManager.getBigAttachInfo(
						Integer.parseInt(downUser.get(User.MAIL_USER_SEQ)), uid);
				
				if(vo != null){
					int downLoadCnt = vo.getDownloadCount();
					String expireDay = vo.getExpireTime();
					Date tm = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
					long today = Long.parseLong(sdf.format(tm));
					long expiredate = Long.parseLong(expireDay);
					
					if(!isUserDownLoad){
						if(isLimitCountUse && limitCount == downLoadCnt){
							msg = resource.getMessage("bigattach.01");
							isSuccess = false;
						} else if(expiredate < today){
							msg = resource.getMessage("bigattach.02");
							isSuccess = false;
						} else {
							if(isLimitCountUse){
								downLoadCnt++;			
								vo.setDownloadCount(downLoadCnt);
							} else {
								vo.setDownloadCount(0);
							}						
							bigAttachManager.updateBigattachInfo(vo);						   
						}
					}
					
					if(isSuccess){					
						Map<String, String>confMap = 	bigAttachManager.getBigAttachConnectInfo(downUser);
						store = factory.connect(request.getRemoteAddr(), confMap);
						folder = store.getFolder(FolderHandler.BIGATTACHHOME);
						
						folder.open(false);
				        message = folder.getMessageByUID(Long.parseLong(uid), false);
				        
				        String fileName = message.getSubject();
				        fileName = new String(fileName.getBytes());
						String fileSize = message.getWebFolderFileSize();
				       
						String agent = request.getHeader("user-agent");			
						fileName = StringUtils.getDownloadFileName(fileName, agent);			
						
						response.setHeader("Content-Type",
					        	"application/download; name=\"" + fileName + "\"");
						response.setHeader("Content-Disposition",
					        	"attachment; filename=\"" + fileName + "\"");			
						response.setHeader("Content-Length", fileSize);
						
						BufferedOutputStream out = new BufferedOutputStream(
								response.getOutputStream());
						
						BufferedInputStream in =  new BufferedInputStream(message.getInputStream());
						
						try {
							byte[] buffer = new byte[1024 * 4];
							int n;
							while ((n = in.read(buffer, 0, buffer.length)) != -1) {
								out.write(buffer, 0, n);
							}
						} catch (Exception e) {
							LogManager.writeErr(this, e.getMessage(), e);
						}
						
						in.close();						
						out.close();
				        
				        folder.close(false);
					}
				} else {
					isSuccess = false;
					msg = resource.getMessage("bigattach.08");
				}
			}
	        
		} catch (Exception e) {
			if(e.getCause() instanceof SocketException){
			}else{
				LogManager.writeErr(this, e.getMessage(), e);	
			}
			msg = resource.getMessage("bigattach.08");
			isSuccess = false;
		} finally {
			if(store !=null && store.isConnected())
				store.close();
				
		}
		
		if(!isSuccess){			
			request.setAttribute("errMsg",msg);
			request.setAttribute("actionType", "close");								
			forward = "errorAlert";
		}
		
		return forward;
	}
	

}
