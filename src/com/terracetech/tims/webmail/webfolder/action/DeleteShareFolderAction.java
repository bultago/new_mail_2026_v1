package com.terracetech.tims.webmail.webfolder.action;

import java.util.Locale;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;

public class DeleteShareFolderAction extends BaseAction {
	private WebfolderManager webfolderManager = null;
	private String type = null;
	private String path = null;
	private String nodeNum = null;
	
	public void setWebfolderManager(WebfolderManager webfolderManager) {
		this.webfolderManager = webfolderManager;
	}
	
	public String execute() throws Exception {
		
		I18nResources resource = getMessageResource("webfolder");
		String msg = resource.getMessage("deleteShare.ok");
		boolean isError = false;		
		try {
			path = path.substring(1);
			String dbpath = StringUtils.IMAPFolderEncode(path);
			dbpath = dbpath.replaceAll("/",".");
			
			writeWebfolderLog(true, "wfolder_share_delete", "WEBFOLDERROOT."+dbpath, "", "");			
			webfolderManager.deleteMyShareFolder(Integer.parseInt(user.get(User.MAIL_USER_SEQ)), dbpath);
			
			int dis = path.lastIndexOf("/");
			if(dis>0){
				path = path.substring(0,dis);
			}else{
				path="";
			}		
			nodeNum = nodeNum.substring(0,nodeNum.lastIndexOf("|"));
			nodeNum = nodeNum.substring(0,nodeNum.lastIndexOf("|")+1);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isError = true;
		}
		request.setAttribute("path", path);
	    request.setAttribute("type", type);
	    request.setAttribute("nodeNum", nodeNum);
	    request.setAttribute("msg", msg);
	    
	    if (isError) {
	    	return "subError2";
	    }
	    
		return "result";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNodeNum() {
		return nodeNum;
	}

	public void setNodeNum(String nodeNum) {
		this.nodeNum = nodeNum;
	}

}
