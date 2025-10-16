package com.terracetech.tims.service.tms.vo;

public class BbsInfoVO {

	private int bbsId = 0;
	private int bbsDepth = 0;
	private String bbsName = null;
	private String bbsType = null;
	private String bbsFullId = null;
	private boolean existNew = false;
	
	
	
	public boolean isExistNew() {
		return existNew;
	}
	public void setExistNew(boolean existNew) {
		this.existNew = existNew;
	}
	public int getBbsId() {
		return bbsId;
	}
	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}
	public int getBbsDepth() {
		return bbsDepth;
	}
	public void setBbsDepth(int bbsDepth) {
		this.bbsDepth = bbsDepth;
	}
	public String getBbsName() {
		return bbsName;
	}
	public void setBbsName(String bbsName) {
		this.bbsName = bbsName;
	}
	public String getBbsType() {
		return bbsType;
	}
	public void setBbsType(String bbsType) {
		this.bbsType = bbsType;
	}
	public String getBbsFullId() {
		return bbsFullId;
	}
	public void setBbsFullId(String bbsFullId) {
		this.bbsFullId = bbsFullId;
	}
	
}
