package com.terracetech.tims.service.aync.data;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

public class ContactData implements ISyncData {

	private int domainSeq;
	
	private int memberSeq;
	
	private int addrbookSeq;
	
	private String groupName = null;

	private int userSeq;
	
	private int groupSeq;

	private String memberName = null;

	private String memberEmail = null;

	private String firstName = null;

	private String middleName = null;

	private String lastName = null;

	private String nickName = null;

	private String privateHomepage = null;

	private String officeHomepage = null;

	private String birthDay = null;

	private String anniversaryDay = null;

	private String mobileNo = null;

	private String homeTel = null;

	private String homeFax = null;

	private String homePostalCode = null;

	private String homeCountry = null;
	
	private String homeState = null;

	private String homeCity = null;

	private String homeStreet = null;

	private String homeBasicAddress = null;

	private String homeExtAddress = null;

	private String companyName = null;

	private String departmentName = null;
	
	private String titleName = null;

	private String officeTel = null;
	
	private String officeFax = null;

	private String officePostalCode = null;

	private String officeCountry = null;
	
	private String officeState = null;

	private String officeCity = null;

	private String officeStreet = null;

	private String officeBasicAddress = null;

	private String officeExtAddress = null;
	
	private String description = null;
	
	private String clientId = null;

	public int getMemberSeq() {
		return memberSeq;
	}

	public void setMemberSeq(int memberSeq) {
		this.memberSeq = memberSeq;
	}

	public int getAddrbookSeq() {
		return addrbookSeq;
	}

	public void setAddrbookSeq(int addrbookSeq) {
		this.addrbookSeq = addrbookSeq;
	}

	public int getDomainSeq() {
		return domainSeq;
	}

	public void setDomainSeq(int domainSeq) {
		this.domainSeq = domainSeq;
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

	public int getGroupSeq() {
		return groupSeq;
	}

	public void setGroupSeq(int groupSeq) {
		this.groupSeq = groupSeq;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPrivateHomepage() {
		return privateHomepage;
	}

	public void setPrivateHomepage(String privateHomepage) {
		this.privateHomepage = privateHomepage;
	}

	public String getOfficeHomepage() {
		return officeHomepage;
	}

	public void setOfficeHomepage(String officeHomepage) {
		this.officeHomepage = officeHomepage;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public String getAnniversaryDay() {
		return anniversaryDay;
	}

	public void setAnniversaryDay(String anniversaryDay) {
		this.anniversaryDay = anniversaryDay;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getHomeTel() {
		return homeTel;
	}

	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}

	public String getHomeFax() {
		return homeFax;
	}

	public void setHomeFax(String homeFax) {
		this.homeFax = homeFax;
	}

	public String getHomePostalCode() {
		return homePostalCode;
	}

	public void setHomePostalCode(String homePostalCode) {
		this.homePostalCode = homePostalCode;
	}

	public String getHomeCountry() {
		return homeCountry;
	}

	public void setHomeCountry(String homeCountry) {
		this.homeCountry = homeCountry;
	}

	public String getHomeState() {
		return homeState;
	}

	public void setHomeState(String homeState) {
		this.homeState = homeState;
	}

	public String getHomeCity() {
		return homeCity;
	}

	public void setHomeCity(String homeCity) {
		this.homeCity = homeCity;
	}

	public String getHomeStreet() {
		return homeStreet;
	}

	public void setHomeStreet(String homeStreet) {
		this.homeStreet = homeStreet;
	}

	public String getHomeBasicAddress() {
		return homeBasicAddress;
	}

	public void setHomeBasicAddress(String homeBasicAddress) {
		this.homeBasicAddress = homeBasicAddress;
	}

	public String getHomeExtAddress() {
		return homeExtAddress;
	}

	public void setHomeExtAddress(String homeExtAddress) {
		this.homeExtAddress = homeExtAddress;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getOfficeTel() {
		return officeTel;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}

	public String getOfficeFax() {
		return officeFax;
	}

	public void setOfficeFax(String officeFax) {
		this.officeFax = officeFax;
	}

	public String getOfficePostalCode() {
		return officePostalCode;
	}

	public void setOfficePostalCode(String officePostalCode) {
		this.officePostalCode = officePostalCode;
	}

	public String getOfficeCountry() {
		return officeCountry;
	}

	public void setOfficeCountry(String officeCountry) {
		this.officeCountry = officeCountry;
	}

	public String getOfficeState() {
		return officeState;
	}

	public void setOfficeState(String officeState) {
		this.officeState = officeState;
	}

	public String getOfficeCity() {
		return officeCity;
	}

	public void setOfficeCity(String officeCity) {
		this.officeCity = officeCity;
	}

	public String getOfficeStreet() {
		return officeStreet;
	}

	public void setOfficeStreet(String officeStreet) {
		this.officeStreet = officeStreet;
	}

	public String getOfficeBasicAddress() {
		return officeBasicAddress;
	}

	public void setOfficeBasicAddress(String officeBasicAddress) {
		this.officeBasicAddress = officeBasicAddress;
	}

	public String getOfficeExtAddress() {
		return officeExtAddress;
	}

	public void setOfficeExtAddress(String officeExtAddress) {
		this.officeExtAddress = officeExtAddress;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setDataValue(String key, String value){
		if ("ServerId".equalsIgnoreCase(key)){
			setMemberSeq(Integer.parseInt(value));
		}else if ("ClientId".equalsIgnoreCase(key)){
			setClientId(value);
		}else if ("LastName".equalsIgnoreCase(key)){
			setLastName(value);
		}else if ("FirstName".equalsIgnoreCase(key)){
			setFirstName(value);
		}else if ("FileAs".equalsIgnoreCase(key)){
			setMemberName(value);
		}else if ("CompanyName".equalsIgnoreCase(key)){
			setCompanyName(value);
		}else if ("Department".equalsIgnoreCase(key)){
			setDepartmentName(value);
		}else if ("Title".equalsIgnoreCase(key)){
			setTitleName(value);
		}else if ("BusinessPhoneNumber".equalsIgnoreCase(key)){
			setOfficeTel(value);
		}else if ("BusinessCity".equalsIgnoreCase(key)){
			setOfficeCity(value);
		}else if ("BusinessCountry".equalsIgnoreCase(key)){
			setOfficeCountry(value);
		}else if ("BusinessPostalCode".equalsIgnoreCase(key)){
			setOfficePostalCode(value);
		}else if ("BusinessState".equalsIgnoreCase(key)){
			setOfficeState(value);
		}else if ("BusinessStreet".equalsIgnoreCase(key)){
			setOfficeStreet(value);
		}else if ("BusinessFaxNumber".equalsIgnoreCase(key)){
			setOfficeFax(value);
		}else if ("HomePhoneNumber".equalsIgnoreCase(key)){
			setHomeTel(value);
		}else if ("HomeCity".equalsIgnoreCase(key)){
			setHomeCity(value);
		}else if ("HomeCountry".equalsIgnoreCase(key)){
			setHomeCountry(value);
		}else if ("HomePostalCode".equalsIgnoreCase(key)){
			setHomePostalCode(value);
		}else if ("HomeState".equalsIgnoreCase(key)){
			setHomeState(value);
		}else if ("HomeStreet".equalsIgnoreCase(key)){
			setHomeStreet(value);
		}else if ("HomeFaxNumber".equalsIgnoreCase(key)){
			setHomeFax(value);
		}else if ("MobilePhoneNumber".equalsIgnoreCase(key)){
			setMobileNo(value);
		}else if ("Email1Address".equalsIgnoreCase(key)){
			InternetAddress utils;
			try {
				utils = new InternetAddress(value);
				setMemberEmail(utils.getAddress());
			} catch (AddressException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
}
