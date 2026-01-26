package com.terracetech.tims.service.aync.action;

import java.util.ArrayList;
import java.util.List;

import com.terracetech.tims.service.aync.data.SyncResultData;
import com.terracetech.tims.webmail.mailuser.User;

public class AbstractSyncAction {

	private User user = null;
	
	private List<SyncResultData> addData = new ArrayList<SyncResultData>();
	private List<SyncResultData> delData = new ArrayList<SyncResultData>();
	private List<SyncResultData> modData = new ArrayList<SyncResultData>();
	private List<SyncResultData> syncData = new ArrayList<SyncResultData>();
	
	public void setUser(User user){
		this.user = user;
	}

	protected User getUser() {
		return user;
	}
	
	public void setAddResult(SyncResultData data){
		addData.add(data);
	}

	public List<SyncResultData> getAddData() {
		return addData;
	}

}
