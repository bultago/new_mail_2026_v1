package com.terracetech.tims.webmail.setting.action;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserPhotoVO;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class DeletePictureAction extends BaseAction {

	private SettingManager settingManager = null;

	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
	
	public String execute() throws Exception {
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		JSONObject pictureInfo = new JSONObject();
		
		try {
			UserPhotoVO userPhotoVo = settingManager.readPictureInfo(mailUserSeq);
			if (userPhotoVo != null) {
				settingManager.deleteUserPicture(mailUserSeq);
				ViewPictureAction.cacheMap.remove(user.get(User.MAIL_USER_SEQ));
				pictureInfo.put("isSuccess", true);
			} else {
				pictureInfo.put("isSuccess", false);
				pictureInfo.put("msg", "empty");
			}
			
		} catch (Exception e) {
			pictureInfo.put("isSuccess", false);
			pictureInfo.put("msg", "error");
			LogManager.writeErr(this, e.getMessage(), e);
		}
		
		ResponseUtil.processResponse(response, pictureInfo);
		
		return null;
	}
}
