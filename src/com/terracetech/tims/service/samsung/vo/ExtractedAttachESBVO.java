package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;

public class ExtractedAttachESBVO implements Serializable{

	private static final long serialVersionUID = 20091102L;

	/**
	 * 다운로드 서버 정보
	 */
	private String downloadServer;
	
	/**
	 * 첨부파일 full path
	 */
	private String realFilePath;

	public String getDownloadServer() {
		return downloadServer;
	}

	public void setDownloadServer(String downloadServer) {
		this.downloadServer = downloadServer;
	}

	public String getRealFilePath() {
		return realFilePath;
	}

	public void setRealFilePath(String realFilePath) {
		this.realFilePath = realFilePath;
	}
}
