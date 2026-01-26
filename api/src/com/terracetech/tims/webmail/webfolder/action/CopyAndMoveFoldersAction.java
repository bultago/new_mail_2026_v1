package com.terracetech.tims.webmail.webfolder.action;

import java.util.Map;

import jakarta.mail.Flags;
import jakarta.mail.MessagingException;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.webfolder.ibean.FileLogData;
import com.terracetech.tims.webmail.webfolder.manager.WebFolderUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;

public class CopyAndMoveFoldersAction extends BaseAction {
	private WebfolderManager webfolderManager = null;
	
	private int userSeq = 0;
	private String nodeNum = null;
	private String sroot = null;
	private String path = null;
	private String type = null;
	private int targetUserSeq = 0;
	private String targetPath = null;
	private String targetType = null;
	private String cmType = null;
	private String fid = null;
	private String uid = null;

	public void setWebfolderManager(WebfolderManager webfolderManager) {
		this.webfolderManager = webfolderManager;
	}
	
	public String execute() throws Exception {
		TMailStore store = null;
		TMailStore targetStore = null;
		TMailFolder folder = null;
		TMailFolder targetFolder = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource("webfolder");
		
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		String fids[] = null;
		if (StringUtils.isNotEmpty(fid)) {
			fids = fid.split(",");
		}
		
		String uids[] = null;
		if (StringUtils.isNotEmpty(uid)) {
			uids = uid.split(",");
		}
		boolean isError = false;
		try {
			
			String msg = null;
			int depthCnt = 1;
			boolean isRoot = false;
			boolean isTargetRoot = false;
			Map<String, String> confMap = null;
			Map<String, String> targetConfMap = null;
			String sfolderType = "";
			String tfolderType = "";
						
			if (StringUtils.isEmpty(path)) {
				path = "/";
			} else {
				path = WebFolderUtils.base64Decode(path);
			}
			
			if ("/".equals(path)) {
				isRoot = true;
			}
			
			if (StringUtils.isEmpty(targetPath)) {
				targetPath = "/";
			} else {
				targetPath = WebFolderUtils.base64Decode(targetPath);
			}
			
			if ("/".equals(targetPath)) {
				isTargetRoot = true;
			}
			
			if ("share".equals(type)) {
				msg = resource.getMessage("share.alert.001");
				
				if (StringUtils.isNotEmpty(sroot)) {
					sroot = WebFolderUtils.base64Decode(sroot);
				}
				
				String encodeSroot = sroot.replaceAll(EnvConstants.getBasicSetting("webfolder.root")+"\\.", "");
				encodeSroot = StringUtils.IMAPFolderEncode(encodeSroot);
				if(!webfolderManager.vaildateShareFolder(encodeSroot, mailDomainSeq, userSeq, mailUserSeq)){
				    request.setAttribute("path", "/");
				    request.setAttribute("type", "user");
				    request.setAttribute("nodeNum", "0|");
				    request.setAttribute("msg", msg);
					return "result";
				}
				confMap = webfolderManager.getWebfolderShareConnectInfo(userSeq);
				sfolderType = "S";
			}
			else if ("public".equals(type)) {
				confMap = webfolderManager.getWebfolderPublicConnectInfo(user.get(User.MAIL_DOMAIN));
				sfolderType = "P";
			}
			else {
				confMap = webfolderManager.getWebfolderMyConnectInfo(user);
			}

			store = factory.connect(request.getRemoteAddr(), confMap);

			if ("share".equals(targetType)) {
				msg = resource.getMessage("share.alert.001");
				
				if (StringUtils.isNotEmpty(sroot)) {
					sroot = WebFolderUtils.base64Decode(sroot);
				}
				
				String encodeSroot = sroot.replaceAll(EnvConstants.getBasicSetting("webfolder.root")+"\\.", "");
				encodeSroot = StringUtils.IMAPFolderEncode(encodeSroot);
				if(!webfolderManager.vaildateShareFolder(encodeSroot, mailDomainSeq, targetUserSeq, mailUserSeq)){
					 request.setAttribute("path", "/");
					 request.setAttribute("type", "user");
					 request.setAttribute("nodeNum", "0|");
					 request.setAttribute("msg", msg);
					 return "result";
				}
				targetConfMap = webfolderManager.getWebfolderShareConnectInfo(targetUserSeq);
				tfolderType = "S";
			}
			else if ("public".equals(targetType)) {
				if (!webfolderManager.getPublicFolderAuth(Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)), Integer.parseInt(user.get(User.MAIL_USER_SEQ)))) {
					msg = resource.getMessage("public.writeauth.fail");
					request.setAttribute("path", "/");
					request.setAttribute("type", "user");
					request.setAttribute("nodeNum", "0|");
					request.setAttribute("msg", msg);
					return "result";
				}
				targetConfMap = webfolderManager.getWebfolderPublicConnectInfo(user.get(User.MAIL_DOMAIN));
				tfolderType = "P";
			}
			else {
				targetConfMap = webfolderManager.getWebfolderMyConnectInfo(user);
			}
			
			targetStore = factory.connect(request.getRemoteAddr(), targetConfMap);
			
			if (isRoot) {
	        	folder = store.getDefaultWebFolder();
	        }
	        else { 
	            char separator = '.';
	            String xpath = path.substring(1); // remove the leading '/'
	            xpath = xpath.replaceAll("/", "" + separator);
	            folder = store.getWebFolder(xpath);
	        }
			
			if (isTargetRoot) {
	        	targetFolder = targetStore.getDefaultWebFolder();
	        }
	        else { 
	        	char separator = '.';
	            String xpath = targetPath.replaceAll("/", "" + separator);
	            depthCnt = webfolderManager.getDepthCount(xpath);
	            xpath = StringUtils.IMAPFolderEncode(xpath);
	            targetFolder = targetStore.getWebFolder(xpath);
	        }
			
			if (!targetFolder.exists()) {
				msg = resource.getMessage("error.wrongfolder");			
				request.setAttribute("path", "/");
				request.setAttribute("type", "user");
				request.setAttribute("nodeNum", "0|");
				request.setAttribute("msg", msg);

	            store.close();
	            targetStore.close();
	            
	            return "result";
			}
			
			boolean isUserFolder = false;
			String sId = confMap.get(User.MAIL_UID);
			String tId = targetConfMap.get(User.MAIL_UID);
			if (sId.equals(tId)) isUserFolder = true;
			String sFolderName = null;
        	String tFolderName = null;
        	String actionName = null;			
			
			 // copy folders
	        if (fids != null) {
	        	String fidString = null;	        	
	            for (int i = 0; i < fids.length; i++) {
	            	fidString = WebFolderUtils.base64Decode(fids[i]);
	            	if (fidString.length() > 0){
	            		
		                TMailFolder src = folder.getFolder(fidString);
		                TMailFolder dst = targetFolder.getFolder(fidString);
		                sFolderName = src.getFullName();
		                tFolderName = dst.getFullName();
		                
		                if (dst.exists()) {
		                	msg = resource.getMessage("alert.moveerr");
	                        request.setAttribute("path", "/");
	                        request.setAttribute("type", "user");
	                        request.setAttribute("nodeNum", "0|");
	        				request.setAttribute("msg", msg);
	                    	
	                        store.close();
	                        targetStore.close();
	                        
	                        return "result";
		                } else {
		                	dst.create();
		                }
		
		                try {
		                	if (cmType.equals("copy")){
		                		actionName = "wfolder_copy_folder";
		                		webfolderManager.copyRFolders(src, dst, depthCnt);
		                	} else if (cmType.equals("move") && isUserFolder){
		                		actionName = "wfolder_move_folder";
		                		webfolderManager.moveRFolders(src, dst, depthCnt);
		                        src.delete();	                		
		                	} else {
		                		actionName = "wfolder_move_folder";
		                		webfolderManager.copyRFolders(src, dst, depthCnt);
		                		src.delete();
		                	}
		                	
		                	writeWebfolderLog(true, actionName, sfolderType+sFolderName, tfolderType+tFolderName, "");
		                }
		                catch (MessagingException e) { // quota check
		                    String em = e.getMessage();
		                    LogManager.writeErr(this, e.getMessage(), e);
		                    
		                    if (em.indexOf("exceeded") != -1 && em.indexOf("quota") != -1) {
		                    	
		                        msg = resource.getMessage("error.fullquota");
		                        request.setAttribute("path", "/");
		                        request.setAttribute("type", "user");
		                        request.setAttribute("nodeNum", "0|");
		        				request.setAttribute("msg", msg);
		                    	
		                        store.close();
		                        targetStore.close();
		                        
		                        return "result";
		                    }
		                    else if (em.indexOf("deth") != -1 && em.indexOf("over") != -1) {
		                    	
		                    	msg = resource.getMessage("error.deth");
		                    	request.setAttribute("path", "/");
		                    	request.setAttribute("type", "user");
		                    	request.setAttribute("nodeNum", "0|");
		        				request.setAttribute("msg", msg);
		                    	
		            		    store.close();
		                        targetStore.close();
		                        
		                        return "result";
		                        
		                    }else{
		                    	store.close();
		                        targetStore.close();
		                        throw e;
		                    }
		                }
		            }
	            }
	        } 
	       
	        // copy files
	        if (uids != null) {
	            long luids[] = new long[uids.length];

	            for (int i = 0; i < luids.length; i++) {
	                luids[i] = Long.parseLong(uids[i]);
	            }

	            folder.open(true);	            
	            TMailMessage[] messages = folder.getMessagesByUID(luids);	            
	            FileLogData[] logData = new FileLogData[messages.length];
	            sFolderName = sfolderType+folder.getFullName();
	            tFolderName = tfolderType+targetFolder.getFullName();	            
	            for (int i = 0; i < logData.length; i++) {
	            	logData[i] = new FileLogData();
	            	logData[i].setFileName(messages[i].getSubject());
	            	logData[i].setSize(messages[i].getWebFolderFileSize());
	            	logData[i].setSourceFolder(sFolderName);
	            	logData[i].setTargetFolder(tFolderName);
				}
	            
	            try {
	            	if (cmType.equals("copy")){
	            		actionName = "wfolder_copy_file";
	            		targetFolder.appendMessagesBinary(messages);
	            	} else if (cmType.equals("move") && isUserFolder){
	            		actionName = "wfolder_move_file";
	            		folder.xmove(luids, targetFolder.getFullName());            		           		
	            	} else {
	            		actionName = "wfolder_move_file";
	            		targetFolder.appendMessagesBinary(messages);
	            		folder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);            		
	            	}
	            	
	            	for (int i = 0; i < logData.length; i++) {
	            		writeWebfolderLog(true,actionName, logData[i].getSourceFolder(), logData[i].getTargetFolder(), "","", Long.parseLong(logData[i].getSize()),logData[i].getFileName(),"");
					}
	            	
	            }
	            catch (MessagingException e) { // qouta check
	                String em = e.getMessage();

	                LogManager.writeErr(this, e.getMessage(), e);

	                if (em.indexOf("exceeded") != -1 && em.indexOf("quota") != -1) {
	          
	                	msg = resource.getMessage("error.fullquota");
	                	request.setAttribute("path", "/");
	                	request.setAttribute("type", "user");
	                	request.setAttribute("nodeNum", "0|");
        				request.setAttribute("msg", msg);
	                	
	                    store.close();
	                    targetStore.close();
	                    
	                    return "result";
	                }
	                else  if (em.indexOf("deth") != -1 && em.indexOf("over") != -1) {
                    	
	                	msg = resource.getMessage("error.deth");
	                	request.setAttribute("path", "/");
	                	request.setAttribute("type", "user");
	                	request.setAttribute("nodeNum", "0|");
        				request.setAttribute("msg", msg);
                    	
            		    store.close();
                        targetStore.close();
                    	
                        return "result";
                    }
	                else {
	                	store.close();
	                    targetStore.close();
	                    throw e;
	                }
	            }
	            folder.close(true);
	            targetFolder.close(true);
	        }
	        store.close();
	        targetStore.close();
	        
	        if (cmType.equals("copy")){
	    		msg = resource.getMessage("share.alert.006");
	    	} else if (cmType.equals("move")){
	    		msg = resource.getMessage("share.alert.007");
	    	}

		    request.setAttribute("path", path);
		    request.setAttribute("userSeq", userSeq);
		    request.setAttribute("type", type);
		    request.setAttribute("nodeNum", nodeNum);
		    request.setAttribute("sroot", sroot);
		    request.setAttribute("msg", msg);
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isError = true;
		}finally {
			if(store != null && store.isConnected()) store.close();
			if(targetStore != null && targetStore.isConnected()) targetStore.close();
		}
		
		if (isError) {
			return "subError2";
		}
		
		return "result";
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

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public int getTargetUserSeq() {
		return targetUserSeq;
	}

	public void setTargetUserSeq(int targetUserSeq) {
		this.targetUserSeq = targetUserSeq;
	}

	public String getCmType() {
		return cmType;
	}

	public void setCmType(String cmType) {
		this.cmType = cmType;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
}
