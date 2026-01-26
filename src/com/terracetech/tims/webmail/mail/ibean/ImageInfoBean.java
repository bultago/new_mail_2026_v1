package com.terracetech.tims.webmail.mail.ibean;

public class ImageInfoBean {
	private String cid = null;
	private boolean resize = false;
	private String orgImgName = null;
	private String resizeImgName = null;
	private int width = 0;
	private int height = 0;
	
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public boolean isResize() {
		return resize;
	}
	public void setResize(boolean resize) {
		this.resize = resize;
	}
	public String getOrgImgName() {
		return orgImgName;
	}
	public void setOrgImgName(String orgImgName) {
		this.orgImgName = orgImgName;
	}
	public String getResizeImgName() {
		return resizeImgName;
	}
	public void setResizeImgName(String resizeImgName) {
		this.resizeImgName = resizeImgName;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	
	
	

}
