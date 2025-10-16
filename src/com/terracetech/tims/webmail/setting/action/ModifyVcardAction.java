package com.terracetech.tims.webmail.setting.action;

import java.util.Locale;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.VCardManager;
import com.terracetech.tims.webmail.setting.vo.UserInfoVO;
import com.terracetech.tims.webmail.setting.vo.VCardVO;

public class ModifyVcardAction extends BaseAction {

	private VCardManager vcardManager = null;
	
	private String memberEmail = null;
	private String memberName = null;
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

	public void setVcardManager(VCardManager vcardManager) {
		this.vcardManager = vcardManager;
	}
	
	public String execute() throws Exception {
		I18nResources resource = getMessageResource("setting");
		
		User user = UserAuthManager.getUser(request);
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String msg = null;
		try {
			VCardVO vcardVo = changeVcardVo();
			vcardVo.setUserSeq(mailUserSeq);
			
			vcardManager.modifyVcardInfo(vcardVo);
			msg = resource.getMessage("update.ok");
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("update.fail");
		}
		request.setAttribute("msg", msg);
		return "success";
	}
	
	private VCardVO changeVcardVo() {
		VCardVO vcardVo = new VCardVO();
		vcardVo.setMemberEmail(memberEmail);
		vcardVo.setMemberName(memberName);
		vcardVo.setFirstName(firstName);
		vcardVo.setMiddleName(middleName);
		vcardVo.setLastName(lastName);
		vcardVo.setNickName(nickName);
		vcardVo.setBirthDay(birthDay);
		vcardVo.setAnniversaryDay(anniversaryDay);
		vcardVo.setPrivateHomepage(privateHomepage);
		vcardVo.setMobileNo(mobileNo);
		vcardVo.setHomeTel(homeTel);
		vcardVo.setHomeFax(homeFax);
		vcardVo.setHomePostalCode(homePostalCode);
		vcardVo.setHomeCountry(homeCountry);
		vcardVo.setHomeState(homeState);
		vcardVo.setHomeCity(homeCity);
		vcardVo.setHomeStreet(homeStreet);
		vcardVo.setHomeBasicAddress(homeBasicAddress);
		vcardVo.setHomeExtAddress(homeExtAddress);
		vcardVo.setCompanyName(companyName);
		vcardVo.setDepartmentName(departmentName);
		vcardVo.setTitleName(titleName);
		vcardVo.setOfficeTel(officeTel);
		vcardVo.setOfficeFax(officeFax);
		vcardVo.setOfficePostalCode(officePostalCode);
		vcardVo.setOfficeCountry(officeCountry);
		vcardVo.setOfficeState(officeState);
		vcardVo.setOfficeCity(officeCity);
		vcardVo.setOfficeStreet(officeStreet);
		vcardVo.setOfficeBasicAddress(officeBasicAddress);
		vcardVo.setOfficeExtAddress(officeExtAddress);
		vcardVo.setOfficeHomepage(officeHomepage);
		vcardVo.setDescription(description);
		
		return vcardVo;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setPrivateHomepage(String privateHomepage) {
		this.privateHomepage = privateHomepage;
	}

	public void setOfficeHomepage(String officeHomepage) {
		this.officeHomepage = officeHomepage;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public void setAnniversaryDay(String anniversaryDay) {
		this.anniversaryDay = anniversaryDay;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}

	public void setHomeFax(String homeFax) {
		this.homeFax = homeFax;
	}

	public void setHomePostalCode(String homePostalCode) {
		this.homePostalCode = homePostalCode;
	}

	public void setHomeCountry(String homeCountry) {
		this.homeCountry = homeCountry;
	}

	public void setHomeState(String homeState) {
		this.homeState = homeState;
	}

	public void setHomeCity(String homeCity) {
		this.homeCity = homeCity;
	}

	public void setHomeStreet(String homeStreet) {
		this.homeStreet = homeStreet;
	}

	public void setHomeBasicAddress(String homeBasicAddress) {
		this.homeBasicAddress = homeBasicAddress;
	}

	public void setHomeExtAddress(String homeExtAddress) {
		this.homeExtAddress = homeExtAddress;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}

	public void setOfficeFax(String officeFax) {
		this.officeFax = officeFax;
	}

	public void setOfficePostalCode(String officePostalCode) {
		this.officePostalCode = officePostalCode;
	}

	public void setOfficeCountry(String officeCountry) {
		this.officeCountry = officeCountry;
	}

	public void setOfficeState(String officeState) {
		this.officeState = officeState;
	}

	public void setOfficeCity(String officeCity) {
		this.officeCity = officeCity;
	}

	public void setOfficeStreet(String officeStreet) {
		this.officeStreet = officeStreet;
	}

	public void setOfficeBasicAddress(String officeBasicAddress) {
		this.officeBasicAddress = officeBasicAddress;
	}

	public void setOfficeExtAddress(String officeExtAddress) {
		this.officeExtAddress = officeExtAddress;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
