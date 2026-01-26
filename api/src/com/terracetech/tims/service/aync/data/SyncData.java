package com.terracetech.tims.service.aync.data;

import java.util.ArrayList;
import java.util.List;

public class SyncData {

	private int status = 0;
	
	private String syncKey = null;
	
	private String target = null;
	
	private int count = 0;
	
	private List<Object> addClientDataList = new ArrayList<Object>();
	private List<Object> modClientDataList = new ArrayList<Object>();
	private List<Object> delClientDataList = new ArrayList<Object>();
	
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

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public List<Object> getAddClientDataList() {
		return addClientDataList;
	}

	public void setAddClientData(Object data) {
		this.addClientDataList.add(data);
	}

	public List<Object> getModClientDataList() {
		return modClientDataList;
	}

	public void setModClientData(Object data) {
		this.modClientDataList.add(data);
	}

	public List<Object> getDelClientDataList() {
		return delClientDataList;
	}

	public void setDelClientData(Object data) {
		this.delClientDataList.add(data);
	}
	
}
