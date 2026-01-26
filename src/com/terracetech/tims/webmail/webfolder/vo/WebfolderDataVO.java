package com.terracetech.tims.webmail.webfolder.vo;

public class WebfolderDataVO {
	
	private String folderName;
	
	private boolean child;
	
	private String path;
	
	private String realPath;
	
	private int mailUserSeq;
	
	private String mailUid;
	
	private String nodeNumber;
	
	private String share;
	
	private String shareRoot;
	
	private String privil;
	
	private int fuid;
	
	private int targetType = 0;
	
	private String targetValue = null;

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public boolean isChild() {
		return child;
	}

	public void setChild(boolean child) {
		this.child = child;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public String getNodeNumber() {
		return nodeNumber;
	}

	public void setNodeNumber(String nodeNumber) {
		this.nodeNumber = nodeNumber;
	}

	public String getShare() {
		return share;
	}

	public void setShare(String share) {
		this.share = share;
	}

	public String getShareRoot() {
		return shareRoot;
	}

	public void setShareRoot(String shareRoot) {
		this.shareRoot = shareRoot;
	}

	public String getPrivil() {
		return privil;
	}

	public void setPrivil(String privil) {
		this.privil = privil;
	}

	public int getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}
	
	public String getMailUid() {
		return mailUid;
	}

	public void setMailUid(String mailUid) {
		this.mailUid = mailUid;
	}

	public int getFuid() {
		return fuid;
	}

	public void setFuid(int fuid) {
		this.fuid = fuid;
	}

	public int getTargetType() {
		return targetType;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}
}
