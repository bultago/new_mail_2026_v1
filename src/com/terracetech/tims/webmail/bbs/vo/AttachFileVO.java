/**
 * AttatchFileVO.java 2008. 11. 26.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.bbs.vo;


/**
 * <p>
 * <strong>AttatchFileVO.java</strong> Class Description
 * </p>
 * <p>
 * ÁÖ¿ä¼³¸í
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
public class AttachFileVO {

	private int bbsId = 0;
	private int mailDomainSeq = 0;
	private int contentId = 0;
	private String attPath = null;
	private String attName = null;
	private int attSize;

	public void setBbsId(int bbsid) {
		this.bbsId = bbsid;
	}

	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public void setContentId(int contentid) {
		this.contentId = contentid;
	}

	public void setAttPath(String attPath) {
		this.attPath = attPath;
	}

	public void setAttName(String attname) {
		this.attName = attname;
	}

	public void setAttSize(int attsize) {
		this.attSize = attsize;
	}

	public int getBbsId() {
		return bbsId;
	}
	
	public int getMailDomainSeq() {
		return mailDomainSeq;
	}

	public int getContentId() {
		return contentId;
	}

	public String getAttPath() {
		return attPath;
	}

	public String getAttName() {
		return attName;
	}

	public int getAttSize() {
		return attSize;
	}

}
