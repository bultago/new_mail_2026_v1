package com.terracetech.tims.webmail.addrbook.ibean;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class AddressBookMemberBean {
	
	private AddressBookMemberVO member;
	

	public AddressBookMemberBean(AddressBookMemberVO member) {
		super();
		this.member = member;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJson(){
		JSONObject json = new JSONObject();
		if(member ==null)
			return json;
		
		json.put("id", member.getMemberSeq());
		json.put("name", getValue(member.getMemberName()));
		json.put("firstName", getValue(member.getFirstName()));
		json.put("lastName", getValue(member.getLastName()));
		json.put("middleName", getValue(member.getMiddleName()));
		json.put("memberName", getValue(member.getMemberName()));
		json.put("nickName", getValue(member.getNickName()));
		json.put("birthDay", getValue(member.getBirthDay()));
		json.put("anniversaryDay", getValue(member.getAnniversaryDay()));
		json.put("memberEmail", getValue(member.getMemberEmail()));
		json.put("mobileNo", getValue(member.getMobileNo()));
		json.put("homePostalCode", getValue(member.getHomePostalCode()));
		json.put("homeCountry", getValue(member.getHomeCountry()));
		json.put("homeState", getValue(member.getHomeState()));
		json.put("homeCity", getValue(member.getHomeCity()));
		json.put("homeStreet", getValue(member.getHomeStreet()));
		json.put("homeExtAddress", getValue(member.getHomeExtAddress()));
		json.put("homeTel", getValue(member.getHomeTel()));
		json.put("homeFax", getValue(member.getHomeFax()));
		json.put("privateHomepage", getValue(member.getPrivateHomepage()));
		json.put("companyName", getValue(member.getCompanyName()));
		json.put("departmentName", getValue(member.getDepartmentName()));
		json.put("titleName", getValue(member.getTitleName()));
		json.put("officeCountry", getValue(member.getOfficeCountry()));
		json.put("officeState", getValue(member.getOfficeState()));
		json.put("officeCity", getValue(member.getOfficeCity()));
		json.put("officeStreet", getValue(member.getOfficeStreet()));
		json.put("officeExtAddress", getValue(member.getOfficeExtAddress()));
		json.put("officePostalCode", getValue(member.getOfficePostalCode()));
		json.put("officeTel", getValue(member.getOfficeTel()));
		json.put("officeFax", getValue(member.getOfficeFax()));
		json.put("officeHomepage", getValue(member.getOfficeHomepage()));
		
		return json;
	}
	
	private String getValue(String val){
		if(StringUtils.isEmpty(val))
			return "";
		
		return val;
	}
}
