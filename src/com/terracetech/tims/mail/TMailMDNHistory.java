package com.terracetech.tims.mail;

import java.util.Date;

public class TMailMDNHistory {
	private int readSeq = 0;
	private Date date = null;
	
	public TMailMDNHistory(int readSeq, Date date) {
		this.readSeq = readSeq;
		this.date = date;
	}

	public int getReadSeq() {
		return readSeq;
	}

	public void setReadSeq(int readSeq) {
		this.readSeq = readSeq;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
