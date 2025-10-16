package com.terracetech.tims.webmail.mail.action;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class FolderInfoAction extends BaseAction {
	
	private static final long serialVersionUID = -4406949680538512301L;
	
	private MailManager mailManager = null;
	private SettingManager userSettingManager = null;
	
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	public void setUserSettingManager(SettingManager userSettingManager) {
		this.userSettingManager = userSettingManager;
	}

	public void prepare() throws Exception {
		String info = request.getParameter("info");
		boolean isInfo = (info != null) ? true : false;
		if(!isInfo){
			setAuthCheck(false);
		}
		super.prepare();
	}
    
	public String execute() throws Exception {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		JSONObject jObj = new JSONObject();		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();		
		I18nResources resource = getMessageResource();
		
		String info = request.getParameter("info");
		boolean isInfo = (info != null)?true:false;
		
		
		try {			
			store = factory.connect(request.getRemoteAddr(), user);
		} catch (Exception e) {
			throw new Exception(resource.getMessage("error.imapconn"));
		} 
		
		JSONArray defaultList = null;
		JSONArray userList = null;
		try{
			mailManager.setProcessResource(store, getMessageResource());
			JSONArray folderList = new JSONArray();
			defaultList = mailManager.getJsonFolderList(EnvConstants.DEFAULT_FOLDER,false,-1);
			userList = mailManager.getJsonFolderList(EnvConstants.USER_FOLDER,false,-1);
			
			int size = defaultList.size();
			for (int i = 0; i < size ; i++) {
				folderList.add(defaultList.get(i));
			}
			
			size = userList.size();
			for (int i = 0; i < size ; i++) {
				folderList.add(userList.get(i));
			}
			
			if(!isInfo){
				UserEtcInfoVO userSettingVo = userSettingManager.readUserEtcInfo(userSeq);
				jObj.put("quotaInfo",(mailManager.getQuotaRootInfo("Inbox").toJson()).get("percent"));
				jObj.put("alrimInteval",(userSettingVo != null)?userSettingVo.getNotiInterval():"0");
			}			
			jObj.put("folderInfo",folderList);			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new Exception(resource.getMessage("error.folderInfo"));
		} finally{
			if(store !=null && store.isConnected())
				store.close();			
			defaultList = null;
			userList = null;
		}
		
		ResponseUtil.processResponse(response,jObj);
		
		return null;
	}
}
