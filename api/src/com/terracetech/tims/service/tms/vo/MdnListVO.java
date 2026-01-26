package com.terracetech.tims.service.tms.vo;

public class MdnListVO {
	private MdnInfoVO[] mdnList = null;
	private int totalCnt = 0;
	
	public MdnInfoVO[] getMdnList() {
		return mdnList;
	}
	public void setMdnList(MdnInfoVO[] mdnList) {
		this.mdnList = mdnList;
	}
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	
	
}
