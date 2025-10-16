package com.terracetech.tims.service.tms.vo;

import java.io.Serializable;

public class AttachFileVO implements Serializable{
	
	private static final long serialVersionUID = 20100304L;
	
	private long size = 0;
	private String name = null;
	private String type = null;
	private String depth = null;
	private boolean uploaded = false;
	
	private byte[] filedata = null;
	
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public byte[] getFiledata() {
		return filedata;
	}
	public void setFiledata(byte[] filedata) {
		this.filedata = filedata;
	}
	public boolean isUploaded() {
		return uploaded;
	}
	public void setUploaded(boolean uploaded) {
		this.uploaded = uploaded;
	}
	
}
