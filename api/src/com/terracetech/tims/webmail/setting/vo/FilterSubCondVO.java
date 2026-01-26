package com.terracetech.tims.webmail.setting.vo;

public class FilterSubCondVO {
	private int userSeq, condSeq, subcondSeq;
	private String field, inorex, regex, pattern, createTime, modifyTime;

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

	public int getSubcondSeq() {
		return subcondSeq;
	}

	public void setSubcondSeq(int subcondSeq) {
		this.subcondSeq = subcondSeq;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getInorex() {
		return inorex;
	}

	public void setInorex(String inorex) {
		this.inorex = inorex;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}