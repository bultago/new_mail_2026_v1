package com.terracetech.tims.service.tms.vo;

public class FolderContentVO {
	private String folderName = null;
	private String fullFolderName = null;
	private String encFolderName = null;	
	private int depth = 0;
	private int total = 0;
	private int unseen = 0;
	
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public String getFullFolderName() {
		return fullFolderName;
	}
	public void setFullFolderName(String fullFolderName) {
		this.fullFolderName = fullFolderName;
	}
	public String getEncFolderName() {
		return encFolderName;
	}
	public void setEncFolderName(String encFolderName) {
		this.encFolderName = encFolderName;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getUnseen() {
		return unseen;
	}
	public void setUnseen(int unseen) {
		this.unseen = unseen;
	}
}
