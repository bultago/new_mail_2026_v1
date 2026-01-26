package com.terracetech.tims.webmail.webfolder.action;

import java.util.Locale;
import java.util.Map;

import org.bouncycastle.util.encoders.Base64;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebFolderUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;

public class RenameFolderAction extends BaseAction {
	private WebfolderManager webfolderManager = null;
	private int userSeq = 0;
	private String nodeNum = null;
	private String sroot = null;
	private String path = null;
	private String type = null;
	private String rfName = null;
	private String folderName = null;

	public void setWebfolderManager(WebfolderManager webfolderManager) {
		this.webfolderManager = webfolderManager;
	}
	
	public String execute() throws Exception {
		
		TMailStore store = null;
		TMailFolder folder = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource("webfolder");
		boolean isError = false;
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
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
					return null;
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
			
			rfName = WebFolderUtils.base64Decode(rfName);
			
			folderName = WebFolderUtils.base64Decode(folderName);
			
			store = factory.connect(request.getRemoteAddr(), confMap);
			
			TMailFolder renameFolder = null;
		    if (isRoot) {
		    	folder = store.getWebFolder(folderName);
		    	renameFolder = store.getWebFolder(rfName);
			}
			else {
				char separator = '.';
		        String xpath = path.substring(1);
		        xpath = xpath.replaceAll("/", "" + separator);
		        folder = store.getWebFolder(xpath + separator + folderName);
		        renameFolder = store.getWebFolder(xpath + separator + rfName);
			}
		    
			if (renameFolder.exists()) {
				request.setAttribute("msg", resource.getMessage("error.folderexists"));
				request.setAttribute("path", "/");
				request.setAttribute("nodeNum", "0|");
	            store.close();
	            return "result";
			}
			
			writeWebfolderLog(true, "wfolder_rename_folder", folderType + folder.getFullName(), renameFolder.getFullName(), "");
			
			folder.renameTo(renameFolder);

	        request.setAttribute("path", path);
		    request.setAttribute("userSeq", userSeq);
		    request.setAttribute("type", type);
		    request.setAttribute("nodeNum", nodeNum);
		    request.setAttribute("sroot", sroot);
		    request.setAttribute("msg", resource.getMessage("folder.rename.info"));
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isError = true;
		} finally {
			if (store != null && store.isConnected()) store.close();
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

	public String getRfName() {
		return rfName;
	}

	public void setRfName(String rfName) {
		this.rfName = rfName;
	}
	
	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
}
