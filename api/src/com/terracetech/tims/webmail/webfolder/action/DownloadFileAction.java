package com.terracetech.tims.webmail.webfolder.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.codec.binary.Base64;
import com.terracetech.tims.webmail.util.StringUtils;

import org.eclipse.angus.mail.imap.IMAPMessage;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.util.ZipUtil;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderDataVO;

public class DownloadFileAction extends BaseAction {
	
	private static final long serialVersionUID = -94720325571631332L;
	
	WebfolderManager webfolderManager = null;
	private int userSeq = 0;
	private String nodeNum = null;
	private String sroot = null;
	private String path = null;
	private String type = null;
	private String dwuid = null;
	
	public void setWebfolderManager(WebfolderManager webfolderManager) {
		this.webfolderManager = webfolderManager;
	}
	
	public String execute() throws Exception {
		
		TMailStore store = null;
		TMailFolder folder = null;
		TMailMessage[] messages = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource("webfolder");
		WebfolderDataVO webfolderDataVo = null;
		
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		boolean isJapen = "jp".equalsIgnoreCase(EnvConstants.getBasicSetting("setup.state"));
		try {
			boolean isRoot = false;
			Map<String, String> confMap = null;
			
			String folderType = "";
			if ("share".equals(type)) {
				String encodeSroot = sroot.replaceAll(EnvConstants.getBasicSetting("webfolder.root")+"\\.", "");
				encodeSroot = StringUtils.IMAPFolderEncode(encodeSroot);
				webfolderDataVo = webfolderManager.getShareInfo(encodeSroot, mailDomainSeq, userSeq, mailUserSeq);
				if (webfolderDataVo == null) {			
					request.setAttribute("msg", resource.getMessage("share.alert.001"));
					request.setAttribute("path", "/");
					request.setAttribute("type", "user");
					request.setAttribute("nodeNum", "0|");
					return "result";
				}
				confMap = webfolderManager.getWebfolderShareConnectInfo(userSeq);
				folderType = "S";
			}
			else if ("public".equals(type)) {
				confMap = webfolderManager.getWebfolderPublicConnectInfo(user.get(User.MAIL_DOMAIN));
				folderType = "P";
			}
			else{
				confMap = webfolderManager.getWebfolderMyConnectInfo(user);
			}
			
			if (StringUtils.isEmpty(path) || "/".equals(path)) {
				isRoot = true;
			}
			
			store = factory.connect(request.getRemoteAddr(), confMap);
			
			if (isRoot) {
			    folder = store.getDefaultWebFolder();
			}
			else {
				char separator = '.';
		        String xpath = path.substring(1);
		        xpath = xpath.replaceAll("/", "" + separator);
		        folder = store.getWebFolder(xpath);
			}
			
			String[] uids = dwuid.split("\\|");
						
			folder.open(false);
	        messages = folder.getMessagesByUID(FormatUtil.parseStringToLong(uids));
	        boolean existMessage = true;
	        if (messages == null) {
	            existMessage = false;
	        } else {
	            for (TMailMessage message : messages) {
	                if (message.getMessage() == null) {
	                    existMessage = false;
                            break;
	                }
	            }
	        }
	        
	        if (!existMessage) {
	            request.setAttribute("msg", resource.getMessage("share.notexist"));
                    request.setAttribute("path", path);
                    request.setAttribute("userSeq", userSeq);
                    request.setAttribute("type", type);
                    request.setAttribute("nodeNum", nodeNum);
                    request.setAttribute("sroot", sroot);
                    return "result"; 
	        }
	        
	        BufferedInputStream in = null;
	        String filename = null;
	        String fileSizeStr = null;
	        File zipFile = null;
	        ZipUtil zipUtil = null;
	        if(messages.length > 1){
	        	String uniqueDir = user.get(User.EMAIL) + "_" + System.currentTimeMillis();
				String zipFileDir = EnvConstants.getBasicSetting("tmpdir")+EnvConstants.DIR_SEPARATOR+uniqueDir;  
				String zipFileName = uniqueDir + ".zip";
				String zipFilePath = EnvConstants.getBasicSetting("tmpdir") + EnvConstants.DIR_SEPARATOR + zipFileName;
				System.out.println("");
	        	if(!isJapen){
					getMakeZipFile(zipFileDir,messages);
				} else{
					getJPMakeZipFile(zipFilePath,messages);
				}
	        	
	        	filename = "webfolder_files_"+System.currentTimeMillis() + ".zip";
	        	zipFile = new File(zipFilePath);
	        	in = new BufferedInputStream(new FileInputStream(zipFilePath));
	        	
	        } else {
	        	in = new BufferedInputStream(messages[0].getInputStream());
	        	filename = messages[0].getSubject();
	 	        fileSizeStr = messages[0].getToString();
	 	        
	 	       writeWebfolderLog(true, "wfolder_download_file", folderType + folder.getFullName(), "", "", "", Long.parseLong(messages[0].getWebFolderFileSize()), filename, "");
	        }
	        
	        String agent 	= request.getHeader("user-agent");			
	        filename = StringUtils.getDownloadFileName(filename, agent);			
			
			response.setHeader("Content-Type",
		        	"application/download; name=\"" + filename + "\"");
			response.setHeader("Content-Disposition",
		        	"attachment; filename=\"" + filename + "\"");		
			
			BufferedOutputStream out = new BufferedOutputStream(
					response.getOutputStream());	        
	       
	        long bufferSize = 1024 * 1024;
	        int n;
	        byte[] buffer = null;
	        try {
		        if (StringUtils.isNotEmpty(fileSizeStr)) {
			        fileSizeStr = fileSizeStr.replace("@", "");
			        long fileSize = Long.parseLong(fileSizeStr);
		        
			        bufferSize = (bufferSize > fileSize)?fileSize:bufferSize;
			        buffer = new byte[(int)bufferSize];
			        
			        long totalSize = 0;	        
			        while ((n = in.read(buffer, 0, buffer.length)) != -1) {	        	
			        	if(n >= bufferSize){
			        		out.write(buffer, 0, n);
			        	}
			            totalSize += n;
			            if((fileSize-totalSize) > 0 && ((fileSize-totalSize) < bufferSize)){
			            	buffer = new byte[(int)(fileSize-totalSize)];
			            	bufferSize = fileSize-totalSize;
			            }
			        }
		        } else {
		        	buffer = new byte[(int)bufferSize];
					while ((n = in.read(buffer, 0, buffer.length)) != -1) {
		            	out.write(buffer, 0, n);
		            }
		        }
	
		        out.flush();
	        } catch (Exception ex) {
	        	if(ex.getCause() instanceof SocketException){
				}else{
					LogManager.writeErr(this, ex.getMessage(), ex);	
				}
			} finally {
				in.close();
		        out.close();
		        if(zipFile != null)zipFile.delete();
			}
	        folder.close(true);
			
		}catch (Exception e) {
			if(e.getCause() instanceof SocketException){
			}else{
				LogManager.writeErr(this, e.getMessage(), e);	
			}
		}finally {
			if (store != null && store.isConnected()) store.close();
		}
		
		return null;
	}
	
	private void getMakeZipFile(String zipFileDir, TMailMessage[] messages) {
		try {
			Date date = new Date();
			String fileName = null;
			File file = null;
			File dir = null;
			InputStream in = null;
			BufferedOutputStream output = null;
			String[] paths = null;
			FileOutputStream fos = null; 
			TMailMessage mailMessage = null;
			
			dir = new File(zipFileDir);
			if(!dir.exists()) dir.mkdirs();
			ZipUtil zipUtil = null;
			int i = 0;
			byte[] buf = null;
			if(messages != null && messages.length > 0){
				for (int k=0;k<messages.length; k++) {
					mailMessage = messages[k];
					fileName = mailMessage.getSubject();
					
					in = mailMessage.getInputStream();
					file = new File(zipFileDir +EnvConstants.DIR_SEPARATOR + fileName);
					output = new BufferedOutputStream(new FileOutputStream(file));
					
					i = 0;
					buf = new byte[1024 * 1024];
					while ((i = in.read(buf)) != -1) {
						output.write(buf, 0, i);
					}
					if(in != null) in.close();
					if(output != null) output.close();
				} // end for
				
				zipUtil = new ZipUtil();
				zipUtil.setDebug("true".equalsIgnoreCase(EnvConstants.getBasicSetting("log.debug")));
				String charset = StringUtils.isEmpty(EnvConstants.getMailSetting("default.zip.charset")) ? "EUC-KR" : EnvConstants.getMailSetting("default.zip.charset");
				zipUtil.zip(new File(zipFileDir), charset, false);
			} // end if
			
		} catch (Exception e) {
			e.printStackTrace();			
			LogManager.writeErr(this, e.getMessage(), e);
		}
	}
	
	private void getJPMakeZipFile(String zipFilePath, TMailMessage[] messages) {
		try {			
			org.apache.tools.zip.ZipOutputStream zipOut = 
				new org.apache.tools.zip.ZipOutputStream(new FileOutputStream(zipFilePath));
			zipOut.setEncoding("Shift-JIS");
			
			Date date = new Date();
			String fileName = null;
			for (TMailMessage mailMessage : messages) {
				fileName = mailMessage.getSubject();
				org.apache.tools.zip.ZipEntry entry = new org.apache.tools.zip.ZipEntry(fileName);
				entry.setTime(date.getTime());					
				zipOut.putNextEntry(entry);
				
				InputStream in = new BufferedInputStream(mailMessage.getInputStream());
				int i;
				byte[] buf = new byte[1024 * 1024];
				while ((i = in.read(buf)) != -1) {
					zipOut.write(buf, 0, i);
				}

				zipOut.closeEntry();
				in.close();				
			}
			
			zipOut.close();
			
		} catch (Exception e) {
			e.printStackTrace();			
			LogManager.writeErr(this, e.getMessage(), e);
		}		
	}	

	public int getUserSeq() {
		return userSeq;
	}
	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	public String getNodeNum() {
		return nodeNum;
	}
	public void setNodeNum(String nodeNum) {
		this.nodeNum = nodeNum;
	}
	public String getSroot() {
		return sroot;
	}
	public void setSroot(String sroot) {
		this.sroot = sroot;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setDwuid(String dwuid) {
		this.dwuid = dwuid;
	}
	
}
