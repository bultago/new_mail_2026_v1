package com.terracetech.tims.service.tms.vo;

import java.util.Locale;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.util.StringUtils;

public class ContactMemberVO {
	
	private int domainSeq;

	private int memberSeq;
	
	private int addrbookSeq;

	private int userSeq;
	
	private int groupSeq;
	
	private String groupName = null;

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
	
	private Locale locale;

    public int getDomainSeq() {
		return domainSeq;
	}

	public void setDomainSeq(int domainSeq) {
		this.domainSeq = domainSeq;
	}

	public int getMemberSeq() {
		return memberSeq;
	}

	public void setMemberSeq(int memberSeq) {
		this.memberSeq = memberSeq;
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

	public int getAddrbookSeq() {
		return addrbookSeq;
	}

	public void setAddrbookSeq(int addrbookSeq) {
		this.addrbookSeq = addrbookSeq;
	}

	public int getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
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

        public Locale getLocale() {
            return locale;
        }
    
        public void setLocale(Locale locale) {
            this.locale = locale;
        }
        
        @SuppressWarnings("unchecked")
        public JSONObject toJson() {
            JSONObject member = new JSONObject();
            member.put("memberSeq", getMemberSeq());
            member.put("addrbookSeq", getAddrbookSeq());
            member.put("groupSeq", getGroupSeq());
            member.put("userSeq", getUserSeq());
            member.put("groupName", StringUtils.EscapeHTMLTag(getGroupName()));
            member.put("memberName", StringUtils.EscapeHTMLTag(getMemberName()));
            member.put("memberEmail", StringUtils.EscapeHTMLTag(getMemberEmail()));
            member.put("firstName", StringUtils.EscapeHTMLTag(getFirstName()));
            member.put("middleName", StringUtils.EscapeHTMLTag(getMiddleName()));
            member.put("lastName", StringUtils.EscapeHTMLTag(getLastName()));
            member.put("nickName", StringUtils.EscapeHTMLTag(getNickName()));
            member.put("privateHomepage", StringUtils.EscapeHTMLTag(getPrivateHomepage()));
            member.put("officeHomepage", StringUtils.EscapeHTMLTag(getOfficeHomepage()));
            member.put("birthDay", StringUtils.EscapeHTMLTag(getBirthDay()));
            member.put("anniversaryDay", StringUtils.EscapeHTMLTag(getAnniversaryDay()));
            member.put("mobileNo", StringUtils.EscapeHTMLTag(getMobileNo()));
            member.put("homeTel", StringUtils.EscapeHTMLTag(getHomeTel()));
            member.put("homeFax", StringUtils.EscapeHTMLTag(getHomeFax()));
            member.put("homePostalCode", StringUtils.EscapeHTMLTag(getHomePostalCode()));
            member.put("homeCountry", StringUtils.EscapeHTMLTag(getHomeCountry()));
            member.put("homeState", StringUtils.EscapeHTMLTag(getHomeState()));
            member.put("homeCity", StringUtils.EscapeHTMLTag(getHomeCity()));
            member.put("homeStreet", StringUtils.EscapeHTMLTag(getHomeStreet()));
            member.put("homeBasicAddress", StringUtils.EscapeHTMLTag(getHomeBasicAddress()));
            member.put("homeExtAddress", StringUtils.EscapeHTMLTag(getHomeExtAddress()));
            member.put("companyName", StringUtils.EscapeHTMLTag(getCompanyName()));
            member.put("departmentName", StringUtils.EscapeHTMLTag(getDepartmentName()));
            member.put("titleName", StringUtils.EscapeHTMLTag(getTitleName()));
            member.put("officeTel", StringUtils.EscapeHTMLTag(getOfficeTel()));
            member.put("officeFax", StringUtils.EscapeHTMLTag(getOfficeFax()));
            member.put("officePostalCode", StringUtils.EscapeHTMLTag(getOfficePostalCode()));
            member.put("officeCountry", StringUtils.EscapeHTMLTag(getOfficeCountry()));
            member.put("officeState", StringUtils.EscapeHTMLTag(getOfficeState()));
            member.put("officeCity", StringUtils.EscapeHTMLTag(getOfficeCity()));
            member.put("officeStreet", StringUtils.EscapeHTMLTag(getOfficeStreet()));
            member.put("officeBasicAddress", StringUtils.EscapeHTMLTag(getOfficeBasicAddress()));
            member.put("officeExtAddress", StringUtils.EscapeHTMLTag(getOfficeExtAddress()));
            
            return member;
        }
}
