package com.terracetech.tims.webmail.webfolder.action;

import java.util.Locale;
import java.util.Map;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.MakeMessage;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;

public class WriteAttachFileAction extends BaseAction {

	private WebfolderManager webfolderManager = null;
	
	private int userSeq = 0;
	private String nodeNum = null;
	private String sroot = null;
	private String path = null;
	private String type = null;
	private String[] uid = null;
	String[] attachList = null;
	
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
		
		try {
			
			boolean isRoot = false;
			Map<String, String> confMap = null;
			String folderType = "";
			
			if (StringUtils.isEmpty(path) || "/".equals(path)) {
				isRoot = true;
			}
			
			if ("share".equals(type)) {
				String encodeSroot = sroot.replaceAll(EnvConstants.getBasicSetting("webfolder.root")+"\\.", "");
				encodeSroot = StringUtils.IMAPFolderEncode(encodeSroot);
				if(!webfolderManager.vaildateShareFolder(encodeSroot, mailDomainSeq, userSeq, mailUserSeq)){
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
			else {
				confMap = webfolderManager.getWebfolderMyConnectInfo(user);
			}
			store = factory.connect(request.getRemoteAddr(), confMap);
		
			if (isRoot) {
	        	folder = store.getDefaultWebFolder();
	        }
	        else { 
	            char separator = '.';
	            String xpath = path.substring(1); // remove the leading '/'
	            xpath = xpath.replaceAll("/", "" + separator);
	            folder = store.getWebFolder(xpath);
	        }

			if (uid != null) {
	            long luids[] = new long[uid.length];

	            for (int i = 0; i < luids.length; i++) {
	                luids[i] = Long.parseLong(uid[i]);
	            }

	            folder.open(true);
	            TMailMessage[] messages = folder.getMessagesByUID(luids);
	            
	            String[][] attaches = webfolderManager.getWebFolderAttachFiles(messages,user.get(User.EMAIL));
	            
	            if (attaches != null) {
	            	attachList = new String[attaches.length];
	                for (int i = 0; i < attaches.length; i++) {
	                	attachList[i] = "attachAddList[" + i + "] = new Array(\"" +
	                        attaches[i][0] + "\", \"" + attaches[i][1]+"\", " +
	                        attaches[i][2] + ", " + luids[i] + ");\n";
	                }
	            }
	            
	            for (int i = 0; i < messages.length; i++) {
	            	writeWebfolderLog(true,"wfolder_mail_send", folderType+folder.getFullName(),"", "", "", Long.parseLong(messages[i].getWebFolderFileSize()),messages[i].getSubject(),"");
				}
	            folder.close(true);
	            store.close();
	            
	            return "attachList";
			}
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			String msg = resource.getMessage("alert.attachFile");
			MakeMessage.printAlertCloseOnly(msg);
		}finally {
			if(store != null && store.isConnected()) store.close();
		}	
		return null;
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

	public String[] getUid() {
		return uid;
	}

	public void setUid(String[] uid) {
		this.uid = uid;
	}

	public String[] getAttachList() {
		return attachList;
	}

	public void setAttachList(String[] attachList) {
		this.attachList = attachList;
	}
}
