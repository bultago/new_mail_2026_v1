package com.terracetech.tims.webmail.webfolder.vo;

public class WebfolderShareVO {
	
	private String uid;
	
	private String domain;
	
	private int mailUserSeq;
	
	private String name;
	
	private String email;
	
	private String auth;
	
	private int type;
	
	private int fuid;
	
	private String folderPath;
	
	private String comment;
	
	private int shareType;
	
	private String curTime;
	
	private String folderName;
	
	private String createTime;
	
	private String modTime;
	
	private boolean allShare;
	
	private boolean alluserCheck;
	
	private String allShareAuth;
	
	private boolean alreadyShare;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public int getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}

	public String getDomain() {
		return domain;
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

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public int getFuid() {
		return fuid;
	}

	public void setFuid(int fuid) {
		this.fuid = fuid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getShareType() {
		return shareType;
	}

	public void setShareType(int shareType) {
		this.shareType = shareType;
	}

	public String getCurTime() {
		return curTime;
	}

	public void setCurTime(String curTime) {
		this.curTime = curTime;
	}
	
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModTime() {
		return modTime;
	}

	public void setModTime(String modTime) {
		this.modTime = modTime;
	}


	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public boolean isAllShare() {
		return allShare;
	}

	public void setAllShare(boolean allShare) {
		this.allShare = allShare;
	}

	public boolean isAlluserCheck() {
		return alluserCheck;
	}

	public void setAlluserCheck(boolean alluserCheck) {
		this.alluserCheck = alluserCheck;
	}

	public String getAllShareAuth() {
		return allShareAuth;
	}

	public void setAllShareAuth(String allShareAuth) {
		this.allShareAuth = allShareAuth;
	}

	public boolean isAlreadyShare() {
		return alreadyShare;
	}

	public void setAlreadyShare(boolean alreadyShare) {
		this.alreadyShare = alreadyShare;
	}
}
