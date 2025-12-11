package com.terracetech.tims.service.tms.vo;


public class MdnContentVO {
	private int mdnResponseTotal;
	
	private int mdnResponsePage;
	
	private String messageTitle;
	
	private String messageID;

	private String sendDate;
	
	private long preUid = -1;
	private long nextUid = -1;

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
	
	private MdnRcptContentVO rcptVos[] = null;

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

	public long getPreUid() {
		return preUid;
	}

	public void setPreUid(long preUid) {
		this.preUid = preUid;
	}

	public long getNextUid() {
		return nextUid;
	}

	public void setNextUid(long nextUid) {
		this.nextUid = nextUid;
	}

	public MdnRcptContentVO[] getRcptVos() {
		return rcptVos;
	}

	public void setRcptVos(MdnRcptContentVO[] rcptVos) {
		this.rcptVos = rcptVos;
	}
    
    
}
