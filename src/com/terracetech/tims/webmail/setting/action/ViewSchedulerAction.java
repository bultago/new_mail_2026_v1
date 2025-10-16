package com.terracetech.tims.webmail.setting.action;

import java.util.List;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareVO;

public class ViewSchedulerAction extends BaseAction {

	private List<SchedulerShareVO> shareGroupList = null;
	private SchedulerManager schedulerManager = null;

	public void setSchedulerManager(SchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
	}

	public String execute() throws Exception {
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		shareGroupList = schedulerManager.getShareGroupList(mailUserSeq);
		
		return "success";
	}

	public List<SchedulerShareVO> getShareGroupList() {
		return shareGroupList;
	}
}
