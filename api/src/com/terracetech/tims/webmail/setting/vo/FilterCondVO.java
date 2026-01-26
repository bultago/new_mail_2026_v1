package com.terracetech.tims.webmail.setting.vo;

public class FilterCondVO {

	private int userSeq, condSeq;
	private String condName, condApply, condOp, condPolicy;
	private FilterSubCondVO[] subConds;
	
	public int getUserSeq() {
		return userSeq;
	}
	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	public int getCondSeq() {
		return condSeq;
	}
	public void setCondSeq(int condSeq) {
		this.condSeq = condSeq;
	}
	public String getCondName() {
		return condName;
	}
	public void setCondName(String condName) {
		this.condName = condName;
	}
	public String getCondApply() {
		return condApply;
	}
	public void setCondApply(String condApply) {
		this.condApply = condApply;
	}
	public String getCondOp() {
		return condOp;
	}
	public void setCondOp(String condOp) {
		this.condOp = condOp;
	}
	public String getCondPolicy() {
		return condPolicy;
	}
	public void setCondPolicy(String condPolicy) {
		this.condPolicy = condPolicy;
	}
	public FilterSubCondVO[] getSubConds() {
		return subConds;
	}
	public void setSubConds(FilterSubCondVO[] subConds) {
		this.subConds = subConds;
	}
	
}
