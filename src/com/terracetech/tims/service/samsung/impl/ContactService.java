package com.terracetech.tims.service.samsung.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.terracetech.tims.service.samsung.IContactService;
import com.terracetech.tims.service.samsung.vo.AttachWSVO;
import com.terracetech.tims.service.samsung.vo.ContactWSVO;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.exception.SaveFailedException;


public class ContactService implements IContactService {
	
	public Logger log = Logger.getLogger(this.getClass());
	
	private AddressBookManager manager;
	
	public void setManager(AddressBookManager manager) {
		this.manager = manager;
	}

	public int addContact(String licenseKey, ContactWSVO param) {
		int userSeq = 0;
		int domainSeq = 0;
		
		AddressBookMemberVO member = convertContactVO(param);
		member.setUserSeq(userSeq);
		
		try {
			manager.savePrivateAddressMemberWithTransactional(member, 0, domainSeq);
		} catch (SaveFailedException e) {
			log.error(e.getMessage(), e);
		}
		return SUCCESS;
	}

	public int addContactWithAttach(String licenseKey, ContactWSVO param,  AttachWSVO[] attach) {
		return addContact(licenseKey, param);
	}

	public int delContact(String licenseKey, ContactWSVO param) {
		int userSeq = 0;
		int memberSeq = Integer.parseInt(param.getBcuid());
		int[] memberSeqs = {memberSeq};
		try {
			manager.deletePrivateMember(userSeq, memberSeqs);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return SUCCESS;
	}

	public ContactWSVO getContact(String licenseKey, ContactWSVO param) {
		int userSeq = 0;
		int memberSeq = Integer.parseInt(param.getBcuid());
		
		AddressBookMemberVO member = manager.readPrivateAddressMember(userSeq, memberSeq);
		
		return convertAddressBookMemberVO(member);
	}

	public ContactWSVO[] getContactList(String licenseKey, ContactWSVO param) {
		int userSeq = 0;
		List<AddressBookMemberVO> list = manager.readPrivateMemberListByIndex(userSeq, 0, "all", 1, 90000, "name", "asc");
		List<ContactWSVO> result = new ArrayList<ContactWSVO>();
		
		for (AddressBookMemberVO addressBookMemberVO : list) {
			result.add(convertAddressBookMemberVO(addressBookMemberVO));
		}
		
		return result.toArray(new ContactWSVO[result.size()]);
	}

	public int modContact(String licenseKey, ContactWSVO param) {
		int userSeq = 0;
		
		try {
			manager.updatePrivateAddressMember(convertContactVO(param));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return SUCCESS;
	}

	public int modContactWithAttach(String licenseKey, ContactWSVO param, AttachWSVO[] attach) {
		return modContact(licenseKey, param);
	}

	public String testConnect() {
		return "success";
	}
	
	private AddressBookMemberVO convertContactVO(ContactWSVO param) {
		AddressBookMemberVO member = new AddressBookMemberVO();
		member.setMemberName(param.getName());
		member.setFirstName(param.getFname());
		member.setLastName(param.getLname());
		member.setMobileNo(param.getMobile());
		
		member.setHomeTel(param.getHtel1());
		member.setHomeFax(param.getHfax());
		member.setHomePostalCode(param.getHpost());
		
		member.setCompanyName(param.getCompany());
		member.setDepartmentName(param.getDept());
		member.setOfficePostalCode(param.getCpost());
		member.setOfficeTel(param.getCtel1());
		member.setOfficeFax(param.getCfax());
		return member;
	}
	
	private ContactWSVO convertAddressBookMemberVO(AddressBookMemberVO param) {
		ContactWSVO member = new ContactWSVO();
		member.setName(param.getMemberName());
		member.setFname(param.getFirstName());
		member.setLname(param.getLastName());
		member.setMobile(param.getMobileNo());
		
		member.setHtel1(param.getHomeTel());
		member.setHfax(param.getHomeFax());
		member.setHpost(param.getHomePostalCode());
		
		member.setCompany(param.getCompanyName());
		member.setDept(param.getDepartmentName());
		member.setCpost(param.getOfficePostalCode());
		member.setCtel1(param.getOfficeTel());
		member.setCfax(param.getOfficeFax());
		
		return member;
	}
}
