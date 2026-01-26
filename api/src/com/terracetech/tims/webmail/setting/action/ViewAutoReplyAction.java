package com.terracetech.tims.webmail.setting.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.AutoReplyListVO;
import com.terracetech.tims.webmail.setting.vo.AutoReplyVO;
import com.terracetech.tims.webmail.util.StringUtils;


public class ViewAutoReplyAction extends BaseAction{
	
	private static final long serialVersionUID = 20090109L;
	
	private SettingManager manager = null;
	
	private AutoReplyVO replyVo;
	
	private AutoReplyListVO[] whiteList;

	public void setManager(SettingManager manager) {
		this.manager = manager;
	}
	
	public AutoReplyVO getReplyVo() {
		return replyVo;
	}
	
	public AutoReplyListVO[] getWhiteList() {
		return whiteList;
	}

	public String execute() throws Exception{
		
		User user = UserAuthManager.getUser(request);
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		replyVo = manager.readAutoReply(mailUserSeq);
		
		if ((StringUtils.isEmpty(replyVo.getAutoReplyMode())) || ("NONE".equalsIgnoreCase(replyVo.getAutoReplyMode()))) {
			replyVo.setActiveMode("off");
			replyVo.setAutoReplyMode("REPLYALL");
		}
		else {
			replyVo.setActiveMode("on");
		}
	
		if (replyVo.getAutoReplyStartTime() != null) {
			replyVo.setAutoReplyStartTime(replyVo.getAutoReplyStartTime());
		}
		
		if (replyVo.getAutoReplyEndTime() != null) {
			replyVo.setAutoReplyEndTime(replyVo.getAutoReplyEndTime());
		}
		
		List<AutoReplyListVO> list = manager.readAutoReplyWhiteList(mailUserSeq);
		whiteList = list.toArray(new AutoReplyListVO[list.size()]);
		
		return "success";
	}
	
}
