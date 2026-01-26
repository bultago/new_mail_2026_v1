package com.terracetech.tims.service.aync.data;

import java.util.ArrayList;
import java.util.List;

public class PingData {
	
	private String syncKey = null;
	
	private int heartbeatInterval = 0;

	private List<FolderData> folders = null;

	public PingData() {
		folders = new ArrayList<FolderData>();
	}
	
	public String getSyncKey() {
		return syncKey;
	}

	public void setSyncKey(String syncKey) {
		this.syncKey = syncKey;
	}

	public int getHeartbeatInterval() {
		return heartbeatInterval;
	}

	public void setHeartbeatInterval(int heartbeatInterval) {
		this.heartbeatInterval = heartbeatInterval;
	}

	public void addFolder(FolderData folder){
		folders.add(folder);
	}

	public List<FolderData> getFolders() {
		return folders;
	}
	
}
