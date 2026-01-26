package com.terracetech.tims.service.aync.data;

public class SyncResultData {

	private String clientId = null;
	
	private String serverId = null;
	
	private int Status = 0;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}
}
