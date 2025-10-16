/**
 * UserInfoVO.java 2008. 12. 3.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.vo;

import java.io.Serializable;

/**
 * <p><strong>UserInfoVO.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class MailUserInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int mailUserSeq, creatorSeq ;
	
	private String agreeTime, alternativeEmail, applyTime, bigAttachQuota, 
		classCode, companyName, createTime, deferTime, departmentName, 
		description, empno, firstName, birthday, homeBasicAddress, homeCity, homeCountry, 
		homeExtAddress, homeFax, homePostalCode, homeState, homeStreet, homeTel, 
		imapLoginTime, lastLoginTime, lastName, middleName, mobileNo, modifyTime, 
		officeBasicAddress, officeCity, officeCountry, officeExtAddress, officeFax, 
		officePostalCode, officeState, officeStreet, officeTel, passAnswer, 
		passChgTime, passQuestion, passQuestionCode, popLoginTime, privateHomepage, 
		registerStatus, ssn, userLanguage, userName, webFolderQuota, webmailLoginTime,
		massSend,userDN;

	public int getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}

	public int getCreatorSeq() {
		return creatorSeq;
	}

	public void setCreatorSeq(int creatorSeq) {
		this.creatorSeq = creatorSeq;
	}

	public String getAgreeTime() {
		return agreeTime;
	}

	public void setAgreeTime(String agreeTime) {
		this.agreeTime = agreeTime;
	}

	public String getAlternativeEmail() {
		return alternativeEmail;
	}

	public void setAlternativeEmail(String alternativeEmail) {
		this.alternativeEmail = alternativeEmail;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getBigAttachQuota() {
		return bigAttachQuota;
	}

	public void setBigAttachQuota(String bigAttachQuota) {
		this.bigAttachQuota = bigAttachQuota;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDeferTime() {
		return deferTime;
	}

	public void setDeferTime(String deferTime) {
		this.deferTime = deferTime;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmpno() {
		return empno;
	}

	public void setEmpno(String empno) {
		this.empno = empno;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getHomeBasicAddress() {
		return homeBasicAddress;
	}

	public void setHomeBasicAddress(String homeBasicAddress) {
		this.homeBasicAddress = homeBasicAddress;
	}

	public String getHomeCity() {
		return homeCity;
	}

	public void setHomeCity(String homeCity) {
		this.homeCity = homeCity;
	}

	public String getHomeCountry() {
		return homeCountry;
	}

	public void setHomeCountry(String homeCountry) {
		this.homeCountry = homeCountry;
	}

	public String getHomeExtAddress() {
		return homeExtAddress;
	}

	public void setHomeExtAddress(String homeExtAddress) {
		this.homeExtAddress = homeExtAddress;
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

	public String getHomeState() {
		return homeState;
	}

	public void setHomeState(String homeState) {
		this.homeState = homeState;
	}

	public String getHomeStreet() {
		return homeStreet;
	}

	public void setHomeStreet(String homeStreet) {
		this.homeStreet = homeStreet;
	}

	public String getHomeTel() {
		return homeTel;
	}

	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}

	public String getImapLoginTime() {
		return imapLoginTime;
	}

	public void setImapLoginTime(String imapLoginTime) {
		this.imapLoginTime = imapLoginTime;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getOfficeBasicAddress() {
		return officeBasicAddress;
	}

	public void setOfficeBasicAddress(String officeBasicAddress) {
		this.officeBasicAddress = officeBasicAddress;
	}

	public String getOfficeCity() {
		return officeCity;
	}

	public void setOfficeCity(String officeCity) {
		this.officeCity = officeCity;
	}

	public String getOfficeCountry() {
		return officeCountry;
	}

	public void setOfficeCountry(String officeCountry) {
		this.officeCountry = officeCountry;
	}

	public String getOfficeExtAddress() {
		return officeExtAddress;
	}

	public void setOfficeExtAddress(String officeExtAddress) {
		this.officeExtAddress = officeExtAddress;
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

	public String getOfficeState() {
		return officeState;
	}

	public void setOfficeState(String officeState) {
		this.officeState = officeState;
	}

	public String getOfficeStreet() {
		return officeStreet;
	}

	public void setOfficeStreet(String officeStreet) {
		this.officeStreet = officeStreet;
	}

	public String getOfficeTel() {
		return officeTel;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}

	public String getPassAnswer() {
		return passAnswer;
	}

	public void setPassAnswer(String passAnswer) {
		this.passAnswer = passAnswer;
	}

	public String getPassChgTime() {
		return passChgTime;
	}

	public void setPassChgTime(String passChgTime) {
		this.passChgTime = passChgTime;
	}

	public String getPassQuestion() {
		return passQuestion;
	}

	public void setPassQuestion(String passQuestion) {
		this.passQuestion = passQuestion;
	}

	public String getPassQuestionCode() {
		return passQuestionCode;
	}

	public void setPassQuestionCode(String passQuestionCode) {
		this.passQuestionCode = passQuestionCode;
	}

	public String getPopLoginTime() {
		return popLoginTime;
	}

	public void setPopLoginTime(String popLoginTime) {
		this.popLoginTime = popLoginTime;
	}

	public String getPrivateHomepage() {
		return privateHomepage;
	}

	public void setPrivateHomepage(String privateHomepage) {
		this.privateHomepage = privateHomepage;
	}

	public String getRegisterStatus() {
		return registerStatus;
	}

	public void setRegisterStatus(String registerStatus) {
		this.registerStatus = registerStatus;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getUserLanguage() {
		return userLanguage;
	}

	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWebFolderQuota() {
		return webFolderQuota;
	}

	public void setWebFolderQuota(String webFolderQuota) {
		this.webFolderQuota = webFolderQuota;
	}

	public String getWebmailLoginTime() {
		return webmailLoginTime;
	}

	public void setWebmailLoginTime(String webmailLoginTime) {
		this.webmailLoginTime = webmailLoginTime;
	}

	public String getMassSend() {
		return massSend;
	}

	public void setMassSend(String massSend) {
		this.massSend = massSend;
	}

	public String getUserDN() {
		return userDN;
	}

	public void setUserDN(String userDN) {
		this.userDN = userDN;
	}
	
	
}