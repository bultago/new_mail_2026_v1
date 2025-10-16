/**
 * MailUserAddressVO.java 2008  2008-09-26
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.vo;

/**
 * <p><strong>MailUserAddressVO.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li>사용자 주소 정보를 가져오는 VO. 주소정보에 관한 공통적인  VO객체</li>
 * <li>주소정보에관한 정보 포함. outlook 기준의 정보 필드 포함.</li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class MailUserAddressVO {

	/**
	 * <p>핸드폰 번호 정보</p>
	 */
	private String mobileNo = null;
	/**
	 * <p>집 전화번호 정보</p>
	 */
	private String homeTel = null;
	/**
	 * <p>집 팩스번호 정보</p>
	 */
	private String homeFax = null;
	/**
	 * <p>집 우편번호</p>
	 */
	private String homePostalCode = null;
	/**
	 * <p>사용자 국가 정보</p>
	 */
	private String homeCountry = null;
	/**
	 * <p>사용자의 거주 주 정보</p>
	 */
	private String homeState = null;
	/**
	 * <p>사용자 도시 정보</p>
	 */
	private String homeCity = null;
	/**
	 * <p>사용자의 주소 Street 정보</p>
	 */
	private String homeStreet = null;
	/**
	 * <p>기본 주소 정보</p>
	 */
	private String homeBasicAddress = null;
	/**
	 * <p>확장 주소 정보</p>
	 */
	private String homepage = null;
	/**
	 * <p>회사명 정보</p>
	 */
	private String companyName = null;
	/**
	 * <p>부서 정보</p>
	 */
	private String departmentName = null;
	/**
	 * <p>사무실 전화 번호 정보</p>
	 */
	private String officeTel = null;
	/**
	 * <p>사무실 팩스번호 정보</p>
	 */
	private String officeFax = null;
	/**
	 * <p>사무실 우편 번호 정보</p>
	 */
	private String officePostalCode = null;
	/**
	 * <p>사무실 이 위치한 국가 정보</p>
	 */
	private String officeCountry = null;
	/**
	 * <p>사무실 지역 주 정보</p>
	 */
	private String officeState = null;
	/**
	 * <p>사무실 위치 도시 정보</p>
	 */
	private String officeCity = null;
	/**
	 * <p>사무실 Street 정보</p>
	 */
	private String officeStreet = null;
	/**
	 * <p>사무실 기본 주소 정보</p>
	 */
	private String officeBasicAddress = null;
	/**
	 * <p>사무실 확장 주소 정보</p>
	 */
	private String officeExtAddress = null;
	/**
	 * <p>기타 설명 정보</p>
	 */
	private String description = null;
	
	/**
	 * @return mobileNo 값 반환
	 */
	public String getMobileNo() {
		return mobileNo;
	}
	/**
	 * @param mobileNo 파라미터를 mobileNo값에 설정
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	/**
	 * @return homeTel 값 반환
	 */
	public String getHomeTel() {
		return homeTel;
	}
	/**
	 * @param homeTel 파라미터를 homeTel값에 설정
	 */
	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}
	/**
	 * @return homeFax 값 반환
	 */
	public String getHomeFax() {
		return homeFax;
	}
	/**
	 * @param homeFax 파라미터를 homeFax값에 설정
	 */
	public void setHomeFax(String homeFax) {
		this.homeFax = homeFax;
	}
	/**
	 * @return homePostalCode 값 반환
	 */
	public String getHomePostalCode() {
		return homePostalCode;
	}
	/**
	 * @param homePostalCode 파라미터를 homePostalCode값에 설정
	 */
	public void setHomePostalCode(String homePostalCode) {
		this.homePostalCode = homePostalCode;
	}
	/**
	 * @return homeCountry 값 반환
	 */
	public String getHomeCountry() {
		return homeCountry;
	}
	/**
	 * @param homeCountry 파라미터를 homeCountry값에 설정
	 */
	public void setHomeCountry(String homeCountry) {
		this.homeCountry = homeCountry;
	}
	/**
	 * @return homeState 값 반환
	 */
	public String getHomeState() {
		return homeState;
	}
	/**
	 * @param homeState 파라미터를 homeState값에 설정
	 */
	public void setHomeState(String homeState) {
		this.homeState = homeState;
	}
	/**
	 * @return homeCity 값 반환
	 */
	public String getHomeCity() {
		return homeCity;
	}
	/**
	 * @param homeCity 파라미터를 homeCity값에 설정
	 */
	public void setHomeCity(String homeCity) {
		this.homeCity = homeCity;
	}
	/**
	 * @return homeStreet 값 반환
	 */
	public String getHomeStreet() {
		return homeStreet;
	}
	/**
	 * @param homeStreet 파라미터를 homeStreet값에 설정
	 */
	public void setHomeStreet(String homeStreet) {
		this.homeStreet = homeStreet;
	}
	/**
	 * @return homeBasicAddress 값 반환
	 */
	public String getHomeBasicAddress() {
		return homeBasicAddress;
	}
	/**
	 * @param homeBasicAddress 파라미터를 homeBasicAddress값에 설정
	 */
	public void setHomeBasicAddress(String homeBasicAddress) {
		this.homeBasicAddress = homeBasicAddress;
	}
	/**
	 * @return homepage 값 반환
	 */
	public String getHomepage() {
		return homepage;
	}
	/**
	 * @param homepage 파라미터를 homepage값에 설정
	 */
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	/**
	 * @return companyName 값 반환
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName 파라미터를 companyName값에 설정
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @return departmentName 값 반환
	 */
	public String getDepartmentName() {
		return departmentName;
	}
	/**
	 * @param departmentName 파라미터를 departmentName값에 설정
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	/**
	 * @return officeTel 값 반환
	 */
	public String getOfficeTel() {
		return officeTel;
	}
	/**
	 * @param officeTel 파라미터를 officeTel값에 설정
	 */
	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}
	/**
	 * @return officeFax 값 반환
	 */
	public String getOfficeFax() {
		return officeFax;
	}
	/**
	 * @param officeFax 파라미터를 officeFax값에 설정
	 */
	public void setOfficeFax(String officeFax) {
		this.officeFax = officeFax;
	}
	/**
	 * @return officePostalCode 값 반환
	 */
	public String getOfficePostalCode() {
		return officePostalCode;
	}
	/**
	 * @param officePostalCode 파라미터를 officePostalCode값에 설정
	 */
	public void setOfficePostalCode(String officePostalCode) {
		this.officePostalCode = officePostalCode;
	}
	/**
	 * @return officeCountry 값 반환
	 */
	public String getOfficeCountry() {
		return officeCountry;
	}
	/**
	 * @param officeCountry 파라미터를 officeCountry값에 설정
	 */
	public void setOfficeCountry(String officeCountry) {
		this.officeCountry = officeCountry;
	}
	/**
	 * @return officeState 값 반환
	 */
	public String getOfficeState() {
		return officeState;
	}
	/**
	 * @param officeState 파라미터를 officeState값에 설정
	 */
	public void setOfficeState(String officeState) {
		this.officeState = officeState;
	}
	/**
	 * @return officeCity 값 반환
	 */
	public String getOfficeCity() {
		return officeCity;
	}
	/**
	 * @param officeCity 파라미터를 officeCity값에 설정
	 */
	public void setOfficeCity(String officeCity) {
		this.officeCity = officeCity;
	}
	/**
	 * @return officeStreet 값 반환
	 */
	public String getOfficeStreet() {
		return officeStreet;
	}
	/**
	 * @param officeStreet 파라미터를 officeStreet값에 설정
	 */
	public void setOfficeStreet(String officeStreet) {
		this.officeStreet = officeStreet;
	}
	/**
	 * @return officeBasicAddress 값 반환
	 */
	public String getOfficeBasicAddress() {
		return officeBasicAddress;
	}
	/**
	 * @param officeBasicAddress 파라미터를 officeBasicAddress값에 설정
	 */
	public void setOfficeBasicAddress(String officeBasicAddress) {
		this.officeBasicAddress = officeBasicAddress;
	}
	/**
	 * @return officeExtAddress 값 반환
	 */
	public String getOfficeExtAddress() {
		return officeExtAddress;
	}
	/**
	 * @param officeExtAddress 파라미터를 officeExtAddress값에 설정
	 */
	public void setOfficeExtAddress(String officeExtAddress) {
		this.officeExtAddress = officeExtAddress;
	}
	/**
	 * @return description 값 반환
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description 파라미터를 description값에 설정
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	
}
