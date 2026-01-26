package com.terracetech.tims.webmail.setting.action;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class DeleteSchedulerAction extends BaseAction {

	private SchedulerManager schedulerManager = null;
	private String checkShareSeq = null;

	public void setSchedulerManager(SchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
	}

	public String execute() throws Exception {
		
		boolean isSuccess = true;
		JSONObject result = new JSONObject();
		try {
			if (StringUtils.isNotEmpty(checkShareSeq)) {
				schedulerManager.deleteShareGroup(Integer.parseInt(checkShareSeq));
			}
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isSuccess = false;
		}
		
		result.put("isSuccess", isSuccess);
		ResponseUtil.processResponse(response, result);
		
		return null;
	}

	public void setCheckShareSeq(String checkShareSeq) {
		this.checkShareSeq = checkShareSeq;
	}

}
