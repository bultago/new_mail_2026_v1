/**
 * MDNResponsesVO.java 2008. 12. 8.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.ibean;

/**
 * <p>
 * <strong>MDNResponsesVO.java</strong> Class Description
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
public class MDNResponsesBean {

	private int mdnResponseTotal;
	
	private int mdnResponsePage;
	
	private String messageTitle;
	
	private String messageID;

	private String sendDate;

	/**
	 * ÀüÃ¼ ¼ö½ÅÀÚ ¼ö
	 */
	private int countTotal;

	/**
	 * ÀÐÀº »ç¶÷
	 */
	private int countRead = 0;
	
	/**
	 * ¾ÈÀÐÀº »ç¶÷
	 */
	private int countUnseen = 0;
	
	/**
	 * ¹ß¼Û½ÇÆÐ
	 */
	private int countFail = 0;
	
	/**
	 * ¹ß¼ÛÃë¼Ò
	 */
	private int countRecall = 0;
	
	/**
	 * ±âÅ¸
	 */
	private int countEtc = 0;

	private MDNResponsesRcptBean rcptVos[] = null;

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}
	
	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public int getCountTotal() {
		return countTotal;
	}

	public void setCountTotal(int countTotal) {
		this.countTotal = countTotal;
	}

	public int getCountRead() {
		return countRead;
	}

	public void setCountRead(int countRead) {
		this.countRead = countRead;
	}

	public int getCountUnseen() {
		return countUnseen;
	}

	public void setCountUnseen(int countUnseen) {
		this.countUnseen = countUnseen;
	}

	public int getCountFail() {
		return countFail;
	}

	public void setCountFail(int countFail) {
		this.countFail = countFail;
	}

	public int getCountRecall() {
		return countRecall;
	}

	public void setCountRecall(int countRecall) {
		this.countRecall = countRecall;
	}

	public int getCountEtc() {
		return countEtc;
	}

	public void setCountEtc(int countEtc) {
		this.countEtc = countEtc;
	}

	public MDNResponsesRcptBean[] getRcptVos() {
		return rcptVos;
	}

	public void setRcptVos(MDNResponsesRcptBean[] rcptVos) {
		this.rcptVos = rcptVos;
	}

	public int getMdnResponseTotal() {
		return mdnResponseTotal;
	}

	public void setMdnResponseTotal(int mdnResponseTotal) {
		this.mdnResponseTotal = mdnResponseTotal;
	}

	public int getMdnResponsePage() {
		return mdnResponsePage;
	}

	public void setMdnResponsePage(int mdnResponsePage) {
		this.mdnResponsePage = mdnResponsePage;
	}
	
}
