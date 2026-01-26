/**
 * FolderManageAction.java 2009. 3. 6.
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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.search.TMailSearchQuery;
import com.terracetech.tims.mail.tag.TMailTag;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.ladmin.Protocol;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.LadminManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mail.ibean.MailFolderBackupBean;
import com.terracetech.tims.webmail.mail.ibean.MailFolderBean;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mail.vo.SharedFolderVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.ResponseUtil;

/**
 * <p><strong>FolderManageAction.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */

@SuppressWarnings("unchecked")
public class FolderManageAction extends BaseAction {
	
	private static final long serialVersionUID = -2767594908053907162L;
	private MailManager mailManager = null;
	private LadminManager ladminManager = null;
	private SystemConfigManager systemConfigManager = null;
	private SettingManager userSettingManager = null;
	
	public void setSystemConfigManager(SystemConfigManager systemConfigManager) {
		this.systemConfigManager = systemConfigManager;
	}
	
	/**
	 * @param mailManager �Ķ���͸� mailManager���� ����
	 */
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}

	/**
	 * @param ladminManager �Ķ���͸� ladminManager���� ����
	 */
	public void setLadminManager(LadminManager ladminManager) {
		this.ladminManager = ladminManager;
	}
	
	public void setUserSettingManager(SettingManager userSettingManager) {
	        this.userSettingManager = userSettingManager;
	}

	public String viewFolderStateInfo()  throws Exception {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		I18nResources msgResource = getMessageResource("mail");
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		Protocol ladminProtocol  = null;
		String reload = request.getParameter("reload");		
		reload = (reload != null && reload.equals("on"))?"on":"off";
		
		try {
			store = factory.connect(request.getRemoteAddr(), user);
			
			mailManager.setProcessResource(store, new I18nResources(new Locale(user.get(User.LOCALE))));
			
			MailFolderBean[] defaultFolders = 
				mailManager.getFolderList(EnvConstants.DEFAULT_FOLDER,true,userSeq);						
			
			MailFolderBean[] userFolders =  mailManager.getUserAgingFolderList(userSeq,true);
			
			List<SharedFolderVO> sharedFolderList = mailManager.getSharingFolders(userSeq);
			SharedFolderVO[] sharedFolders = null;
			if(sharedFolderList != null && sharedFolderList.size() > 0){
				sharedFolders = new SharedFolderVO[sharedFolderList.size()];
				sharedFolderList.toArray(sharedFolders);
			}
			
			TMailTag[] tagList = mailManager.getTagList(null);
			TMailSearchQuery[] searchFolderList = mailManager.getSearchFolders(null);			
			
			
			ladminProtocol = new Protocol(user.get(User.MAIL_HOST));
			ladminManager.setResource(ladminProtocol,msgResource);
			MailFolderBackupBean backupBean = ladminManager.getFolderBackupStatus(user.get(User.EMAIL));
			
			Map<String, String> searchConfigMap = systemConfigManager.getMailAdvanceSearchConfig();
			String mailSearchConfig = "{bodySearch:'"+searchConfigMap.get("bodySearch")+"',attachSearch:'"+searchConfigMap.get("attachSearch")+"'}";
			UserEtcInfoVO userEtcInfoVO = userSettingManager.readUserEtcInfo(userSeq);
			
			request.setAttribute("backupInfo",backupBean);
			request.setAttribute("dfolders",defaultFolders);
			request.setAttribute("ufolders",userFolders);
			request.setAttribute("sharedfolders",sharedFolders);
			request.setAttribute("tags",tagList);
			request.setAttribute("sfolders",searchFolderList);
			request.setAttribute("reload",reload);	
			request.setAttribute("mailSearchConfig", mailSearchConfig);
			request.setAttribute("isSearchAllFolder", "on".equalsIgnoreCase(userEtcInfoVO.getSearchAllFolder()));
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception("view folder manage error!");
		} finally{
			if(store !=null && store.isConnected())
				store.close();		
			if(ladminProtocol != null){
				ladminProtocol.logout();
			}
		}	
		
		return "list";
	}
	
	public String statusBackupFolder()  throws Exception {
		I18nResources msgResource = getMessageResource("mail");
		Protocol ladminProtocol = null;
		String email = user.get(User.EMAIL);
		JSONObject jObj = null;
		try {
			ladminProtocol = new Protocol(user.get(User.MAIL_HOST));
			ladminManager.setResource(ladminProtocol,msgResource);			
			MailFolderBackupBean backupBean = ladminManager.getFolderBackupStatus(email);
			jObj = backupBean.toJson();			
			
			jObj.put("result", "success");
		} catch (Exception e) {
			// TODO: handle exception
			jObj = new JSONObject();
			jObj.put("result", "error");
		} finally {
			if(ladminProtocol != null){
				ladminProtocol.logout();
			}
		}
		
		jObj.put("type", "status");
		ResponseUtil.processResponse(response, jObj);
		
		return null;
	}
	
	public String startBackupFolder()  throws Exception {
		JSONObject jObj = new JSONObject();
		String folderName = request.getParameter("folderName");
		String email = user.get(User.EMAIL);
		Protocol ladminProtocol = null;
		try {
			ladminProtocol = new Protocol(user.get(User.MAIL_HOST));
			ladminManager.setResource(ladminProtocol);
			
			if(ladminManager.startFolderBackup(email, folderName)){
				jObj.put("result", "success");				
			} else {
				jObj.put("result", "error");
			}			
		} catch (Exception e) {
			jObj.put("result", "error");
		} finally {
			if(ladminProtocol != null){
				ladminProtocol.logout();
			}
		}
		jObj.put("type", "start");
		ResponseUtil.processResponse(response, jObj);
		
		return null;		
	}
	/*
	public String downloadBackupFolder()  throws Exception {		
		Protocol ladminProtocol = null;
		InputStream is = null;		
		String email = user.get(User.EMAIL);
		
		try {
			ladminProtocol = new Protocol(user.get(User.MAIL_HOST));
			ladminManager.setResource(ladminProtocol);
			
			is = ladminManager.getFolderBackupFile(email);
			
			if(is != null){
				String fileName = (new SimpleDateFormat("yyyyMMdd").format(new Date())) 
								+"_" + email+ "_backup.zip";
				
				long fileSize = ladminManager.getFileSize();
				
				response.setHeader("Content-Type",
			        	"application/download; name=\"" + fileName + "\"");
				response.setHeader("Content-Disposition",
			        	"attachment; filename=\"" + fileName + "\"");
				response.setHeader("Content-Length",Long.toString(fileSize));
				
				BufferedOutputStream out = new BufferedOutputStream(
						response.getOutputStream());
				
				try {				
					byte[] buffer = new byte[1024 * 1024];
					int n;
					while ((n = is.read(buffer, 0, buffer.length)) != -1) {
						out.write(buffer, 0, n);
					}

					is.close();
					
				} catch (Exception ex) {
					LogManager.writeErr(this, ex.getMessage(), ex);
				} finally {
					out.close();
				}
			}
			
		} catch (Exception e) {			
			LogManager.writeErr(this, e.getMessage(), e);
		} finally {
			if(ladminProtocol != null){
				ladminProtocol.logout();
			}
		}	
		
		return null;		
	}
	*/
	
	public String downloadBackupFolder()  throws Exception {				
		
		String mailBackupFilePath = EnvConstants.getMailSetting("mail.backup.file.path");
		String mailBackupFileApi = EnvConstants.getMailSetting("mail.backup.file.api");
		String mailBackupFileSizeApi = EnvConstants.getMailSetting("mail.backup.file.size.api");
		String mailBackupFileApiUrl = EnvConstants.getMailSetting("mail.backup.file.api.url");
		
		String email = user.get(User.EMAIL);
		
		String filePath = mailBackupFilePath + "/" + email + ".zip";
		String data = "filePath="+ filePath;
		
		URL url = null;
		URLConnection conn = null;
		
			
		// 메일 백업 파일 사이즈
		String backupFileSizeUrl = mailBackupFileApiUrl + mailBackupFileSizeApi + "?filePath="+ filePath;
		long fileSize = 0L;

		OutputStreamWriter osw = null;
		BufferedReader br = null;
		
		try{
			url = new URL(backupFileSizeUrl);	
			conn = url.openConnection();
			conn.setDoOutput(true);
			osw = new OutputStreamWriter(conn.getOutputStream());
		    osw.write(data);
		    osw.flush();
		    
		    br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
		    StringBuffer sb = new StringBuffer() ;
		    String line;
		    while ((line = br.readLine()) != null) {
		    	sb.append(line) ;
		    }
		    
		    String fileSizeStr = sb.toString();
		    fileSize = Long.parseLong(fileSizeStr);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(osw != null){
				osw.close();
			}
			if(br != null){
				br.close();
			}
		}
	    
	    // 메일 파일 백업
		String backupFileSUrl = mailBackupFileApiUrl + mailBackupFileApi + "?filePath="+ filePath;
		
	    BufferedInputStream bis = null;
	    BufferedOutputStream bos = null;
	    
	    try{
			url = new URL(backupFileSUrl);			
		    conn = url.openConnection();
		    conn.setDoOutput(true);
		    osw = new OutputStreamWriter(conn.getOutputStream());
		    osw.write(data);
		    osw.flush();
		    
		    String fileName = (new SimpleDateFormat("yyyyMMdd").format(new Date())) 
					+"_" + email+ "_backup.zip";
	
			response.setHeader("Content-Type",
		        	"application/download; name=\"" + fileName + "\"");
			response.setHeader("Content-Disposition",
		        	"attachment; filename=\"" + fileName + "\"");
			response.setHeader("Content-Length",Long.toString(fileSize));
			
			bis = new BufferedInputStream(conn.getInputStream());
			bos = new BufferedOutputStream(response.getOutputStream());
				
			byte[] buffer = new byte[1024 * 1024];
			int n;
			while ((n = bis.read(buffer, 0, buffer.length)) != -1) {
				bos.write(buffer, 0, n);
			}
			
			deleteBackupFile();
			
	    }catch(Exception e){
	    	e.printStackTrace();
	    }finally{
	    	if(bis != null){
	    		bis.close();
	    	}
	    	if(bos != null){
	    		bos.close();
	    	}
	    }
	    
		return null;		
	}

	public String deleteBackupFile()  throws Exception {
	//	JSONObject jObj = new JSONObject();
		Protocol ladminProtocol = null;
		String email = user.get(User.EMAIL);
		try {
			ladminProtocol = new Protocol(user.get(User.MAIL_HOST));
			ladminManager.setResource(ladminProtocol);
			ladminManager.deleteFolderBackup(email);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ladminProtocol != null){
				ladminProtocol.logout();
			}
		}
		
		return null;		
	}
	
	public String deleteBackupFolder()  throws Exception {
		JSONObject jObj = new JSONObject();				
		Protocol ladminProtocol = null;
		String email = user.get(User.EMAIL);
		try {
			ladminProtocol = new Protocol(user.get(User.MAIL_HOST));
			ladminManager.setResource(ladminProtocol);
			
			if(ladminManager.deleteFolderBackup(email)){
				jObj.put("result", "success");				
			} else {
				jObj.put("result", "error");
			}			
		} catch (Exception e) {
			jObj.put("result", "error");
		} finally {
			if(ladminProtocol != null){
				ladminProtocol.logout();
			}
		}
		jObj.put("type", "delete");
		ResponseUtil.processResponse(response, jObj);
		
		return null;		
	}
	
	public String changeUserFolderAgingDay() throws Exception {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		JSONObject jObj = new JSONObject();
		
		String selectIdx = request.getParameter("selectIdx");
		String folderName = request.getParameter("folderName");
		int newAgingDay = Integer.parseInt(request.getParameter("newAgingDay"));
		int preAgingDay = Integer.parseInt(request.getParameter("preAgingDay"));		
		jObj.put("agingDay", newAgingDay);
		jObj.put("selectIdx", selectIdx);
		
		try {			
			mailManager.saveUserFolderAging(userSeq, preAgingDay, newAgingDay, folderName);
			jObj.put("result", "success");
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			jObj.put("result", "error");				
		}		
		
		ResponseUtil.processResponse(response, jObj);
		return null;	
	}

}
