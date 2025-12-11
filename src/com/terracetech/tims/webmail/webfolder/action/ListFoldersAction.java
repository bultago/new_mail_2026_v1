package com.terracetech.tims.webmail.webfolder.action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jakarta.mail.FolderNotFoundException;

import org.bouncycastle.util.encoders.Base64;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.webfolder.ibean.WebfolderSearchInfoBean;
import com.terracetech.tims.webmail.webfolder.manager.WebFolderUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderDataVO;

public class ListFoldersAction extends BaseAction {

	private WebfolderManager webfolderManager = null;
	private List<TMailFolder> folderList = null;
	private int userSeq = 0;
	private int currentPage = 1;
	private String nodeNum = null;
	private String sroot = null;
	private String path = null;
	private String type = null;
	private String sortby = null;
	private String sortDir = null;
	private String searchType = null;
	private String keyWord = null;

	public String execute() throws Exception {

		TMailStore store = null;
		TMailFolder folder = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		I18nResources resource = getMessageResource("webfolder");
		
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		boolean isError = false;
		try {		
			Map<String, String> confMap = null;
			WebfolderDataVO webfolderDataVo = null;
			boolean isRoot = false;
			boolean folderShare = false;
			int pageBase = 10;
			int fid = 0;
			String currentPath = null;
			
			if (StringUtils.isEmpty(nodeNum)) nodeNum = "0|";			
			if (StringUtils.isEmpty(type)) type = "user";

			if (type.equals("share")) {
				
				if (StringUtils.isNotEmpty(sroot)) {
					sroot = WebFolderUtils.base64Decode(sroot);
				}
				
				String encodeSroot = sroot.replaceAll(EnvConstants.getBasicSetting("webfolder.root")+"\\.", "");
				encodeSroot = StringUtils.IMAPFolderEncode(encodeSroot);
				webfolderDataVo = webfolderManager.getShareInfo(encodeSroot, mailDomainSeq, userSeq, mailUserSeq);
				
				if (webfolderDataVo != null) {
					request.setAttribute("folderAuth", webfolderDataVo.getPrivil());
				}
				else {
					request.setAttribute("path", "/");
					request.setAttribute("type", "user");
					request.setAttribute("nodeNum", "0|");
					request.setAttribute("msg", resource.getMessage("share.alert.001"));
					return "result";		
				}
				confMap = webfolderManager.getWebfolderShareConnectInfo(userSeq);
				request.setAttribute("shareSeq", userSeq);
				request.setAttribute("shareId", webfolderDataVo.getMailUid());
			}
			else if (type.equals("public")) {
				String auth = "R";
				if (webfolderManager.getPublicFolderAuth(mailDomainSeq, Integer.parseInt(user.get(User.MAIL_USER_SEQ)))) {
					auth = "W";
				}
				confMap = webfolderManager.getWebfolderPublicConnectInfo(user.get(User.MAIL_DOMAIN));
				request.setAttribute("folderShare", "");
				request.setAttribute("folderAuth", auth);
				request.setAttribute("shareSeq", "");
			}
			else {
				confMap = webfolderManager.getWebfolderMyConnectInfo(user);
				request.setAttribute("folderShare", "");
				request.setAttribute("folderAuth", "W");
				request.setAttribute("shareSeq", "");
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
			    currentPath = EnvConstants.getBasicSetting("webfolder.root")+"./";
			}
			else if (path.indexOf("/") < 0) {			
			    String xpath = StringUtils.IMAPFolderDecode(path);
			    folder = store.getWebFolder(xpath);
			    currentPath = folder.getFullName();
			} else {
			    // separator = store.getDefaultFolder().getSeparator();
			    char separator = '.';
			    int pos = path.indexOf("/") + 1;
			    String xpath = path.substring(pos > 1 ? 0 : pos); // remove the leading '/'
			    
			    xpath = xpath.replaceAll("/", "" + separator);
			    
			    folder = store.getWebFolder(xpath);			    
			    currentPath = folder.getFullName();
			}
			
			if (StringUtils.isEmpty(sortby)) sortby = "date";
			if (StringUtils.isEmpty(sortDir)) sortDir = "desc";
			
			/* sort parameters begin */
			int key = TMailFolder.SORT_ARRIVAL;
			int dir = TMailFolder.SORT_DESCENDING;
			
			if (sortby.equals("size")) key = TMailFolder.SORT_SIZE;
			else if (sortby.equals("name")) key = TMailFolder.SORT_SUBJECT;
			else if (sortby.equals("kind")) key = TMailFolder.SORT_CC;
			
			if (sortDir.equals("asce")) {
			    dir = TMailFolder.SORT_ASCENDING;
			} 
			
			folder.open(true);
			folder.setSortKey(key);
			folder.setSortDirection(dir);
			
			String checkPath = folder.getEncName();
			checkPath = checkPath.replaceAll(EnvConstants.getBasicSetting("webfolder.root")+"\\.", "");

			if(type.equals("share")){				
				if(checkPath.equals(webfolderDataVo.getRealPath())){
					isRoot = true;
				}				
			} else if (type.equals("user")){
				int folderUid = webfolderManager.readMyShareFolderUid(mailUserSeq, checkPath);
				if (folderUid > 0){
					folderShare = true;
					fid = folderUid;
				}								
			}
			
			TMailFolder[] folders = folder.list();
			Arrays.sort(folders);
			
			WebfolderDataVO[] webfolderDataVos = null;
			if(folders != null){
				int folderUid = 0;
				webfolderDataVos = new WebfolderDataVO[folders.length];
				for (int i = 0; i < folders.length; i++) {	
					folderUid = 0;
					webfolderDataVos[i] = new WebfolderDataVO();
					webfolderDataVos[i].setFolderName(folders[i].getName());
					webfolderDataVos[i].setRealPath(folders[i].getFullName());
					webfolderDataVos[i].setNodeNumber(nodeNum+i+"|");
					if(folders[i].getSubfoldersCount() > 0){
						webfolderDataVos[i].setChild(true);    		        		
		        	} else {
		        		webfolderDataVos[i].setChild(false);
		        	}
					
					webfolderDataVos[i].setShare("false");

					checkPath = folders[i].getEncName().replaceAll(EnvConstants.getBasicSetting("webfolder.root")+"\\.","");
					
					folderUid = webfolderManager.readMyShareFolderUid(mailUserSeq, checkPath);
			        	
		        	if (folderUid > 0){
		        		webfolderDataVos[i].setFuid(folderUid);
		        		webfolderDataVos[i].setShare("true");
		        	}
		        	webfolderDataVo = null;	        	
				}
			}
			
			WebfolderSearchInfoBean searchBean = new WebfolderSearchInfoBean();
			if (StringUtils.isEmpty(searchType)) {
				searchType = "name";
			}
			searchBean.setSearchType(searchType);
			
			if (StringUtils.isNotEmpty(keyWord)) {
				keyWord = WebFolderUtils.base64Decode(keyWord);
			}
			
			searchBean.setPattern(keyWord);

			int start = (currentPage - 1) * pageBase;
			TMailMessage[] messages = folder.sort(start, pageBase, searchBean.getSearchTerm());
			
			int nmessges = folder.getSortedMessageCount();
			int npages = (int) Math.ceil((double) nmessges / pageBase);
			
			PageManager pm = new PageManager();
			pm.initParameter(nmessges, pageBase, 10);
	
			if (0 < npages && npages < currentPage) {
				currentPage = npages;
			    start = (currentPage - 1) * pageBase;
			    messages = folder.sort(start, pageBase, searchBean.getSearchTerm());
			}
			pm.setPage(currentPage);
			
			// quota
			com.sun.mail.imap.Quota[] quotas =
			    store.getQuota(EnvConstants.getBasicSetting("webfolder.root"));
			long usage = (long) (quotas[0].resources[0].usage * 1024);
			long limit = quotas[0].resources[0].limit * 1024;
			long max = limit - usage;
			int percent = (int) ((double) (usage * 10) / limit * 100);
			percent = (int) java.lang.Math.round((double) percent / 10);
			
			int attachMaxSize = webfolderManager.getWebFolderAttachMaxSize(mailDomainSeq);
			attachMaxSize = (attachMaxSize / 1024 / 1024);
			request.setAttribute("attachMaxSize", attachMaxSize);
			request.setAttribute("isFolderShare", new Boolean(folderShare));
			request.setAttribute("isRoot", new Boolean(isRoot));
			request.setAttribute("fid", fid);			
			request.setAttribute("folders", webfolderDataVos);
			request.setAttribute("messages", messages);
			request.setAttribute("nodeNum", nodeNum);
			request.setAttribute("currentPath", currentPath);
			request.setAttribute("pm", pm);
			// ������� �뷮
			request.setAttribute("quotaUsage", FormatUtil.toUnitString(usage, 2));
			// ������� �뷮
			request.setAttribute("quotaUsagePercent", percent);
			// ��ü �뷮
			request.setAttribute("quotaLimit", FormatUtil.toUnitString(limit, 2));
			// ���� �뷮
			request.setAttribute("quotaMax", FormatUtil.toUnitString(max, 2));
			
			request.setAttribute("pageBase", pageBase);
			request.setAttribute("totalCount", nmessges);
		}catch (FolderNotFoundException e) {
			LogManager.writeErr(this, e.getMessage(), e);
			String msg = resource.getMessage("folder.notfound");
			request.setAttribute("path", "/");
			request.setAttribute("nodeNum", "0|");
			request.setAttribute("msg", msg);
			return "result";
		}
		catch(Exception e) {
			isError = true;
			LogManager.writeErr(this, e.getMessage(), e);
		}finally {
			if (folder != null){
				folder.close(true);
				folder = null;
			}
			if (store != null && store.isConnected()) store.close();
		}
		
		if (isError) {
			return "subError2";
		}
		return "success";
	}
	
	public void setWebfolderManager(WebfolderManager webfolderManager) {
		this.webfolderManager = webfolderManager;
	}
	
	public List<TMailFolder> getFolderList() {
		return folderList;
	}

	public void setFolderList(List<TMailFolder> folderList) {
		this.folderList = folderList;
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
	
	public String getSortby() {
		return sortby;
	}

	public void setSortby(String sortby) {
		this.sortby = sortby;
	}

	public String getSortDir() {
		return sortDir;
	}

	public void setSortDir(String sortDir) {
		this.sortDir = sortDir;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
}
