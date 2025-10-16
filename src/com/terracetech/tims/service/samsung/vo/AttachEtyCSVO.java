package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;

import javax.activation.DataHandler;

public class AttachEtyCSVO implements Serializable{

	private static final long serialVersionUID = 20091102L;
	
	/**
	 * 일련번호
	 */
	private int iSeqID;
	
	/**
	 * 실제 파일명
	 */
	private String fileName;
	
	/**
	 * 파일경로
	 */
	private String localPath;
	
	/**
	 * 서버 임시파일명
	 */
	private String localFileName;
	
	/**
	 * 첨부파일크기
	 */
	private long fileSize;
	
	/**
	 * 첨부파일 서버정보
	 */
	private String fileServer;
	
	/**
	 * 파일정보키
	 *  “3124<-1”
	 */
	private String msgKeyId;
	
	/**
	 * File file = new File("/TEST/test.ppt");
 	 * FileDataSource fds = new FileDataSource(file);
 	 * DataHandler dh = new DataHandler(fds);
	 */
	private DataHandler file;

	public int getiSeqID() {
		return iSeqID;
	}

	public void setiSeqID(int iSeqID) {
		this.iSeqID = iSeqID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getLocalFileName() {
		return localFileName;
	}

	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileServer() {
		return fileServer;
	}

	public void setFileServer(String fileServer) {
		this.fileServer = fileServer;
	}

	public String getMsgKeyId() {
		return msgKeyId;
	}

	public void setMsgKeyId(String msgKeyId) {
		this.msgKeyId = msgKeyId;
	}

	public DataHandler getFile() {
		return file;
	}

	public void setFile(DataHandler file) {
		this.file = file;
	}
	
}
