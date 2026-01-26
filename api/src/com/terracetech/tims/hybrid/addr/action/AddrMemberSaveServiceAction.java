package com.terracetech.tims.hybrid.addr.action;

import java.util.Locale;

import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.ContactService;
import com.terracetech.tims.service.tms.vo.ContactMemberVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class AddrMemberSaveServiceAction extends BaseAction {
    
    private int memberSeq;

    private int bookSeq;

    private int groupSeq;

    private String memberName = null;
    
    private String memberNameHiragana = null;

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
    
    
    private ContactService contactService = null;
    
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }
    
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        int errorCode = HybridErrorCode.SUCCESS;
        JSONObject result = new JSONObject();
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        
        ContactMemberVO contactMemberVO = new ContactMemberVO();
        contactMemberVO.setDomainSeq(mailDomainSeq);
        contactMemberVO.setUserSeq(mailUserSeq);
        contactMemberVO.setLocale(new Locale(user.get(User.USER_LOCALE)));
        contactMemberVO.setAddrbookSeq(bookSeq);
        contactMemberVO.setGroupSeq(groupSeq);
        contactMemberVO.setMemberSeq(memberSeq);
        contactMemberVO.setMemberName(memberName);
        contactMemberVO.setMemberEmail(memberEmail);
        contactMemberVO.setFirstName(firstName);
        contactMemberVO.setMiddleName(middleName);
        contactMemberVO.setLastName(lastName);
        contactMemberVO.setNickName(nickName);
        contactMemberVO.setPrivateHomepage(privateHomepage);
        contactMemberVO.setOfficeHomepage(officeHomepage);
        contactMemberVO.setBirthDay(birthDay);
        contactMemberVO.setAnniversaryDay(anniversaryDay);
        contactMemberVO.setMobileNo(mobileNo);
        contactMemberVO.setHomeTel(homeTel);
        contactMemberVO.setHomeFax(homeFax);
        contactMemberVO.setHomePostalCode(homePostalCode);
        contactMemberVO.setHomeCountry(homeCountry);
        contactMemberVO.setHomeState(homeState);
        contactMemberVO.setHomeCity(homeCity);
        contactMemberVO.setHomeStreet(homeStreet);
        contactMemberVO.setHomeBasicAddress(homeBasicAddress);
        contactMemberVO.setHomeExtAddress(homeExtAddress);
        contactMemberVO.setCompanyName(companyName);
        contactMemberVO.setDepartmentName(departmentName);
        contactMemberVO.setTitleName(titleName);
        contactMemberVO.setOfficeTel(officeTel);
        contactMemberVO.setOfficeFax(officeFax);
        contactMemberVO.setOfficePostalCode(officePostalCode);
        contactMemberVO.setOfficeCountry(officeCountry);
        contactMemberVO.setOfficeState(officeState);
        contactMemberVO.setOfficeCity(officeCity);
        contactMemberVO.setOfficeStreet(officeStreet);
        contactMemberVO.setOfficeBasicAddress(officeBasicAddress);
        contactMemberVO.setOfficeExtAddress(officeExtAddress);
        
        int resultCode = 0;
        try {
            if (memberSeq <= 0) {
                resultCode = contactService.addContact(contactMemberVO);
            } else {
                resultCode = contactService.modContact(contactMemberVO);
            }
        }catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }
        
        errorCode = (resultCode <= 0) ? HybridErrorCode.ERROR : errorCode;
        
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        String returnName = request.getParameter("addrSave");
        ResponseUtil.makeAjaxMessage(response, returnName+"("+result+")");
        
        return null;
    }

    public void setMemberSeq(int memberSeq) {
        this.memberSeq = memberSeq;
    }

    public void setBookSeq(int bookSeq) {
        this.bookSeq = bookSeq;
    }

    public void setGroupSeq(int groupSeq) {
        this.groupSeq = groupSeq;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberNameHiragana(String memberNameHiragana) {
		this.memberNameHiragana = memberNameHiragana;
	}

	public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
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
}
