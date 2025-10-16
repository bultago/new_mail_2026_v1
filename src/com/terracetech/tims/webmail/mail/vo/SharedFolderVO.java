package com.terracetech.tims.webmail.mail.vo;

import com.terracetech.tims.mail.TMailUtility;

public class SharedFolderVO {
	private int folderUid = 0;
	private int sharedUserSeq = 0;
	private String folderName = null;
	private String sharedUserName = null;
	private String sharedUserId = null;
		
	public int getFolderUid() {
		return folderUid;
	}
	public void setFolderUid(int folderUid) {
		this.folderUid = folderUid;
	}
	public int getSharedUserSeq() {
		return sharedUserSeq;
	}
	public void setSharedUserSeq(int sharedUserSeq) {
		this.sharedUserSeq = sharedUserSeq;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	
	public String getPrintFolderName() {		
		String str = TMailUtility.IMAPFolderDecode(folderName); 
		int dotIdx = str.lastIndexOf("."); 
		if (dotIdx > 0) { 
			str = str.substring(dotIdx + 1, str.length()); 
		} 
		return str; 
	}
	public String getSharedUserName() {
		return sharedUserName;
	}
	public void setSharedUserName(String sharedUserName) {
		this.sharedUserName = sharedUserName;
	}
	public String getSharedUserId() {
		return sharedUserId;
	}
	public void setSharedUserId(String sharedUserId) {
		this.sharedUserId = sharedUserId;
	}
	
}
