package com.terracetech.tims.webmail.setting.vo;

public class PSpamRuleVO {

	private int userSeq;
	private String applyAllowedlistOnly, applyWhitelist, applyBlacklist, 
		applyPbaysian, applyRuleLevel, pspamRuleLevel, pspamPolicy;
	
	private PSpameListItemVO[] whiteList, blackList;
	
	public int getUserSeq() {
		return userSeq;
	}
	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	public String getApplyAllowedlistOnly() {
		return applyAllowedlistOnly;
	}
	public void setApplyAllowedlistOnly(String applyAllowedlistOnly) {
		this.applyAllowedlistOnly = applyAllowedlistOnly;
	}
	public String getApplyWhitelist() {
		return applyWhitelist;
	}
	public void setApplyWhitelist(String applyWhitelist) {
		this.applyWhitelist = applyWhitelist;
	}
	public String getApplyBlacklist() {
		return applyBlacklist;
	}
	public void setApplyBlacklist(String applyBlacklist) {
		this.applyBlacklist = applyBlacklist;
	}
	public String getApplyPbaysian() {
		return applyPbaysian;
	}
	public void setApplyPbaysian(String applyPbaysian) {
		this.applyPbaysian = applyPbaysian;
	}
	public String getApplyRuleLevel() {
		return applyRuleLevel;
	}
	public void setApplyRuleLevel(String applyRuleLevel) {
		this.applyRuleLevel = applyRuleLevel;
	}
	public String getPspamRuleLevel() {
		return pspamRuleLevel;
	}
	public void setPspamRuleLevel(String pspamRuleLevel) {
		this.pspamRuleLevel = pspamRuleLevel;
	}
	public String getPspamPolicy() {
		return pspamPolicy;
	}
	public void setPspamPolicy(String pspamPolicy) {
		this.pspamPolicy = pspamPolicy;
	}
	public PSpameListItemVO[] getWhiteList() {
		return whiteList;
	}
	public void setWhiteList(PSpameListItemVO[] whiteList) {
		this.whiteList = whiteList;
	}
	public PSpameListItemVO[] getBlackList() {
		return blackList;
	}
	public void setBlackList(PSpameListItemVO[] blackList) {
		this.blackList = blackList;
	}
}
