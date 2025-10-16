package com.terracetech.tims.service.mcnc.vo;

import java.io.Serializable;

/**
 * 연락처 데이터 클래스
 * @author kevin
 *
 */
public class ContactWDO implements Serializable{
	
	
	private static final long serialVersionUID = 1331111127262871994L;

	/**
	 * 사진 데이터 없음
	 */
	public static final int		FLAG_PICTURE_DATA_NO			= 0;
	
	/**
	 * 사진 데이터 있음
	 */
	public static final int		FLAG_PICTURE_DATA_INCLUDED		= 1;
	
	/**
	 * 사진 데이터는 있으나, 이 오브젝트에 포함되지 않음
	 */
	public static final int		FLAG_PICTURE_DATA_NOT_INCLUDED	= 2;
	
	private String contactID;
	private String userName;
	private String mailAddress;

	private String companyName;
	private String companyCode;
	private String departmentName;
	private String departmentCode;
	private String jobClassification;

	private String mobile1;
	private String mobile2;
	private String mobile3;
	
	private String officePhone1;
	private String officePhone2;
	private String officePhone3;
	
	private String fax1;
	private String fax2;
	private String fax3;

	private int 			flagPictureData;
	private AttachmentWDO	pictureData;
	
	private PayloadWDO[]			payload;

	
	/**
	 * 연락처 ID를 얻는다.
	 * @return 연락처 ID
	 */
	public String getContactID() {
		return contactID;
	}

	/**
	 * 연락처 ID를 세팅한다.
	 * @param contactID 연락처 ID
	 */
	public void setContactID(String contactID) {
		this.contactID = contactID;
	}

	/**
	 * 회사 이름을 얻는다.
	 * @return 회사 이름
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * 회사 이름을 세팅한다.
	 * @param companyName 회사 이름
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * 이름을 얻는다.
	 * @return 이름
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * 이름을 세팅한다.
	 * @param userName 이름
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * 메일 주소를 얻는다.
	 * @return 메일 주소
	 */
	public String getMailAddress() {
		return mailAddress;
	}
	
	/**
	 * 메일 주소를 세팅한다.
	 * @param mailAddress 메일 주소
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	
	/**
	 * 부서명을 얻는다.
	 * @return 부서명
	 */
	public String getDepartmentName() {
		return departmentName;
	}
	
	/**
	 * 부서명을 세팅한다.
	 * @param departmentName 부서명
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	/**
	 * 직책을 얻는다. 
	 * @return 직책
	 */
	public String getJobClassification() {
		return jobClassification;
	}

	/**
	 * 직책을 세팅한다.
	 * @param jobClassification 직책
	 */
	public void setJobClassification(String jobClassification) {
		this.jobClassification = jobClassification;
	}

	/**
	 * 모바일 폰 앞 번호를 얻는다.
	 * @return 모바일 폰 앞 번호
	 */
	public String getMobile1() {
		return mobile1;
	}

	/**
	 * 모바일 폰 번호를 세팅한다.
	 * @param mobile1 앞 번호
	 */
	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}
	
	/**
	 * 모바일 폰 중간 번호를 얻는다.
	 * @return 모바일 폰 중간 번호
	 */
	public String getMobile2() {
		return mobile2;
	}

	/**
	 * 모바일 폰 번호를 세팅한다.
	 * @param mobile2 중간 번호
	 */
	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}

	/**
	 * 모바일 폰 마지막 번호를 얻는다.
	 * @return 모바일 폰 마지막 번호
	 */
	public String getMobile3() {
		return mobile3;
	}

	/**
	 * 모바일 폰 번호를 세팅한다.
	 * @param mobile3 마지막 번호
	 */
	public void setMobile3(String mobile3) {
		this.mobile3 = mobile3;
	}
	
	/**
	 * 회사 전화 앞 번호를 얻는다.
	 * @return 회사 전화 앞 번호
	 */
	public String getOfficePhone1() {
		return officePhone1;
	}

	/**
	 * 회사 전화 앞 번호를 세팅한다.
	 * @param officePhone1 회사 전화 앞 번호
	 */
	public void setOfficePhone1(String officePhone1) {
		this.officePhone1 = officePhone1;
	}

	/**
	 * 회사 전화 중간 번호를 얻는다.
	 * @return 회사 전화 중간 번호
	 */
	public String getOfficePhone2() {
		return officePhone2;
	}

	/**
	 * 회사 전화 중간 번호를 세팅한다.
	 * @param officePhone2 회사 전화 중간 번호
	 */
	public void setOfficePhone2(String officePhone2) {
		this.officePhone2 = officePhone2;
	}

	/**
	 * 회사 전화 마지막 번호를 얻는다.
	 * @return 회사 전화 마지막 번호
	 */
	public String getOfficePhone3() {
		return officePhone3;
	}

	/**
	 * 회사 전화 마지막 번호를 세팅한다.
	 * @param officePhone3 회사 전화 마지막 번호
	 */
	public void setOfficePhone3(String officePhone3) {
		this.officePhone3 = officePhone3;
	}

	/**
	 * 팩스 앞 번호를 얻는다.
	 * @return 팩스 앞 번호
	 */
	public String getFax1() {
		return fax1;
	}

	/**
	 * 팩스 앞 번호를 세팅한다.
	 * @param fax1 팩스 앞 번호
	 */
	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}

	/**
	 * 팩스 중간 번호를 얻는다.
	 * @return 팩스 중간 번호
	 */
	public String getFax2() {
		return fax2;
	}

	/**
	 * 팩스 중간 번호를 세팅한다.
	 * @param fax2 팩스 중간 번호
	 */
	public void setFax2(String fax2) {
		this.fax2 = fax2;
	}

	/**
	 * 팩스 마지막 번호를 얻는다.
	 * @return 팩스 마지막 번호
	 */
	public String getFax3() {
		return fax3;
	}

	/**
	 * 팩스 마지막 번호를 세팅한다.
	 * @param fax3 팩스 마지막 번호
	 */
	public void setFax3(String fax3) {
		this.fax3 = fax3;
	}

	/**
	 * 사진 데이터 유무에 대한 상태를 얻는다.
	 * @return 사진 데이터 유무 상태
	 */
	public int getFlagPictureData() {
		return flagPictureData;
	}
	
	/**
	 * 사진 데이터 유무에 대한 상태를 세팅한다.
	 * @param flagPictureData 사진 데이터 유무 상태
	 */
	public void setFlagPictureData(int flagPictureData) {
		this.flagPictureData = flagPictureData;
	}
	
	/**
	 * 사진 데이터를 얻는다.
	 * @return 사진 데이터
	 */
	public AttachmentWDO getPictureData() {
		return pictureData;
	}
	
	/**
	 * 사진 데이터를 세팅한다.
	 * @param pictureData 사진 데이터
	 */
	public void setPictureData(AttachmentWDO pictureData) {
		this.pictureData = pictureData;
	}
	

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public PayloadWDO[] getPayload() {
		return payload;
	}

	public void setPayload(PayloadWDO[] payload) {
		this.payload = payload;
	}
}