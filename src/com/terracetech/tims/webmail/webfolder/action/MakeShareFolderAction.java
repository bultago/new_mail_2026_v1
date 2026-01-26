package com.terracetech.tims.webmail.webfolder.action;

import java.util.Locale;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderShareVO;

public class MakeShareFolderAction extends BaseAction {
	
	private WebfolderManager webfolderManager = null;
	private int fuid = 0;
	private String type = null;
	private String nodeNum = null;
	private String path = null;
	private String comment = null;
	private String mode = null;
	private String rwAuthSelect = null;
	private String shareMode = null;
	private String folderPath = null;
	private String[] readAuthBox = null;
	private String[] rwAuthBox = null;
	
	public void setWebfolderManager(WebfolderManager webfolderManager) {
		this.webfolderManager = webfolderManager;
	}

	public String execute() throws Exception {

		I18nResources resource = getMessageResource("webfolder");
		boolean isError = false;		
		
		WebfolderShareVO webfolderShareVo = new WebfolderShareVO();
		webfolderShareVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		webfolderShareVo.setFolderPath(folderPath);
		webfolderShareVo.setAuth(rwAuthSelect);
		webfolderShareVo.setComment(comment);
		webfolderShareVo.setCurTime(FormatUtil.getBasicDateStr());		
		
		try {
			if ("all".equals(shareMode)) {
				webfolderShareVo.setShareType(0);
				webfolderManager.setShareAllFolder(webfolderShareVo);
			} 
			else if ("each".equals(shareMode)) {
				webfolderShareVo.setShareType(1);
				webfolderManager.setShareAllFolder(webfolderShareVo);
				fuid = webfolderShareVo.getFuid();
				webfolderManager.setShareTargetFolder(fuid, readAuthBox, rwAuthBox);
			}			
			
			
			path = path.substring(1);  // Æú´õ¾Õ¿¡'/'¾ø¾Ö°í
			int dis = path.lastIndexOf("/"); //ÇöÀç °øÀ¯ÇÏ´Â Æú´õº¸´Ù ÇÑ´Ü°è »óÀ§·Î °¡±â À§ÇØ			
			writeWebfolderLog(true, "wfolder_share_create", "WEBFOLDERROOT."+folderPath.replaceAll("/","."), "", "");
			
			if(dis > 0){
				path = path.substring(0,dis);
			}else{
				path="";
			}
			
			nodeNum = nodeNum.substring(0,nodeNum.lastIndexOf("|"));
			nodeNum = nodeNum.substring(0,nodeNum.lastIndexOf("|")+1);
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isError = true;
		}
	    request.setAttribute("path", path);
	    request.setAttribute("type", type);
	    request.setAttribute("nodeNum", nodeNum);
	    request.setAttribute("msg", resource.getMessage("share.ok"));
	    
	    if (isError) {
	    	return "subError2";
	    }
	    
		return "result";
	}
	
	public String executeUpdate() throws Exception {
		User user = EnvConstants.getTestUser();		
		I18nResources resource = getMessageResource("webfolder");
		boolean isError = false;
		
		WebfolderShareVO webfolderShareVo = new WebfolderShareVO();
		webfolderShareVo.setFuid(fuid);
		webfolderShareVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		webfolderShareVo.setFolderPath(folderPath);
		webfolderShareVo.setAuth(rwAuthSelect);
		webfolderShareVo.setComment(comment);
		webfolderShareVo.setCurTime(FormatUtil.getBasicDateStr());
	
		try {
			if ("all".equals(shareMode)) {
				webfolderShareVo.setShareType(0);
				webfolderManager.modifyShareAllFolder(webfolderShareVo);
				webfolderManager.deleteShareTargetFolder(fuid);
			} 
			else if ("each".equals(shareMode)) {
				webfolderShareVo.setShareType(1);
				webfolderManager.modifyShareAllFolder(webfolderShareVo);
				webfolderManager.modifyShareTargetFolder(fuid, readAuthBox, rwAuthBox);
			}
			
			writeWebfolderLog(true, "wfolder_share_create", "WEBFOLDERROOT."+folderPath.replaceAll("/","."), "", "");
			
			path = path.substring(1);  // Æú´õ¾Õ¿¡'/'¾ø¾Ö°í
			int dis = path.lastIndexOf("/"); //ÇöÀç °øÀ¯ÇÏ´Â Æú´õº¸´Ù ÇÑ´Ü°è »óÀ§·Î °¡±â À§ÇØ
			if(dis > 0){
				path = path.substring(0,dis);
			}else{
				path="";
			}
			
			nodeNum = nodeNum.substring(0,nodeNum.lastIndexOf("|"));
			nodeNum = nodeNum.substring(0,nodeNum.lastIndexOf("|")+1);
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isError = true;
		}
		
	    request.setAttribute("path", path);
	    request.setAttribute("type", type);
	    request.setAttribute("nodeNum", nodeNum);
	    request.setAttribute("msg", resource.getMessage("share.ok"));
	    
	    if (isError) {
	    	return "subError2";
	    }
	    
		return "result";
	}

	
	public int getFuid() {
		return fuid;
	}

	public void setFuid(int fuid) {
		this.fuid = fuid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNodeNum() {
		return nodeNum;
	}

	public void setNodeNum(String nodeNum) {
		this.nodeNum = nodeNum;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getShareMode() {
		return shareMode;
	}

	public void setShareMode(String shareMode) {
		this.shareMode = shareMode;
	}
	
	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getRwAuthSelect() {
		return rwAuthSelect;
	}

	public void setRwAuthSelect(String rwAuthSelect) {
		this.rwAuthSelect = rwAuthSelect;
	}

	public String[] getReadAuthBox() {
		return readAuthBox;
	}

	public void setReadAuthBox(String[] readAuthBox) {
		this.readAuthBox = readAuthBox;
	}

	public String[] getRwAuthBox() {
		return rwAuthBox;
	}

	public void setRwAuthBox(String[] rwAuthBox) {
		this.rwAuthBox = rwAuthBox;
	}
	
}
