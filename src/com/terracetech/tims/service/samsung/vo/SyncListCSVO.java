package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;


public class SyncListCSVO implements Serializable{

	private static final long serialVersionUID = 20091102L;

	/**
	 * 메일함 폴더명
	 */
	private String folderName;
	

	/**
	 * 동기화 대상 메일 정보 목록
	 */
	private SyncMailESBVO[] syncMailESBVOs;


	public String getFolderName() {
		return folderName;
	}


	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}


	public SyncMailESBVO[] getSyncMailESBVOs() {
		return syncMailESBVOs;
	}


	public void setSyncMailESBVOs(SyncMailESBVO[] syncMailESBVOs) {
		this.syncMailESBVOs = syncMailESBVOs;
	}
	
}
