package com.terracetech.tims.webmail.webfolder.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderDataVO;

public class FolderTreeAction extends BaseAction {
	
	private WebfolderManager webfolderManager = null;
	private String viewType = null;
	private List<WebfolderDataVO> userFolderList = null;
	private List<WebfolderDataVO> shareFolderList = null;
	private List<WebfolderDataVO> publicFolderList = null;

	public String execute() throws Exception {
	
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		
		I18nResources resource = getMessageResource("webfolder");
		
		if (StringUtils.isEmpty(viewType)) viewType = "view";
		
		Map<String, String> confMap = null;
		
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));

		shareFolderList = new ArrayList<WebfolderDataVO>();
		List<WebfolderDataVO> myFolderList = null;
		boolean isError = false;
		try {
			myFolderList = webfolderManager.getMyShareFolder(domainSeq, userSeq);
			
			if (myFolderList != null && myFolderList.size() > 0) {
				Map<String, Integer> folderUids = new HashMap<String, Integer>();
				int folderUid = 0;
				String folderUidStr = null;
				for (int i=0; i <myFolderList.size(); i++) {
					TMailFolder mailFolder = null;
					try {
						folderUid = myFolderList.get(i).getFuid();
						folderUidStr = Integer.toString(folderUid);
	
						if (folderUids.containsKey(folderUidStr)) {
							continue;
						}
						
						folderUids.put(folderUidStr, folderUid);
						
						int mailUserSeq = myFolderList.get(i).getMailUserSeq();
						
						confMap = webfolderManager.getWebfolderShareConnectInfo(mailUserSeq);
						store = factory.connect(request.getRemoteAddr(), confMap);
						
						String path = myFolderList.get(i).getRealPath();
	
						String xpath = StringUtils.IMAPFolderDecode(path);
						mailFolder = store.getWebFolder(xpath);
						
						if (!mailFolder.exists()) {
							webfolderManager.deleteMyShareFolder(mailUserSeq, xpath);
							continue;
						}
						
						String folderName = mailFolder.getName();
	    	            String nodePath = mailFolder.getEncName();	    	            
	    	            String fpath = mailFolder.getFullName();
	    	            
	    	            mailFolder.open(false);    		        	
			        	if(mailFolder.getSubfoldersCount() > 0){
			        		myFolderList.get(i).setChild(true);    		        		
			        	} else {
			        		myFolderList.get(i).setChild(false);
			        	}
			        	myFolderList.get(i).setFolderName("["+myFolderList.get(i).getMailUid()+"]"+folderName);
			        	myFolderList.get(i).setRealPath(myFolderList.get(i).getMailUserSeq()+"|"+fpath);
			        	myFolderList.get(i).setPath(myFolderList.get(i).getMailUserSeq()+"|"+nodePath);
			        	myFolderList.get(i).setNodeNumber(Integer.toString(i)+"|");
			        	myFolderList.get(i).setShareRoot(fpath);
			        	shareFolderList.add(myFolderList.get(i));
					} catch (Exception e) {
						LogManager.writeErr(this, e.getMessage(), e);
						isError = true;
					}finally{
						if (mailFolder != null){
							mailFolder.close(false);
							mailFolder = null;
						}
						if (store != null && store.isConnected()) store.close();    					
					}
				}
				
				if (shareFolderList != null &&shareFolderList.size() > 0) {
					for (int i=0; i <shareFolderList.size(); i++) {
						shareFolderList.get(i).setNodeNumber(Integer.toString(i)+"|");
					}
				}
			}
		
			try {
				confMap = webfolderManager.getWebfolderMyConnectInfo(user);
				store = factory.connect(request.getRemoteAddr(), confMap);
				
				userFolderList = new ArrayList<WebfolderDataVO>();
				WebfolderDataVO webfolderDataVo = new WebfolderDataVO();
				webfolderDataVo.setPath("/");
				webfolderDataVo.setFolderName(resource.getMessage("webfolder.title"));
				webfolderDataVo.setRealPath(EnvConstants.getBasicSetting("webfolder.root")+"./");
				
				if (store.getDefaultFolder().getSubfoldersCount() > 0){
					webfolderDataVo.setChild(true);        		
	        	} else {
	        		webfolderDataVo.setChild(false);
	        	}
	        	
				webfolderDataVo.setPrivil("W");
				webfolderDataVo.setShareRoot("/");
				webfolderDataVo.setNodeNumber("0|");
				
				userFolderList.add(webfolderDataVo);
	           
			}catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
				isError = true;
			} finally {
				store.close();
			}
		
			try {
				confMap = webfolderManager.getWebfolderPublicConnectInfo(user.get(User.MAIL_DOMAIN));
				store = factory.connect(request.getRemoteAddr(), confMap);
				
				publicFolderList = new ArrayList<WebfolderDataVO>();
				WebfolderDataVO webfolderDataVo = new WebfolderDataVO();
				webfolderDataVo.setPath("/");
				webfolderDataVo.setFolderName(resource.getMessage("webfolder.public.title"));
				webfolderDataVo.setRealPath(EnvConstants.getBasicSetting("webfolder.root")+"./");
				
				if (store.getDefaultFolder().getSubfoldersCount() > 0){
					webfolderDataVo.setChild(true);        		
	        	} else {
	        		webfolderDataVo.setChild(false);
	        	}
	        	
				webfolderDataVo.setPrivil("W");
				webfolderDataVo.setShareRoot("/");
				webfolderDataVo.setNodeNumber("0|");
				
				publicFolderList.add(webfolderDataVo);
			}catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
				isError = true;
			} finally {
				if (store != null && store.isConnected()) store.close();
			}
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isError = true;
		}
		
		if (isError) {
			return "subError2";
		}
			
		return viewType;
	}
	
	public void setWebfolderManager(WebfolderManager webfolderManager) {
		this.webfolderManager = webfolderManager;
	}
	
	public String getViewType() {
		return viewType;
	}
	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public List<WebfolderDataVO> getUserFolderList() {
		return userFolderList;
	}

	public List<WebfolderDataVO> getShareFolderList() {
		return shareFolderList;
	}

	public List<WebfolderDataVO> getPublicFolderList() {
		return publicFolderList;
	}

}
