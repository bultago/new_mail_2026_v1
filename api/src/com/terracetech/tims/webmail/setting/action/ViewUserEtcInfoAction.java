package com.terracetech.tims.webmail.setting.action;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;

public class ViewUserEtcInfoAction extends BaseAction {

private static final long serialVersionUID = 20090109L;
	
	private SettingManager manager = null;
	
	private UserEtcInfoVO userEtcInfoVo;

	public void setManager(SettingManager manager) {
		this.manager = manager;
	}

	public String execute() throws Exception{
		
		User user = UserAuthManager.getUser(request);
		
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		userEtcInfoVo = manager.readUserEtcInfo(userSeq);
		
		return "success";
	}

	public UserEtcInfoVO getUserEtcInfoVo() {
		return userEtcInfoVo;
	}

	public void setUserEtcInfoVo(UserEtcInfoVO userEtcInfoVo) {
		this.userEtcInfoVo = userEtcInfoVo;
	}
	
}
