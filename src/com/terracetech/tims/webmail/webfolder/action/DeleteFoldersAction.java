package com.terracetech.tims.webmail.webfolder.action;

import java.util.Map;

import javax.mail.Flags;

import org.bouncycastle.util.encoders.Base64;

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
import com.terracetech.tims.webmail.webfolder.manager.WebFolderUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;

public class DeleteFoldersAction extends BaseAction {
	private WebfolderManager webfolderManager = null;
	private int userSeq = 0;
	private String nodeNum = null;
	private String sroot = null;
	private String path = null;
	private String type = null;
	private String fid = null;
	private String uid = null;

	public void setWebfolderManager(WebfolderManager webfolderManager) {
		this.webfolderManager = webfolderManager;
	}
	
	public String execute() throws Exception {
		
		TMailStore store = null;
		TMailFolder folder = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource("webfolder");
		
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		boolean isError = false;
		
		String fids[] = null;
		if (StringUtils.isNotEmpty(fid)) {
			fids = fid.split(",");
		}
		
		String uids[] = null;
		if (StringUtils.isNotEmpty(uid)) {
			uids = uid.split(",");
		}
		
		try {
			boolean isRoot = false;
			Map<String, String> confMap = null;
			String folderType = "";
			
			if ("share".equals(type)) {
				
				if (StringUtils.isNotEmpty(sroot)) {
					sroot = WebFolderUtils.base64Decode(sroot);
				}
				
				String encodeSroot = sroot.replaceAll(EnvConstants.getBasicSetting("webfolder.root")+"\\.", "");
				encodeSroot = StringUtils.IMAPFolderEncode(encodeSroot);
				if(!webfolderManager.vaildateShareFolder(encodeSroot, mailDomainSeq, userSeq, mailUserSeq)){
					request.setAttribute("path", "/");
					request.setAttribute("type", "user");
					request.setAttribute("nodeNum", "0|");
					request.setAttribute("msg", resource.getMessage("share.alert.001"));
					return "result";
				}
				confMap = webfolderManager.getWebfolderShareConnectInfo(userSeq);
				folderType = "S";
			}
			else if ("public".equals(type)) {
				confMap = webfolderManager.getWebfolderPublicConnectInfo(user.get(User.MAIL_DOMAIN));
				folderType = "P";
			}
			else {
				confMap = webfolderManager.getWebfolderMyConnectInfo(user);
			}
			
			if (StringUtils.isEmpty(path)) {
				path = "/";
			} else {
				path = WebFolderUtils.base64Decode(path);
			}
			
			if ("/".equals(path)) {
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
			
			if (fids != null) {
	            /* 0:sync with cache  2:delete completely 3:reload forcefully */ 
				String fidString = null;
				TMailFolder tmpfolder = null;;
	            for (int i = 0; i < fids.length; i++) {
	            	fidString = WebFolderUtils.base64Decode(fids[i]);
	            	tmpfolder = folder.getFolder(fidString);
	            	writeWebfolderLog(true,"wfolder_delete_folder", folderType+tmpfolder.getFullName(),"","");
	                tmpfolder.delete();
	            }
	        }
			
			if (uids != null) {
	            long luids[] = new long[uids.length];

	            for (int i = 0; i < luids.length; i++) {
	                luids[i] = Long.parseLong(uids[i]);
	            }

	            folder.open(true);
	            TMailMessage[] messages = folder.getMessagesByUID(luids);	            
	            for (int i = 0; i < messages.length; i++) {
	            	writeWebfolderLog(true,"wfolder_delete_file", folderType+folder.getFullName(),"", "", "", Long.parseLong(messages[i].getWebFolderFileSize()),messages[i].getSubject(),"");
				}
	            folder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
	            folder.close(true);
	        }
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isError = true;
		}finally {
			if (store != null && store.isConnected()) store.close();
		}
		
		request.setAttribute("path", path);
	    request.setAttribute("userSeq", userSeq);
	    request.setAttribute("type", type);
	    request.setAttribute("nodeNum", nodeNum);
	    request.setAttribute("sroot", sroot);
	    request.setAttribute("msg", resource.getMessage("folder.delete.info"));
	    
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
