/**
 * DownloadAllAttachAction.java 2009. 2. 23.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.bbs.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Map;
import java.util.StringTokenizer;

import com.terracetech.tims.webmail.util.StringUtils;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.util.ZipUtil;

/**
 * <p><strong>DownloadAllAttachAction.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author jhlee
 * @since Tims7
 * @version 7.0 
 */
public class DownloadAllAttachAction extends BaseAction {
	
	private static final long serialVersionUID = 7599504824370174717L;
	private BbsManager bbsManager = null;
	private long uid = 0;
	private String part = null;
	private int bbsId = 0;
	
	public void setBbsManager(BbsManager bbsManager) {
		this.bbsManager = bbsManager;
	}
	
	public String execute() throws Exception{

		User user = UserAuthManager.getUser(request);
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailFolder folder = null;
		try {

			Map<String, String> confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
			store = factory.connect(request.getRemoteAddr(), confMap);
			folder = store.getFolder(Integer.toString(bbsId));
			
			folder.open(false);
			TMailMessage message = folder.getMessageByUID(uid, false);
			TMailPart[] attParts = message.getAttachFiles();

	        String agent = request.getHeader("user-agent");
			String subject = message.getSubject();
			subject = (subject != null) ? subject : "No Subject";
			subject = subject.replaceAll("\\\\", "");
			subject = subject.replaceAll("[\t\n\r]", "_");
			subject = subject.replaceAll("[/:*?\"<>| ]", "_");
			String uniqueDir = user.get(User.EMAIL) + "_" + System.currentTimeMillis();
			String zipFileDir = EnvConstants.getBasicSetting("tmpdir")+EnvConstants.DIR_SEPARATOR+uniqueDir;  
			String zipFileName = uniqueDir + ".zip";
			String zipFilePath = EnvConstants.getBasicSetting("tmpdir") + EnvConstants.DIR_SEPARATOR + zipFileName;
			String zipFileName_stream = subject + ".zip";
			zipFileName_stream = 
				StringUtils.getDownloadFileName(zipFileName_stream, agent);
			try {
				String[] paths = part.split("_");
				
				StringTokenizer st = null;
				int[] attPath = null;
				TMailPart myPart = null;
				InputStream in = null;
				String fileName = "";
				File file = null;
				BufferedOutputStream output = null;
				ZipUtil zipUtil = null;
				FileOutputStream fos = null;
				int j;
				byte[] buf = null;
				File dir = new File(zipFileDir);
				if(!dir.exists()) dir.mkdirs();
				for (int k = 0; k < paths.length; k++) {

					try{
						st = new StringTokenizer(paths[k], ":");
						attPath = new int[st.countTokens()];
						for (int i = 0; i < attPath.length; i++) {
							attPath[i] = Integer.parseInt(st.nextToken());
						}
	
						myPart = new TMailPart(paths[k], message.getPart(attPath));
						fileName = myPart.getFileName();
						in = myPart.getInputStream();
	
						file = new File(zipFileDir +EnvConstants.DIR_SEPARATOR + fileName);
						output = new BufferedOutputStream(new FileOutputStream(file));
	
						j = 0;
						buf = new byte[1024 * 1024];
						while ((j = in.read(buf)) != -1) {
							output.write(buf, 0, j);
						}

					}catch(Exception e){
						e.printStackTrace();
						continue;
					}finally{
						try{ if(in != null) in.close(); }catch(Exception ex){}
						try{ if(output != null) output.close(); }catch(Exception ex){}
					}
				} // end for
				
				zipUtil = new ZipUtil();
				zipUtil.setDebug("true".equalsIgnoreCase(EnvConstants.getBasicSetting("log.debug")));
				String charset = StringUtils.isEmpty(EnvConstants.getMailSetting("default.zip.charset")) ? "EUC-KR" : EnvConstants.getMailSetting("default.zip.charset");
				zipUtil.zip(new File(zipFileDir), charset, false);
				
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
			}
	        
			response.setHeader("Content-Type",
					"application/download; name=\"" + zipFileName_stream + "\"");
			response.setHeader("Content-Disposition",
				"attachment; filename=\"" + zipFileName_stream + "\"");

			BufferedOutputStream out = new BufferedOutputStream(
				response.getOutputStream());

			FileInputStream in = new FileInputStream(zipFilePath);
			byte[] buffer = new byte[1024 * 1024];
			int n;

			while((n = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, n);
			}

			in.close();
			out.close();

			File f = new File(zipFilePath);
			f.delete();
			
		} catch (Exception e) {
			if(e.getCause() instanceof SocketException){
			}else{
				LogManager.writeErr(this, e.getMessage(), e);	
			}
		}finally{
			store.close();
			folder.close(false);
		}		
		
		return null;
	}
	
	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public int getBbsId() {
		return bbsId;
	}

	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}
	
}
