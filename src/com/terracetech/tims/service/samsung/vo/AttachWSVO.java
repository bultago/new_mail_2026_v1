package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;

import javax.activation.DataHandler;

public class AttachWSVO  implements Serializable{

	private static final long serialVersionUID = 20091102L;
	
	/**
	 * 문서에 첨부 등록시 문서에 대해서 첨부 파일 한개 이상을 등록할 수 있기때문에 
	 * 첨부가 있을시 문서당 FID가 생성됨. 
	 * 문서 수정/삭제시 첨부도 같이 추가/수정/삭제가 필요할때 FID가 필요함. 
	 */
	private String fid = null;

	/**
	 * 첨부 파일이 저장서버에 등록될 때 생성되는 UID
	 * 파일당 하나의 고유 UID 생성됨
	 */
	private String storFid = null;

	/**
	 * 첨부를 공통으로 사용하기때문에 각 모듈명을 입력해야함.
	 * 게시 : BB_Attach
	 * 일정 : CS_Attach
	 * 명함 : BC2_Attach
	 */
	private String jndiName = null;

	/**
	 * 파일의 원본이름
	 */
	private String fileName = null;

	/**
	 * 서버에 올려질때 파일중복을 피하기위해 고유 파일명이 생성됨
	 */
	private String encodedFileName = null;
	
	/**
	 * 파일 사이즈 (byte)
	 */
	private byte fileSize = 0;
	
	/**
	 * 한 문서에 파일이 여러개일 경우 파일간 순서
	 */
	private String fileSeq = null;
	
	/**
	 * 기존 문서에 등록된 파일을 삭제시 Y로 맵핑
	 */
	private String delFlag = null;
	
	/**
	 * 실제 파일을 읽어들여 DataHandler로 주고 받음
	 */
	private DataHandler fileData = null;

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getStorFid() {
		return storFid;
	}

	public void setStorFid(String storFid) {
		this.storFid = storFid;
	}

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getEncodedFileName() {
		return encodedFileName;
	}

	public void setEncodedFileName(String encodedFileName) {
		this.encodedFileName = encodedFileName;
	}

	public DataHandler getFileData() {
		return fileData;
	}

	public void setFileData(DataHandler fileData) {
		this.fileData = fileData;
	}

	public byte getFileSize() {
		return fileSize;
	}

	public void setFileSize(byte fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileSeq() {
		return fileSeq;
	}

	public void setFileSeq(String fileSeq) {
		this.fileSeq = fileSeq;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	
}
