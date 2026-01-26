package com.terracetech.tims.webmail.webfolder.action;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderDataVO;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderShareVO;

public class ShareFolderAction extends BaseAction {
	private WebfolderManager webfolderManager = null;
	private List<WebfolderShareVO> userList = null;
	private WebfolderShareVO folder = null;
	private int fuid = 0;
	private String path = null;
	private String nodeNum = null;
	private String type = null;
	
	public void setWebfolderManager(WebfolderManager webfolderManager) {
		this.webfolderManager = webfolderManager;
	}
	
	public String execute() throws Exception {
		
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		String folderShare = path.substring(1);
		
		String fpath = StringUtils.IMAPFolderEncode(folderShare);
		fpath = fpath.replaceAll("/",".");

		String fname = folderShare.substring(folderShare.lastIndexOf("/")+1);

		userList = webfolderManager.getShareMemberList(userSeq, fpath);
		
		String mode = "new";
		if (userList != null && userList.size() > 0) {
			int targetType = 0;
			String targetValue = null;
			int mailUserSeq = 0;
			Map<String, String> userInfo = null;
			String uid = null;
			String name = null;
			
			for (int i=0; i< userList.size(); i++) {
				try {
					targetType = userList.get(i).getType();
					targetValue = userList.get(i).getEmail();
					
					if (StringUtils.isNotEmpty(targetValue)) {
						if (targetType == 1) {
							mailUserSeq = Integer.parseInt(targetValue);
							userInfo = webfolderManager.getUserInfo(mailUserSeq);
							uid = userInfo.get("uid");
							name = userInfo.get("name");
							userList.get(i).setUid(uid);
							userList.get(i).setName(name);
						}
					}
				}
				catch (Exception e) {
				}
			}
			mode= "modify";
		}
		
		
		folder = new WebfolderShareVO();
		folder.setFuid(fuid);
		folder.setFolderPath(fpath);
		folder.setFolderName(fname);
		
		WebfolderShareVO webfolderShareVo = webfolderManager.getShareAllFolderInfo(userSeq, fpath);
		if (webfolderShareVo != null) {
			folder.setComment(webfolderShareVo.getComment());
		}
		
		WebfolderDataVO webfolderDataVo = webfolderManager.readAllShareFolder(userSeq, fpath);
		if (webfolderDataVo != null) {
			folder.setAllShare(true);
			folder.setAllShareAuth(webfolderDataVo.getPrivil());
			folder.setAuth("");
			folder.setAlluserCheck(true);
			
			mode= "modify";
		}
		
		request.setAttribute("mode", mode);
		
		return "success";
	}

	public int getFuid() {
		return fuid;
	}

	public void setFuid(int fuid) {
		this.fuid = fuid;
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
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<WebfolderShareVO> getUserList() {
		return userList;
	}

	public void setUserList(List<WebfolderShareVO> userList) {
		this.userList = userList;
	}

	public WebfolderShareVO getFolder() {
		return folder;
	}

	public void setFolder(WebfolderShareVO folder) {
		this.folder = folder;
	}
	
}
