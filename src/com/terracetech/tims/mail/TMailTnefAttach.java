package com.terracetech.tims.mail;

import net.freeutils.tnef.Attachment;

public class TMailTnefAttach {
	private String attachKey = null;
	private String fileName = null;
	private Attachment attachment = null;
	
	
	public String getAttachKey() {
		return attachKey;
	}
	public void setAttachKey(String attachKey) {
		this.attachKey = attachKey;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Attachment getAttachment() {
		return attachment;
	}
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}
	
	
}
