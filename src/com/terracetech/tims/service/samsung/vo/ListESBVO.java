package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;


public class ListESBVO implements Serializable{

	private static final long serialVersionUID = 20091102L;
	
	/**
	 * 메일함 폴더명
	 */
	private String folderName;

	/**
	 * 총 메일 개수
	 */
	private int iTotalCount;
	
	/**
	 *Mail Header VO 배열
	 */
	private HeaderESBVO[] headerVOs;
	
	/**
	 * 검색 결과 갯수
	 */
	private int iSearchCount;
	
	/**
	 * 현재 페이지 번호
	 */
	private int iPageNo;

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public int getiTotalCount() {
		return iTotalCount;
	}

	public void setiTotalCount(int iTotalCount) {
		this.iTotalCount = iTotalCount;
	}

	public HeaderESBVO[] getHeaderVOs() {
		return headerVOs;
	}

	public void setHeaderVOs(HeaderESBVO[] headerVOs) {
		this.headerVOs = headerVOs;
	}

	public int getiSearchCount() {
		return iSearchCount;
	}

	public void setiSearchCount(int iSearchCount) {
		this.iSearchCount = iSearchCount;
	}

	public int getiPageNo() {
		return iPageNo;
	}

	public void setiPageNo(int iPageNo) {
		this.iPageNo = iPageNo;
	}
	
}
