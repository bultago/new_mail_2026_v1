package com.terracetech.tims.service.tms.vo;

public class FolderInfoVO {
	private FolderContentVO[] defaultFolders = null;
	private FolderContentVO[] userFolders = null;
	
	public FolderContentVO[] getDefaultFolders() {
		return defaultFolders;
	}
	public void setDefaultFolders(FolderContentVO[] defaultFolders) {
		this.defaultFolders = defaultFolders;
	}
	public FolderContentVO[] getUserFolders() {
		return userFolders;
	}
	public void setUserFolders(FolderContentVO[] userFolders) {
		this.userFolders = userFolders;
	}	
}
