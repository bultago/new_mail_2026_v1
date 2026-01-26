package com.terracetech.tims.service.aync.data;

public class FolderSyncData {

	private int status = 0;
	
	private String syncKey = null;
	
	private int count = 0;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSyncKey() {
		return syncKey;
	}

	public void setSyncKey(String syncKey) {
		this.syncKey = syncKey;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
