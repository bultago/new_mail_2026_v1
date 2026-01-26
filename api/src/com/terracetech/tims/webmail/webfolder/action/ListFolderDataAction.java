package com.terracetech.tims.webmail.webfolder.action;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import jakarta.mail.MessagingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebFolderUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderDataVO;

public class ListFolderDataAction extends BaseAction {
	WebfolderManager webfolderManager = null;
	private int userSeq = 0;
	private String nodeName = null;
	private String sroot = null;
	private String path = null;
	private String type = null;
	
	public void setWebfolderManager(WebfolderManager webfolderManager) {
		this.webfolderManager = webfolderManager;
	}
	
	public String execute() throws Exception {
		
		TMailStore store = null;
		TMailFolder folder = null;
		TMailStoreFactory factory = new TMailStoreFactory();
	
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		try {
			boolean isRoot = false;

			Map<String, String> confMap = null;

			if ("share".equals(type)) {
				confMap = webfolderManager.getWebfolderShareConnectInfo(userSeq);
			}
			else if ("public".equals(type)) {
				confMap = webfolderManager.getWebfolderPublicConnectInfo(user.get(User.MAIL_DOMAIN));
			}
			else {
				confMap = webfolderManager.getWebfolderMyConnectInfo(user);
			}
			
			String nodeId = nodeName.substring(nodeName.lastIndexOf("_")+1,nodeName.length());
			path = StringUtils.IMAPFolderEncode(path);
			
			if (StringUtils.isEmpty(path) || "/".equals(path)) {
				isRoot = true;
			}

			store = factory.connect(request.getRemoteAddr(), confMap);
			
			if (isRoot) {
			    folder = store.getDefaultWebFolder();
			}
			else if (path.indexOf("/") < 0) {
				String xpath = StringUtils.IMAPFolderDecode(path);
	            folder = store.getWebFolder(xpath);			
			} 
			else {
				char separator = '.';
				int pos = path.indexOf("/") + 1;
				String xpath = path.substring(pos > 1 ? 0 : pos); // remove the leading '/'
	            xpath = xpath.replaceAll("/", "" + separator);
	            folder = store.getWebFolder(xpath);            
			}
			
			int key = TMailFolder.SORT_ARRIVAL;
			int dir = TMailFolder.SORT_DESCENDING;
	        folder.open(true);
	        /* sort parameters begin */	
			folder.setSortKey(key);
			folder.setSortDirection(dir);
			
			TMailFolder[] folders = folder.list();
	        Arrays.sort(folders);
	        JSONObject json = new JSONObject();
	        json.put("treeNodeid", nodeName);
	        JSONArray jarray = new JSONArray();
	        TMailFolder tmpFolder = null;
	        
	        for (int i = 0; i < folders.length; i++) {
	        	JSONObject nodeJson = new JSONObject();
	        	String folderName = folders[i].getName();
	        	String tmpPath = folders[i].getEncName();	        	
	        	String fpath = folders[i].getFullName();
	        	nodeJson.put("type", type);
	        	nodeJson.put("folderName", folderName);
	        	if("share".equals(type)){
	        		nodeJson.put("nodePath", userSeq +"|"+fpath);
	        	} else {
	        		nodeJson.put("nodePath", fpath);
	        	}

	        	String checkPath = tmpPath.replaceAll(EnvConstants.getBasicSetting("webfolder.root")+"\\.", "");
	        	
        		int folderUid = webfolderManager.readMyShareFolderUid(mailUserSeq, checkPath);
        		if (folderUid > 0) {
        			nodeJson.put("share", "true");
        		}
        		else {
        			nodeJson.put("share", "false");
        		}
	        	
	        	
	        	tmpPath = StringUtils.IMAPFolderDecode(tmpPath);
	        	tmpFolder = store.getWebFolder(tmpPath);
	        	
	        	try {
	        		tmpFolder.open(false);		        	
		        	if(tmpFolder.getSubfoldersCount() > 0){
		        		nodeJson.put("child", "true");		        		
		        	} else {
		        		nodeJson.put("child", "false");
		        	}
	        	}catch (MessagingException me) {
	        		nodeJson.put("child", "false");
				}
	        	nodeJson.put("nodeNum", nodeId+i+"|");
	        	
	        	jarray.add(nodeJson);	        	
	        	tmpFolder.close(false);
	        	tmpFolder = null;
			}        
	        
	        json.put("nodes", jarray);
	        
	        folder.close(true);
	        
	        response.setHeader("Content-Type", "text/html; charset=UTF-8");
	        response.addHeader("Cache-Control","no-store");
			response.addHeader("Pragma", "no-cache");
			PrintWriter out = response.getWriter();
			out.println(json);
			
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}finally {
			if (store != null && store.isConnected()) store.close();
		}
		return null;
	}

	public int getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
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
