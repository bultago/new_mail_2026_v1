package com.terracetech.tims.webmail.common.vo;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.util.StringUtils;

public class DocTemplateVO {
	private int docTemplateSeq = 0;
	private String name;
	private String description;
	private byte[] template;
	
	public int getDocTemplateSeq() {
		return docTemplateSeq;
	}
	public void setDocTemplateSeq(int docTemplateSeq) {
		this.docTemplateSeq = docTemplateSeq;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public byte[] getTemplate() {
		return template;
	}
	public void setTemplate(byte[] template) {
		this.template = template;
	}
	@SuppressWarnings("all")
	public Object toJson(){
		JSONObject jObj = new JSONObject();
		jObj.put("seq", docTemplateSeq);
		jObj.put("name", StringUtils.EscapeHTMLTag(name));
		jObj.put("desc", StringUtils.EscapeHTMLTag(description));
		return jObj;
	}
	
}
