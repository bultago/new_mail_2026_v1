package com.terracetech.tims.webmail.setting.vo;

public class UserPhotoVO {
    
    private int mailUserSeq;

    private byte[] userPhoto;

	public int getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}

	public byte[] getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(byte[] userPhoto) {
		this.userPhoto = userPhoto;
	}
    
}