package com.terracetech.tims.service.tms.vo;

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
public class ContactGroupVO {

	private int domainSeq;
	
	/**
	 * <p>Group °íÀ¯ Á¤º¸</p>
	 */
	private int groupSeq;
	
	/**
	 * <p>Group¸í</p>
	 */
	private String groupName;
	
	private int userSeq;
	
	private int adrbookSeq;
	
	public int getDomainSeq() {
		return domainSeq;
	}

	public void setDomainSeq(int domainSeq) {
		this.domainSeq = domainSeq;
	}

	public int getGroupSeq() {
		return groupSeq;
	}

	public void setGroupSeq(int groupSeq) {
		this.groupSeq = groupSeq;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}

	public int getAdrbookSeq() {
		return adrbookSeq;
	}

	public void setAdrbookSeq(int adrbookSeq) {
		this.adrbookSeq = adrbookSeq;
	}
	
}
