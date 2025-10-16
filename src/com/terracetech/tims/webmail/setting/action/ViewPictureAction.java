package com.terracetech.tims.webmail.setting.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserPhotoVO;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class ViewPictureAction extends BaseAction {
	
	private static final long serialVersionUID = 20090925L;
	
	private SettingManager settingManager = null;
	
	private String userSeq = null;
	
	public static Map<String, String> cacheMap = new HashMap<String, String>();
	
	public String getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(String userSeq) {
		this.userSeq = userSeq;
	}

	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
	
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		JSONObject pictureInfo = new JSONObject();
		
		if(StringUtils.isEmpty(userSeq)){
			pictureInfo.put("isExist", false);
			pictureInfo.put("msg", "empty");
			
			ResponseUtil.processResponse(response, pictureInfo);
			return null;
		}
		
		int mailUserSeq = Integer.parseInt(userSeq);
		
		UserPhotoVO userPhotoVo = settingManager.readPictureInfo(mailUserSeq);
		
		try {
			SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmmss");
			String currentDate = sdf.format(new Date());
			
			if (userPhotoVo != null && userPhotoVo.getUserPhoto() != null) {
				String viewSrc = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
				String strLocalhost = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
				String viewUrl = EnvConstants.getAttachSetting("attach.url")+ "/";
				
				viewUrl = strLocalhost + viewUrl;
				String fileName = "picture.gif";
				fileName = userSeq + "_" + currentDate + "_" + fileName;
				File file = null;
				
				Object cache = cacheMap.get(userSeq);
				
				if (cache == null) {
					cacheMap.put(userSeq, fileName);
					file = new File(viewSrc+fileName);
					if (FileUtil.writeFile(userPhotoVo.getUserPhoto(), file)) {
						pictureInfo.put("isExist", true);
						pictureInfo.put("pictureSrc", viewUrl+fileName);
					}
					else {
						pictureInfo.put("isExist", false);
						pictureInfo.put("msg", "empty");
					}
				} else {
					String chcheFileName = (String)cache;
					file = new File(viewSrc+chcheFileName);
					if (FileUtil.writeFile(userPhotoVo.getUserPhoto(), file)) {
						pictureInfo.put("isExist", true);
						pictureInfo.put("pictureSrc", viewUrl+chcheFileName+"?"+currentDate);
					}
					else {
						pictureInfo.put("isExist", false);
						pictureInfo.put("msg", "empty");
					}
					
				}
			} else {
				pictureInfo.put("isExist", false);
				pictureInfo.put("msg", "empty");
			}
		}catch (Exception e) {
			pictureInfo.put("isExist", false);
			pictureInfo.put("msg", "error");
			LogManager.writeErr(this, e.getMessage(), e);
		}
		
		ResponseUtil.processResponse(response, pictureInfo);
		
		return null;
	}

}
