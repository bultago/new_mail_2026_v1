package com.terracetech.tims.webmail.webfolder.action;

import java.util.Map;

import org.bouncycastle.util.encoders.Base64;

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

public class CreateFolderAction extends BaseAction {
	private WebfolderManager webfolderManager = null;
	private int userSeq = 0;
	private String nodeNum = null;
	private String sroot = null;
	private String path = null;
	private String type = null;
	private String fName = null;

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
				if(!webfolderManager.vaildateShareFolder(encodeSroot, mailDomainSeq, userSeq, mailUserSeq)) {
					String msg = resource.getMessage("share.alert.001");
					request.setAttribute("path", "/");
					request.setAttribute("type", "user");
					request.setAttribute("nodeNum", "0|");
    				request.setAttribute("msg", msg);

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
			
			fName = StringUtils.isEmpty(fName) ? request.getParameter("fName") : fName;
			
			fName = WebFolderUtils.base64Decode(fName);
			
			store = factory.connect(request.getRemoteAddr(), confMap);
			
			if (isRoot) {
			    folder = store.getWebFolder(fName);
			}
			else {
				char separator = '.';
		        String xpath = path.substring(1);
		        xpath = xpath.replaceAll("/", "" + separator);
		        folder = store.getWebFolder(xpath+separator+fName);
			}
			
			if (folder.exists()) {
				request.setAttribute("msg", resource.getMessage("error.folderexists"));
				request.setAttribute("path", "/");
				request.setAttribute("nodeNum", "0|");
	            store.close();
	            return "result";
			}
			
			folder.create();
			
			writeWebfolderLog(true, "wfolder_create_folder", folderType+folder.getFullName(), "", "");

	        request.setAttribute("path", path);
		    request.setAttribute("userSeq", userSeq);
		    request.setAttribute("type", type);
		    request.setAttribute("nodeNum", nodeNum);
		    request.setAttribute("sroot", sroot);
		    request.setAttribute("msg", resource.getMessage("folder.create.info"));
	
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
	
	public String getFName() {
		return fName;
	}

	public void setFName(String name) {
		fName = name;
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
}
