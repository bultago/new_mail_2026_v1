package com.terracetech.tims.service.tms.impl;

import com.terracetech.tims.service.aync.data.CalendarData;
import com.terracetech.tims.service.aync.data.ContactData;
import com.terracetech.tims.service.tms.vo.ContactMemberVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;

public class Convert {

	public static ContactMemberVO convert(ContactData data){
		ContactMemberVO vo = new ContactMemberVO();
		vo.setDomainSeq(data.getDomainSeq());
		vo.setMemberSeq(data.getMemberSeq());
		vo.setUserSeq(data.getUserSeq());
		vo.setClientId(data.getClientId());
		
		vo.setAnniversaryDay(data.getAnniversaryDay());
		vo.setBirthDay(data.getBirthDay());
		vo.setCompanyName(data.getCompanyName());
		vo.setDepartmentName(data.getDepartmentName());
		vo.setDescription(data.getDescription());
		vo.setFirstName(data.getFirstName());
		vo.setGroupSeq(data.getGroupSeq());
		vo.setHomeBasicAddress(data.getHomeBasicAddress());
		vo.setHomeCity(data.getHomeCity());
		vo.setHomeCountry(data.getHomeCity());
		vo.setHomeExtAddress(data.getHomeExtAddress());
		vo.setHomeFax(data.getHomeFax());
		vo.setHomePostalCode(data.getHomePostalCode());
		vo.setHomeState(data.getHomeState());
		vo.setHomeStreet(data.getHomeStreet());
		vo.setHomeTel(data.getHomeTel());
		vo.setLastName(data.getLastName());
		vo.setMemberEmail(data.getMemberEmail());
		vo.setMemberName(data.getMemberName());
		vo.setMemberSeq(data.getMemberSeq());
		vo.setMiddleName(data.getMiddleName());
		vo.setMobileNo(data.getMobileNo());
		vo.setNickName(data.getNickName());
		vo.setOfficeBasicAddress(data.getOfficeBasicAddress());
		vo.setOfficeCity(data.getOfficeCity());
		vo.setOfficeCountry(data.getOfficeCountry());
		vo.setOfficeExtAddress(data.getOfficeExtAddress());
		vo.setOfficeFax(data.getOfficeFax());
		vo.setOfficeHomepage(data.getOfficeHomepage());
		vo.setOfficePostalCode(data.getOfficePostalCode());
		vo.setOfficeState(data.getOfficeState());
		vo.setOfficeStreet(data.getOfficeStreet());
		vo.setOfficeTel(data.getOfficeTel());
		vo.setPrivateHomepage(data.getPrivateHomepage());
		vo.setTitleName(data.getTitleName());
		
		
		return vo;
	}
	
	/**
	 * ContactMemberVO is converted to  AddressBookMemberVO
	 * @param member
	 * @return
	 */
	public static AddressBookMemberVO converVO(ContactMemberVO member){
		AddressBookMemberVO vo = new AddressBookMemberVO();
		
		vo.setUserSeq(member.getUserSeq());
		vo.setAddrbookSeq(member.getAddrbookSeq());
		
		vo.setAnniversaryDay(member.getAnniversaryDay());
		vo.setBirthDay(member.getBirthDay());
		vo.setCompanyName(member.getCompanyName());
		vo.setDepartmentName(member.getDepartmentName());
		vo.setDescription(member.getDescription());
		vo.setFirstName(member.getFirstName());
		vo.setGroupSeq(member.getGroupSeq());
		vo.setHomeBasicAddress(member.getHomeBasicAddress());
		vo.setHomeCity(member.getHomeCity());
		vo.setHomeCountry(member.getHomeCountry());
		vo.setHomeExtAddress(member.getHomeExtAddress());
		vo.setHomeFax(member.getHomeFax());
		vo.setHomePostalCode(member.getHomePostalCode());
		vo.setHomeState(member.getHomeState());
		vo.setHomeStreet(member.getHomeStreet());
		vo.setHomeTel(member.getHomeTel());
		vo.setLastName(member.getLastName());
		vo.setMemberEmail(member.getMemberEmail());
		vo.setMemberName(member.getMemberName());
		vo.setMemberSeq(member.getMemberSeq());
		vo.setMiddleName(member.getMiddleName());
		vo.setMobileNo(member.getMobileNo());
		vo.setNickName(member.getNickName());
		vo.setOfficeBasicAddress(member.getOfficeBasicAddress());
		vo.setOfficeCity(member.getOfficeCity());
		vo.setOfficeCountry(member.getOfficeCountry());
		vo.setOfficeExtAddress(member.getOfficeExtAddress());
		vo.setOfficeFax(member.getOfficeFax());
		vo.setOfficeHomepage(member.getOfficeHomepage());
		vo.setOfficePostalCode(member.getOfficePostalCode());
		vo.setOfficeState(member.getOfficeState());
		vo.setOfficeStreet(member.getOfficeStreet());
		vo.setOfficeTel(member.getOfficeTel());
		vo.setPrivateHomepage(member.getPrivateHomepage());
		vo.setTitleName(member.getTitleName());
		
		vo.setClientId(member.getClientId());
		
		return vo;
	}
	
	/**
	 * AddressBookMemberVO is converted to  ContactMemberVO
	 * @param member
	 * @return
	 */
	public static ContactMemberVO converVO(AddressBookMemberVO member){
		ContactMemberVO vo = new ContactMemberVO();
		
		vo.setUserSeq(member.getUserSeq());
		vo.setAddrbookSeq(member.getAddrbookSeq());
		
		vo.setAnniversaryDay(member.getAnniversaryDay());
		vo.setBirthDay(member.getBirthDay());
		vo.setCompanyName(member.getCompanyName());
		vo.setDepartmentName(member.getDepartmentName());
		vo.setDescription(member.getDescription());
		vo.setFirstName(member.getFirstName());
		vo.setGroupSeq(member.getGroupSeq());
		vo.setHomeBasicAddress(member.getHomeBasicAddress());
		vo.setHomeCity(member.getHomeCity());
		vo.setHomeCountry(member.getHomeCountry());
		vo.setHomeExtAddress(member.getHomeExtAddress());
		vo.setHomeFax(member.getHomeFax());
		vo.setHomePostalCode(member.getHomePostalCode());
		vo.setHomeState(member.getHomeState());
		vo.setHomeStreet(member.getHomeStreet());
		vo.setHomeTel(member.getHomeTel());
		vo.setLastName(member.getLastName());
		vo.setMemberEmail(member.getMemberEmail());
		vo.setMemberName(member.getMemberName());
		vo.setMemberSeq(member.getMemberSeq());
		vo.setMiddleName(member.getMiddleName());
		vo.setMobileNo(member.getMobileNo());
		vo.setNickName(member.getNickName());
		vo.setOfficeBasicAddress(member.getOfficeBasicAddress());
		vo.setOfficeCity(member.getOfficeCity());
		vo.setOfficeCountry(member.getOfficeCountry());
		vo.setOfficeExtAddress(member.getOfficeExtAddress());
		vo.setOfficeFax(member.getOfficeFax());
		vo.setOfficeHomepage(member.getOfficeHomepage());
		vo.setOfficePostalCode(member.getOfficePostalCode());
		vo.setOfficeState(member.getOfficeState());
		vo.setOfficeStreet(member.getOfficeStreet());
		vo.setOfficeTel(member.getOfficeTel());
		vo.setPrivateHomepage(member.getPrivateHomepage());
		vo.setTitleName(member.getTitleName());
		
		return vo;
	}
	
	public static SchedulerDataVO convert(CalendarData data){
		data.calculateRepeatTeram();
		
		SchedulerDataVO vo = new SchedulerDataVO();
		vo.setMailUserSeq(data.getMailUserSeq());
		vo.setSchedulerId(data.getSchedulerId());
		vo.setStartDate(data.getStartDate());
		vo.setEndDate(data.getEndDate());
		vo.setTitle(data.getTitle());
		vo.setLocation(data.getLocation());
		vo.setContent(data.getContent());
		vo.setAllDay(data.getAllDay());
		vo.setHoliday(data.getHoliday());
		vo.setRepeatFlag(data.getRepeatFlag());
		vo.setRepeatTerm(data.getRepeatTerm());
		vo.setRepeatEndDate(data.getRepeatEndDate());
		vo.setCreateTime(data.getCreateTime());
		vo.setModifyTime(data.getModifyTime());
		vo.setCheckShare(data.getCheckShare());
		vo.setShareName(data.getShareName());
		
		return vo;
	}
}