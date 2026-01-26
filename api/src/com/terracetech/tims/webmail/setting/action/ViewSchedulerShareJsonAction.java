package com.terracetech.tims.webmail.setting.action;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class ViewSchedulerShareJsonAction extends BaseAction {

	private SchedulerManager schedulerManager = null;
	private int shareSeq = 0;

	public void setSchedulerManager(SchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
	}

	public String execute() throws Exception {
		JSONObject shareGroup = schedulerManager.getShareGroup(shareSeq);
		
		ResponseUtil.processResponse(response, shareGroup);
		
		return null;
	}

	public void setShareSeq(int shareSeq) {
		this.shareSeq = shareSeq;
	}
	
}
