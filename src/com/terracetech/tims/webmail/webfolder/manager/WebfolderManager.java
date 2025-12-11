package com.terracetech.tims.webmail.webfolder.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.dao.SystemConfigDao;
import com.terracetech.tims.webmail.mail.manager.StoreHandler;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.UserInfo;
import com.terracetech.tims.webmail.mailuser.dao.MailUserDao;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.QuotaUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.webfolder.dao.WebfolderDao;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderDataVO;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderQuotaVO;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderShareVO;

@Service
@Transactional
public class WebfolderManager {
	
	private WebfolderDao webfolderDao = null;
	private MailUserDao mailUserDao = null;
	private SystemConfigDao systemConfigDao;
	
	public void setMailUserDao(MailUserDao mailUserDao) {
		this.mailUserDao = mailUserDao;
	}

	public void setWebfolderDao(WebfolderDao webfolderDao) {
		this.webfolderDao = webfolderDao;
	}
	
	public void setSystemConfigDao(SystemConfigDao systemConfigDao) {
		this.systemConfigDao = systemConfigDao;
	}

	public List<TMailFolder> listRFolders(TMailFolder folder) throws MessagingException {
        List<TMailFolder> list = new ArrayList<TMailFolder>();
		TMailFolder[] folders = folder.list();
        Arrays.sort(folders);

        for (int i = 0; i < folders.length; i++) {
        	list.add(folders[i]);
            listRFolders(folders[i]);
        }
        
        return list;
    }
	
	public WebfolderDataVO getShareInfo(String sroot, int mailDomainSeq, int mailUserSeq, int myUserSeq) {
		
		WebfolderDataVO webFolderDataVo = null;
		
		webFolderDataVo = webfolderDao.readAllShareFolder(mailUserSeq, sroot);
		
		if (webFolderDataVo == null) {
			List<WebfolderDataVO> webfolderDataList= webfolderDao.readShareAllFolderData(mailUserSeq, myUserSeq, sroot);
			if (webfolderDataList != null && webfolderDataList.size() > 0) {
				int includeMe = 0;
				int targetType = 0;
				String targetValue = null;
				WebfolderDataVO webFolderTempVo = null;
				for (int i=0; i < webfolderDataList.size(); i++) {
					webFolderTempVo = webfolderDataList.get(i);
					targetType = webFolderTempVo.getTargetType();
					if (targetType == 0) {
						targetValue = webFolderTempVo.getTargetValue();
						includeMe = 0;
						if (targetValue.lastIndexOf("+") != -1) {
							includeMe = webfolderDao.readSubOrgFullCodeCount(mailDomainSeq, myUserSeq, targetValue.substring(0, targetValue.length()-1));
						} else {
							includeMe = webfolderDao.readOrgFullCodeCount(mailDomainSeq, myUserSeq, targetValue);
						}
						
						if (includeMe > 0) {
							webFolderDataVo = webFolderTempVo;
							break;
						}
					} else {
						webFolderDataVo = webFolderTempVo;
						break;
					}
				}
			}
		}

		if (webFolderDataVo != null) {
			webFolderDataVo.setShare("share");
		}
		return webFolderDataVo;
	}
	
	public boolean vaildateShareFolder(String sroot, int mailDomainSeq, int mailUserSeq, int myUserSeq) {
    		
		boolean isShareFolder = true;

		WebfolderDataVO webFolderDataVo = getShareInfo(sroot, mailDomainSeq, mailUserSeq, myUserSeq);
		
		if (webFolderDataVo == null){		
		    isShareFolder = false;
		}
		
		return isShareFolder;
	}
	
	public Map<String, String> getUserInfo (int mailUserSeq) {
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.put("name", mailUserDao.readMailUserName(mailUserSeq));
		userInfo.put("uid", mailUserDao.readMailUid(mailUserSeq));
		
		return userInfo;
	}
	
	public List<WebfolderDataVO> getMyShareFolder(int domainSeq, int userSeq) {
		
		List<WebfolderDataVO> myShareFolder = new ArrayList<WebfolderDataVO>(); 
		
		try {

			List<WebfolderDataVO> publicShareFolder = webfolderDao.readAllShareFolderList(domainSeq, userSeq);
	
			List<WebfolderDataVO> privatefolderList = webfolderDao.readShareFolderList(domainSeq, userSeq);
	
			if (privatefolderList != null && privatefolderList.size() > 0) {
				WebfolderDataVO webfolderDataVo = null;
				int targetType = 0;
				String targetValue = null;
				int includeMe = 0;
				for (int i=0; i < privatefolderList.size(); i++) {
					webfolderDataVo = privatefolderList.get(i);
					targetType = webfolderDataVo.getTargetType();
					if (targetType == 0) {
						targetValue = webfolderDataVo.getTargetValue();
						includeMe = 0;
						if (targetValue.lastIndexOf("+") != -1) {
							includeMe = webfolderDao.readSubOrgFullCodeCount(domainSeq, userSeq, targetValue.substring(0, targetValue.length()-1));
						} else {
							includeMe = webfolderDao.readOrgFullCodeCount(domainSeq, userSeq, targetValue);
						}
						
						if (includeMe > 0) {
							myShareFolder.add(webfolderDataVo);
						}
					} else {
						myShareFolder.add(webfolderDataVo);
					}
				}
			}
			
			if (publicShareFolder != null && publicShareFolder.size() > 0) {
				myShareFolder.addAll(publicShareFolder);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return myShareFolder;
	}
	
	public int getDepthCount(String path){
    	StringTokenizer st = new StringTokenizer(path,".");
    	return st.countTokens()+1;
    }
	
	public void copyRFolders(TMailFolder folder, TMailFolder targetfolder, int dethCnt)
    throws MessagingException {

	    // copy folders
		TMailFolder[] folders = folder.list();
	
	    for (int i = 0; i < folders.length; i++) {
	    	if(dethCnt >= 5){
	    		throw new MessagingException("over deth count!!!");
	    	}
	    	dethCnt++;
	    	
	    	TMailFolder dst = targetfolder.getFolder(folders[i].getName());
	
	        dst.create();
	        copyRFolders(folders[i], dst, dethCnt);
	    }

	    // copy files
	    folder.open(true);
	    TMailMessage[] messages = folder.getMessages();
	            
	    if (messages.length > 0) {        	
	    	targetfolder.appendMessagesBinary(messages);
	    }
	
	    folder.close(true);
	}
	
	public void moveRFolders(TMailFolder folder, TMailFolder targetfolder,int dethCnt)
    throws MessagingException {

	    // move folders
		TMailFolder[] folders = folder.list();
	
	    for (int i = 0; i < folders.length; i++) {
	    	if(dethCnt >= 5){
	    		throw new MessagingException("over deth count!!!");
	    	}
	    	dethCnt++;
	    	
	    	TMailFolder dst = targetfolder.getFolder(folders[i].getName());	
	        dst.create();
		        
	        moveRFolders(folders[i], dst, dethCnt);
	    }
	
	    // move files
	    folder.open(true);
	    TMailMessage[] messages = folder.getMessages();
	
	    if (messages.length > 0) {
	        long[] luids = new long[messages.length];
	
	        for (int i = 0; i < luids.length; i++) {
	            luids[i] = folder.getUID(messages[i]);
	        }
	
	        folder.xmove(luids, targetfolder.getFullName());
	    }
	
	    folder.close(true);
	}
	
	public List<WebfolderShareVO> getSearchShareFolder(int userSeq, String mailUid, String type, String pattern, int skipResult, int maxResult) {
		List<WebfolderShareVO> shareList = webfolderDao.readSearchShareFolder(userSeq, mailUid, type, pattern, skipResult, maxResult);
		if (shareList != null && shareList.size() > 0) {
			for(int i=0; i<shareList.size(); i++) {
				shareList.get(i).setFolderPath(StringUtils.IMAPFolderDecode(shareList.get(i).getFolderPath()));
			}
		}
		return shareList;
	}
	
	public int getSearchShareFolderCount(int userSeq, String mailUid, String type, String pattern) {
		return webfolderDao.readSearchShareFolderCount(userSeq, mailUid, type, pattern);
	}
	
	public void addShareFolder(int userSeq, int fUid) {
		int count = webfolderDao.readShareFolderCountByUid(userSeq, fUid);
		if (count == 0) {
			webfolderDao.saveShareFolder(userSeq, fUid);
		}
	}
	
	public List<WebfolderShareVO> getShareMemberList(int userSeq, String path) {
		return webfolderDao.readShareMemberList(userSeq, path);
	}
	
	public void setShareAllFolder(WebfolderShareVO webfolderShareVo) {
		webfolderDao.saveShareAllFolder(webfolderShareVo);
	}
	
	public void modifyShareAllFolder(WebfolderShareVO webfolderShareVo) {
		webfolderDao.modifyShareAllFolder(webfolderShareVo);
	}
	
	public WebfolderShareVO getShareAllFolderInfo(int userSeq, String path) {
		return webfolderDao.readShareAllFolder1(userSeq, path);
	}
	
	public void setShareTargetFolder(int fuid, String[] readAuthBox, String[] rwAuthBox) {
		StringTokenizer st = null;
		WebfolderShareVO webfolderShareVo = new WebfolderShareVO();
		webfolderShareVo.setFuid(fuid);
		webfolderShareVo.setCurTime(FormatUtil.getBasicDateStr());
		
		if (readAuthBox != null && readAuthBox.length > 0) {
			webfolderShareVo.setAuth("R");
			String targetType = null;
			String targetValue = null;
			String[] data = null;
			for (int i=0; i< readAuthBox.length; i++) {
				st = new StringTokenizer(readAuthBox[i], "|");
				if (st.countTokens() > 0) {
					targetType = st.nextToken();
					targetValue = st.nextToken();
					
					if ("U".equalsIgnoreCase(targetType)) {
						webfolderShareVo.setShareType(1);
						data = targetValue.split("/");
						webfolderShareVo.setEmail(data[0]);
						webfolderShareVo.setName("");
					} else {
						webfolderShareVo.setShareType(0);
						webfolderShareVo.setEmail(targetValue);
						webfolderShareVo.setName("");
					}
					webfolderDao.saveShareTarget(webfolderShareVo);
				}
			}
		}
		
		if (rwAuthBox != null && rwAuthBox.length > 0) {
			webfolderShareVo.setAuth("W");
			String targetType = null;
			String targetValue = null;
			String[] data = null;
			for (int i=0; i< rwAuthBox.length; i++) {
				st = new StringTokenizer(rwAuthBox[i], "|");
				if (st.countTokens() > 0) {
					targetType = st.nextToken();
					targetValue = st.nextToken();
					
					if ("U".equalsIgnoreCase(targetType)) {
						webfolderShareVo.setShareType(1);
						data = targetValue.split("/");
						webfolderShareVo.setEmail(data[0]);
						webfolderShareVo.setName("");
					} else {
						webfolderShareVo.setShareType(0);
						webfolderShareVo.setEmail(targetValue);
						webfolderShareVo.setName("");
					}
					webfolderDao.saveShareTarget(webfolderShareVo);
				}
			}
		}
	}
	
	public void modifyShareTargetFolder(int fuid, String[] readAuthBox, String[] rwAuthBox) {
		webfolderDao.deleteShareTargetFolder(fuid);
		setShareTargetFolder(fuid, readAuthBox, rwAuthBox);
	}
	
	public int getShareTargetFolderCount(int fuid) {
		return webfolderDao.readShareTargetFolderCount(fuid);
	}
	
	public void deleteMyShareFolder(int userSeq, String path) {
		webfolderDao.deleteShareAllFolder(userSeq, path);
	}
	
	public void deleteShareTargetFolder(int fUid) {
		webfolderDao.deleteShareTargetFolder(fUid);
	}
	
	public void deleteShareFolder(int fuid, int userSeq) {
		webfolderDao.deleteShareFolder(fuid, userSeq);
	}
	
	public boolean getPublicFolderAuth(int domainSeq, int userSeq) {
		boolean writeAuth = false;
		int count = webfolderDao.readPublicFolderAuth(userSeq);
		int adminCount = webfolderDao.readPublicFolderAdminAuth(domainSeq, userSeq);
		if (count+adminCount > 0) {
			writeAuth = true;
		}
		return writeAuth;
	}
	
	public Map<String, String> getWebfolderShareConnectInfo(int userSeq) {
		Map<String, String> confMap = new HashMap<String, String>();
		WebfolderQuotaVO webfolderQuotaVo = mailUserDao.readWebfolderInfo(userSeq);
		long webfodlerQuota = WebFolderUtils.calculQuota(0, webfolderQuotaVo.getWebfolderQuota(), webfolderQuotaVo.getWebfolderAddQuota());
		String path = webfolderQuotaVo.getMessageStore();
		String webfolderHome = EnvConstants.getBasicSetting("webfolder.home");
		StringBuffer args = new StringBuffer();
		args.append(path);
		args.append(webfolderHome).append(" ");
		args.append(webfodlerQuota / FileUtil.SIZE_MEGA).append(" ");
		args.append(webfodlerQuota % FileUtil.SIZE_MEGA).append(" ");
		args.append("100000 0 90 50").append(" ");
		args.append(webfolderQuotaVo.getMailUserSeq()).append(" ");
		args.append(webfolderQuotaVo.getMailDomainSeq()).append(" ");		
		
		confMap.put(User.MAIL_UID, webfolderQuotaVo.getMailUid());
		confMap.put(User.MAIL_DOMAIN, webfolderQuotaVo.getMailDomain());
		confMap.put(User.EMAIL, webfolderQuotaVo.getMailUid()+"@"+webfolderQuotaVo.getMailDomain());
		confMap.put(User.MAIL_HOST, systemConfigDao.getHostName(webfolderQuotaVo.getMailHost()));
		confMap.put(User.IMAP_LOGIN_ARGS, args.toString());
		
		return confMap;
	}
	
	public Map<String, String> getWebfolderMyConnectInfo(User user) {
		Map<String, String> confMap = new HashMap<String, String>();
	
		confMap.put(User.MAIL_UID, user.get(User.MAIL_UID));
		confMap.put(User.MAIL_DOMAIN, user.get(User.MAIL_DOMAIN));
		confMap.put(User.EMAIL, user.get(User.EMAIL));
		confMap.put(User.MAIL_HOST, user.get(User.MAIL_HOST));
		confMap.put(User.IMAP_LOGIN_ARGS, user.get(User.WEBFOLDER_LOGIN_ARGS));	
		
		return confMap;
	}
	
	public Map<String, String> getWebfolderPublicConnectInfo(String domain) {
		Map<String, String> confMap = new HashMap<String, String>();
		String adminId = EnvConstants.getUtilSetting("webfolder.admin");
		Map<String, Object> adminMap = mailUserDao.readMailUserAuthInfo(adminId, domain);
		if (adminMap == null || adminMap.isEmpty()) {
			return null;
		}	
		UserInfo userInfo = new UserInfo();
		userInfo.setUserInfoMap(adminMap);
		int userSeq = Integer.parseInt(userInfo.get(User.MAIL_USER_SEQ));
			
		WebfolderQuotaVO webfolderQuotaVo = mailUserDao.readWebfolderInfo(userSeq);
		long webfodlerQuota = WebFolderUtils.calculQuota(EnvConstants.WEBFOLDER_QUOTA_SIZE, webfolderQuotaVo.getWebfolderQuota(), webfolderQuotaVo.getWebfolderAddQuota());
		String path = webfolderQuotaVo.getMessageStore();
		String webfolderHome = EnvConstants.getBasicSetting("webfolder.home");
		StringBuffer args = new StringBuffer();
		args.append(path);
		args.append(webfolderHome).append(" ");
		args.append(webfodlerQuota / FileUtil.SIZE_MEGA).append(" ");
		args.append(webfodlerQuota % FileUtil.SIZE_MEGA).append(" ");
		args.append("100000 0 90 50").append(" ");
		args.append(webfolderQuotaVo.getMailUserSeq()).append(" ");
		args.append(webfolderQuotaVo.getMailDomainSeq());		
		
		confMap.put(User.MAIL_UID, webfolderQuotaVo.getMailUid());
		confMap.put(User.MAIL_DOMAIN, webfolderQuotaVo.getMailDomain());
		confMap.put(User.EMAIL, webfolderQuotaVo.getMailUid()+"@"+webfolderQuotaVo.getMailDomain());
		confMap.put(User.MAIL_HOST, systemConfigDao.getHostName(webfolderQuotaVo.getMailHost()));
		confMap.put(User.IMAP_LOGIN_ARGS, args.toString());
		
		return confMap;
	}
	
	public String[][] getWebFolderAttachFiles(TMailMessage[] messages, String email) throws Exception {
		String[][] attArray = new String[messages.length][3];
		String fileName = null;
		File file = null;
		 for (int i = 0; i < messages.length; i++) {			 
			fileName =  "attach_"+Long.toString(System.nanoTime()) + email + ".u";
			
			file = new File(EnvConstants.getBasicSetting("tmpdir")+EnvConstants.DIR_SEPARATOR+fileName);
			 
			if (!file.exists())
				file.createNewFile();
			
			InputStream in = messages[i].getInputStream();
			FileOutputStream out = new FileOutputStream(file);
			int n = 0;
			int size = 0;
			byte[] buffer = new byte[1024 * 4];
			while ((n = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, n);
				size += n;
			}
			out.close();
			in.close();
			
			String name = messages[i].getSubject();
			String path = file.getPath().replaceAll("\\\\", "/");
			
			 attArray[i][0] = fileName;
             attArray[i][1] = name;
             attArray[i][2] = Integer.toString(size);           
		 }
		 return attArray;
	}
	
	public int getWebFolderAttachMaxSize(int mailDomainSeq) {
		return webfolderDao.readWebFolderAttachMaxSize(mailDomainSeq);
	}
	
	public List<WebfolderShareVO> getMemberList(String type ,String pattern ,String uid, int domain) {
		return webfolderDao.readMemberList(type, pattern, uid, domain);
	}
	
	public int readMyShareFolderUid(int mailUserSeq, String folderDir) {
		return webfolderDao.readMyShareFolderUid(mailUserSeq, folderDir);
	}
	
	public WebfolderDataVO readAllShareFolder(int mailUserSeq, String folderDir) {
		return webfolderDao.readAllShareFolder(mailUserSeq, folderDir);
	}
	
}
