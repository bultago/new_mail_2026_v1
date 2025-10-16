package com.terracetech.tims.webmail.addrbook.vo;

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
public class AddressBookGroupVO {

	/**
	 * <p>Group 고유 정보</p>
	 */
	private int groupSeq;
	
	/**
	 * <p>Group명</p>
	 */
	private String groupName;
	
	private int userSeq;
	
	private int adrbookSeq;

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
