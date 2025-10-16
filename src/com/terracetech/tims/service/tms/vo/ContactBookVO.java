package com.terracetech.tims.service.tms.vo;

import java.io.Serializable;

/**
 * <p><strong>GroupVo.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li>그룹 정보를 가져오는 VO. 그룹정보에 관한 공통적인  VO객체</li>
 * </ul>
 * @author jpjung
 * @since Tims7
 * @version 7.0 
 */
public class ContactBookVO implements Serializable{
	
	private static final long serialVersionUID = 20100309L;

	private int domainSeq;
	
	private int adrbookSeq;
	
	private String bookName;
	
	private int userSeq;

	public int getDomainSeq() {
		return domainSeq;
	}

	public void setDomainSeq(int domainSeq) {
		this.domainSeq = domainSeq;
	}

	public int getAdrbookSeq() {
		return adrbookSeq;
	}

	public void setAdrbookSeq(int adrbookSeq) {
		this.adrbookSeq = adrbookSeq;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public int getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	
}
