package com.terracetech.tims.service.tms.vo;

public class ListContentVO {	
	private long id = 0;	
	private String folderName = null;
	private String folderEncName = null;
	private String folderFullName = null;
	private String folderDepthName = null;
	private String subject = null;
	private String name = null;	
	private String email = null;
	private String size = null;
	private int byteSize = 0;
	private String flag = null;
	private String date = null;
	private int priority = 0;
	private String preview = null;
    	private String tagNameList = null;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public String getFolderEncName() {
		return folderEncName;
	}
	public void setFolderEncName(String folderEncName) {
		this.folderEncName = folderEncName;
	}
	public String getFolderFullName() {
		return folderFullName;
	}
	public void setFolderFullName(String folderFullName) {
		this.folderFullName = folderFullName;
	}
	public String getFolderDepthName() {
		return folderDepthName;
	}
	public void setFolderDepthName(String folderDepthName) {
		this.folderDepthName = folderDepthName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getByteSize() {
		return byteSize;
	}
	public void setByteSize(int byteSize) {
		this.byteSize = byteSize;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getTagNameList() {
        return tagNameList;
    }

    public void setTagNameList(String tagNameList) {
        this.tagNameList = tagNameList;
    }
}
