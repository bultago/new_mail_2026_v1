package com.terracetech.tims.service.tms.vo;

public class BbsContentInfoVO {

	private int bbsId = 0;
	private int totalCount = 0;
	private String bbsName = null;
	private String bbsType = null;
	private boolean bbsAdmin = false;
	private boolean bbsCreator = false;
	private String writeAuth = "ADMIN";
	
	private BbsContentListVO[] noticeContentList = null;
	private BbsContentListVO[] bbsContentList = null;
	
	
	public boolean isBbsAdmin() {
		return bbsAdmin;
	}

	public void setBbsAdmin(boolean bbsAdmin) {
		this.bbsAdmin = bbsAdmin;
	}

	public boolean isBbsCreator() {
		return bbsCreator;
	}

	public void setBbsCreator(boolean bbsCreator) {
		this.bbsCreator = bbsCreator;
	}

	public String getWriteAuth() {
		return writeAuth;
	}

	public void setWriteAuth(String writeAuth) {
		this.writeAuth = writeAuth;
	}

	public String getBbsType() {
		return bbsType;
	}

	public void setBbsType(String bbsType) {
		this.bbsType = bbsType;
	}

	public int getBbsId() {
		return bbsId;
	}

	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}

	public String getBbsName() {
		return bbsName;
	}

	public void setBbsName(String bbsName) {
		this.bbsName = bbsName;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public BbsContentListVO[] getNoticeContentList() {
		return noticeContentList;
	}

	public void setNoticeContentList(BbsContentListVO[] noticeContentList) {
		this.noticeContentList = noticeContentList;
	}
	
	public BbsContentListVO[] getBbsContentList() {
		return bbsContentList;
	}

	public void setBbsContentList(BbsContentListVO[] bbsContentList) {
		this.bbsContentList = bbsContentList;
	}
}
