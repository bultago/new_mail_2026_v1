package com.terracetech.tims.webmail.setting.action;

import java.io.File;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserPhotoVO;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class SavePictureAction extends BaseAction {

	private String picturePath = null;
	private String pictureName = null;
	private SettingManager settingManager = null;
	
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}

	public String execute() throws Exception {
		
		UserPhotoVO userPhotoVo = null;
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		JSONObject pictureInfo = new JSONObject();
		
		try {
			if (picturePath != null && !"".equals(picturePath)) {
				File file = new File(picturePath);
				if (file.exists()) {
					userPhotoVo = new UserPhotoVO();
					byte[] userPhoto = FileUtil.readFile(file);
					userPhotoVo.setMailUserSeq(mailUserSeq);
					userPhotoVo.setUserPhoto(userPhoto);
					
					UserPhotoVO userPhotoVoTmp = settingManager.readPictureInfo(mailUserSeq);
					
					if (userPhotoVoTmp == null) {
						settingManager.saveUserPicture(userPhotoVo);
					}
					else {
						settingManager.modifyUserPicture(userPhotoVo);
					}
					ViewPictureAction.cacheMap.remove(user.get(User.MAIL_USER_SEQ));
					pictureInfo.put("isSuccess", true);
				} else {
					pictureInfo.put("isSuccess", false);
					pictureInfo.put("msg", "empty");
				}
			} else {
				pictureInfo.put("isSuccess", false);
				pictureInfo.put("msg", "empty");
			}
		}catch (Exception e) {
			pictureInfo.put("isSuccess", false);
			pictureInfo.put("msg", "error");
			LogManager.writeErr(this, e.getMessage(), e);
		}
		
		ResponseUtil.processResponse(response, pictureInfo);
		
		return null;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}
	
}
