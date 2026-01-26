package com.terracetech.tims.service.tms.vo;

import java.io.Serializable;

/**
 * <p><strong>GroupVo.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li>±×·ì Á¤º¸¸¦ °¡Á®¿À´Â VO. ±×·ìÁ¤º¸¿¡ °üÇÑ °øÅëÀûÀÎ  VO°´Ã¼</li>
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
