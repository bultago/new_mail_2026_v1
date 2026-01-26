package com.terracetech.tims.webmail.setting.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.AutoReplyVO;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class SaveAutoReplyAction extends BaseAction{

	private static final long serialVersionUID = 20090109L;
	
	private SettingManager manager = null;
	
	private String mode;
	
	private String replyMode;
	
	private String startTime;
	
	private String endTime;

	private String[] whiteList;
	
	private String autoReplyText;

	private String autoReplySubject;
	
	public void setWhiteList(String[] whiteList) {
		this.whiteList = whiteList;
	}
	
	public void setAutoReplyText(String autoReplyText) {
		this.autoReplyText = autoReplyText;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setReplyMode(String replyMode) {
		this.replyMode = replyMode;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setManager(SettingManager manager) {
		this.manager = manager;
	}
	
	public String execute() throws Exception{
		I18nResources resource = getMessageResource("setting");
		
		User user = UserAuthManager.getUser(request);
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String rMode = "NONE";
		String msg = null;
		try {
			AutoReplyVO autoReplyVO = new AutoReplyVO();
			autoReplyVO.setUserSeq(mailUserSeq);
			
			if ("on".equals(mode)) {
				rMode = replyMode;
			} else {
				rMode = "NONE";
			}
			
			autoReplyVO.setAutoReplyMode(rMode);
			if (StringUtils.isNotEmpty(startTime)) {
				autoReplyVO.setAutoReplyStartTime(FormatUtil.getDateStr(startTime+"-00-00-00"));
			}
			if (StringUtils.isNotEmpty(endTime)) {
				autoReplyVO.setAutoReplyEndTime(FormatUtil.getDateStr(endTime+"-23-59-59"));
			}
			autoReplyVO.setAutoReplyText(autoReplyText);
			autoReplyVO.setAutoReplySubject(autoReplySubject);
			
			manager.saveAutoReply(autoReplyVO, whiteList);
			msg = resource.getMessage("update.ok");
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("update.fail");
		}
		request.setAttribute("msg", msg);
		return "success";
	}

	public String getAutoReplySubject() {
		return autoReplySubject;
	}

	public void setAutoReplySubject(String autoReplySubject) {
		this.autoReplySubject = autoReplySubject;
	}
}
